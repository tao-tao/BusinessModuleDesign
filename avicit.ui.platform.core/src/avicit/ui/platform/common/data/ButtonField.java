package avicit.ui.platform.common.data;

import org.apache.commons.lang.StringUtils;

import avicit.ui.runtime.util.ICloneable;

public final class ButtonField implements ICloneable{
	private static final long serialVersionUID = 8312139864854170366L;

	private String _type = "";
	private String _xtype = "";

	private String _name = "";
	private String _text = "";
	private String _event = "click";
	private String _ref = "";
	
	public ButtonField() {
	}

	public ButtonField(String name) {
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
	public ButtonField(String name, String type) {
		this(name);
		setType(type);
	}

	public Object clone(){
		ButtonField df = new ButtonField(this._name, this._type);
		df._text = this._text;
		df._xtype = this._xtype;
		
		return df;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ButtonField)
			return (StringUtils.equals(this._name, ((ButtonField)obj)._name)) && (StringUtils.equals(this._event, ((ButtonField)obj)._event));
		return false;
	}

	public final String getName() {
		return _name;
	}

	public final void setName(String name) {
		_name = name;
	}

	public final String getRef() {
		return _ref;
	}

	public final void setRef(String name) {
		_ref = name;
	}

	public final String getEvent() {
		return _event;
	}

	public final void setEvent(String name) {
		_event = name;
	}

	public String getType() {
		return _type;
	}

	public void setType(String _data) {
		this._type = _data;
	}

	public String getXtype() {
		return _xtype;
	}

	public void setXtype(String _xtype) {
		this._xtype = _xtype;
	}

	public String getText() {
		return _text;
	}

	public void setText(String _label) {
		this._text = _label;
	}

}