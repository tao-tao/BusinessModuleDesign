package avicit.ui.view.navigator.views;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IDecorationContext;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.navigator.CommonViewer;

import avicit.ui.view.navigator.IProjectNavigator;
import avicit.ui.view.navigator.ResourceMapper;


public class AvicitProjectViewer extends CommonViewer implements IDecorationContext {

	AvicitProjectNavigator projectNavigator;
	private ResourceMapper resourceMapper;
	private IDecorationContext decorationContext;    
//	private AndFilter staticFilter;
	private boolean filterConfiguration;

	public AvicitProjectViewer(String viewerId, Composite parent, int style) {
		this(new AvicitProjectNavigator(), viewerId, parent, style);
	}

	public AvicitProjectViewer(Composite aParent, int aStyle) {
		this(new AvicitProjectNavigator(), "org.eclipse.ui.navigator.view", aParent, aStyle); //$NON-NLS-1$
	}

	public AvicitProjectViewer(AvicitProjectNavigator navigator, String aViewerId, Composite aParent, int aStyle) {
		super(aViewerId, aParent, aStyle);
		this.projectNavigator = navigator;
//		staticFilter = new AndFilter();
//		staticFilter.addFilter(new SourceContentFilter());
//		this.addFilter(new EmptyPackageFilter());
		resourceMapper = new ResourceMapper(this);
		// resourceMapper = new ResourceMapper(this);
		// webPageFilter = new WebPageFilter();
//		 configurationFileFilter = new ConfigurationFileFilter();
		// debugFilter = new DebugResourceFilter();
//		 dotFilter = new DotFilter();
//		 javaFilter = new JavaFilter();
		// webContentPackageFilter = new WebContentPackageFilter();
		// bizletJavaFilter = new BizletJavaFilter();
		 filterConfiguration = false;
		// filterWebPage = true;
		// filterJavaBizlet = true;
		// this.navigator = navigator;
		// staticFilter.addFilter(javaFilter);
		// staticFilter.addFilter(debugFilter);
		// staticFilter.addFilter(dotFilter);
		 addExtraFilters();
	}

	@Override
	protected void init() {
		super.init();
		IBaseLabelProvider labelProvider =  getLabelProvider();
		if(labelProvider instanceof DecoratingLabelProvider)
		{
			DecoratingLabelProvider decoratingLabelProvider = (DecoratingLabelProvider)labelProvider;
			decorationContext = decoratingLabelProvider.getDecorationContext();
			decoratingLabelProvider.setDecorationContext(this);
		}
	}

	@Override
    public void resetFilters()
    {
        getTree().setRedraw(false);
        super.resetFilters();
        addExtraFilters();
        getTree().setRedraw(true);
    }

    private void addExtraFilters()
    {
//        addFilter(staticFilter);
    }

	public AvicitProjectNavigator getNavigator() {
		return this.projectNavigator;
	}

	@Override
	public void doUpdateItem(Item item, Object element) {
		super.doUpdateItem(item, element);
	}

	public void doUpdateItem(Widget item) {
		try {
			for (TreeItem treeItem = (TreeItem) item; treeItem != null; treeItem = treeItem.getParentItem())
				doUpdateItem(((Item) (treeItem)), treeItem.getData());

		} catch (Exception _ex) {
			_ex.printStackTrace();
		}
	}

	@Override
	protected void mapElement(Object element, Widget item) {
		super.mapElement(element, item);
		if (item instanceof Item)
			resourceMapper.addToMap(element, (Item) item);
	}

	@Override
	protected void unmapElement(Object element, Widget item) {
		if (item instanceof Item)
			resourceMapper.removeFromMap(element, (Item) item);
		super.unmapElement(element, item);
	}

	@Override
	protected void unmapAllElements() {
		resourceMapper.clearMap();
		super.unmapAllElements();
	}

    public final boolean isFilterConfiguration()
    {
        return filterConfiguration;
    }

    public final void setFilterConfiguration(boolean filterConfiguration)
    {
        if(this.filterConfiguration == filterConfiguration)
            return;
        this.filterConfiguration = filterConfiguration;
        refresh();
    }

/*
	@Override
	protected void handleLabelProviderChanged(LabelProviderChangedEvent event) {
		if (event instanceof org.eclipse.jdt.ui.ProblemsLabelDecorator.ProblemsLabelChangedEvent) {
			org.eclipse.jdt.ui.ProblemsLabelDecorator.ProblemsLabelChangedEvent e = (org.eclipse.jdt.ui.ProblemsLabelDecorator.ProblemsLabelChangedEvent) event;
			if (!e.isMarkerChange() && canIgnoreChangesFromAnnotionModel())
				return;
		}
		Object changed[] = addAditionalProblemParents(event.getElements());
		if (!ArrayUtils.isEmpty(changed) && !resourceMapper.isEmpty()) {
			ArrayList others = new ArrayList();
			for (int i = 0; i < changed.length; i++) {
				Object current = changed[i];
				others.add(current);
			}

			if (others.isEmpty())
				return;
			event = new LabelProviderChangedEvent((IBaseLabelProvider) event.getSource(), others.toArray());
		} else if (event.getElements() != changed)
			event = new LabelProviderChangedEvent((IBaseLabelProvider) event.getSource(), changed);
		super.handleLabelProviderChanged(event);
	}

	private boolean canIgnoreChangesFromAnnotionModel() {
		Object contentProvider = getContentProvider();
		return (contentProvider instanceof IWorkingCopyProvider) && !((IWorkingCopyProvider) contentProvider).providesWorkingCopies();
	}

	protected Object[] addAditionalProblemParents(Object elements[]) {
		return elements;
	}
*/
//	@Override
	public String[] getProperties() {
		return (String[])ArrayUtils.add(decorationContext.getProperties(), IProjectNavigator.class.getName());
	}

//	@Override
	public Object getProperty(String property) {
		if (IProjectNavigator.class.getName().equals(property)) {
			AvicitProjectNavigator navigatorInstance = AvicitProjectNavigator.getViewInstance();
			if (navigatorInstance == null || navigatorInstance.getCommonViewer() == this)
				return getNavigator();
		}
		return decorationContext.getProperty(property);
	}

	public ResourceMapper getResourceMapper() {
		return resourceMapper;
	}

}
