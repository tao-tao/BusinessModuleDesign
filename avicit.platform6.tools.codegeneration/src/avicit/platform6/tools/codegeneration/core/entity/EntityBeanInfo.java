/*
 * EntityBeanInfo.java
 *
 * Created on 2008年1月17日, 下午3:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package avicit.platform6.tools.codegeneration.core.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * 实体对象信息
 * @author Administrator
 */
public class EntityBeanInfo {
    
    private String standName;   //并的标准名称，也就是类名称
    private TableInfo table;   //对应的数据库表
    private List<EntityParamInfo> params;  //所有属性列表
    private WeakHashMap<String, List<EntityParamInfo>> typeParams = new WeakHashMap<String, List<EntityParamInfo>>();//根据类型不同返回不同的属性列表
    
    private String upperFirstCharName;  //第一个字母为大写的写法，例如：Id
    private String lowerFirstCharName;   //第一个字母为小写的写法，例如：id
    private String uppderAllCharName;   //整个为大写
    private String lowerAllCharName;     //整个为小写
    
    
    /** Creates a new instance of EntityBeanInfo */
    public EntityBeanInfo() {
        params=new ArrayList();
    }

    public TableInfo getTable() {
        return table;
    }

    public void setTable(TableInfo table) {
        this.table = table;
    }

    public List<EntityParamInfo> getParams() {
        return params;
    }

    public void setParams(List<EntityParamInfo> params) {
        this.params = params;
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

    public String getStandName() {
        return standName;
    }

    public void setStandName(String standName) {
        this.standName = standName;
    }

	public WeakHashMap<String, List<EntityParamInfo>> getTypeParams() {
		return typeParams;
	}

	public void setTypeParams(String type,List<EntityParamInfo> params) {
		this.typeParams.put(type, params);
	}
    
}
