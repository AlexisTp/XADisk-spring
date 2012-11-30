package org.xadisk.integration.spring.xadisk_spring.database;

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
import org.xadisk.filesystem.exceptions.FileAlreadyExistsException;
import org.xadisk.filesystem.exceptions.FileNotExistsException;
import org.xadisk.filesystem.exceptions.FileUnderUseException;
import org.xadisk.filesystem.exceptions.InsufficientPermissionOnFileException;
import org.xadisk.filesystem.exceptions.LockingFailedException;
import org.xadisk.filesystem.exceptions.NoTransactionAssociatedException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Context.class })
public class TwoRessourceTest {

	@Autowired
	private TwoResourceWorker twoResourceWorker;

	@Autowired
	private CustomEntityDao customEntityDao;
	
	File directory;
	
	CustomEntity ce = new CustomEntity(1l, "test");

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
		if (customEntityDao.get(1l) != null){
			customEntityDao.delete(ce);
		}
	}
	
	@Test
	public void testTwoRessourceCommit() throws IOException,
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

		twoResourceWorker.testCommit(a, b, ce, false);

		Assert.assertTrue(!a.exists());
		Assert.assertTrue(b.exists());
		Assert.assertNotNull(customEntityDao.get(1l));

	}
	
	@Test
	public void testTwoRessourceCommit2() throws IOException,
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

		twoResourceWorker.testCommit(a, b, ce, false);

		Assert.assertTrue(!a.exists());
		Assert.assertTrue(b.exists());
		Assert.assertNotNull(customEntityDao.get(1l));

	}

	@Test
	public void testTwoRessourceRollBack1() throws IOException,
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

		try{
			twoResourceWorker.testCommit(a, b, ce, true);	
		}
		catch (Exception e) {
			
		}

		Assert.assertTrue(a.exists());
		Assert.assertTrue(!b.exists());
		Assert.assertNull(customEntityDao.get(1l));

	}
	
	@Test
	public void testTwoRessourceRollBack2() throws IOException,
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

		try{
			twoResourceWorker.testCommit(a, b, ce, true);	
		}
		catch (Exception e) {
			
		}

		Assert.assertTrue(a.exists());
		Assert.assertTrue(!b.exists());
		Assert.assertNull(customEntityDao.get(1l));

	}

}
