package avicit.ui.platform.common.data;

import org.apache.commons.lang.StringUtils;

import avicit.ui.common.util.ICloneable;

public final class ListDataField implements ICloneable{
	private static final long serialVersionUID = 8312139864854170366L;

	private String dateformat = "";
	private boolean hidden = false;
	private String mapping = "";

	private String dataIndex = "";
	private String header = "";
	private boolean search =false;
	
	public ListDataField() {
	}

	public Object clone(){
		ListDataField df = new ListDataField();
		
		return df;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ListDataField)
			return StringUtils.equals(this.dataIndex, ((ListDataField)obj).dataIndex);
		return false;
	}

	public String getDateformat() {
		return dateformat;
	}

	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}

	public boolean getHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public String getMapping() {
		return mapping;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	public String getDataIndex() {
		return dataIndex;
	}

	public void setDataIndex(String dataIndex) {
		this.dataIndex = dataIndex;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public boolean getSearch() {
		return search;
	}

	public void setSearch(boolean search) {
		this.search = search;
	}



}