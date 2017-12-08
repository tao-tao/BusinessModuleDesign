package avicit.ui.platform.common.util.transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.beanutils.PropertyUtils;

public class DataTransformBean {

	private Object obj;

	private ArrayList<String> propslist = new ArrayList<String>();

	private HashMap<String, IPropTransformer> transformers = new HashMap<String, IPropTransformer>();

	protected static String shiftCapital(String propname) {
		if (propname.equals(""))
			return propname;
		char fc = propname.charAt(0);
		if (Character.isUpperCase(fc)) {
			propname = Character.toLowerCase(fc) + propname.length() > 1 ? propname.substring(1) : "";
		}
		return propname;
	}

	public DataTransformBean(Object obj, String[] props, IPropTransformer[] propTransformers) {

		HashSet<String> propSet = new HashSet<String>();

		if (null != obj) {
			this.obj = obj;
		}

		if (null != props) {
			for (int i = 0; i < props.length; i++) {
				String propname = shiftCapital(props[i]);
				if (propSet.add(propname)) {
					this.propslist.add(propname);
				}
			}
		}

		if (null != propTransformers) {
			for (int i = 0; i < propTransformers.length; i++) {
				String propName = shiftCapital(propTransformers[i].getPropName());
				if (propSet.add(propName)) {
					this.propslist.add(propName);
				}
				this.transformers.put(propName, propTransformers[i]);
			}
		}
	}

	public void pullOutData(Object destSimpleBean) {
		for (String prop : propslist) {
			Object value = this.getValue(prop);
			try {
				PropertyUtils.setSimpleProperty(destSimpleBean, prop, value);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void pushInData(Object srcSimpleBean) {
		for (String prop : propslist) {
			Object value;
			try {
				value = PropertyUtils.getSimpleProperty(srcSimpleBean, prop);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			this.setValue(prop, value);
		}
	}

	public static int StringToInt(String str) {
		Integer data = 0;
		try {
			data = new Integer(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static boolean StringToBoolean(String str) {
		boolean va = false;
		try {
			va = new Boolean(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return va;
	}

	private void checkObjNotNull() {
		if (null == this.obj) {
			throw new RuntimeException("obj cannot be null");
		}
	}

	private boolean isTransformedProperty(String prop) {
		return this.transformers.containsKey(prop);
	}

	private Object getValue(String prop) {
		Object value;
		if (this.isTransformedProperty(prop)) {
			value = this.transformers.get(prop).getPropValue();
		} else {
			this.checkObjNotNull();
			try {
				value = PropertyUtils.getSimpleProperty(this.obj, prop);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return value;
	}

	private void setValue(String prop, Object value) {
		if (this.isTransformedProperty(prop)) {
			this.transformers.get(prop).setPropValue(value);
		} else {
			this.checkObjNotNull();
			try {
				PropertyUtils.setSimpleProperty(this.obj, prop, value);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void pullOutData(DataTransformBean destBean) {
		for (String prop : propslist) {
			Object value = this.getValue(prop);
			destBean.setValue(prop, value);
		}
	}

	public void pushInData(DataTransformBean srcBean) {
		srcBean.pullOutData(this);
	}

}
