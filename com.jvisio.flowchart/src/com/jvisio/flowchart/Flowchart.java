/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.jvisio.flowchart;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Flowchart</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.jvisio.flowchart.Flowchart#getTitle <em>Title</em>}</li>
 *   <li>{@link com.jvisio.flowchart.Flowchart#getDescription <em>Description</em>}</li>
 *   <li>{@link com.jvisio.flowchart.Flowchart#getElements <em>Elements</em>}</li>
 *   <li>{@link com.jvisio.flowchart.Flowchart#getTransitions <em>Transitions</em>}</li>
 *   <li>{@link com.jvisio.flowchart.Flowchart#getLanes <em>Lanes</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.jvisio.flowchart.FlowchartPackage#getFlowchart()
 * @model
 * @generated
 */
public interface Flowchart extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2007 Marcos Luiz Freire Pereira. ";

	/**
	 * Returns the value of the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Title</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Title</em>' attribute.
	 * @see #setTitle(String)
	 * @see com.jvisio.flowchart.FlowchartPackage#getFlowchart_Title()
	 * @model
	 * @generated
	 */
	String getTitle();

	/**
	 * Sets the value of the '{@link com.jvisio.flowchart.Flowchart#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
	void setTitle(String value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see com.jvisio.flowchart.FlowchartPackage#getFlowchart_Description()
	 * @model unique="false"
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link com.jvisio.flowchart.Flowchart#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Elements</b></em>' containment reference list.
	 * The list contents are of type {@link com.jvisio.flowchart.FlowElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Elements</em>' containment reference list.
	 * @see com.jvisio.flowchart.FlowchartPackage#getFlowchart_Elements()
	 * @model type="com.jvisio.flowchart.FlowElement" containment="true"
	 * @generated
	 */
	EList getElements();

	/**
	 * Returns the value of the '<em><b>Transitions</b></em>' containment reference list.
	 * The list contents are of type {@link com.jvisio.flowchart.Transition}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Transitions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transitions</em>' containment reference list.
	 * @see com.jvisio.flowchart.FlowchartPackage#getFlowchart_Transitions()
	 * @model type="com.jvisio.flowchart.Transition" containment="true"
	 * @generated
	 */
	EList getTransitions();

	/**
	 * Returns the value of the '<em><b>Lanes</b></em>' containment reference list.
	 * The list contents are of type {@link com.jvisio.flowchart.SwimLane}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lanes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lanes</em>' containment reference list.
	 * @see com.jvisio.flowchart.FlowchartPackage#getFlowchart_Lanes()
	 * @model type="com.jvisio.flowchart.SwimLane" containment="true"
	 * @generated
	 */
	EList getLanes();

} // Flowchart