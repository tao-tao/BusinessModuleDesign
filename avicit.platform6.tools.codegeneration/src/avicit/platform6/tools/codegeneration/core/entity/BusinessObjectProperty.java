package avicit.platform6.tools.codegeneration.core.entity;

import avicit.platform6.tools.codegeneration.CodeGenerationActivator;

/**
 * 金航数码科技有限责任公司 研发中心平台部 
 * 作者：dingrc 
 * 邮箱：dingrc@avicit.com 
 * 创建时间：2012-12-10 
 * 
 * 类说明：
 * 修改记录：
 */
public class BusinessObjectProperty {
	String name;
	String column;
	String type;
	String length;
	String isList;
	String isEditable;
	boolean isRequired = false;
	private String label;

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
		if (column.equals("ID") || column.equals("LAST_UPDATE_DATE")
				|| column.equals("LAST_UPDATED_BY")
				|| column.equals("CREATION_DATE")
				|| column.equals("CREATED_BY")
				|| column.equals("LAST_UPDATE_IP") || column.equals("VERSION")
				|| column.startsWith("ATTRIBUTE_")) {
			this.isList = CodeGenerationActivator.HIDDEN;
			this.isEditable = CodeGenerationActivator.UNEDITABLE;
		} else {
			this.isList = CodeGenerationActivator.VISIBLE;
			this.isEditable = CodeGenerationActivator.EDITABLE;
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
//		if (type.equals("java.lang.String"))
//			this.type = "string";
//		else if (type.equals("java.sql.Blob"))
//			this.type = "string";
//		else if (type.equals("java.sql.Clob"))
//			this.type = "string";
//		else if (type.equals("java.sql.Date"))
//			this.type = "date";
//		else if (type.equals("java.lang.Long"))
//			this.type = "long";
//		else if (type.equals("java.math.BigDecimal"))
//			this.type = "double";
//		else if (type.equals("java.lang.Integer"))
//			this.type = "int";
//		else if (type.equals("java.lang.Boolean"))
//			this.type = "boolean";
//		else
			this.type = type;
	}
	
	public void setTypeWithOracleColumnType(String type, long dataPrecision, long dataScale) {
		if (type.equals("DATE")) {
			this.type = "date";
		} else if (type.equals("NUMBER")) {
			if (dataScale < 1) {
				if (dataPrecision > 0 && dataPrecision < 10) {
					this.type = "int";
				} else if (dataPrecision > 9 && dataPrecision < 19) {
					this.type = "long";
				} else {
					this.type = "double";
				}
			} else {
				this.type = "double";
			} 
		} else {
			this.type = "string";
		}
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsList() {
		return isList;
	}

	public void setIsList(String isList) {
		this.isList = isList;
	}

	public String getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(String isEditable) {
		this.isEditable = isEditable;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(String isRequired) {
		if (isRequired.equals("true")
				&& !(column.equals("ID") || column.equals("LAST_UPDATE_DATE")
						|| column.equals("LAST_UPDATED_BY")
						|| column.equals("CREATION_DATE")
						|| column.equals("CREATED_BY")
						|| column.equals("LAST_UPDATE_IP")
						|| column.equals("VERSION") || column
						.startsWith("ATTRIBUTE_")))
			this.isRequired = true;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
