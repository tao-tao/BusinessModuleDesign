package avicit.ui.platform.common.util.transformer;

import org.eclipse.swt.widgets.Spinner;

public class SWTSpinnerTransformer extends AbstractTransformer {
	Spinner spinner;
	public SWTSpinnerTransformer(String propName,Spinner spinner) {
		super(propName);
		this.spinner = spinner;
	}

	public Object getPropValue() {
		return this.spinner.getSelection();
	}

	public void setPropValue(Object value) {
		this.spinner.setSelection((Integer) value);
	}

}
