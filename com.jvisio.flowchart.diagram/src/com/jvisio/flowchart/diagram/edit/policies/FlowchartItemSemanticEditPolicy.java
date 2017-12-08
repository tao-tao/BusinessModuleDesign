package com.jvisio.flowchart.diagram.edit.policies;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.commands.DuplicateEObjectsCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import org.eclipse.gmf.runtime.notation.View;

import com.jvisio.flowchart.FlowchartPackage;
import com.jvisio.flowchart.diagram.providers.FlowchartElementTypes;

/**
 * @generated
 */
public class FlowchartItemSemanticEditPolicy extends
		FlowchartBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	protected Command getCreateCommand(CreateElementRequest req) {
		if (FlowchartElementTypes.Terminator_1001 == req.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(FlowchartPackage.eINSTANCE
						.getFlowchart_Elements());
			}
			return getMSLWrapper(new CreateTerminator_1001Command(req));
		}
		if (FlowchartElementTypes.Action_1002 == req.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(FlowchartPackage.eINSTANCE
						.getFlowchart_Elements());
			}
			return getMSLWrapper(new CreateAction_1002Command(req));
		}
		if (FlowchartElementTypes.Decision_1003 == req.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(FlowchartPackage.eINSTANCE
						.getFlowchart_Elements());
			}
			return getMSLWrapper(new CreateDecision_1003Command(req));
		}
		if (FlowchartElementTypes.Subprocess_1004 == req.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(FlowchartPackage.eINSTANCE
						.getFlowchart_Elements());
			}
			return getMSLWrapper(new CreateSubprocess_1004Command(req));
		}
		if (FlowchartElementTypes.Connector_1005 == req.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(FlowchartPackage.eINSTANCE
						.getFlowchart_Elements());
			}
			return getMSLWrapper(new CreateConnector_1005Command(req));
		}
		if (FlowchartElementTypes.Wait_1006 == req.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(FlowchartPackage.eINSTANCE
						.getFlowchart_Elements());
			}
			return getMSLWrapper(new CreateWait_1006Command(req));
		}
		return super.getCreateCommand(req);
	}

	/**
	 * @generated
	 */
	private static class CreateTerminator_1001Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateTerminator_1001Command(CreateElementRequest req) {
			super(req);
		}

		/**
		 * @generated
		 */
		protected EClass getEClassToEdit() {
			return FlowchartPackage.eINSTANCE.getFlowchart();
		};

		/**
		 * @generated
		 */
		protected EObject getElementToEdit() {
			EObject container = ((CreateElementRequest) getRequest())
					.getContainer();
			if (container instanceof View) {
				container = ((View) container).getElement();
			}
			return container;
		}
	}

	/**
	 * @generated
	 */
	private static class CreateAction_1002Command extends CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateAction_1002Command(CreateElementRequest req) {
			super(req);
		}

		/**
		 * @generated
		 */
		protected EClass getEClassToEdit() {
			return FlowchartPackage.eINSTANCE.getFlowchart();
		};

		/**
		 * @generated
		 */
		protected EObject getElementToEdit() {
			EObject container = ((CreateElementRequest) getRequest())
					.getContainer();
			if (container instanceof View) {
				container = ((View) container).getElement();
			}
			return container;
		}
	}

	/**
	 * @generated
	 */
	private static class CreateDecision_1003Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateDecision_1003Command(CreateElementRequest req) {
			super(req);
		}

		/**
		 * @generated
		 */
		protected EClass getEClassToEdit() {
			return FlowchartPackage.eINSTANCE.getFlowchart();
		};

		/**
		 * @generated
		 */
		protected EObject getElementToEdit() {
			EObject container = ((CreateElementRequest) getRequest())
					.getContainer();
			if (container instanceof View) {
				container = ((View) container).getElement();
			}
			return container;
		}
	}

	/**
	 * @generated
	 */
	private static class CreateSubprocess_1004Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateSubprocess_1004Command(CreateElementRequest req) {
			super(req);
		}

		/**
		 * @generated
		 */
		protected EClass getEClassToEdit() {
			return FlowchartPackage.eINSTANCE.getFlowchart();
		};

		/**
		 * @generated
		 */
		protected EObject getElementToEdit() {
			EObject container = ((CreateElementRequest) getRequest())
					.getContainer();
			if (container instanceof View) {
				container = ((View) container).getElement();
			}
			return container;
		}
	}

	/**
	 * @generated
	 */
	private static class CreateConnector_1005Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateConnector_1005Command(CreateElementRequest req) {
			super(req);
		}

		/**
		 * @generated
		 */
		protected EClass getEClassToEdit() {
			return FlowchartPackage.eINSTANCE.getFlowchart();
		};

		/**
		 * @generated
		 */
		protected EObject getElementToEdit() {
			EObject container = ((CreateElementRequest) getRequest())
					.getContainer();
			if (container instanceof View) {
				container = ((View) container).getElement();
			}
			return container;
		}
	}

	/**
	 * @generated
	 */
	private static class CreateWait_1006Command extends CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateWait_1006Command(CreateElementRequest req) {
			super(req);
		}

		/**
		 * @generated
		 */
		protected EClass getEClassToEdit() {
			return FlowchartPackage.eINSTANCE.getFlowchart();
		};

		/**
		 * @generated
		 */
		protected EObject getElementToEdit() {
			EObject container = ((CreateElementRequest) getRequest())
					.getContainer();
			if (container instanceof View) {
				container = ((View) container).getElement();
			}
			return container;
		}
	}

	/**
	 * @generated
	 */
	protected Command getDuplicateCommand(DuplicateElementsRequest req) {
		TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
				.getEditingDomain();
		return getMSLWrapper(new DuplicateAnythingCommand(editingDomain, req));
	}

	/**
	 * @generated
	 */
	private static class DuplicateAnythingCommand extends
			DuplicateEObjectsCommand {

		/**
		 * @generated
		 */
		public DuplicateAnythingCommand(
				TransactionalEditingDomain editingDomain,
				DuplicateElementsRequest req) {
			super(editingDomain, req.getLabel(), req
					.getElementsToBeDuplicated(), req
					.getAllDuplicatedElementsMap());
		}
	}
}
