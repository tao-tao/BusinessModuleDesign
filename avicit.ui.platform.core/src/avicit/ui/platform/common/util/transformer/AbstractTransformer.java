package avicit.ui.platform.common.util.transformer;

public abstract class AbstractTransformer implements IPropTransformer {

	public AbstractTransformer(String propName) {
		this.propName = DataTransformBean.shiftCapital(propName);
	}

	private String propName;

	public String getPropName() {
		return this.propName;
	}

}
