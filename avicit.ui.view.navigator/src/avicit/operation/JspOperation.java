package avicit.operation;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;



public class JspOperation extends EcValidatorAdapter implements EcOperation {

	public static final String DJPATH="WebRoot/eform/eformsys/fceform/djfile/";
	public static final String djFile = "WebRoot/eform/eformsys/fceform/djfile";
	
	public boolean delete(IProject project,String filePath) {
		filePath = filePath.substring(filePath.indexOf(project.getName())+project.getName().length()+1);
		try {
			//filePath = JspDesigner.getModelIdPath(project, filePath);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String redj =filePath.replace("META-INF\\jsp\\", "").replace(".jsp", ".dj");
		IFile f =project.getFile(DJPATH+redj);
		if(f.exists()){
			try {
				f.delete(true, null);
				return true;
			} catch (CoreException e) {	
				e.printStackTrace();
			}
		}
		return false;
	}


	

	@Override
	public boolean rename(IProject project, String fileName,String filePath, String newName) {
		String path = filePath.substring(1);
		path = path.substring(path.indexOf("\\") + 1);
		String app = "";
		try {
		//	app = JspDesigner.getModelId(project,path);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String djfilePath = app + "\\" + filePath.substring(filePath.indexOf("META-INF\\jsp")+12).replace(".jsp", ".dj");
		File file =project.getFile(DJPATH+djfilePath).getLocation().toFile();
		String djPath = file.toString().substring(0,file.toString().lastIndexOf("\\")+1)+newName.replace(".jsp", ".dj");
		if(file.exists()){
			path = path.replaceAll(fileName, newName);
			String jspStr = getFileContext(project.getFile(path));
			String djStr = getFileContext(project.getFile(DJPATH+djfilePath));
			jspStr = jspStr.replaceAll("dj_sn=\""+fileName.replace(".jsp", "")+"\"", "dj_sn=\""+newName.replace(".jsp", "")+"\"");
			djStr = djStr.replaceAll("dj_sn=\""+fileName.replace(".jsp", "")+"\"", "dj_sn=\""+newName.replace(".jsp", "")+"\"");
			try {
				byte[] byteArray = jspStr.getBytes("GBK");
				ByteArrayInputStream baos = new ByteArrayInputStream(byteArray);
				IFile ifile = project.getFile(path);
				ifile.setContents(baos, true, true, null);
				byte [] byteArrayDJ = djStr.getBytes("GBK");
				ByteArrayInputStream baosDJ = new ByteArrayInputStream(byteArrayDJ);
				IFile ifileDJ = project.getFile(DJPATH+djfilePath);
				ifileDJ.setContents(baosDJ, true, true, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			file.renameTo(new File(djPath));
			return true;
		}
		return false;
	}

	






	@Override
	public boolean copy(IProject project, String filePath, String newPackage) {
		filePath = filePath.substring(filePath.indexOf(project.getName())+project.getName().length()+1);
		String jspStr = getFileContext(project.getFile(filePath));
		try {
			//filePath = JspDesigner.getModelIdPath(project, filePath);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
		String oldPackage = filePath.replace("META-INF\\jsp\\", "");
		oldPackage = oldPackage.substring(0, oldPackage.lastIndexOf("\\") + 1);
		oldPackage = oldPackage.replaceAll("\\\\", "/");
		String newJspPath = newPackage.substring(newPackage.indexOf(project.getName())+project.getName().length()+1)+"\\"+fileName;
		newPackage = newPackage.substring(newPackage.indexOf(project.getName())+project.getName().length()+1);
		try {
		//	newPackage = JspDesigner.getModelIdPath(project, newPackage);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		newPackage = newPackage.replace("META-INF\\jsp\\", "");
		newPackage = newPackage.replaceAll("\\\\", "/");
		if(!newPackage.endsWith("/")){
			newPackage += "/";
		}
		jspStr = jspStr.replaceAll("packages=\""+oldPackage+"\"", "packages=\""+newPackage+"\"");
		try {
			byte[] byteArray = jspStr.getBytes("GBK");
			ByteArrayInputStream baos = new ByteArrayInputStream(byteArray);
			IFile ifile = project.getFile(newJspPath);
			ifile.setContents(baos, true, true, null);
			
			IFolder folder = null;
			String packagePath = "";
			String [] packages = newPackage.split("/");
			for(int i = 0; i < packages.length; i++){
				if(!packages[i].equals("")){
					packagePath += ("/"+packages[i]);
					folder = project.getFolder(djFile+packagePath);	
					if(!folder.exists()){
						folder.create(true, true, null);
					}
				}
			}
			IFile djFile = folder.getFile(fileName.replace(".jsp", ".dj"));
			String djfilePath = filePath.replace("META-INF\\jsp\\", "").replace(".jsp", ".dj");
			String djStr = getFileContext(project.getFile(DJPATH+djfilePath));
			djStr = djStr.replaceAll("packages=\""+oldPackage+"\"", "packages=\""+newPackage+"\"");
			byte[] byteArrayDJ = djStr.getBytes("GBK");
			ByteArrayInputStream baosDJ = new ByteArrayInputStream(byteArrayDJ);
			if(djFile.exists())
				djFile.setContents(baosDJ, true, true, null);
			else
				djFile.create(baosDJ, true, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}




	@Override
	public void validator(DiagramErrors errors, IFile file) {
		// TODO Auto-generated method stub
		
	}
	


	private String getFileContext(IFile file)
	{
		StringBuilder context = new StringBuilder();
		BufferedReader bis = null;
		try {
			bis = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file.getLocationURI()))));
			String temp = "";
			while ((temp = bis.readLine()) != null) {
			     context.append(temp).append("\n");
			}
		} catch (IOException e) {
			//e.printStackTrace();
		} finally {
			if(bis!=null)
				try {
					bis.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}
		return context.toString();
	}


}
