package avicit.ui.platform.common.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TextEditDialog extends Dialog{

	private Text text;
	private String txt;
	private Button hiddenButton;
	
	protected TextEditDialog(Shell parentShell, String txt) {
		super(parentShell);
		this.txt = txt;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setLayout(new GridLayout());
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout());
		text = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		text.setText(txt);

		hiddenButton = new Button(comp, SWT.PUSH);
		hiddenButton.setVisible(false);
		hiddenButton.setEnabled(false);
		return comp;
	}

	@Override
	protected void okPressed() {
		txt = text.getText();
		super.okPressed();
	}

	protected void createButtonsForButtonBar(Composite parent) {
	    super.createButtonsForButtonBar(parent);

		parent.getShell().setDefaultButton(hiddenButton);
	  }

	@Override
	protected Point getInitialSize() {
		return new Point(500, 400);
	}

	public String getText(){
		return txt;
	}

}
