package net.java.amateras.uml.java;

import java.util.ArrayList;
import java.util.List;

import net.java.amateras.uml.DiagramSerializer;
import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.classdiagram.model.ClassModel;
import net.java.amateras.uml.classdiagram.model.CommonEntityModel;
import net.java.amateras.uml.classdiagram.model.InterfaceModel;
import net.java.amateras.uml.model.AbstractUMLEntityModel;
import net.java.amateras.uml.model.AbstractUMLModel;
import net.java.amateras.uml.model.RootModel;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/**
 * 
 * @类名称   ： ClassSynAction.java  
 * @类描述   ：类同步action
 * @作者           : yao lei@tansun.com.cn  
 * @创建时间 ： 2013-12-31 下午3:37:51
 * @版本           ： 1.00
 * 
 * @修改记录: 
 * @版本            修改人          修改时间                  修改内容描述
 * ----------------------------------------
 * @1.00    姚蕾            2013-12-31下午3:37:51
 * ----------------------------------------
 */
public class ClassSynAction {
	//当前文件
	public IFile file;
	//根Model
	RootModel root;
	public IProgressMonitor monitor;

	public IProgressMonitor getMinitor() {
		return monitor;
	}

	public void setMinitor(IProgressMonitor minitor) {
		this.monitor = minitor;
	}

	public ClassSynAction(IFile file) {
		this.file = file;
	}
	/**
	 * 
	 * @方法名称    ：syn
	 * @功能描述    ：同步操作
	 * @param   ：无
	 * @return  ：void
	 * @throws  ：无
	 * @since   ：Ver 1.00
	 */
	public void syn() {
		if (file.exists()) {
			try {
				monitor.beginTask(
						"正在同步文件：" + file.getLocationURI(), 1);
				root = DiagramSerializer.deserialize(file.getContents());
				execute(getAllActorModel());
				file.setContents(DiagramSerializer.serialize(root), true, true,
						monitor);
			} catch (Exception ex) {
				UMLPlugin.logException(ex);
			}
		}
	}
	
	/**
	 * 
	 * @方法名称    ：getAllActorModel
	 * @功能描述    ：获取所有uml对象
	 * @param   ：无
	 * @return  ：List<CommonEntityModel>
	 * @throws  ：无
	 * @since   ：Ver 1.00
	 */
	public List<CommonEntityModel> getAllActorModel() {
		List<CommonEntityModel> allClass = new ArrayList<CommonEntityModel>();

		for (AbstractUMLModel umlMode : root.getChildren()) {

			if (umlMode instanceof CommonEntityModel) {
				allClass.add((CommonEntityModel) umlMode);
			}
		}

		return allClass;
	}
	
	/**
	 * 
	 * @方法名称    ：execute
	 * @功能描述    ：执行操作
	 * @param   ：无
	 * @return  ：void
	 * @throws  ：无
	 * @since   ：Ver 1.00
	 */
	public void execute(List<CommonEntityModel> target) {
		for (CommonEntityModel model : new ArrayList<CommonEntityModel>(target)) {
			String className = null;

			if (model instanceof ClassModel) {
				className = ((ClassModel) model).getName();

			} else if (model instanceof InterfaceModel) {
				className = ((InterfaceModel) model).getName();
			}

			className = UMLJavaUtils.stripGenerics(className);

			IJavaProject javaProject = JavaCore.create(file.getProject());
			try {
				IType type = javaProject.findType(className);
				if (type != null && type.exists()) {
					RefreshClassModelCommand rcomman = new RefreshClassModelCommand(
							root, type);
					rcomman.setModel(model);
					model.getChildren().clear();
					Rectangle rect = ((AbstractUMLEntityModel) model)
							.getConstraint();
					rcomman.setLocation(new Point(rect.x, rect.y));
					rcomman.execute();
				}
			} catch (JavaModelException e) {
				UMLPlugin.logException(e);
			}
		}

	}
	public void getRunnable(){
		
	}
}
