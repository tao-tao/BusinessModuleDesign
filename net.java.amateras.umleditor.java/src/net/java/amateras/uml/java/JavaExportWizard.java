package net.java.amateras.uml.java;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.java.amateras.uml.classdiagram.model.Argument;
import net.java.amateras.uml.classdiagram.model.AttributeModel;
import net.java.amateras.uml.classdiagram.model.ClassModel;
import net.java.amateras.uml.classdiagram.model.GeneralizationModel;
import net.java.amateras.uml.classdiagram.model.InterfaceModel;
import net.java.amateras.uml.classdiagram.model.OperationModel;
import net.java.amateras.uml.classdiagram.model.RealizationModel;
import net.java.amateras.uml.model.AbstractUMLConnectionModel;
import net.java.amateras.uml.model.AbstractUMLEntityModel;
import net.java.amateras.uml.model.AbstractUMLModel;
import net.java.amateras.uml.model.RootModel;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.wizard.Wizard;

/**
 * 
 * @类名称   ： JavaExportWizard.java  
 * @类描述   ：导出java类wizard
 * @作者           : yao lei@tansun.com.cn  
 * @创建时间 ： 2013-12-31 下午3:46:12
 * @版本           ： 1.00
 * 
 * @修改记录: 
 * @版本            修改人          修改时间                  修改内容描述
 * ----------------------------------------
 * @1.00            姚蕾            2013-12-31下午3:46:12
 * ----------------------------------------
 */
public class JavaExportWizard extends Wizard {
	
	//当前page
	private JavaExportWizardPage folderPage;
	//当前工程
	private IJavaProject project;
	private Map<String, AbstractUMLEntityModel> target = new HashMap<String, AbstractUMLEntityModel> ();
	
	public JavaExportWizard(IJavaProject project, RootModel root){
		this.project = project;
		
		setWindowTitle(UMLJavaPlugin.getDefault().getResourceString("generate.dialog.title"));
		
		
		List<AbstractUMLModel> children = root.getChildren();
		for(int i=0;i<children.size();i++){
			Object child = children.get(i);
			if(child instanceof ClassModel){
				this.target.put(((ClassModel)child).getName(), (AbstractUMLEntityModel) child);
			}
			if(child instanceof InterfaceModel){
				this.target.put(((InterfaceModel)child).getName(), (AbstractUMLEntityModel) child);
			}
		}
	}
	
	/**
	 * 
	 * @方法名称    ：addPages
	 * @功能描述    ：添加页面
	 * @param   ：无
	 * @return  ：void
	 * @throws  ：无
	 * @since   ：Ver 1.00
	 */
	public void addPages() {
		Iterator<String> ite = target.keySet().iterator();
		List<String> classNames = new ArrayList<String>();
		while(ite.hasNext()){
			classNames.add(ite.next());
		}
		
		this.folderPage = new JavaExportWizardPage(project, 
				classNames.toArray(new String[classNames.size()]));
		
		addPage(folderPage);
	}

	/**
	 * 
	 * @方法名称    ：performFinish
	 * @功能描述    ：完成操作
	 * @param   ：无
	 * @return  ：boolean
	 * @throws  ：无
	 * @since   ：Ver 1.00
	 */
	public boolean performFinish() {
		String outputDir = folderPage.getOutputFolder().split(Contants.LINE)[0];
		if(outputDir.startsWith(Contants.LINE)){
			outputDir = outputDir.substring(1);
		}
		if(outputDir.endsWith(Contants.LINE)){
			outputDir = outputDir.substring(0, outputDir.length()-1);
		}
		
		String[] selection = folderPage.getGenerateClasses();
		
		for(int i=0;i<selection.length;i++){
			Object obj = target.get(selection[i]);
			String fileName = getModelName((AbstractUMLEntityModel)obj).replace(Contants.POINT,Contants.LINE);
			if(!outputDir.equals(Contants.BLANK)){
				fileName = outputDir + Contants.LINE + fileName;
			}
			fileName = fileName.replaceAll("<.+?>", Contants.BLANK);
			
			// create parent folders
			String[] folders = fileName.split(Contants.LINE);
			StringBuffer sb = new StringBuffer();
			
			for(int j=0;j<folders.length-1;j++){
				sb.append(Contants.LINE).append(folders[j]);
				IFolder folder = project.getProject().getFolder(new Path(sb.toString()));
				if(!folder.exists()){
					try {
						folder.create(true, true, new NullProgressMonitor());
					} catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
			
			IFile file = project.getProject().getFile(new Path(fileName + ".java"));
			String source = Contants.BLANK;
			
			if(obj instanceof ClassModel){
				source = createClassSource((ClassModel)obj);
			} else if(obj instanceof InterfaceModel){
				source = createInterfaceSource((InterfaceModel)obj);
			}
			
			// create a source file
			try {
				if(file.exists()){
					file.setContents(new ByteArrayInputStream(source.getBytes()),
						true, true, new NullProgressMonitor());
				} else {
					file.create(new ByteArrayInputStream(source.getBytes()),
						true, new NullProgressMonitor());
				}
			} catch(Exception ex){
				ex.printStackTrace();
				return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 * 
	 * @方法名称    ：createClassSource
	 * @功能描述    ： 创建java类
	 * @param   ：ClassModel
	 * @return  ：String
	 * @throws  ：无
	 * @since   ：Ver 1.00
	 */
	private String createClassSource(ClassModel model){
		String[] names = splitClassName(model.getName());
		StringBuffer sb = new StringBuffer();
		
		if(!names[0].equals(Contants.BLANK)){
			sb.append(Contants.PACKAGE).append(names[0]).append(Contants.LINE_FEED);
		}
		
		sb.append("public ");
		if(model.isAbstract()){
			sb.append("abstract ");
		}
		sb.append("class ").append(names[1]);
		sb.append(createParentRelation(model));
		sb.append(" {\n");
		List<AbstractUMLModel> children = model.getChildren();
		for(int i=0;i<children.size();i++){
			Object child = children.get(i);
			if(child instanceof AttributeModel){
				sb.append(createAttribute((AttributeModel)child)).append(Contants.LINE_N);
			}
		}
		sb.append(Contants.LINE_N);
		for(int i=0;i<children.size();i++){
			Object child = children.get(i);
			if(child instanceof OperationModel){
				OperationModel ope = (OperationModel)child;
				sb.append(createMethodSignature(ope));
				if(ope.isAbstract()){
					sb.append(Contants.LINE_FEED);
				} else {
					sb.append("{");
					sb.append(Contants.LINE_N);
					if(!ope.getType().equals("void") && ope.getType().length()!=0 && !ope.isConstructor()){
						sb.append("        ");
						sb.append("return ");
						sb.append(getDefaultValue(ope.getType()));
						sb.append(";\n");
					}
					sb.append("    ");
					sb.append("}\n\n");
				}
			}
		}
		sb.append("}\n");
		return sb.toString();
	}
	
	/**
	 * 
	 * @方法名称    ：createInterfaceSource
	 * @功能描述    ：创建接口类的内容
	 * @param   ：InterfaceModel
	 * @return  ：String
	 * @throws  ：无
	 * @since   ：Ver 1.00
	 */
	private String createInterfaceSource(InterfaceModel model){
		String[] names = splitClassName(model.getName());
		StringBuffer sb = new StringBuffer();
		
		if(!names[0].equals(Contants.BLANK)){
			sb.append(Contants.PACKAGE).append(names[0]).append(Contants.LINE_FEED);
		}
		
		sb.append("public interface ").append(names[1]);
		sb.append(createParentRelation(model));
		sb.append(" {\n");
		List<AbstractUMLModel> children = model.getChildren();
		for(int i=0;i<children.size();i++){
			Object child = children.get(i);
			if(child instanceof AttributeModel){
				sb.append(createAttribute((AttributeModel)child)).append(Contants.LINE_N);
			}
		}
		sb.append(Contants.LINE_N);
		for(int i=0;i<children.size();i++){
			Object child = children.get(i);
			if(child instanceof OperationModel){
				sb.append(createMethodSignature((OperationModel)child));
				sb.append(Contants.LINE_FEED);
			}
		}
		sb.append("}\n");
		return sb.toString();
	}
	
	/**
	 * 
	 * @方法名称    ：createAttribute
	 * @功能描述    ：创建属性部分
	 * @param   ：AttributeModel
	 * @return  ：String
	 * @throws  ：无
	 * @since   ：Ver 1.00
	 */
	private String createAttribute(AttributeModel attr){
		StringBuffer sb = new StringBuffer();
		sb.append("    ");
		String visibility = attr.getVisibility().toString();
		if(!visibility.equals("package")){
			sb.append(attr.getVisibility().toString());
			sb.append(Contants.TWO_BLANK);
		}
		if(attr.isStatic()){
			sb.append("static ");
		}
		sb.append(attr.getType());
		sb.append(Contants.TWO_BLANK);
		sb.append(attr.getName());
		
		if(attr.getParent() instanceof InterfaceModel){
			if(UMLJavaUtils.isPrimitive(attr.getType())){
				if(attr.getType().equals("boolean")){
					sb.append(" = false");
				} else {
					sb.append(" = 0");
				}
			} else {
				sb.append(" = null");
			}
		}
		
		sb.append(";");
		return sb.toString();
	}
	
	/**
	 * 
	 * @方法名称    ：createMethodSignature
	 * @功能描述    ：创建方法部分
	 * @param   ：OperationModel
	 * @return  ：String
	 * @throws  ：无
	 * @since   ：Ver 1.00
	 */
	private String createMethodSignature(OperationModel ope){
		StringBuffer sb = new StringBuffer();
		sb.append("    ");
		String visibility = ope.getVisibility().toString();
		if(!visibility.equals("package")){
			sb.append(ope.getVisibility().toString());
			sb.append(Contants.TWO_BLANK);
		}
		if(ope.isStatic()){
			sb.append("static ");
		}
		if(ope.isAbstract()){
			sb.append("abstract ");
		}
		if(ope.getType().length()!=0 && !ope.isConstructor()){
			sb.append(ope.getType());
			sb.append(Contants.TWO_BLANK);
		}
		sb.append(ope.getName());
		sb.append("(");
		List<Argument> params = ope.getParams();
		for(int i=0;i<params.size();i++){
			if(i!=0){
				sb.append(Contants.COMMA);
			}
			Argument arg = params.get(i);
			sb.append(arg.getType());
			sb.append(Contants.TWO_BLANK);
			sb.append(arg.getName());
		}
		sb.append(")");
		return sb.toString();
	}
	
	/**
	 * 
	 * @方法名称    ：createParentRelation
	 * @功能描述    ：创建映射关系部分
	 * @param   ：AbstractUMLEntityModel
	 * @return  ：String
	 * @throws  ：无
	 * @since   ：Ver 1.00
	 */
	private String createParentRelation(AbstractUMLEntityModel model){
		StringBuffer sb = new StringBuffer();
		List<AbstractUMLConnectionModel> conns = model.getModelSourceConnections();
		int extendsCount = 0;
		for(AbstractUMLConnectionModel conn: conns){
			if(conn instanceof GeneralizationModel){
				if(extendsCount==0){
					sb.append(" extends ");
				} else {
					sb.append(Contants.COMMA);
				}
				AbstractUMLEntityModel target = ((GeneralizationModel) conn).getTarget();
				sb.append(getModelName(target));
				extendsCount++;
			}
		}
		int implementsCount = 0;
		for(AbstractUMLConnectionModel conn: conns){
			if(conn instanceof RealizationModel){
				if(implementsCount==0){
					sb.append(" implements ");
				} else {
					sb.append(Contants.COMMA);
				}
				AbstractUMLEntityModel target = ((RealizationModel) conn).getTarget();
				sb.append(getModelName(target));
				implementsCount++;
			}
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @方法名称    ：splitClassName
	 * @功能描述    ：分割类名称
	 * @param   ：String
	 * @return  ：String[]
	 * @throws  ：无
	 * @since   ：Ver 1.00
	 */
	private String[] splitClassName(String className){
		int index = className.lastIndexOf(Contants.POINT);
		if(index < 0){
			return new String[]{Contants.BLANK,className};
		}
		return new String[]{
				className.substring(0, index),
				className.substring(index+1)
		};
	}
	
	/**
	 * 
	 * @方法名称    ：getDefaultValue
	 * @功能描述    ：获取属性类型
	 * @param   ：String
	 * @return  ：String
	 * @throws  ：无
	 * @since   ：Ver 1.00
	 */
	private String getDefaultValue(String type){
		if(type.equals("int") || type.equals("long") || type.equals("double") || 
		   type.equals("short") || type.equals("char")){
			return "0";
		}
		if(type.equals("boolean")){
			return "false";
		}
		return "null";
	}
	
	/**
	 * 
	 * @方法名称    ：getModelName
	 * @功能描述    ：获取model类型
	 * @param   ：AbstractUMLEntityModel
	 * @return  ：String
	 * @throws  ：无
	 * @since   ：Ver 1.00
	 */
	private String getModelName(AbstractUMLEntityModel model){
		if(model instanceof ClassModel){
			return ((ClassModel)model).getName();
		}
		if(model instanceof InterfaceModel){
			return ((InterfaceModel)model).getName();
		}
		return null;
	}

}
