/*******************************************************************************
 * Copyright (c) 2002 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v0.5
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 * IBM - Initial implementation
 ******************************************************************************/
package org.eclipse.team.tests.ccvs.core.provider;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.ccvs.core.CVSProviderPlugin;
import org.eclipse.team.tests.ccvs.core.CVSTestSetup;
import org.eclipse.team.tests.ccvs.core.EclipseTest;

/**
 * Test linked resources
 */
public class LinkResourcesTest extends EclipseTest {

	/**
	 * Constructor for CVSProviderTest
	 */
	public LinkResourcesTest() {
		super();
	}

	/**
	 * Constructor for CVSProviderTest
	 */
	public LinkResourcesTest(String name) {
		super(name);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(LinkResourcesTest.class);
		return new CVSTestSetup(suite);
		//return new CVSTestSetup(new WatchEditTest("testReadOnly"));
	}

	
	public void testMapSuccess() throws CoreException, TeamException {
		IProject project = getUniqueTestProject("testLinkSuccess");
		buildResources(project, new String[] { "changed.txt", "deleted.txt", "folder1/", "folder1/a.txt" }, true);
		IFolder folder = project.getFolder("link");
		folder.createLink(new Path("C:/temp"), IResource.ALLOW_MISSING_LOCAL, null);
		RepositoryProvider.map(project, CVSProviderPlugin.getTypeId());
	}
	
	public void testLinkSuccess() throws CoreException, TeamException {
		IProject project = createProject("testLinkFailure", new String[] { "changed.txt", "deleted.txt", "folder1/", "folder1/a.txt" });
		IFolder folder = project.getFolder("link");
		folder.createLink(new Path("C:/temp"), IResource.ALLOW_MISSING_LOCAL, null);
		assertIsIgnored(folder);
	}
}
