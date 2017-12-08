package avicit.ui.runtime.core.node;


import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;

public class DesignerContentProvider {

	public static final DesignerContentProvider INSTANCE = new DesignerContentProvider();

	// Reading the stuff
	public FileEditorInput getGpdEditorInput(IEditorInput input) {
		return (FileEditorInput) input;
	}

	public static String createInitialPageX(String proId, String proName) throws JavaModelException {
		int pos = proId.indexOf(".");
		String processId = proId;
		if (pos >= 0) {
			processId = proId.substring(0, pos);
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"gbk\"?>");
		buffer.append("\n");
		//xyz update 初始化时，高度增大以便显示出名称 50-->65
		buffer.append("<process" + " id=\"" + processId + "\" name=\"" + proName + "\"><start id=\"start\" name=\"start\" x=\"0\" y=\"100\" w=\"85\" h=\"65\"></start>" + "<end id=\"end\" name=\"end\" x=\"480\" y=\"100\" w=\"85\" h=\"65\"></end>" + "</process>");

		return buffer.toString();
	}

	public static String createInitialBiz(String proId, String proName) throws JavaModelException {
		int pos = proId.indexOf(".");
		String processId = proId;
		if (pos >= 0) {
			processId = proId.substring(0, pos);
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"gbk\"?>");
		buffer.append("\n");
		buffer.append("<process" + " Id=\"" + processId + "\" Name=\"" + proName + "\"><start id=\"start\" name=\"start\" x=\"0\" y=\"100\" w=\"85\" h=\"65\"></start>" + "<end id=\"end\" name=\"end\" x=\"480\" y=\"100\" w=\"85\" h=\"65\"></end>" + "</process>");

		return buffer.toString();
	}

	public static String createInitialSQLMap(String proId, String proName) throws JavaModelException {
		int pos = proId.indexOf(".");
		String processId = proId;
		if (pos >= 0) {
			processId = proId.substring(0, pos);
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"gbk\"?>");
		buffer.append("\n");
		buffer.append("<!DOCTYPE sqlMap PUBLIC \"-//iBATIS.com//DTD SQL MAP 2.0//EN\" "
					+ "\"http://ibatis.apache.org/dtd/sql-map-2.dtd\">"
					+ "<sqlMap namespace=\""+ processId + "\">"
					+ "<sql id=\"queryCountHeader\">"
					+ "<![CDATA[select count(*) as thecount from (]]>"
					+ "</sql>"
					+ "<sql id=\"queryCountFooter\"><![CDATA[)]]>"
					+ "</sql>"
					+ "<sql id=\"queryHeader\">select * from (select innerresult.*, rownum innerindex from (</sql>"
					+ "<sql id=\"queryFooter\" ><![CDATA[) innerresult) where innerindex > #begin# and innerindex <= #end#]]>"
					+ "</sql></sqlMap>");

		return buffer.toString();
	}

	public static String createInitialServiceConfig() throws JavaModelException {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buffer.append("\n");
		buffer.append("<beans xmlns=\"http://www.springframework.org/schema/beans\" xmlns:sca=\"http://www.springframework.org/schema/sca\" xmlns:ec=\"http://www.tansun.com/schema/ec\" xmlns:aop=\"http://www.springframework.org/schema/aop\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:p=\"http://www.springframework.org/schema/p\" xmlns:context=\"http://www.springframework.org/schema/context\" xmlns:tx=\"http://www.springframework.org/schema/tx\" \n"
				+ "xsi:schemaLocation=\"http://www.springframework.org/schema/beans\n" 
				+ "http://www.springframework.org/schema/beans/spring-beans-2.5.xsd\n"
				+ "http://www.springframework.org/schema/context\n"
				+ "http://www.springframework.org/schema/context/spring-context-2.5.xsd\n"
				+ "http://www.springframework.org/schema/aop\n"
				+ "http://www.springframework.org/schema/aop/spring-aop-2.5.xsd\n"
				+ "http://www.springframework.org/schema/tx\n"
				+ "http://www.springframework.org/schema/tx/spring-tx.xsd\n"
				+ "http://www.tansun.com/schema/ec\n" 
				+ "http://www.tansun.com/schema/ec.xsd\n" 
				+ "http://www.springframework.org/schema/sca http://www.osoa.org/xmlns/sca/1.0/spring-sca.xsd\">\n" 
				+ "</beans>");

		return buffer.toString();
	}
	/**
	 * 
	 * @方法名称    ：createInitialTld
	 * @功能描述    ：
	 * @see class #method： (指明相关的方法名，如查询某一个客户的资料信息，
	 * 可能根据客户号查询或客户名称查询，则这两个方
	 * 法名可认为是相关的，可在此罗列出）
	 * @逻辑描述    ：
	 * @param   ：无
	 * @return  ：String
	 * @throws  ：无
	 * @since   ：Ver 1.00
	 */
	public static String createInitialTld(String proId, String proName) throws JavaModelException {
		int pos = proId.indexOf(".");
		String processId = proId;
		if (pos >= 0) {
			processId = proId.substring(0, pos);
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"gbk\"?>");
		buffer.append("\n");
		buffer.append("<process" + " Id=\"" + processId + "\" Name=\"" + proName + "\"><start id=\"start\" name=\"start\" x=\"0\" y=\"100\" w=\"85\" h=\"50\"></start>" + "<end id=\"end\" name=\"end\" x=\"480\" y=\"100\" w=\"85\" h=\"50\"></end>" + "</process>");

		return buffer.toString();
	}

}
