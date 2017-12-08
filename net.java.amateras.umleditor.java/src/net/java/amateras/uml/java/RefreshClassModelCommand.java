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
 * @类名称: RefreshClassModelCommand.java
 * @类描述: 刷新类
 * @作者: 史辉丰
 * @创建时间: 2013-11-6 上午10:28:01
 * @版本: 1.00
 * 
 * @修改记录: <p>
 *        版本 修改人 修改时间 修改内容描述<br>
 *        ----------------------------------------<br>
 *        1.00 user 2013-11-6 上午10:28:01<br>
 *        ----------------------------------------<br>
 *        </p>
 */
public class RefreshClassModelCommand extends Command {

	/**
	 * 对应的实体文件
	 */
	private IType[] types;
	/**
	 * 根Model
	 */
	private RootModel root;
	/**
	 * 创建Model
	 */
	private List<AbstractUMLEntityModel> models;
	/**
	 * 定位
	 */
	private Point location;
	/**
	 * 传入模型
	 */
	 public AbstractUMLEntityModel rmodel;
	public AbstractUMLEntityModel getModel() {
		return rmodel;
	}

	public void setModel(AbstractUMLEntityModel model) {
		this.rmodel = model;
	}

	/**
	 * 构造同步构造函数
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
	 * @方法名称    ：setLocation
	 * @功能描述    ：设置位置
	 * @param   ：无
	 * @return  ：void
	 * @throws  ：无
	 * @since   ：Ver 1.00
	 */
	public void setLocation(Point location){
		this.location = location;
	}
	
	/**
	 * 
	 * @方法名称   ：execute
	 * @功能描述   ：执行同步
	 * @param   ：无
	 * @return  ：void
	 * @throws  ：无
	 * @since   ：Ver 1.00
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
	 * @方法名称   ：addConnections
	 * @功能描述   ： 添加关系
	 * @param   ：List<AbstractUMLEntityModel>
	 * @return  ：void
	 * @throws  ：无
	 * @since   ：Ver 1.00
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
	 * @方法名称   ：createModel
	 * @功能描述   ：创建Model
	 * @param   ：IType
	 * @return  ：AbstractUMLEntityModel
	 * @throws  ：无
	 * @since   ：Ver 1.00
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
	 * @方法名称   ：undo
	 * @功能描述   ：撤销操作
	 * @param   ：无
	 * @return  ：无
	 * @throws  ：无
	 * @since   ：Ver 1.00
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
