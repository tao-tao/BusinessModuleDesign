package com.tansun.runtime.resource.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.model.IWorkbenchAdapter2;

import avicit.ui.runtime.core.cluster.FunctionClusterNode;
import avicit.ui.runtime.core.cluster.FunctionClusterWorkbenchAdapter;
import avicit.ui.runtime.core.cluster.function.ComponentNode;
import avicit.ui.runtime.core.develope.control.ControlNode;
import avicit.ui.runtime.core.develope.control.ControlWorkbenchAdapter;
import avicit.ui.runtime.core.develope.display.ViewNode;
import avicit.ui.runtime.core.develope.display.ViewWorkbenchAdapter;
import avicit.ui.runtime.core.node.AbstractResourceNode;
import avicit.ui.runtime.core.node.BpelNode;
import avicit.ui.runtime.core.node.CommonChildNode;
import avicit.ui.runtime.core.node.CommonNode;
import avicit.ui.runtime.core.node.ComponentLibNode;
import avicit.ui.runtime.core.node.ControllerJavaNode;
import avicit.ui.runtime.core.node.ControllerNode;
import avicit.ui.runtime.core.node.ControllerPageXNode;
import avicit.ui.runtime.core.node.DaoModelNode;
import avicit.ui.runtime.core.node.DaoSpringNode;
import avicit.ui.runtime.core.node.DataMappingNode;
import avicit.ui.runtime.core.node.DataModelNode;
import avicit.ui.runtime.core.node.DataSQLNode;
import avicit.ui.runtime.core.node.DevelopNode;
import avicit.ui.runtime.core.node.JavaBeanNode;
import avicit.ui.runtime.core.node.JavaLibrariesNode;
import avicit.ui.runtime.core.node.JavaSourceNode;
import avicit.ui.runtime.core.node.PackageNode;
import avicit.ui.runtime.core.node.PresentationFormNode;
import avicit.ui.runtime.core.node.PresentationNode;
import avicit.ui.runtime.core.node.ProcessModelNode;
import avicit.ui.runtime.core.node.ProcessTemplateNode;
import avicit.ui.runtime.core.node.ProjectNode;
import avicit.ui.runtime.core.node.ServiceModelNode;
import avicit.ui.runtime.core.node.ServiceNode;
import avicit.ui.runtime.core.node.SpringNode;
import avicit.ui.runtime.core.requirement.analysis.AnalysisNode;
import avicit.ui.runtime.core.requirement.analysis.AnalysisWorkbenchAdapter;
import avicit.ui.runtime.core.requirement.analysis.epc.EpcNode;
import avicit.ui.runtime.core.requirement.analysis.epc.EpcWorkbenchAdapter;
import avicit.ui.runtime.core.requirement.analysis.organization.OrganizationNode;
import avicit.ui.runtime.core.requirement.analysis.organization.OrganizationWorkbenchAdapter;
import avicit.ui.runtime.core.requirement.analysis.usecase.FunctionUseCaseNode;
import avicit.ui.runtime.core.requirement.analysis.usecase.FunctionUseCaseNodeAdapter;
import avicit.ui.runtime.core.requirement.designer.bpmdesigner.BpmDesignerNode;
import avicit.ui.runtime.core.requirement.designer.erm.ErNode;
import avicit.ui.runtime.core.requirement.designer.erm.ErNodeAdaptper;
import avicit.ui.runtime.core.requirement.designer.logicflow.LogicFlowNode;
import avicit.ui.runtime.core.requirement.requirement.RequirementNode;
import avicit.ui.runtime.core.requirement.requirement.RequirementWorkbenchAdapter;
import avicit.ui.runtime.core.requirement.requirement.business.BusinessNode;
import avicit.ui.runtime.core.requirement.requirement.business.BusinessUseCaseNode;
import avicit.ui.runtime.core.requirement.requirement.business.BusinessUseCaseNodeAdapter;
import avicit.ui.runtime.core.requirement.requirement.business.BusinessWorkbenchAdapter;
import avicit.ui.runtime.core.requirement.requirement.document.DocumentNode;
import avicit.ui.runtime.core.requirement.requirement.document.DocumentWorkbenchAdapter;
import avicit.ui.runtime.core.requirement.requirement.function.FunctionStructureNode;
import avicit.ui.runtime.core.requirement.requirement.function.FunctionStructureWorkbenchAdapter;
import avicit.ui.runtime.core.subsystem.SubSystemNode;
import avicit.ui.runtime.core.subsystem.SubSystemWorkbenchAdapter;
import avicit.ui.runtime.core.usecase.common.UseCaseCommonNode;
import avicit.ui.runtime.core.usecase.common.UseCaseCommonNodeAdapter;

public class WorkbenchAdapterFactory implements IAdapterFactory {
	static WorkbenchAdapterFactory instance = new WorkbenchAdapterFactory();

	public WorkbenchAdapterFactory() {
	}

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject == null)
			return null;
		if (IWorkbenchAdapter.class == adapterType
				|| IWorkbenchAdapter2.class == adapterType) {
			if (adaptableObject instanceof JavaLibrariesNode)
				return JavaLibrariesWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof JavaSourceNode)
				return JavaSourceWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof PresentationNode)
				return PresentationWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof ControllerNode)
				return ControllerWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof ServiceNode)
				return ServiceWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof BpelNode)
				return BpelWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof DataModelNode)
				return DataModelWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof ControllerPageXNode)
				return ControllerPageXWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof ControllerJavaNode)
				return ControllerJavaWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof PackageNode)
				return PackageNodeWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof PresentationFormNode)
				return PresentationFormWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof ProcessModelNode)
				return ProcessModelWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof ProcessTemplateNode)
				return ProcessTemplateWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof DataMappingNode)
				return DataMappingWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof ProjectNode)
				return ProjectWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof SpringNode)
				return SpringWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof DataSQLNode)
				return DataSQLWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof ErNode)
				return ErNodeAdaptper.getInstance();
			/*if (adaptableObject instanceof ActivityNode)
				return ActivityNodeAdaptper.getInstance();
			if (adaptableObject instanceof SequenceNode)
				return SequenceNodeAdaptper.getInstance();
//			if (adaptableObject instanceof com.tansun.ec.designer.ermodel.ErNode)
//				return com.tansun.ec.designer.ermodel.ErNodeAdaptper.getInstance();
			if (adaptableObject instanceof ClassNode)
				return ClassNodeAdaptper.getInstance();
			if (adaptableObject instanceof UseCaseNode)
				return UseCaseNodeAdaptper.getInstance();
			if (adaptableObject instanceof com.tansun.ec.top.designer.classuml.ClassNode)
				return com.tansun.ec.top.designer.classuml.ClassNodeAdaptper.getInstance();
			if (adaptableObject instanceof com.tansun.ec.top.designer.usecaseuml.UseCaseNode)
				return com.tansun.ec.top.designer.usecaseuml.UseCaseNodeAdaptper.getInstance();
			if (adaptableObject instanceof com.tansun.ec.top.designer.activityuml.ActivityNode)
				return com.tansun.ec.top.designer.activityuml.ActivityNodeAdaptper.getInstance();
			if (adaptableObject instanceof com.tansun.ec.top.designer.sequenceuml.SequenceNode)
				return com.tansun.ec.top.designer.sequenceuml.SequenceNodeAdaptper.getInstance();*/
			if (adaptableObject instanceof DaoModelNode)
				return DaoModelWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof JavaBeanNode)
				return JavaBeanWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof DaoSpringNode)
				return DaoSpringWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof CommonNode)
				return CommonWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof avicit.ui.runtime.core.requirement.designer.GuizeNode)
				return avicit.ui.runtime.core.requirement.designer.GuizeWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof avicit.ui.runtime.core.requirement.designer.erm.GuizeNode)
				return avicit.ui.runtime.core.requirement.designer.erm.GuizeWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof CommonChildNode)
				return CommonChildWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof ServiceModelNode)
				return ServiceModelWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof ComponentLibNode)
				return ComponentLibWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof AnalysisNode)//分析设计
				return AnalysisWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof EpcNode)//EPC建模
				return EpcWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof SubSystemNode)//组件
				return SubSystemWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof FunctionClusterNode)//功能集
				return FunctionClusterWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof UseCaseCommonNode)//配置文件
				return UseCaseCommonNodeAdapter.getInstance();
			if (adaptableObject instanceof ComponentNode)//功能模块
				return ComponentWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof DevelopNode)//开发
				return DevelopeWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof ViewNode)//开发--展示
				return ViewWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof ControlNode)//开发--展示
				return ControlWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof RequirementNode)//需求
				return RequirementWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof FunctionStructureNode)//需求--功能结构建模
				return FunctionStructureWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof FunctionUseCaseNode)//需求--功能结构建模
				return FunctionUseCaseNodeAdapter.getInstance();
			if (adaptableObject instanceof BusinessUseCaseNode)//需求--功能结构建模
				return BusinessUseCaseNodeAdapter.getInstance();
			if (adaptableObject instanceof BusinessNode)//需求--业务流程建模
				return BusinessWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof DocumentNode)//需求--需求文档
				return DocumentWorkbenchAdapter.getInstance();

//			if (adaptableObject instanceof avicit.ui.runtime.core.develope.service.ServiceNode)//开发--服务
//				return ServiceWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof OrganizationNode)//组织机构建模
				return OrganizationWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof avicit.ui.runtime.core.requirement.requirement.usecase.UseCaseNode)//用例建模
				return avicit.ui.runtime.core.requirement.requirement.usecase.UseCaseWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof ControlNode)
				return ControlWorkbenchAdapter.getInstance();	
	
/*			if (adaptableObject instanceof RfNode)
				return RfAdapter.getInstance();
			if (adaptableObject instanceof DrlNode)
				return DrlWorkbenchAdapter.getInstance();
*/			//----------------------------------------------------
			if(adaptableObject instanceof AbstractResourceNode){
	            return ((AbstractResourceNode)adaptableObject).getAdapter(adaptableObject, adapterType);
	    	}
	    	
	    	//-----------------------------------------------
			if (adaptableObject instanceof BpmDesignerNode)//流程设计
				return avicit.ui.runtime.core.requirement.designer.bpmdesigner.GuizeWorkbenchAdapter.getInstance();
			if (adaptableObject instanceof LogicFlowNode)//逻辑流建模
				return avicit.ui.runtime.core.requirement.designer.logicflow.GuizeWorkbenchAdapter.getInstance();
		}
		return DefaultWorkbenchAdapter.getInstance();
	}

	public Class[] getAdapterList() {
		return classes;
	}

	private static final Class classes[] = { IWorkbenchAdapter.class,
			IWorkbenchAdapter2.class };

	public static WorkbenchAdapterFactory getInstance() {
		return instance;
	}

}