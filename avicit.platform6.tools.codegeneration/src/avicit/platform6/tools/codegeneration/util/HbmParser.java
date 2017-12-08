package avicit.platform6.tools.codegeneration.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.DefaultAttribute;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;

import avicit.platform6.tools.codegeneration.core.entity.BusinessObjectProperty;
/**
 * <p>金航数码科技有限责任公司</p>
 * <p>作者：dingrc</p>
 * <p>邮箱：dingrc@avicit.com</p>
 * <p>创建时间：2012-12-10</p>
 *
 * <p>类说明：hbm文件解析,从老代码生成器拿来</p>
 * <p>修改记录：</p>
 */
public class HbmParser {
	@SuppressWarnings("unchecked")
	public boolean parseHbm(List<BusinessObjectProperty> boList, Document doc,
			Map<String, String> labels) throws Exception {
		// 是否需要乐观锁
		boolean optimisticLock = false;
		boList.clear();
		Element rootElement = doc.getRootElement();
		for (Iterator i = rootElement.elementIterator("class"); i.hasNext();) {
			Element classElement = (Element) i.next();
			for (Iterator j = classElement.elementIterator(); j.hasNext();) {
				Element propertyElement = (Element) j.next();
				if (propertyElement.getName().equals("meta")) {
					continue;
				} else if (propertyElement.getName().equals("composite-id")) {
					// 目前来说吧，又碰到了新的情况，就是可能是composite-id
					List props = propertyElement.elements("key-property");
					for (Object obj : props) {
						Element keyProp = (Element) obj;
						BusinessObjectProperty boProperty = new BusinessObjectProperty();
						boList.add(boProperty);
						List attrs = keyProp.attributes();
						for (Object objAttr : attrs) {
							Attribute attr = (Attribute) objAttr;
							if (attr.getName().equals("name")) {
								boProperty.setName(attr.getText());
							} else if (attr.getName().equals("type")) {
								boProperty.setType(attr.getText());
							} else if (attr.getName().equals("column")) {
								boProperty.setColumn(attr.getText());
								String label = labels.get(attr.getText());
								if (label == null) {
									label = attr.getText();
								}
								boProperty.setLabel(label);
							} else if (attr.getName().equals("length")) {
								boProperty.setLength(attr.getText());
							} else if (attr.getName().equals("not-null")) {
								boProperty.setRequired(attr.getText());
							}
						}
					}
					continue;
				}
				BusinessObjectProperty boProperty = new BusinessObjectProperty();
				boList.add(boProperty);
				// 先遍历属性
				for (Iterator k = propertyElement.attributeIterator(); k
						.hasNext();) {
					Attribute attribute = (Attribute) k.next();
					if (attribute.getName().equals("name")) {
						// 只有存在version字段时，才增加乐观锁
						String text = attribute.getText();
						if (text.toLowerCase().equals("version")) {
							optimisticLock = true;
						}
						boProperty.setName(attribute.getText());
					} else if (attribute.getName().equals("type")) {
						boProperty.setType(attribute.getText());
					} else if (attribute.getName().equals("column")) {
						boProperty.setColumn(attribute.getText());
						String label = labels.get(attribute.getText());
						if (label == null) {
							label = attribute.getText();
						}
						boProperty.setLabel(label);
					} else if (attribute.getName().equals("length")) {
						boProperty.setLength(attribute.getText());
					} else if (attribute.getName().equals("not-null")) {
						boProperty.setRequired(attribute.getText());
					}
				}
				// 再遍历子节点
				for (Iterator k = propertyElement.elementIterator(); k
						.hasNext();) {
					Element columnElement = (Element) k.next();
					if (columnElement.getName().equals("column")) {
						for (Iterator l = columnElement.attributeIterator(); l
								.hasNext();) {
							DefaultAttribute attribute = (DefaultAttribute) l
									.next();
							if (attribute.getName().equals("name")) {
								boProperty.setColumn(attribute.getText());
							} else if (attribute.getName().equals("length")) {
								boProperty.setLength(attribute.getText());
							} else if (attribute.getName().equals("not-null")) {
								boProperty.setRequired(attribute.getText());
							}
						}
					}
				}
			}
		}
		return optimisticLock;
	}

	public void parseJava(IFile javaFile) throws Exception {
		// String className = businessObject.substring(businessObject
		// .lastIndexOf(".") + 1);
		String className = javaFile.getName().substring(0,
				javaFile.getName().lastIndexOf("."));
		String head = "public class " + className
				+ " implements Serializable {";
		InputStream source = null;
		try {
			StringBuilder sb = new StringBuilder();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					javaFile.getContents(), "UTF-8"));
			String line = in.readLine();
			boolean hasFormId = false;
			boolean hasFormName = false;
			boolean hasTitle = false;
			boolean hasSecrectLevel = false;
			while (line != null) {
				if (line.equals(head)) {
					line = head
							.replace(
									className,
									className
											+ " extends avicit.platform6.core.domain.BeanBase");
				}
				if (line.contains(" long ")) {
					line = line.replace(" long ", " Long ");
				}
				if (line.contains("(long ")) {
					line = line.replace("(long ", "(Long ");
				}
				if (line.contains(" int ") && !line.contains("hashCode")) {
					line = line.replace(" int ", " Integer ");
				}
				if (line.contains("(int ") && !line.contains("hashCode")) {
					line = line.replace("(int ", "(Integer ");
				}
				if (line.contains(" BigDecimal ")) {
					line = line.replace(" BigDecimal ", " Double ");
				}
				if (line.contains("(BigDecimal ")) {
					line = line.replace("(BigDecimal ", "(Double ");
				}
				if (line.contains(" Clob ")) {
					line = line.replace(" Clob ", " String ");
				}
				if (line.contains("(Clob ")) {
					line = line.replace("(Clob ", "(String ");
				}
				if (line.contains(" Blob ")) {
					line = line.replace(" Blob ", " String ");
				}
				if (line.contains("(Blob ")) {
					line = line.replace("(Blob ", "(String ");
				}
				if (line.contains("public String getFormId() {")) {
					hasFormId = true;
				}
				if (line.contains("public String getFormName() {")) {
					hasFormName = true;
				}
				if (line.contains("public String getTitle() {")) {
					hasTitle = true;
				}
				if (line.contains("public String getSecretLevel() {")) {
					hasSecrectLevel = true;
				}
				String temp = line;
				line = in.readLine();
				if (line == null && temp.equals("}")) {
					if (!hasFormId) {
						sb.append("    public String getFormId() {\n");
						sb.append("        return id;\n");
						sb.append("    }\n");
					}
					if (!hasFormName) {
						sb.append("    public String getFormName() {\n");
						sb.append("        return null;\n");
						sb.append("    }\n");
					}
					if (!hasTitle) {
						sb.append("    public String getTitle() {\n");
						sb.append("        return null;\n");
						sb.append("    }\n");
					}
					if (!hasSecrectLevel) {
						sb.append("    public String getSecretLevel() {\n");
						sb.append("        return null;\n");
						sb.append("    }\n");
					}
					sb.append("}\n");
				} else {
					sb.append(temp + "\n");
				}
			}
			in.close();
			source = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
			javaFile.setContents(source, IResource.FORCE, null);
		} finally {
			try {
				if (source != null)
					source.close();
			} catch (Exception e) {

			}
		}
	}
}
