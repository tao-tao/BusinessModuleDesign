/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.jvisio.flowchart;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.jvisio.flowchart.FlowchartFactory
 * @model kind="package"
 * @generated
 */
public interface FlowchartPackage extends EPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2007 Marcos Luiz Freire Pereira. ";

	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "flowchart";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "null";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "flowchart";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	FlowchartPackage eINSTANCE = com.jvisio.flowchart.impl.FlowchartPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.jvisio.flowchart.FlowElement <em>Flow Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.jvisio.flowchart.FlowElement
	 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getFlowElement()
	 * @generated
	 */
	int FLOW_ELEMENT = 7;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOW_ELEMENT__NAME = 0;

	/**
	 * The number of structural features of the '<em>Flow Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOW_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.jvisio.flowchart.impl.TerminatorImpl <em>Terminator</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.jvisio.flowchart.impl.TerminatorImpl
	 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getTerminator()
	 * @generated
	 */
	int TERMINATOR = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TERMINATOR__NAME = FLOW_ELEMENT__NAME;

	/**
	 * The number of structural features of the '<em>Terminator</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TERMINATOR_FEATURE_COUNT = FLOW_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.jvisio.flowchart.impl.ActionImpl <em>Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.jvisio.flowchart.impl.ActionImpl
	 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getAction()
	 * @generated
	 */
	int ACTION = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION__NAME = FLOW_ELEMENT__NAME;

	/**
	 * The number of structural features of the '<em>Action</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION_FEATURE_COUNT = FLOW_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.jvisio.flowchart.impl.DecisionImpl <em>Decision</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.jvisio.flowchart.impl.DecisionImpl
	 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getDecision()
	 * @generated
	 */
	int DECISION = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DECISION__NAME = FLOW_ELEMENT__NAME;

	/**
	 * The number of structural features of the '<em>Decision</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DECISION_FEATURE_COUNT = FLOW_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.jvisio.flowchart.impl.TransitionImpl <em>Transition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.jvisio.flowchart.impl.TransitionImpl
	 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getTransition()
	 * @generated
	 */
	int TRANSITION = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSITION__NAME = 0;

	/**
	 * The feature id for the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSITION__SOURCE = 1;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSITION__TARGET = 2;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSITION__TYPE = 3;

	/**
	 * The number of structural features of the '<em>Transition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSITION_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link com.jvisio.flowchart.impl.ConnectorImpl <em>Connector</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.jvisio.flowchart.impl.ConnectorImpl
	 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getConnector()
	 * @generated
	 */
	int CONNECTOR = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTOR__NAME = FLOW_ELEMENT__NAME;

	/**
	 * The number of structural features of the '<em>Connector</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTOR_FEATURE_COUNT = FLOW_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.jvisio.flowchart.impl.SubprocessImpl <em>Subprocess</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.jvisio.flowchart.impl.SubprocessImpl
	 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getSubprocess()
	 * @generated
	 */
	int SUBPROCESS = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBPROCESS__NAME = FLOW_ELEMENT__NAME;

	/**
	 * The number of structural features of the '<em>Subprocess</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBPROCESS_FEATURE_COUNT = FLOW_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.jvisio.flowchart.impl.WaitImpl <em>Wait</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.jvisio.flowchart.impl.WaitImpl
	 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getWait()
	 * @generated
	 */
	int WAIT = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAIT__NAME = FLOW_ELEMENT__NAME;

	/**
	 * The number of structural features of the '<em>Wait</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAIT_FEATURE_COUNT = FLOW_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.jvisio.flowchart.impl.FlowchartImpl <em>Flowchart</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.jvisio.flowchart.impl.FlowchartImpl
	 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getFlowchart()
	 * @generated
	 */
	int FLOWCHART = 8;

	/**
	 * The feature id for the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOWCHART__TITLE = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOWCHART__DESCRIPTION = 1;

	/**
	 * The feature id for the '<em><b>Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOWCHART__ELEMENTS = 2;

	/**
	 * The feature id for the '<em><b>Transitions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOWCHART__TRANSITIONS = 3;

	/**
	 * The feature id for the '<em><b>Lanes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOWCHART__LANES = 4;

	/**
	 * The number of structural features of the '<em>Flowchart</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOWCHART_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link com.jvisio.flowchart.impl.InputOutputImpl <em>Input Output</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.jvisio.flowchart.impl.InputOutputImpl
	 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getInputOutput()
	 * @generated
	 */
	int INPUT_OUTPUT = 9;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_OUTPUT__NAME = ACTION__NAME;

	/**
	 * The number of structural features of the '<em>Input Output</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_OUTPUT_FEATURE_COUNT = ACTION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.jvisio.flowchart.impl.ManualOperationImpl <em>Manual Operation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.jvisio.flowchart.impl.ManualOperationImpl
	 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getManualOperation()
	 * @generated
	 */
	int MANUAL_OPERATION = 10;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANUAL_OPERATION__NAME = ACTION__NAME;

	/**
	 * The number of structural features of the '<em>Manual Operation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANUAL_OPERATION_FEATURE_COUNT = ACTION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.jvisio.flowchart.impl.DocumentImpl <em>Document</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.jvisio.flowchart.impl.DocumentImpl
	 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getDocument()
	 * @generated
	 */
	int DOCUMENT = 11;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT__NAME = ACTION__NAME;

	/**
	 * The number of structural features of the '<em>Document</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_FEATURE_COUNT = ACTION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.jvisio.flowchart.impl.DiskImpl <em>Disk</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.jvisio.flowchart.impl.DiskImpl
	 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getDisk()
	 * @generated
	 */
	int DISK = 12;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISK__NAME = ACTION__NAME;

	/**
	 * The number of structural features of the '<em>Disk</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISK_FEATURE_COUNT = ACTION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.jvisio.flowchart.impl.DisplayImpl <em>Display</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.jvisio.flowchart.impl.DisplayImpl
	 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getDisplay()
	 * @generated
	 */
	int DISPLAY = 13;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISPLAY__NAME = ACTION__NAME;

	/**
	 * The number of structural features of the '<em>Display</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISPLAY_FEATURE_COUNT = ACTION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.jvisio.flowchart.impl.SwimLaneImpl <em>Swim Lane</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.jvisio.flowchart.impl.SwimLaneImpl
	 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getSwimLane()
	 * @generated
	 */
	int SWIM_LANE = 14;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWIM_LANE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWIM_LANE__TYPE = 1;

	/**
	 * The number of structural features of the '<em>Swim Lane</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWIM_LANE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.jvisio.flowchart.impl.TapeImpl <em>Tape</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.jvisio.flowchart.impl.TapeImpl
	 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getTape()
	 * @generated
	 */
	int TAPE = 15;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TAPE__NAME = ACTION__NAME;

	/**
	 * The number of structural features of the '<em>Tape</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TAPE_FEATURE_COUNT = ACTION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.jvisio.flowchart.TransitionType <em>Transition Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.jvisio.flowchart.TransitionType
	 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getTransitionType()
	 * @generated
	 */
	int TRANSITION_TYPE = 16;

	/**
	 * The meta object id for the '{@link com.jvisio.flowchart.SwimLaneType <em>Swim Lane Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.jvisio.flowchart.SwimLaneType
	 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getSwimLaneType()
	 * @generated
	 */
	int SWIM_LANE_TYPE = 17;


	/**
	 * Returns the meta object for class '{@link com.jvisio.flowchart.Terminator <em>Terminator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Terminator</em>'.
	 * @see com.jvisio.flowchart.Terminator
	 * @generated
	 */
	EClass getTerminator();

	/**
	 * Returns the meta object for class '{@link com.jvisio.flowchart.Action <em>Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Action</em>'.
	 * @see com.jvisio.flowchart.Action
	 * @generated
	 */
	EClass getAction();

	/**
	 * Returns the meta object for class '{@link com.jvisio.flowchart.Decision <em>Decision</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Decision</em>'.
	 * @see com.jvisio.flowchart.Decision
	 * @generated
	 */
	EClass getDecision();

	/**
	 * Returns the meta object for class '{@link com.jvisio.flowchart.Transition <em>Transition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Transition</em>'.
	 * @see com.jvisio.flowchart.Transition
	 * @generated
	 */
	EClass getTransition();

	/**
	 * Returns the meta object for the attribute '{@link com.jvisio.flowchart.Transition#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.jvisio.flowchart.Transition#getName()
	 * @see #getTransition()
	 * @generated
	 */
	EAttribute getTransition_Name();

	/**
	 * Returns the meta object for the reference '{@link com.jvisio.flowchart.Transition#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source</em>'.
	 * @see com.jvisio.flowchart.Transition#getSource()
	 * @see #getTransition()
	 * @generated
	 */
	EReference getTransition_Source();

	/**
	 * Returns the meta object for the reference '{@link com.jvisio.flowchart.Transition#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see com.jvisio.flowchart.Transition#getTarget()
	 * @see #getTransition()
	 * @generated
	 */
	EReference getTransition_Target();

	/**
	 * Returns the meta object for the attribute '{@link com.jvisio.flowchart.Transition#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see com.jvisio.flowchart.Transition#getType()
	 * @see #getTransition()
	 * @generated
	 */
	EAttribute getTransition_Type();

	/**
	 * Returns the meta object for class '{@link com.jvisio.flowchart.Connector <em>Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Connector</em>'.
	 * @see com.jvisio.flowchart.Connector
	 * @generated
	 */
	EClass getConnector();

	/**
	 * Returns the meta object for class '{@link com.jvisio.flowchart.Subprocess <em>Subprocess</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Subprocess</em>'.
	 * @see com.jvisio.flowchart.Subprocess
	 * @generated
	 */
	EClass getSubprocess();

	/**
	 * Returns the meta object for class '{@link com.jvisio.flowchart.Wait <em>Wait</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Wait</em>'.
	 * @see com.jvisio.flowchart.Wait
	 * @generated
	 */
	EClass getWait();

	/**
	 * Returns the meta object for class '{@link com.jvisio.flowchart.FlowElement <em>Flow Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Flow Element</em>'.
	 * @see com.jvisio.flowchart.FlowElement
	 * @generated
	 */
	EClass getFlowElement();

	/**
	 * Returns the meta object for the attribute '{@link com.jvisio.flowchart.FlowElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.jvisio.flowchart.FlowElement#getName()
	 * @see #getFlowElement()
	 * @generated
	 */
	EAttribute getFlowElement_Name();

	/**
	 * Returns the meta object for class '{@link com.jvisio.flowchart.Flowchart <em>Flowchart</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Flowchart</em>'.
	 * @see com.jvisio.flowchart.Flowchart
	 * @generated
	 */
	EClass getFlowchart();

	/**
	 * Returns the meta object for the attribute '{@link com.jvisio.flowchart.Flowchart#getTitle <em>Title</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Title</em>'.
	 * @see com.jvisio.flowchart.Flowchart#getTitle()
	 * @see #getFlowchart()
	 * @generated
	 */
	EAttribute getFlowchart_Title();

	/**
	 * Returns the meta object for the attribute '{@link com.jvisio.flowchart.Flowchart#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see com.jvisio.flowchart.Flowchart#getDescription()
	 * @see #getFlowchart()
	 * @generated
	 */
	EAttribute getFlowchart_Description();

	/**
	 * Returns the meta object for the containment reference list '{@link com.jvisio.flowchart.Flowchart#getElements <em>Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Elements</em>'.
	 * @see com.jvisio.flowchart.Flowchart#getElements()
	 * @see #getFlowchart()
	 * @generated
	 */
	EReference getFlowchart_Elements();

	/**
	 * Returns the meta object for the containment reference list '{@link com.jvisio.flowchart.Flowchart#getTransitions <em>Transitions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Transitions</em>'.
	 * @see com.jvisio.flowchart.Flowchart#getTransitions()
	 * @see #getFlowchart()
	 * @generated
	 */
	EReference getFlowchart_Transitions();

	/**
	 * Returns the meta object for the containment reference list '{@link com.jvisio.flowchart.Flowchart#getLanes <em>Lanes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Lanes</em>'.
	 * @see com.jvisio.flowchart.Flowchart#getLanes()
	 * @see #getFlowchart()
	 * @generated
	 */
	EReference getFlowchart_Lanes();

	/**
	 * Returns the meta object for class '{@link com.jvisio.flowchart.InputOutput <em>Input Output</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Input Output</em>'.
	 * @see com.jvisio.flowchart.InputOutput
	 * @generated
	 */
	EClass getInputOutput();

	/**
	 * Returns the meta object for class '{@link com.jvisio.flowchart.ManualOperation <em>Manual Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Manual Operation</em>'.
	 * @see com.jvisio.flowchart.ManualOperation
	 * @generated
	 */
	EClass getManualOperation();

	/**
	 * Returns the meta object for class '{@link com.jvisio.flowchart.Document <em>Document</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document</em>'.
	 * @see com.jvisio.flowchart.Document
	 * @generated
	 */
	EClass getDocument();

	/**
	 * Returns the meta object for class '{@link com.jvisio.flowchart.Disk <em>Disk</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Disk</em>'.
	 * @see com.jvisio.flowchart.Disk
	 * @generated
	 */
	EClass getDisk();

	/**
	 * Returns the meta object for class '{@link com.jvisio.flowchart.Display <em>Display</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Display</em>'.
	 * @see com.jvisio.flowchart.Display
	 * @generated
	 */
	EClass getDisplay();

	/**
	 * Returns the meta object for class '{@link com.jvisio.flowchart.SwimLane <em>Swim Lane</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Swim Lane</em>'.
	 * @see com.jvisio.flowchart.SwimLane
	 * @generated
	 */
	EClass getSwimLane();

	/**
	 * Returns the meta object for the attribute '{@link com.jvisio.flowchart.SwimLane#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.jvisio.flowchart.SwimLane#getName()
	 * @see #getSwimLane()
	 * @generated
	 */
	EAttribute getSwimLane_Name();

	/**
	 * Returns the meta object for the attribute '{@link com.jvisio.flowchart.SwimLane#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see com.jvisio.flowchart.SwimLane#getType()
	 * @see #getSwimLane()
	 * @generated
	 */
	EAttribute getSwimLane_Type();

	/**
	 * Returns the meta object for class '{@link com.jvisio.flowchart.Tape <em>Tape</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tape</em>'.
	 * @see com.jvisio.flowchart.Tape
	 * @generated
	 */
	EClass getTape();

	/**
	 * Returns the meta object for enum '{@link com.jvisio.flowchart.TransitionType <em>Transition Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Transition Type</em>'.
	 * @see com.jvisio.flowchart.TransitionType
	 * @generated
	 */
	EEnum getTransitionType();

	/**
	 * Returns the meta object for enum '{@link com.jvisio.flowchart.SwimLaneType <em>Swim Lane Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Swim Lane Type</em>'.
	 * @see com.jvisio.flowchart.SwimLaneType
	 * @generated
	 */
	EEnum getSwimLaneType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	FlowchartFactory getFlowchartFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.jvisio.flowchart.impl.TerminatorImpl <em>Terminator</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.jvisio.flowchart.impl.TerminatorImpl
		 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getTerminator()
		 * @generated
		 */
		EClass TERMINATOR = eINSTANCE.getTerminator();

		/**
		 * The meta object literal for the '{@link com.jvisio.flowchart.impl.ActionImpl <em>Action</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.jvisio.flowchart.impl.ActionImpl
		 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getAction()
		 * @generated
		 */
		EClass ACTION = eINSTANCE.getAction();

		/**
		 * The meta object literal for the '{@link com.jvisio.flowchart.impl.DecisionImpl <em>Decision</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.jvisio.flowchart.impl.DecisionImpl
		 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getDecision()
		 * @generated
		 */
		EClass DECISION = eINSTANCE.getDecision();

		/**
		 * The meta object literal for the '{@link com.jvisio.flowchart.impl.TransitionImpl <em>Transition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.jvisio.flowchart.impl.TransitionImpl
		 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getTransition()
		 * @generated
		 */
		EClass TRANSITION = eINSTANCE.getTransition();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSITION__NAME = eINSTANCE.getTransition_Name();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRANSITION__SOURCE = eINSTANCE.getTransition_Source();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRANSITION__TARGET = eINSTANCE.getTransition_Target();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSITION__TYPE = eINSTANCE.getTransition_Type();

		/**
		 * The meta object literal for the '{@link com.jvisio.flowchart.impl.ConnectorImpl <em>Connector</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.jvisio.flowchart.impl.ConnectorImpl
		 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getConnector()
		 * @generated
		 */
		EClass CONNECTOR = eINSTANCE.getConnector();

		/**
		 * The meta object literal for the '{@link com.jvisio.flowchart.impl.SubprocessImpl <em>Subprocess</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.jvisio.flowchart.impl.SubprocessImpl
		 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getSubprocess()
		 * @generated
		 */
		EClass SUBPROCESS = eINSTANCE.getSubprocess();

		/**
		 * The meta object literal for the '{@link com.jvisio.flowchart.impl.WaitImpl <em>Wait</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.jvisio.flowchart.impl.WaitImpl
		 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getWait()
		 * @generated
		 */
		EClass WAIT = eINSTANCE.getWait();

		/**
		 * The meta object literal for the '{@link com.jvisio.flowchart.FlowElement <em>Flow Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.jvisio.flowchart.FlowElement
		 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getFlowElement()
		 * @generated
		 */
		EClass FLOW_ELEMENT = eINSTANCE.getFlowElement();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FLOW_ELEMENT__NAME = eINSTANCE.getFlowElement_Name();

		/**
		 * The meta object literal for the '{@link com.jvisio.flowchart.impl.FlowchartImpl <em>Flowchart</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.jvisio.flowchart.impl.FlowchartImpl
		 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getFlowchart()
		 * @generated
		 */
		EClass FLOWCHART = eINSTANCE.getFlowchart();

		/**
		 * The meta object literal for the '<em><b>Title</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FLOWCHART__TITLE = eINSTANCE.getFlowchart_Title();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FLOWCHART__DESCRIPTION = eINSTANCE.getFlowchart_Description();

		/**
		 * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLOWCHART__ELEMENTS = eINSTANCE.getFlowchart_Elements();

		/**
		 * The meta object literal for the '<em><b>Transitions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLOWCHART__TRANSITIONS = eINSTANCE.getFlowchart_Transitions();

		/**
		 * The meta object literal for the '<em><b>Lanes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLOWCHART__LANES = eINSTANCE.getFlowchart_Lanes();

		/**
		 * The meta object literal for the '{@link com.jvisio.flowchart.impl.InputOutputImpl <em>Input Output</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.jvisio.flowchart.impl.InputOutputImpl
		 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getInputOutput()
		 * @generated
		 */
		EClass INPUT_OUTPUT = eINSTANCE.getInputOutput();

		/**
		 * The meta object literal for the '{@link com.jvisio.flowchart.impl.ManualOperationImpl <em>Manual Operation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.jvisio.flowchart.impl.ManualOperationImpl
		 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getManualOperation()
		 * @generated
		 */
		EClass MANUAL_OPERATION = eINSTANCE.getManualOperation();

		/**
		 * The meta object literal for the '{@link com.jvisio.flowchart.impl.DocumentImpl <em>Document</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.jvisio.flowchart.impl.DocumentImpl
		 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getDocument()
		 * @generated
		 */
		EClass DOCUMENT = eINSTANCE.getDocument();

		/**
		 * The meta object literal for the '{@link com.jvisio.flowchart.impl.DiskImpl <em>Disk</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.jvisio.flowchart.impl.DiskImpl
		 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getDisk()
		 * @generated
		 */
		EClass DISK = eINSTANCE.getDisk();

		/**
		 * The meta object literal for the '{@link com.jvisio.flowchart.impl.DisplayImpl <em>Display</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.jvisio.flowchart.impl.DisplayImpl
		 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getDisplay()
		 * @generated
		 */
		EClass DISPLAY = eINSTANCE.getDisplay();

		/**
		 * The meta object literal for the '{@link com.jvisio.flowchart.impl.SwimLaneImpl <em>Swim Lane</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.jvisio.flowchart.impl.SwimLaneImpl
		 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getSwimLane()
		 * @generated
		 */
		EClass SWIM_LANE = eINSTANCE.getSwimLane();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWIM_LANE__NAME = eINSTANCE.getSwimLane_Name();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWIM_LANE__TYPE = eINSTANCE.getSwimLane_Type();

		/**
		 * The meta object literal for the '{@link com.jvisio.flowchart.impl.TapeImpl <em>Tape</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.jvisio.flowchart.impl.TapeImpl
		 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getTape()
		 * @generated
		 */
		EClass TAPE = eINSTANCE.getTape();

		/**
		 * The meta object literal for the '{@link com.jvisio.flowchart.TransitionType <em>Transition Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.jvisio.flowchart.TransitionType
		 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getTransitionType()
		 * @generated
		 */
		EEnum TRANSITION_TYPE = eINSTANCE.getTransitionType();

		/**
		 * The meta object literal for the '{@link com.jvisio.flowchart.SwimLaneType <em>Swim Lane Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.jvisio.flowchart.SwimLaneType
		 * @see com.jvisio.flowchart.impl.FlowchartPackageImpl#getSwimLaneType()
		 * @generated
		 */
		EEnum SWIM_LANE_TYPE = eINSTANCE.getSwimLaneType();

	}

} //FlowchartPackage
