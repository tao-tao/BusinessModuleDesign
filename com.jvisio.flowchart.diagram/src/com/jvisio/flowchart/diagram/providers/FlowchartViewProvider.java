package com.jvisio.flowchart.diagram.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;

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
import com.jvisio.flowchart.diagram.part.FlowchartVisualIDRegistry;
import com.jvisio.flowchart.diagram.view.factories.ActionNameViewFactory;
import com.jvisio.flowchart.diagram.view.factories.ActionViewFactory;
import com.jvisio.flowchart.diagram.view.factories.ConnectorNameViewFactory;
import com.jvisio.flowchart.diagram.view.factories.ConnectorViewFactory;
import com.jvisio.flowchart.diagram.view.factories.DecisionNameViewFactory;
import com.jvisio.flowchart.diagram.view.factories.DecisionViewFactory;
import com.jvisio.flowchart.diagram.view.factories.FlowchartViewFactory;
import com.jvisio.flowchart.diagram.view.factories.SubprocessNameViewFactory;
import com.jvisio.flowchart.diagram.view.factories.SubprocessViewFactory;
import com.jvisio.flowchart.diagram.view.factories.TerminatorNameViewFactory;
import com.jvisio.flowchart.diagram.view.factories.TerminatorViewFactory;
import com.jvisio.flowchart.diagram.view.factories.TransitionNameViewFactory;
import com.jvisio.flowchart.diagram.view.factories.TransitionViewFactory;
import com.jvisio.flowchart.diagram.view.factories.WaitNameViewFactory;
import com.jvisio.flowchart.diagram.view.factories.WaitViewFactory;

/**
 * @generated
 */
public class FlowchartViewProvider extends AbstractViewProvider {

	/**
	 * @generated
	 */
	protected Class getDiagramViewClass(IAdaptable semanticAdapter,
			String diagramKind) {
		EObject semanticElement = getSemanticElement(semanticAdapter);
		if (FlowchartEditPart.MODEL_ID.equals(diagramKind)
				&& FlowchartVisualIDRegistry
						.getDiagramVisualID(semanticElement) != -1) {
			return FlowchartViewFactory.class;
		}
		return null;
	}

	/**
	 * @generated
	 */
	protected Class getNodeViewClass(IAdaptable semanticAdapter,
			View containerView, String semanticHint) {
		if (containerView == null) {
			return null;
		}
		IElementType elementType = getSemanticElementType(semanticAdapter);
		if (elementType != null
				&& !FlowchartElementTypes.isKnownElementType(elementType)) {
			return null;
		}
		EClass semanticType = getSemanticEClass(semanticAdapter);
		EObject semanticElement = getSemanticElement(semanticAdapter);
		int nodeVID = FlowchartVisualIDRegistry.getNodeVisualID(containerView,
				semanticElement, semanticType, semanticHint);
		switch (nodeVID) {
		case TerminatorEditPart.VISUAL_ID:
			return TerminatorViewFactory.class;
		case TerminatorNameEditPart.VISUAL_ID:
			return TerminatorNameViewFactory.class;
		case ActionEditPart.VISUAL_ID:
			return ActionViewFactory.class;
		case ActionNameEditPart.VISUAL_ID:
			return ActionNameViewFactory.class;
		case DecisionEditPart.VISUAL_ID:
			return DecisionViewFactory.class;
		case DecisionNameEditPart.VISUAL_ID:
			return DecisionNameViewFactory.class;
		case SubprocessEditPart.VISUAL_ID:
			return SubprocessViewFactory.class;
		case SubprocessNameEditPart.VISUAL_ID:
			return SubprocessNameViewFactory.class;
		case ConnectorEditPart.VISUAL_ID:
			return ConnectorViewFactory.class;
		case ConnectorNameEditPart.VISUAL_ID:
			return ConnectorNameViewFactory.class;
		case WaitEditPart.VISUAL_ID:
			return WaitViewFactory.class;
		case WaitNameEditPart.VISUAL_ID:
			return WaitNameViewFactory.class;
		case TransitionNameEditPart.VISUAL_ID:
			return TransitionNameViewFactory.class;
		}
		return null;
	}

	/**
	 * @generated
	 */
	protected Class getEdgeViewClass(IAdaptable semanticAdapter,
			View containerView, String semanticHint) {
		IElementType elementType = getSemanticElementType(semanticAdapter);
		if (elementType != null
				&& !FlowchartElementTypes.isKnownElementType(elementType)) {
			return null;
		}
		EClass semanticType = getSemanticEClass(semanticAdapter);
		if (semanticType == null) {
			return null;
		}
		EObject semanticElement = getSemanticElement(semanticAdapter);
		int linkVID = FlowchartVisualIDRegistry.getLinkWithClassVisualID(
				semanticElement, semanticType);
		switch (linkVID) {
		case TransitionEditPart.VISUAL_ID:
			return TransitionViewFactory.class;
		}
		return getUnrecognizedConnectorViewClass(semanticAdapter,
				containerView, semanticHint);
	}

	/**
	 * @generated
	 */
	private IElementType getSemanticElementType(IAdaptable semanticAdapter) {
		if (semanticAdapter == null) {
			return null;
		}
		return (IElementType) semanticAdapter.getAdapter(IElementType.class);
	}

	/**
	 * @generated
	 */
	private Class getUnrecognizedConnectorViewClass(IAdaptable semanticAdapter,
			View containerView, String semanticHint) {
		// Handle unrecognized child node classes here
		return null;
	}

}
