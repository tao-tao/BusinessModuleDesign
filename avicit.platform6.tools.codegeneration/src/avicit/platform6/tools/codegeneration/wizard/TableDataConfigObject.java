package avicit.platform6.tools.codegeneration.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

public class TableDataConfigObject {
	
	private String name;
	
	private String code;
	
	private Boolean state;
	
	private Image image;
	
	
	private List<TableDataConfigObject> childenTree = new  ArrayList<TableDataConfigObject>();
	

	public TableDataConfigObject(String code,String name){
		this.code=code;
		this.name=name;
	}
	
	public TableDataConfigObject(String code,String name,List<TableDataConfigObject> childenTree){
		
		this.name=name;
		this.code=code;
		this.childenTree = childenTree;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public List<TableDataConfigObject> getChildenTree() {
		return childenTree;
	}

	public void setChildenTree(List<TableDataConfigObject> childenTree) {
		this.childenTree = childenTree;
	}
	
}
