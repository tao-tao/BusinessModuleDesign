/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.jvisio.flowchart.provider;


import com.jvisio.flowchart.Flowchart;
import com.jvisio.flowchart.FlowchartFactory;
import com.jvisio.flowchart.FlowchartPackage;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.jvisio.flowchart.Flowchart} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class FlowchartItemProvider
	extends ItemProviderAdapter
	implements	
		IEditingDomainItemProvider,	
		IStructuredItemContentProvider,	
		ITreeItemContentProvider,	
		IItemLabelProvider,	
		IItemPropertySource {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2007 Marcos Luiz Freire Pereira. ";

	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FlowchartItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addTitlePropertyDescriptor(object);
			addDescriptionPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Title feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTitlePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Flowchart_title_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Flowchart_title_feature", "_UI_Flowchart_type"),
				 FlowchartPackage.Literals.FLOWCHART__TITLE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Description feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDescriptionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Flowchart_description_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Flowchart_description_feature", "_UI_Flowchart_type"),
				 FlowchartPackage.Literals.FLOWCHART__DESCRIPTION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Collection getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(FlowchartPackage.Literals.FLOWCHART__ELEMENTS);
			childrenFeatures.add(FlowchartPackage.Literals.FLOWCHART__TRANSITIONS);
			childrenFeatures.add(FlowchartPackage.Literals.FLOWCHART__LANES);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns Flowchart.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Flowchart"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getText(Object object) {
		String label = ((Flowchart)object).getTitle();
		return label == null || label.length() == 0 ?
			getString("_UI_Flowchart_type") :
			getString("_UI_Flowchart_type") + " " + label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(Flowchart.class)) {
			case FlowchartPackage.FLOWCHART__TITLE:
			case FlowchartPackage.FLOWCHART__DESCRIPTION:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case FlowchartPackage.FLOWCHART__ELEMENTS:
			case FlowchartPackage.FLOWCHART__TRANSITIONS:
			case FlowchartPackage.FLOWCHART__LANES:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds to the collection of {@link org.eclipse.emf.edit.command.CommandParameter}s
	 * describing all of the children that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void collectNewChildDescriptors(Collection newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(FlowchartPackage.Literals.FLOWCHART__ELEMENTS,
				 FlowchartFactory.eINSTANCE.createTerminator()));

		newChildDescriptors.add
			(createChildParameter
				(FlowchartPackage.Literals.FLOWCHART__ELEMENTS,
				 FlowchartFactory.eINSTANCE.createAction()));

		newChildDescriptors.add
			(createChildParameter
				(FlowchartPackage.Literals.FLOWCHART__ELEMENTS,
				 FlowchartFactory.eINSTANCE.createDecision()));

		newChildDescriptors.add
			(createChildParameter
				(FlowchartPackage.Literals.FLOWCHART__ELEMENTS,
				 FlowchartFactory.eINSTANCE.createConnector()));

		newChildDescriptors.add
			(createChildParameter
				(FlowchartPackage.Literals.FLOWCHART__ELEMENTS,
				 FlowchartFactory.eINSTANCE.createSubprocess()));

		newChildDescriptors.add
			(createChildParameter
				(FlowchartPackage.Literals.FLOWCHART__ELEMENTS,
				 FlowchartFactory.eINSTANCE.createWait()));

		newChildDescriptors.add
			(createChildParameter
				(FlowchartPackage.Literals.FLOWCHART__ELEMENTS,
				 FlowchartFactory.eINSTANCE.createInputOutput()));

		newChildDescriptors.add
			(createChildParameter
				(FlowchartPackage.Literals.FLOWCHART__ELEMENTS,
				 FlowchartFactory.eINSTANCE.createManualOperation()));

		newChildDescriptors.add
			(createChildParameter
				(FlowchartPackage.Literals.FLOWCHART__ELEMENTS,
				 FlowchartFactory.eINSTANCE.createDocument()));

		newChildDescriptors.add
			(createChildParameter
				(FlowchartPackage.Literals.FLOWCHART__ELEMENTS,
				 FlowchartFactory.eINSTANCE.createDisk()));

		newChildDescriptors.add
			(createChildParameter
				(FlowchartPackage.Literals.FLOWCHART__ELEMENTS,
				 FlowchartFactory.eINSTANCE.createDisplay()));

		newChildDescriptors.add
			(createChildParameter
				(FlowchartPackage.Literals.FLOWCHART__ELEMENTS,
				 FlowchartFactory.eINSTANCE.createTape()));

		newChildDescriptors.add
			(createChildParameter
				(FlowchartPackage.Literals.FLOWCHART__TRANSITIONS,
				 FlowchartFactory.eINSTANCE.createTransition()));

		newChildDescriptors.add
			(createChildParameter
				(FlowchartPackage.Literals.FLOWCHART__LANES,
				 FlowchartFactory.eINSTANCE.createSwimLane()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceLocator getResourceLocator() {
		return FlowchartEditPlugin.INSTANCE;
	}

}
