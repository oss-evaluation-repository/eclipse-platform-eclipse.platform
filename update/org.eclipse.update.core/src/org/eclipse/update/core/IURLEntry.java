/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.update.core;

import java.net.URL;

import org.eclipse.core.runtime.IAdaptable;

/**
 * URL entry is an annotated URL object. It allows descriptive text to be
 * associated with a URL. When used as description object, the annotation
 * typically corresponds to short descriptive text, with the URL reference
 * pointing to full browsable description.
 * <p>
 * Clients may implement this interface. However, in most cases clients should 
 * directly instantiate or subclass the provided implementation of this 
 * interface.
 * </p>
 * @see org.eclipse.update.core.URLEntry
 * @since 2.0
 */
public interface IURLEntry extends IAdaptable {

	public static final int UPDATE_SITE = 0;
	public static final int WEB_SITE = 1;	

	/** 
	 * Returns the URL annotation or <code>null</code> if none
	 * 
	 * @return url annotation or <code>null</code> if none
	 * @since 2.0 
	 */
	public String getAnnotation();

	/**
	 * Returns the actual URL.
	 * 
	 * @return url.
	 * @since 2.0 
	 */
	public URL getURL();
	
	/**
	 * Returns the type of the URLEntry
	 * 
	 * @see UPDATE_SITE
	 * @see WEB_SITE
	 * @return type
	 * @since 2.0 
	 */
	public int getType();	
}
