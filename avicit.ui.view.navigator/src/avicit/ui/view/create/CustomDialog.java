package avicit.ui.view.create;


import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public abstract class CustomDialog extends TitleAreaDialog {

	public  String beforeMessage;
	public Image image;
	public String titleName;
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
	 * ���� Javadoc��
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.TitleAreaDialog#createContents(org.eclipse.
	 * swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		super.createContents(parent);
		this.getShell().setText(this.DialogName);// ���öԻ��������
		this.setTitle(this.titleName);
		this.setMessage(this.getInitMessage(), IMessageProvider.INFORMATION);// ���ó�ʼ���Ի������ʾ��Ϣ
		if(this.getImage()!=null)
		this.setTitleImage(this.getImage());
		return parent;
	}

	protected Control createDialogArea(Composite composite) {
		this.crateArea(composite);
		return composite;
	}

	public abstract void crateArea(Composite parent);
	public void checkValid() {
		
	}
}
