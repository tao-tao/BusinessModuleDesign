package avicit.platform6.tools.codegeneration.wizard;


import org.apache.commons.lang.StringUtils;


public final class DataField implements ICloneable{
	private static final long serialVersionUID = 8312139864854170366L;

	private String _dataType = "";
	private String _data = "";
	private String _xtype = "";
	private boolean _hidden = false;
	private String _name = "";
	private String _label = "";
	
	public DataField() {
	}

	public DataField(String name) {
		_name = name;
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
	public DataField(String name, String dataType) {
		this(name);
		setDataType(dataType);
	}

	public Object clone(){
		DataField df = new DataField(this._name, this._dataType);
		df._data = this._data;
		df._dataType = this._dataType;
		df._label = this._label;
		df._name = this._name;
		df._xtype = this._xtype;
		
		return df;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DataField)
			return StringUtils.equals(this._name, ((DataField)obj)._name);
		return false;
	}

	public final String getName() {
		return _name;
	}

	public final void setName(String name) {
		_name = name;
	}

	public String getDataType() {
		return _dataType;
	}

	public void setDataType(String type) {
		_dataType = type;
	}

	public String getData() {
		return _data;
	}

	public void setData(String _data) {
		this._data = _data;
	}

	public String getXtype() {
		return _xtype;
	}

	public void setXtype(String _xtype) {
		this._xtype = _xtype;
	}

	public String getFieldLabel() {
		return _label;
	}

	public void setFieldLabel(String _label) {
		this._label = _label;
	}

	public boolean getHidden() {
		return _hidden;
	}

	public void setHidden(boolean hidden) {
		this._hidden = hidden;
	}

}