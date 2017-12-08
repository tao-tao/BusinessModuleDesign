package com.jvisio.flowchart.diagram.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.GetParserOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider;
import org.eclipse.gmf.runtime.notation.View;

import com.jvisio.flowchart.FlowchartPackage;
import com.jvisio.flowchart.diagram.edit.parts.ActionNameEditPart;
import com.jvisio.flowchart.diagram.edit.parts.ConnectorNameEditPart;
import com.jvisio.flowchart.diagram.edit.parts.DecisionNameEditPart;
import com.jvisio.flowchart.diagram.edit.parts.SubprocessNameEditPart;
import com.jvisio.flowchart.diagram.edit.parts.TerminatorNameEditPart;
import com.jvisio.flowchart.diagram.edit.parts.TransitionNameEditPart;
import com.jvisio.flowchart.diagram.edit.parts.WaitNameEditPart;
import com.jvisio.flowchart.diagram.part.FlowchartVisualIDRegistry;

/**
 * @generated
 */
public class FlowchartParserProvider extends AbstractProvider implements
		IParserProvider {

	/**
	 * @generated
	 */
	private IParser terminatorTerminatorName_4001Parser;

	/**
	 * @generated
	 */
	private IParser getTerminatorTerminatorName_4001Parser() {
		if (terminatorTerminatorName_4001Parser == null) {
			terminatorTerminatorName_4001Parser = createTerminatorTerminatorName_4001Parser();
		}
		return terminatorTerminatorName_4001Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createTerminatorTerminatorName_4001Parser() {
		FlowchartStructuralFeatureParser parser = new FlowchartStructuralFeatureParser(
				FlowchartPackage.eINSTANCE.getFlowElement()
						.getEStructuralFeature("name")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser actionActionName_4002Parser;

	/**
	 * @generated
	 */
	private IParser getActionActionName_4002Parser() {
		if (actionActionName_4002Parser == null) {
			actionActionName_4002Parser = createActionActionName_4002Parser();
		}
		return actionActionName_4002Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createActionActionName_4002Parser() {
		FlowchartStructuralFeatureParser parser = new FlowchartStructuralFeatureParser(
				FlowchartPackage.eINSTANCE.getFlowElement()
						.getEStructuralFeature("name")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser decisionDecisionName_4003Parser;

	/**
	 * @generated
	 */
	private IParser getDecisionDecisionName_4003Parser() {
		if (decisionDecisionName_4003Parser == null) {
			decisionDecisionName_4003Parser = createDecisionDecisionName_4003Parser();
		}
		return decisionDecisionName_4003Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createDecisionDecisionName_4003Parser() {
		FlowchartStructuralFeatureParser parser = new FlowchartStructuralFeatureParser(
				FlowchartPackage.eINSTANCE.getFlowElement()
						.getEStructuralFeature("name")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser subprocessSubprocessName_4004Parser;

	/**
	 * @generated
	 */
	private IParser getSubprocessSubprocessName_4004Parser() {
		if (subprocessSubprocessName_4004Parser == null) {
			subprocessSubprocessName_4004Parser = createSubprocessSubprocessName_4004Parser();
		}
		return subprocessSubprocessName_4004Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createSubprocessSubprocessName_4004Parser() {
		FlowchartStructuralFeatureParser parser = new FlowchartStructuralFeatureParser(
				FlowchartPackage.eINSTANCE.getFlowElement()
						.getEStructuralFeature("name")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser connectorConnectorName_4005Parser;

	/**
	 * @generated
	 */
	private IParser getConnectorConnectorName_4005Parser() {
		if (connectorConnectorName_4005Parser == null) {
			connectorConnectorName_4005Parser = createConnectorConnectorName_4005Parser();
		}
		return connectorConnectorName_4005Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createConnectorConnectorName_4005Parser() {
		FlowchartStructuralFeatureParser parser = new FlowchartStructuralFeatureParser(
				FlowchartPackage.eINSTANCE.getFlowElement()
						.getEStructuralFeature("name")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser waitWaitName_4006Parser;

	/**
	 * @generated
	 */
	private IParser getWaitWaitName_4006Parser() {
		if (waitWaitName_4006Parser == null) {
			waitWaitName_4006Parser = createWaitWaitName_4006Parser();
		}
		return waitWaitName_4006Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createWaitWaitName_4006Parser() {
		FlowchartStructuralFeatureParser parser = new FlowchartStructuralFeatureParser(
				FlowchartPackage.eINSTANCE.getFlowElement()
						.getEStructuralFeature("name")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser transitionTransitionName_4007Parser;

	/**
	 * @generated
	 */
	private IParser getTransitionTransitionName_4007Parser() {
		if (transitionTransitionName_4007Parser == null) {
			transitionTransitionName_4007Parser = createTransitionTransitionName_4007Parser();
		}
		return transitionTransitionName_4007Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createTransitionTransitionName_4007Parser() {
		FlowchartStructuralFeatureParser parser = new FlowchartStructuralFeatureParser(
				FlowchartPackage.eINSTANCE.getTransition()
						.getEStructuralFeature("name")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	protected IParser getParser(int visualID) {
		switch (visualID) {
		case TerminatorNameEditPart.VISUAL_ID:
			return getTerminatorTerminatorName_4001Parser();
		case ActionNameEditPart.VISUAL_ID:
			return getActionActionName_4002Parser();
		case DecisionNameEditPart.VISUAL_ID:
			return getDecisionDecisionName_4003Parser();
		case SubprocessNameEditPart.VISUAL_ID:
			return getSubprocessSubprocessName_4004Parser();
		case ConnectorNameEditPart.VISUAL_ID:
			return getConnectorConnectorName_4005Parser();
		case WaitNameEditPart.VISUAL_ID:
			return getWaitWaitName_4006Parser();
		case TransitionNameEditPart.VISUAL_ID:
			return getTransitionTransitionName_4007Parser();
		}
		return null;
	}

	/**
	 * @generated
	 */
	public IParser getParser(IAdaptable hint) {
		String vid = (String) hint.getAdapter(String.class);
		if (vid != null) {
			return getParser(FlowchartVisualIDRegistry.getVisualID(vid));
		}
		View view = (View) hint.getAdapter(View.class);
		if (view != null) {
			return getParser(FlowchartVisualIDRegistry.getVisualID(view));
		}
		return null;
	}

	/**
	 * @generated
	 */
	public boolean provides(IOperation operation) {
		if (operation instanceof GetParserOperation) {
			IAdaptable hint = ((GetParserOperation) operation).getHint();
			if (FlowchartElementTypes.getElement(hint) == null) {
				return false;
			}
			return getParser(hint) != null;
		}
		return false;
	}
}
