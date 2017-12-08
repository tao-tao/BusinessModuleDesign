package avicit.ui.platform.common.util.transformer;

import org.eclipse.swt.widgets.Button;

public class SWTRadioTransformer extends AbstractTransformer {

	private Button yesButton;
	private Button noButton;

	public SWTRadioTransformer(String propName, Button yesButton,
			Button noButton) {
		super(propName);
		this.yesButton = yesButton;
		this.noButton = noButton;
	}

	public Object getPropValue() {
		return this.yesButton.getSelection();
	}

	public void setPropValue(Object value) {
		Boolean yes = (Boolean) value;
		if (yes) {
			this.yesButton.setSelection(true);
			this.noButton.setSelection(false);
		} else {
			this.yesButton.setSelection(false);
			this.noButton.setSelection(true);
		}
	}

}
