package avicit.ui.runtime.core.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import avicit.ui.common.util.XmlHelp;
import avicit.ui.common.util.XmlHelper;
import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.platform.common.data.XmlParsedFile;
import avicit.ui.view.exception.OpenServiceConfigAction;
import avicit.ui.view.module.ServiceModelFactory;

public class SpringNode extends AbstractFileNode {
	IFileDelegate folder = null;

	public SpringNode(IFileDelegate folder) {
		super(folder);
		this.folder = folder;
		setOrder(2);
	}

	public Object getModel() {
		return this.getFile();
	}

	public List getModelChildren() {

		XmlParsedFile internalFile = new XmlParsedFile((IFile) this
				.getResource().getResource());

		List attNodeList = null;
		Map attMap = new HashMap();
		try {
			
			StringBuffer stringBuffer = new StringBuffer(internalFile.getFile().getLocation().toString());
			int index = stringBuffer.lastIndexOf(".spring.xml");
			if(index != -1){
				stringBuffer.delete(index, stringBuffer.length());
			}
			stringBuffer.append(".att.xml");
			//String p = "2iui/META-INF/spring/sgsd/safsagf.att.xml";
			//IFile file = (IFile) folder.getProject().getFile(p).getResource();
			//String url = file.getLocation().toString();
			Document doc = XmlHelper.openXmlConfig(stringBuffer.toString());
			List rootList = XmlHelper.getChildElementsByName(doc, "root");
			if (rootList != null || rootList.size() > 1) {
				org.w3c.dom.Node node = (Node) rootList.get(0);
				attNodeList = XmlHelper.getChildElementsByName(node, "node");
				for (int i = 0; i < attNodeList.size(); i++) {

					org.w3c.dom.Node att = (Node) attNodeList.get(i);
					org.w3c.dom.Node id = XmlHelp.getNodeAttribute(att, "id");
					org.w3c.dom.Node inf = XmlHelp.getNodeAttribute(att,
							"interface");
					if (id != null && inf != null) {
						attMap.put(id.getNodeValue(), inf.getNodeValue());
					}
				}
			}
		} catch (Exception e) {
		}
		List all = new ArrayList();
		if (internalFile != null) {
			List children = XmlHelp.getChildElementsByName(
					internalFile.getEle(), "bean");
			for (int i = 0; i < children.size(); i++) {
				org.w3c.dom.Node node = (Node) children.get(i);
				org.w3c.dom.Node att = XmlHelp.getNodeAttribute(node, "id");
				if (att != null) {
					JavaBeanNode bean = new JavaBeanNode();
					bean.setParent(this);
					bean.setType(this.getType());
					bean.setId(att.getNodeValue());
					if (attMap.get(att.getNodeValue()) != null) {
						String str = (String) attMap.get(att.getNodeValue());
						bean.setInf(str);
					}else{
						bean.setInf("");
					}
					// System.out.println(bean.getName());
					// att = XmlHelp.getNodeAttribute(node, "name");
					// if(att != null)
					// bean.setName(att.getNodeValue());
					att = XmlHelp.getNodeAttribute(node, "class");
					if (att != null)
						bean.setClazz(att.getNodeValue());
					// att = XmlHelp.getNodeAttribute(node, "id");

					all.add(bean);
				}
			}
		}
		internalFile.close();
		return all;
	}

	@Override
	public String getDisplayName() {
		String name = this.getFile().getName();
		int dotIndex = name.indexOf(".");
		if (dotIndex > 0)
			name = name.substring(0, dotIndex);
		return name;
	}

	public String getType() {
		return ServiceModelFactory.TYPE;
	}

	public Object getAdapter(Class adapter) {
		if (IResourceDelegate.class == adapter)
			return getResource();
		if (IProject.class == adapter) {
			try {
				return getResource().getProject().getAdapter(adapter);
			} catch (ResourceException e) {
				return null;
			}
		}
		if (IDoubleClickListener.class == adapter) {
			return new OpenServiceConfigAction();
		} else {
			return super.getAdapter(adapter);
		}
	}
}