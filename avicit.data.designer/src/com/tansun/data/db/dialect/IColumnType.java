package com.tansun.data.db.dialect;

public interface IColumnType {
	
	public String getName();
	
	public String getLogicalName();
	
	public boolean supportSize();
	
	public int getType();
	
	public int getMinSize();
	
	public int getMaxSize();
	
	
	public int getDMinsize();
	public int getDMaxsize();
}
