/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.debug.internal.ui.launchConfigurations;

import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchDelegate;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;
import org.eclipse.debug.internal.core.DebugCoreMessages;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.text.MessageFormat;

/**
 * Allows the user to specify to see and copy the command line to be executed
 * for the launch.
 * 
 * @since 3.13
 */
public class ShowCommandLineDialog extends MessageDialog {
	Text fModuleArgumentsText;
	ILaunchConfiguration flaunchConfiguration;


	public ShowCommandLineDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage, int dialogImageType, String[] dialogButtonLabels, int defaultIndex, ILaunchConfiguration config) {
		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels, defaultIndex);
		flaunchConfiguration = config;
	}

	@Override
	protected Control createCustomArea(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout());
		Font font = parent.getFont();

		Group group = new Group(comp, SWT.NONE);
		GridLayout topLayout = new GridLayout();
		group.setLayout(topLayout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = convertHeightInCharsToPixels(15);
		gd.widthHint = convertWidthInCharsToPixels(50);
		group.setLayoutData(gd);
		group.setFont(font);


		// Label description = new Label(group, SWT.WRAP);
		// description.setText(ActionMessages.Override_Dependencies_label1);
		fModuleArgumentsText = new Text(group, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = convertHeightInCharsToPixels(10);
		gd.widthHint = convertWidthInCharsToPixels(60);
		fModuleArgumentsText.setLayoutData(gd);

		String command = "Could not retrieve the command"; //$NON-NLS-1$
		try {
			Set<String> modes = flaunchConfiguration.getModes();
			modes.add(ILaunchManager.RUN_MODE);
			ILaunchDelegate[] delegates = flaunchConfiguration.getType().getDelegates(modes);
			if (delegates.length ==1) {
				ILaunchConfigurationDelegate delegate = delegates[0].getDelegate();
				ILaunchConfigurationDelegate2 delegate2;
				ILaunch launch = null;
				if (delegate instanceof ILaunchConfigurationDelegate2) {
					delegate2 = (ILaunchConfigurationDelegate2) delegate;
					if (delegate2 != null) {
						launch = delegate2.getLaunch(flaunchConfiguration, ILaunchManager.RUN_MODE);
					}
					if (launch == null) {
						launch = new Launch(flaunchConfiguration, ILaunchManager.RUN_MODE, null);
					} else {
						// ensure the launch mode is valid
						if (!ILaunchManager.RUN_MODE.equals(launch.getLaunchMode())) {
							IStatus status = new Status(IStatus.ERROR, DebugPlugin.getUniqueIdentifier(),
									DebugPlugin.ERROR, MessageFormat.format(DebugCoreMessages.LaunchConfiguration_14,
											ILaunchManager.RUN_MODE, launch.getLaunchMode()),
									null);
							throw new CoreException(status);
						}
					}
				}
				command = delegate.showCommandLine(flaunchConfiguration, ILaunchManager.RUN_MODE, launch, null);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		fModuleArgumentsText.setText(command);
		fModuleArgumentsText.setEditable(false);

		return comp;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == OK) {
			Clipboard clipboard = new Clipboard(null);
			try {
				TextTransfer textTransfer = TextTransfer.getInstance();
				Transfer[] transfers = new Transfer[] { textTransfer };
				Object[] data = new Object[] { fModuleArgumentsText.getText() };
				clipboard.setContents(data, transfers);
			} finally {
				clipboard.dispose();
			}
		}
		super.buttonPressed(buttonId);
	}



}
