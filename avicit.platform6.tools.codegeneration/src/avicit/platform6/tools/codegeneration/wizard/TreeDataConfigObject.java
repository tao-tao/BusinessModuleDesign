package avicit.platform6.tools.codegeneration.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

public class TreeDataConfigObject {
	
	private String name;
	
	private String code;
	
	private String state;
	
	private Image image;
	
	
	private List<TreeDataConfigObject> childenTree = new  ArrayList<TreeDataConfigObject>();
	

	public TreeDataConfigObject(String code,String name){
		this.code=code;
		this.name=name;
	}
	
	public TreeDataConfigObject(String code,String name,List<TreeDataConfigObject> childenTree){
		
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public List<TreeDataConfigObject> getChildenTree() {
		return childenTree;
	}

	public void setChildenTree(List<TreeDataConfigObject> childenTree) {
		this.childenTree = childenTree;
	}
	
}
