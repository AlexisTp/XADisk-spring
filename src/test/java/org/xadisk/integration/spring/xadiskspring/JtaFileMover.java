package org.xadisk.integration.spring.xadiskspring;

import java.io.File;

import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.xadisk.bridge.proxies.interfaces.XASession;
import org.xadisk.filesystem.exceptions.FileAlreadyExistsException;
import org.xadisk.filesystem.exceptions.FileNotExistsException;
import org.xadisk.filesystem.exceptions.FileUnderUseException;
import org.xadisk.filesystem.exceptions.InsufficientPermissionOnFileException;
import org.xadisk.filesystem.exceptions.LockingFailedException;
import org.xadisk.filesystem.exceptions.NoTransactionAssociatedException;
import org.xadisk.integration.spring.xadiskspring.XADiskSessionFactory;

public class JtaFileMover implements FileMover {

	private XADiskSessionFactory sessionFactory;

	public JtaFileMover(XADiskSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void move(File a, File b, boolean dryRun)
			throws FileAlreadyExistsException, FileNotExistsException,
			FileUnderUseException, InsufficientPermissionOnFileException,
			LockingFailedException, NoTransactionAssociatedException,
			InterruptedException, IllegalStateException, RollbackException,
			SystemException {
		XASession session = sessionFactory.getCurrentSession();
		session.moveFile(a, b);		
		if (dryRun) {
			throw new RuntimeException();
		}
	}

}
