package avicit.ui.platform.common.util.transformer;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


public class XMLChildElementsTransformer extends AbstractTransformer {
	Element pelement;
	String elementName;

	public XMLChildElementsTransformer(String propName, Element parentElement,
			String elementName) {
		super(propName);
		this.pelement = parentElement;
		this.elementName = elementName;
	}
	
	private static String getElementText(org.w3c.dom.Node parent) {
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

	public Object getPropValue() {
		
		Node enode = crOrGet(pelement, this.elementName);
		return getElementText(enode);

	}
	
	private static void setElementText(org.w3c.dom.Node node, String text) {
		boolean isSet = false;
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			org.w3c.dom.Node child = list.item(i);
			if (child instanceof Text) {
				isSet = true;
				child.setNodeValue(text);
				break;
			}
		}
		if (!isSet) {
			org.w3c.dom.Node textNode = node.getOwnerDocument()
					.createTextNode(text);
			node.appendChild(textNode);
		}
	}
	
	private static org.w3c.dom.Node crOrGet(org.w3c.dom.Node parent, String name) {
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

	
	public void setPropValue(Object value) {
		Node enode = crOrGet(pelement, this.elementName);
		setElementText(enode, (String) value);

	}

}
