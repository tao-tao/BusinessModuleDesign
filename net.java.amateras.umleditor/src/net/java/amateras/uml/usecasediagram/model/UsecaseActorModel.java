/**
 * 
 */
package net.java.amateras.uml.usecasediagram.model;

import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.model.AbstractUMLEntityModel;
import net.java.amateras.uml.model.EntityModel;
import net.java.amateras.uml.model.ICloneableModel;
import net.java.amateras.uml.usecasediagram.property.ActorImagePropertyDescriptor;
import net.java.amateras.uml.usecasediagram.property.MyPropertyDescriptor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.ColorPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * @author Takahiro Shida.
 *
 */
@SuppressWarnings("serial")
public class UsecaseActorModel extends AbstractUMLEntityModel implements EntityModel, ICloneableModel {

	public static final String P_IMAGE = "_image";
	public static final String P_DESC = "_desc";
	private static final Dimension MINIMUM_SIZE = new Dimension(40, 50);
	private String name;
	private String desc;
	private String otherinfo;
	private String fdec;
	private String xdec;
	private String cata;
	public String getCata() {
		return cata;
	}

	public void setCata(String cata) {
		this.cata = cata;
	}

	public String getFdec() {
		return fdec;
	}

	public void setFdec(String fdec) {
		this.fdec = fdec;
	}

	public String getXdec() {
		return xdec;
	}

	public void setXdec(String xdec) {
		this.xdec = xdec;
	}

	public String getOtherinfo() {
		return otherinfo;
	}

	public void setOtherinfo(String otherinfo) {
		this.otherinfo = otherinfo;
	}

	public String getDesc() {
		return desc;
	}

	public void setConstraint(Rectangle constraint) {
		Dimension size = constraint.getSize();
		if (MINIMUM_SIZE.contains(size)) {
			constraint.setSize(MINIMUM_SIZE);
		}

//		if( constraint.x > 100 && constraint.x <= 200 ){
//			constraint.x = constraint.x - 100;
//		}
//		
//		if( constraint.x > 200){
//			constraint.x -= 200;
//		}

		super.setConstraint(constraint);
	}

	public void setDesc(String desc) {
		String old = this.desc;
		this.desc = desc;
		firePropertyChange(P_DESC, old, desc);
	}

	private String imagePath;
	
	public UsecaseActorModel() {
		super();
		setName("actor");
		setDesc("desc");
		setOtherinfo("otherinfo");
		setFdec("Fdec");
		setXdec("Xdec");
		setCata("cata");
	}
	
	public void setName(String name) {
		String old = this.name;
		this.name = name;
		firePropertyChange(P_ENTITY_NAME, old, name);
	}
	
	public String getName() {
		return name;
	}
	
	public void setImagePath(String image) {
		String old = this.imagePath;
		this.imagePath = image;
		firePropertyChange(P_IMAGE, old, image);
	}
	
	public IFile getImageFile() {
		if ("".equals(imagePath) || imagePath == null) {
			return null;
		}
		return ResourcesPlugin.getWorkspace().getRoot().getFile(Path.fromPortableString(imagePath));			
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new TextPropertyDescriptor(P_ENTITY_NAME, UMLPlugin
						.getDefault().getResourceString("property.name")),
				new MyPropertyDescriptor(P_DESC, "����",this.getDesc()),
				new ActorImagePropertyDescriptor(P_IMAGE, UMLPlugin
						.getDefault().getResourceString("property.image")),
				new ColorPropertyDescriptor(P_FOREGROUND_COLOR, UMLPlugin
						.getDefault().getResourceString("property.foreground"))};
	}
	
	public Object getPropertyValue(Object id) {
		if (P_ENTITY_NAME.equals(id)) {
			return name;
		} else if (P_IMAGE.equals(id)) {
			return imagePath;
		}else if (P_DESC.equals(id)) {
			return desc;
		}
		return super.getPropertyValue(id);
	}
	
	public void setPropertyValue(Object id, Object value) {
		if (P_ENTITY_NAME.equals(id)) {
			setName((String) value);
		} else if (P_IMAGE.equals(id)) {
			setImagePath((String) value);
		}else if (P_DESC.equals(id)) {
			setDesc((String) value);
		}
		super.setPropertyValue(id, value);
	}
	
	public boolean isPropertySet(Object id) {
		return P_DESC.equals(id) ||P_ENTITY_NAME.equals(id) || P_IMAGE.equals(id) || super.isPropertySet(id);
	}
	
	public Object clone(){
		UsecaseActorModel model = new UsecaseActorModel();
		model.setName(getName());
		model.setImagePath(getImagePath());
		model.setForegroundColor(getForegroundColor().getRGB());
		model.setParent(getParent());
		model.setConstraint(new Rectangle(getConstraint()));
		return model;
	}
}
