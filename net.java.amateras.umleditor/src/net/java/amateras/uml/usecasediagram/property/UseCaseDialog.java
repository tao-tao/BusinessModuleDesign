package net.java.amateras.uml.usecasediagram.property;

import java.util.ArrayList;
import java.util.List;

import net.java.amateras.uml.usecasediagram.model.UsecaseModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class UseCaseDialog extends CustomDialog {
	
	
	private static Shell shell;
	ProcessCpt pcpt;
	UsecaseModel model;
	Text textArea;
	
	public UseCaseDialog(Shell parentShell, UsecaseModel model) {
		super(shell);

		this.model=model;
		this.value=model.getInput().getInputname();
		//dialog 显示用例名称
		this.setInitMessage(model.getName());
		this.setBeforeMessage(model.getName());
		this.setDialogName(model.getName());
		this.setTitleName(model.getName());
	}
	
	protected int getShellStyle(){
		
		return super.getShellStyle()|SWT.RESIZE|SWT.MAX;
	}

	
	
	public String getString(){
		return value;
	}
	@Override
	protected void okPressed() {
		// TODO Auto-generated method stub
		//model.setName(this.composite.getText());
		//model.getInput().getContent().add(this.composite.getText());
		pcpt.onBnClick_Apply();
		super.okPressed();
	}
	protected Control createDialogArea(Composite parent) {
		Composite my=(Composite)super.createDialogArea(parent);
		//update by lidong
		pcpt = new ProcessCpt(my, SWT.NONE,model);
		
		pcpt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite = new Text(my, SWT.BORDER|SWT.H_SCROLL|SWT.V_SCROLL);
		GridData fd=new GridData(SWT.FILL,SWT.FILL,false,false,1,1);
		fd.heightHint=205;
		composite.setLayoutData(fd);
		composite.setText(value);
		
		return pcpt;
	}	
}

