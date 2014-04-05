package org.xadisk.integration.spring.xadiskspring;

import java.io.File;

import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.xadisk.filesystem.exceptions.FileAlreadyExistsException;
import org.xadisk.filesystem.exceptions.FileNotExistsException;
import org.xadisk.filesystem.exceptions.FileUnderUseException;
import org.xadisk.filesystem.exceptions.InsufficientPermissionOnFileException;
import org.xadisk.filesystem.exceptions.LockingFailedException;
import org.xadisk.filesystem.exceptions.NoTransactionAssociatedException;

public interface FileMover {

	abstract void move(File a, File b, boolean dryRun)
			throws FileAlreadyExistsException, FileNotExistsException,
			FileUnderUseException, InsufficientPermissionOnFileException,
			LockingFailedException, NoTransactionAssociatedException,
			InterruptedException, IllegalStateException, RollbackException,
			SystemException;

}