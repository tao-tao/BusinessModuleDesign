package avicit.ui.platform.common.ui;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class TextSelector extends TextDialogCellEditor {
	public TextSelector(Composite parent) {
		super(parent);
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DialogCellEditor#updateContents(java.lang.Object)
	 */
	protected void updateContents(Object value) {
		
		if (_text == null || _text.isDisposed()) {
			return;
		}
		if(value != null && value instanceof String)
		{
			_text.setText((String)value);
		}
	}

	protected Object openDialogBox(Control cellEditorWindow) {
		Shell shell = cellEditorWindow.getShell();
		TextEditDialog dialog = new TextEditDialog(shell, _text.getText());
		if (dialog.open() == Window.OK) {
			return dialog.getText();
		}
		return null;
	}
}
