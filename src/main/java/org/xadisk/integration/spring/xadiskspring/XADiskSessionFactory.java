package org.xadisk.integration.spring.xadiskspring;

import java.util.Hashtable;
import java.util.Map;

import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.xadisk.bridge.proxies.interfaces.XAFileSystem;
import org.xadisk.bridge.proxies.interfaces.XASession;

/**
 * Return current XASession from Transaction
 * 
 * @author AlexisTp
 * 
 */
public class XADiskSessionFactory {

	private TransactionManager txManager;

	private transient Map<Transaction, XASession> currentSessionMap = new Hashtable<Transaction, XASession>();

	private XAFileSystem xaFileSystem;

	public XADiskSessionFactory(TransactionManager txManager,
			XAFileSystem xaFileSystem) {
		this.txManager = txManager;
		this.xaFileSystem = xaFileSystem;
	}

	public XASession getCurrentSession() throws IllegalStateException,
			RollbackException, SystemException {
		Transaction txn = txManager.getTransaction();
		if (txn == null) {
			throw new RuntimeException("There is not Transaction");
		}
		if (!isInProgress(txn.getStatus())) {
			// We could register the session against the transaction even though
			// it is
			// not started, but we'd have no guarantee of ever getting the map
			// entries cleaned up (aside from spawning threads).
			throw new RuntimeException("Current transaction is not in progress");
		}
		XASession session = currentSessionMap.get(txn);
		if (session == null) {
			session = buildOrObtainSession();
			try {
				txn.registerSynchronization(buildCleanupSynch(txn));
			} catch (Throwable t) {
				throw new RuntimeException(
						"Unable to register cleanup Synchronization with TransactionManager");
			}

			currentSessionMap.put(txn, session);
			txManager.getTransaction().enlistResource(session.getXAResource());
		}

		return session;
	}

	private boolean isInProgress(int status) {

		return status == Status.STATUS_ACTIVE
				|| status == Status.STATUS_MARKED_ROLLBACK;

	}

	protected XASession buildOrObtainSession() {
		return xaFileSystem.createSessionForXATransaction();
	}

	private CleanupSynch buildCleanupSynch(Object transactionIdentifier) {
		return new CleanupSynch(transactionIdentifier, this);
	}

	/**
	 * JTA transaction synch used for cleanup of the internal session map.
	 */
	protected static class CleanupSynch implements Synchronization {
		private Object transactionIdentifier;
		private XADiskSessionFactory context;

		public CleanupSynch(Object transactionIdentifier,
				XADiskSessionFactory context) {
			this.transactionIdentifier = transactionIdentifier;
			this.context = context;
		}

		public void beforeCompletion() {
		}

		public void afterCompletion(int i) {
			context.currentSessionMap.remove(transactionIdentifier);
		}
	}
}
