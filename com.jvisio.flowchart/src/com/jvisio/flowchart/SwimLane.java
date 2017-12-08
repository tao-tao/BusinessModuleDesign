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
 * A representation of the model object '<em><b>Swim Lane</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.jvisio.flowchart.SwimLane#getName <em>Name</em>}</li>
 *   <li>{@link com.jvisio.flowchart.SwimLane#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.jvisio.flowchart.FlowchartPackage#getSwimLane()
 * @model
 * @generated
 */
public interface SwimLane extends EObject {
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
	 * @see com.jvisio.flowchart.FlowchartPackage#getSwimLane_Name()
	 * @model unique="false"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link com.jvisio.flowchart.SwimLane#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.jvisio.flowchart.SwimLaneType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see com.jvisio.flowchart.SwimLaneType
	 * @see #setType(SwimLaneType)
	 * @see com.jvisio.flowchart.FlowchartPackage#getSwimLane_Type()
	 * @model unique="false"
	 * @generated
	 */
	SwimLaneType getType();

	/**
	 * Sets the value of the '{@link com.jvisio.flowchart.SwimLane#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see com.jvisio.flowchart.SwimLaneType
	 * @see #getType()
	 * @generated
	 */
	void setType(SwimLaneType value);

} // SwimLane