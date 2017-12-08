package net.java.amateras.uml.java;

import java.util.ArrayList;
import java.util.List;

import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.classdiagram.model.AssociationModel;
import net.java.amateras.uml.classdiagram.model.AttributeModel;
import net.java.amateras.uml.classdiagram.model.ClassModel;
import net.java.amateras.uml.classdiagram.model.DependencyModel;
import net.java.amateras.uml.classdiagram.model.InterfaceModel;
import net.java.amateras.uml.classdiagram.model.OperationModel;
import net.java.amateras.uml.model.AbstractUMLConnectionModel;
import net.java.amateras.uml.model.AbstractUMLEntityModel;
import net.java.amateras.uml.model.RootModel;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
/**
 * @������: RefreshClassModelCommand.java
 * @������: ˢ����
 * @����: ʷ�Է�
 * @����ʱ��: 2013-11-6 ����10:28:01
 * @�汾: 1.00
 * 
 * @�޸ļ�¼: <p>
 *        �汾 �޸��� �޸�ʱ�� �޸���������<br>
 *        ----------------------------------------<br>
 *        1.00 user 2013-11-6 ����10:28:01<br>
 *        ----------------------------------------<br>
 *        </p>
 */
public class RefreshClassModelCommand extends Command {

	/**
	 * ��Ӧ��ʵ���ļ�
	 */
	private IType[] types;
	/**
	 * ��Model
	 */
	private RootModel root;
	/**
	 * ����Model
	 */
	private List<AbstractUMLEntityModel> models;
	/**
	 * ��λ
	 */
	private Point location;
	/**
	 * ����ģ��
	 */
	 public AbstractUMLEntityModel rmodel;
	public AbstractUMLEntityModel getModel() {
		return rmodel;
	}

	public void setModel(AbstractUMLEntityModel model) {
		this.rmodel = model;
	}

	/**
	 * ����ͬ�����캯��
	 */
	public RefreshClassModelCommand(RootModel root,IType type){
		this(root, new IType[]{ type });
	}
	
	/**
	 * Constructor for the two or more types adding.
	 * 
	 * @param root the root model
	 * @param types types to add
	 */
	public RefreshClassModelCommand(RootModel root,IType[] types){
		this.root = root;
		this.types = types;
	}

	/**
	 * 
	 * @��������    ��setLocation
	 * @��������    ������λ��
	 * @param   ����
	 * @return  ��void
	 * @throws  ����
	 * @since   ��Ver 1.00
	 */
	public void setLocation(Point location){
		this.location = location;
	}
	
	/**
	 * 
	 * @��������   ��execute
	 * @��������   ��ִ��ͬ��
	 * @param   ����
	 * @return  ��void
	 * @throws  ����
	 * @since   ��Ver 1.00
	 */
	public void execute(){
		for (int i = 0; i < ((AbstractUMLEntityModel)rmodel)
				.getModelSourceConnections().size(); i++) {
			AbstractUMLConnectionModel model = (AbstractUMLConnectionModel) ((AbstractUMLEntityModel)rmodel)
					.getModelSourceConnections()
					.get(i);
			if(model instanceof AssociationModel||model instanceof  DependencyModel){
				continue;
			}
			model.detachSource();
			model.detachTarget();
		}
		for (int i = 0; i < ((AbstractUMLEntityModel) rmodel)
				.getModelTargetConnections().size(); i++) {
			AbstractUMLConnectionModel model = (AbstractUMLConnectionModel) ((AbstractUMLEntityModel) rmodel)
					.getModelTargetConnections()
					.get(i);
			if(model instanceof AssociationModel||model instanceof  DependencyModel){
				continue;
			}
			

			model.detachSource();
			model.detachTarget();
		}
		models = new ArrayList<AbstractUMLEntityModel>();
		List<AbstractUMLEntityModel> addedModels = new ArrayList<AbstractUMLEntityModel>();
		for(int i=0;i<types.length;i++){
			AbstractUMLEntityModel entity = createModel(types[i]);
			addedModels.add(entity);
			if(entity != null){
				if(location!=null){
					entity.setConstraint(new Rectangle(
							location.x + (i * 10), 
							location.y + (i * 10), -1, -1));
				} else {
					entity.setConstraint(new Rectangle(10, 10, -1, -1));
				}
				//root.copyPresentation(entity);
				//root.addChild(entity);
				models.add(entity);
			}
		}
		
		addConnections(addedModels);
	}
	
	/**
	 * 
	 * @��������   ��addConnections
	 * @��������   �� ��ӹ�ϵ
	 * @param   ��List<AbstractUMLEntityModel>
	 * @return  ��void
	 * @throws  ����
	 * @since   ��Ver 1.00
	 */
	private void addConnections(List<AbstractUMLEntityModel> addedModels){
		try {
			for(int i=0;i<addedModels.size();i++){
				AbstractUMLEntityModel model = addedModels.get(i);
				if(types[i].isInterface()){
					
					UMLJavaUtils.appendInterfacesConnection(this.root, types[i], model);
				} else {
					UMLJavaUtils.appendSuperClassConnection(this.root, types[i], model);
					UMLJavaUtils.appendInterfacesConnection(this.root, types[i], model);
					UMLJavaUtils.appendAggregationConnection(this.root, types[i], (ClassModel) model);
				}
				UMLJavaUtils.appendSubConnection(root, types[i].getJavaProject(), model);
			}
		} catch(JavaModelException ex){
			UMLPlugin.logException(ex);
		}
	}

	/**
	 * 
	 * @��������   ��createModel
	 * @��������   ������Model
	 * @param   ��IType
	 * @return  ��AbstractUMLEntityModel
	 * @throws  ����
	 * @since   ��Ver 1.00
	 */
	private AbstractUMLEntityModel createModel(IType type){
		try {
			if(type.isInterface()){
				InterfaceModel model =(InterfaceModel)rmodel;
				model.setName(type.getFullyQualifiedParameterizedName());
				AttributeModel[] fields = UMLJavaUtils.getFields(type);
				for(int i=0;i<fields.length;i++){
					model.addChild(fields[i]);
				}
				OperationModel[] methods = UMLJavaUtils.getMethods(type);
				for(int i=0;i<methods.length;i++){
					model.addChild(methods[i]);
				}
				return model;
				
			} else if(type.isClass()){
				ClassModel model = (ClassModel)rmodel;
				model.setName(type.getFullyQualifiedParameterizedName());
				AttributeModel[] attrs = UMLJavaUtils.getFields(type);
				for(int i=0;i<attrs.length;i++){
					model.addChild(attrs[i]);
				}
				OperationModel[] methods = UMLJavaUtils.getMethods(type);
				for(int i=0;i<methods.length;i++){
					model.addChild(methods[i]);
				}
				return model;
			}
		} catch(Exception ex){
			UMLPlugin.logException(ex);
		}
		return null;
	}
	
	/**
	 * 
	 * @��������   ��undo
	 * @��������   ����������
	 * @param   ����
	 * @return  ����
	 * @throws  ����
	 * @since   ��Ver 1.00
	 */
	public void undo(){
		for(AbstractUMLEntityModel model: models){
			for(AbstractUMLConnectionModel conn: model.getModelSourceConnections()){
				conn.detachSource();
				conn.detachTarget();
			}
			for(AbstractUMLConnectionModel conn: model.getModelTargetConnections()){
				conn.detachSource();
				conn.detachTarget();
			}
			this.root.removeChild(model);
		}
	}
}
