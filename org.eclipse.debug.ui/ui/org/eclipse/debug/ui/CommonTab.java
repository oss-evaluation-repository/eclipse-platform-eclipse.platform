package org.eclipse.debug.ui;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
 
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

/**
 * Common launch configuration tab to specify the location a launch configuration
 * is stored, whether it should appear in the favorites list, and perspective
 * switching for an associated launch.
 * <p>
 * Clients may instantiate this class. This class is not intended to be subclassed.
 * </p>
 * @since 2.0
 */
public class CommonTab extends AbstractLaunchConfigurationTab {
		
	// Local/shared UI widgets
	private Label fLocalSharedLabel;
	private Button fLocalRadioButton;
	private Button fSharedRadioButton;
	
	// Shared location UI widgets
	private Label fSharedLocationLabel;
	private Text fSharedLocationText;
	private Button fSharedLocationButton;
	
	/**
	 * The combo box specifying the run perspective
	 */
	private Combo fRunPerspectiveCombo;
	
	/**
	 * Label for the run perspective combo box
	 */
	private Label fRunPerspectiveLabel;
	
	/**
	 * The combo box specifying the debug perspective
	 */
	private Combo fDebugPerspectiveCombo;
	
	/**
	 * Label for the debug perspective combo box
	 */
	private Label fDebugPerspectiveLabel;

	/**
	 * The label that acts as header for the 'switch to perspective' widgets
	 */
	private Label fSwitchToLabel;

	/**
	 * The check box specifying run favoite
	 */
	private Button fRunFavoriteButton;	
	
	/**
	 * The check box specifying debug favoite
	 */
	private Button fDebugFavoriteButton;
		
	/**
	 * Constant for perspective Combo widgets
	 */
	private static final String PERSPECTIVE_NONE = "none";
		
	/**
	 * @see ILaunchConfigurationTab#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		GridLayout topLayout = new GridLayout();
		comp.setLayout(topLayout);		
		GridData gd;

		createVerticalSpacer(comp);
		
		Composite radioComp = new Composite(comp, SWT.NONE);
		GridLayout radioLayout = new GridLayout();
		radioLayout.marginHeight = 0;
		radioLayout.marginWidth = 0;
		radioComp.setLayout(radioLayout);
		
		setLocalSharedLabel(new Label(radioComp, SWT.NONE));
		getLocalSharedLabel().setText("Type of launch configuration:");
		
		setLocalRadioButton(new Button(radioComp, SWT.RADIO));
		getLocalRadioButton().setText("L&ocal");
		setSharedRadioButton(new Button(radioComp, SWT.RADIO));
		getSharedRadioButton().setText("S&hared");
		getSharedRadioButton().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handleSharedRadioButtonSelected();
			}
		});
		
		Composite locationComp = new Composite(comp, SWT.NONE);
		GridLayout locationLayout = new GridLayout();
		locationLayout.numColumns = 2;
		locationLayout.marginHeight = 0;
		locationLayout.marginWidth = 0;
		locationComp.setLayout(locationLayout);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		locationComp.setLayoutData(gd);
		
		setSharedLocationLabel(new Label(locationComp, SWT.NONE));
		getSharedLocationLabel().setText("Location of shared confi&guration:");
		gd = new GridData();
		gd.horizontalSpan = 2;
		getSharedLocationLabel().setLayoutData(gd);
		
		setSharedLocationText(new Text(locationComp, SWT.SINGLE | SWT.BORDER));
		gd = new GridData(GridData.FILL_HORIZONTAL);
		getSharedLocationText().setLayoutData(gd);
		getSharedLocationText().addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				updateLaunchConfigurationDialog();
			}
		});
		
		setSharedLocationButton(createPushButton(locationComp, "&Browse...", null));	
		getSharedLocationButton().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handleSharedLocationButtonSelected();
			}
		});	

		getLocalRadioButton().setSelection(true);
		setSharedEnabled(false);

		createVerticalSpacer(comp);
		createVerticalSpacer(comp);
		
		setSwitchToLabel(new Label(comp, SWT.HORIZONTAL | SWT.LEFT));
		getSwitchToLabel().setText("Switch to/Open perspective when launched in:");
		gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.horizontalSpan = 3;
		getSwitchToLabel().setLayoutData(gd);
		
		Composite perspComp = new Composite(comp, SWT.NONE);
		GridLayout perspLayout = new GridLayout();
		perspLayout.numColumns = 2;
		perspComp.setLayout(perspLayout);
		
		setRunPerspectiveLabel(new Label(perspComp, SWT.NONE));
		getRunPerspectiveLabel().setText("Run mode");
		
		setRunPerspectiveCombo(new Combo(perspComp, SWT.DROP_DOWN | SWT.READ_ONLY));
		gd = new GridData(GridData.GRAB_HORIZONTAL);
		getRunPerspectiveCombo().setLayoutData(gd);
		fillWithPerspectives(getRunPerspectiveCombo());
		
		setDebugPerspectiveLabel(new Label(perspComp, SWT.NONE));
		getDebugPerspectiveLabel().setText("Debug mode");
		
		setDebugPerspectiveCombo(new Combo(perspComp, SWT.DROP_DOWN |SWT.READ_ONLY));
		gd = new GridData(GridData.GRAB_HORIZONTAL);
		getDebugPerspectiveCombo().setLayoutData(gd);		
		fillWithPerspectives(getDebugPerspectiveCombo());				
		
		createVerticalSpacer(comp);
		createVerticalSpacer(comp);
		
		Composite favComp = new Composite(comp, SWT.NONE);
		GridLayout favLayout = new GridLayout();
		favLayout.numColumns = 1;
		favComp.setLayout(favLayout);
		
		Label favLabel = new Label(favComp, SWT.HORIZONTAL | SWT.LEFT);
		favLabel.setText("Display in favorites menu:");
		
		setRunFavoriteButton(new Button(favComp, SWT.CHECK));
		getRunFavoriteButton().setText("&Run");
						
		setDebugFavoriteButton(new Button(favComp, SWT.CHECK));
		getDebugFavoriteButton().setText("Debu&g");
		

	}

	/**
	 * Create some empty space 
	 */
	private void createVerticalSpacer(Composite comp) {
		new Label(comp, SWT.NONE);
	}

	private void setSharedLocationButton(Button sharedLocationButton) {
		this.fSharedLocationButton = sharedLocationButton;
	}

	private Button getSharedLocationButton() {
		return fSharedLocationButton;
	}

	private void setSharedLocationText(Text sharedLocationText) {
		this.fSharedLocationText = sharedLocationText;
	}

	private Text getSharedLocationText() {
		return fSharedLocationText;
	}

	private void setSharedLocationLabel(Label sharedLocationLabel) {
		this.fSharedLocationLabel = sharedLocationLabel;
	}

	private Label getSharedLocationLabel() {
		return fSharedLocationLabel;
	}

	private void setLocalSharedLabel(Label localSharedLabel) {
		fLocalSharedLabel = localSharedLabel;
	}

	private Label getLocalSharedLabel() {
		return fLocalSharedLabel;
	}

 	private void setLocalRadioButton(Button button) {
 		fLocalRadioButton = button;
 	}
 	
 	private Button getLocalRadioButton() {
 		return fLocalRadioButton;
 	} 	
 	
 	private void setSharedRadioButton(Button button) {
 		fSharedRadioButton = button;
 	}
 	
 	private Button getSharedRadioButton() {
 		return fSharedRadioButton;
 	} 	
 	
	/**
	 * Returns the perspective combo assoicated with the
	 * debug perspective button.
	 * 
	 * @return a combo box
	 */
	private Combo getDebugPerspectiveCombo() {
		return fDebugPerspectiveCombo;
	}

	/**
	 * Sets the perspective combo assoicated with the
	 * debug perspective button.
	 * 
	 * @param combo a combo box
	 */
	private void setDebugPerspectiveCombo(Combo combo) {
		fDebugPerspectiveCombo = combo;
	}

	/**
	 * Returns the perspective combo assoicated with the
	 * run perspective button.
	 * 
	 * @return a combo box
	 */
	private Combo getRunPerspectiveCombo() {
		return fRunPerspectiveCombo;
	}

	/**
	 * Sets the perspective combo assoicated with the
	 * run perspective button.
	 * 
	 * @param combo a combo box
	 */
	private void setRunPerspectiveCombo(Combo combo) {
		fRunPerspectiveCombo = combo;
	}

	private void setRunPerspectiveLabel(Label fRunPerspectiveLabel) {
		this.fRunPerspectiveLabel = fRunPerspectiveLabel;
	}

	private Label getRunPerspectiveLabel() {
		return fRunPerspectiveLabel;
	}

	private void setDebugPerspectiveLabel(Label fDebugPerspectiveLabel) {
		this.fDebugPerspectiveLabel = fDebugPerspectiveLabel;
	}

	private Label getDebugPerspectiveLabel() {
		return fDebugPerspectiveLabel;
	}

	private void setSwitchToLabel(Label switchToLabel) {
		fSwitchToLabel = switchToLabel;
	}

	private Label getSwitchToLabel() {
		return fSwitchToLabel;
	}

	private void handleSharedRadioButtonSelected() {
		setSharedEnabled(isShared());
		updateLaunchConfigurationDialog();
	}
	
	private void setSharedEnabled(boolean enable) {
		getSharedLocationLabel().setEnabled(enable);
		getSharedLocationText().setEnabled(enable);
		getSharedLocationButton().setEnabled(enable);
	}
	
	private boolean isShared() {
		return getSharedRadioButton().getSelection();
	}
	
	private void handleSharedLocationButtonSelected() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(),
																	   getWorkspaceRoot(),
																	   false,
																	   "Select a location for the launch configuration");
		
		String currentContainerString = getSharedLocationText().getText();
		IContainer currentContainer = getContainer(currentContainerString);
		if (currentContainer != null) {
			IPath path = currentContainer.getFullPath();
			dialog.setInitialSelections(new Object[] {path});
		}
		
		
		dialog.open();
		Object[] results = dialog.getResult();		
		if ((results != null) && (results.length > 0) && (results[0] instanceof IPath)) {
			IPath path = (IPath)results[0];
			String containerName = path.toOSString();
			getSharedLocationText().setText(containerName);
		}		
	}
	
	private IContainer getContainer(String path) {
		Path containerPath = new Path(path);
		return (IContainer) getWorkspaceRoot().findMember(containerPath);
	}
	
	/**
	 * Returns the perspective with the given label, or
	 * <code>null</code> if none is found.
	 * 
	 * @param label perspective label
	 * @return perspective descriptor
	 */
	private IPerspectiveDescriptor getPerspectiveWithLabel(String label) {		
		return PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithLabel(label);
	}
	
	/**
	 * Returns the perspective with the given id, or
	 * <code>null</code> if none is found.
	 * 
	 * @param id perspective identifier
	 * @return perspective descriptor
	 */
	private IPerspectiveDescriptor getPerspectiveWithId(String id) {		
		return PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(id);
	}	

	/**
	 * Fills the given combo box with the labels of all existing
	 * perspectives and one to indicate 'none'.
	 * 
	 * @param combo combo box
	 */
	private void fillWithPerspectives(Combo combo) {
		combo.add(PERSPECTIVE_NONE);
		IPerspectiveRegistry reg = PlatformUI.getWorkbench().getPerspectiveRegistry();
		IPerspectiveDescriptor[] persps = reg.getPerspectives();
		for (int i = 0; i < persps.length; i++) {
			combo.add(persps[i].getLabel());
		}
	}
	
	/**
	 * @see ILaunchConfigurationTab#initializeFrom(ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration configuration) {	
		updateLocalSharedFromConfig(configuration);
		updateSharedLocationFromConfig(configuration);
		updateRunPerspectiveFromConfig(configuration);
		updateDebugPerspectiveFromConfig(configuration);
		updateFavoritesFromConfig(configuration);
	}
	
	private void updateLocalSharedFromConfig(ILaunchConfiguration config) {
		boolean isShared = !config.isLocal();
		getSharedRadioButton().setSelection(isShared);
		getLocalRadioButton().setSelection(!isShared);
		setSharedEnabled(isShared);
	}
	
	private void updateSharedLocationFromConfig(ILaunchConfiguration config) {
		IFile file = config.getFile();
		if (file != null) {
			IContainer parent = file.getParent();
			if (parent != null) {
				String containerName = parent.getFullPath().toOSString();
				getSharedLocationText().setText(containerName);
			}
		}
	}
	
	private void updateRunPerspectiveFromConfig(ILaunchConfiguration config) {
		ILaunchConfigurationType type = null;
		String runPerspID = null;
		try {
			runPerspID = config.getAttribute(IDebugUIConstants.ATTR_TARGET_RUN_PERSPECTIVE, (String)null);
			type = config.getType();
		} catch (CoreException ce) {
			updatePerspectiveCombo(getRunPerspectiveCombo(), null);
			getRunPerspectiveCombo().setEnabled(false);
			getRunPerspectiveLabel().setEnabled(false);
			return;
		}
		updatePerspectiveCombo(getRunPerspectiveCombo(), runPerspID);
		boolean enable = type.supportsMode(ILaunchManager.RUN_MODE);
		getRunPerspectiveCombo().setEnabled(enable);
		getRunPerspectiveLabel().setEnabled(enable);
	}
	
	private void updateDebugPerspectiveFromConfig(ILaunchConfiguration config) {
		ILaunchConfigurationType type = null;
		String debugPerspID = null;
		try {			
			debugPerspID = config.getAttribute(IDebugUIConstants.ATTR_TARGET_DEBUG_PERSPECTIVE, (String)null);
			type = config.getType();
		} catch (CoreException ce) {
			updatePerspectiveCombo(getDebugPerspectiveCombo(), null);
			getDebugPerspectiveCombo().setEnabled(false);
			getDebugPerspectiveLabel().setEnabled(false);
			return;
		}
		updatePerspectiveCombo(getDebugPerspectiveCombo(), debugPerspID);
		boolean enable = type.supportsMode(ILaunchManager.DEBUG_MODE);
		getDebugPerspectiveCombo().setEnabled(enable);
		getDebugPerspectiveLabel().setEnabled(enable);
	}
	
	private void updateFavoritesFromConfig(ILaunchConfiguration config) {
		ILaunchConfigurationType type = null;
		boolean isDebug = false;
		boolean isRun = false;
		try {
			type = config.getType();
			isDebug = config.getAttribute(IDebugUIConstants.ATTR_DEBUG_FAVORITE, false);
			isRun = config.getAttribute(IDebugUIConstants.ATTR_RUN_FAVORITE, false);
		} catch (CoreException ce) {
			getDebugFavoriteButton().setEnabled(false);
			getRunFavoriteButton().setEnabled(false);
			return;
		}
		getDebugFavoriteButton().setEnabled(type.supportsMode(ILaunchManager.DEBUG_MODE));
		getRunFavoriteButton().setEnabled(type.supportsMode(ILaunchManager.RUN_MODE));
		getDebugFavoriteButton().setSelection(isDebug);
		getRunFavoriteButton().setSelection(isRun);
		
	}
	
	/**
	 * Based on the given perspective identifier, update the settings
	 * of the button and associated combo box. The check box is selected
	 * when there is a valid perspective, and the combo box is set to
	 * display the label of the associated perspective. The check box is
	 * deselected, and the combo box is set to the default value (debug
	 * perspective) when the identfier is <code>null</code>.
	 * 
	 * @param button check box button
	 * @param combo combo box with perspective labels
	 * @param id perspective identifier or <code>null</code>
	 */
	private void updatePerspectiveCombo(Combo combo, String id) {
		if (id == null) {
			combo.setText(PERSPECTIVE_NONE);
		} else {
			IPerspectiveDescriptor pd = getPerspectiveWithId(id);
			if (pd == null) {
				// perpective does not exist - reset
				updatePerspectiveCombo(combo, null);
			} else {
				combo.setText(pd.getLabel());
			}
		}
	}

	private void updateConfigFromLocalShared(ILaunchConfigurationWorkingCopy config) {
		if (isShared()) {
			String containerPathString = getSharedLocationText().getText();
			IContainer container = (IContainer) getContainer(containerPathString);
			config.setContainer(container);
		} else {
			config.setContainer(null);
		}
	}
	
	/**
	 * Update the run perspective attribute based on current
	 * UI settings.
	 */
	private void updateConfigFromRunPerspective(ILaunchConfigurationWorkingCopy config) {
		IPerspectiveDescriptor descriptor = getPerspectiveWithLabel(getRunPerspectiveCombo().getText());
		String perspID = null;
		if (descriptor != null) {
			perspID = descriptor.getId();
		}
		config.setAttribute(IDebugUIConstants.ATTR_TARGET_RUN_PERSPECTIVE, perspID);
	}
	
	/**
	 * Update the debug perspective attribute based on current
	 * UI settings.
	 */
	private void updateConfigFromDebugPerspective(ILaunchConfigurationWorkingCopy config) {
		IPerspectiveDescriptor descriptor = getPerspectiveWithLabel(getDebugPerspectiveCombo().getText());
		String perspID = null;
		if (descriptor != null) {
			perspID = descriptor.getId();
		}
		config.setAttribute(IDebugUIConstants.ATTR_TARGET_DEBUG_PERSPECTIVE, perspID);
	}
	
	/**
	 * Update the favorite settings.
	 * 
	 * NOTE: set to NULL instead of false for backwards compatibility
	 *  when comparing if content is equal, since 'false' is default
	 * 	and will be missing for older configs.
	 */
	private void updateConfigFromFavorites(ILaunchConfigurationWorkingCopy config) {
		if (getDebugFavoriteButton().getSelection()) {
			config.setAttribute(IDebugUIConstants.ATTR_DEBUG_FAVORITE, true);
		} else {
			config.setAttribute(IDebugUIConstants.ATTR_DEBUG_FAVORITE, (String)null);
		} 
		if (getRunFavoriteButton().getSelection()) {
			config.setAttribute(IDebugUIConstants.ATTR_RUN_FAVORITE, true);
		} else {
			config.setAttribute(IDebugUIConstants.ATTR_RUN_FAVORITE, (String)null);
		} 		
	}	
	
	/**
	 * Convenience method for getting the workspace root.
	 */
	private IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	
	/**
	 * @see ILaunchConfigurationTab#isValid()
	 */
	public boolean isValid() {
		setMessage(null);
		setErrorMessage(null);
		
		if (isShared()) {
			String path = fSharedLocationText.getText().trim();
			IContainer container = getContainer(path);
			if (container == null || container.equals(ResourcesPlugin.getWorkspace().getRoot())) {
				setErrorMessage("Invalid location.");
				return false;
			}
		}
		
		return true;
	}

	/**
	 * @see ILaunchConfigurationTab#setDefaults(ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy config) {
		config.setContainer(null);
		String debugId = null;
		if (DebugUITools.getPreferenceStore().getBoolean(IDebugUIConstants.PREF_AUTO_SHOW_DEBUG_VIEW)) {
			debugId = IDebugUIConstants.ID_DEBUG_PERSPECTIVE;
		}
		String runId = null;
		if (DebugUITools.getPreferenceStore().getBoolean(IDebugUIConstants.PREF_AUTO_SHOW_PROCESS_VIEW)) {
			runId = IDebugUIConstants.ID_DEBUG_PERSPECTIVE;
		}		
		config.setAttribute(IDebugUIConstants.ATTR_TARGET_DEBUG_PERSPECTIVE, debugId);
		config.setAttribute(IDebugUIConstants.ATTR_TARGET_RUN_PERSPECTIVE, runId);
	}

	/**
	 * @see ILaunchConfigurationTab#performApply(ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		updateConfigFromDebugPerspective(configuration);
		updateConfigFromRunPerspective(configuration);
		updateConfigFromLocalShared(configuration);
		updateConfigFromFavorites(configuration);
	}

	/**
	 * Returns the check box used to specify a config
	 * as a debug favorite.
	 * 
	 * @return check box
	 */
	private Button getDebugFavoriteButton() {
		return fDebugFavoriteButton;
	}

	/**
	 * Sets the check box used to specify a config
	 * as a debug favorite.
	 * 
	 * @param button check box
	 */
	private void setDebugFavoriteButton(Button button) {
		fDebugFavoriteButton = button;
	}
	
	/**
	 * Returns the check box used to specify a config
	 * as a run favorite.
	 * 
	 * @return check box
	 */
	private Button getRunFavoriteButton() {
		return fRunFavoriteButton;
	}

	/**
	 * Sets the check box used to specify a config
	 * as a run favorite.
	 * 
	 * @param button check box
	 */
	private void setRunFavoriteButton(Button button) {
		fRunFavoriteButton = button;
	}	

	/**
	 * @see ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return "&Common";
	}

}

