/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.jvisio.flowchart;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.jvisio.flowchart.Transition#getName <em>Name</em>}</li>
 *   <li>{@link com.jvisio.flowchart.Transition#getSource <em>Source</em>}</li>
 *   <li>{@link com.jvisio.flowchart.Transition#getTarget <em>Target</em>}</li>
 *   <li>{@link com.jvisio.flowchart.Transition#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.jvisio.flowchart.FlowchartPackage#getTransition()
 * @model
 * @generated
 */
public interface Transition extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2007 Marcos Luiz Freire Pereira. ";

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see com.jvisio.flowchart.FlowchartPackage#getTransition_Name()
	 * @model unique="false"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link com.jvisio.flowchart.Transition#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' reference.
	 * @see #setSource(FlowElement)
	 * @see com.jvisio.flowchart.FlowchartPackage#getTransition_Source()
	 * @model
	 * @generated
	 */
	FlowElement getSource();

	/**
	 * Sets the value of the '{@link com.jvisio.flowchart.Transition#getSource <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' reference.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(FlowElement value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(FlowElement)
	 * @see com.jvisio.flowchart.FlowchartPackage#getTransition_Target()
	 * @model
	 * @generated
	 */
	FlowElement getTarget();

	/**
	 * Sets the value of the '{@link com.jvisio.flowchart.Transition#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(FlowElement value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.jvisio.flowchart.TransitionType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see com.jvisio.flowchart.TransitionType
	 * @see #setType(TransitionType)
	 * @see com.jvisio.flowchart.FlowchartPackage#getTransition_Type()
	 * @model unique="false"
	 * @generated
	 */
	TransitionType getType();

	/**
	 * Sets the value of the '{@link com.jvisio.flowchart.Transition#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see com.jvisio.flowchart.TransitionType
	 * @see #getType()
	 * @generated
	 */
	void setType(TransitionType value);

} // Transition