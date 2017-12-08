/**
 * 
 */
package net.java.amateras.uml.usecasediagram.model;

import java.util.ArrayList;
import java.util.List;

import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.model.AbstractUMLEntityModel;
import net.java.amateras.uml.model.EntityModel;
import net.java.amateras.uml.model.ICloneableModel;
import net.java.amateras.uml.usecasediagram.property.DataField;
import net.java.amateras.uml.usecasediagram.property.MyPropertyDescriptor;
import net.java.amateras.uml.usecasediagram.property.ResourcePropertyDescriptor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.ColorPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * @author shida
 * 
 */
@SuppressWarnings("serial")
public class UsecaseModel extends AbstractUMLEntityModel implements
		EntityModel, ICloneableModel {

	public static final String P_RESOURCE = "_resource";
	public static final String P_DEEXCEPTION = "_deexception";
	public static final String P_DEDEAL = "_dedeal";
	public static final String P_INPUT = "_dinput";
	public static final String P_DREG = "_dreg";
	
	private static final Dimension MINIMUM_SIZE = new Dimension(100, 40);
	private String deException;
	private String deDeal;
	public InPutModel input;
	private String dReg;
	private String name;
	private String desc;
	
	//ҵ�������ʶ
	private String userNeed;
	private String userTitle;
	private String userFlg;
	private String userGrade;
	private String userFirst;
	private String imagePath;
	private String softStatus;
	private String softFlg;
	private String softTitle;
	private String softGrade;
	private String id;
	//NEW
	private String applyre;
	private String constri;
	private String testPoint;
	
	private String noRequirement;
	
	public String getNoRequirement() {
		return noRequirement;
	}

	public void setNoRequirement(String noRequirement) {
		this.noRequirement = noRequirement;
	}

	public String getApplyre() {
		return applyre;
	}

	public void setApplyre(String applyre) {
		this.applyre = applyre;
	}

	public String getConstri() {
		return constri;
	}

	public void setConstri(String constri) {
		this.constri = constri;
	}

	public String getTestPoint() {
		return testPoint;
	}

	public void setTestPoint(String testPoint) {
		this.testPoint = testPoint;
	}

	public String getSoftStatus() {
		return softStatus;
	}

	public void setSoftStatus(String softStatus) {
		this.softStatus = softStatus;
	}

	public String getSoftFlg() {
		return softFlg;
	}

	public void setSoftFlg(String softFlg) {
		this.softFlg = softFlg;
	}

	public String getSoftTitle() {
		return softTitle;
	}

	public void setSoftTitle(String softTitle) {
		this.softTitle = softTitle;
	}

	public String getSoftGrade() {
		return softGrade;
	}

	public void setSoftGrade(String softGrade) {
		this.softGrade = softGrade;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserNeed() {
		return userNeed;
	}

	public void setUserNeed(String userNeed) {
		this.userNeed = userNeed;
	}

	public String getUserTitle() {
		return userTitle;
	}

	public void setUserTitle(String userTitle) {
		this.userTitle = userTitle;
	}

	public String getUserFlg() {
		return userFlg;
	}

	public void setUserFlg(String userFlg) {
		this.userFlg = userFlg;
	}

	public String getUserGrade() {
		return userGrade;
	}

	public void setUserGrade(String userGrade) {
		this.userGrade = userGrade;
	}

	public String getUserFirst() {
		return userFirst;
	}

	public void setUserFirst(String userFirst) {
		this.userFirst = userFirst;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		String old = this.desc;
		this.desc = desc;
	}

	public String getDeException() {
		return deException;
	}

	public void setDeException(String deException) {
		String old = this.deException;
		this.deException = deException;
		firePropertyChange(P_DEEXCEPTION, old, deException);
	}

	public String getDeDeal() {
		return deDeal;
	}

	public void setDeDeal(String deDeal) {
		String old = this.deDeal;
		this.deDeal = deDeal;
		firePropertyChange(P_DEDEAL, old, deDeal);
	}

	public InPutModel getInput() {
		return input;
	}

	public void setInput(InPutModel input) {
		InPutModel old = this.input;
		this.input = input;
		firePropertyChange(P_INPUT, old, input);
	}

	public String getdReg() {
		return dReg;
	}

	public void setdReg(String dReg) {
		String old = this.dReg;
		this.dReg = dReg;
		firePropertyChange(P_DREG, old, dReg);
	}

	private String resource;

	public UsecaseModel() {
		super();
		setName("用例图");
		setDeDeal("业务规则");
		InPutModel im=new InPutModel();
		im.setInputname("用例填写基本信息");
		setInput(im);
		setDeException("权限描述");
		setdReg("参与者");
		setDesc("参与者");
		setConstri("前置条件");
		setTestPoint("后置条件");
		setApplyre("异常操作流程");
		setNoRequirement("非公能需求");
	}

	public void setConstraint(Rectangle constraint) {
		Dimension size = constraint.getSize();
		if (MINIMUM_SIZE.contains(size)) {
			constraint.setSize(MINIMUM_SIZE);
		}
		super.setConstraint(constraint);
	}

	public void setName(String name) {
		String old = this.name;
		this.name = name;
		firePropertyChange(P_ENTITY_NAME, old, name);
	}

	public String getName() {
		return name;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String file) {
		String old = this.resource;
		this.resource = file;
		firePropertyChange(P_RESOURCE, old, file);
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new TextPropertyDescriptor(P_ENTITY_NAME, UMLPlugin
						.getDefault().getResourceString("property.name")),
				new TextPropertyDescriptor(P_DREG, "ҵ�����"),
				new TextPropertyDescriptor(P_INPUT, "����/���"),
				new TextPropertyDescriptor(P_DEDEAL, "�����߼�"),
				new TextPropertyDescriptor(P_DEEXCEPTION, "�쳣����"),
				new ResourcePropertyDescriptor(P_RESOURCE, UMLPlugin
						.getDefault().getResourceString("property.resource")),
				new ColorPropertyDescriptor(P_BACKGROUND_COLOR, UMLPlugin
						.getDefault().getResourceString("property.background")),
				new ColorPropertyDescriptor(P_FOREGROUND_COLOR, UMLPlugin
						.getDefault().getResourceString("property.foreground")) };
	}

	public Object getPropertyValue(Object id) {
		if (P_ENTITY_NAME.equals(id)) {
			return name;
		} else if (P_RESOURCE.equals(id)) {
			return resource;
		} else if (P_DREG.equals(id)) {
			return dReg;
		} else if (P_INPUT.equals(id)) {
			return input.getInputname();
		} else if (P_DEDEAL.equals(id)) {
			return deDeal;
		} else if (P_DEEXCEPTION.equals(id)) {
			return deException;
		}
		return super.getPropertyValue(id);
	}

	public void setPropertyValue(Object id, Object value) {
		if (P_ENTITY_NAME.equals(id)) {
			setName((String) value);
		} else if (P_RESOURCE.equals(id)) {
			setResource((String) value);
		} else if (P_DREG.equals(id)) {
			setdReg((String) value);
		} else if (P_INPUT.equals(id)) {
			setInput((InPutModel) value);
		} else if (P_DEDEAL.equals(id)) {
			setDeDeal((String) value);
		} else if (P_DEEXCEPTION.equals(id)) {
			setDeException((String) value);
		}
		super.setPropertyValue(id, value);
	}

	public boolean isPropertySet(Object id) {
		return P_ENTITY_NAME.equals(id) || P_DREG.equals(id)
				|| P_INPUT.equals(id) || P_DEDEAL.equals(id)
				|| P_DEEXCEPTION.equals(id) || P_RESOURCE.equals(id)
				|| super.isPropertySet(id);
	}

	public IFile getFileResource() {
		if(resource.trim().length()<=0){
			return null;
		}
		return ResourcesPlugin.getWorkspace().getRoot()
				.getFile(Path.fromPortableString(resource));
	}

	public Object clone() {
		UsecaseModel model = new UsecaseModel();
		model.setName(getName());
		//model.setDeDeal(getDeDeal());
		model.setResource(getResource());
		model.setForegroundColor(getForegroundColor().getRGB());
		model.setBackgroundColor(getBackgroundColor().getRGB());
		model.setParent(getParent());
		model.setConstraint(new Rectangle(getConstraint()));
		model.setInput(this.getInput());
		//model.setDeDeal(this.getDeDeal());
		//model.setDeException(this.getDeException());
		//this.setdReg(this.getdReg());
		return model;
	}
	 public class InPutModel {
		public String inputname;
		public DataField[] datas;
		private DroolData[] drools;

		public DataField[] getDatas() {
			return datas;
		}

		public void setDatas(DataField[] datas) {
			this.datas = datas;
		}

		public String getInputname() {
			return inputname;
		}

		public void setInputname(String inputname) {
			this.inputname = inputname;
		}

		public DroolData[] getDrools() {
			return drools;
		}

		public void setDrools(DroolData[] drools) {
			this.drools = drools;
		};
		
	}
}
