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
 * @������   �� ClassSynAction.java  
 * @������   ����ͬ��action
 * @����           : yao lei@tansun.com.cn  
 * @����ʱ�� �� 2013-12-31 ����3:37:51
 * @�汾           �� 1.00
 * 
 * @�޸ļ�¼: 
 * @�汾            �޸���          �޸�ʱ��                  �޸���������
 * ----------------------------------------
 * @1.00    Ҧ��            2013-12-31����3:37:51
 * ----------------------------------------
 */
public class ClassSynAction {
	//��ǰ�ļ�
	public IFile file;
	//��Model
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
	 * @��������    ��syn
	 * @��������    ��ͬ������
	 * @param   ����
	 * @return  ��void
	 * @throws  ����
	 * @since   ��Ver 1.00
	 */
	public void syn() {
		if (file.exists()) {
			try {
				monitor.beginTask(
						"����ͬ���ļ���" + file.getLocationURI(), 1);
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
	 * @��������    ��getAllActorModel
	 * @��������    ����ȡ����uml����
	 * @param   ����
	 * @return  ��List<CommonEntityModel>
	 * @throws  ����
	 * @since   ��Ver 1.00
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
	 * @��������    ��execute
	 * @��������    ��ִ�в���
	 * @param   ����
	 * @return  ��void
	 * @throws  ����
	 * @since   ��Ver 1.00
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
