package avicit.operation;


import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

import avicit.operation.DiagramErrors.DiagramError;

public class EcOperationAdapater {

	private List files;
	//public File file;
	private EcOperation op;
	
	private boolean flg = false;

	public EcOperation getOp() {
		return op;
	}

	public void setOp(EcOperation op) {
		this.op = op;
	}

	private void end(File f) {
	};

	public boolean init(String filePath) {/*
		//this.file=f;
		if (filePath.endsWith(".jsp")) {
			JspOperation op = new JspOperation();
			this.setOp(op);
			return true;
		} else if(filePath.endsWith(".pagex.xml")){
			ControlValidator op = new ControlValidator();
			this.setOp(op);
			return true;
		} else if(filePath.endsWith(".biz.xml")){
			BizValidator op = new BizValidator();
			this.setOp(op);
			return true;
		} else if(filePath.endsWith(".spring.xml")){
			SpringValidator op =new SpringValidator();
			this.setOp(op);
			return true;
		} else if(filePath.endsWith("component-extension.xml")){
			ExtensionValidator op =new ExtensionValidator();
			this.setOp(op);
			return true;
		} else if(filePath.endsWith("component-config.xml")){
			ComponentConfigValidator op =new ComponentConfigValidator();
			this.setOp(op);
			return true;
		} else if(filePath.endsWith("component-ext-config.xml")){
			ComponentExtConfigValidator op =new ComponentExtConfigValidator();
			this.setOp(op);
			return true;
		} else if(filePath.contains("META-INF/exception")){
			ExceptionValidator op =new ExceptionValidator();
			this.setOp(op);
			return true;
		} else if(filePath.endsWith(".blc.xml")){
			BlcValidator op =new BlcValidator();
			this.setOp(op);
			return true;
		}
		return false;
	*/return false;}

	public void addFile(IFile f) {
		files.add(f);
	}

	public boolean delete(IProject project,String filePath) {
		// TODO Auto-generated method stub
		if(init(filePath)){
			flg = op.delete(project,filePath);
		}
		//this.end(filePath);
		return flg;
	}

	public boolean rename(IProject project,String fileName ,String filePath,String newName) {
		
		if(init(filePath)){
			flg = op.rename(project,fileName,filePath,newName);
			try {
				project.getFolder("WebRoot").refreshLocal(IResource.FOLDER, new NullProgressMonitor());
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return flg;
	}

	public boolean copy(IProject project,String filePath,String newPackage) {
		// TODO Auto-generated method stub
		if(init(filePath)){
			flg = op.copy(project,filePath,newPackage);
		}
		//this.end(filePath);
		return flg;
	}
	public DiagramErrors validator(IFile file) {
		
		DiagramErrors errors = null;
		if(init(file.toString())){
			try {
				((IResource) file).deleteMarkers(IMarker.PROBLEM, false, 0);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			errors = new DiagramErrors();
			op.validator(errors,file);
			
			for(DiagramError error: errors.getErrors()){
	            error.addMarker((IFile) file);
	        }
		}
		
		
		return errors;
	}
	
	private DiagramErrors createError(){
		return new DiagramErrors();
	}

	
	
}
