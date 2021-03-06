package avicit.ui.runtime.core.action;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.common.util.AdapterUtil;
import avicit.ui.common.util.WorkbenchUtil;

public class OpenDataModelAction implements IDoubleClickListener{

	public void doubleClick(DoubleClickEvent anEvent) {
		IStructuredSelection selection = (IStructuredSelection)anEvent.getSelection();
		Object ele = selection.getFirstElement();
		IProject pro = (IProject) AdapterUtil.getAdapter(ele, IProject.class);
		IFile file = pro.getFile(new Path("WebRoot/WEB-INF/springxml/applicationContext-common.xml"));
		if(file != null)
		{
			WorkbenchUtil.openFile(WorkbenchUtil.getWorkbenchPage(), file, WorkbenchUtil.EC_BOEDITOR_ID);
		}
	}

}
