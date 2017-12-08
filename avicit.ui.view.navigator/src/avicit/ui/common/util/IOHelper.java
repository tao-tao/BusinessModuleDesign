package avicit.ui.common.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class IOHelper {

	public static String getFileString(File file, String code) {
		Reader re1;
		try {
			re1 = new InputStreamReader(new FileInputStream(file.getPath()), code);
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
		Reader re = re1;
		StringBuilder sb = new StringBuilder();
		
		try {
			char[] buffer = new char[1024];
			while (true) {
				int count = re.read(buffer);
				if (count == -1) {
					break;
				}
				char[] da = new char[count];
		
				System.arraycopy(buffer, 0, da, 0, count);
				sb.append(da);
		
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (re != null) {
				try {
					re.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String str1 = sb.toString();
		
		/*
		 * 在读取某些文件的时候,会在字符串头部生成空格.比如用utf-8编码方式读取头部有"0xFFFE"字节(BOM)的文本文件时.
		 * 出现这个问题,可能是因为utf-8标准中不需要bom,但事实上大部分utf8文件都有bom.但似乎java
		 * reader没有进行足够良好的兼容处理. 一个空格本来不是什么大问题.但是一些xml
		 * parser却不允许prolog以前有任何内容,包括空格,否则抛出异常.
		 * 
		 */
		while (!str1.startsWith("<") && str1.length() > 0) {
			str1 = str1.substring(1);
		}
		String str = str1;
		return str;
	}

}
