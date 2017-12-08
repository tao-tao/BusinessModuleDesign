package com.tansun.runtime.resource.adapter;

import org.eclipse.core.resources.IFile;

import avicit.ui.platform.common.data.XmlParsedFile;
import avicit.ui.runtime.core.node.BpelNode;

public class BpelWorkbenchAdapter extends AbstractModelWorkbenchAdapter {

	private BpelWorkbenchAdapter() {
	}

	public static BpelWorkbenchAdapter getInstance() {
		return instance;
	}

	public String getLabel(Object object) {
		BpelNode node = (BpelNode) object;
		return node.getDisplayName();
	}

	public Object[] getChildren(Object object)
    {
    	/*BpelNode node = (BpelNode)object;
        IResource pagex = node.getFile().getResource();
        String fname = pagex.getName();
        if(fname.lastIndexOf(".")>0)
        	fname = fname.substring(0,fname.lastIndexOf("."));
        IFile javaFile = pagex.getParent().getFile(new Path(fname + ".composite"));
        if(!javaFile.exists())
        {
        	String[] ids = getNameSpace((IFile) pagex);
        	try {
				javaFile.create(new ByteArrayInputStream(getInitContent(ids[0],ids[1]).getBytes()),true,null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
        }
        return new Object[]{javaFile};
        */
		return new Object[0];
    }

	private String[] getNameSpace(IFile file) {
		String[] ret = new String[2];
		XmlParsedFile xmlF = new XmlParsedFile(file);
		ret[0] = xmlF.getEle().getAttribute("name");
		ret[1] = xmlF.getEle().getAttribute("targetNamespace");
		xmlF.close();
		return ret;
	}

	private String getInitContent(String id, String namespace) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n").append("<composite xmlns=\"http://www.osoa.org/xmlns/sca/1.0\"\n").append("targetNamespace=\"http://" + id + "\"\n").append("xmlns:hns=\"" + namespace + "\"\n").append("name=\"" + id + "\">\n").append("<component name=\"" + id + "Component\">\n").append("<implementation.bpel process=\"hns:" + id + "\"/>\n").append("</component>\n").append("</composite>");
		return buffer.toString();
	}

	@Override
	public boolean hasChildren(Object element) {
		return false;
	}

	private static final BpelWorkbenchAdapter instance = new BpelWorkbenchAdapter();

}