package avicit.ui.common.util;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;

/**
 * TODO: This class should be abstract to common utility.
 * 
 * This class implements management of resource in a workspace.
 */
public class WorkspaceUtil {

	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	public WorkspaceUtil() {
		// TODO: can we delete this?
	}

	/**
	 * Create the given file in the workspace resource info tree.
	 */
	public static void ensureExistsInWorkspace(final IFile resource,
			final InputStream contents) {
		if (resource == null) {
			return;
		}
		IWorkspaceRunnable body = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				if (resource.exists()) {
					resource.setContents(contents, true, false, null);
				} else {
					ensureExistsInWorkspace(resource.getParent(), true);
					resource.create(contents, true, null);
				}
			}
		};
		try {
			getWorkspace().run(body, null);
		} catch (CoreException e) {
			// Test.EclipseWorkspaceTest.Error.FileCreationInWorkspace = Fail in
			// creating file:{0} in the workspace resource info tree.
			System.out
					.println("Test.EclipseWorkspaceTest.Error.FileCreationInWorkspace");//$NON-NLS-1$
		}
	}

	/**
	 * Create the given file in the workspace resource info tree.
	 */
	public static void ensureExistsInWorkspace(IFile resource, String contents) {
		// FIXME: We'll need some way for handing file encoding.
		ensureExistsInWorkspace(resource, new ByteArrayInputStream(contents
				.getBytes()));
	}

	/**
	 * Create the given resource in the workspace resource info tree.
	 */
	public static void ensureExistsInWorkspace(final IResource resource,
			final boolean local) {
		IWorkspaceRunnable body = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				create(resource, local);
			}
		};
		try {
			getWorkspace().run(body, null);
		} catch (CoreException e) {
			// Test.EclipseWorkspaceTest.Error.ResourceCreationInWorkspace =
			// Fail in creating resource:{0} in the workspace resource info
			// tree.
			System.out
					.println("Test.EclipseWorkspaceTest.Error.ResourceCreationInWorkspace");//$NON-NLS-1$
		}
	}

	/**
	 * crate the resource if the resource is not existed, create a new one.
	 * 
	 * @param resource -
	 *            resource instance
	 * @param local -
	 *            a flag controlling whether or not the folder will be local
	 *            after the creation
	 * @throws CoreException
	 */
	protected static void create(final IResource resource, boolean local)
			throws CoreException {
		if (resource == null || resource.exists()) {
			return;
		}
		if (!resource.getParent().exists()) {
			create(resource.getParent(), local);
		}
		switch (resource.getType()) {
		case IResource.FILE:
			((IFile) resource).create(local ? new ByteArrayInputStream(
					new byte[0]) : null, true, getMonitor());
			break;
		case IResource.FOLDER:
			((IFolder) resource).create(true, local, getMonitor());
			break;
		case IResource.PROJECT:
			((IProject) resource).create(getMonitor());
			((IProject) resource).open(getMonitor());
			break;
		}
	}

	/**
	 * create and return a NullProgressMonitor
	 * 
	 * @return - NullProgressMonitor
	 */
	public static IProgressMonitor getMonitor() {
		return new NullProgressMonitor();
	}

	/**
	 * Get the project reference for a given path
	 * 
	 * @param path -
	 *            the path
	 * @return IProject - the project reference
	 */
	public static IProject getProjectFor(IPath path) {
		String[] segs = path.segments();
		String projectPath = new String();
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		IProject project = null;
		for (int p = 0; p < projects.length; p++) {
			if (projects[p].isOpen()) {
				for (int s = 0; s < segs.length; s++) {
					if (segs[s].equalsIgnoreCase(projects[p].getName())) {
						// Once we have a match on the project name, then
						// the remainder of the segments equals the project path
						for (int s2 = s + 1; s2 < segs.length; s2++) {
							projectPath = projectPath
									+ IFileFolderConstants.PATH_SEPARATOR
									+ segs[s2];
						}
						project = projects[p];
						break;
					}
				}
			}
		}
		if (project == null) {
			return null;
		}

		// TODO: still don't understand why this refreshLocal is necessary
		// for now, going to only allow it if this method is called
		// when the tree isn't locked. This shouldn't cause a regression, since
		// when the call fails currently things keep on going due to the catch
		if (!project.getWorkspace().isTreeLocked()) {
			try {
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				// TODO C.B.:pushing this down to a warning because it creates
				// really
				// spurious output. Don't know why we are calling refreshLocal
				// at all.
				System.out.println("Error.RefreshingLocal");
			}
		}

		IResource res = project.findMember(new Path(projectPath));
		if ((res != null) && (res.exists())) {
			return project;
		}
		return null;
	}

	/**
	 * Get the project reference for a given file
	 * 
	 * @param file -
	 *            the IFile file reference
	 * @return IProject - the project reference
	 */
	public static IProject getProjectFor(IFile file) {
		IPath testPath = new Path(file.getFullPath().toOSString());
		return getProjectFor(testPath);
	}

	/**
	 * Get the project reference for a given file
	 * 
	 * @param file -
	 *            the File file reference
	 * @return IProject - the project reference
	 */
	public static IProject getProjectFor(File file) {
		IPath testPath = new Path(file.getAbsolutePath());
		return getProjectFor(testPath);
	}

	/**
	 * Get the project-relative resource reference for a given path
	 * 
	 * @param path -
	 *            the path
	 * @return IResource - the project-relative resource
	 */
	public static IResource getProjectRelativeResource(IPath path) {
		String[] segs = path.segments();
		String projectPath = new String();
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		IProject project = null;
		for (int p = 0; p < projects.length; p++) {
			if (projects[p].isOpen()) {
				for (int s = 0; s < segs.length; s++) {
					if (segs[s].equalsIgnoreCase(projects[p].getName())) {
						// Once we have a match on the project name, then
						// the remainder of the segments equals the project path
						for (int s2 = s + 1; s2 < segs.length; s2++) {
							projectPath = projectPath
									+ IFileFolderConstants.PATH_SEPARATOR
									+ segs[s2];
						}
						project = projects[p];
						break;
					}
				}
			}
		}
		if (project == null) {
			return null;
		}

		return project.getFile(projectPath);
	}

	/**
	 * Get the project-relative resource reference for a given file
	 * 
	 * @param file -
	 *            the File file reference
	 * @return IResource - the project-relative resource
	 */
	public static IResource getProjectRelativeResource(File file) {
		IPath testPath = new Path(file.getAbsolutePath());
		return getProjectRelativeResource(testPath);
	}

	public static IFolder getAndEnsureChildFolder(IFolder folder, String pName){
		pName = pName.replace(".", "/");
		String[] names = pName.split("/");
		for(int j=0; j<names.length ; j++)
		{
			if(!StringUtils.isEmpty(names[j]))
			{
				folder = folder.getFolder(new Path(names[j]));
				if(!folder.exists())
					try {
						folder.create(true, true, null);
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		return folder;
	}
	
	public static IFolder getAndEnsureChildFolderByProject(IFolder folder, String pName,String filePath){
		String packagePath ="";
		String[] names = pName.split("/");
		
		for(int i = 0; i < names.length; i++){
			if(!names[i].equals("")){
				packagePath += ("/"+names[i]);
				folder = folder.getProject().getFolder(filePath+packagePath);	
				if(!folder.exists()){
					try {
						folder.create(true, true, null);
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		return folder;
	}
}