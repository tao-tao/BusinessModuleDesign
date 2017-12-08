package avicit.platform6.tools.codegeneration.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtil {

	/**
	 * 压缩指定路径下的指定过滤器过滤完后的文件
	 * @param zipOutPath
	 * @param inFilePath
	 * @param FilenameFilter
	 * @return
	 */
	
	public static   byte[] zipFile(String zipOutPath,String inFilePath,FilenameFilter filenameFilter){
		byte[] datas=null;
		 try {
		   
          FileOutputStream dest = new FileOutputStream(zipOutPath);
          ZipOutputStream zout = new ZipOutputStream(dest);
         
          zout.setEncoding("UTF-8");//设置的和文件名字格式一样或开发环境编码设置一样的话就能正常显示了
          byte data[] = new byte[1024];
          File f = new File(inFilePath);
          if(f.isDirectory()){
        	  File[] fs=f.listFiles(filenameFilter);
        	  for(File fz:fs){
        		  FileInputStream fi = new FileInputStream(fz);
                  ZipEntry entry = new ZipEntry(fz.getName());
                  
                  zout.putNextEntry(entry);
                  int count;
                  while ((count = fi.read(data)) != -1) {
                      zout.write(data, 0, count);
                  }
                  fi.close();
        	  }
          }
         
          
          zout.close();
          dest.close();
          DataInputStream dis = new DataInputStream(new FileInputStream(new File(zipOutPath)));
		   int size = dis.available();
		   datas  = new byte[size];
		   dis.read(datas);
		   dis.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
      return datas;
		
	}
	/**
	 * 压缩指定路径下的全部文件
	 * @param zipOutPath
	 * @param inFilePath
	 * @return
	 */
	
	public static   byte[] zipFile(String zipOutPath,String inFilePath){
		byte[] datas=null;
		 try {
		   
          FileOutputStream dest = new FileOutputStream(zipOutPath);
          ZipOutputStream zout = new ZipOutputStream(dest);
         
          zout.setEncoding("UTF-8");//设置的和文件名字格式一样或开发环境编码设置一样的话就能正常显示了
          byte data[] = new byte[1024];
          File f = new File(inFilePath);
          if(f.isDirectory()){
        	  File[] fs=f.listFiles();
        	  for(File fz:fs){
        		  FileInputStream fi = new FileInputStream(fz);
                  ZipEntry entry = new ZipEntry(fz.getName());
                  
                  zout.putNextEntry(entry);
                  int count;
                  while ((count = fi.read(data)) != -1) {
                      zout.write(data, 0, count);
                  }
                  fi.close();
        	  }
          }
         
          
          zout.close();
          dest.close();
          DataInputStream dis = new DataInputStream(new FileInputStream(new File(zipOutPath)));
		   int size = dis.available();
		   datas  = new byte[size];
		   dis.read(datas);
		   dis.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
      return datas;
		
	}
	/**
	 * 压缩指定路径下的指定文件
	 * @param zipOutPath
	 * @param inFilePath
	 * @param filepaths
	 * @return
	 */
	
	public static   byte[] zipFile(String zipOutPath,String inFilePath,String[] fileNames){
		byte[] datas=null;
		 try {
		   
          FileOutputStream dest = new FileOutputStream(zipOutPath);
          ZipOutputStream zout = new ZipOutputStream(dest);
         
          zout.setEncoding("UTF-8");//设置的和文件名字格式一样或开发环境编码设置一样的话就能正常显示了
          byte data[] = new byte[1024];
          
          for(String filename:fileNames){
        	  File f = new File(inFilePath+File.separator+filename);
        	  FileInputStream fi = new FileInputStream(f);
              ZipEntry entry = new ZipEntry(filename);
              
              zout.putNextEntry(entry);
              int count;
              while ((count = fi.read(data)) != -1) {
                  zout.write(data, 0, count);
              }
              fi.close();
          }
          
          zout.close();
          dest.close();
          DataInputStream dis = new DataInputStream(new FileInputStream(new File(zipOutPath)));
		   int size = dis.available();
		   datas  = new byte[size];
		   dis.read(datas);
		   dis.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
      return datas;
		
	}
	/**
	 * 压缩指定路径下的指定文件，可以指定编码格式
	 * @param zipOutPath
	 * @param inFilePath
	 * @param fileNames
	 * @param encoding
	 * @return
	 */
	public static   byte[] zipFile(String zipOutPath,String inFilePath,String[] fileNames,String encoding){
		byte[] datas=null;
		 try {
		   
          FileOutputStream dest = new FileOutputStream(zipOutPath);
          ZipOutputStream zout = new ZipOutputStream(dest);
         
          zout.setEncoding(encoding);//设置的和文件名字格式一样或开发环境编码设置一样的话就能正常显示了
          byte data[] = new byte[1024];
          
          for(String filename:fileNames){
        	  File f = new File(inFilePath+File.separator+filename);
        	  
        	  FileInputStream fi = new FileInputStream(f);
              ZipEntry entry = new ZipEntry(filename);
              
              zout.putNextEntry(entry);
              int count;
              while ((count = fi.read(data)) != -1) {
                  zout.write(data, 0, count);
              }
              fi.close();
          }
          
          zout.close();
          dest.close();
          DataInputStream dis = new DataInputStream(new FileInputStream(new File(zipOutPath)));
		   int size = dis.available();
		   datas  = new byte[size];
		   dis.read(datas);
		   dis.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
      return datas;
		
	}
	
	/**
	 * 将包含一个文件的，解压缩
	 * @param zipName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String unZip(String zipName,String fileType){

		System.out.println("unZip zipName=="+zipName);
		String zipNames=zipName.substring(zipName.lastIndexOf("\\")+1);//原文件名
		String imageName=zipNames.split("[.]")[0]+"."+fileType;//图片文件名
        String filePath = zipName.substring(0, zipName.lastIndexOf("\\")+1);//文件路径
		try{
			System.out.println("unZip filePath=="+filePath);
			System.out.println("unZip imageName=="+imageName);
			System.out.println("unZip zipNames=="+zipNames);
            ZipFile zipFile=new ZipFile(filePath+zipNames.split("[.]")[0]+".zip");
//            ZipEntry ze = zipFile.getEntry(imageName);
            Enumeration<ZipEntry> ze = zipFile.getEntries();
			InputStream	ps=zipFile.getInputStream(ze.nextElement());
            FileOutputStream fos = new FileOutputStream(filePath + imageName);
            int count;
            byte data[] = new byte[1024];
            while ((count = ps.read(data)) != -1)
            {
               fos.write(data, 0, count);
            }
            zipFile.close();
            ps.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath + imageName;
	}
	public static void main(String[] args) {
		String inurl="D:/javawork/AVICIT_PLATFORM/trunk/integration/liferay-portal-5.1.2/avicit-portal-1.0.0/webapps/ROOT/WEB-INF";
		String[] a={"portlet-custom.xml","liferay-portlet.xml","liferay-display.xml","classes/content/Language-ext.properties","classes/content/Language-ext_zh_CN.properties"};
		
		//zipFile("d:\\a.zip",inurl,a );
		zipFile("d:\\a.zip","E:/logs" );
		

	}
	
}
