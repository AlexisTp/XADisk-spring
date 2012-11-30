package org.xadisk.integration.spring.xadisk_spring;

import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.xadisk.bridge.proxies.interfaces.XAFileSystem;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;

@Configuration
@EnableTransactionManagement
public class BaseContext {

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

	@Bean(destroyMethod="shutdown")
	XAFileSystem xaFileSystem() throws InterruptedException {
		return XAHolder.xafs;
	}
	
	@Bean
	XADiskSessionFactory XADiskSessionFactory(){
		try {
			return  new XADiskSessionFactory(transactionManager(), xaFileSystem());
		} catch (InterruptedException e) {
			return null;
		}
	}
	
	@Bean
	FileMover fileMover(){
		return new JtaFileMover(XADiskSessionFactory());
	}
}