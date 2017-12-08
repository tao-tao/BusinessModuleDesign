package avicit.ui.runtime.core.action;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;

import avicit.ui.common.util.AdapterUtil;
import avicit.ui.common.util.WorkbenchUtil;

public class OpenErAction implements IDoubleClickListener{

	public void doubleClick(DoubleClickEvent anEvent) {
		IStructuredSelection selection = (IStructuredSelection)anEvent.getSelection();
		Object ele = selection.getFirstElement();
		IFile file = (IFile) AdapterUtil.getAdapter(ele, IFile.class);
		if(file != null)
		{
			WorkbenchUtil.openFile(WorkbenchUtil.getWorkbenchPage(), file,"com.tansun.data.designer.editor1" );
		}
	}

}
