package avicit.ui.ec.operation;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;



public interface EcOperation {
	
	public boolean delete(IProject project,String filePath);
	public boolean rename(IProject project,String fileName,String filePath,String newName);
	public boolean copy(IProject project,String filePath,String newPackage);
	public void validator(DiagramErrors errors,IFile file);
	public void refresh();
}
