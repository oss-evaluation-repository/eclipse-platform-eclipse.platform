package org.eclipse.update.core;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import java.io.InputStream;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.*;

/**
 * A PluginContainer manages plug-in archives.
 * A feature and a site are plugin container as they both
 * logically or physically manage the archives that contain
 * plug-ins.
 */
//FIXME: javadoc
// VK: why are we keeping this interface just for the 2 methods?
//     is there ever a case where IPluginContainer is a useful
//     general abstraction?

public interface IPluginContainer extends IAdaptable {
	/**
	 * Returns an array of plug-ins managed by the container
	 * 
	 * @return the accessible plug-ins. Returns an empty array
	 * if there are no plug-ins.
	 */

	IPluginEntry [] getPluginEntries()  ;
	
	/**
	 * Returns the number of managed plug-ins
	 * @return the number of plug-ins
	 */

	int getPluginEntryCount() ;
	
	/**
	 * Size of the archives in Kilo-Bytes
	 * @return the size of the archive to be downloaded
	 * @since 2.0 
	 * @deprecated
	 */

	long getDownloadSize(IPluginEntry entry) ;
	
	/**
	 * Size of the plug-in in KiloBytes
	 * @return the size of the plug-in when installed
	 * @since 2.0 
	 * @deprecated 
	 */

	long getInstallSize(IPluginEntry entry) ;

	/**
	 * Adds a pluginEntry to the list of managed pluginEntry
	 * 
	 * @param entry the plugin entry
	 * @since 2.0 
	 * @deprecated
	 */
	
	void addPluginEntry(IPluginEntry pluginEntry);
	

		
	
	
}

