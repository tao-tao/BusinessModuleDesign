package avicit.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;


/**
 * 
 * @类名称   ： DiagramErrors.java  
 * @类描述   ：xxxxxx 页面
 * @see class  ： （列出与此程序相关的类，如从哪个类继承及功能类似的类等）
 * @exception class：（创建由class 指定的能被抛出的异常）
 * @作者           : yao lei@tansun.com.cn  
 * @创建时间 ： 2012-12-28 上午9:48:58
 * @版本           ： 1.00
 * 
 * @修改记录: 
 * @版本            修改人          修改时间                  修改内容描述
 * ----------------------------------------
 * @1.00     姚蕾            2012-12-28                   上午9:48:58
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
				//为有错行打标记
				map.put(IMarker.LINE_NUMBER,line);
				marker.setAttributes(map);
			} catch(CoreException ex){
				
			}
		}
	}

	
}
