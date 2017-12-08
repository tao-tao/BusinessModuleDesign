package avicit.ui.platform.common.util.transformer;

import org.apache.commons.beanutils.PropertyUtils;

public class IntegerStringTransformer extends AbstractTransformer {

	private Object obj;

	public IntegerStringTransformer(String propName, Object obj) {
		super(propName);
		this.obj = obj;
	}

	public Object getPropValue() {
		Integer value = null;
		try {
			value = (Integer) PropertyUtils.getSimpleProperty(this.obj, this
					.getPropName());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return value.toString();
	}

	public void setPropValue(Object value) {
		Integer va = DataTransformBean.StringToInt((String) value);
		try {
			PropertyUtils.setSimpleProperty(this.obj, this.getPropName(), va);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
