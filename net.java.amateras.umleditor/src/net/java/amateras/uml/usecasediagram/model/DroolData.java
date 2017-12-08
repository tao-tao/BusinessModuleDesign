package net.java.amateras.uml.usecasediagram.model;

import net.java.amateras.uml.usecasediagram.property.DataField;

public class DroolData {
	
	private String path;
	
	public Object clone(){
		DroolData df = new DroolData();
		df.setPath(getPath());
		return df;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
}
