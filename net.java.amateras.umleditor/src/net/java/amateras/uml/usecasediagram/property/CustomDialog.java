package net.java.amateras.uml.usecasediagram.property;

import net.java.amateras.uml.usecasediagram.model.UsecaseModel;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public  class CustomDialog extends TitleAreaDialog {

	public  String beforeMessage;
	public Image image;
	public String titleName;
	public Text composite;
	public String value;
	//public ProcessCpt pcpt;
	public String getBeforeMessage() {
		return beforeMessage;
	}

	public void setBeforeMessage(String beforeMessage) {
		this.beforeMessage = beforeMessage;
	}

	public String DialogName;
	public String initMessage;
	public String getInitMessage() {
		return initMessage;
	}

	public void setInitMessage(String initMessage) {
		this.initMessage = initMessage;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public String getDialogName() {
		return DialogName;
	}

	public void setDialogName(String dialogName) {
		DialogName = dialogName;
	}

	public CustomDialog(Shell parentShell) {
		super(parentShell);
		
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.TitleAreaDialog#createContents(org.eclipse.
	 * swt.widgets.Composite)
	 */
	
	protected Control createContents(Composite parent) {
		super.createContents(parent);
		this.getShell().setText(this.DialogName);// 设置对话框标题栏
		this.setTitle(this.titleName);// 设置标题信息
		this.setMessage(this.getInitMessage(), IMessageProvider.INFORMATION);// 设置初始化对话框的提示信息
		if(this.getImage()!=null)
		this.setTitleImage(this.getImage());
		return parent;
	}
	public void checkValid() {
		
	}
	
}
