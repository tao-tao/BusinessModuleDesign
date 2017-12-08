package avicit.ui.runtime.core.action;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import avicit.ui.common.util.IPlatformConstants;
import avicit.ui.common.util.WorkbenchUtil;
import avicit.ui.runtime.core.node.CommonChildNode;
import avicit.ui.runtime.core.node.CommonNode;
import avicit.ui.runtime.core.requirement.analysis.epc.EpcNode;

public class OpenEpcNodeAction  implements IDoubleClickListener{
	
	public void doubleClick(DoubleClickEvent anEvent) {
		IStructuredSelection selection = (IStructuredSelection)anEvent.getSelection();
		Object ele = selection.getFirstElement();
		if(ele instanceof EpcNode)
		{
			EpcNode child = (EpcNode)ele;
			IFile f = (IFile) child.getFile().getResource();
			try{

//				WorkbenchUtil.openFileInNewEditor(WorkbenchUtil.getWorkbenchPage(), null, child.getEditorID());
				WorkbenchUtil.openFileInNewEditor(WorkbenchUtil.getWorkbenchPage(), f, child.getEditorID());
			}catch(Exception e){
				MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "提示", "此操作需要数据库支持,Spring数据库配置有误,请检查" + IPlatformConstants.SpringFile_Platform + ".");
			}
		}
		else if(ele instanceof CommonNode)
		{
			CommonNode child = (CommonNode)ele;
			IFile f = (IFile) child.getCommonFile();
			WorkbenchUtil.openFileInNewEditor(WorkbenchUtil.getWorkbenchPage(), f, IPlatformConstants.ID_EcPlatformEditor);
		}
	}	

}
