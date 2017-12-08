package avicit.platform6.tools.codegeneration.wizard;


import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
/**
 * @类名称: NewUnitCreationWizardPage.java
 * @类描述: <p>
 * 			该类的主要功能是初始化单元测试类的信息
 *       <p>
 * @Methods:
 * @see class:
 * @exception class
 *                :
 * @作者: user@tansun.com.cn
 * @创建时间: 2013-1-31 上午10:28:01
 * @版本: 1.00
 * 
 * @修改记录: <p>
 *        版本 修改人 修改时间 修改内容描述<br>
 *        ----------------------------------------<br>
 *        1.00 user 2013-1-31 下午17:11:01<br>
 *        ----------------------------------------<br>
 *        </p>
 */
public class NewUnitCreationWizardPage extends BaseWizardPage  {

	private Text text_name;
	private Text text_package;
	private Text text_src;
	private Text text_springFile;
	private Button btn_openFile;

	IStructuredSelection selection;
	IPackageFragmentRoot jp;
	IFile file;
	private Text text_implpackage;

	public NewUnitCreationWizardPage() {
		super("创建单元测试类");
		setTitle("创建单元测试类");
		setDescription("单元测试类是一个独立的业务逻辑单元.");
		this.jp = jp;
		this.file = file;
	}

	/**
	 * @param selection
	 */
	public void init(IStructuredSelection selection) {
		this.selection = selection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = createClientArea(parent);
		Label label = new Label(composite, SWT.WRAP);
		label.setText("请输入要创建的单元测试文件相关信息.");
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(80);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);

		label = new Label(composite, SWT.NONE);
		label.setText("所在项目名称 : ");
		text_src = new Text(composite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		text_src.setLayoutData(gd);
//		text_src.setText(jp.getResource().getProjectRelativePath().toString());
		text_src.setText("李东");
		text_src.setEditable(false);

		label = new Label(composite, SWT.NONE);
		label.setText("组件配置文件名称 : ");
		text_springFile = new Text(composite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		text_springFile.setLayoutData(gd);
//		text_springFile.setText(file.getProjectRelativePath().toString());
		text_springFile.setText("文件路径");
		text_springFile.setEditable(false);

		label = new Label(composite, SWT.NONE);
		label.setText("单元测试类包路径 : ");
		text_package = new Text(composite, SWT.BORDER);
		text_package.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				autoContent();
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		text_package.setLayoutData(gd);
		text_package.setFocus();

		label = new Label(composite, SWT.NONE);
		label.setText("单元测试类名称 : ");
		text_name = new Text(composite, SWT.BORDER);
		text_name.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				verifyContentsValid();
				autoContent();
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		text_name.setLayoutData(gd);

		label = new Label(composite, SWT.NONE);
		label.setText("创建测试类的全名 : ");
		text_implpackage = new Text(composite, SWT.BORDER);
		text_implpackage.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				verifyContentsValid();
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		text_implpackage.setLayoutData(gd);
		text_implpackage.setEditable(false);

		label = new Label(composite, SWT.NONE);
		label.setText("创建完毕后,是否编辑Java文件 : ");

		btn_openFile = new Button(composite, SWT.CHECK);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		btn_openFile.setLayoutData(gd);
		btn_openFile.setText("是");
		btn_openFile.setSelection(true);

		setControl(composite);
		Dialog.applyDialogFont(composite);

//		String name = file.getName();
		String name = "文件名称";
		int index = name.indexOf(".");
		if (index > 0)
			name = name.substring(0, index);

		text_package.setText("ut.control");
		text_name.setText("Test" + name);
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

	private String conalicalName(String name) {
		name = name.trim();
		if (name.length() > 0)
			name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
		return name;
	}

	private void autoContent() {
		this.text_implpackage.setText(this.text_package.getText() + "."
				+ conalicalName(this.text_name.getText()));
	}

	private void verifyContentsValid() {
		if (isBeanNameEmpty()) {
			setErrorMessage("请输入测试类名.");
			setPageComplete(false);
		} else if (implFileExists()) {
			setErrorMessage("测试类类文件已经存在，请修改.");
			setPageComplete(true);
		} else {
			if (!text_package.getText().matches("[a-zA-Z.0-9]+")) {
				setErrorMessage("包名必须是英文字母.");
				setPageComplete(false);

			} else {
				setErrorMessage(null);
				setPageComplete(true);
			}
		}
	}

	private boolean isBeanNameEmpty() {
		String str = text_name.getText();
		return "".equals(str);
	}

	private boolean implFileExists() {
		String infPath = this.text_implpackage.getText();
		int index = infPath.lastIndexOf(".");
		String pack = "";
		String cls = infPath;
		if (index > 0) {
			pack = infPath.substring(0, index);
			cls = infPath.substring(index + 1);
		}
//		ICompilationUnit fprofile = this.jp.getPackageFragment(pack)
//				.getCompilationUnit(cls + ".java");
//		boolean ex = fprofile != null && fprofile.exists();
		
		boolean ex = true;
		return ex;
	}

	public String getImplPath() {
		String infPath = this.text_implpackage.getText();
		return infPath;
	}

	public String getClsName() {
		String infPath = this.text_name.getText();
		return infPath;
	}

	public String getPackName() {
		String infPath = this.text_package.getText();
		return infPath;
	}

	public boolean getOpenFile() {
		return this.btn_openFile.getSelection();
	}

	// public String getProcessName() {
	// String processName = null;
	// String pn = text_processName.getText();
	// if (pn.length() > 0) {
	// processName = pn;
	// } else {
	// processName = text_processFile.getText();
	// }
	// return processName;
	// }

	// public boolean getSpecialCheck() {
	// return this.checkbox_processToTree.getSelection();
	// }
}
