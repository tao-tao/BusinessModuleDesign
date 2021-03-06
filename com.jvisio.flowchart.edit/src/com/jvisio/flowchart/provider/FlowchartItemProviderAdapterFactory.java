/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.jvisio.flowchart.provider;

import com.jvisio.flowchart.util.FlowchartAdapterFactory;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class FlowchartItemProviderAdapterFactory extends FlowchartAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2007 Marcos Luiz Freire Pereira. ";

	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Collection supportedTypes = new ArrayList();

	/**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FlowchartItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);		
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.jvisio.flowchart.Terminator} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TerminatorItemProvider terminatorItemProvider;

	/**
	 * This creates an adapter for a {@link com.jvisio.flowchart.Terminator}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createTerminatorAdapter() {
		if (terminatorItemProvider == null) {
			terminatorItemProvider = new TerminatorItemProvider(this);
		}

		return terminatorItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.jvisio.flowchart.Action} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ActionItemProvider actionItemProvider;

	/**
	 * This creates an adapter for a {@link com.jvisio.flowchart.Action}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createActionAdapter() {
		if (actionItemProvider == null) {
			actionItemProvider = new ActionItemProvider(this);
		}

		return actionItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.jvisio.flowchart.Decision} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DecisionItemProvider decisionItemProvider;

	/**
	 * This creates an adapter for a {@link com.jvisio.flowchart.Decision}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createDecisionAdapter() {
		if (decisionItemProvider == null) {
			decisionItemProvider = new DecisionItemProvider(this);
		}

		return decisionItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.jvisio.flowchart.Transition} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TransitionItemProvider transitionItemProvider;

	/**
	 * This creates an adapter for a {@link com.jvisio.flowchart.Transition}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createTransitionAdapter() {
		if (transitionItemProvider == null) {
			transitionItemProvider = new TransitionItemProvider(this);
		}

		return transitionItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.jvisio.flowchart.Connector} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConnectorItemProvider connectorItemProvider;

	/**
	 * This creates an adapter for a {@link com.jvisio.flowchart.Connector}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createConnectorAdapter() {
		if (connectorItemProvider == null) {
			connectorItemProvider = new ConnectorItemProvider(this);
		}

		return connectorItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.jvisio.flowchart.Subprocess} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SubprocessItemProvider subprocessItemProvider;

	/**
	 * This creates an adapter for a {@link com.jvisio.flowchart.Subprocess}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createSubprocessAdapter() {
		if (subprocessItemProvider == null) {
			subprocessItemProvider = new SubprocessItemProvider(this);
		}

		return subprocessItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.jvisio.flowchart.Wait} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected WaitItemProvider waitItemProvider;

	/**
	 * This creates an adapter for a {@link com.jvisio.flowchart.Wait}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createWaitAdapter() {
		if (waitItemProvider == null) {
			waitItemProvider = new WaitItemProvider(this);
		}

		return waitItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.jvisio.flowchart.Flowchart} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FlowchartItemProvider flowchartItemProvider;

	/**
	 * This creates an adapter for a {@link com.jvisio.flowchart.Flowchart}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createFlowchartAdapter() {
		if (flowchartItemProvider == null) {
			flowchartItemProvider = new FlowchartItemProvider(this);
		}

		return flowchartItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.jvisio.flowchart.InputOutput} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InputOutputItemProvider inputOutputItemProvider;

	/**
	 * This creates an adapter for a {@link com.jvisio.flowchart.InputOutput}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createInputOutputAdapter() {
		if (inputOutputItemProvider == null) {
			inputOutputItemProvider = new InputOutputItemProvider(this);
		}

		return inputOutputItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.jvisio.flowchart.ManualOperation} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ManualOperationItemProvider manualOperationItemProvider;

	/**
	 * This creates an adapter for a {@link com.jvisio.flowchart.ManualOperation}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createManualOperationAdapter() {
		if (manualOperationItemProvider == null) {
			manualOperationItemProvider = new ManualOperationItemProvider(this);
		}

		return manualOperationItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.jvisio.flowchart.Document} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DocumentItemProvider documentItemProvider;

	/**
	 * This creates an adapter for a {@link com.jvisio.flowchart.Document}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createDocumentAdapter() {
		if (documentItemProvider == null) {
			documentItemProvider = new DocumentItemProvider(this);
		}

		return documentItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.jvisio.flowchart.Disk} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DiskItemProvider diskItemProvider;

	/**
	 * This creates an adapter for a {@link com.jvisio.flowchart.Disk}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createDiskAdapter() {
		if (diskItemProvider == null) {
			diskItemProvider = new DiskItemProvider(this);
		}

		return diskItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.jvisio.flowchart.Display} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DisplayItemProvider displayItemProvider;

	/**
	 * This creates an adapter for a {@link com.jvisio.flowchart.Display}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createDisplayAdapter() {
		if (displayItemProvider == null) {
			displayItemProvider = new DisplayItemProvider(this);
		}

		return displayItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.jvisio.flowchart.SwimLane} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SwimLaneItemProvider swimLaneItemProvider;

	/**
	 * This creates an adapter for a {@link com.jvisio.flowchart.SwimLane}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createSwimLaneAdapter() {
		if (swimLaneItemProvider == null) {
			swimLaneItemProvider = new SwimLaneItemProvider(this);
		}

		return swimLaneItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.jvisio.flowchart.Tape} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TapeItemProvider tapeItemProvider;

	/**
	 * This creates an adapter for a {@link com.jvisio.flowchart.Tape}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createTapeAdapter() {
		if (tapeItemProvider == null) {
			tapeItemProvider = new TapeItemProvider(this);
		}

		return tapeItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class) || (((Class)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory. 
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void dispose() {
		if (terminatorItemProvider != null) terminatorItemProvider.dispose();
		if (actionItemProvider != null) actionItemProvider.dispose();
		if (decisionItemProvider != null) decisionItemProvider.dispose();
		if (transitionItemProvider != null) transitionItemProvider.dispose();
		if (connectorItemProvider != null) connectorItemProvider.dispose();
		if (subprocessItemProvider != null) subprocessItemProvider.dispose();
		if (waitItemProvider != null) waitItemProvider.dispose();
		if (flowchartItemProvider != null) flowchartItemProvider.dispose();
		if (inputOutputItemProvider != null) inputOutputItemProvider.dispose();
		if (manualOperationItemProvider != null) manualOperationItemProvider.dispose();
		if (documentItemProvider != null) documentItemProvider.dispose();
		if (diskItemProvider != null) diskItemProvider.dispose();
		if (displayItemProvider != null) displayItemProvider.dispose();
		if (swimLaneItemProvider != null) swimLaneItemProvider.dispose();
		if (tapeItemProvider != null) tapeItemProvider.dispose();
	}

}
