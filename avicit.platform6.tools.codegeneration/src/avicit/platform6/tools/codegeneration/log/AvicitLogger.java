package avicit.platform6.tools.codegeneration.log;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;

import avicit.platform6.tools.codegeneration.CodeGenerationActivator;
/**
 * <p>金航数码科技有限责任公司</p>
 * <p>作者：dingrc</p>
 * <p>邮箱：dingrc@avicit.com</p>
 * <p>创建时间：2012-5-22</p>
 *
 * <p>类说明：日志类</p>
 * <p>修改记录：</p>
 */
public class AvicitLogger extends org.apache.log4j.Logger{
	public static final String PLUGIN_ID = "avicit.platform6.tools.codegeneration";
	protected AvicitLogger(String name) {
		super(name);
		
	}
	public static void logInfo(String message) {
		log(IStatus.INFO, IStatus.OK, message, null);
	}
	public static void openError(Throwable e) {
		openError(e.getMessage(), e);
	}
	
	public static void openError(String msg, Throwable e) {
		IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, 0, msg, e);
		ILog logger = CodeGenerationActivator.getDefault().getLog();
		logger.log(status); 
		ErrorDialog.openError(null, "错误", "操作出错！", status);
	}
	
	public static void logError(String msg, Throwable e) {
		IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, 0, msg, e);
		ILog logger = CodeGenerationActivator.getDefault().getLog();
		logger.log(status); 
	}
	
	public static void logError(Throwable e) {
		logError(e.getMessage(), e);
	}
	public static void openMsg(Shell shell,String msg){
		org.eclipse.jface.dialogs.MessageDialog.openInformation(shell, "提示", msg);
	}
	
	public static void log(int severity, int code, String message, Throwable exception) {
		log(createStatus(severity, code, message, exception));
	}
	
	public static IStatus createStatus(int severity, int code, String message, Throwable exception) {
		return new Status(
			severity,
			CodeGenerationActivator.getDefault().getBundle().getSymbolicName(),
			code,
			message,
			exception);
	}
	
	public static void log(IStatus status) {
		CodeGenerationActivator.getDefault().getLog().log(status);
	}

}
