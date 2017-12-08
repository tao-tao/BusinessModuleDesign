package com.jvisio.flowchart.diagram.part;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

import com.jvisio.flowchart.Action;
import com.jvisio.flowchart.Connector;
import com.jvisio.flowchart.Decision;
import com.jvisio.flowchart.Flowchart;
import com.jvisio.flowchart.FlowchartPackage;
import com.jvisio.flowchart.Subprocess;
import com.jvisio.flowchart.Terminator;
import com.jvisio.flowchart.Transition;
import com.jvisio.flowchart.Wait;
import com.jvisio.flowchart.diagram.edit.parts.ActionEditPart;
import com.jvisio.flowchart.diagram.edit.parts.ActionNameEditPart;
import com.jvisio.flowchart.diagram.edit.parts.ConnectorEditPart;
import com.jvisio.flowchart.diagram.edit.parts.ConnectorNameEditPart;
import com.jvisio.flowchart.diagram.edit.parts.DecisionEditPart;
import com.jvisio.flowchart.diagram.edit.parts.DecisionNameEditPart;
import com.jvisio.flowchart.diagram.edit.parts.FlowchartEditPart;
import com.jvisio.flowchart.diagram.edit.parts.SubprocessEditPart;
import com.jvisio.flowchart.diagram.edit.parts.SubprocessNameEditPart;
import com.jvisio.flowchart.diagram.edit.parts.TerminatorEditPart;
import com.jvisio.flowchart.diagram.edit.parts.TerminatorNameEditPart;
import com.jvisio.flowchart.diagram.edit.parts.TransitionEditPart;
import com.jvisio.flowchart.diagram.edit.parts.TransitionNameEditPart;
import com.jvisio.flowchart.diagram.edit.parts.WaitEditPart;
import com.jvisio.flowchart.diagram.edit.parts.WaitNameEditPart;

/**
 * This registry is used to determine which type of visual object should be
 * created for the corresponding Diagram, Node, ChildNode or Link represented 
 * by a domain model object.
 *
 * @generated
 */
public class FlowchartVisualIDRegistry {

	/**
	 * @generated
	 */
	private static final String DEBUG_KEY = FlowchartDiagramEditorPlugin
			.getInstance().getBundle().getSymbolicName()
			+ "/debug/visualID"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static int getVisualID(View view) {
		if (view instanceof Diagram) {
			if (FlowchartEditPart.MODEL_ID.equals(view.getType())) {
				return FlowchartEditPart.VISUAL_ID;
			} else {
				return -1;
			}
		}
		return getVisualID(view.getType());
	}

	/**
	 * @generated
	 */
	public static String getModelID(View view) {
		View diagram = view.getDiagram();
		while (view != diagram) {
			EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
			if (annotation != null) {
				return (String) annotation.getDetails().get("modelID"); //$NON-NLS-1$
			}
			view = (View) view.eContainer();
		}
		return diagram != null ? diagram.getType() : null;
	}

	/**
	 * @generated
	 */
	public static int getVisualID(String type) {
		try {
			return Integer.parseInt(type);
		} catch (NumberFormatException e) {
			if (Boolean.TRUE.toString().equalsIgnoreCase(
					Platform.getDebugOption(DEBUG_KEY))) {
				FlowchartDiagramEditorPlugin.getInstance().logError(
						"Unable to parse view type as a visualID number: "
								+ type);
			}
		}
		return -1;
	}

	/**
	 * @generated
	 */
	public static String getType(int visualID) {
		return String.valueOf(visualID);
	}

	/**
	 * @generated
	 */
	public static int getDiagramVisualID(EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		EClass domainElementMetaclass = domainElement.eClass();
		return getDiagramVisualID(domainElement, domainElementMetaclass);
	}

	/**
	 * @generated
	 */
	private static int getDiagramVisualID(EObject domainElement,
			EClass domainElementMetaclass) {
		if (FlowchartPackage.eINSTANCE.getFlowchart().isSuperTypeOf(
				domainElementMetaclass)
				&& isDiagramFlowchart_79((Flowchart) domainElement)) {
			return FlowchartEditPart.VISUAL_ID;
		}
		return getUnrecognizedDiagramID(domainElement);
	}

	/**
	 * @generated
	 */
	public static int getNodeVisualID(View containerView, EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		EClass domainElementMetaclass = domainElement.eClass();
		return getNodeVisualID(containerView, domainElement,
				domainElementMetaclass, null);
	}

	/**
	 * @generated
	 */
	public static int getNodeVisualID(View containerView,
			EObject domainElement, EClass domainElementMetaclass,
			String semanticHint) {
		String containerModelID = getModelID(containerView);
		if (!FlowchartEditPart.MODEL_ID.equals(containerModelID)) {
			return -1;
		}
		int containerVisualID;
		if (FlowchartEditPart.MODEL_ID.equals(containerModelID)) {
			containerVisualID = getVisualID(containerView);
		} else {
			if (containerView instanceof Diagram) {
				containerVisualID = FlowchartEditPart.VISUAL_ID;
			} else {
				return -1;
			}
		}
		int nodeVisualID = semanticHint != null ? getVisualID(semanticHint)
				: -1;
		switch (containerVisualID) {
		case TerminatorEditPart.VISUAL_ID:
			if (TerminatorNameEditPart.VISUAL_ID == nodeVisualID) {
				return TerminatorNameEditPart.VISUAL_ID;
			}
			return getUnrecognizedTerminator_1001ChildNodeID(domainElement,
					semanticHint);
		case ActionEditPart.VISUAL_ID:
			if (ActionNameEditPart.VISUAL_ID == nodeVisualID) {
				return ActionNameEditPart.VISUAL_ID;
			}
			return getUnrecognizedAction_1002ChildNodeID(domainElement,
					semanticHint);
		case DecisionEditPart.VISUAL_ID:
			if (DecisionNameEditPart.VISUAL_ID == nodeVisualID) {
				return DecisionNameEditPart.VISUAL_ID;
			}
			return getUnrecognizedDecision_1003ChildNodeID(domainElement,
					semanticHint);
		case SubprocessEditPart.VISUAL_ID:
			if (SubprocessNameEditPart.VISUAL_ID == nodeVisualID) {
				return SubprocessNameEditPart.VISUAL_ID;
			}
			return getUnrecognizedSubprocess_1004ChildNodeID(domainElement,
					semanticHint);
		case ConnectorEditPart.VISUAL_ID:
			if (ConnectorNameEditPart.VISUAL_ID == nodeVisualID) {
				return ConnectorNameEditPart.VISUAL_ID;
			}
			return getUnrecognizedConnector_1005ChildNodeID(domainElement,
					semanticHint);
		case WaitEditPart.VISUAL_ID:
			if (WaitNameEditPart.VISUAL_ID == nodeVisualID) {
				return WaitNameEditPart.VISUAL_ID;
			}
			return getUnrecognizedWait_1006ChildNodeID(domainElement,
					semanticHint);
		case FlowchartEditPart.VISUAL_ID:
			if ((semanticHint == null || TerminatorEditPart.VISUAL_ID == nodeVisualID)
					&& FlowchartPackage.eINSTANCE.getTerminator()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeTerminator_1001((Terminator) domainElement))) {
				return TerminatorEditPart.VISUAL_ID;
			}
			if ((semanticHint == null || ActionEditPart.VISUAL_ID == nodeVisualID)
					&& FlowchartPackage.eINSTANCE.getAction().isSuperTypeOf(
							domainElementMetaclass)
					&& (domainElement == null || isNodeAction_1002((Action) domainElement))) {
				return ActionEditPart.VISUAL_ID;
			}
			if ((semanticHint == null || DecisionEditPart.VISUAL_ID == nodeVisualID)
					&& FlowchartPackage.eINSTANCE.getDecision().isSuperTypeOf(
							domainElementMetaclass)
					&& (domainElement == null || isNodeDecision_1003((Decision) domainElement))) {
				return DecisionEditPart.VISUAL_ID;
			}
			if ((semanticHint == null || SubprocessEditPart.VISUAL_ID == nodeVisualID)
					&& FlowchartPackage.eINSTANCE.getSubprocess()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeSubprocess_1004((Subprocess) domainElement))) {
				return SubprocessEditPart.VISUAL_ID;
			}
			if ((semanticHint == null || ConnectorEditPart.VISUAL_ID == nodeVisualID)
					&& FlowchartPackage.eINSTANCE.getConnector().isSuperTypeOf(
							domainElementMetaclass)
					&& (domainElement == null || isNodeConnector_1005((Connector) domainElement))) {
				return ConnectorEditPart.VISUAL_ID;
			}
			if ((semanticHint == null || WaitEditPart.VISUAL_ID == nodeVisualID)
					&& FlowchartPackage.eINSTANCE.getWait().isSuperTypeOf(
							domainElementMetaclass)
					&& (domainElement == null || isNodeWait_1006((Wait) domainElement))) {
				return WaitEditPart.VISUAL_ID;
			}
			return getUnrecognizedFlowchart_79ChildNodeID(domainElement,
					semanticHint);
		case TransitionEditPart.VISUAL_ID:
			if (TransitionNameEditPart.VISUAL_ID == nodeVisualID) {
				return TransitionNameEditPart.VISUAL_ID;
			}
			return getUnrecognizedTransition_3001LinkLabelID(semanticHint);
		}
		return -1;
	}

	/**
	 * @generated
	 */
	public static int getLinkWithClassVisualID(EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		EClass domainElementMetaclass = domainElement.eClass();
		return getLinkWithClassVisualID(domainElement, domainElementMetaclass);
	}

	/**
	 * @generated
	 */
	public static int getLinkWithClassVisualID(EObject domainElement,
			EClass domainElementMetaclass) {
		if (FlowchartPackage.eINSTANCE.getTransition().isSuperTypeOf(
				domainElementMetaclass)
				&& (domainElement == null || isLinkWithClassTransition_3001((Transition) domainElement))) {
			return TransitionEditPart.VISUAL_ID;
		} else {
			return getUnrecognizedLinkWithClassID(domainElement);
		}
	}

	/**
	 * User can change implementation of this method to check some additional 
	 * conditions here.
	 *
	 * @generated
	 */
	private static boolean isDiagramFlowchart_79(Flowchart element) {
		return true;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 *
	 * @generated
	 */
	private static int getUnrecognizedDiagramID(EObject domainElement) {
		return -1;
	}

	/**
	 * User can change implementation of this method to check some additional 
	 * conditions here.
	 *
	 * @generated
	 */
	private static boolean isNodeTerminator_1001(Terminator element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional 
	 * conditions here.
	 *
	 * @generated
	 */
	private static boolean isNodeAction_1002(Action element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional 
	 * conditions here.
	 *
	 * @generated
	 */
	private static boolean isNodeDecision_1003(Decision element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional 
	 * conditions here.
	 *
	 * @generated
	 */
	private static boolean isNodeSubprocess_1004(Subprocess element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional 
	 * conditions here.
	 *
	 * @generated
	 */
	private static boolean isNodeConnector_1005(Connector element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional 
	 * conditions here.
	 *
	 * @generated
	 */
	private static boolean isNodeWait_1006(Wait element) {
		return true;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 *
	 * @generated
	 */
	private static int getUnrecognizedTerminator_1001ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 *
	 * @generated
	 */
	private static int getUnrecognizedAction_1002ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 *
	 * @generated
	 */
	private static int getUnrecognizedDecision_1003ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 *
	 * @generated
	 */
	private static int getUnrecognizedSubprocess_1004ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 *
	 * @generated
	 */
	private static int getUnrecognizedConnector_1005ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 *
	 * @generated
	 */
	private static int getUnrecognizedWait_1006ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 *
	 * @generated
	 */
	private static int getUnrecognizedFlowchart_79ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 *
	 * @generated
	 */
	private static int getUnrecognizedTransition_3001LinkLabelID(
			String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 *
	 * @generated
	 */
	private static int getUnrecognizedLinkWithClassID(EObject domainElement) {
		return -1;
	}

	/**
	 * User can change implementation of this method to check some additional 
	 * conditions here.
	 *
	 * @generated
	 */
	private static boolean isLinkWithClassTransition_3001(Transition element) {
		return true;
	}
}
