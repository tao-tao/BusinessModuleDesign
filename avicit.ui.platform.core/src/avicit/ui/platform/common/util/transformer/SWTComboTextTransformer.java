package avicit.ui.platform.common.util.transformer;

import org.eclipse.swt.widgets.Combo;

public class SWTComboTextTransformer extends AbstractTransformer {

	private Combo combo;

	public SWTComboTextTransformer(String propName, Combo combo) {
		super(propName);
		this.combo = combo;
	}

	public Object getPropValue() {
		return null;
	}

	public void setPropValue(Object value) {
		if(null!=value){
			this.combo.setText((String) value);
		}
	}

}
