package net.java.amateras.uml.usecasediagram.property;

import org.w3c.dom.Node;

public final class DataField implements ICloneable{
	private static final long serialVersionUID = 8312139864854170366L;

	//_name, _type, _unit, _property, _fieldSource,_isNeed,_initialValue,_fieldScope,_OutDestination
	//�ֶ����
	private String name;
	//�ֶγ��Ⱥ�����
	private String type;
	//�ֶε�λ
	private String unit;
	//����/�������
	private int property;
	//�ֶ�ֵ��Դ
	private int fieldSource;
	//�Ƿ��������
	private int isNeed;
	//�ֶγ�ʼֵ
	private String initialValue;
	//�ֶ�ֵ��Χ���б�
	private String fieldScope;
	//���Ŀ�ĵ�
	private int OutDestination;
	
	
	public DataField() {
	}

	public DataField(String name) {
		this.name = name;
	}

	/**
	 * Construct a new DataField.
	 * 
	 * @param id
	 *            The data field id
	 * @param name
	 *            The data field name
	 * @param dataType
	 *            The data type
	 */
//	public DataField(String name, String dataType) {
//		this(name);
//		setDataType(dataType);
//	}

	public Object clone(){
		DataField df = new DataField(this.name);
		df.setName(getName());
		df.setType(getType());
		df.setUnit(getUnit());
		df.setProperty(getProperty());
		df.setFieldSource(getFieldSource());
		df.setIsNeed(getIsNeed());
		df.setInitialValue(getInitialValue());
		df.setFieldScope(getFieldScope());
		df.setOutDestination(getOutDestination());
		return df;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getFieldSource() {
		return fieldSource;
	}

	public void setFieldSource(int fieldSource) {
		this.fieldSource = fieldSource;
	}
	
	public int getProperty() {
		return property;
	}

	public void setProperty(int property) {
		this.property = property;
	}

	public int getIsNeed() {
		return isNeed;
	}

	public void setIsNeed(int isNeed) {
		this.isNeed = isNeed;
	}

	public String getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(String initialValue) {
		this.initialValue = initialValue;
	}

	public String getFieldScope() {
		return fieldScope;
	}

	public void setFieldScope(String fieldScope) {
		this.fieldScope = fieldScope;
	}

	public int getOutDestination() {
		return OutDestination;
	}

	public void setOutDestination(int outDestination) {
		OutDestination = outDestination;
	}

	

	
}