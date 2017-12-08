package avicit.ui.platform.common.ui;

import java.text.MessageFormat;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public abstract class TextDialogCellEditor extends DialogCellEditor {
	protected Text _text;

	/**
	 * 
	 */
	public TextDialogCellEditor() {
		super();
	}

	/**
	 * @param parent
	 */
	public TextDialogCellEditor(Composite parent) {
		super(parent);
	}

	/**
	 * @param parent
	 * @param style
	 */
	public TextDialogCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite cell) {
		_text = new Text(cell, SWT.LEFT);
		_text.setFont(cell.getFont());
		_text.setEditable(true);
		_text.setBackground(cell.getBackground());
		_text.addKeyListener(new KeyAdapter() {
			// hook key pressed - see PR 14201
			public void keyPressed(KeyEvent e) {
				keyReleaseOccured(e);
				// disposed this cell editor
				if ((getControl() == null) || getControl().isDisposed()) {
					return;
				}
			}
		});
		//�༭��Ϊֻ��
		_text.setEditable(false);
		// when the text control has focus, the cellEditor will deactive even
		// when you press the button.
		// Add the follow codes enable switch to the button control.
		//������ͱ༭�򶼿�����������
//		_text.addFocusListener(new FocusAdapter() {
//			public void focusLost(FocusEvent e) {
//				boolean newValidState = isCorrect(_text.getText());
//				if (newValidState) {
//					markDirty();
//					doSetValue(_text.getText());
//				} else {
//					// try to insert the current value into the error message.
//					setErrorMessage(MessageFormat.format(getErrorMessage(),
//							new Object[] { _text.getText().toString() }));
//				}
//			}
//		});

		return _text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	protected void doSetFocus() {
		_text.setFocus();
		_text.selectAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#updateContents(java.lang.Object)
	 */
	protected void updateContents(Object value) {
		if (_text == null || _text.isDisposed()) {
			return;
		}

		String text = "";//$NON-NLS-1$
		if (value != null) {
			text = value.toString();
		}
		_text.setText(text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#keyReleaseOccured(org.eclipse.swt.events.KeyEvent)
	 */
	protected void keyReleaseOccured(KeyEvent keyEvent) {
		if (keyEvent.character == '\r') {
			boolean newValidState = isCorrect(_text.getText());
			if (newValidState) {
				markDirty();
				doSetValue(_text.getText());
			} else {
				// try to insert the current value into the error message.
				setErrorMessage(MessageFormat.format(getErrorMessage(), new Object[] { _text.getText().toString() }));
			}
			fireApplyEditorValue();
		}
		super.keyReleaseOccured(keyEvent);
	}
}
