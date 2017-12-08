package avicit.ui.ec.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;


/**
 * 
 * @�����   �� DiagramErrors.java  
 * @������   ��xxxxxx ҳ��
 * @see class  �� ���г���˳�����ص��࣬����ĸ���̳м��������Ƶ���ȣ�
 * @exception class����������class ָ�����ܱ��׳����쳣��
 * @����           : yao lei@tansun.com.cn  
 * @����ʱ�� �� 2012-12-28 ����9:48:58
 * @�汾           �� 1.00
 * 
 * @�޸ļ�¼: 
 * @�汾            �޸���          �޸�ʱ��                  �޸���������
 * ----------------------------------------
 * @1.00     Ҧ��            2012-12-28                   ����9:48:58
 * ----------------------------------------
 */
public class DiagramErrors {
	
	private List<DiagramError> errors = new ArrayList<DiagramError>();
	
	public static final String ERROR_PREFIX = "[ERROR]";
    public static final String WARNING_PREFIX = "[WARN]";
    public static final String LEVEL_ERROR = "ERROR";
    
	private static String createTableMessage(String message){
		return message;
	}
	
	/**
	 * Add an error messagefor TableModel.
	 * 
	 * @param level the error level
	 * @param table the table model
	 * @param message the error message
	 */
	public void addError( String message,String id,int line){
	    //if(level.equals(DBPlugin.LEVEL_ERROR)){
    	this.errors.add(new DiagramError(createTableMessage(message), id,line));
    		
	    //} 
	}

	public void addError( String message,int line){
	    //if(level.equals(DBPlugin.LEVEL_ERROR)){
    	this.errors.add(new DiagramError(createTableMessage(message),line));
    		
	    //} 
	}
	/**
	 * Returns all errors.
	 * 
	 * @return all errors
	 */
	public List<DiagramError> getErrors(){
		return this.errors;
	}
	
	public static class DiagramError {
		
		
		private String message;
		private String id;
		private int line;
		
		private DiagramError(String message,int line){
			
			this.message = message;
			this.line =line;
		}

		private DiagramError(String message, String id,int line){
			
			this.message = message;
			this.id = id;
			this.line =line;
		}

		/**
		 * Returns the error message
		 * @return the error message
		 */
		public String getMessage() {
			return message;
		}
		
		/**
		 * Returns the error level
		 * @return the error level
		 */
		public String getId(){
			return this.id;
		}
		
		/**
		 * Add marker to the given file.
		 * @param file the ER-Diagram file
		 */
		public void addMarker(IFile file){
			addMarker(file, IMarker.SEVERITY_ERROR, message);
		}
		
		/**
		 * Adds marker to the specified line.
		 * 
		 * @param resource the target resource
		 * @param type the error type that defined by IMaker
		 * @param message the error message
		 */
		private  void addMarker(IResource resource, int type, String message){
			try {
				IMarker marker = resource.createMarker(IMarker.PROBLEM);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(IMarker.SEVERITY, new Integer(type));
				map.put(IMarker.MESSAGE, message);
				//Ϊ�д��д���
				map.put(IMarker.LINE_NUMBER,line);
				marker.setAttributes(map);
			} catch(CoreException ex){
				
			}
		}
	}

	
}
