package avicit.ui.platform.common.data;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

public class DefaultProjectCpt extends SashForm implements IContainsProject {

	private IProject pro;

	public DefaultProjectCpt(Composite parent, int style) {
		super(parent, style);
	}

	public IProject getProject() {
		return this.pro;
	}
	

	public void setProject(IProject pro) {
		this.pro = pro;
	}

}
