package com.avicit.platform.wizards;

import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WorkingSetGroup;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.dialogs.ProjectContentsLocationArea;

public abstract class AvicitPage extends WizardPage{
	private String initialProjectFieldValue;
	Text projectNameField;
	private Listener nameModifyListener;
	private ProjectContentsLocationArea locationArea;
	private WorkingSetGroup workingSetGroup;
	private static final int SIZING_TEXT_FIELD_WIDTH = 250;
	Combo combo;
	Combo frameCombo;
	private String name;
	Button support;
	private boolean isManager;
	public Text ptn;
	public boolean isManager() {
		return isManager;
	}

	public void setManager(boolean isManager) {
		this.isManager = isManager;
		
	}

	public AvicitPage(String pageName) {
		super(pageName);
		this.name = pageName;
		this.nameModifyListener = new Listener() {
			public void handleEvent(Event e) {
				AvicitPage.this.setLocationForSelection();
				boolean valid = AvicitPage.this.validatePage();
				AvicitPage.this.setPageComplete(valid);
			}
		};
		setPageComplete(false);
	}

	

	public AvicitPage(String pageName, IStructuredSelection selection,
			String[] workingSetTypes) {
		this(pageName);
	}

	public void createControl(Composite parent) {
		this.createEcPage(parent);
	}

	public abstract void createEcPage(Composite parent);

	public abstract void change();

	public void createMainPage(Composite parent) {
		Composite composite = new Composite(parent, 0);

		initializeDialogUnits(parent);

		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(composite,
						"org.eclipse.ui.ide.new_project_wizard_page_context");

		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(1808));

		createProjectNameGroup(composite);
		this.locationArea = new ProjectContentsLocationArea(getErrorReporter(),
				composite);
		if (this.initialProjectFieldValue != null) {
			this.locationArea.updateProjectName(this.initialProjectFieldValue);
		}

		setButtonLayoutData(this.locationArea.getBrowseButton());

		setPageComplete(validatePage());

		setErrorMessage(null);
		setMessage(null);
		setControl(composite);
		Dialog.applyDialogFont(composite);
		if (isManager) {
			//updateData(loadPoperties());
			
		}
	}

	public void createIssupport() {

	}

	

	public WorkingSetGroup createWorkingSetGroup(Composite composite,
			IStructuredSelection selection, String[] supportedWorkingSetTypes) {
		if (this.workingSetGroup != null)
			return this.workingSetGroup;
		this.workingSetGroup = new WorkingSetGroup(composite, selection,
				supportedWorkingSetTypes);
		return this.workingSetGroup;
	}

	private ProjectContentsLocationArea.IErrorMessageReporter getErrorReporter() {
		return new ProjectContentsLocationArea.IErrorMessageReporter() {
			public void reportError(String errorMessage, boolean infoOnly) {
				if (infoOnly) {
					AvicitPage.this.setMessage(errorMessage, 1);
					AvicitPage.this.setErrorMessage(null);
				} else {
					AvicitPage.this.setErrorMessage(errorMessage);
				}
				boolean valid = errorMessage == null;
				if (valid) {
					valid = AvicitPage.this.validatePage();
				}

				AvicitPage.this.setPageComplete(valid);
			}
		};
	}

	private final void createProjectNameGroup(Composite parent) {
		Composite projectGroup = new Composite(parent, 0);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		projectGroup.setLayout(layout);
		projectGroup.setLayoutData(new GridData(768));

		Label projectLabel = new Label(projectGroup, 0);
		projectLabel.setText("项目名称");
		//projectLabel.setFont(parent.getFont());
		//GridData gd_text_name = new GridData(SWT.FILL, SWT.CENTER, true, false,2,1);
		
		this.projectNameField = new Text(projectGroup, 2048);
		//this.projectNameField.setLayoutData(gd_text_name);
		//new Label(projectGroup, 0);
		Label pt = new Label(projectGroup, 1);
		pt.setText("项目类型:");
		ptn=new Text(projectGroup,SWT.None);
		ptn.setText(this.name);
		ptn.setEnabled(false);
		//new Label(projectGroup, 0);
		Label projectLabe2 = new Label(projectGroup, 1);
		projectLabe2.setText("");
		Composite group = new Composite(projectGroup, 0);
		group.setLayout(new FillLayout(SWT.HORIZONTAL));
		Label noUse = new Label(projectGroup, 1);
		//support = new Button(projectGroup, SWT.CHECK);
		GridData data = new GridData(768);
		data.widthHint = 250;
		this.projectNameField.setLayoutData(data);

		if (this.initialProjectFieldValue != null) {
			this.projectNameField.setText(this.initialProjectFieldValue);
		}
		this.projectNameField.addListener(24, this.nameModifyListener);
	}

	public IPath getLocationPath() {
		return new Path(this.locationArea.getProjectLocation());
	}

	public URI getLocationURI() {
		return this.locationArea.getProjectLocationURI();
	}

	public IProject getProjectHandle() {
		return ResourcesPlugin.getWorkspace().getRoot()
				.getProject(getProjectName());
	}

	public String getProjectName() {
		if (this.projectNameField == null) {
			return this.initialProjectFieldValue;
		}

		return getProjectNameFieldValue();
	}

	private String getProjectNameFieldValue() {
		if (this.projectNameField == null) {
			return "";
		}

		return this.projectNameField.getText().trim();
	}

	public void setInitialProjectName(String name) {
		if (name == null) {
			this.initialProjectFieldValue = null;
		} else {
			this.initialProjectFieldValue = name.trim();
			if (this.locationArea != null)
				this.locationArea.updateProjectName(name.trim());
		}
	}

	void setLocationForSelection() {
		this.locationArea.updateProjectName(getProjectNameFieldValue());
	}

	protected boolean validatePage() {
		if(isManager){
			return true;
		}
		IWorkspace workspace = IDEWorkbenchPlugin.getPluginWorkspace();

		String projectFieldContents = getProjectNameFieldValue();
		if (projectFieldContents.equals("")) {
			setErrorMessage(null);
			setMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectNameEmpty);
			return false;
		}

		IStatus nameStatus = workspace.validateName(projectFieldContents, 4);
		if (!(nameStatus.isOK())) {
			setErrorMessage(nameStatus.getMessage());
			return false;
		}

		IProject handle = getProjectHandle();
		if (handle.exists()) {
			setErrorMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectExistsMessage);
			return false;
			
			
		}

		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(getProjectNameFieldValue());
		this.locationArea.setExistingProject(project);

		String validLocationMessage = this.locationArea.checkValidLocation();
		if (validLocationMessage != null) {
			setErrorMessage(validLocationMessage);
			return false;
		}

		setErrorMessage(null);
		setMessage(null);
		return true;
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible)
			this.projectNameField.setFocus();
	}

	public boolean useDefaults() {
		return this.locationArea.isDefault();
	}

	public IWorkingSet[] getSelectedWorkingSets() {
		return ((this.workingSetGroup == null) ? new IWorkingSet[0]
				: this.workingSetGroup.getSelectedWorkingSets());
	}

	public String getSupport(){
		if(support.getSelection()){
			return "1";
		}else{
			return "0";
		}
	}
	
}
