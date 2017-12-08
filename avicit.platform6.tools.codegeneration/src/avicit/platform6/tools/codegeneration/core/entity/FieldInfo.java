package avicit.platform6.tools.codegeneration.core.entity;

import java.sql.ResultSetMetaData;

import avicit.platform6.tools.codegeneration.core.util.StringUtils;

/**
 * 数据库字段对象
 */
public class FieldInfo {

    private String fieldName;   //字段名称
    private String fieldLabel;  //字段标题
    private int fieldType;     //字段类型（整形）
    private String fieldTypeName;   //字段类型名称
    private int fieldDisplaySize;   //字段值长度
    private String fieldClassName;   //字段对应类名
    private String fieldSchemaName;   //对应的schema名称
    private String fieldTableName;    //字段所在的表名
    private String comment;           //字段的注释
    private EntityParamInfo paramInfo;   //对应的属性对象
    private boolean isNullable;       //字段是否为空（true：可以为空；false：不可为空；未知状态也设置为不可为空）
    private String lowerFildName;
    
    private boolean isHidden;
    
    public String selectTypeFlag;
    
    public enum selectType{
    	selectUser,selectRole,selectDept,selectPosition,selectGroup,selcetLookup
    };
    
    public enum infoType{
    	FORM,TABLE,SERATCH
    };

	public FieldInfo() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public int getFieldType() {
        return fieldType;
    }

    public void setFieldType(int fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldTypeName() {
        return fieldTypeName;
    }

    public void setFieldTypeName(String fieldTypeName) {
        this.fieldTypeName = fieldTypeName;
    }

    public int getFieldDisplaySize() {
        return fieldDisplaySize;
    }

    public void setFieldDisplaySize(int fieldDisplaySize) {
        this.fieldDisplaySize = fieldDisplaySize;
    }

    public String getFieldClassName() {
        return fieldClassName;
    }

    public void setFieldClassName(String fieldClassName) {
        if (fieldClassName != null && fieldClassName.equals("java.sql.Timestamp")) {
            this.fieldClassName = "java.util.Date";
        } else {
            this.fieldClassName = fieldClassName;
        }

    }

    public String getFieldSchemaName() {
        return fieldSchemaName;
    }

    public void setFieldSchemaName(String fieldSchemaName) {
        this.fieldSchemaName = fieldSchemaName;
    }

    public String getFieldTableName() {
        return fieldTableName;
    }

    public void setFieldTableName(String fieldTableName) {
        this.fieldTableName = fieldTableName;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        if (comment == null) {
            this.comment = comment;
        } else {
            String str = comment;
            str = StringUtils.replace(str, System.getProperty("line.separator"), " ");
            str = StringUtils.replace(str, "\r", " ");
            str = StringUtils.replace(str, "\n", " ");
            this.comment = str;
        }

    }

    public boolean isIsNullable() {
        return isNullable;
    }

    public void setIsNullable(int _isNullable) {
        if(_isNullable==ResultSetMetaData.columnNoNulls){
            this.isNullable = false;
        }else if(_isNullable==ResultSetMetaData.columnNullable) {
            this.isNullable = true;
        }else if(_isNullable==ResultSetMetaData.columnNullableUnknown){   //不可知情况下也设置为“不可为空”
            this.isNullable = false;
        }
        
    }
    public void setIsNullable(boolean _isNullable) {
    	 this.isNullable = _isNullable;
        
    }


    public EntityParamInfo getParamInfo() {
        return paramInfo;
    }

    public void setParamInfo(EntityParamInfo paramInfo) {
        this.paramInfo = paramInfo;
    }
    
    public String getLowerFildName() {
		return this.fieldName.toLowerCase();
	}

	public void setLowerFildName(String lowerFildName) {
		this.lowerFildName = lowerFildName;
	}

	public boolean isNullable() {
		return isNullable;
	}

	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public String getSelectTypeFlag() {
		return selectTypeFlag;
	}

	public void setSelectTypeFlag(String selectTypeFlag) {
		this.selectTypeFlag = selectTypeFlag;
	}

	
	
}
