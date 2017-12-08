/**
 * 
 */
package avicit.ui.platform.common.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

import avicit.ui.platform.common.util.WebrootUtil;

public class ActionButtonSelectionDialog extends TreeViewerSelectionDialog {

	public ActionButtonSelectionDialog(Shell parentShell,
			String statusMessage, int style) {
		
		super(parentShell, statusMessage, style|SWT.MULTI | SWT.CHECK);

		this.setContentProvider(new ArrayTreeContentProvider());
		this.setLabelProvider(new LabelProvider());
		this.setTitle("ѡ�������ť");
	}

	@Override
	protected Object findInputElement() {
		List commands = WebrootUtil.getAllButtons();
		return commands;
	}

	@Override
	protected boolean isValidSelection(Object selection) {
		return true;
	}
	@Override
	protected void okPressed() {
		
		List chosenData = new ArrayList();
		TreeItem[] items = getTreeViewer().getTree().getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].getChecked())
				chosenData.add(items[i].getData());
		}

		setResult(chosenData);
		setReturnCode(OK);
		close();
		
	}
	
}