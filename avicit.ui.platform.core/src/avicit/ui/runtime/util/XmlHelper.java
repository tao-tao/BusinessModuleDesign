package avicit.ui.runtime.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class XmlHelper {

	public static List getChildElementsByName(org.w3c.dom.Node parent, String name) {
		List result = new ArrayList();
		if(parent != null)
		{
			NodeList list = parent.getChildNodes();
			for(int i=0; i<list.getLength() ; i++)
			{
				org.w3c.dom.Node child = list.item(i);
				if(child instanceof Text)
					continue;
				if(child.getNodeName().equalsIgnoreCase(name))
				{
					result.add(child);
				}
			}
		}
		return result;
	}

	public static org.w3c.dom.Node createOrGetChildElement(org.w3c.dom.Node parent, String name) {
		if(parent == null)
			return null;
		NodeList list = parent.getChildNodes();
		for(int i=0; i<list.getLength() ; i++)
		{
			org.w3c.dom.Node child = list.item(i);
			if(child.getNodeName().equalsIgnoreCase(name))
			{
				return child;
			}
		}
		org.w3c.dom.Node child = parent.getOwnerDocument().createElement(name);
		parent.appendChild(child);
		return child;
	}

	public static org.w3c.dom.Node createChildElement(org.w3c.dom.Node parent, String name) {
		if(parent == null)
			return null;
		org.w3c.dom.Node child = parent.getOwnerDocument().createElement(name);
		parent.appendChild(child);
		child.normalize();
		return child;
	}

	
	public static org.w3c.dom.Node getChildElement(org.w3c.dom.Node parent, String name) {
		if(parent == null)
			return null;
		NodeList list = parent.getChildNodes();
		for(int i=0; i<list.getLength() ; i++)
		{
			org.w3c.dom.Node child = list.item(i);
			if(child.getNodeName().equalsIgnoreCase(name))
			{
				return child;
			}
		}
		return null;
	}
	
	public static org.w3c.dom.Node getChildElementByName(org.w3c.dom.Node parent, String name, String attname, String attvalue) {
		if(parent == null)
			return null;
		NodeList list = parent.getChildNodes();
		for(int i=0; i<list.getLength() ; i++)
		{
			org.w3c.dom.Node child = list.item(i);
			if(child.getNodeName().equalsIgnoreCase(name))
			{
				NamedNodeMap attrs = child.getAttributes();
				org.w3c.dom.Node att = attrs.getNamedItem(attname);
				if( att!= null && att.getNodeValue().equalsIgnoreCase(attvalue))
				{
					return child;
				}				
			}
		}
		return null;
	}

	public static org.w3c.dom.Node getChildElementByChildText(org.w3c.dom.Node parent, String name, String childname, String childvalue) {
		if(parent == null)
			return null;
		NodeList list = parent.getChildNodes();
		for(int i=0; i<list.getLength() ; i++)
		{
			org.w3c.dom.Node child = list.item(i);
			if(child.getNodeName().equalsIgnoreCase(name))
			{
				Node desendent = XmlHelper.getChildElement(child,childname);
				if( desendent!= null && childvalue.equals(XmlHelper.getElementText(desendent)))
				{
					return child;
				}				
			}
		}
		return null;
	}

	public static org.w3c.dom.Node getNodeAttribute(org.w3c.dom.Node parent, String name) {
		Node node = parent.getAttributes().getNamedItem(name);
		return node;
	}

	public static void setElementValueByName(org.w3c.dom.Node parent, String elementName, String value)
	{
		org.w3c.dom.Node node =  XmlHelper.getChildElement(parent, elementName);
		if(node == null)
		{
			org.w3c.dom.Element element = parent.getOwnerDocument().createElement(elementName);
			setElementText(element, value);
			parent.appendChild(element);
		}
		else
		{
			setElementText(node, value);
		}
	}
	
	public static String getElementText(org.w3c.dom.Node parent)
	{
		if(parent == null)
			return null;
		
		NodeList list = parent.getChildNodes();
		for(int i=0; i<list.getLength() ; i++)
		{
			org.w3c.dom.Node child = list.item(i);
			if(child instanceof Text)
			{
				String text = child.getNodeValue();
				if(!text.trim().equalsIgnoreCase(""))
					return text.trim();
			}
		}
		return "";
	}
	
	public static void setElementText(org.w3c.dom.Node parent, String text)
	{
		boolean isSet = false;
		NodeList list = parent.getChildNodes();
		for(int i=0; i<list.getLength() ; i++)
		{
			org.w3c.dom.Node child = list.item(i);
			if(child instanceof Text)
			{
				isSet = true;
				child.setNodeValue(text);
				break;
			}			
		}
		if(!isSet)
		{
			org.w3c.dom.Node textNode = parent.getOwnerDocument().createTextNode(text);
			parent.appendChild(textNode);
		}
	}
	
	public static void setElementAtt(org.w3c.dom.Node parent, String name, String value) {
		if(parent == null)
			return;
		org.w3c.dom.Node node = parent.getAttributes().getNamedItem(name);
		if (node == null) {
			node = parent.getOwnerDocument().createAttribute(name);
			parent.getAttributes().setNamedItem(node);
		}
		node.setNodeValue(value);
	}
	
	public static	Document openXmlConfig(String strXmlFileName) {		
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

			// Set an ErrorHandler before parsing

//			db.setErrorHandler(new ErrorHandler(System.err));

			// Step 3: parse the input file
			doc = db.parse(new File(strXmlFileName));
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
			if (dbf != null)
				dbf = null;
			if (db != null)
				db = null;
		}

		return doc;
	}
	
	public static void setDataElementText(org.w3c.dom.Node parent, String text) {
		boolean isSet = false;
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			org.w3c.dom.Node child = list.item(i);
			if (child instanceof Text) {
				if(((Text)child).getNodeType() == Text.CDATA_SECTION_NODE)
				{
					isSet = true;
					child.setNodeValue(text);
					break;
				}
				else
				{
					parent.removeChild(child);
				}
			}
		}
		if (!isSet) {
			Text textNode = parent.getOwnerDocument().createCDATASection(text);
			parent.appendChild(textNode);
		}
	}

}
