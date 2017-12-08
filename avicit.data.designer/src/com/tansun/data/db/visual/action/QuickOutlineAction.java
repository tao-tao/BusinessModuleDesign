/*package com.tansun.data.db.visual.action;


import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

import com.tansun.data.db.DBPlugin;
import com.tansun.data.db.util.UIUtils;
import com.tansun.data.db.visual.editor.VisualDBInformationControl;

public class QuickOutlineAction extends Action {

	public QuickOutlineAction() {
		super(DBPlugin.getResourceString("action.quickOutline"));
		setAccelerator(SWT.CTRL | 'O');
		setId(QuickOutlineAction.class.getName());
		setActionDefinitionId(QuickOutlineAction.class.getName());
	}

	@Override
	public void run(){
		GraphicalViewer viewer = (GraphicalViewer)
			UIUtils.getActiveEditor().getAdapter(GraphicalViewer.class);

		VisualDBInformationControl quickOutline = new VisualDBInformationControl(
				viewer.getControl().getShell(), viewer);

		quickOutline.setVisible(true);
	}

}
*/