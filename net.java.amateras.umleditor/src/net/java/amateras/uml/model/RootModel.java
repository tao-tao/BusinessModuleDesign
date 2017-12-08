package net.java.amateras.uml.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.properties.BooleanPropertyDescriptor;
import net.java.amateras.uml.sequencediagram.model.ActorModel;
import net.java.amateras.uml.usecasediagram.model.SystemModel;
import net.java.amateras.uml.usecasediagram.model.UsecaseActorModel;
import net.java.amateras.uml.usecasediagram.model.UsecaseModel;
import net.java.amateras.uml.usecasediagram.property.DataField;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.ColorPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

/**
 * The root model of the UML editor.
 * 
 * @author Naoki Takezoe
 */
public class RootModel extends AbstractUMLEntityModel {

	public static final String P_CHILDREN = "_children";
	
	public void copyFrom(RootModel model) {
		getChildren().clear();
		getChildren().addAll(model.getChildren());
		firePropertyChange(P_CHILDREN, null, null);
	}

	public void setBackgroundColor(RGB backgroundColor) {
		super.setBackgroundColor(backgroundColor);
		for (Iterator<AbstractUMLModel> iter = getChildren().iterator(); iter
				.hasNext();) {
			AbstractUMLEntityModel element = (AbstractUMLEntityModel) iter
					.next();
			element.setBackgroundColor(backgroundColor);
			List<AbstractUMLConnectionModel> connections = element
					.getModelSourceConnections();
			for (Iterator<AbstractUMLConnectionModel> iterator = connections
					.iterator(); iterator.hasNext();) {
				AbstractUMLConnectionModel connection = (AbstractUMLConnectionModel) iterator
						.next();
				connection.setBackgroundColor(backgroundColor);
			}
		}
	}

	public void setForegroundColor(RGB foregroundColor) {
		super.setForegroundColor(foregroundColor);
		for (Iterator<AbstractUMLModel> iter = getChildren().iterator(); iter
				.hasNext();) {
			AbstractUMLEntityModel element = (AbstractUMLEntityModel) iter
					.next();
			element.setForegroundColor(foregroundColor);
			List<AbstractUMLConnectionModel> connections = element
					.getModelSourceConnections();
			for (Iterator<AbstractUMLConnectionModel> iterator = connections
					.iterator(); iterator.hasNext();) {
				AbstractUMLConnectionModel connection = (AbstractUMLConnectionModel) iterator
						.next();
				connection.setForegroundColor(foregroundColor);
			}
		}
	}

	public void setFilterProperty(Map<String, Boolean> filterProperty) {
		super.setFilterProperty(filterProperty);
		for (Iterator<AbstractUMLModel> iter = getChildren().iterator(); iter
				.hasNext();) {
			AbstractUMLEntityModel element = (AbstractUMLEntityModel) iter
					.next();
			copyFilter(element);
		}
	}

	public void setShowIcon(boolean showIcon) {
		super.setShowIcon(showIcon);
		for (Iterator<AbstractUMLModel> iter = getChildren().iterator(); iter
				.hasNext();) {
			AbstractUMLEntityModel element = (AbstractUMLEntityModel) iter
					.next();
			element.setShowIcon(showIcon);
		}
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new ColorPropertyDescriptor(P_BACKGROUND_COLOR, UMLPlugin
						.getDefault().getResourceString("property.background")),
				new BooleanPropertyDescriptor(P_SHOW_ICON, UMLPlugin
						.getDefault().getResourceString("property.showicon")) };
	}

	public void copyFilter(AbstractUMLEntityModel model) {
		Map<String, Boolean> newMap = new HashMap<String, Boolean>();
		newMap.putAll(this.getFilterProperty());
		model.setFilterProperty(newMap);
	}

	public List<UsecaseModel> getAllUseCaseModel() {
		 List<UsecaseModel> allCase = new ArrayList<UsecaseModel>();
		
		List<AbstractUMLModel> umlModelList = this.getChildren();
		if (umlModelList != null && umlModelList.size() > 0) {
			for (AbstractUMLModel umlMode : umlModelList) {

				if (umlMode instanceof UsecaseModel) {
					allCase.add((UsecaseModel) umlMode);
				} else if (umlMode instanceof SystemModel) {
					SystemModel smodel = (SystemModel) umlMode;
					getCases(smodel,allCase);
				}
			}
		}
		return allCase;
	}
	public List<UsecaseActorModel> getAllActorModel() {
		 List<UsecaseActorModel> allActor = new ArrayList<UsecaseActorModel>();
		List<AbstractUMLModel> umlModelList = this.getChildren();
		if (umlModelList != null && umlModelList.size() > 0) {
			for (AbstractUMLModel umlMode : umlModelList) {

				if (umlMode instanceof UsecaseActorModel) {
					allActor.add((UsecaseActorModel) umlMode);
				} else if (umlMode instanceof SystemModel) {
					SystemModel smodel = (SystemModel) umlMode;
					getActors(smodel,allActor);
				}
			}
		}
		return allActor;
	}

	public void getCases(SystemModel smodel,List<UsecaseModel> allcase) {
		List<AbstractUMLModel> umlModelList = smodel.getChildren();
		if (umlModelList != null && umlModelList.size() > 0) {
			for (AbstractUMLModel umlMode : umlModelList) {

				if (umlMode instanceof UsecaseModel) {
					// 业务处理流程
					allcase.add((UsecaseModel) umlMode);
				} else if (umlMode instanceof SystemModel) {
					getCases((SystemModel)umlMode,allcase);
				}
			}
		}
	}
	public void getActors(SystemModel smodel,List<UsecaseActorModel> allactor) {
		List<AbstractUMLModel> umlModelList = smodel.getChildren();
		if (umlModelList != null && umlModelList.size() > 0) {
			for (AbstractUMLModel umlMode : umlModelList) {

				if (umlMode instanceof UsecaseActorModel) {
					// 业务处理流程
					allactor.add((UsecaseActorModel) umlMode);
				} else if (umlMode instanceof SystemModel) {
					getActors((SystemModel)umlMode,allactor);
				}
			}
		}
	}

}
