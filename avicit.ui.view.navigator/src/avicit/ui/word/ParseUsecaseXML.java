package avicit.ui.word;

import java.io.File;
import java.net.MalformedURLException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParseUsecaseXML {

	public void parse(File file, UsecaseModelInfo info){
			try {

				File file1 = new File("D:/IDE/Test/src/avicit/test/META-INF/requirement/usercase/newfile.ucd");
				SAXReader reader = new SAXReader();

				Document doc;

				doc = reader.read(file1);

				Element root = doc.getRootElement();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
