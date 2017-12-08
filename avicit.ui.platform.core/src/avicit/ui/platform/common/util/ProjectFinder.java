package avicit.ui.platform.common.util;

import org.apache.log4j.Logger;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.part.FileEditorInput;

import avicit.ui.platform.common.data.IContainsProject;

//import com.tansun.workflow.designer.editor.DesignerEditor;

public class ProjectFinder {
	/**
	 * Logger for this class
	 */
	static final Logger logger = Logger.getLogger(ProjectFinder.class);

	public static IJavaProject getCurrentJavaProject() {
		IJavaProject jp = null;
		try {
			jp = JavaCore.create(PlatformHelper.getCurrentFileByActiveEditor().getProject());
		} catch (Exception e) {
			logger.warn("getCurrentJavaProject() - e=" + e, e); //$NON-NLS-1$
		}
		return jp;
	}
	
	public static IProject getProjectByLookup(Control cpt){
		IContainsProject p = getProjectCpt(cpt);
		if(null==p){
			throw new RuntimeException("cannot find IContainsProject by lookup");
		}else{
			if (logger.isDebugEnabled()) {
				logger.debug("getProjectByLookup(Control) - find project:" + p.getProject().getName());
			}

			return p.getProject();
		}
	}

	private static IContainsProject getProjectCpt(Control cpt) {
		if(null==cpt ){
			return null;
		}else if(cpt instanceof IContainsProject){
			return (IContainsProject) cpt;
		}else{
			return getProjectCpt(cpt.getParent());
		}
		
	}

	public static IProject getCurrentProjectByActiveEditor() {
		IProject pro = null;
		try {
			pro = PlatformHelper.getCurrentFileByActiveEditor().getProject().getProject();
		} catch (Exception e) {
			logger.warn("getCurrentProject() - e=" + e, e); //$NON-NLS-1$
		}
		return pro;
	}
}
