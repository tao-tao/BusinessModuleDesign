/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.jvisio.flowchart.impl;

import com.jvisio.flowchart.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class FlowchartFactoryImpl extends EFactoryImpl implements FlowchartFactory {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2007 Marcos Luiz Freire Pereira. ";

	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static FlowchartFactory init() {
		try {
			FlowchartFactory theFlowchartFactory = (FlowchartFactory)EPackage.Registry.INSTANCE.getEFactory("null"); 
			if (theFlowchartFactory != null) {
				return theFlowchartFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new FlowchartFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FlowchartFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case FlowchartPackage.TERMINATOR: return createTerminator();
			case FlowchartPackage.ACTION: return createAction();
			case FlowchartPackage.DECISION: return createDecision();
			case FlowchartPackage.TRANSITION: return createTransition();
			case FlowchartPackage.CONNECTOR: return createConnector();
			case FlowchartPackage.SUBPROCESS: return createSubprocess();
			case FlowchartPackage.WAIT: return createWait();
			case FlowchartPackage.FLOWCHART: return createFlowchart();
			case FlowchartPackage.INPUT_OUTPUT: return createInputOutput();
			case FlowchartPackage.MANUAL_OPERATION: return createManualOperation();
			case FlowchartPackage.DOCUMENT: return createDocument();
			case FlowchartPackage.DISK: return createDisk();
			case FlowchartPackage.DISPLAY: return createDisplay();
			case FlowchartPackage.SWIM_LANE: return createSwimLane();
			case FlowchartPackage.TAPE: return createTape();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case FlowchartPackage.TRANSITION_TYPE:
				return createTransitionTypeFromString(eDataType, initialValue);
			case FlowchartPackage.SWIM_LANE_TYPE:
				return createSwimLaneTypeFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case FlowchartPackage.TRANSITION_TYPE:
				return convertTransitionTypeToString(eDataType, instanceValue);
			case FlowchartPackage.SWIM_LANE_TYPE:
				return convertSwimLaneTypeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Terminator createTerminator() {
		TerminatorImpl terminator = new TerminatorImpl();
		return terminator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Action createAction() {
		ActionImpl action = new ActionImpl();
		return action;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Decision createDecision() {
		DecisionImpl decision = new DecisionImpl();
		return decision;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Transition createTransition() {
		TransitionImpl transition = new TransitionImpl();
		return transition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Connector createConnector() {
		ConnectorImpl connector = new ConnectorImpl();
		return connector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Subprocess createSubprocess() {
		SubprocessImpl subprocess = new SubprocessImpl();
		return subprocess;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Wait createWait() {
		WaitImpl wait = new WaitImpl();
		return wait;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Flowchart createFlowchart() {
		FlowchartImpl flowchart = new FlowchartImpl();
		return flowchart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InputOutput createInputOutput() {
		InputOutputImpl inputOutput = new InputOutputImpl();
		return inputOutput;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ManualOperation createManualOperation() {
		ManualOperationImpl manualOperation = new ManualOperationImpl();
		return manualOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Document createDocument() {
		DocumentImpl document = new DocumentImpl();
		return document;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Disk createDisk() {
		DiskImpl disk = new DiskImpl();
		return disk;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Display createDisplay() {
		DisplayImpl display = new DisplayImpl();
		return display;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SwimLane createSwimLane() {
		SwimLaneImpl swimLane = new SwimLaneImpl();
		return swimLane;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Tape createTape() {
		TapeImpl tape = new TapeImpl();
		return tape;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransitionType createTransitionTypeFromString(EDataType eDataType, String initialValue) {
		TransitionType result = TransitionType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTransitionTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SwimLaneType createSwimLaneTypeFromString(EDataType eDataType, String initialValue) {
		SwimLaneType result = SwimLaneType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSwimLaneTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FlowchartPackage getFlowchartPackage() {
		return (FlowchartPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static FlowchartPackage getPackage() {
		return FlowchartPackage.eINSTANCE;
	}

} //FlowchartFactoryImpl
