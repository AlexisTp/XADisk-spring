package org.xadisk.integration.spring.xadisk_spring;

import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.xadisk.bridge.proxies.interfaces.XAFileSystem;
import org.xadisk.bridge.proxies.interfaces.XAFileSystemProxy;
import org.xadisk.filesystem.standalone.StandaloneFileSystemConfiguration;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "org.xadisk.integration.spring.xadisk_spring" })
public class Context {

	@Bean
	TransactionManager transactionManager() {
		UserTransactionManager tm = new UserTransactionManager();
		tm.setForceShutdown(false);
		return tm;
	}

	@Bean
	PlatformTransactionManager plateformTransactionManager()
			throws SystemException {

		UserTransactionImp userTxImp = new UserTransactionImp();
		userTxImp.setTransactionTimeout(300);
		return new JtaTransactionManager(userTxImp, transactionManager());
	}

	@Bean
	XAFileSystem xaFileSystem() throws InterruptedException {
		StandaloneFileSystemConfiguration configuration = new StandaloneFileSystemConfiguration(
				"C:\\xadisk", "id-1");

		configuration.setSynchronizeDirectoryChanges(false);
		
		XAFileSystem xafs = XAFileSystemProxy
				.bootNativeXAFileSystem(configuration);

		System.out.println("\nBooting XADisk...\n");

		xafs.waitForBootup(100000);

		return xafs;
	}
}