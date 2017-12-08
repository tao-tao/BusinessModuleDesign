package avicit.ui.runtime.resource.inf;

import java.io.File;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;

import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IOutputFolderDelegate;
import avicit.ui.core.runtime.resource.IProjectDelegate;
import avicit.ui.core.runtime.resource.IRootDelegate;
import avicit.ui.core.runtime.resource.ISourceFolderDelegate;
import avicit.ui.runtime.util.IDataContainer;

public interface IResourceDelegate extends IAdaptable, IDataContainer {

	public abstract int getType() throws ResourceException;

	public abstract IProjectDelegate getProject() throws ResourceException;

	public abstract IFolderDelegate getParent() throws ResourceException;

	public abstract ISourceFolderDelegate getSourceFolder();

	public abstract boolean exists();

	public abstract String getName();

	public abstract String getFullPath();

	public abstract String getProjectRelativePath();

	public abstract String getSourceRelativePath() throws ResourceException;

	public abstract void delete() throws ResourceException;

	public abstract IRootDelegate getRoot() throws ResourceException;

	public abstract void create();

	public abstract long getLastModified();

	public abstract File getFile();

	public abstract String getProtocol();

	public abstract boolean isArchive();

	public abstract boolean isEditable();

	public abstract boolean isBinary();

	public abstract String getPersistentProperty(String s);

	public abstract void setPersistentProperty(String s, String s1);
	
	public IResource getResource();

	public static final IResourceDelegate NO_RESOURCES[] = new IResourceDelegate[0];
	public static final IProjectDelegate NO_PROJECTS[] = new IProjectDelegate[0];
	public static final IFileDelegate NO_FILES[] = new IFileDelegate[0];
	public static final IFolderDelegate NO_FOLDERS[] = new IFolderDelegate[0];
	public static final ISourceFolderDelegate NO_SOURCE_FOLDERS[] = new ISourceFolderDelegate[0];
	public static final IOutputFolderDelegate NO_OUTPUT_FOLDERS[] = new IOutputFolderDelegate[0];
	public static final int FILE = 1;
	public static final int FOLDER = 2;
	public static final int PROJECT = 4;
	public static final int ROOT = 8;

}