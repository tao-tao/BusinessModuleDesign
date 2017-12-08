package avicit.ui.core.runtime.resource;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.osgi.framework.Bundle;

import avicit.ui.common.util.AdapterUtil;
import avicit.ui.common.util.CollectionUtil;
import avicit.ui.common.util.EclipseDirectoryExistValidator;
import avicit.ui.common.util.EclipseFileExistValidator;
import avicit.ui.common.util.EclipseProjectExistValidator;
import avicit.ui.common.util.StringUtil;
import avicit.ui.view.exception.ExceptionUtil;

public class EclipseResourceManager {

    private EclipseResourceManager()
    {
    }

    public static EclipseResourceManager getInstance()
    {
        return instance;
    }

    public IFolderDelegate getParent(EclipseResourceDelegate resourceDelegate)
    {
        IResource resource = resourceDelegate.getResource();
        IPath path = resource.getFullPath();
        int segments = path.segmentCount();
        if(segments == 2)
            return getProject(resourceDelegate);
//        if(segments == 1)
//        {
//            return RuntimeManager.getRoot();
//        } else
        {
            Assert.isLegal(segments > 2, path.toString());
            return new EclipseFolderDelegate(resource.getParent());
        }
    }

    public EclipseProjectDelegate getProject(EclipseResourceDelegate resourceDelegate)
    {
        return new EclipseProjectDelegate(resourceDelegate.getResource().getProject());
    }

    public EclipseProjectDelegate getProject(String name)
    {
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
        return new EclipseProjectDelegate(project);
    }

    public EclipseProjectDelegate[] getProjects()
    {
        IProject projects[] = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        EclipseProjectDelegate delegates[] = new EclipseProjectDelegate[projects.length];
        for(int i = 0; i < delegates.length; i++)
            delegates[i] = new EclipseProjectDelegate(projects[i]);

        return delegates;
    }

    public EclipseResourceDelegate[] getChildren(EclipseFolderDelegate folderDelegate)
        throws CoreException
    {
        if(!folderDelegate.getEclipseFolder().isAccessible())
            return new EclipseResourceDelegate[0];
        IResource resources[] = folderDelegate.getEclipseFolder().members();
        EclipseResourceDelegate delegates[] = new EclipseResourceDelegate[resources.length];
        for(int i = 0; i < delegates.length; i++)
            if(resources[i].getType() == 1)
                delegates[i] = new EclipseFileDelegate((IFile)resources[i]);
            else
            if(resources[i].getType() == 2)
                delegates[i] = new EclipseFolderDelegate((IFolder)resources[i]);

        return delegates;
    }

    public EclipseFolderDelegate getFolder(EclipseFolderDelegate folderDelegate, String path)
    {
        Path folderPath = new Path(path);
        if(folderPath.segmentCount() == 1 && folderDelegate.getEclipseFolder().getParent() == null)
        {
            return getProject(path);
        } else
        {
            IFolder folder = folderDelegate.getEclipseFolder().getFolder(folderPath);
            return new EclipseFolderDelegate(folder);
        }
    }

    public EclipseFileDelegate getFile(EclipseFolderDelegate folderDelegate, String path)
    {
        IFile file = folderDelegate.getEclipseFolder().getFile(new Path(path));
        return new EclipseFileDelegate(file);
    }

    public static EclipseSourceFolderDelegate getSourceFolder(EclipseResourceDelegate resourceDelegate)
    {
        IResource resource;
        IProject project;
        resource = resourceDelegate.getResource();
        project = resource.getProject();
        if(!isJavaProject(project))
            return null;
        IFolder folder = getSourceFolder(resource);
        if(folder != null)
            return new EclipseSourceFolderDelegate(folder);
        else
            return null;
    }

    public static boolean isValidResource(IResource resource)
    {
        return resource != null && resource.isAccessible();
    }

    public static boolean isValidProject(IProject project)
    {
        return project != null && project.isOpen() && project.isAccessible();
    }

    public static IFolder getSourceFolder(IResource resource)
    {
        if(resource == null)
            return null;
        IPath path;
        IProject project;
        path = resource.getFullPath();
        project = resource.getProject();
        if(!isValidProject(project))
            return null;
        if(!isJavaProject(project))
            return null;
        IFolder srcFolders[];
		try {
			srcFolders = getSourceFolders(project);
	        if(ArrayUtils.isEmpty(srcFolders))
	            return null;
	        for(int i=0; i<srcFolders.length; i++)
	        {
		        IFolder folder = srcFolders[i];
		        if(folder.getFullPath().isPrefixOf(path))
		            return folder;
	        }
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }

    public static IFolder getSourceFolderByOutputResource(IResource outputResource)
    {
        IProject project = outputResource.getProject();
        if(!isJavaProject(project))
            return null;
        
        IJavaProject javaProject;
        IWorkspaceRoot workspaceRoot;
        javaProject = JavaCore.create(project);
        workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        IClasspathEntry entries[];
        try {
			entries = javaProject.getRawClasspath();

	        for(int i=0; i<entries.length; i++)
	        {
		        
		        IClasspathEntry entry;
		        entry = entries[i];
		        if(3 != entry.getEntryKind())
		            continue; /* Loop/switch isn't completed */
		        IFolder sourceFolder;
		        IFolder currentOutputFolder;
		        sourceFolder = workspaceRoot.getFolder(entry.getPath());
		        currentOutputFolder = null;
		        if(entry.getOutputLocation() == null)
		            currentOutputFolder = workspaceRoot.getFolder(javaProject.getOutputLocation());
		        else
		            currentOutputFolder = workspaceRoot.getFolder(entry.getOutputLocation());
		        if(isPrefixOf(currentOutputFolder, outputResource))
		            return sourceFolder;
	        }
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
        
        return null;
    }

    public EclipseSourceFolderDelegate[] getSourceFolders(EclipseProjectDelegate projectDelegate)
    {
        IProject project = projectDelegate.getEclipseProject();
        if(!isJavaProject(project))
    		return new EclipseSourceFolderDelegate[0];
        
        EclipseSourceFolderDelegate folders[];
        try {
        	IFolder srcFolders[];
    		srcFolders = getSourceFolders(project);
	        folders = new EclipseSourceFolderDelegate[srcFolders.length];
	        for(int i = 0; i < srcFolders.length; i++)
	        {
	            IFolder folder = srcFolders[i];
	            folders[i] = new EclipseSourceFolderDelegate(folder);
	        }
	        return folders;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new EclipseSourceFolderDelegate[0];
    }

    public String getSourceRelativePath(EclipseResourceDelegate resourceDelegate)
    {
        return getSourceRelativePath(resourceDelegate.getResource());
    }

    public static final String getSourceRelativePath(IResource resource)
    {
        IFolder sourceFolder = getSourceFolder(resource);
        return getRelativePath(sourceFolder, resource);
    }

    public static final String getOutputRelativePath(IResource resource)
    {
        IFolder outputFolder = getOutputFolder(resource);
        return getRelativePath(outputFolder, resource);
    }

    public static final String getRelativePath(IContainer parentFolder, IResource resource)
    {
        if(parentFolder == null || resource == null)
            return null;
        IPath parentPath = parentFolder.getFullPath();
        IPath path = resource.getFullPath();
        if(parentPath.isPrefixOf(path))
        {
            int count = parentPath.segmentCount();
            return path.removeFirstSegments(count).toString();
        } else
        {
            return null;
        }
    }

    public static final boolean isPrefixOf(IContainer parentFolder, IResource resource)
    {
        if(parentFolder == null || resource == null)
        {
            return false;
        } else
        {
            IPath parentPath = parentFolder.getFullPath();
            IPath path = resource.getFullPath();
            return parentPath.isPrefixOf(path);
        }
    }

    public EclipseOutputFolderDelegate getOutputFolder(EclipseSourceFolderDelegate folderDelegate)
    {
        if(folderDelegate == null)
            return null;
        IContainer container = folderDelegate.getEclipseFolder();
        IFolder folder = getOutputFolder(container);
        if(folder == null)
            return null;
        else
            return new EclipseOutputFolderDelegate(folder);
    }

    public static String newFolderName(String r_DefaultFolderName)
    {
        if(StringUtils.isEmpty(r_DefaultFolderName))
            r_DefaultFolderName = "newFolder";
        EclipseDirectoryExistValidator t_Validator = new EclipseDirectoryExistValidator();
        String t_FileName = r_DefaultFolderName;
        for(int t_Count = 1; t_Validator.validate(t_FileName, null, null); t_Count++)
        {
            t_FileName = (new StringBuilder(String.valueOf(FilenameUtils.getFullPath(r_DefaultFolderName)))).append(File.separator).append(FilenameUtils.getBaseName(r_DefaultFolderName)).append("_").append(t_Count).toString();
            if(FilenameUtils.getExtension(t_FileName) != null)
                t_FileName = (new StringBuilder(String.valueOf(t_FileName))).append(".").append(FilenameUtils.getExtension(r_DefaultFolderName)).toString();
        }

        return t_FileName;
    }

    public static String newFileName(String r_DefaultFileName)
    {
        if(StringUtils.isEmpty(r_DefaultFileName))
            r_DefaultFileName = "newFile";
        EclipseFileExistValidator t_Validator = new EclipseFileExistValidator();
        String t_FileName = r_DefaultFileName;
        for(int t_Count = 1; t_Validator.validate(t_FileName, null, null); t_Count++)
        {
            t_FileName = (new StringBuilder(String.valueOf(FilenameUtils.getFullPath(r_DefaultFileName)))).append(File.separator).append(FilenameUtils.getBaseName(r_DefaultFileName)).append("_").append(t_Count).toString();
            if(FilenameUtils.getExtension(t_FileName) != null)
                t_FileName = (new StringBuilder(String.valueOf(t_FileName))).append(".").append(FilenameUtils.getExtension(r_DefaultFileName)).toString();
        }

        return t_FileName;
    }

    public static String newProjectName(String r_DefaultProjectName)
    {
        if(StringUtils.isEmpty(r_DefaultProjectName))
            r_DefaultProjectName = "project";
        EclipseProjectExistValidator t_Validator = new EclipseProjectExistValidator();
        String t_ProjectName = r_DefaultProjectName;
        for(int t_Count = 1; t_Validator.validate(t_ProjectName, null, null); t_Count++)
            t_ProjectName = (new StringBuilder(String.valueOf(r_DefaultProjectName))).append(t_Count).toString();

        return t_ProjectName;
    }

    private static IFolder getOutputFolder(IContainer container)
    {
        IProject project = container.getProject();
        if(!isJavaProject(project))
            return null;
        IJavaProject javaProject = JavaCore.create(project);
        IClasspathEntry entries[];
        try {
			entries = javaProject.getRawClasspath();
	        for(int i=0;i < entries.length; i++){
		        IFolder folder;
		        IClasspathEntry entry = entries[i];
		        if(!entry.getPath().equals(container.getFullPath()))
		            continue; /* Loop/switch isn't completed */
		        IPath outputLocation = entry.getOutputLocation();
		        if(outputLocation == null)
		            continue; /* Loop/switch isn't completed */
		        folder = project.getWorkspace().getRoot().getFolder(outputLocation);
		        
		        return folder;
	        }
	        IFolder folder = project.getWorkspace().getRoot().getFolder(javaProject.getOutputLocation());
	        return folder;
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }

    public static IFolder getOutputFolder(IResource resource)
    {
        IFolder sourceFolder = getSourceFolder(resource);
        if(sourceFolder == null)
            sourceFolder = getSourceFolderByOutputResource(resource);
        if(sourceFolder == null)
            return null;
        else
            return getOutputFolder(((IContainer) (sourceFolder)));
    }

    public static IFolder getCorrespondOutputFolder(IFolder folder)
    {
        IFolder outputFolder = getOutputFolder(folder);
        if(outputFolder == null)
            return null;
        String path = getSourceRelativePath(folder);
        if(path != null)
            return outputFolder.getFolder(path);
        else
            return null;
    }

    public static IFolder[] getSourceFolders(IProject project)
        throws CoreException
    {
        return getSourceFolders(project, false);
    }

    public static IFolder[] getSourceFolders(IProject project, boolean checkExist)
        throws /*CoreException, */JavaModelException
    {
        if(!isJavaProject(project))
            return null;
        IJavaProject javaProject = JavaCore.create(project);
        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        List folderList = new ArrayList(20);
        IClasspathEntry entries[] = javaProject.getRawClasspath();
        for(int i = 0; i < entries.length; i++)
        {
            IClasspathEntry entry = entries[i];
            if(3 == entry.getEntryKind())
                try
                {
                    IFolder folder = workspaceRoot.getFolder(entry.getPath());
                    if(!checkExist || folder.exists())
                        folderList.add(folder);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
        }

        IFolder folders[] = new IFolder[folderList.size()];
        folderList.toArray(folders);
        return folders;
    }

    public static void removeSourceFolder(IFolder folder)
        throws JavaModelException, CoreException
    {
        IProject project = folder.getProject();
        if(!isJavaProject(project))
            return;
        IJavaProject javaProject = JavaCore.create(project);
        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        Set set = new HashSet(20);
        IClasspathEntry entries[] = javaProject.getRawClasspath();
        CollectionUtils.addAll(set, entries);
        for(int i = 0; i < entries.length; i++)
        {
            IClasspathEntry entry = entries[i];
            if(3 == entry.getEntryKind())
            {
                IFolder sourceFolder = workspaceRoot.getFolder(entry.getPath());
                if(sourceFolder.equals(folder))
                    set.remove(entry);
            }
        }

        entries = new IClasspathEntry[set.size()];
        set.toArray(entries);
        javaProject.setRawClasspath(entries, null);
    }

    public static IFolder[] getOutputFolders(IProject project)
        throws /*CoreException, */JavaModelException
    {
        IJavaProject javaProject = JavaCore.create(project);
        if(!isJavaProject(project))
            return EMPTY_FOLDERS;
        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        List folderList = new ArrayList(20);
        IClasspathEntry entries[] = javaProject.getRawClasspath();
        for(int i = 0; i < entries.length; i++)
        {
            IClasspathEntry entry = entries[i];
            if(3 == entry.getEntryKind() && entry.getOutputLocation() != null)
            {
                IFolder folder = workspaceRoot.getFolder(entry.getOutputLocation());
                if(folder != null && folder.exists())
                    folderList.add(folder);
            }
        }

        try
        {
            IFolder folder = workspaceRoot.getFolder(javaProject.getOutputLocation());
            if(folder != null && folder.exists())
                folderList.add(folder);
        }
        catch(Exception _ex) { }
        IFolder folders[] = new IFolder[folderList.size()];
        folderList.toArray(folders);
        return folders;
    }

    public static boolean isSourceFolder(IFolder folder)
    {
        IProject project = folder.getProject();
        if(!isJavaProject(project))
            return false;
        try {
            IFolder srcFolders[];
			srcFolders = getSourceFolders(project);
		    for(int i=0; i< srcFolders.length; i++)
		    {
		        if(srcFolders[i].equals(folder))
		            return true;
		    }
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
    }

    public static boolean isOutputFolder(IFolder folder)
    {
        IProject project = folder.getProject();
        if(!isJavaProject(project))
            return false;
        IFolder outputFolders[];
        try {
			outputFolders = getOutputFolders(project);

	        for(int i=0;i < outputFolders.length;i++)
	        {
		        if(outputFolders[i].equals(folder))
		            return true;
	        }
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
    }

    public static List getAffectedResources(IResourceDelta delta, int kind, int memberFlags)
    {
        if(delta == null)
            return new ArrayList();
        List resourceList = new ArrayList();
        List deltaList = new ArrayList();
        deltaList.add(delta);
        for(; deltaList.size() > 0; deltaList.remove(0))
        {
            IResourceDelta resourceDelta = (IResourceDelta)deltaList.get(0);
            IResourceDelta deltas[] = resourceDelta.getAffectedChildren(kind, memberFlags);
            for(int i = 0; i < deltas.length; i++)
            {
                IResourceDelta childResourceDelta = deltas[i];
                deltaList.add(childResourceDelta);
                resourceList.add(childResourceDelta.getResource());
            }

        }

        return resourceList;
    }

    public static List getAffectedResources(IResourceDelta delta, int kind)
    {
        return getAffectedResources(delta, kind, 0);
    }

    public static List getAffectedResources(IResourceDelta delta)
    {
        return getAffectedResources(delta, 7, 0);
    }

    public static void forceCreateFolder(IFolder folder)
    {
        if(folder == null || folder.exists())
            return;
        try
        {
            if(folder != null && !folder.exists())
                folder.refreshLocal(0, null);
        }
        catch(CoreException _ex) { }

        if(folder == null || folder.exists())
            return;
        IProject project = folder.getProject();
        if(!isValidProject(project))
            return;
        List folders = new ArrayList();
        for(IContainer currentContainer = folder; !currentContainer.exists(); currentContainer = currentContainer.getParent())
            folders.add(currentContainer);

        Collections.reverse(folders);
        for(int i = 0; i < folders.size(); i++)
        {
            IContainer container = (IContainer)folders.get(i);
            if(!container.exists())
                if(container instanceof IFolder)
                    try
                    {
                        ((IFolder)container).create(true, true, null);
                    }
                    catch(CoreException e)
                    {
                        throw new RuntimeException(e);
                    }
                else
                if(container instanceof IProject)
                    try
                    {
                        ((IProject)container).create(null);
                    }
                    catch(CoreException e)
                    {
                        throw new RuntimeException(e);
                    }
                else
                    throw new IllegalArgumentException((new StringBuilder("please check,the folder of  '")).append(container.getFullPath()).append("' is not valid").toString());
        }
    }

    public static IFileDelegate[] findFiles(IProjectDelegate project, String paths[])
    {
        if(paths == null)
            return IResourceDelegate.NO_FILES;
        List list = new ArrayList();
        for(int i = 0; i < paths.length; i++)
        {
            IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(paths[i]));
            if(file.exists())
                list.add(new EclipseFileDelegate(file));
        }

        IFileDelegate fileDelegates[] = new IFileDelegate[list.size()];
        list.toArray(fileDelegates);
        return fileDelegates;
    }
/*
    public static IFileDelegate[] findFiles(IContribution contribution, String paths[])
    {
        if(paths == null)
            return IResourceDelegate.NO_FILES;
        List list = new ArrayList();
        for(int i = 0; i < paths.length; i++)
        {
            IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(paths[i]));
            if(file.exists())
                list.add(new EclipseFileDelegate(file));
        }

        IFileDelegate fileDelegates[] = new IFileDelegate[list.size()];
        list.toArray(fileDelegates);
        return fileDelegates;
    }
*/
    public static IResource getResource(Object object)
    {
        if(object == null)
            return null;
        if(object instanceof IResource)
            return (IResource)object;
        Object adapter = AdapterUtil.getAdapter(object, IResource.class);
        if(adapter instanceof IResource)
            return (IResource)adapter;
        else
            return null;
    }

    public static boolean hasResource(IContainer folder, String name)
        throws CoreException
    {
        if(name == null)
            return hasResource(folder, ((String []) (null)));
        else
            return hasResource(folder, new String[] {
                name
            });
    }

    public static boolean hasResource(IContainer folder, String names[])
        throws CoreException
    {
        return hasResource(folder, names, 3);
    }

    public static boolean hasFile(IContainer folder, String fileName)
        throws CoreException
    {
        if(fileName == null)
            return hasFile(folder, ((String []) (null)));
        else
            return hasFile(folder, new String[] {
                fileName
            });
    }

    public static IResource[] getResources(IContainer folder, String names[])
        throws CoreException
    {
        List fileList = getResources(folder, names, 3);
        IResource files[] = new IResource[fileList.size()];
        fileList.toArray(files);
        return files;
    }

    public static IResource[] getResources(IContainer folder, String name)
        throws CoreException
    {
        if(name == null)
            return getResources(folder, ((String []) (null)));
        else
            return getResources(folder, new String[] {
                name
            });
    }

    public static IFile[] getFiles(IContainer folder, String fileNames[])
        throws CoreException
    {
        List fileList = getResources(folder, fileNames, 1);
        IFile files[] = new IFile[fileList.size()];
        fileList.toArray(files);
        return files;
    }

    public static boolean hasFile(IContainer folder, String fileNames[])
        throws CoreException
    {
        return hasResource(folder, fileNames, 1);
    }

    public static boolean hasFolder(IContainer folder, String folderName)
        throws CoreException
    {
        if(folderName == null)
            return hasFolder(folder, ((String []) (null)));
        else
            return hasFolder(folder, new String[] {
                folderName
            });
    }

    private static boolean hasResource(IContainer folder, String names[], int type)
        throws CoreException
    {
        if(!isValidResource(folder))
            return false;
        IResource resources[] = folder.members();
        for(int i = 0; i < resources.length; i++)
        {
            IResource resource = resources[i];
            if((resource.getType() & type) != 0 && (ArrayUtils.isEmpty(names) || StringUtil.isWildcardMatchOne(resource.getName(), names, false)))
                return true;
        }

        return false;
    }

    public static IFolder[] getFolders(IContainer folder, String folderName)
        throws CoreException
    {
        if(folderName == null)
            return getFolders(folder, ((String []) (null)));
        else
            return getFolders(folder, new String[] {
                folderName
            });
    }

    public static IFile[] getFiles(IContainer folder, String fileName)
        throws CoreException
    {
        if(fileName == null)
            return getFiles(folder, ((String []) (null)));
        else
            return getFiles(folder, new String[] {
                fileName
            });
    }

    public static IFolder[] getFolders(IContainer folder, String folderNames[])
        throws CoreException
    {
        List folderList = getResources(folder, folderNames, 2);
        IFolder folders[] = new IFolder[folderList.size()];
        folderList.toArray(folders);
        return folders;
    }

    private static List getResources(IContainer folder, String names[], int type)
        throws CoreException
    {
        List resourceList = new ArrayList();
        if(!isValidResource(folder))
            return resourceList;
        IResource resources[] = folder.members();
        for(int i = 0; i < resources.length; i++)
        {
            IResource resource = resources[i];
            if((resource.getType() & type) != 0 && (ArrayUtils.isEmpty(names) || StringUtil.isWildcardMatchOne(resource.getName(), names, false)))
                resourceList.add(resource);
        }

        return resourceList;
    }

    public static boolean hasFolder(IContainer folder, String folderNames[])
        throws CoreException
    {
        return hasResource(folder, folderNames, 2);
    }

    public static boolean isEmpty(IContainer folder)
        throws CoreException
    {
        if(isValidResource(folder))
        {
            IResource resources[] = folder.members();
            return ArrayUtils.isEmpty(resources);
        } else
        {
            return true;
        }
    }

    public static IFile[] getFiles(String fileNames[])
    {
        if(ArrayUtils.isEmpty(fileNames))
            return new IFile[0];
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IFile files[] = new IFile[fileNames.length];
        for(int i = 0; i < fileNames.length; i++)
        {
            String fileName = fileNames[i];
            files[i] = root.getFile(new Path(fileName));
        }

        return files;
    }

    public static boolean hasNature(IProject project, String nature)
//        throws CoreException
    {
        Set natureSet = internalGetNatures(project);
        return natureSet.contains(nature);
    }

    public static String[] getNatures(IProject project)
        throws CoreException
    {
        Set natureSet = internalGetNatures(project);
        String natures[] = new String[natureSet.size()];
        natureSet.toArray(natures);
        return natures;
    }

    private static Set internalGetNatures(IProject project)
//        throws CoreException
    {
        Set natureSet = new TreeSet();
        if(!isValidProject(project))
        {
            return natureSet;
        } else
        {
            IProjectDescription description = null;
			try {
				description = project.getDescription();
			} catch (CoreException e) {
				e.printStackTrace();
			}
            CollectionUtil.addAllQuietly(natureSet, description.getNatureIds());
            return natureSet;
        }
    }

    public static boolean isJavaProject(IProject project)
//        throws CoreException
    {
        return hasNature(project, "org.eclipse.jdt.core.javanature");
    }

    public static void copy(final Bundle bundle, final String bundlePath, final IContainer container, IProgressMonitor monitor)
    {
//
//    	if(!isValidResource(container))
//            return;
//        IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
//
//            public void run(IProgressMonitor r_InnerMonitor)
//                throws CoreException
//            {
//                Enumeration enumeration;
//                String newBundlePath;
//                enumeration = bundle.findEntries(bundlePath, "*", true);
//                if(enumeration == null)
//                    return;
//                newBundlePath = FilenameUtil.normalizeInUnixStyle(bundlePath);
//                
//                URL url;
//                java.io.InputStream inputStream;
//                String filePath;
//                url = (URL)enumeration.nextElement();
//                if(url.getPath().endsWith("/"))
//                    continue; /* Loop/switch isn't completed */
//                inputStream = null;
//                filePath = StringUtils.removeStart(url.getPath(), newBundlePath);
//                try
//                {
//                    inputStream = url.openStream();
//                    IFile file = container.getFile(new Path(filePath));
//                    IContainer parentContainer = file.getParent();
//                    if(parentContainer.getType() == 2)
//                    {
//                        EclipseResourceManager.forceCreateFolder((IFolder)parentContainer);
//                        if(file.exists())
//                            file.setContents(inputStream, true, true, r_InnerMonitor);
//                        else
//                            file.create(inputStream, true, r_InnerMonitor);
//                    }
//                }
//                catch(Exception e)
//                {
//                    ExceptionUtil.getInstance().logException(e);
//                }
//                IOUtils.closeQuietly(inputStream);
//                throw exception;
//                IOUtils.closeQuietly(inputStream);
//                continue; /* Loop/switch isn't completed */
//                IOUtils.closeQuietly(inputStream);
//                if(enumeration.hasMoreElements())
//            }
//
//            private final Bundle val$bundle;
//            private final String val$bundlePath;
//            private final IContainer val$container;
//
//            
//            {
//                bundle = bundle1;
//                bundlePath = s;
//                container = icontainer;
//                super();
//            }
//        };
//        try
//        {
//            ResourcesPlugin.getWorkspace().run(runnable, monitor);
//        }
//        catch(CoreException e)
//        {
//            e.printStackTrace();
//        }
    }

    public static boolean isSynchronized(IResource resource)
    {
        if(!isValidResource(resource))
            return false;
        else
            return resource.isSynchronized(1);
    }

    public static boolean hasAncestor(IResource resource, Collection ancestors)
    {
        if(resource == null || CollectionUtils.isEmpty(ancestors))
            return false;
        IPath resourcePath = resource.getFullPath();
        for(Iterator iterator = ancestors.iterator(); iterator.hasNext();)
        {
            IResource newResource = (IResource)iterator.next();
            if(newResource != null && newResource.getFullPath().isPrefixOf(resourcePath))
                return true;
        }

        return false;
    }

    public static IContainer[] compute(IContainer containers[])
    {
        Set containerSet = new HashSet();
        CollectionUtil.addAllQuietly(containerSet, containers);
        for(int i = 0; i < containers.length; i++)
        {
            IContainer container = containers[i];
            if(container != null)
            {
                IPath path = container.getFullPath();
                boolean founded = false;
                for(int j = 0; j < containers.length; j++)
                {
                    if(i == j)
                        continue;
                    IContainer targetContainer = containers[j];
                    if(targetContainer == null)
                        continue;
                    IPath targetPath = targetContainer.getFullPath();
                    if(!targetPath.isPrefixOf(path) && !targetPath.equals(path))
                        continue;
                    founded = true;
                    break;
                }

                if(founded)
                    containerSet.remove(container);
            }
        }

        IContainer results[] = new IContainer[containerSet.size()];
        containerSet.toArray(results);
        return results;
    }

    public static boolean isSubResource(Collection resources, IResource resource)
    {
        if(CollectionUtils.isEmpty(resources) || resource == null)
            return false;
        IPath filePath = resource.getFullPath();
        for(Iterator iterator = resources.iterator(); iterator.hasNext();)
        {
            IResource newResource = (IResource)iterator.next();
            if(newResource instanceof IContainer)
            {
                IContainer container = (IContainer)newResource;
                IPath folderPath = container.getFullPath();
                if(folderPath.isPrefixOf(filePath) || folderPath.equals(filePath))
                    return true;
            }
        }

        return false;
    }

    public static IResource[] computeOut(IContainer containers[], IResource resources[])
    {
        Set fileSet = new HashSet();
        CollectionUtil.addAllQuietly(fileSet, resources);
        for(int i = 0; i < resources.length; i++)
        {
            IResource resource = resources[i];
            boolean founded = false;
            if(resource != null)
            {
                IPath resourcePath = resource.getFullPath();
                for(int j = 0; j < containers.length; j++)
                {
                    IContainer folder = containers[j];
                    if(folder != null)
                    {
                        IPath folderPath = folder.getFullPath();
                        if(folderPath.isPrefixOf(resourcePath) || folderPath.equals(resourcePath))
                            founded = true;
                    }
                }

                if(founded)
                    fileSet.remove(resource);
            }
        }

        IFile results[] = new IFile[fileSet.size()];
        fileSet.toArray(results);
        return results;
    }

    public static IResource[] computeMin(IResource resources[])
    {
        Set resourceSet = new HashSet();
        CollectionUtil.addAllQuietly(resourceSet, resources);
        if(!ArrayUtils.isEmpty(resources))
        {
            for(int i = 0; i < resources.length; i++)
            {
                IResource resource = resources[i];
                if(resource != null)
                {
                    IPath path = resource.getFullPath();
                    boolean founded = false;
                    for(int j = 0; j < resources.length; j++)
                        if(i != j)
                        {
                            IResource targetResource = resources[j];
                            if(targetResource != null)
                            {
                                IPath targetPath = targetResource.getFullPath();
                                if(targetPath.isPrefixOf(path) || targetPath.equals(path))
                                    founded = true;
                            }
                        }

                    if(founded)
                        resourceSet.remove(resource);
                }
            }

        }
        IResource results[] = new IResource[resourceSet.size()];
        resourceSet.toArray(results);
        return results;
    }

    public static void refreshLocal(String path, IProgressMonitor monitor)
    {
        if(path != null && path.length() > 0)
        {
            IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path((new StringBuilder(String.valueOf(File.separator))).append(path).toString()));
            IContainer parentContainer = null;
            if(isValidResource(file))
            {
                parentContainer = file.getParent();
            } else
            {
                IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path((new StringBuilder(String.valueOf(File.separator))).append(path).toString()));
                if(isValidResource(folder))
                    parentContainer = folder.getParent();
            }
            if(isValidResource(parentContainer))
                try
                {
                    parentContainer.refreshLocal(2, monitor);
                }
                catch(CoreException e)
                {
                    ExceptionUtil.getInstance().logException(e);
                }
        }
    }
/*
    public static boolean isAcceptable(IResource resource, IModelFactory modelFactory)
    {
        if(!isValidResource(resource))
            return false;
        if(resource instanceof IContainer)
            return true;
        if(modelFactory == null)
        {
            return true;
        } else
        {
            String extensions[] = modelFactory.getExtensionNames();
            return StringUtil.isWildcardMatchOne(resource.getName(), extensions, false);
        }
    }
*/
    public static boolean hasDotInSourceFolder(IFolder folder)
    {
        String sourcePath = getSourceRelativePath(folder);
        if(sourcePath == null)
            return false;
        else
            return StringUtils.contains(sourcePath, ".");
    }

    public static boolean isFileCanBeCompile(IFile file)
    {
        String sourcePath = getSourceRelativePath(file.getParent());
        if(sourcePath == null)
            return false;
        else
            return !StringUtils.contains(sourcePath, ".");
    }

    public static File getOSFile(IResource resource)
    {
        if(resource == null)
        {
            return null;
        } else
        {
            File file = resource.getLocation().toFile();
            return file;
        }
    }

    public static void copyFile(IFile eclipseFile, File targetFile)
        throws IOException
    {
        if(isValidResource(eclipseFile))
        {
            File sourceFile = eclipseFile.getLocation().toFile();
            FileUtils.copyFile(sourceFile, targetFile);
        }
    }

    public static void copyFileToFolder(IFile eclipseFile, File targetFoder)
        throws IOException
    {
        if(isValidResource(eclipseFile))
        {
            File sourceFile = eclipseFile.getLocation().toFile();
            FileUtils.copyFileToDirectory(sourceFile, targetFoder);
        }
    }

    public static IProject getProject(Object object)
    {
        IResource resource = (IResource)AdapterUtil.getAdapter(object, IResource.class);
        if(resource == null)
            return null;
        else
            return resource.getProject();
    }

    public static IPath[] getPaths(File files[])
    {
        if(ArrayUtils.isEmpty(files))
            return EMPTY_PATHS;
        IPath paths[] = new IPath[files.length];
        for(int i = 0; i < files.length; i++)
            paths[i] = new Path(files[i].getAbsolutePath());

        return paths;
    }

    public static IPath[] getPaths(IResource resources[])
    {
        if(ArrayUtils.isEmpty(resources))
            return EMPTY_PATHS;
        IPath paths[] = new IPath[resources.length];
        for(int i = 0; i < resources.length; i++)
            paths[i] = resources[i].getFullPath();

        return paths;
    }
/*
    public static EclipseSourceFolderDelegate getSourceFolder(IContribution contribution)
    {
        IFolder contributionFolder;
        IPath contributionPath;
        contributionFolder = (IFolder)contribution.getFolder().getAdapter(IFolder.class);
        contributionPath = contributionFolder.getFullPath();
        int i;
        int j;
        IFolder aifolder[];
        IFolder srcFolders[] = getSourceFolders(contributionFolder.getProject());
        aifolder = srcFolders;
        i = 0;
        j = aifolder.length;
          goto _L1
_L3:
        IFolder srcFolder = aifolder[i];
        if(contributionPath.isPrefixOf(srcFolder.getFullPath()))
            return new EclipseSourceFolderDelegate(srcFolder);
        i++;
_L1:
        if(i < j) goto _L3; else goto _L2
_L2:
        break MISSING_BLOCK_LABEL_99;
        CoreException e;
        e;
        e.printStackTrace();
        return null;
    }
*/
    public static IResourceDelegate getResourceDelegate(IResource resource)
    {
        IResourceDelegate resourceDelegate = null;
        if(1 == resource.getType())
            resourceDelegate = new EclipseFileDelegate((IFile)resource);
        else
        if(2 == resource.getType())
            resourceDelegate = new EclipseFolderDelegate((IFolder)resource);
        else
        if(4 == resource.getType())
            resourceDelegate = new EclipseProjectDelegate((IProject)resource);
        return resourceDelegate;
    }

    private static final EclipseResourceManager instance = new EclipseResourceManager();
//    private static TraceLogger logger = RuntimeManager.getLogger();
    public static final IResource EMPTY_RESOURCES[] = new IResource[0];
    public static final IFile EMPTY_FILES[] = new IFile[0];
    public static final IFolder EMPTY_FOLDERS[] = new IFolder[0];
    public static final IProject EMPTY_PROJECTS[] = new IProject[0];
    public static final IPath EMPTY_PATHS[] = new IPath[0];

}