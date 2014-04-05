package org.xadisk.integration.spring.xadiskspring;

import org.xadisk.bridge.proxies.interfaces.XAFileSystem;
import org.xadisk.bridge.proxies.interfaces.XAFileSystemProxy;
import org.xadisk.filesystem.standalone.StandaloneFileSystemConfiguration;

/**
 * Can only boot an XADisk per jvm ?
 * It was disturbing so I removed it.
 * 
 * @author TORRESA
 * 
 */
public class XAHolder {

	public static XAFileSystem xafs;

	static {
		StandaloneFileSystemConfiguration configuration = new StandaloneFileSystemConfiguration(
				"./target/xadisk", "id-1");

		configuration.setSynchronizeDirectoryChanges(false);

		xafs = XAFileSystemProxy.bootNativeXAFileSystem(configuration);

		System.out.println("\nBooting XADisk...\n");

		try {
			xafs.waitForBootup(100000);
		} catch (InterruptedException e) {
			
		}

	}

}
