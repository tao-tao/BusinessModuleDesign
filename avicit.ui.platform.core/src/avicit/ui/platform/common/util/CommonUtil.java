package avicit.ui.platform.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * @author <a href="mailto: jhudson8@users.sourceforge.net">Joe Hudson</a>
 */
public class CommonUtil {

	/**
	 * Return the contents of the Stream as a String.
	 * Note:  If the InputStream represents a null String, the Java implementation will try to read from the stream for a certain amount of time
	 * before timing out.
	 * @param is the InputStream to transform into a String
	 * @return the String representation of the Stream
	 */
	public static String getStringFromStream (InputStream is)
		throws IOException
	{
		try {
			InputStreamReader reader = new InputStreamReader(is);
			char[] buffer = new char[1024];
			StringWriter writer = new StringWriter();
			int bytes_read;
			while ((bytes_read = reader.read(buffer)) != -1)
			{
				writer.write(buffer, 0, bytes_read);
			}
			return (writer.toString());
		}
		finally {
			if (null != is) is.close();
		}
	}
	
	public static String getStringFromStream (InputStream is,String charset)
	throws IOException
	{
		try {
			InputStreamReader reader = new InputStreamReader(is,charset);
			char[] buffer = new char[1024];
			StringWriter writer = new StringWriter();
			int bytes_read;
			while ((bytes_read = reader.read(buffer)) != -1)
			{
				writer.write(buffer, 0, bytes_read);
			}
			return (writer.toString());
		}
		finally {
			if (null != is) is.close();
		}
	}

	public static String firstLetterUpper (String s) {
		if (null == s) return null;
		else if (s.length() == 0) return s;
		else {
			return s.substring(0, 1).toUpperCase() + s.substring(1, s.length());
		}
	}

	public static String firstLetterLower (String s) {
		if (null == s) return null;
		else if (s.length() == 0) return s;
		else {
			return s.substring(0, 1).toLowerCase() + s.substring(1, s.length());
		}
	}

	public static String getClassPart (String fullClassName) {
		if (null == fullClassName) return null;
		int index = fullClassName.lastIndexOf(".");
		if (index == -1) return fullClassName;
		else return fullClassName.substring(index + 1, fullClassName.length());
	}
	
	private static final char[] vowelList = {'A', 'E', 'I', 'O', 'U'};
	public static String getPropName (String tableName) {
		if (tableName.toUpperCase().equals(tableName)) {
			boolean vowelsFound = false;
			for (int i=0; i<tableName.toCharArray().length; i++) {
				char c = tableName.toCharArray()[i];
				for (int j=0; j<vowelList.length; j++) {
					if (c == vowelList[j]) vowelsFound = true;
				}
			}
			if (vowelsFound) {
				tableName = tableName.toLowerCase();
			}
		}
		return getJavaNameCap(tableName);
	}
	
	public static String getJavaNameCap (String s) {
		if (s.indexOf("_") < 0 && s.indexOf("-") < 0) {
			return firstLetterUpper(s);
		}
		else {
			StringBuffer sb = new StringBuffer();
			boolean upper = true;
			char[] arr = s.toCharArray();
			for (int i=0; i<arr.length; i++) {
				if (arr[i] == '_' || arr[i] == '-') upper = true;
				else if (upper) {sb.append(Character.toUpperCase(arr[i])); upper = false;}
				else sb.append(Character.toLowerCase(arr[i]));
			}
			return sb.toString();
		}
	}

	public static String getJavaName (String s) {
		if (null == s) return null;
		else return firstLetterLower(getJavaNameCap(s));
	}

	public static String getPropDescription (String s) {
		if (null == s) return null;
		StringBuffer sb = new StringBuffer();
		boolean upper = true;
		char[] arr = s.toCharArray();
		for (int i=0; i<arr.length; i++) {
			if (i == 0) sb.append(Character.toUpperCase(arr[i]));
			else if (Character.isUpperCase(arr[i])) sb.append(" " + arr[i]);
			else sb.append(arr[i]);
		}
		return sb.toString();
	}
	
	public static String getPackagePart (String fullClassName) {
		if (null == fullClassName) return null;
		int index = fullClassName.lastIndexOf(".");
		if (index == -1) return "";
		else return fullClassName.substring(0, index);
	}
	
	public static String addPackageExtension (String packageStr, String extension) {
		if (null == packageStr || packageStr.trim().length() == 0) return extension;
		else return packageStr + "." + extension;
	}

	public static boolean deleteDir (File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		
		// The directory is now empty so delete it
		return dir.delete();
	}

	public static Object load(ClassLoader loader, String className) {
		try {
			return loader.loadClass(className).newInstance();
		}
		catch (Throwable t) {
			return null;
		}
	}

	public static void showError (String error, Shell shell) {
		MessageDialog.openError(
				shell,
				null,
				error);
	}
	
	public static void showError (String title, String error, Shell shell) {
		if (null == title) title = "An error has occured";
		if (null == shell) shell = new Shell();
		MessageDialog.openError(
				shell,
				title,
				error);
	}

	public static String getStaticName (String s) {
		boolean wasLastSpace = false;
		if (null == s) return null;
		if (s.toUpperCase().equals(s)) return s;
		else {
			StringBuffer sb = new StringBuffer();
			for (int i=0; i<s.toCharArray().length; i++) {
				char c = s.toCharArray()[i];
				if (Character.isLetterOrDigit(c)) {
					if (c == ' ' || c == '-') {
						sb.append("_");
						wasLastSpace = true;
					}
					else if (Character.isUpperCase(c) || i == 0) {
						if (sb.length() > 0 && !wasLastSpace) sb.append("_");
						sb.append(Character.toUpperCase(c));
						wasLastSpace = false;
					}
					else {
						sb.append(Character.toUpperCase(c));
						wasLastSpace = false;
					}
				}
				else if (Character.isWhitespace(c) || c == '.' || c == '-' || c == '_') {
					if (!wasLastSpace) {
						sb.append("_");
						wasLastSpace = true;
					}
				}
			}
			return sb.toString();
		}
	}

	/*
	public static void copyFile (File f1, File f2) throws IOException {
	    FileInputStream fis  = null;
	    FileOutputStream fos  = null;
	    try {
	    	if (!f2.exists()) f2.createNewFile();
	    	fis = new FileInputStream(f1);
	    	fos = new FileOutputStream(f2);
	    	byte[] buf = new byte[1024];
	    	int i = 0;
	    	while((i=fis.read(buf))!=-1) {
	    		fos.write(buf, 0, i);
	    	}
	    }
	    catch (IOException e) {
	    	Plugin.log("Can not copy " + f1.getAbsolutePath() + " to " + f2.getAbsolutePath());
	    	throw e;
	    }
	    finally {
	    	if (null != fis) fis.close();
	    	if (null != fos) fos.close();
	    }
	}
	*/

	/**
	 * Replace the contents of the from string with the contents of the to string in the base string
	 * @param base the string to replace part of
	 * @param from the string to be replaced
	 * @param to the string to replace
	 */
	public static String stringReplace( String base, String from, String to )
	{
		StringBuffer sb1 = new StringBuffer(base);
		StringBuffer sb2 = new StringBuffer(base.length() + 50);
		char[] f = from.toCharArray();
		char[] t = to.toCharArray();
		char[] test = new char[f.length];
		
		for (int x = 0; x < sb1.length(); x++) {
			
			if (x + test.length > sb1.length()) {
				sb2.append(sb1.charAt(x));
			} else {
				sb1.getChars(x, x + test.length, test, 0);
				if (aEquals(test, f)) {
					sb2.append(t);
					x = x + test.length - 1;		
				} else {
					sb2.append(sb1.charAt(x));
				}
			}
		}
		return sb2.toString();
	}
	
	static private boolean aEquals(char[] a, char[] b) {
		if (a.length != b.length) {
			return false;
		}
		for (int x = 0; x < a.length; x++) {
			if (a[x] != b[x]) {
				return false;
			}	
		}
		return true;	
	}
	
}