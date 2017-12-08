package avicit.platform6.tools.codegeneration.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
/**
 * <p>金航数码科技有限责任公司</p>
 * <p>作者：dingrc</p>
 * <p>邮箱：dingrc@avicit.com</p>
 * <p>创建时间：2012-12-10</p>
 *
 * <p>类说明：MyEntityResolver,从老代码生成器拿来</p>
 * <p>修改记录：</p>
 */
public class MyEntityResolver implements EntityResolver {

	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {
		if (systemId == null) {
			return null;
		}

		try {
			URL url = new URL(systemId);
			String file = url.getFile();

			if ((file != null) && (file.indexOf('/') > -1)) {
				file = file.substring(file.lastIndexOf('/') + 1);
			}

			if ("hibernate.sourceforge.net".equals(url.getHost())
					&& systemId.endsWith(".dtd")) {
				InputStream is = getClass().getResourceAsStream(
						"/META-INF/" + file);

				if (is == null) {
					is = getClass().getResourceAsStream('/' + file);
				}

				if (is != null) {
					return new InputSource(is);
				}
			}
		}
		catch (MalformedURLException e) {
			InputStream is = getClass().getResourceAsStream(
					"/META-INF/" + systemId);
			if (is == null) {
				is = getClass().getResourceAsStream('/' + systemId);
			}
			if (is != null) {
				return new InputSource(is);
			}
		}
		return null;
	}
}
