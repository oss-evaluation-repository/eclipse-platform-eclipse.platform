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

import org.eclipse.core.runtime.CoreException;

/**
 * Verifier. This interface abstracts the archive verification step
 * performed by specific feature implementations. The actual details
 * of the verification are the responsibility of the concrete implementation.
 * <p>
 * Clients may implement this interface.
 * </p>
 * @see org.eclipse.update.core.IVerificationResult
 * @see org.eclipse.update.core.IFeatureContentProvider#getVerifier()
 * @since 2.0
 */
public interface IVerifier {

	/**
	 * Perform verification of the specified archive.
	 * 
	 * @param feature feature containing this archive
	 * @param reference actual archive reference
	 * @param isFeatureVerification <code>true</code> indicates the specified
	 * reference should be considered as part of the feature description
	 * information (ie. verifying the overall feature), 
	 * <code>false</code> indicates the specified reference is a plug-in
	 * or a non-plug-in archive file (ie. verifying a component of the
	 * feature)
	 * @param monitor progress monitor, can be <code>null</code>
	 * @return verification result
	 * @exception CoreException
	 * @since 2.0
	 */
	public IVerificationResult verify(
		IFeature feature,
		ContentReference reference,
		boolean isFeatureVerification,
		InstallMonitor monitor)
		throws CoreException;
		
	/**
	 * Sets the parent verifier.
	 * 
	 * The parent verifier can only be set once by the parent feature.
	 * It may used for different verification strategies. 
	 * (for instance, you may decide that both the parent and current verifier
	 * must sucessfully verify the content reference, or that only one of them must verify)
	 * @param parentVerifier the parent verifier.
	 */
	public void setParent(IVerifier parentVerifier);
	
	/**
	 * Returns the parent verifier
	 * 
	 * @return the parent verifier
	 * @since 2.0
	 */
	public IVerifier getParent();
}
