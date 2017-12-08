package avicit.ui.platform.common.util.transformer;

import org.eclipse.swt.widgets.Button;

public class SWTCheckButtonTransformer extends AbstractTransformer {
	Button checkb;
	public SWTCheckButtonTransformer(String propName,Button checkb) {
		super(propName);
		this.checkb = checkb;
	}

	public Object getPropValue() {
		return this.checkb.getSelection();
	}

	public void setPropValue(Object value) {
		this.checkb.setSelection((Boolean) value);
	}

}
