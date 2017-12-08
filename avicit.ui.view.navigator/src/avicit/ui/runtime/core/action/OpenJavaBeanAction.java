package avicit.ui.runtime.core.action;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PartInitException;

import avicit.ui.runtime.core.node.JavaBeanNode;

public class OpenJavaBeanAction implements IDoubleClickListener{

	public void doubleClick(DoubleClickEvent anEvent) {
		IStructuredSelection selection = (IStructuredSelection)anEvent.getSelection();
		JavaBeanNode ele = (JavaBeanNode) selection.getFirstElement();
		ICompilationUnit cu;
		try {
			cu = ele.getCU();
			if(cu != null && cu.exists())
			{
				JavaUI.openInEditor(cu);
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}

}
