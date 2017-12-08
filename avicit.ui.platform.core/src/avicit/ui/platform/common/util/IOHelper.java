package avicit.ui.platform.common.util;

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
		 * �ڶ�ȡĳЩ�ļ���ʱ��,�����ַ�ͷ����ɿո�.������utf-8���뷽ʽ��ȡͷ����"0xFFFE"�ֽ�(BOM)���ı��ļ�ʱ.
		 * �����������,��������Ϊutf-8��׼�в���Ҫbom,����ʵ�ϴ󲿷�utf8�ļ�����bom.���ƺ�java
		 * readerû�н����㹻���õļ��ݴ���. һ���ո�������ʲô������.����һЩxml
		 * parserȴ������prolog��ǰ���κ�����,�����ո�,�����׳��쳣.
		 * 
		 */
		while (!str1.startsWith("<") && str1.length() > 0) {
			str1 = str1.substring(1);
		}
		String str = str1;
		return str;
	}

}
