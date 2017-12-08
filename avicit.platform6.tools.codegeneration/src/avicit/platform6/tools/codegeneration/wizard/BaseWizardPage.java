package avicit.platform6.tools.codegeneration.wizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.osgi.framework.Bundle;

import avicit.platform6.tools.codegeneration.CodeGenerationActivator;


/**
 * <p>金航数码科技有限责任公司</p>
 * <p>作者：dingrc</p>
 * <p>邮箱：dingrc@avicit.com</p>
 * <p>创建时间：2012-12-10</p>
 *
 * <p>类说明：向导基类</p>
 * <p>修改记录：</p>
 */
public abstract class BaseWizardPage extends WizardPage {
	// 首选项
	protected IPreferenceStore codeStore;

	protected BaseWizardPage(String pageName) {
		super(pageName);
		codeStore = CodeGenerationActivator.getDefault().getPreferenceStore();
	}
	protected int getMaxFieldWidth() {
		return convertWidthInCharsToPixels(40);
	}
	protected CodeGenerationWizard getRealWizard() {
		IWizard wizard = getWizard();
		return (CodeGenerationWizard) wizard;
	}
	protected IProject getProject() {
		return getRealWizard().getProject();
	}
	protected Bundle getBundle() {
		return CodeGenerationActivator.getDefault().getBundle();
	}
	
	protected IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	
	protected IJavaProject getJavaProject() {
		// IPackageFragmentRoot root= getPackageFragmentRoot();
		// if (root != null) {
		// return root.getJavaProject();
		// }
		return JavaCore.create(getProject());
	}

}
