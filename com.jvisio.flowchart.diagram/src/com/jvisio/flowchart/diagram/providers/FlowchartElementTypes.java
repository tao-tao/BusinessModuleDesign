package com.jvisio.flowchart.diagram.providers;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

import com.jvisio.flowchart.FlowchartPackage;
import com.jvisio.flowchart.diagram.part.FlowchartDiagramEditorPlugin;

/**
 * @generated
 */
public class FlowchartElementTypes {

	/**
	 * @generated
	 */
	private FlowchartElementTypes() {
	}

	/**
	 * @generated
	 */
	private static Map elements;

	/**
	 * @generated
	 */
	private static ImageRegistry imageRegistry;

	/**
	 * @generated
	 */
	private static ImageRegistry getImageRegistry() {
		if (imageRegistry == null) {
			imageRegistry = new ImageRegistry();
		}
		return imageRegistry;
	}

	/**
	 * @generated
	 */
	private static String getImageRegistryKey(ENamedElement element) {
		return element.getName();
	}

	/**
	 * @generated
	 */
	private static ImageDescriptor getProvidedImageDescriptor(
			ENamedElement element) {
		if (element instanceof EStructuralFeature) {
			element = ((EStructuralFeature) element).getEContainingClass();
		}
		if (element instanceof EClass) {
			EClass eClass = (EClass) element;
			if (!eClass.isAbstract()) {
				return FlowchartDiagramEditorPlugin.getInstance()
						.getItemImageDescriptor(
								eClass.getEPackage().getEFactoryInstance()
										.create(eClass));
			}
		}
		// TODO : support structural features
		return null;
	}

	/**
	 * @generated
	 */
	public static ImageDescriptor getImageDescriptor(ENamedElement element) {
		String key = getImageRegistryKey(element);
		ImageDescriptor imageDescriptor = getImageRegistry().getDescriptor(key);
		if (imageDescriptor == null) {
			imageDescriptor = getProvidedImageDescriptor(element);
			if (imageDescriptor == null) {
				imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
			}
			getImageRegistry().put(key, imageDescriptor);
		}
		return imageDescriptor;
	}

	/**
	 * @generated
	 */
	public static Image getImage(ENamedElement element) {
		String key = getImageRegistryKey(element);
		Image image = getImageRegistry().get(key);
		if (image == null) {
			ImageDescriptor imageDescriptor = getProvidedImageDescriptor(element);
			if (imageDescriptor == null) {
				imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
			}
			getImageRegistry().put(key, imageDescriptor);
			image = getImageRegistry().get(key);
		}
		return image;
	}

	/**
	 * @generated
	 */
	public static ImageDescriptor getImageDescriptor(IAdaptable hint) {
		ENamedElement element = getElement(hint);
		if (element == null) {
			return null;
		}
		return getImageDescriptor(element);
	}

	/**
	 * @generated
	 */
	public static Image getImage(IAdaptable hint) {
		ENamedElement element = getElement(hint);
		if (element == null) {
			return null;
		}
		return getImage(element);
	}

	/**
	 * Returns 'type' of the ecore object associated with the hint.
	 * 
	 * @generated
	 */
	public static ENamedElement getElement(IAdaptable hint) {
		Object type = hint.getAdapter(IElementType.class);
		if (elements == null) {
			elements = new IdentityHashMap();
			elements.put(Flowchart_79, FlowchartPackage.eINSTANCE
					.getFlowchart());
			elements.put(Terminator_1001, FlowchartPackage.eINSTANCE
					.getTerminator());
			elements.put(Action_1002, FlowchartPackage.eINSTANCE.getAction());
			elements.put(Decision_1003, FlowchartPackage.eINSTANCE
					.getDecision());
			elements.put(Subprocess_1004, FlowchartPackage.eINSTANCE
					.getSubprocess());
			elements.put(Connector_1005, FlowchartPackage.eINSTANCE
					.getConnector());
			elements.put(Wait_1006, FlowchartPackage.eINSTANCE.getWait());
			elements.put(Transition_3001, FlowchartPackage.eINSTANCE
					.getTransition());
		}
		return (ENamedElement) elements.get(type);
	}

	/**
	 * @generated
	 */
	public static final IElementType Flowchart_79 = getElementType("com.jvisio.flowchart.diagram.Flowchart_79"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Terminator_1001 = getElementType("com.jvisio.flowchart.diagram.Terminator_1001"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Action_1002 = getElementType("com.jvisio.flowchart.diagram.Action_1002"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Decision_1003 = getElementType("com.jvisio.flowchart.diagram.Decision_1003"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Subprocess_1004 = getElementType("com.jvisio.flowchart.diagram.Subprocess_1004"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Connector_1005 = getElementType("com.jvisio.flowchart.diagram.Connector_1005"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Wait_1006 = getElementType("com.jvisio.flowchart.diagram.Wait_1006"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Transition_3001 = getElementType("com.jvisio.flowchart.diagram.Transition_3001"); //$NON-NLS-1$
	
	
	public static final IElementType SwimLane_3002 = getElementType("com.jvisio.flowchart.diagram.SwimLane_3002");

	/**
	 * @generated
	 */
	private static IElementType getElementType(String id) {
		return ElementTypeRegistry.getInstance().getType(id);
	}

	/**
	 * @generated
	 */
	private static Set KNOWN_ELEMENT_TYPES;

	/**
	 * @generated
	 */
	public static boolean isKnownElementType(IElementType elementType) {
		if (KNOWN_ELEMENT_TYPES == null) {
			KNOWN_ELEMENT_TYPES = new HashSet();
			KNOWN_ELEMENT_TYPES.add(Flowchart_79);
			KNOWN_ELEMENT_TYPES.add(Terminator_1001);
			KNOWN_ELEMENT_TYPES.add(Action_1002);
			KNOWN_ELEMENT_TYPES.add(Decision_1003);
			KNOWN_ELEMENT_TYPES.add(Subprocess_1004);
			KNOWN_ELEMENT_TYPES.add(Connector_1005);
			KNOWN_ELEMENT_TYPES.add(Wait_1006);
			KNOWN_ELEMENT_TYPES.add(Transition_3001);
			KNOWN_ELEMENT_TYPES.add(SwimLane_3002);
		}
		return KNOWN_ELEMENT_TYPES.contains(elementType);
	}
}
