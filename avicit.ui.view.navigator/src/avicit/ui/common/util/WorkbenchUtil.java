package avicit.ui.common.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.internal.registry.Contribution;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

import avicit.ui.core.runtime.resource.EclipseProjectDelegate;
import avicit.ui.core.runtime.resource.EclipseResourceManager;
import avicit.ui.core.runtime.resource.IProjectDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.INode;

import com.jniwrapper.win32.system.Module;

public class WorkbenchUtil
{

    public static WorkbenchUtil getInstance()
    {
        return instance;
    }

    private WorkbenchUtil()
    {
    }

    public static IEditorPart findEditor(IFile file)
    {
        if(!EclipseResourceManager.isValidResource(file))
            return null;
        final IEditorInput input = new FileEditorInput(file);
        IWorkbenchWindow window = getWorkbenchWindow();
        if(window == null)
            return null;
        final IWorkbenchPage page = getWorkbenchPage();
        final List list = new ArrayList(1);
        if(page == null)
            return null;
        Display display = window.getShell().getDisplay();
        if(display != null)
        {
            if(display.getThread() == Thread.currentThread())
                return page.findEditor(input);
            display.syncExec(new Runnable() {
                public void run()
                {
                    list.add(page.findEditor(input));
                }
            });
        }
        if(list.isEmpty())
            return null;
        else
            return (IEditorPart)list.get(0);
    }

    public static IEditorPart activeEditor(IFile file, boolean forceOpen)
        throws PartInitException
    {
        if(!EclipseResourceManager.isValidResource(file))
            return null;
        IEditorInput input = new FileEditorInput(file);
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorPart editor = page.findEditor(input);
        if(editor != null)
        {
            page.bringToTop(editor);
            return editor;
        }
        if(forceOpen)
            return IDE.openEditor(page, file);
        else
            return null;
    }

    public static boolean hasSubFolders(IStructuredSelection selection)
    {
        Iterator iterator = selection.iterator();
        IFolder folder;
        while(iterator.hasNext())
        {
	        Object object = iterator.next();
	        object = AdapterUtil.getAdapter(object, IResource.class);
	        if(object instanceof IFolder)
	        {
		        folder = (IFolder)object;
		        try {
					if(EclipseResourceManager.hasFolder(folder, (String)null))
					    return true;
				} catch (CoreException e) {
				}
	        }
        }
        return false;
    }

    public static Set computeRelevantResources(IStructuredSelection selection, boolean includingSubFolder)
    {
        Set results = new HashSet();
        for(Iterator iterator = selection.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            IResource resource = null;
            if(object instanceof IResource)
                resource = (IResource)object;
            if(object instanceof ICompilationUnit)
            {
                IJavaElement element = (IJavaElement)object;
                if(element.isReadOnly())
                {
                    results.clear();
                    return results;
                }
                results.add(element.getResource());
            } else
            {
                if(object instanceof INode)
                {
                	INode eosElement = (INode)object;
                    if(!eosElement.isVirtual())
                    {
                        results.clear();
                        return results;
                    }
                }
                if(object instanceof Contribution)
                {
                    Object resourceCandidate = AdapterUtil.getAdapter(object, IResource.class);
                    if(resourceCandidate instanceof IResource)
                        resource = ((IResource)resourceCandidate).getParent();
                }
                if(object instanceof Module)
                {
                    Object resourceCandidate = AdapterUtil.getAdapter(object, IResource.class);
                    if(resourceCandidate instanceof IResource)
                        resource = (IResource)resourceCandidate;
                }
                {
                    resource = (IResource)AdapterUtil.getAdapter(object, IResource.class);
                    if(EclipseResourceManager.isValidResource(resource))
                    {
                        results.add(resource);
                    } else
                    {
                        results.clear();
                        return results;
                    }
                }
            }
        }

        if(!results.isEmpty())
        {
            IResource candidateResources[] = new IResource[results.size()];
            results.toArray(candidateResources);
            IResource toBeDeletedResources[] = EclipseResourceManager.computeMin(candidateResources);
            results.clear();
            CollectionUtils.addAll(results, toBeDeletedResources);
        }
        return results;
    }

    public static ImageDescriptor getImageDescriptor(INode model)
    {
        IResourceDelegate resourceDelegate = model.getResource();
        if(resourceDelegate != null)
            return getImageDescriptor(resourceDelegate.getName());
        else
            return null;
    }

    public static ImageDescriptor getImageDescriptor(String fileName)
    {
        return PlatformUI.getWorkbench().getEditorRegistry().getImageDescriptor(fileName);
    }

    public static ImageDescriptor getImageDescriptor(IFile file)
    {
        if(file == null)
            return null;
        else
            return PlatformUI.getWorkbench().getEditorRegistry().getImageDescriptor(file.getName());
    }

    public static IEditorPart openFile(IWorkbenchPage page, IFile file, String editorId)
    {
        boolean activate = OpenStrategy.activateOnOpen();
        try {
//        	IDE.openEditor(page, input, editorId);
        	if(editorId == null)
        		return IDE.openEditor(page, file, activate);
        	else
        		return IDE.openEditor(page, file, editorId, activate);
		} catch (PartInitException e) {
		}
		return null;
    }

    public static IEditorPart openFileInNewEditor(IWorkbenchPage page, IFile file, String editorId)
    {
        boolean activate = OpenStrategy.activateOnOpen();
		IEditorInput input = new FileEditorInput(file);
        try {
//        	IDE.openEditor(page, input, editorId);
        	if(editorId == null)
        		return IDE.openEditor(page, file, activate);
        	else
        	{
        		IEditorReference[] editors = page.findEditors(input, editorId, IWorkbenchPage.MATCH_INPUT);
        		for(int i=0; i<editors.length; i++)
        		{
        			if(editorId.equals(editors[i].getId()))
					{
        				page.bringToTop(editors[i].getEditor(false));
        				return editors[i].getEditor(false);
					}
        		}
        		IEditorPart editor = page.openEditor(input, editorId, true, IWorkbenchPage.MATCH_NONE);
        		
        		return editor;
        	}
		} catch (PartInitException e) {
			try {
				IEditorPart editor = IDE.openEditor(page, file, activate);
				if(editor != null)
					return editor;
			} catch (PartInitException e1) {
				e1.printStackTrace();
			}
		}
		return null;
    }

    public static IEditorPart openFile(IFile file)
    {
        return openFile(getWorkbenchPage(), file, null);
    }

    public static boolean closeEditor(IEditorReference editorReference, final boolean save)
    {
        if(editorReference != null)
        {
            final IEditorPart editor = editorReference.getEditor(false);
            if(editor == null)
            {
                return false;
            } else
            {
                Display display = editor.getSite().getShell().getDisplay();
                display.asyncExec(new Runnable() {

                    public void run()
                    {
                        editor.getSite().getPage().closeEditor(editor, save);
                    }
                });
                return true;
            }
        } else
        {
            return false;
        }
    }

    public static boolean closeEditors(IEditorReference editorReferences[], boolean save)
    {
        if(!ArrayUtils.isEmpty(editorReferences))
        {
            for(int i = 0; i < editorReferences.length; i++)
            {
                IEditorReference editorReference = editorReferences[i];
                closeEditor(editorReference, save);
            }

            return true;
        } else
        {
            return false;
        }
    }

    public static boolean resetEditors(IEditorReference editorReferences[], IFile file)
        throws PartInitException
    {
        if(!ArrayUtils.isEmpty(editorReferences))
        {
            for(int i = 0; i < editorReferences.length; i++)
            {
                IEditorReference editorReference = editorReferences[i];
                resetEditor(editorReference, file);
            }

            return true;
        } else
        {
            return false;
        }
    }

    public static boolean resetEditor(IEditorReference editorReference, IFile file)
        throws PartInitException
    {
        if(editorReference != null)
        {
            IEditorPart editor = editorReference.getEditor(false);
            if(editor == null)
            {
                return false;
            } else
            {
                org.eclipse.ui.IEditorSite editorSite = editor.getEditorSite();
                IEditorInput input = new FileEditorInput(file);
                editor.init(editorSite, input);
                return true;
            }
        } else
        {
            return false;
        }
    }

    public static IWorkbenchWindow getWorkbenchWindow()
    {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if(window == null)
        {
            IWorkbenchWindow windows[] = PlatformUI.getWorkbench().getWorkbenchWindows();
            if(!ArrayUtils.isEmpty(windows))
                window = windows[0];
        }
        return window;
    }

    public static IEditorPart getActiveEditor()
    {
        IWorkbenchPage page = getWorkbenchPage();
        if(page == null)
            return null;
        else
            return page.getActiveEditor();
    }

    public static IWorkbenchPage getWorkbenchPage()
    {
        IWorkbenchWindow window = getWorkbenchWindow();
        if(window == null)
            return null;
        IWorkbenchPage page = window.getActivePage();
        if(page == null)
        {
            IWorkbenchPage pages[] = window.getPages();
            if(!ArrayUtils.isEmpty(pages))
                page = pages[0];
        }
        return page;
    }

    public static Shell getShell()
    {
        IWorkbenchWindow window = getWorkbenchWindow();
        if(window == null)
            return getDisplay().getActiveShell();
        else
            return window.getShell();
    }

    public static IViewPart findView(String viewId)
    {
        IWorkbenchPage page = getWorkbenchPage();
        if(page != null)
            return page.findView(viewId);
        else
            return null;
    }

    public static Display getStandardDisplay()
    {
        Display display = Display.getCurrent();
        if(display == null)
            display = Display.getDefault();
        return display;
    }

    public static IViewPart findView(String viewId, boolean forceActive)
    {
        IWorkbenchPage page = getWorkbenchPage();
        if(page != null)
        {
            IViewPart viewPart = page.findView(viewId);
            if(viewPart == null || forceActive)
                try
                {
                    viewPart = page.showView(viewId);
                }
                catch(PartInitException _ex) { }
            return viewPart;
        } else
        {
            return null;
        }
    }

    public static IEditorReference[] getEditorReferences()
    {
        IWorkbenchPage page = getWorkbenchPage();
        if(page == null)
        {
            return new IEditorReference[0];
        } else
        {
            IEditorReference editorReferences[] = page.getEditorReferences();
            return editorReferences;
        }
    }

    public static IProject getActiveProject()
    {
        IWorkbenchPage page = getWorkbenchPage();
        if(page == null)
            return null;
        IWorkbenchPart part = page.getActivePart();
        Object adaptable = null;
        if(part instanceof IEditorPart)
        {
            IEditorPart editorPart = (IEditorPart)part;
            IEditorInput input = editorPart.getEditorInput();
            adaptable = input.getAdapter(IFile.class);
        } else
        {
            IViewPart viewPart = (IViewPart)part;
            adaptable = viewPart.getAdapter(IResource.class);
            if(adaptable == null)
            {
                ISelection selection = page.getSelection();
                if(isValidSelection(selection) && (selection instanceof IStructuredSelection))
                {
                    IStructuredSelection newSelection = (IStructuredSelection)selection;
                    Object objects[] = newSelection.toArray();
                    if(!ArrayUtils.isEmpty(objects))
                    {
                        for(int i = 0; i < objects.length; i++)
                        {
                            Object object = objects[i];
                            adaptable = AdapterUtil.getAdapter(object, IResource.class);
                            if(adaptable != null)
                                break;
                        }

                    }
                }
            }
        }
        if(adaptable == null)
        {
            IEditorPart editor = getActiveEditor();
            if(editor == null)
                return null;
            IEditorInput input = editor.getEditorInput();
            adaptable = input.getAdapter(IFile.class);
        }
        if(adaptable instanceof IResource)
        {
            IResource resource = (IResource)adaptable;
            IProject project = resource.getProject();
            return project;
        } else
        {
            return null;
        }
    }

    public static IProjectDelegate getEcProject()
    {
        IProjectDelegate projectDelegate = null;
        IProject eosProject = getActiveProject();
        if(eosProject != null)
        {
            projectDelegate = new EclipseProjectDelegate(eosProject.getProject());
        } else
        {
            EclipseProjectDelegate projectDelegates[] = EclipseResourceManager.getInstance().getProjects();
            if(!ArrayUtils.isEmpty(projectDelegates))
                projectDelegate = projectDelegates[0];
        }
        return projectDelegate;
    }

    public static IResource getResource(Object object)
    {
        if(object == null)
            return null;
        if(object instanceof IResource)
            return (IResource)object;
        object = AdapterUtil.getAdapter(object, IResource.class);
        if(object instanceof IResource)
            return (IResource)object;
        else
            return null;
    }

    public static boolean isValidSelection(ISelection selection)
    {
        return selection != null && !selection.isEmpty();
    }

    public static IWorkbenchPart getActiveWorkbenchPart()
    {
        IWorkbenchPage page = getWorkbenchPage();
        if(page == null)
        {
            return null;
        } else
        {
            IWorkbenchPart part = page.getActivePart();
            return part;
        }
    }

    public static IResource[] getResourceList(IStructuredSelection selection, boolean allowAdapter)
    {
        if(selection == null)
            return null;
        List selectionList = selection.toList();
        Set resultSet = new HashSet();
        for(int i = 0; i < selectionList.size(); i++)
        {
            Object object = selectionList.get(i);
            if(object instanceof IResource)
                resultSet.add(object);
            else
            if(allowAdapter)
            {
                object = AdapterUtil.getAdapter(object, IResource.class);
                if(object instanceof IResource)
                    resultSet.add(object);
            }
        }

        IResource results[] = new IResource[resultSet.size()];
        resultSet.toArray(results);
        return results;
    }

    public static Display getDisplay()
    {
        Display display = Display.getCurrent();
        if(display != null)
            return display;
        else
            return Display.getDefault();
    }

    public static void copyToClipboard(String content, Clipboard clipboard, String errorTitle, String errorDescription)
    {
        if(StringUtils.isEmpty(content))
            return;
        Object data[] = {
            content
        };
        Transfer dataTypes[] = {
            TextTransfer.getInstance()
        };
        try
        {
            clipboard.setContents(data, dataTypes);
        }
        catch(SWTError e)
        {
            if(e.code != 2002)
                throw e;
            if(MessageDialog.openQuestion(getShell(), errorTitle, errorDescription))
                clipboard.setContents(data, dataTypes);
        }
    }

    public static boolean isWebContent(IResource resource)
    {
        return getWebContentRelativePath(resource) != null;
    }

    public static String getWebContentRelativePath(IResource resource)
    {
        if(resource == null)
            return null;
        IProject project = resource.getProject();
        if(project == null)
            return null;
        IFolder folder = project.getFolder("WebRoot");
        if(resource.equals(folder))
            return "/";
        else
            return EclipseResourceManager.getRelativePath(folder, resource);
    }

    public static boolean checkCurrentEditor()
    {
        IEditorPart editor = getActiveEditor();
        if(editor == null)
            return true;
        IEditorInput editorInput = editor.getEditorInput();
        if(editorInput == null || !(editorInput instanceof FileEditorInput))
            return true;
        IFile file = ((FileEditorInput)editorInput).getFile();
        if(file == null)
            return false;
        if(file.isReadOnly())
        {
            IStatus status = ResourcesPlugin.getWorkspace().validateEdit(new IFile[] {
                file
            }, getShell());
            if(!status.isOK())
                return false;
        }
        return true;
    }

    private static IFile[] getAllFiles(Set resources)
    {
        Set files = new HashSet();
        IResource resource;
        for(Iterator iterator = resources.iterator(); iterator.hasNext(); files.addAll((Collection)Arrays.asList(getAllFiles(resource))))
            resource = (IResource)iterator.next();

        return (IFile[])files.toArray(new IFile[files.size()]);
    }

    private static IFile[] getAllFiles(IResource resource)
    {
        Set files = new HashSet();
        try
        {
            if(1 == resource.getType())
            {
                IFile file = (IFile)resource;
                files.add(file);
            } else
            if(resource instanceof IContainer)
            {
                IContainer container = (IContainer)resource;
                IResource children[] = container.members();
                IResource airesource[] = children;
                int i = 0;
                for(int j = airesource.length; i < j; i++)
                {
                    IResource child = airesource[i];
                    files.addAll((Collection)Arrays.asList(getAllFiles(child)));
                }

            }
        }
        catch(CoreException e)
        {
            e.printStackTrace();
        }
        return (IFile[])files.toArray(new IFile[files.size()]);
    }

    public static final String BUSINESS_ID = "com.jvisio.flowchart.diagram.part.FlowchartDiagramEditorID";
    public static final String EXTENSION_ID = "com.tansun.ui.workbench.editorDetached";
    public static final String EC_FORMEDITOR_ID = "com.tansun.form.designer.editors.FormEditor";
    public static final String EC_PROCESSEDITOR_ID = "com.tansun.workflow.designer.editor.DesignerEditor";
    public static final String EC_CONTROLLEREDITOR_ID = "com.tansun.controller.designer.editor.DesignerEditor";
    public static final String EC_BIZ_ID = "com.tansun.biz.designer.editor.DesignerEditor";
    public static final String EC_SPRINGEDITOR_ID = "com.tansun.service.designer.editor.DesignerEditor";
    public static final String EC_BPELEDITOR_ID = "org.eclipse.bpel.ui.bpeleditor";
    public static final String EC_BOEDITOR_ID = "com.tansun.easycare.editor.BOEditor";
    public static final String EC_EXTENSION_ID = "com.tansun.component.config.editors.ComponentExtensionXMLMultiPageEditor";

    private static final WorkbenchUtil instance = new WorkbenchUtil();

}