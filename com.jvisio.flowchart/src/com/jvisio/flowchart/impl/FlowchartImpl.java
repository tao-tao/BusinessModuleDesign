/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.jvisio.flowchart.impl;

import com.jvisio.flowchart.FlowElement;
import com.jvisio.flowchart.Flowchart;
import com.jvisio.flowchart.FlowchartPackage;
import com.jvisio.flowchart.SwimLane;
import com.jvisio.flowchart.Transition;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Flowchart</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jvisio.flowchart.impl.FlowchartImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link com.jvisio.flowchart.impl.FlowchartImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link com.jvisio.flowchart.impl.FlowchartImpl#getElements <em>Elements</em>}</li>
 *   <li>{@link com.jvisio.flowchart.impl.FlowchartImpl#getTransitions <em>Transitions</em>}</li>
 *   <li>{@link com.jvisio.flowchart.impl.FlowchartImpl#getLanes <em>Lanes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FlowchartImpl extends EObjectImpl implements Flowchart {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2007 Marcos Luiz Freire Pereira. ";

	/**
	 * The default value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
	protected static final String TITLE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
	protected String title = TITLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getElements() <em>Elements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElements()
	 * @generated
	 * @ordered
	 */
	protected EList elements = null;

	/**
	 * The cached value of the '{@link #getTransitions() <em>Transitions</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransitions()
	 * @generated
	 * @ordered
	 */
	protected EList transitions = null;

	/**
	 * The cached value of the '{@link #getLanes() <em>Lanes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLanes()
	 * @generated
	 * @ordered
	 */
	protected EList lanes = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FlowchartImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return FlowchartPackage.Literals.FLOWCHART;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTitle(String newTitle) {
		String oldTitle = title;
		title = newTitle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FlowchartPackage.FLOWCHART__TITLE, oldTitle, title));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FlowchartPackage.FLOWCHART__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getElements() {
		if (elements == null) {
			elements = new EObjectContainmentEList(FlowElement.class, this, FlowchartPackage.FLOWCHART__ELEMENTS);
		}
		return elements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getTransitions() {
		if (transitions == null) {
			transitions = new EObjectContainmentEList(Transition.class, this, FlowchartPackage.FLOWCHART__TRANSITIONS);
		}
		return transitions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getLanes() {
		if (lanes == null) {
			lanes = new EObjectContainmentEList(SwimLane.class, this, FlowchartPackage.FLOWCHART__LANES);
		}
		return lanes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FlowchartPackage.FLOWCHART__ELEMENTS:
				return ((InternalEList)getElements()).basicRemove(otherEnd, msgs);
			case FlowchartPackage.FLOWCHART__TRANSITIONS:
				return ((InternalEList)getTransitions()).basicRemove(otherEnd, msgs);
			case FlowchartPackage.FLOWCHART__LANES:
				return ((InternalEList)getLanes()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FlowchartPackage.FLOWCHART__TITLE:
				return getTitle();
			case FlowchartPackage.FLOWCHART__DESCRIPTION:
				return getDescription();
			case FlowchartPackage.FLOWCHART__ELEMENTS:
				return getElements();
			case FlowchartPackage.FLOWCHART__TRANSITIONS:
				return getTransitions();
			case FlowchartPackage.FLOWCHART__LANES:
				return getLanes();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case FlowchartPackage.FLOWCHART__TITLE:
				setTitle((String)newValue);
				return;
			case FlowchartPackage.FLOWCHART__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case FlowchartPackage.FLOWCHART__ELEMENTS:
				getElements().clear();
				getElements().addAll((Collection)newValue);
				return;
			case FlowchartPackage.FLOWCHART__TRANSITIONS:
				getTransitions().clear();
				getTransitions().addAll((Collection)newValue);
				return;
			case FlowchartPackage.FLOWCHART__LANES:
				getLanes().clear();
				getLanes().addAll((Collection)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case FlowchartPackage.FLOWCHART__TITLE:
				setTitle(TITLE_EDEFAULT);
				return;
			case FlowchartPackage.FLOWCHART__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case FlowchartPackage.FLOWCHART__ELEMENTS:
				getElements().clear();
				return;
			case FlowchartPackage.FLOWCHART__TRANSITIONS:
				getTransitions().clear();
				return;
			case FlowchartPackage.FLOWCHART__LANES:
				getLanes().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case FlowchartPackage.FLOWCHART__TITLE:
				return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
			case FlowchartPackage.FLOWCHART__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case FlowchartPackage.FLOWCHART__ELEMENTS:
				return elements != null && !elements.isEmpty();
			case FlowchartPackage.FLOWCHART__TRANSITIONS:
				return transitions != null && !transitions.isEmpty();
			case FlowchartPackage.FLOWCHART__LANES:
				return lanes != null && !lanes.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (title: ");
		result.append(title);
		result.append(", description: ");
		result.append(description);
		result.append(')');
		return result.toString();
	}

} //FlowchartImpl