package avicit.ui.ec.operation;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

public abstract class EcValidatorAdapter implements EcOperation{

	@Override
	public boolean delete(IProject project, String filePath) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rename(IProject project, String fileName,String filePath, String newName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean copy(IProject project, String filePath, String packageName) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 
	 * @�������    ��getLineForError
	 * @��������    ����ȡ�������ڵ�����
	 * @see class #method�� (ָ����صķ��������ѯĳһ���ͻ���������Ϣ��
	 * ���ܸ�ݿͻ��Ų�ѯ��ͻ���Ʋ�ѯ������������
	 * �������Ϊ����صģ����ڴ����г���
	 * @�߼�����    ��
	 * @param   ����
	 * @return  ��int
	 * @throws  ����
	 * @since   ��Ver 1.00
	 */
	 public static int getLineForError(File sourceFile,String tag){  
		 FileReader in = null;
		 LineNumberReader reader = null;
		 try{
	        in = new FileReader(sourceFile);     
	        reader = new LineNumberReader(in);     
	        String s = "";          
	        int lines = 0;     
	        while (s != null) {     
	            lines++;     
	            s = reader.readLine();  
	            if(s == null){
	            	break;
	            }
	            if(s.contains(tag)){
	            	return  reader.getLineNumber();
	            }
	   
	        }     
	        
	    }catch(Exception e){
	    	e.printStackTrace();
	    }finally{
	    	IOUtils.closeQuietly(reader);
	    	IOUtils.closeQuietly(in);
	    }
		 return -1;
	
	 }

	@Override
	public void validator(DiagramErrors errors, IFile file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
}
