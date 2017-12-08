package com.tansun.data.db.visual.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import avicit.ui.platform.db.DBTable;

import com.tansun.data.db.DBPlugin;
import com.tansun.data.db.visual.editor.VisualDBEditor;
import com.tansun.data.db.visual.editpart.TableEditPart;
import com.tansun.data.db.visual.model.TableModel;

public class EditeAction extends SelectionAction {
	private List<TableEditPart> ls = new ArrayList<TableEditPart>();
	private List<DBTable> lt = new ArrayList<DBTable>();

	public EditeAction(VisualDBEditor editor) {
		super(editor);
		// setAccelerator(SWT.CTRL | 'm');
		setId(EditeAction.class.getName());
		// setActionDefinitionId(MakeJavaAction.class.getName());
		setText(DBPlugin.getResourceString("action.Eh"));

	}

	@Override
	public void run() {

		List<Object> selection = getSelectedObjects();
		for (int i = 0; i < selection.size(); i++) {
			Object obj = selection.get(i);
			if (obj instanceof TableEditPart) {

				// ((TableEditPart)
				// obj).getFigure().setBackgroundColor(ColorConstants.green);
				TableModel tmodel = (TableModel) ((TableEditPart) obj)
						.getModel();

				openFile(tmodel);
			}
		}

	}
	

	public void openFile(TableModel model) {
		try {
			
			
			
			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(model.getProjectName());
			IFile file = project.getFile(getPath(model.getHbmPath()));
			IDE.openEditor(page, file);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	public void setInfo(){
		
	}

	public String getPath(String con){
		String s=con.substring(1);
		if(s.contains("/"))
		return s.substring(s.indexOf("/"));
		else
			return s;
	}
	@SuppressWarnings("unchecked")
	protected boolean calculateEnabled() {
		List<Object> selection = getSelectedObjects();
		for (int i = 0; i < selection.size(); i++) {
			Object obj = selection.get(i);
			if (obj instanceof TableEditPart) {
				return true;
			}
		}
		return false;
	}
}
