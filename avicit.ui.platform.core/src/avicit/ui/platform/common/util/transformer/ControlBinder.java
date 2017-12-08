/**
 * 
 */
package avicit.ui.platform.common.util.transformer;

import org.eclipse.swt.widgets.Control;

public class ControlBinder {
	Control ctl;
	String prop;

	public Control getCtl() {
		return ctl;
	}

	public void setCtl(Control ctl) {
		this.ctl = ctl;
	}

	public String getProp() {
		return prop;
	}

	public void setProp(String prop) {
		this.prop = prop;
	}

	public ControlBinder(Control ctl, String prop) {
		this.ctl = ctl;
		this.prop = prop;

	}
}