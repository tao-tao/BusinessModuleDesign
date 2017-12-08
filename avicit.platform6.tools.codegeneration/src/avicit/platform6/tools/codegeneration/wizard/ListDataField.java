package avicit.platform6.tools.codegeneration.wizard;


import org.apache.commons.lang.StringUtils;


public final class ListDataField implements ICloneable{
	private static final long serialVersionUID = 8312139864854170366L;

	private String dateformat = "";
	private boolean hidden = false;
	private String mapping = "";
	private int version;
	
	private String[] hiddenArray = new String[2];
	
	
	private int clstype;
	
	private int selecttype;

	

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

	public String[] getHiddenArray() {
		return hiddenArray;
	}

	public void setHiddenArray(String[] hiddenArray) {
		this.hiddenArray = hiddenArray;
	}

	public int getClstype() {
		return clstype;
	}

	public void setClstype(int clstype) {
		this.clstype = clstype;
	}

	public int getSelecttype() {
		return selecttype;
	}

	public void setSelecttype(int selecttype) {
		this.selecttype = selecttype;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}