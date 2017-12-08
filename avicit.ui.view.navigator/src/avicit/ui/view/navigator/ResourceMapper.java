package avicit.ui.view.navigator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.model.IWorkbenchAdapter;

import avicit.ui.common.util.AdapterUtil;
import avicit.ui.core.runtime.resource.EclipseFileDelegate;
import avicit.ui.core.runtime.resource.EclipseFolderDelegate;
import avicit.ui.core.runtime.resource.EclipseProjectDelegate;
import avicit.ui.core.runtime.resource.EclipseResourceManager;
import avicit.ui.core.runtime.resource.EclipseSourceFolderDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.cluster.FunctionClusterModelFactory;
import avicit.ui.runtime.core.cluster.FunctionClusterNode;
import avicit.ui.runtime.core.cluster.function.ComponentModelFactory;
import avicit.ui.runtime.core.cluster.function.ComponentNode;
import avicit.ui.runtime.core.node.AbstractFolderNode;
import avicit.ui.runtime.core.node.AbstractNode;
import avicit.ui.runtime.core.node.ComponentLibNode;
import avicit.ui.runtime.core.node.GuizeNode;
import avicit.ui.runtime.core.node.NavigatorMessages;
import avicit.ui.runtime.core.node.ProjectNode;
import avicit.ui.view.module.ComponentLibModelFactory;
import avicit.ui.view.module.GuizeModelFactory;
import avicit.ui.view.module.IModelChangeListener;
import avicit.ui.view.module.ModelChangeEvent;
import avicit.ui.view.module.ModelManagerImpl;
import avicit.ui.view.module.ProjectModelFactory;
import avicit.ui.view.navigator.builder.AvicitLibNature;
import avicit.ui.view.navigator.util.SwtResourceUtil;
import avicit.ui.view.navigator.views.AvicitProjectNavigator;
import avicit.ui.view.navigator.views.AvicitProjectViewer;

import com.tansun.runtime.resource.adapter.ExpressionAdapterManager;
import com.tansun.runtime.resource.adapter.WorkbenchAdapterFactory;

@SuppressWarnings("restriction")
public class ResourceMapper implements IResourceChangeListener,
		IModelChangeListener {
	private IProject pro;

	public ResourceMapper(AvicitProjectViewer viewer) {
		resourceToItems = new HashMap();
		reuseLists = new Stack();
		this.viewer = viewer;
	}

	protected void setRedraw(boolean redraw) {
		if (viewer == null)
			return;
		Control control = viewer.getControl();
		if (SwtResourceUtil.isValid(control))
			control.setRedraw(redraw);
	}

	private void update(Item item) {
//		if (SwtResourceUtil.isValid(item))
		if(item != null && !item.isDisposed())
			viewer.doUpdateItem(item);
	}

	private void update(Object object) {
		if (object == null)
			return;
		if (object instanceof Item) {
			update((Item) object);
		} else {
			List list = (List) object;
			for (int i = 0; i < list.size(); i++)
				update((Item) list.get(i));
		}
	}

	public void update(Iterator iterator) {
		Set set = new HashSet();
		while (iterator.hasNext()) {
			IResource resource = (IResource) iterator.next();
			Object object;
			for (object = null; object == null;) {
				object = resourceToItems.get(resource);
				resource = resource.getParent();
				if (resource == null)
					break;
			}

			if (object != null)
				set.add(object);
		}
		for (iterator = set.iterator(); iterator.hasNext(); update(iterator
				.next()))
			;
	}

	private void refresh(Object object) {
		if (object == null)
			return;
		if (object instanceof Item) {
			refresh((Item) object);
		} else {
			List list = (List) object;
			for (int i = 0; i < list.size(); i++)
				refresh((Item) list.get(i));
		}
	}

	private void refresh(Iterator iterator) {
		Set set = new HashSet();
		Object object;
		for (; iterator.hasNext(); set.add(object)) {
			IResource resource = (IResource) iterator.next();
			object = resourceToItems.get(resource);
		}

		for (iterator = set.iterator(); iterator.hasNext();)
			refresh(iterator.next());
	}

	private void refresh(Item item) {
//		if (SwtResourceUtil.isValid(item)) {
		if(item != null && !item.isDisposed()){
			Object data = item.getData();
			// viewer.setHasChildren(data,true);
			viewer.refresh(data, true);
			viewer.doUpdateItem(item);
		}
	}

	public void addToMap(Object element, Item item) {
		if (item == null)
			return;
		IResource resources[] = getCorrespondingResource(element);
		if (!ArrayUtils.isEmpty(resources)) {
			for (int i = 0; i < resources.length; i++) {
				IResource resource = resources[i];
				addToMap(item, resource);
			}
		}
	}

	private void addToMap(Item item, IResource resource) {
		Object existingMapping = resourceToItems.get(resource);
		if (existingMapping == null) {
			resourceToItems.put(resource, item);
			return;
		}
		if (existingMapping instanceof Item) {
			if (existingMapping != item) {
				List list = getNewList();
				list.add(existingMapping);
				list.add(item);
				resourceToItems.put(resource, list);
			}
		} else {
			List list = (List) existingMapping;
			if (!list.contains(item))
				list.add(item);
		}
	}

	public void removeFromMap(Object element, Item item) {
		IResource resources[] = getCorrespondingResource(element);
		if (resources != null) {
			for (int i = 0; i < resources.length; i++) {
				IResource resource = resources[i];
				if (resource != null)
					removeFromMap(resource, item);
			}
		}
	}

	public void removeFromMap(IResource resource, Item item) {
		Object existingMapping = resourceToItems.get(resource);
		if (existingMapping == null)
			return;
		if (existingMapping instanceof Item) {
			resourceToItems.remove(resource);
		} else {
			List list = (List) existingMapping;
			list.remove(item);
			if (list.isEmpty()) {
				resourceToItems.remove(list);
				releaseList(list);
			}
		}
	}

	private List getNewList() {
		if (!reuseLists.isEmpty())
			return (List) (List) reuseLists.pop();
		else
			return new ArrayList(2);
	}

	private void releaseList(List list) {
		if (reuseLists.size() < 10)
			reuseLists.push(list);
	}

	public void clearMap() {
		resourceToItems.clear();
	}

	public boolean isEmpty() {
		return resourceToItems.isEmpty();
	}

	private boolean isControlValid() {
		Control ctrl = viewer.getControl();
//		return SwtResourceUtil.isValid(ctrl);
		return true;
	}

	public void resourceChanged(IResourceChangeEvent event) {
		IResourceDelta delta = event.getDelta();

		if (delta == null) {
			return;
		} else {
			processResourceDeltas(new IResourceDelta[] { delta }, 0x80000000);
			return;
		}
	}

	private void processResourceDeltas(IResourceDelta deltas[], int delay) {
		if (ArrayUtils.isEmpty(deltas))
			return;
		Collection refreshList = new HashSet();
		Collection updateList = new HashSet();
		boolean refreshAll = false;
		for (int i = 0; i < deltas.length; i++) {
			IResourceDelta delta = deltas[i];
			refreshAll = processDelta(delta, refreshList, updateList);
			if (refreshAll) {

				break;
			}

			if (!isControlValid()) {

				return;
			}

		}
		// System.out.println("..................2"+pro);
		refresh(refreshAll, refreshList, updateList, delay);

	}

	private boolean processDelta(final IResourceDelta delta,
			Collection refreshList, Collection updateList) {
		// List deltaList;
		// boolean flatView;
		List deltaList = new ArrayList();
		deltaList.add(delta);
		// flatView = true;
		AvicitProjectNavigator navigator = AvicitProjectNavigator.getViewInstance();
		if (navigator != null) {
			IResourceDelta resourceDelta;
			while (deltaList.size() > 0) {
				resourceDelta = (IResourceDelta) deltaList.remove(0);
				final IResource resource = resourceDelta.getResource();
				IResourceDelta deltas[] = resourceDelta.getAffectedChildren(7,
						2);
				for (int i = 0; i < deltas.length; i++) {
					IResourceDelta childResourceDelta = deltas[i];
					deltaList.add(childResourceDelta);
				}
				if(resource.getName().contains("WebRoot")){
					continue;
				}

				if (resource.getType() == IResource.FOLDER
						&& EclipseResourceManager
								.isOutputFolder((IFolder) resource))
					return true;

//				if( resourceDelta.getKind() == IResourceDelta.REMOVED && resource.getParent() instanceof AbstractFolderNode ){
//					deltaList.add(resource);
//					return false;
//				}

				if (resourceDelta.getKind() == IResourceDelta.REMOVED) {
					ModelManagerImpl
							.getInstance()
							.getPool()
							.detachModel(
									new EclipseProjectDelegate(
											resource.getProject()),
									FunctionClusterModelFactory.TYPE);
					refreshList.add(resource.getProject());
					continue;
				}

				if (resource.getName().equals(".settings"))
					continue;

				if (resource.getType() == IResource.PROJECT
						&& ((IProject) resource).isOpen()
						&& ((resourceDelta.getKind() & 2) != 0 || (resourceDelta
								.getKind() & 1) != 0))
					continue;
				// IFolder sourceFolder = null;
				if(resource.getName().contains(".index"))
					continue;
				if(resource.getName().startsWith("sub-system"))
					return false;
//					continue;
				if(!resource.getName().contains(".") )
					continue;
				IContainer parent = resource.getParent();
				if (parent instanceof IProject) {
					try {
						if (((IProject) parent)
								.hasNature(AvicitLibNature.NATURE_ID)
								&& ((resourceDelta.getKind() & 2) != 0 || (resourceDelta
										.getKind() & 1) != 0)) {
							
							ProjectNode parentNode = (ProjectNode) ModelManagerImpl
									.getInstance()
									.getPool()
									.getModel(
											new EclipseProjectDelegate(
													(IProject) parent),
											ProjectModelFactory.TYPE, true,
											null);
							parentNode.setChildren(null);
							refreshList.add(parent);
							continue;
						}
					} catch (CoreException e) {
						e.printStackTrace();
					}
				} else if (parent != null) {
					IContainer pparent = parent.getParent();
					try {
						if (pparent instanceof IProject
								&& ((IProject) pparent)
										.hasNature(AvicitLibNature.NATURE_ID)) {
							
							ComponentLibNode parentNode = (ComponentLibNode) ModelManagerImpl
									.getInstance()
									.getPool()
									.getModel(
											new EclipseFolderDelegate(
													(IFolder) parent),
											ComponentLibModelFactory.TYPE,
											true, null);
							if (parentNode != null) {
								parentNode.setChildren(null);
								refreshList.add(parent);
								continue;
							}
						}
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
				if (resource.getType() == IResource.FILE) {
					IFile file = (IFile) resource;
					pro = file.getProject();
					if (".classpath".equalsIgnoreCase(file.getName())) {
						
						ModelManagerImpl
								.getInstance()
								.getPool()
								.detachModel(
										new EclipseProjectDelegate(
												resource.getProject()),
										ProjectModelFactory.TYPE);
						refreshList.add(resource.getProject());
					} else if (".project".equalsIgnoreCase(file.getName())) {
						refreshList.add(resource.getProject());
					} else {
						IFolder sourceFolder = EclipseResourceManager
								.getSourceFolder(file);
						if (sourceFolder == null) {
							ProjectchangeModelNode(resourceDelta.getKind(),
									file);
							//System.out.println(resource.getType()+"  "+resource.getName());


						} 
						else if(".ec".equalsIgnoreCase(file.getName()) && resourceDelta.getKind() == 2){
							ModelManagerImpl
							.getInstance()
							.getPool()
							.detachModel(
									new EclipseProjectDelegate(
											resource.getProject()),
									ProjectModelFactory.TYPE);
					refreshList.add(resource.getProject());
							
						}
						
						else if (".ec".equalsIgnoreCase(file.getName())) {
							refreshComponent(resourceDelta.getKind(),
									sourceFolder, file);
						} 
						//0408
						else if(".sub".equalsIgnoreCase(file.getName()) && resourceDelta.getKind() == 2){
							ModelManagerImpl
							.getInstance()
							.getPool()
							.detachModel(
									new EclipseProjectDelegate(
											resource.getProject()),
									ProjectModelFactory.TYPE);
					refreshList.add(resource.getProject());
//							System.out.println("KS----------"+ resourceDelta.getKind() +"--------"+resourceDelta.getFullPath());
						} 
						else if (((resourceDelta.getFlags() & IResourceDelta.CONTENT) != 0 || (resourceDelta
								.getFlags() & IResourceDelta.REPLACED) != 0)) {
							refreshList.add(resource);
						} else /*
								 * if ((resourceDelta.getKind() &
								 * IResourceDelta.ADDED) != 0 ||
								 * (resourceDelta.getKind() &
								 * IResourceDelta.REMOVED) != 0)
								 */
						{
							//changeModelNode(resourceDelta.getKind(), file);
						}
					}
					continue;
				} 
				

			}
		}
		return false;
	}

	public void changeModelNode(int i, IFile file) {
		Job job = new AddRemoveModelNode("changeModelNode", i, file);
		job.schedule();
	}

	public void ProjectchangeModelNode(int i, IFile file) {
		Job job = new ProjectAddRemoveModelNode("ProjectchangeModelNode", i,
				file);
		job.schedule();
	}

	public void refreshModelNode(int i, AbstractFolderNode folderNode,
			AbstractNode fileNode) {

		// Job job = new RefreshModelNode("refreshModelNode", i, folderNode,
		// fileNode);
		// job.schedule();
	}

	public void refreshComponent(int i, IFolder sourceFolder, IFile file) {
		Job job = new RefreshComponentNode("refreshComponent", i, sourceFolder,
				file);
		job.schedule();
	}

	public void refresh(boolean refreshAll, Collection refreshList,
			Collection updateList, long delay) {
		if (!isControlValid())
			return;
		Job job = null;
		if (refreshAll) {
			job = refreshAll();
		} else {
			if (refreshList.isEmpty() && updateList.isEmpty())
				return;
			job = refreshList(refreshList, updateList);
		}
		if (delay > 0L)
			job.schedule(delay);
		else
			job.schedule();
	}

	private Job refreshList(final Collection refreshList,
			final Collection updateList) {
		updateList.addAll(refreshList);
		Job job = new Job("refreshAll") {
			protected IStatus run(IProgressMonitor monitor) {
				if (isControlValid()) {
					Display display = Display.getDefault();
					if (display != null)
						display.asyncExec(new Runnable() {

							public void run() {
//								if (SwtResourceUtil.isValid(viewer.getTree()))
								if(true)
									try {
										refresh(refreshList.iterator());
									} catch (Exception _ex) {
									}
//								if (SwtResourceUtil.isValid(viewer.getTree()))
								if(viewer.getTree() != null && !viewer.getTree().isDisposed())
									try {
										update(updateList.iterator());
									} catch (Exception _ex) {
									}
							}
						});
				}
				return Status.OK_STATUS;
			}
		};
		return job;
	}

	private Job refreshAll() {
		Job job = new Job(NavigatorMessages.REFRESH_PROJECT_VIEW) {
			protected IStatus run(IProgressMonitor monitor) {
				Display display = Display.getDefault();
				if (display != null)
					display.asyncExec(new Runnable() {

						public void run() {
//							 if (SwtResourceUtil.isValid(viewer.getTree()))
//							 try {
//							 viewer.refresh();
//							 } catch (Exception _ex) {
//							 }
						}
					});
				return Status.OK_STATUS;
			}
		};

		job.setPriority(Job.SHORT);
		return job;
	}

	private static IResource[] getCorrespondingResource(Object element) {
		if (!(element instanceof INode
				|| element instanceof PackageFragmentRoot || element instanceof IProject))
			return new IResource[0];
		Object object = ExpressionAdapterManager.getInstance().getAdapter(
				element, IResourceAnalyser.class);
		if (object instanceof IResourceAnalyser) {
			IResourceAnalyser analyser = (IResourceAnalyser) object;
			return analyser.getRelevantResources(element);
		}
		object = AdapterUtil.getAdapter(element, IResource.class);
		if (object instanceof IResource)
			return (new IResource[] { (IResource) object });
		return null;
	}



	class RefreshComponentNode extends Job {

		private IFolder sourceFolder;
		private IFile file;
		private int type;

		public RefreshComponentNode(String name, int type,
				IFolder sourceFolder, IFile file) {
			super(name);
			this.sourceFolder = sourceFolder;
			this.file = file;
			this.type = type;
		}

		protected IStatus run(IProgressMonitor arg0) {
			Display display = Display.getDefault();
			if (display != null)
				display.syncExec(new Runnable() {
					public void run() {
						if (type == IResourceDelta.ADDED) {
						
							ModelManagerImpl
									.getInstance()
									.getPool()
									.detachModel(
											new EclipseSourceFolderDelegate(
													sourceFolder),
											ComponentModelFactory.TYPE);
							ModelManagerImpl
									.getInstance()
									.getPool()
									.detachModel(new EclipseFileDelegate(file),
											ComponentModelFactory.TYPE);

							final IProject project = sourceFolder.getProject();
							
							ProjectNode parent = (ProjectNode) ModelManagerImpl
									.getInstance()
									.getPool()
									.getModel(
											new EclipseProjectDelegate(project),
											ProjectModelFactory.TYPE, true,
											null);
							parent.setChildren(null);
							if (SwtResourceUtil.isValid(viewer.getTree())) {
								try {
									// resourceToItems.remove(sourceFolder);
									// viewer.add(parent, child);
									// Object object =
									// resourceToItems.get(sourceFolder);
									// update(object);
									Object object = resourceToItems
											.get(project);
									update(object);
									refresh(object);
									// viewer.refresh(project);
								} catch (Exception _ex) {
									_ex.printStackTrace();
								}
							}
							return;
						}

						Object item = resourceToItems.get(sourceFolder);
						if (item instanceof List) {
							Object node = null;
							for (int i = 0; i < ((List) item).size(); i++) {
								Item item1 = (Item) ((List) item).get(i);
								if (!item1.isDisposed()) {
									if (item1.getData() instanceof ComponentNode) {
										node = item1;
										break;
									}
								}
							}
							item = node;
						}
						Object model = null;
						if (item != null && item instanceof Item) {
							Item obj = (Item) item;
							if (!obj.isDisposed())
								model = obj.getData();
						}

						if (model == null || !(model instanceof ComponentNode))
							return;

						if (type == IResourceDelta.REMOVED) {
							
							ModelManagerImpl
									.getInstance()
									.getPool()
									.detachModel(
											new EclipseSourceFolderDelegate(
													sourceFolder),
											ComponentModelFactory.TYPE);
							// final ComponentNode child = (ComponentNode)
							// ModelManagerImpl.getInstance().getPool().getModel(new
							// EclipseSourceFolderDelegate(sourceFolder),
							// ComponentModelFactory.TYPE, true, null);

							ComponentNode child = (ComponentNode) model;
							ProjectNode parent = (ProjectNode) child
									.getParentNode();
							parent.setChildren(null);
							if (SwtResourceUtil.isValid(viewer.getTree())) {
								try {
									viewer.remove(model);
									// resourceToItems.remove(sourceFolder);
									// viewer.add(parent, child);
									Object object = resourceToItems
											.get(sourceFolder.getProject());
									// update(object);
									// object =
									// resourceToItems.get(sourceFolder);

									update(object);
									refresh(object);
								} catch (Exception _ex) {
									_ex.printStackTrace();
								}
							}
							return;
						}
						if (type == IResourceDelta.CHANGED) {
							// ModelManagerImpl.getInstance().getPool().detachModel(new
							// EclipseSourceFolderDelegate(sourceFolder),
							// ComponentModelFactory.TYPE);

							// ModelManagerImpl.getInstance().getPool().detachModel(new
							// EclipseFileDelegate(file));
							// model =
							// ModelManagerImpl.getInstance().getPool().getModel(new
							// EclipseSourceFolderDelegate(sourceFolder),
							// ComponentModelFactory.TYPE, true, null);
							// ((ComponentNode)model).setChildren(null);
							// if(!(model instanceof ComponentNode))
							// return;

							WorkbenchAdapterFactory factory = new WorkbenchAdapterFactory();
							ComponentNode node = (ComponentNode) model;
							node.reloadProperties();
							Object[] newChild = ComponentModelFactory
									.createOrGetChildren(node, true);

							List newList = new ArrayList();
							Object childItem = resourceToItems.get(file);
							List oldList = new ArrayList();
							if (childItem != null
									&& !(childItem instanceof List)) {
								oldList.add(childItem);
							} else if (childItem != null)
								oldList.addAll((List) childItem);
							if (node.getChildren() != null) {
								// List old = (List)childItem;
								Iterator itor = oldList.iterator();
								while (itor.hasNext()) {
									Item childmodel = (Item) itor.next();
									if (childmodel == null)
										continue;
									if (!childmodel.isDisposed()
											&& ArrayUtils.contains(newChild,
													childmodel.getData())) {
										newChild = ArrayUtils.removeElement(
												newChild, childmodel.getData());
										itor.remove();
									} else {
										if (SwtResourceUtil.isValid(viewer
												.getTree())) {
											try {
												if (!childmodel.isDisposed()) {
													INode cnode = (INode) childmodel
															.getData();
													ModelManagerImpl
															.getInstance()
															.getPool()
															.detachModel(
																	cnode.getResource(),
																	cnode.getType());
													node.getChildren().remove(
															cnode);
													// viewer.remove(cnode);
													update(childmodel);
												}
											} catch (Exception _ex) {
												_ex.printStackTrace();
											}
										}
									}
								}
								for (int i = 0; i < newChild.length; i++) {
									if (SwtResourceUtil.isValid(viewer
											.getTree())
											&& newChild[i] instanceof INode) {
										INode childnode = (INode) newChild[i];
										try {
											node.getChildren().add(childnode);
											// viewer.add(node, childnode);
										} catch (Exception _ex) {
											_ex.printStackTrace();
										}
									}
								}
								if (newChild.length > 0 || oldList.size() > 0) {
									try {
										Object object = resourceToItems
												.get(sourceFolder);
										update(object);
										refresh(object);
										object = resourceToItems.get(file);
										update(object);
										refresh(object);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						}
						refresh(resourceToItems.get(sourceFolder.getProject()));
					}
				});
			return Status.OK_STATUS;
		}
	}

	class AddRemoveModelNode extends Job {

		private IFile file;
		private int type;

		public AddRemoveModelNode(String name, int type, IFile file) {
			super(name);
			this.file = file;
			this.type = type;
		}

		protected IStatus run(IProgressMonitor arg0) {
			Display display = Display.getDefault();
			if (display != null)
				display.asyncExec(new Runnable() {
					public void run() {
						//System.out.println(type + file.getName());
						if (type == IResourceDelta.CHANGED|| file.getFullPath().toString()
								.contains("WebRoot")||file.getName().endsWith(".jpeg"))
							return;
						System.out.println("project add dele");
						AbstractFolderNode folderNode = null;
						AbstractNode childNode = null;
						IFolder sourceFolder = EclipseResourceManager
								.getSourceFolder(file);
						final IFolder parentFolder = (IFolder) file.getParent();

						if (parentFolder != null) {
							Object item = resourceToItems.get(file);
							Object model = null;
							if (item instanceof List) {
								Object node = null;
								for (int i = 0; i < ((List) item).size(); i++) {
									Item item1 = (Item) ((List) item).get(i);
									if (item1.getData() instanceof INode) {
										model = item1.getData();
										break;
									}
								}
							} else if (item instanceof Item) {
								if (!((Item) item).isDisposed())
									if (((Item) item).getData() instanceof INode)
										model = ((Item) item).getData();
							}
							if ((model != null)) {
								Object pmodel = ((AbstractNode) model)
										.getParent();
								if (pmodel instanceof AbstractFolderNode) {
									childNode = (AbstractNode) model;
									folderNode = (AbstractFolderNode) pmodel;
								}
							} else if (((type & IResourceDelta.ADDED) != 0)
									|| (type & IResourceDelta.CHANGED) != 0) {
								Object o = ModelManagerImpl
										.getInstance()
										.getPool()
										.getModel(
												new EclipseSourceFolderDelegate(
														sourceFolder),
												ComponentModelFactory.TYPE,
												true, null);

								ComponentNode component = null;
								if (o instanceof ComponentNode)
									component = (ComponentNode) o;

								if (component != null) {
									childNode = component
											.getOrCreateNode(
													new EclipseFileDelegate(
															file), true);
									if (childNode != null)
										folderNode = (AbstractFolderNode) childNode
												.getParent();
								}
							}
						}

						if (folderNode == null)
							return;

						// IFolder folder = (IFolder)
						// folderNode.getResource().getResource();

						AvicitProjectViewer viewer = AvicitProjectNavigator.getViewer();
						List refresh = new ArrayList();
						if (type == IResourceDelta.ADDED /*
														 * (type &
														 * IResourceDelta
														 * .CHANGED) != 0
														 */) {
							IWorkbenchAdapter adapter = (IWorkbenchAdapter) new WorkbenchAdapterFactory()
									.getAdapter(folderNode,
											IWorkbenchAdapter.class);
							if (adapter != null) {
								adapter.getChildren(folderNode);
								List children = folderNode.getChildren();
								if (children != null) {
									// children.add(childNode);
									if (SwtResourceUtil.isValid(viewer
											.getTree())) {
										if (folderNode.getOrder() == -1) {
											AbstractFolderNode p = (AbstractFolderNode) folderNode
													.getParent();
											IWorkbenchAdapter adapter1 = (IWorkbenchAdapter) new WorkbenchAdapterFactory()
													.getAdapter(
															p,
															IWorkbenchAdapter.class);
											if (adapter1 != null) {
												adapter1.getChildren(p);
											}
											if (p.getChildren() != null) {
												p.getChildren().add(folderNode);
											}
											// viewer.add(folderNode.getParent(),
											// folderNode);
											refresh.add(p.getResource()
													.getResource());
											folderNode.setOrder(1);
										}
										// viewer.add(folderNode, childNode);
										refresh.add(folderNode.getResource()
												.getResource());
									}
								}
							}
						} else if (type == IResourceDelta.REMOVED) {
							IWorkbenchAdapter adapter = (IWorkbenchAdapter) new WorkbenchAdapterFactory()
									.getAdapter(folderNode,
											IWorkbenchAdapter.class);
							if (adapter != null) {
								adapter.getChildren(folderNode);
								List children = folderNode.getChildren();
								if (children != null) {
									children.remove(childNode);
									if (SwtResourceUtil.isValid(viewer
											.getTree())) {
										// viewer.remove(folderNode, new
										// Object[]{childNode});
										refresh.add(folderNode.getResource()
												.getResource());
									}
								}
							}
						}

						if (SwtResourceUtil.isValid(viewer.getTree())) {
							if ((type & IResourceDelta.CHANGED) == 0)
								refresh.add(folderNode.getResource()
										.getResource());
							refresh.add(childNode.getResource().getResource());
							refresh(false, refresh, refresh, 500);
						}
					}
				});
			return Status.OK_STATUS;
		}
	}

	class ProjectAddRemoveModelNode extends Job {

		private IFile file;
		private int type;

		public ProjectAddRemoveModelNode(String name, int type, IFile file) {
			super(name);
			this.file = file;
			this.type = type;
		}

		protected IStatus run(IProgressMonitor arg0) {
			Display display = Display.getDefault();
			if (display != null)
				display.asyncExec(new Runnable() {
					public void run() {
						if (type == IResourceDelta.CHANGED
								|| file.getFullPath().toString()
										.contains("WebRoot"))
							return;
						System.out.println("project add dele real"+file.getFullPath());
						AbstractFolderNode folderNode = null;
						AbstractNode childNode = null;
						IFolder sourceFolder = file.getProject().getFolder(
								file.getParent().getFullPath());
						if (file.getParent() instanceof IProject) {
							return;
						}
						final IFolder parentFolder = (IFolder) file.getParent();
						sourceFolder = parentFolder;
						if (parentFolder != null) {
							Object item = resourceToItems.get(file);
							Object model = null;
							if (item instanceof List) {
								Object node = null;
								for (int i = 0; i < ((List) item).size(); i++) {
									Item item1 = (Item) ((List) item).get(i);
									if (item1.getData() instanceof INode) {
										model = item1.getData();
										break;
									}
								}
							} else if (item instanceof Item) {
								if (!((Item) item).isDisposed())
									if (((Item) item).getData() instanceof INode)
										model = ((Item) item).getData();
							}
							if ((model != null)) {
								Object pmodel = ((AbstractNode) model)
										.getParent();
								if (pmodel instanceof AbstractFolderNode) {
									childNode = (AbstractNode) model;
									folderNode = (AbstractFolderNode) pmodel;
								}
							} else if (((type & IResourceDelta.ADDED) != 0)
									|| (type & IResourceDelta.CHANGED) != 0) {

								Object o = ModelManagerImpl
										.getInstance()
										.getPool()
										.getModel(
												new EclipseFolderDelegate(
														sourceFolder
																.getParent()),
												GuizeModelFactory.TYPE, true,
												null);

								GuizeNode pmodel = null;
								if (o instanceof GuizeNode)
									pmodel = (GuizeNode) o;

								if (pmodel != null) {

									childNode = pmodel
											.getOrCreateNode(
													new EclipseFileDelegate(
															file), true);
									if (childNode != null)
										folderNode = (AbstractFolderNode) childNode
												.getParent();
								}
							}
						}

						if (folderNode == null)
							return;

						// IFolder folder = (IFolder)
						// folderNode.getResource().getResource();

						AvicitProjectViewer viewer = AvicitProjectNavigator.getViewer();
						List refresh = new ArrayList();
						if (type == IResourceDelta.ADDED /*
														 * (type &
														 * IResourceDelta
														 * .CHANGED) != 0
														 */) {
							IWorkbenchAdapter adapter = (IWorkbenchAdapter) new WorkbenchAdapterFactory()
									.getAdapter(folderNode,
											IWorkbenchAdapter.class);
							if (adapter != null) {
								adapter.getChildren(folderNode);
								List children = folderNode.getChildren();
								if (children != null) {
									// children.add(childNode);
									if (SwtResourceUtil.isValid(viewer
											.getTree())) {
										if (folderNode.getOrder() == -1) {
											AbstractFolderNode p = (AbstractFolderNode) folderNode
													.getParent();

											IWorkbenchAdapter adapter1 = (IWorkbenchAdapter) new WorkbenchAdapterFactory()
													.getAdapter(
															p,
															IWorkbenchAdapter.class);
											if (adapter1 != null) {
												adapter1.getChildren(p);
											}
											if (p.getChildren() != null) {
												p.getChildren().add(folderNode);
											}
											// viewer.add(folderNode.getParent(),
											// folderNode);
											refresh.add(p.getResource()
													.getResource());
											folderNode.setOrder(1);
										}
										// viewer.add(folderNode, childNode);
										try {
											refresh.add(folderNode
													.getResource().getProject()
													.getResource());
										} catch (ResourceException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
							}
						} else if (type == IResourceDelta.REMOVED) {
							IWorkbenchAdapter adapter = (IWorkbenchAdapter) new WorkbenchAdapterFactory()
									.getAdapter(folderNode,
											IWorkbenchAdapter.class);
							if (adapter != null) {
								adapter.getChildren(folderNode);
								List children = folderNode.getChildren();
								if (children != null) {
									children.remove(childNode);
									if (SwtResourceUtil.isValid(viewer
											.getTree())) {
										// viewer.remove(folderNode, new
										// Object[]{childNode});
										refresh.add(folderNode.getResource()
												.getResource());
									}
								}
							}
						}

						if (SwtResourceUtil.isValid(viewer.getTree())) {
							if ((type & IResourceDelta.CHANGED) == 0)
								refresh.add(folderNode.getResource()
										.getResource());
							refresh.add(childNode.getResource().getResource());
							refresh(false, refresh, refresh, 500);
						}
					}
				});
			return Status.OK_STATUS;
		}
	}

	private HashMap resourceToItems;
	private Stack reuseLists;
	private AvicitProjectViewer viewer;

	@Override
	public void modelChanged(ModelChangeEvent modelchangeevent) {
		// TODO Auto-generated method stub
		System.out.println("");
	}
}