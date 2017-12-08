package com.tansun.data.db.visual.model;

import java.io.Serializable;

import com.tansun.data.db.dialect.IColumnType;


public class ColumnModel implements Serializable {
	
	private String columnName = "";
	private String logicalName = "";
	private IColumnType columnType = null;
	private String size = "";
	private boolean notNull = false;
	private boolean primaryKey = false;
	private String description = "";
	private boolean autoIncrement = false;
	private String defaultValue = "";
	private DommainModel dommain = null;
	private int digits;
	public int getDigits() {
		return digits;
	}

	public void setDigits(int digits) {
		this.digits = digits;
	}

	public String getLogicalName(){
		return this.logicalName;
	}
	
	public void setLogicalName(String logicalName){
		this.logicalName = logicalName;
	}
	
	public String getColumnName() {
		return columnName;
	}
	
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public IColumnType getColumnType() {
		if(this.dommain != null){
			return this.dommain.getType();
		}
		return columnType;
	}
	
	public void setColumnType(IColumnType columnType) {
		this.columnType = columnType;
	}
	
	public boolean isNotNull() {
		return notNull;
	}
	
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}
	
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public String getSize() {
		if(this.dommain != null){
			return this.dommain.getSize();
		}
		if(size.length()==0)
			return 0+"";
		return size;
	}
	
	public void setSize(String size) {
		this.size = size;
	}
	
	public String getDescription(){
		if(this.description == null){
			this.description = "";
		}
		return this.description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}
	
	public String getDefaultValue(){
		if(this.defaultValue==null){
			this.defaultValue = "";
		}
		return this.defaultValue;
	}
	
	public void setDefaultValue(String defaultValue){
		this.defaultValue = defaultValue;
	}
	
	public DommainModel getDommain() {
		return dommain;
	}

	public void setDommain(DommainModel dommain) {
		this.dommain = dommain;
	}

	public String toString(){
		return getColumnName();
	}
	/*public String validator(){
		int i=Integer.valueOf(this.getSize());
		if(i<columnType.getMinSize()||i>columnType.getMaxSize()){
			return 
		}
	}*/
}
