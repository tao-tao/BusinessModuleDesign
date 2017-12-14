/** *********Explanation to this code: *************************************************************************
These 100 lines codes are abstracted from XMLUtil.java from one of my latest projects: BusinessModuleDesign.
BusinessModuleDesign is an open source Business Model designer, user can drag and drop the graph in this designer to build their business models and the whole project source code can be found from the URL: https://github.com/tao-tao/BusinessModuleDesign . The business module designer is written in Java and XML based on Eclipse SWT/JFace, EMF and GEF. The reason why I choose these codes is because they're more complicated than the experimental codes, and these codes can be re-used in different business platforms and experimental environments as well. And these 100 codes can completely provide a suit of functions in XML file parsing, formatting, reading and are well formatted and easy-to-understand with added annotations to show my quality of code writing in Java.
****************************************************************************************************************
*/

package avicit.platform6.tools.codegeneration.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

@SuppressWarnings("unchecked")
public final class XMLUtil {

    //Format XML file
    public static String formatXml(String str) throws Exception {
		  Document document = null;
		  document = DocumentHelper.parseText(str.trim());
		  OutputFormat format = OutputFormat.createPrettyPrint();
		  format.setEncoding("UTF-8");
		  StringWriter writer = new StringWriter();
		  XMLWriter xmlWriter = new XMLWriter(writer, format);
		  xmlWriter.write(document);
		  xmlWriter.close();
		  return writer.toString();
		 }

    //Read XML file from a file name.
    public static Document loadXmlFile(String filePath) throws DocumentException {
        return loadXmlFile(new File(filePath));
    }

    public static Document loadXmlFile(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(file);
    }

    //Parse the entire xml files and save the search result in a list.
    public static void parseXML(String searchName, Element element, List results) throws Exception{
		if (element != null && !element.elements().isEmpty()) {
			for (Iterator it = element.elements().iterator(); it.hasNext();) {
				Object child = it.next();
				if (child instanceof Element) {
					Element ele = (Element) child;
					if (ele != null && !ele.elements().isEmpty()) {
						List cache = results;    //Cache the result to save the calculation times.
						parseXML(searchName, ele, results);
						if (results.size() == cache.size()) {
							continue;
						}
					} else if (ele.getName().equals(searchName)) {
						results.add(ele);
					} else {
						continue;
					}
				}
			}
		}
	}

    //Save xml file with format into a filePath
    public static boolean saveXml(String filePath, Document document, OutputFormat format) throws IOException {
        return saveXml(new File(filePath), document, format);
    }

    public static boolean saveXml(File file, Document document, OutputFormat format) throws IOException {
        FileOutputStream fos = null;
        XMLWriter xmlWriter = null;
        try {
            file.getParentFile().mkdirs();
            fos = new FileOutputStream(file);
            xmlWriter = new XMLWriter(fos, format);
            xmlWriter.write(document);
            return true;
        } finally {
        	if(xmlWriter != null) {
        		xmlWriter.close();
        	}
        	if(fos != null) {
        		fos.close();
        	}
        }
    }

    //Add attribute to the file.
    public static void setAttribute(Element element, String name, String value) {
        Attribute attribute = element.attribute(name);
        if (attribute != null) {
            attribute.setValue(value);
        } else {
            element.addAttribute(name, value);
        }
    }

    //Set Text to the file element.
    public static void setText(Element element, String value) {
        element.setText(value);
    }
}