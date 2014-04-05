package org.xadisk.integration.spring.xadiskspring.simpleMoves;

import java.io.File;
import java.io.IOException;

import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xadisk.bridge.proxies.interfaces.XAFileSystem;
import org.xadisk.filesystem.exceptions.FileAlreadyExistsException;
import org.xadisk.filesystem.exceptions.FileNotExistsException;
import org.xadisk.filesystem.exceptions.FileUnderUseException;
import org.xadisk.filesystem.exceptions.InsufficientPermissionOnFileException;
import org.xadisk.filesystem.exceptions.LockingFailedException;
import org.xadisk.filesystem.exceptions.NoTransactionAssociatedException;
import org.xadisk.integration.spring.xadiskspring.BaseContext;
import org.xadisk.integration.spring.xadiskspring.FileMover;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BaseContext.class })
public class FileMoverTest {

	@Autowired
	FileMover fileMover;

	@Autowired
	private XAFileSystem XAFileSystem;

	File directory;

	@Before
	public void init() throws IOException {
		directory = File.createTempFile("temp",
				Long.toString(System.nanoTime()));
		directory.delete();
		directory.mkdir();
	}

	@After
	public void cleanUp() throws IOException {
		FileUtils.forceDelete(directory);
	}

	@Test
	public void testMove() throws IOException, FileAlreadyExistsException,
			FileNotExistsException, FileUnderUseException,
			InsufficientPermissionOnFileException, LockingFailedException,
			NoTransactionAssociatedException, IllegalStateException,
			InterruptedException, RollbackException, SystemException {
		File a = new File(directory.getAbsolutePath() + File.separator
				+ "a.txt");
		a.createNewFile();
		File b = new File(directory.getAbsolutePath() + File.separator
				+ "b.txt");
		fileMover.move(a, b, false);

		Assert.assertTrue(!a.exists());
		Assert.assertTrue(b.exists());
	}

	@Test
	public void testMoveRollBack() throws IOException,
			FileAlreadyExistsException, FileNotExistsException,
			FileUnderUseException, InsufficientPermissionOnFileException,
			LockingFailedException, NoTransactionAssociatedException,
			IllegalStateException, InterruptedException, RollbackException,
			SystemException {
		File a = new File(directory.getAbsolutePath() + File.separator
				+ "a.txt");
		a.createNewFile();
		File b = new File(directory.getAbsolutePath() + File.separator
				+ "b.txt");
		try {
			fileMover.move(a, b, true);
		} catch (RuntimeException e) {

		}

		Assert.assertTrue(a.exists());
		Assert.assertTrue(!b.exists());
	}
}
