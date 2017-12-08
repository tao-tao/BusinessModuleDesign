/**
 * 
 */
package net.java.amateras.uml.usecasediagram.property;

import java.awt.event.MouseAdapter;

import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * @author shida
 * 
 */
public class MyPropertyDescriptor extends PropertyDescriptor {
	private String id;
	private String value;
	private String newcontent;
	public String getValue() {
		return value;
	}

	public MyPropertyDescriptor setValue(String value) {
		this.value = value;
		return this;
	}

	public MyPropertyDescriptor(Object id, String displayName, String value) {
		super(id, displayName);
		this.id = (String) displayName;
		this.value = value;

	}

	public CellEditor createPropertyEditor(Composite parent) {
		ImageSelectionDialogCellEditor editor = new ImageSelectionDialogCellEditor(
				parent);
		if (getValidator() != null) {
			editor.setValidator(getValidator());
		}

		return editor;
	}

	/**
	 * このPropertyDescriptorで使用するセルエディタ。 ListEditDialogを使用してリストの編集を行います。
	 */
	private class ImageSelectionDialogCellEditor extends DialogCellEditor {
		Shell s;
		Text textArea;

		public ImageSelectionDialogCellEditor(Composite parent) {
			super(parent);
		}

		protected Object openDialogBox(Control cellEditorWindow) {
			/*UseCaseAcDialog dialog=new UseCaseAcDialog(cellEditorWindow.getShell(),id,value);
			if (dialog.open() == Dialog.OK) {
				return dialog.getString();
			}*/
			return null;
			
		}

	}
}
