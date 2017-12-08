package com.jvisio.flowchart.diagram.edit.policies;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateRelationshipCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.notation.View;

import com.jvisio.flowchart.FlowElement;
import com.jvisio.flowchart.Flowchart;
import com.jvisio.flowchart.FlowchartPackage;
import com.jvisio.flowchart.Transition;
import com.jvisio.flowchart.diagram.providers.FlowchartElementTypes;

/**
 * @generated
 */
public class WaitItemSemanticEditPolicy extends
		FlowchartBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	protected Command getDestroyElementCommand(DestroyElementRequest req) {
		return getMSLWrapper(new DestroyElementCommand(req) {

			protected EObject getElementToDestroy() {
				View view = (View) getHost().getModel();
				EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
				if (annotation != null) {
					return view;
				}
				return super.getElementToDestroy();
			}

		});
	}

	/**
	 * @generated
	 */
	protected Command getCreateRelationshipCommand(CreateRelationshipRequest req) {
		if (FlowchartElementTypes.Transition_3001 == req.getElementType()) {
			return req.getTarget() == null ? getCreateStartOutgoingTransition3001Command(req)
					: getCreateCompleteIncomingTransition3001Command(req);
		}
		return super.getCreateRelationshipCommand(req);
	}

	/**
	 * @generated
	 */
	protected Command getCreateStartOutgoingTransition3001Command(
			CreateRelationshipRequest req) {
		return new Command() {
		};
	}

	/**
	 * @generated
	 */
	protected Command getCreateCompleteIncomingTransition3001Command(
			CreateRelationshipRequest req) {
		if (!(req.getSource() instanceof FlowElement)) {
			return UnexecutableCommand.INSTANCE;
		}
		final Flowchart element = (Flowchart) getRelationshipContainer(req
				.getSource(), FlowchartPackage.eINSTANCE.getFlowchart(), req
				.getElementType());
		if (element == null) {
			return UnexecutableCommand.INSTANCE;
		}
		if (req.getContainmentFeature() == null) {
			req.setContainmentFeature(FlowchartPackage.eINSTANCE
					.getFlowchart_Transitions());
		}
		return getMSLWrapper(new CreateIncomingTransition3001Command(req) {

			/**
			 * @generated
			 */
			protected EObject getElementToEdit() {
				return element;
			}
		});
	}

	/**
	 * @generated
	 */
	private static class CreateIncomingTransition3001Command extends
			CreateRelationshipCommand {

		/**
		 * @generated
		 */
		public CreateIncomingTransition3001Command(CreateRelationshipRequest req) {
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
		protected void setElementToEdit(EObject element) {
			throw new UnsupportedOperationException();
		}

		/**
		 * @generated
		 */
		protected EObject doDefaultElementCreation() {
			Transition newElement = (Transition) super
					.doDefaultElementCreation();
			if (newElement != null) {
				newElement.setTarget((FlowElement) getTarget());
				newElement.setSource((FlowElement) getSource());
			}
			return newElement;
		}
	}
}
