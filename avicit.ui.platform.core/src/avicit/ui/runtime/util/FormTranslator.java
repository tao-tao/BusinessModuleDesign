package avicit.ui.runtime.util;

import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import avicit.ui.platform.common.data.EventAnno;
import avicit.ui.platform.common.util.WorkspaceUtil;

import com.ec.module.define.Bean;
import com.ec.module.define.BeanInfo;
import com.ec.module.define.Event;
import com.ec.module.define.Init;
import com.ec.module.define.Module;

public class FormTranslator {

	public static void translate(Class cls, IFile javaFile, IFolder folder, boolean delete)
	{

		if (cls == null)
			return;

		Module module = (Module) cls.getAnnotation(Module.class);
		if(module == null)
			return;

		String pName = "";
		String fName = module.path();
		int idex = fName.lastIndexOf("/");
		if(idex>=0){
			pName = fName.substring(0,idex);
			fName = fName.substring(idex+1);
		}
		folder = WorkspaceUtil.getAndEnsureChildFolder(folder, pName);
		
		IFile pagexFile = folder.getFile(new Path(fName + ".pagex.xml"));

		boolean isNew = false;
		if (pagexFile == null || !pagexFile.exists() || pagexFile.getModificationStamp() < javaFile.getModificationStamp())
			isNew = true;

		if (module != null && isNew) {
			// IFile file = PlatformHelper.getClassFile(cls.getName());
			List allAnnos = new ArrayList();
			Class theClass = cls;
			while(theClass != null)
			{
				Field[] fields = theClass.getDeclaredFields();
				for(int i=0; i<fields.length; i++)
				{
					Bean bean = fields[i].getAnnotation(Bean.class);
					if(bean != null)
					{
						EventAnno event = new EventAnno();
						event.name = bean.name();
						event.type = "bean";
						event.field = fields[i].getName();
						allAnnos.add(event);
					}
				}
				theClass = theClass.getSuperclass();
			}
			Method[] methods = cls.getMethods();
			for(int i=0; i<methods.length; i++)
			{
				Event bean = methods[i].getAnnotation(Event.class);
				if(bean != null)
				{
					EventAnno event = new EventAnno();
					try{
						event.name = bean.name();
					}catch(Exception e)
					{
						event.name = "click";
					}
					event.field = bean.field();
					event.type = "event";
					event.method = methods[i].getName();
					try{
						event.ref = bean.ref();
					}catch(Exception e){
						event.ref = "";
					}
					try{
						event.target = bean.target();
					}catch(Exception e){
						event.target = "self";
					}
					allAnnos.add(event);
				}
				Init init = methods[i].getAnnotation(Init.class);
				if(init != null)
				{
					EventAnno event = new EventAnno();
					event.type = "init";
					event.method = methods[i].getName();
					allAnnos.add(event);
				}
			}
/*			
			JavaCodeEngine engine = JavaCodeEngine.newInstance(javaFile);
			FieldDeclaration[] all = engine.getAllFieldDeclare();
			for (int j = 0; j < all.length; j++) {
				List modifiers = all[j].modifiers();
				for (int a = 0; a < modifiers.size(); a++) {
					Object o = modifiers.get(a);
					if (o instanceof NormalAnnotation) {
						NormalAnnotation bean = (NormalAnnotation) o;
						EventAnno event = new EventAnno();
						List values = bean.values();
						for (int k = 0; k < values.size(); k++) {
							MemberValuePair ov = (MemberValuePair) values.get(k);
							String name = ov.getName().toString();
							String value = ov.getValue().toString().trim();
							if (value.startsWith("\""))
								value = value.substring(1, value.length() - 1);
							if (name.equals("name"))
								event.name = value;
						}
						event.type = "bean";
						VariableDeclarationFragment f = (VariableDeclarationFragment) all[j].fragments().get(0);
						event.field = f.getName().getIdentifier();
						allAnnos.add(event);
//						if (delete)
//							bean.delete();
					}
//					else if (delete && o instanceof Annotation) {
//						((Annotation) o).delete();
//					}
				}
			}
			MethodDeclaration[] allMethod = engine.getAllMethodDeclare();
			for (int j = 0; j < allMethod.length; j++) {
				List modifiers = allMethod[j].modifiers();
				for (int a = 0; a < modifiers.size(); a++) {
					Object o = modifiers.get(a);
					if (o instanceof NormalAnnotation) {
						NormalAnnotation bean = (NormalAnnotation) o;
						EventAnno event = new EventAnno();
						List values = bean.values();
						for (int k = 0; k < values.size(); k++) {
							MemberValuePair ov = (MemberValuePair) values.get(k);
							String name = ov.getName().toString();
							String value = ov.getValue().toString().trim();
							if (value.startsWith("\""))
								value = value.substring(1, value.length() - 1);
							if (name.equals("name")) {
								if (value.equals("Event.CLICK"))
									value = "click";
								else if (value.equals("Event.VALID"))
									value = "valid";
								else if (value.equals("Event.CHANGE"))
									value = "change";
								else if (value.equals("Event.SELECT"))
									value = "select";
								event.name = value;
							} else if (name.equals("field"))
								event.field = value;
							else if (name.equals("ref"))
								event.ref = value;
							else if (name.equals("target")) {
								if (value.equals("Event.TARGET_SELF"))
									value = "self";
								event.target = value;
							}
						}
						event.type = bean.getTypeName().getFullyQualifiedName().toLowerCase();
						event.method = allMethod[j].getName().getFullyQualifiedName();
						allAnnos.add(event);
//						if (delete)
//							bean.delete();
					} 
//					else if (delete && o instanceof Annotation) {
//						((Annotation) o).delete();
//					}
				}
			}
			if (delete) {
				try {
					engine.deleteModule();
					engine.save();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			engine.close();
*/	
			String path = module.path();
			String template = null;
			try{
				template = module.html();
			}catch(Exception e)
			{
				template = "";
			}
			String cache = Boolean.toString(module.cache());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				Document doc = dbf.newDocumentBuilder().newDocument();
				Element root = doc.createElement("process");
				doc.appendChild(root);
				addNewObject(root, path, template, cache, cls.getName(), allAnnos);
				StringWriter stringOut = new StringWriter();
				XMLFormatter formater = new XMLFormatter(stringOut);
				formater.printNode(doc, "\t");
				String content = stringOut.getBuffer().toString();
				if (!pagexFile.exists())
					pagexFile.create(new StringBufferInputStream(content), true, null);
				else
					pagexFile.setContents(new StringBufferInputStream(content), true, true, null);

			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	

	public static void addNewObject(Element pojo, String path, String template, String cache, String moduleClass, List anots) {

		int index = moduleClass.lastIndexOf(".");
		String moduleName = index > 0 ? moduleClass.substring(index + 1) : moduleClass;
		String packName = index > 0 ? moduleClass.substring(0, index) : "";

		XmlHelper.setElementAtt(pojo, "id", moduleName);
		XmlHelper.setElementAtt(pojo, "name", moduleName);
		XmlHelper.setElementAtt(pojo, "pack", packName);
		XmlHelper.setElementAtt(pojo, "path", path);
		XmlHelper.setElementAtt(pojo, "template", template);
		XmlHelper.setElementAtt(pojo, "cache", cache);
		XmlHelper.setElementAtt(pojo, "cls", moduleClass);
		// parent = XmlHelper.createOrGetChildElement(parent, "annotations");
		// List children = XmlHelper.getChildElementsByName(parent,
		// "annotation");
		// for(int i=0; i<children.size();i++)
		// {
		// Node control = (Node)children.get(i);
		// parent.removeChild(control);
		// }
		int yIndex = 0;
		for (int i = 0; i < anots.size(); i++) {
			EventAnno event = (EventAnno) anots.get(i);
			if ("event".equals(event.type) && (StringUtils.isEmpty(event.name) || StringUtils.isEmpty(event.field) || StringUtils.isEmpty(event.method)))
				continue;
			if ("init".equals(event.type) && StringUtils.isEmpty(event.method))
				continue;
			if ("bean".equals(event.type) && (StringUtils.isEmpty(event.name) || StringUtils.isEmpty(event.field)))
				continue;
			Node node = null;
			if ("init".equals(event.type)) {
				Node startnode = XmlHelper.createChildElement(pojo, "start");
				XmlHelper.setElementAtt(startnode, "id", "start");
				XmlHelper.setElementAtt(startnode, "name", "start");
				setContraint(startnode, 0, 100, 80, 55);
				Node endnode = XmlHelper.createChildElement(pojo, "end");
				XmlHelper.setElementAtt(endnode, "id", "end");
				XmlHelper.setElementAtt(endnode, "name", "end");
				setContraint(endnode, 640, 100, 80, 55);
				node = XmlHelper.createChildElement(pojo, "javamethod");
				XmlHelper.setElementAtt(node, "name", event.method);
				XmlHelper.setElementAtt(node, "id", event.method);
				XmlHelper.setElementAtt(node, "cls", moduleClass);
				setContraint(node, 160, 100, 80, 55);
				XmlHelper.setElementAtt(node, "type", event.type);
				Node tran = createTransition(pojo, "start", event.method, "init", null, null, "", null);

				node = XmlHelper.createChildElement(pojo, "form");
				XmlHelper.setElementAtt(node, "name", moduleName);
				XmlHelper.setElementAtt(node, "pack", packName);
				XmlHelper.setElementAtt(node, "id", "defaultform");
				setContraint(node, 320, 100, 80, 55);
				createTransition(pojo, event.method, "defaultform", "default", "", "show", "default", null);
				// createTransition(pojo, "init", "end", "end");
			} else if ("bean".equals(event.type)) {
				node = XmlHelper.createChildElement(pojo, "bean");
				XmlHelper.setElementAtt(node, "id", event.field);
				XmlHelper.setElementAtt(node, "name", event.name);
				setContraint(node, 160, 100, 80, 55);
			} else {
				node = XmlHelper.createChildElement(pojo, "javamethod");
				XmlHelper.setElementAtt(node, "name", event.method);
				XmlHelper.setElementAtt(node, "id", event.method);
				XmlHelper.setElementAtt(node, "cls", moduleClass);
				XmlHelper.setElementAtt(node, "target", event.target);
				XmlHelper.setElementAtt(node, "event", event.name);
				XmlHelper.setElementAtt(node, "field", event.field);
//				if (!StringUtils.isEmpty(event.ref)) {
//					String[] params = event.ref.split(",");
//					for (int k = 0; k < params.length; k++) {
//						Node data = XmlHelper.createChildElement(node, "data");
//						XmlHelper.setElementAtt(data, "name", params[k]);
//						XmlHelper.setElementAtt(data, "type", "String");
//						XmlHelper.setElementAtt(data, "io", "in");
//						XmlHelper.setElementAtt(data, "param", params[k]);
//					}
//				}
				setContraint(node, 480, yIndex * 100 + 100, 80, 55);

				yIndex++;
				createTransition(pojo, "defaultform", event.method, event.field + "_" + event.name, event.field, event.name, event.field + "_" + event.name, event.ref);
				createTransition(pojo, event.method, "end", event.method + "_end", null, null, event.method + "_end", event.ref);
			}
		}

		if (yIndex == 0)
			createTransition(pojo, "defaultform", "end", "default end", null, null, "", null);

	}

	private static void setContraint(Node node, int x, int y, int w, int h) {
		XmlHelper.setElementAtt(node, "x", Integer.toString(x));
		XmlHelper.setElementAtt(node, "y", Integer.toString(y));
		XmlHelper.setElementAtt(node, "w", Integer.toString(w));
		XmlHelper.setElementAtt(node, "h", Integer.toString(h));
	}

	private static Node createTransition(Node parent, String from, String to, String name, String field, String event, String id, String ref) {
		Node node = XmlHelper.createChildElement(parent, "transition");
		XmlHelper.setElementAtt(node, "from", from);
		XmlHelper.setElementAtt(node, "to", to);
		XmlHelper.setElementAtt(node, "name", name);
		XmlHelper.setElementAtt(node, "id", id);
		if(!StringUtils.isEmpty(ref))
			XmlHelper.setElementAtt(node, "ref", ref);
		if (field != null)
			XmlHelper.setElementAtt(node, "field", field);
		if (event != null)
			XmlHelper.setElementAtt(node, "event", event);

		return node;
	}

}
