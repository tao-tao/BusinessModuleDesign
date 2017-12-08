package net.java.amateras.uml.usecasediagram.property;

import net.java.amateras.uml.usecasediagram.model.UsecaseActorModel;
import net.java.amateras.uml.usecasediagram.model.UsecaseModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class UseCaseAcDialog extends CustomDialog {
	
	
	private static Shell shell;
	UsecaseActorModel model;
	UsecaseActorModel selection;
	Text textArea;
	ActorProcessCpt pcpt;
	public UseCaseAcDialog(Shell parentShell,UsecaseActorModel amodel) {
		super(shell);
		
		this.selection=selection;
		this.value=value;
		this.model=amodel;
		this.setInitMessage(amodel.getName());
		this.setBeforeMessage(amodel.getName());
		this.setDialogName(amodel.getName());
		this.setTitleName(amodel.getName());
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
		pcpt.onBnClick_Apply();
		super.okPressed();
	}
	protected Control createDialogArea(Composite parent) {
		Composite my=(Composite)super.createDialogArea(parent);
		pcpt = new ActorProcessCpt(my, SWT.NONE,model);
		
		pcpt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		return pcpt;
	}	
}

