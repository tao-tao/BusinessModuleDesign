package avicit.ui.runtime.core.node;


import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import avicit.ui.common.util.PlatformHelper;
import avicit.ui.common.util.ProjectFinder;

public class NewPageXWizardPage extends WizardPage {

	private Text text_folder;
	private Text text_processFile;
	private Label processExist;
	private Text text_processName;
//	private Button checkbox_processToTree;
//	private Button browseButton;

	private IWorkspaceRoot workspaceRoot;
	private IProject currProject;
	private String folderPath;

	public NewPageXWizardPage() {
		super("新建功能模块");
		setTitle("新建功能模块");
		setDescription("新建功能模块.");
		workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
	}

	/**
	 * @param selection
	 */
	public void init(IStructuredSelection selection) {
		if(selection.getFirstElement() instanceof PackageNode)
		{
			PackageNode node = (PackageNode) selection.getFirstElement();
			folderPath = node.getFolder().getProjectRelativePath();
	
			IPath path = getSelectionRelativePath(selection);
			if (null != path) {
				folderPath = path.toString();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = createClientArea(parent);
		createLabel(composite);
		createContainerField(composite);
		createProcessField(composite);
		setControl(composite);
		Dialog.applyDialogFont(composite);
		setPageComplete(false);
	}

	private Composite createClientArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.numColumns = 2;
		composite.setLayout(layout);
		return composite;
	}

	private void createLabel(Composite composite) {
		Label label = new Label(composite, SWT.WRAP);
		label.setText("������Ҫ�����Ŀ����������Ϣ.");
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(80);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);
	}

	private void createContainerField(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("�����ļ�����·�� : ");
		text_folder = new Text(parent, SWT.BORDER);
		text_folder.setText(folderPath);
//		text_folder.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent e) {
//				verifyContentsValid();
//			}
//		});
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		text_folder.setLayoutData(gd);
		text_folder.setEditable(false);
//		browseButton = new Button(parent, SWT.NONE);
//		browseButton.setText("ѡ��Ŀ¼");
//		browseButton.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				chooseFolder(text_folder);
//			}
//		});
//		gd = new GridData();
//		gd.widthHint = convertWidthInCharsToPixels(15);
//		browseButton.setLayoutData(gd);
	}

	private void createProcessField(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("�������ļ���� (�����׺) : ");
		text_processFile = new Text(parent, SWT.BORDER);
		text_processFile.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				verifyContentsValid();
			}
		});
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		text_processFile.setLayoutData(gd);
		text_processFile.setFocus();
//		processExist = new Label(parent, SWT.NONE);
//		gd = new GridData();
//		gd.widthHint = convertWidthInCharsToPixels(15);
//		processExist.setLayoutData(gd);

		Label namelabel = new Label(parent, SWT.NONE);
		namelabel.setText("���������: ");
		text_processName = new Text(parent, SWT.BORDER);
		text_processName.setLayoutData(gd);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		text_processName.setLayoutData(gd);
		this.text_processName.setText("");

//		checklabel.setText("�Ƿ���ӵ�Tree�� : ");
//		checkbox_processToTree = new Button(parent, SWT.CHECK);
//		checkbox_processToTree.setText("��");
//		checkbox_processToTree.setLayoutData(gd);
//		checkbox_processToTree.setSelection(false);
	}

	private IPath getSelectionRelativePath(IStructuredSelection selection) {
		if (selection == null || selection.isEmpty()) {
			IProject cjp = ProjectFinder.getCurrentProjectByActiveEditor();
			IFile cf = PlatformHelper.getCurrentFileByActiveEditor();
			if (cjp == null) {
				throw new RuntimeException("������ѡ��һ����Ŀ");
			} else {
				this.currProject = cjp;
				if (null != cf) {
					//  
				}

				return null;
			}
		} else {

			IResource res = null;

			Object selectedElement = selection.getFirstElement();

			IAdaptable adaptable = (IAdaptable) selectedElement;
			res = (IResource) adaptable.getAdapter(IResource.class);

			this.currProject = res.getProject();

			int rtype = res.getType();
			switch (rtype) {
			case IResource.FOLDER:
				return res.getProjectRelativePath();
			case IResource.FILE:
				File file = (File) res;
				return file.getParent().getProjectRelativePath();
			case IResource.PROJECT:
				return null;
			case IResource.ROOT:
			default:
			}
			return null;
		}
	}

	private void verifyContentsValid() {
		if (!checkContainerPathValid()) {
			setErrorMessage("The folder does not exist.");
			setPageComplete(false);
		} else if (isProcessNameEmpty()) {
			setErrorMessage("Enter a name for the process.");
			setPageComplete(false);
		} else if (processFileExists()) {
			setErrorMessage("A process with this name already exists.");
			setPageComplete(false);
		} else {
			setErrorMessage(null);
			setPageComplete(true);
		}
	}

	private boolean processFileExists() {
		IPath processfile = new Path(this.text_folder.getText()).append(this.getProcessFileName());
		IResource fprofile = this.currProject.findMember(processfile);
		boolean ex = fprofile != null;

		return ex;
	}

	private boolean isProcessNameEmpty() {
		String str = text_processFile.getText();
		return "".equals(str);
	}

	private boolean checkContainerPathValid() {

		String str = this.text_folder.getText();
		if (str.length() == 0) {
			return false;
		} else {
			IPath path = this.currProject.getFullPath().append(str);
			IStatus validate = ResourcesPlugin.getWorkspace().validatePath(path.toString(), IResource.FOLDER);
			if (validate.matches(IStatus.ERROR)) {
				return false;
			} else {
				IResource res = this.workspaceRoot.findMember(path);
				if (res != null) {
					if (res.getType() != IResource.FOLDER) {
						return false;
					} else {
						return true;
					}
				} else {
					return false;
				}
			}
		}

	}

	private String getProcessFileName() {
		String processFile = text_processFile.getText();
		int indx = processFile.indexOf('.');
		if (indx != -1) {
			processFile = processFile.substring(0, indx);
		}

		return processFile + ".pagex.xml";
	}

	public IFile getProcessFile() {
		IPath path = this.currProject.getFullPath().append(this.text_folder.getText()).append(this.getProcessFileName());
		return workspaceRoot.getFile(path);
	}

	public String getProcessName() {
		String processName = null;
		String pn = text_processName.getText();
		if (pn.length() > 0) {
			processName = pn;
		} else {
			processName = text_processFile.getText();
		}
		return processName;
	}

//	public boolean getSpecialCheck() {
//		return this.checkbox_processToTree.getSelection();
//	}
}
