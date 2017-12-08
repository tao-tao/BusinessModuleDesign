package avicit.ui.platform.common.util.transformer;

import org.apache.commons.beanutils.PropertyUtils;

public class BooleanStringTransformer extends AbstractTransformer {
	Object obj;

	public BooleanStringTransformer(String propName, Object obj) {
		super(propName);
		this.obj = obj;
	}

	public Object getPropValue() {
		Boolean value = false;
		try {
			value = (Boolean) PropertyUtils.getSimpleProperty(this.obj, this
					.getPropName());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return value.toString();
	}

	public void setPropValue(Object value) {
		Boolean va = DataTransformBean.StringToBoolean((String) value);
		try {
			PropertyUtils.setSimpleProperty(this.obj, this.getPropName(), va);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
