package avicit.platform6.tools.codegeneration.core.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import avicit.platform6.tools.codegeneration.core.entity.BusinessObjectProperty;
import avicit.platform6.tools.codegeneration.core.entity.EntityBeanInfo;
import avicit.platform6.tools.codegeneration.core.entity.EntityParamInfo;
import avicit.platform6.tools.codegeneration.core.entity.FieldInfo;
import avicit.platform6.tools.codegeneration.core.entity.TableInfo;
import avicit.platform6.tools.codegeneration.core.util.StringUtils;
/**
 * <p>金航数码科技有限责任公司</p>
 * <p>作者：dingrc</p>
 * <p>邮箱：dingrc@avicit.com</p>
 * <p>创建时间：2012-12-10</p>
 *
 * <p>类说明：BusinessObjectProperty工具类</p>
 * <p>修改记录：</p>
 */
public class BOP2TableInfoUtil {
	public  TableInfo Bo2TableInfo(List<BusinessObjectProperty> boList,String tableName ){
		 TableInfo tableInfo = new TableInfo();
	        EntityBeanInfo entityBeanInfo = new EntityBeanInfo();
	        tableInfo.setEntityBean(entityBeanInfo);
	        entityBeanInfo.setTable(tableInfo);

	        tableInfo.setTableName(tableName);
	        tableInfo.setTableComment(tableName);

	        try {
	           

	          
	            List primarykeys = new ArrayList();
	                primarykeys.add("ID");
	           
	           
	            for (int i = 0; i < boList.size(); i++) {
	                FieldInfo fieldInfo = new FieldInfo();
	                fieldInfo.setFieldName(boList.get(i).getColumn());
	                fieldInfo.setFieldLabel(boList.get(i).getColumn());
	                fieldInfo.setFieldTableName(tableName);
	                if(boList.get(i).getType().toLowerCase().equals("string")){
	                	fieldInfo.setFieldTypeName("java.lang.String");
	                }else{
	                	fieldInfo.setFieldTypeName(boList.get(i).getType());
	                }
	                String len=boList.get(i).getLength();	
	                if(null!=len && !"".equals(len)){
	                	fieldInfo.setFieldDisplaySize(new Integer(len));
	                }
	                
	                fieldInfo.setFieldClassName(boList.get(i).getType());
	                fieldInfo.setIsNullable(!boList.get(i).isRequired());
	                fieldInfo.setComment(fieldInfo.getFieldLabel());

	               

	                if (primarykeys.contains(fieldInfo.getFieldName())) {
	                    tableInfo.getPrimaryKeys().add(fieldInfo);
	                } else {
	                    tableInfo.getFields().add(fieldInfo);
	                }

	            }
	            //表的主键集合

	            entityBeanInfo.setStandName(buildStandBeanName(tableInfo.getTableName()));
	            entityBeanInfo.setUpperFirstCharName(StringUtils.capitalize(entityBeanInfo.getStandName()));
	            entityBeanInfo.setUppderAllCharName(entityBeanInfo.getStandName().toUpperCase());
	            entityBeanInfo.setLowerFirstCharName(StringUtils.uncapitalize(entityBeanInfo.getStandName()));
	            entityBeanInfo.setLowerAllCharName(entityBeanInfo.getStandName().toLowerCase());
	            buildParamsForEntityBean(entityBeanInfo);

	           
	           
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	           // throw new Exception((new StringBuilder("枚举字段列表出错，错误信息为：")).append(e.getMessage()).toString());
	        }
	        return tableInfo;
	}
	
	
	 /**
     *构建标准类名
     */
    private String buildStandBeanName(String tableName) {
        if (tableName == null || tableName.trim().equals("")) {
            return "";
        }
        if (tableName.indexOf("_") <= 0 || tableName.endsWith("_")) {   //不包含“_”或第一个字符为“_”,或最后一个字符为“_”
            return StringUtils.capitalize(tableName.toLowerCase());  //首字母大写，其余均小写
        }
        String[] tableNameStrs = tableName.split("_");
        String returnStr = "";
        for (int i = 0; i < tableNameStrs.length; i++) {
            returnStr += StringUtils.capitalize(tableNameStrs[i].toLowerCase());
        }
        return returnStr;
    }

    /**
     *构建标准类名
     */
    private String buildStandParamName(String fieldName) {
        if (fieldName == null || fieldName.trim().equals("")) {
            return "";
        }
        if (fieldName.indexOf("_") <= 0 || fieldName.endsWith("_")) {   //不包含“_”或第一个字符为“_”,或最后一个字符为“_”
            return fieldName.toLowerCase();  //首字母大写，其余均小写
        }
        String[] tableNameStrs = fieldName.split("_");
        String returnStr = "";
        for (int i = 0; i < tableNameStrs.length; i++) {
            if (i == 0) {
                returnStr = tableNameStrs[i].toLowerCase();    //第一个字母全小写
            } else {
                returnStr += StringUtils.capitalize(tableNameStrs[i].toLowerCase());   //首字母大写
            }

        }
        return returnStr;
    }

    /**
     * 根据表字段来构建实体的属性对象
     * @param entityBeanInfo
     */
    private void buildParamsForEntityBean(EntityBeanInfo entityBeanInfo) {
        buildParamsFromFields(entityBeanInfo, entityBeanInfo.getTable().getPrimaryKeys());
        buildParamsFromFields(entityBeanInfo, entityBeanInfo.getTable().getFields());
    }

    /**
     * 根据字段列表来获得属性列表
     * @param entityBeanInfo
     * @param fields
     */
    private void buildParamsFromFields(EntityBeanInfo entityBeanInfo, List fields) {
        if (fields == null) {
            return;
        }
        for (Iterator i = fields.iterator(); i.hasNext();) {
            FieldInfo fieldInfo = (FieldInfo) i.next();
            if (fieldInfo == null) {
                continue;
            }
            EntityParamInfo paramInfo = new EntityParamInfo();
            fieldInfo.setParamInfo(paramInfo);
            paramInfo.setEntityBean(entityBeanInfo);
            paramInfo.setField(fieldInfo);
            paramInfo.setStandName(buildStandParamName(fieldInfo.getFieldName()));
            paramInfo.setUpperFirstCharName(StringUtils.capitalize(paramInfo.getStandName()));
            paramInfo.setUppderAllCharName(paramInfo.getStandName().toUpperCase());
            paramInfo.setLowerFirstCharName(StringUtils.uncapitalize(paramInfo.getStandName()));
            paramInfo.setLowerAllCharName(paramInfo.getStandName().toLowerCase());
            paramInfo.setParamType(fieldInfo.getFieldClassName());
            entityBeanInfo.getParams().add(paramInfo);


        }
    }
    
    
}
