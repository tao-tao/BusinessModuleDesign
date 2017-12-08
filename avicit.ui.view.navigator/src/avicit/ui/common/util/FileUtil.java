package avicit.ui.common.util;


import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tools.ant.DirectoryScanner;
import org.dom4j.Element;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import avicit.ui.core.runtime.resource.EclipseFolderDelegate;
import avicit.ui.runtime.core.cluster.FunctionClusterNode;
import avicit.ui.runtime.core.cluster.function.ComponentNode;
import avicit.ui.runtime.core.node.PackageNode;
import avicit.ui.runtime.core.subsystem.SubSystemNode;

public final class FileUtil
{

    private FileUtil()
    {
    }

    public static void writeFile(byte content[], String fileName)
        throws IOException
    {
        File t_Dir = new File(FilenameUtils.getFullPath(fileName));
        if(!t_Dir.exists() && !t_Dir.mkdirs())
        {
            throw new IOException("The parent path is not valid.");
        } else
        {
            FileUtils.writeByteArrayToFile(new File(fileName), content);
            return;
        }
    }

    public static void writeFile(String string, String filename)
        throws Exception
    {
        FileUtils.writeStringToFile(new File(filename), string);
    }

    public static void writeFile(String string, String filename, String charSet)
        throws Exception
    {
        FileUtils.writeStringToFile(new File(filename), string, charSet);
    }

    public static String classResourceToString(ClassLoader classLoader, String resourcePath, String charSet)
        throws IOException
    {
        return IOUtils.toString(classLoader.getResourceAsStream(resourcePath), charSet);
    }

    public static String readFile(String filename)
        throws Exception
    {
        return readFile(filename, OsUtil.getDefaultEncoding());
    }

    public static String readFile(String filename, String charSet)
        throws Exception
    {
        java.io.InputStream inputStream = null;
        String s;
        inputStream = new FileInputStream(filename);
        s = IOUtils.toString(inputStream, charSet);
        IOUtils.closeQuietly(inputStream);
        return s;
    }

    public static void copyDirectory(File sourceDirectory, File targetDirectory, FileFilter filter)
        throws IOException
    {
        if(null == sourceDirectory || !sourceDirectory.exists())
            return;
        if(!targetDirectory.exists() && !targetDirectory.mkdirs())
            throw new IOException("DirCopyFailed");
        File t_Files[] = sourceDirectory.listFiles();
        for(int i = 0; i < t_Files.length; i++)
        {
            File t_File = t_Files[i];
            if(t_File.isDirectory())
            {
                File t_NextDirectory = new File(targetDirectory, t_File.getName());
                copyDirectory(t_File, t_NextDirectory, filter);
                continue;
            }
            if(filter == null || filter.accept(t_Files[i]))
                copyFile(t_File, targetDirectory);
        }

    }

	public static void copyFile(File sourceFile, File targetFile) {
		try {
			if (!sourceFile.exists()) {
				return;
			}

			if (targetFile.isDirectory()) {
				targetFile = new File(targetFile, sourceFile.getName());
			}

			FileChannel source = null;
			FileChannel destination = null;
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(targetFile).getChannel();

			if (destination != null && source != null) {
				destination.transferFrom(source, 0, source.size());
			}

			if (source != null) {
				source.close();
			}

			if (destination != null) {
				destination.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public static void deleteFile(File file)
        throws IOException
    {
        if(file.exists())
            if(file.isFile())
                file.delete();
            else
                FileUtils.deleteDirectory(file);
    }

    public static boolean mkdir(String fileName)
    {
        return mkdir(new File(fileName));
    }

    public static boolean mkdir(File file)
    {
        if(file.exists())
            return true;
        if(!file.isDirectory())
            file = file.getParentFile();
        return file.mkdirs();
    }

    public static String[] listFiles(String targetDir, String includePatterns[], String excludePatterns[])
    {
        DirectoryScanner scan;
        scan = new DirectoryScanner();
        scan.setBasedir(targetDir);
        scan.setIncludes(includePatterns);
        scan.setExcludes(excludePatterns);
        scan.scan();
        return scan.getIncludedFiles();
    }

    public static boolean existDefaultPackage(IFolder folder){
    	//0403直接返回true即可，后续可再定制
    	return true;
//    	boolean flag = false;
//    	IResource[] resources;
//		try {
//			resources = ((IFolder) folder).members();
//			if(resources.length ==0 || null == resources){
//				flag = true;
//				return flag;
//			}
//			for (int i = 0; i < resources.length; i++) {
//				if(resources[i] instanceof IFile){
//					flag = true;
//					break;
//				}
//			}
//		} catch (CoreException e) {
//			e.printStackTrace();
//		}
//    	return flag;
    }

    public static void listFiles(IFolder folder, List result, String path, String[] fileExt, String excludes){
			try {
				IResource[] resources = ((IFolder) folder).members();
				List files = new ArrayList();
				for (int i = 0; i < resources.length; i++) {
					if(resources[i] instanceof IFolder)
					{
						if(excludes != null && excludes.equals(resources[i].getName()))
							continue;
						listFiles((IFolder)resources[i], result, "".equals(path)?resources[i].getName(): path+"."+resources[i].getName() ,fileExt, excludes);
					}
					else
					{
						String ext = resources[i].getName();
						for(int j=0; j<fileExt.length; j++)
						{
							if (ext.endsWith(fileExt[j])) {
								files.add(resources[i]);
								break;
							}
						}
					}
				}
//				if(files.size()>0)
					result.add(new PackageNode(path, folder, files));
			} catch (CoreException e) {
				e.printStackTrace();
			}
    }

    public static void listSubSystemNode(IFolder folder, List result, String path, String[] fileExt, String excludes){
			try {
				IResource[] resources = ((IFolder) folder).members();
				List files = new ArrayList();
				for (int i = 0; i < resources.length; i++) {
					if(resources[i] instanceof IFolder)
					{
						if(excludes != null && excludes.equals(resources[i].getName()))
							continue;
						listSubSystemNode((IFolder)resources[i], result, "".equals(path)?resources[i].getName(): path+"."+resources[i].getName() ,fileExt, excludes);
					}
					else
					{
						String ext = resources[i].getName();
						for(int j=0; j<fileExt.length; j++)
						{
							if (ext.endsWith(fileExt[j])) {
								files.add(resources[i]);
								break;
							}
						}
					}
				}
				if(files.size()>0)
					result.add(new SubSystemNode(new EclipseFolderDelegate(folder)));
			} catch (CoreException e) {
				e.printStackTrace();
			}
    }

    public static void listComponentNode(IFolder folder, List result, String path, String[] fileExt, String excludes){
		try {
			IResource[] resources = ((IFolder) folder).members();
			List files = new ArrayList();
			for (int i = 0; i < resources.length; i++) {
				if(resources[i] instanceof IFolder)
				{
					if(excludes != null && excludes.equals(resources[i].getName()))
						continue;
					listComponentNode((IFolder)resources[i], result, "".equals(path)?resources[i].getName(): path+"."+resources[i].getName() ,fileExt, excludes);
				}
				else
				{
					String ext = resources[i].getName();
					for(int j=0; j<fileExt.length; j++)
					{
						if (ext.endsWith(fileExt[j])) {
							files.add(resources[i]);
							break;
						}
					}
				}
			}

			if (files.size() > 0)
				result.add(new ComponentNode(new EclipseFolderDelegate(folder.getParent())));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public static void listFunctionClusterNode(IFolder folder, List result, String path, String[] fileExt, String excludes){
		try {
			IResource[] resources = ((IFolder) folder).members();
			List files = new ArrayList();
			for (int i = 0; i < resources.length; i++) {
				if(resources[i] instanceof IFolder)
				{
					if(excludes != null && excludes.equals(resources[i].getName()))
						continue;
					listFunctionClusterNode((IFolder)resources[i], result, "".equals(path)?resources[i].getName(): path+"."+resources[i].getName() ,fileExt, excludes);
				}
				else
				{
					String ext = resources[i].getName();
					for(int j=0; j<fileExt.length; j++)
					{
						if (ext.endsWith(fileExt[j])) {
							files.add(resources[i]);
							break;
						}
					}
				}
			}

			if (files.size() > 0)
				result.add(new FunctionClusterNode(new EclipseFolderDelegate(folder)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//    public static void listFunctionClusterNode(IFolder folder, List result, String path, String[] fileExt, String excludes){
//    	
//    }

    public static boolean modify(String path,String name) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc;
		try {
			builder = factory.newDocumentBuilder();
			
			doc=builder.parse(new File(path));
			doc.normalize(); 
			Node node = doc.getFirstChild();
			NodeList  firstList=node.getChildNodes();     
		    for(int i =0;i<firstList.getLength();i++){ 
		    	Node child =firstList.item(i);
				if (child.getNodeName().equalsIgnoreCase("module")) {
					NodeList  secondList=child.getChildNodes();
					for(int j=0;j<secondList.getLength();j++){
						Node childWeb =secondList.item(j);
						if (childWeb.getNodeName().equalsIgnoreCase("web")) {
							NodeList  thirdList=childWeb.getChildNodes();
							for(int k=0;k<thirdList.getLength();k++){
								Node childEnd =thirdList.item(k);
								//System.out.println(childEnd.getNodeName());
								if (childEnd.getNodeName().equalsIgnoreCase("web-uri")) {
									childEnd.setTextContent(name+".war");
									//System.out.println("web-uri:"+childEnd.getTextContent());
								}
								if (childEnd.getNodeName().equalsIgnoreCase("context-root")) {
									childEnd.setTextContent("/"+name);
									//System.out.println("context-root:"+childEnd.getTextContent());
								}
							}
							break;
						}
						
						
					}
					break;
				}
				
				
		    }
		    TransformerFactory tFactory = TransformerFactory.newInstance();       
            Transformer transformer = tFactory.newTransformer();    
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); 
            DOMSource source = new DOMSource(doc);     
            StreamResult result = new StreamResult(path);       
            transformer.transform(source, result); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return true;
	}
	
	 public static void writeXML(Document doc, String file) {
         try {
        	 TransformerFactory tFactory = TransformerFactory.newInstance();       
             Transformer transformer = tFactory.newTransformer();    
             transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); 
             DOMSource source = new DOMSource(doc);     
             StreamResult result = new StreamResult(file);       
             transformer.transform(source, result); 
            // t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(file)));
         } catch (TransformerException e) {
             e.printStackTrace();
         } catch (Exception e) {
             e.printStackTrace();  
         }
     }

	public static void parseXML(String searchName, Element element, List results) {

		if (element != null && !element.elements().isEmpty()) {
			for (Iterator it = element.elements().iterator(); it.hasNext();) {
				Object child = it.next();

				if (child instanceof Element) {
					Element ele = (Element) child;

					if (ele != null && !ele.elements().isEmpty()) {
						List cache = results;

						parseXML(searchName, ele, results);

						if (results.size() == cache.size()) {
							continue;
						}
					} else if (ele.getName().equals(searchName)) {
						results.add(ele);
					} else {
						continue;
					}
				}
			}
		}
	}

    public static void main(String[] args){
    	//String[] ss = FileUtil.listFiles("C:\\t\\g", new String[]{"*.txt"}, new String[0]);
    	//System.out.println(ss.length);
    	modify("E:\\application.xml","");
    }
}