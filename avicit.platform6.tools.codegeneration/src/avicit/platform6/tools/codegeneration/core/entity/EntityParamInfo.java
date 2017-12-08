/*
 * EntityParam.java
 *
 * Created on 2008年1月17日, 下午3:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package avicit.platform6.tools.codegeneration.core.entity;

/**
 * 参数描述对象
 * @author Administrator
 */
public class EntityParamInfo {
    
    private String standName; //参数名称，例如：id,archArchives
    private String paramType;  //参数类型，例如：java.lang.String;java.util.Date
    private String upperFirstCharName;  //第一个字母为大写的写法，例如：Id
    private String lowerFirstCharName;   //第一个字母为小写的写法，例如：id
    private String uppderAllCharName;   //整个为大写
    private String lowerAllCharName;     //整个为小写
    
    private EntityBeanInfo entityBean;    //所属的实体类对象
    private FieldInfo field;    //对应的数据库字段对象
    
    
    /** Creates a new instance of EntityParam */
    public EntityParamInfo() {
    }

    public String getStandName() {
        return standName;
    }

    public void setStandName(String standName) {
        this.standName = standName;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getUpperFirstCharName() {
        return upperFirstCharName;
    }

    public void setUpperFirstCharName(String upperFirstCharName) {
        this.upperFirstCharName = upperFirstCharName;
    }

    public String getLowerFirstCharName() {
        return lowerFirstCharName;
    }

    public void setLowerFirstCharName(String lowerFirstCharName) {
        this.lowerFirstCharName = lowerFirstCharName;
    }

    public String getUppderAllCharName() {
        return uppderAllCharName;
    }

    public void setUppderAllCharName(String uppderAllCharName) {
        this.uppderAllCharName = uppderAllCharName;
    }

    public String getLowerAllCharName() {
        return lowerAllCharName;
    }

    public void setLowerAllCharName(String lowerAllCharName) {
        this.lowerAllCharName = lowerAllCharName;
    }

    public EntityBeanInfo getEntityBean() {
        return entityBean;
    }

    public void setEntityBean(EntityBeanInfo entityBean) {
        this.entityBean = entityBean;
    }

    public FieldInfo getField() {
        return field;
    }

    public void setField(FieldInfo field) {
        this.field = field;
    }
    
}
