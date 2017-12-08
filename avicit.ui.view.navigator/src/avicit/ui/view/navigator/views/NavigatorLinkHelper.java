package avicit.ui.view.navigator.views;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.internal.ui.javaeditor.IClassFileEditorInput;
import org.eclipse.jdt.internal.ui.javaeditor.JarEntryEditorInput;
import org.eclipse.jdt.internal.ui.navigator.JavaFileLinkHelper;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.navigator.ILinkHelper;

import avicit.ui.common.util.StringUtil;
import avicit.ui.core.runtime.resource.EclipseFileDelegate;
import avicit.ui.core.runtime.resource.EclipseProjectDelegate;
import avicit.ui.core.runtime.resource.EclipseResourceManager;
import avicit.ui.core.runtime.resource.ISourceFolderDelegate;
import avicit.ui.runtime.core.INode;
import avicit.ui.runtime.core.cluster.function.ComponentNode;
import avicit.ui.view.module.ModelManagerImpl;
import avicit.ui.view.module.ProjectModelFactory;
import avicit.ui.view.navigator.builder.AvicitNature;

import com.tansun.runtime.resource.adapter.ComponentModelCreatorAdapter;
import com.tansun.runtime.resource.adapter.ComponentWorkbenchAdapter;
import com.tansun.runtime.resource.adapter.ProjectWorkbenchAdapter;


public class NavigatorLinkHelper extends JavaFileLinkHelper implements ILinkHelper {

	public NavigatorLinkHelper() {
	}

	public void activateEditor(IWorkbenchPage page, IStructuredSelection selection) {
        if(selection == null || selection.isEmpty())
            return;
        Object element = selection.getFirstElement();
        org.eclipse.ui.IEditorPart part = EditorUtility.isOpenInEditor(element);
        if(part != null)
        {
            page.bringToTop(part);
            if(element instanceof IJavaElement)
                EditorUtility.revealInEditor(part, (IJavaElement)element);
        }
	}

	public IStructuredSelection findSelection(IEditorInput input) {
		IFile file;
		try{
		if (input instanceof IClassFileEditorInput)
			((IClassFileEditorInput) input).getClassFile();
		else if (input instanceof IFileEditorInput) {
			file = ((IFileEditorInput) input).getFile();
			JavaCore.create(file);
		} else if (input instanceof JarEntryEditorInput)
			((JarEntryEditorInput) input).getStorage();
		file = (IFile) input.getAdapter(IFile.class);
		if (!EclipseResourceManager.isValidResource(file))
			return StructuredSelection.EMPTY;
		ILinkHelper linkHelper = findLinkHelper(file);
		if (linkHelper != null && linkHelper.getClass() != NavigatorLinkHelper.class)
			return linkHelper.findSelection(input);
		else
			return createLinkModel(file);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static IStructuredSelection createLinkModel(IFile file) {
		IProject project;
		if (!EclipseResourceManager.isValidResource(file))
			return StructuredSelection.EMPTY;
		project = file.getProject();
		if (!EclipseResourceManager.isValidProject(project))
			return StructuredSelection.EMPTY;
		if (!AvicitNature.isEcProject(project)) {
			return handleNoEcResource(file, project);
		} else {
			AvicitProjectNavigator navigator = AvicitProjectNavigator.getViewInstance();
			IStructuredSelection value = null;
			if (navigator != null)
				value = createModel(file);
			if (value != null && !value.isEmpty()) {
				return value;
			} else if ("java".equalsIgnoreCase(file.getFileExtension())) {
				org.eclipse.jdt.core.IJavaElement element = JavaCore.create(file);
				return new StructuredSelection(element);
			}
		}
		return new StructuredSelection(file);
	}

	static Set extensions = new HashSet();
	static{
		extensions.add("pagex.xml");
		extensions.add("spring.xml");
		extensions.add("hbm.xml");
		extensions.add("js");
	}
	
	private static IStructuredSelection handleNoEcResource(IFile file, IProject project) {
		return new StructuredSelection(file);
	}

	private static IStructuredSelection createModel(IFile file) {
		EclipseFileDelegate fileDelegate = new EclipseFileDelegate(file);
		IProject project = file.getProject();
		Object model = ModelManagerImpl.getInstance().getPool().getModel(new EclipseProjectDelegate(project), ProjectModelFactory.TYPE, true, null);
        Object[] srcs = ProjectWorkbenchAdapter.getInstance().getChildren(model);
		ISourceFolderDelegate folderDelegate = fileDelegate.getSourceFolder();
		for(int i=0; i<srcs.length; i++)
		{
			if(srcs[i] instanceof ComponentNode)
			{
				if(((ComponentNode)srcs[i]).getResource().equals(folderDelegate))
				{
					Object[] coms = ComponentWorkbenchAdapter.getInstance().getChildren((ComponentNode)srcs[i]);
					for(int j=0; j<coms.length; j++)
					{
//						if(coms[j] instanceof ControllerNode)
						{
//							ControllerWorkbenchAdapter.getInstance().getChildren(coms[j]);
							INode model1 = ComponentModelCreatorAdapter.getInstance().getOrCreateNode(fileDelegate, true);
//							IModelFactory mf = ModelManagerImpl.getInstance().getModelFactory(fileDelegate, null);
//							INode model1 = null;
//							if(mf != null)
//							{
//								model1 = (INode) ModelManagerImpl.getInstance().getPool().getModel(fileDelegate, mf.getType(), true, null);
//								if(model1 == null && mf instanceof IModelCreator)
//									model1 = ((IModelCreator)mf).getOrCreateNode(fileDelegate, true);
//							}
							if (model1 == null)
								return StructuredSelection.EMPTY;
							else
								return new StructuredSelection(model1);
						}
					}
				}
			}
		}
		return StructuredSelection.EMPTY;
		
	}

	public ILinkHelper findLinkHelper(IFile file) {
		if (file == null)
			return null;
		String fileName = file.getName();
		for (Iterator iterator = linkHelpers.keySet().iterator(); iterator.hasNext();) {
			String wildName = (String) iterator.next();
			if (StringUtil.isWildcardMatch(fileName, wildName, false))
				return (ILinkHelper) linkHelpers.get(wildName);
		}

		return null;
	}

	private final Map linkHelpers = new HashMap();

	public void setLinkHelper(String wildName, ILinkHelper linkHellper) {
		linkHelpers.put(wildName, linkHellper);
	}

	public void removeLinkHelper(String wildName) {
		linkHelpers.remove(wildName);
	}

}