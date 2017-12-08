/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.jvisio.flowchart;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Swim Lane Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.jvisio.flowchart.FlowchartPackage#getSwimLaneType()
 * @model
 * @generated
 */
public final class SwimLaneType extends AbstractEnumerator {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2007 Marcos Luiz Freire Pereira. ";

	/**
	 * The '<em><b>HORIZONTAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>HORIZONTAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #HORIZONTAL_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int HORIZONTAL = 0;

	/**
	 * The '<em><b>VERTICAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>VERTICAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #VERTICAL_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int VERTICAL = 0;

	/**
	 * The '<em><b>HORIZONTAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #HORIZONTAL
	 * @generated
	 * @ordered
	 */
	public static final SwimLaneType HORIZONTAL_LITERAL = new SwimLaneType(HORIZONTAL, "HORIZONTAL", "HORIZONTAL");

	/**
	 * The '<em><b>VERTICAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #VERTICAL
	 * @generated
	 * @ordered
	 */
	public static final SwimLaneType VERTICAL_LITERAL = new SwimLaneType(VERTICAL, "VERTICAL", "VERTICAL");

	/**
	 * An array of all the '<em><b>Swim Lane Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final SwimLaneType[] VALUES_ARRAY =
		new SwimLaneType[] {
			HORIZONTAL_LITERAL,
			VERTICAL_LITERAL,
		};

	/**
	 * A public read-only list of all the '<em><b>Swim Lane Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Swim Lane Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SwimLaneType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SwimLaneType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Swim Lane Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SwimLaneType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SwimLaneType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Swim Lane Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SwimLaneType get(int value) {
		switch (value) {
			case HORIZONTAL: return HORIZONTAL_LITERAL;
		}
		return null;	
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private SwimLaneType(int value, String name, String literal) {
		super(value, name, literal);
	}

} //SwimLaneType
