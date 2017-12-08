/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.jvisio.flowchart.util;

import com.jvisio.flowchart.*;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see com.jvisio.flowchart.FlowchartPackage
 * @generated
 */
public class FlowchartSwitch {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2007 Marcos Luiz Freire Pereira. ";

	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static FlowchartPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FlowchartSwitch() {
		if (modelPackage == null) {
			modelPackage = FlowchartPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public Object doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected Object doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		}
		else {
			List eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject) :
					doSwitch((EClass)eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected Object doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case FlowchartPackage.TERMINATOR: {
				Terminator terminator = (Terminator)theEObject;
				Object result = caseTerminator(terminator);
				if (result == null) result = caseFlowElement(terminator);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FlowchartPackage.ACTION: {
				Action action = (Action)theEObject;
				Object result = caseAction(action);
				if (result == null) result = caseFlowElement(action);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FlowchartPackage.DECISION: {
				Decision decision = (Decision)theEObject;
				Object result = caseDecision(decision);
				if (result == null) result = caseFlowElement(decision);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FlowchartPackage.TRANSITION: {
				Transition transition = (Transition)theEObject;
				Object result = caseTransition(transition);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FlowchartPackage.CONNECTOR: {
				Connector connector = (Connector)theEObject;
				Object result = caseConnector(connector);
				if (result == null) result = caseFlowElement(connector);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FlowchartPackage.SUBPROCESS: {
				Subprocess subprocess = (Subprocess)theEObject;
				Object result = caseSubprocess(subprocess);
				if (result == null) result = caseFlowElement(subprocess);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FlowchartPackage.WAIT: {
				Wait wait = (Wait)theEObject;
				Object result = caseWait(wait);
				if (result == null) result = caseFlowElement(wait);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FlowchartPackage.FLOW_ELEMENT: {
				FlowElement flowElement = (FlowElement)theEObject;
				Object result = caseFlowElement(flowElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FlowchartPackage.FLOWCHART: {
				Flowchart flowchart = (Flowchart)theEObject;
				Object result = caseFlowchart(flowchart);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FlowchartPackage.INPUT_OUTPUT: {
				InputOutput inputOutput = (InputOutput)theEObject;
				Object result = caseInputOutput(inputOutput);
				if (result == null) result = caseAction(inputOutput);
				if (result == null) result = caseFlowElement(inputOutput);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FlowchartPackage.MANUAL_OPERATION: {
				ManualOperation manualOperation = (ManualOperation)theEObject;
				Object result = caseManualOperation(manualOperation);
				if (result == null) result = caseAction(manualOperation);
				if (result == null) result = caseFlowElement(manualOperation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FlowchartPackage.DOCUMENT: {
				Document document = (Document)theEObject;
				Object result = caseDocument(document);
				if (result == null) result = caseAction(document);
				if (result == null) result = caseFlowElement(document);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FlowchartPackage.DISK: {
				Disk disk = (Disk)theEObject;
				Object result = caseDisk(disk);
				if (result == null) result = caseAction(disk);
				if (result == null) result = caseFlowElement(disk);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FlowchartPackage.DISPLAY: {
				Display display = (Display)theEObject;
				Object result = caseDisplay(display);
				if (result == null) result = caseAction(display);
				if (result == null) result = caseFlowElement(display);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FlowchartPackage.SWIM_LANE: {
				SwimLane swimLane = (SwimLane)theEObject;
				Object result = caseSwimLane(swimLane);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FlowchartPackage.TAPE: {
				Tape tape = (Tape)theEObject;
				Object result = caseTape(tape);
				if (result == null) result = caseAction(tape);
				if (result == null) result = caseFlowElement(tape);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Terminator</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Terminator</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseTerminator(Terminator object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Action</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Action</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAction(Action object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Decision</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Decision</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseDecision(Decision object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Transition</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Transition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseTransition(Transition object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Connector</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Connector</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseConnector(Connector object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Subprocess</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Subprocess</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseSubprocess(Subprocess object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Wait</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Wait</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseWait(Wait object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Flow Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Flow Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseFlowElement(FlowElement object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Flowchart</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Flowchart</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseFlowchart(Flowchart object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Input Output</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Input Output</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseInputOutput(InputOutput object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Manual Operation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Manual Operation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseManualOperation(ManualOperation object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Document</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Document</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseDocument(Document object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Disk</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Disk</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseDisk(Disk object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Display</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Display</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseDisplay(Display object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Swim Lane</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Swim Lane</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseSwimLane(SwimLane object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Tape</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Tape</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseTape(Tape object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public Object defaultCase(EObject object) {
		return null;
	}

} //FlowchartSwitch
