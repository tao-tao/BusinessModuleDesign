package avicit.ui.platform.common.util.transformer;

import org.eclipse.swt.widgets.Combo;

public class SWTComboTransformer extends AbstractTransformer {

	private Combo combo;

	public SWTComboTransformer(String propName, Combo combo) {
		super(propName);
		this.combo = combo;
	}

	public Object getPropValue() {
		return this.combo.getSelectionIndex();
	}

	public void setPropValue(Object value) {
		Integer index = (Integer) value;
		this.combo.select(index);
	}

}
