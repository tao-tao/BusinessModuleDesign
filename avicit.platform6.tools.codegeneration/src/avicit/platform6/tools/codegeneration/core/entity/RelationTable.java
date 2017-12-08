/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package avicit.platform6.tools.codegeneration.core.entity;

/**
 *子表对象
 * @author Administrator
 */
public class RelationTable {

    private TableInfo parentTable;
    private TableInfo childTable;
    private FieldInfo parentField;
    private FieldInfo childField;

  
    

    public TableInfo getParentTable() {
        return parentTable;
    }

    public void setParentTable(TableInfo parentTable) {
        this.parentTable = parentTable;
    }

    public TableInfo getChildTable() {
        return childTable;
    }

    public void setChildTable(TableInfo childTable) {
        this.childTable = childTable;
    }

    public FieldInfo getParentField() {
        return parentField;
    }

    public void setParentField(FieldInfo parentField) {
        this.parentField = parentField;
    }

    public FieldInfo getChildField() {
        return childField;
    }

    public void setChildField(FieldInfo childField) {
        this.childField = childField;
    }
}
