package org.xadisk.integration.spring.xadisk_spring.database;

import java.io.File;

import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.xadisk.filesystem.exceptions.FileAlreadyExistsException;
import org.xadisk.filesystem.exceptions.FileNotExistsException;
import org.xadisk.filesystem.exceptions.FileUnderUseException;
import org.xadisk.filesystem.exceptions.InsufficientPermissionOnFileException;
import org.xadisk.filesystem.exceptions.LockingFailedException;
import org.xadisk.filesystem.exceptions.NoTransactionAssociatedException;
import org.xadisk.integration.spring.xadisk_spring.FileMover;

@Component
public class TwoResourceWorker {

	@Autowired
	private CustomEntityDao customEntityDao;

	@Autowired
	private FileMover fileMover;
	
	@Transactional
	public void testCommit(File a, File b, CustomEntity ce, boolean dryRun) throws FileAlreadyExistsException, FileNotExistsException, FileUnderUseException, InsufficientPermissionOnFileException, LockingFailedException, NoTransactionAssociatedException, IllegalStateException, InterruptedException, RollbackException, SystemException{
		customEntityDao.save(ce);
		fileMover.move(a, b, dryRun);
	}
}
