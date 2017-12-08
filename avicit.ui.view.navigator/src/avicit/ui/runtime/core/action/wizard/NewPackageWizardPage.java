package avicit.ui.runtime.core.action.wizard;

import org.eclipse.core.internal.resources.ResourceException;
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

import avicit.ui.core.runtime.resource.IFolderDelegate;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.node.AbstractNode;


public class NewPackageWizardPage extends WizardPage {

	private IStructuredSelection selection;
	Text text_srcPath ;
	Text text_packName;
	AbstractNode node;
	
	public NewPackageWizardPage(IStructuredSelection selection) {
		super("wizardPage");
		setTitle("创建新元素");
		setDescription("在源文件夹下创建新的包路径.");
		setPageComplete(false);
		updateStatus("请输入新的包路径", false);
		this.selection = selection;
		node = (AbstractNode)selection.getFirstElement();
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);

		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;

		container.setLayout(gridLayout);

		Label idLabel = new Label(container, SWT.NONE);
		idLabel.setText("源文件路径");

		text_srcPath = new Text(container, SWT.BORDER);
		final GridData gd_text_id = new GridData(SWT.FILL, SWT.CENTER, true, false);
		text_srcPath.setLayoutData(gd_text_id);

		text_srcPath.setEditable(false);

		Label label = new Label(container, SWT.NONE);
		label.setText("包名称(可输入空格,代表根路径)");

		text_packName = new Text(container, SWT.BORDER);
		final GridData gd_text_name = new GridData(SWT.FILL, SWT.CENTER, true, false);
		text_packName.setLayoutData(gd_text_name);
		text_packName.addModifyListener(new ModifyListener() {
             public void modifyText(ModifyEvent e) {
                    Text text=(Text)e.getSource();
                    String name=text.getText();
                    name = name.replace(".", "/");
                    IFolderDelegate folder = null;
					try {
						folder = node.getResource().getProject().getFolder(text_srcPath.getText()+"/"+name.trim());
						if(folder.exists() && name!=""){
							updateStatus("包路径已存在,请重命名.", false);
						}else if(name.length()==0){
							updateStatus("请输入新的包路径", false);
						}else{
							updateStatus(null, true);
						}
						   
					} catch (ResourceException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//IFolderDelegate folder = node.getResource().getSourceFolder().getFolder(name);
					
             }
		});
		text_packName.setFocus();
		setControl(container);
		updateData();
	}
	
	private void updateData(){
		if(this.node instanceof AbstractFolderNode)
		{
			this.text_srcPath.setText(this.node.getConfigPath());
		}
	}
	
	public String fetchData(){
		return this.text_packName.getText();
	}

	private void updateStatus(String message, boolean canComplete) {
		setErrorMessage(message);
		setPageComplete(canComplete);
	}
}
