package avicit.ui.common.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.swt.widgets.Table;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

/**
 * 
 */
/**
 * 
 */
public class XmlHelp {

	public static org.w3c.dom.Node cog(org.w3c.dom.Node parent, String name) {
		return createOrGetChildElement(parent, name);
	}

	public static org.w3c.dom.Node createChildElement(org.w3c.dom.Node parent, String name) {
		if (parent == null)
			return null;
		org.w3c.dom.Node child = parent.getOwnerDocument().createElement(name);
		parent.appendChild(child);
		return child;
	}

	public static org.w3c.dom.Node createOrGetChildElement(org.w3c.dom.Node parent, String name) {
		if (parent == null)
			return null;
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			org.w3c.dom.Node child = list.item(i);
			if (child.getNodeName().equalsIgnoreCase(name)) {
				return child;
			}
		}
		org.w3c.dom.Node child = parent.getOwnerDocument().createElement(name);
		parent.appendChild(child);
		return child;
	}

	public static org.w3c.dom.Node getChildElement(org.w3c.dom.Node parent, String name) {
		if (parent == null)
			return null;
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			org.w3c.dom.Node child = list.item(i);
			if (child.getNodeName().equalsIgnoreCase(name)) {
				return child;
			}
		}
		return null;
	}

	public static org.w3c.dom.Node getChildElementByName(org.w3c.dom.Node parent, String name, String attname) {
		if (parent == null)
			return null;
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			org.w3c.dom.Node child = list.item(i);
			if (child.getNodeName().equalsIgnoreCase(name)) {
				NamedNodeMap attrs = child.getAttributes();
				org.w3c.dom.Node att = attrs.getNamedItem("Name");
				if (att != null && att.getNodeValue().equalsIgnoreCase(attname)) {
					return child;
				}
			}
		}
		return null;
	}

	public static org.w3c.dom.Node getChildElementByName(org.w3c.dom.Node parent, String name, String attName, String attvalue) {
		if (parent == null)
			return null;
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			org.w3c.dom.Node child = list.item(i);
			if (child.getNodeName().equalsIgnoreCase(name)) {
				NamedNodeMap attrs = child.getAttributes();
				org.w3c.dom.Node att = attrs.getNamedItem(attName);
				if (att != null && att.getNodeValue().equalsIgnoreCase(attvalue)) {
					return child;
				}
			}
		}
		return null;
	}

	public static org.w3c.dom.Node createOrGetChildElementByName(org.w3c.dom.Node parent, String name, String attName, String attvalue) {
		if (parent == null)
			return null;
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			org.w3c.dom.Node child = list.item(i);
			if (child.getNodeName().equalsIgnoreCase(name)) {
				NamedNodeMap attrs = child.getAttributes();
				org.w3c.dom.Node att = attrs.getNamedItem(attName);
				if (att != null && att.getNodeValue().equalsIgnoreCase(attvalue)) {
					return child;
				}
			}
		}
		Node child = parent.getOwnerDocument().createElement(name);
		org.w3c.dom.Node att = child.getOwnerDocument().createAttribute(attName);
		att.setNodeValue(attvalue);
		child.getAttributes().setNamedItem(att);
		parent.appendChild(child);
		return child;
	}

	public static List getChildElementsByName(org.w3c.dom.Node parent, String name) {
		List result = new ArrayList();
		if (parent != null) {
			NodeList list = parent.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				org.w3c.dom.Node child = list.item(i);
				if (child instanceof Text)
					continue;
				if (child.getNodeName().equalsIgnoreCase(name)) {
					result.add(child);
				}
			}
		}
		return result;
	}

	public static List getChildElements(org.w3c.dom.Node parent) {
		List result = new ArrayList();
		if (parent != null) {
			NodeList list = parent.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				org.w3c.dom.Node child = list.item(i);
				if (child instanceof Text)
					continue;
				result.add(child);
			}
		}
		return result;
	}

	public static String getElementText(org.w3c.dom.Node parent) {
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			org.w3c.dom.Node child = list.item(i);
			if (child instanceof Text) {
				String text = child.getNodeValue();
				if (!text.trim().equalsIgnoreCase(""))
					return text.trim();
			}
		}
		return "";
	}

	public static org.w3c.dom.Node getNodeAttribute(org.w3c.dom.Node parent, String name) {
		org.w3c.dom.Node node = parent.getAttributes().getNamedItem(name);
		return node;
	}

	public static org.w3c.dom.Node createOrGetNodeAttribute(org.w3c.dom.Node parent, String name) {
		org.w3c.dom.Node node = parent.getAttributes().getNamedItem(name);
		if (node == null) {
			node = parent.getOwnerDocument().createAttribute(name);
			parent.getAttributes().setNamedItem(node);
		}
		return node;
	}

	public static void setElementText(org.w3c.dom.Node parent, String text) {
		boolean isSet = false;
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			org.w3c.dom.Node child = list.item(i);
			if (child instanceof Text) {
				isSet = true;
				child.setNodeValue(text);
				break;
			}
		}
		if (!isSet) {
			org.w3c.dom.Node textNode = (IDOMNode) parent.getOwnerDocument().createTextNode(text);
			parent.appendChild(textNode);
		}
	}

	public static void setDataElementText(org.w3c.dom.Node parent, String text) {
		boolean isSet = false;
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			org.w3c.dom.Node child = list.item(i);
			if (child instanceof Text) {
				if (((Text) child).getNodeType() == Text.CDATA_SECTION_NODE) {
					isSet = true;
					child.setNodeValue(text);
					break;
				}
			}
		}
		if (!isSet) {
			Text textNode = parent.getOwnerDocument().createCDATASection(text);
			parent.appendChild(textNode);
		}
	}

	public static void setElementValueByName(org.w3c.dom.Node parent, String elementName, String value) {
		org.w3c.dom.Node node = XmlHelp.getChildElement(parent, elementName);
		if (node == null) {
			org.w3c.dom.Element element = parent.getOwnerDocument().createElement(elementName);
			setElementText(element, value);
			parent.appendChild(element);
		} else {
			setElementText(node, value);
		}
	}

	public static String getSpringPropertyValue(Node data, String propName) {
		Node property = XmlHelp.getChildElementByName(data, "property", "name", propName);
		if (property != null) {
			Node child = XmlHelp.getChildElement(property, "value");
			if (child != null)
				return XmlHelp.getElementText(child);
		}
		return "";
	}

	public static String getSpringPropertyAttValue(Node data, String attName, String propName) {
		Node property = XmlHelp.getChildElementByName(data, "property", "name", propName);
		if (property != null) {
			Node child = XmlHelp.getNodeAttribute(property, attName);
			if (child != null)
				return child.getNodeValue();
		}
		return "";
	}

	public static Node getSpringProperty(Node data, String propName) {
		Node property = XmlHelp.getChildElementByName(data, "property", "name", propName);
		if (property != null) {
			return property;
		}
		return null;
	}

	public static String getSpringPropertyRefValue(Node data, String propName) {
		Node property = XmlHelp.getChildElementByName(data, "property", "name", propName);
		if (property != null) {
			Node child = XmlHelp.getChildElement(property, "ref");
			if (child != null)
			{
				Node bean = child.getAttributes().getNamedItem("bean");
				if(bean != null)
					return bean.getNodeValue();
			}
		}
		return "";
	}

	public static void setSpringPropertyValue(Node data, String propName, String value) {
		Node property = XmlHelp.getChildElementByName(data, "property", "name", propName);
		if (property == null)
			property = XmlHelp.createOrGetChildElementByName(data, "property", "name", propName);

		if (property != null) {
			Node child = XmlHelp.createOrGetChildElement(property, "value");
			if (child != null) {
				XmlHelp.setElementText(child, value);
			}
		}
	}

	public static void setSpringPropertyAttValue(Node data, String propName, String attName, String value) {
		Node property = XmlHelp.getChildElementByName(data, "property", "name", propName);
		if (property == null)
			property = XmlHelp.createOrGetChildElementByName(data, "property", "name", propName);

		if (property != null) {
			Node child = XmlHelp.createOrGetNodeAttribute(property, attName);
			if (child != null)
				child.setNodeValue(value);
		}
	}

	public static void removeSpringPropertyAttValue(Node data, String propName, String attName) {
		Node property = XmlHelp.getChildElementByName(data, "property", "name", propName);
		if (property != null && property.getAttributes().getNamedItem(attName) != null) {
			property.getAttributes().removeNamedItem(attName);
		}
	}

	public static void setSpringPropertyDATAValue(Node data, String propName, String value) {
		Node property = XmlHelp.getChildElementByName(data, "property", "name", propName);
		if (property == null)
			property = XmlHelp.createOrGetChildElementByName(data, "property", "name", propName);

		if (property != null) {
			Node child = XmlHelp.createOrGetChildElement(property, "value");
			if (child != null) {
				XmlHelp.setDataElementText(child, value);
			}
		}
	}

	public static String getNodeAttributeValue(org.w3c.dom.Node parent, String name, String defaultValue) {
		org.w3c.dom.Node node = parent.getAttributes().getNamedItem(name);
		if(node != null)
			return node.getNodeValue();
		return defaultValue;
	}

	public static void setElementAtt(org.w3c.dom.Node parent, String name, String value) {
		if (parent == null)
			return;
		org.w3c.dom.Node node = parent.getAttributes().getNamedItem(name);
		if(value == null && node != null)
		{
			parent.getAttributes().removeNamedItem(name);
		} else if (value != null) {
			if(node == null)
			{
				node = parent.getOwnerDocument().createAttribute(name);
				parent.getAttributes().setNamedItem(node);
			}
			node.setNodeValue(value);
		}
	}

	/**
	 * 似乎不支持序列化
	 * 
	 * @param doc
	 */
	public static void testDomSerilizer(Document doc) {

		try {
			DOMImplementationRegistry reg = DOMImplementationRegistry.newInstance();

			DOMImplementationLS impl = (DOMImplementationLS) reg.getDOMImplementation("LS");

			LSSerializer writer = impl.createLSSerializer();
			String str = writer.writeToString(doc);
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static String getColumntext(Table table, int index) {
		return table.getColumn(index).getText();
	}
	public static	Document openXmlConfig(InputStream inputStrem) {		
		// Step 1: create a DocumentBuilderFactory and configure it
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		Document doc = null;

		dbf.setNamespaceAware(true);

		dbf.setValidating(false);

		dbf.setIgnoringComments(true);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setCoalescing(true);
		// The opposite of creating entity ref nodes is expanding them inline
		dbf.setExpandEntityReferences(false);
		try {

			db = dbf.newDocumentBuilder();

			doc = db.parse(inputStrem);
		} catch (SAXException saxError) {
			doc = null;
			System.out.println("SAXException:" + saxError.getMessage());
		} catch (ParserConfigurationException parserError) {
			doc = null;
			System.out.println("ParserConfigurationException:"
					+ parserError.getMessage());
		} catch (IOException ioError) {
			doc = null;
			System.out.println("IOException:" + ioError.getMessage());
		} finally {
			if (dbf != null){
				dbf = null;
			}
			if (db != null){
				db = null;
			}
			if(inputStrem != null){
				try {
					inputStrem.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return doc;
	}
}
