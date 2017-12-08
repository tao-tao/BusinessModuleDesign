package com.tansun.data.db.dialect;

import java.io.Serializable;

public class ColumnType implements IColumnType, Serializable {
	
	private String name;
	private String logicalName;
	private boolean supportSize;
	private int type;
	private String origName;
	private int minsize;
	private int maxsize;
	
	private int dminsize;
	private int dmaxsize;
	public int getDminsize() {
		return dminsize;
	}

	public void setDminsize(int dminsize) {
		this.dminsize = dminsize;
	}

	public int getDmaxsize() {
		return dmaxsize;
	}

	public void setDmaxsize(int dmaxsize) {
		this.dmaxsize = dmaxsize;
	}

	public int getMinsize() {
		return minsize;
	}

	public void setMinsize(int minsize) {
		this.minsize = minsize;
	}

	public int getMaxsize() {
		return maxsize;
	}

	public void setMaxsize(int maxsize) {
		this.maxsize = maxsize;
	}

	public String getOrigName() {
		return origName;
	}

	public void setOrigName(String origName) {
		this.origName = origName;
	}

	public ColumnType(String name, String logicalName, boolean supportSize,int type){
		this.name = name;
		this.logicalName = logicalName;
		this.supportSize = supportSize;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLogicalName(){
		return logicalName;
	}
	
	public boolean supportSize() {
		return supportSize;
	}
	
	public int getType(){
		return type;
	}
	
	public String toString(){
		//return getLogicalName() + " - " + getName();
		return getName() + " - " + getLogicalName();
	}

	@Override
	public int getMinSize() {
		// TODO Auto-generated method stub
		return this.minsize;
	}

	@Override
	public int getMaxSize() {
		// TODO Auto-generated method stub
		return this.maxsize;
	}

	@Override
	public int getDMinsize() {
		// TODO Auto-generated method stub
		return this.dminsize;
	}

	@Override
	public int getDMaxsize() {
		// TODO Auto-generated method stub
		return this.dmaxsize;
	}
}
