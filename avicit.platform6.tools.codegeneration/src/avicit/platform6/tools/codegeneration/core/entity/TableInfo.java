/*
 * TableInfo.java
 *
 * Created on 2008年1月17日, 下午3:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package avicit.platform6.tools.codegeneration.core.entity;

import java.util.ArrayList;
import java.util.List;

import avicit.platform6.tools.codegeneration.core.util.StringUtils;

/**
 * 表对象
 *
 * @author Administrator
 */
public class TableInfo {
	
    private String tableName;   //表名
    private String schemaName;   //schema或catalog名称
    private String tableComment;   //表信息（注释）
    private List<FieldInfo> primaryKeys;    //主键字段列表
    private List<FieldInfo> fields;         //普通字段列表
    private FieldInfo primaryField;       //第一个主键字段
    private boolean onlyUniquePrimaryKey;   //是否只有唯一主键
    private EntityBeanInfo entityBean;    //对应的实体类对象
    private List<RelationTable> childTables;  //子表列表（根据外键来关联）
    private List<RelationTable> parentTables;  //主表列表（根据外键来关联）
    private FieldInfo treePrimaryField;        //树主关联字段
    private FieldInfo treePanrentField;         //树父关联字段
    private FieldInfo treeDisplayField;         //树显示字段
    private String lowerTableName; //小写表名



	/**
     * Creates a new instance of TableInfo
     */
    public TableInfo() {
        primaryKeys = new ArrayList();
        fields = new ArrayList();
        childTables = new ArrayList();
        parentTables = new ArrayList();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public List<FieldInfo> getPrimaryKeys() {
        if (primaryKeys != null && primaryKeys.size() == 1) {
            primaryField = primaryKeys.get(0);
            onlyUniquePrimaryKey = true;
        }
        return primaryKeys;
        
    }

    public void setPrimaryKeys(List<FieldInfo> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public List<FieldInfo> getFields() {
        return fields;
    }

    public void setFields(List<FieldInfo> fields) {
        this.fields = fields;
    }

    public EntityBeanInfo getEntityBean() {
        return entityBean;
    }

    public void setEntityBean(EntityBeanInfo entityBean) {
        this.entityBean = entityBean;
    }

    public FieldInfo getPrimaryField() {
        return primaryField;
    }

    public void setPrimaryField(FieldInfo primaryField) {
        this.primaryField = primaryField;
    }

    public boolean isOnlyUniquePrimaryKey() {
        return onlyUniquePrimaryKey;
    }

    public void setOnlyUniquePrimaryKey(boolean onlyUniquePrimaryKey) {
        this.onlyUniquePrimaryKey = onlyUniquePrimaryKey;
    }

    public List<RelationTable> getChildTables() {
        return childTables;
    }

    public void setChildTables(List<RelationTable> childTables) {
        this.childTables = childTables;
    }

    public List<RelationTable> getParentTables() {
        return parentTables;
    }

    public void setParentTables(List<RelationTable> parentTables) {
        this.parentTables = parentTables;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        if (tableComment == null) {
            this.tableComment = tableComment;
        } else {
            String str = tableComment;
            str = StringUtils.replace(str, System.getProperty("line.separator"), " ");
            str = StringUtils.replace(str, "\r", " ");
            str = StringUtils.replace(str, "\n", " ");
            this.tableComment=str;
        }

    }
    
    public String getLowerTableName() {
		return this.tableName.toLowerCase();
	}

	public void setLowerTableName(String lowerTableName) {
		this.lowerTableName = lowerTableName;
	}

	public FieldInfo getTreePrimaryField() {
		return treePrimaryField;
	}

	public void setTreePrimaryField(FieldInfo treePrimaryField) {
		this.treePrimaryField = treePrimaryField;
	}

	public FieldInfo getTreePanrentField() {
		return treePanrentField;
	}

	public void setTreePanrentField(FieldInfo treePanrentField) {
		this.treePanrentField = treePanrentField;
	}

	public FieldInfo getTreeDisplayField() {
		return treeDisplayField;
	}

	public void setTreeDisplayField(FieldInfo treeDisplayField) {
		this.treeDisplayField = treeDisplayField;
	}  
	
	
}
