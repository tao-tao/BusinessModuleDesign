package avicit.platform6.tools.codegeneration.core.common;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;

import avicit.platform6.tools.codegeneration.core.entity.EntityBeanInfo;
import avicit.platform6.tools.codegeneration.core.entity.EntityParamInfo;
import avicit.platform6.tools.codegeneration.core.entity.FieldInfo;
import avicit.platform6.tools.codegeneration.core.entity.FieldInfo.infoType;
import avicit.platform6.tools.codegeneration.core.entity.FieldInfo.selectType;
import avicit.platform6.tools.codegeneration.core.entity.RelationTable;
import avicit.platform6.tools.codegeneration.core.entity.TableInfo;
import avicit.platform6.tools.codegeneration.core.util.DatabaseType;
import avicit.platform6.tools.codegeneration.core.util.StringUtils;
import avicit.platform6.tools.codegeneration.wizard.CodeGenerationWizard;
import avicit.platform6.tools.codegeneration.wizard.GridFormPage;
import avicit.platform6.tools.codegeneration.wizard.ListDataField;
import avicit.platform6.tools.codegeneration.wizard.ListDataFieldCpt;
/**
 * <p>金航数码科技有限责任公司</p>
 * <p>作者：dingrc</p>
 * <p>邮箱：dingrc@avicit.com</p>
 * <p>创建时间：2012-12-10</p>
 *
 * <p>类说明：数据库表操作工具</p>
 * <p>修改记录：</p>
 */
public class TableUtil {

    private DBConnectionFactory dBConnectionFactory;
    private Connection conn;
    private DatabaseMetaData dm;
    private CodeGenerationWizard codeWizard;

    public TableUtil() {
        dBConnectionFactory = DBConnectionFactory.getInstance();
    }

    public TableUtil(Connection conn)
            throws Exception {
        dBConnectionFactory = DBConnectionFactory.getInstance();
        if (conn == null) {
            throw new Exception("数据库连接为空");
        } else {
            this.conn = conn;
            return;
        }
    }

    //add by zl
    
    public void buildForeignKeyForTablesByFields(TableInfo mainTable,TableInfo subTable,String mainKey,String subKey){
    	 //定义一个关联表
        RelationTable relationTable = new RelationTable();
    	
        relationTable.setParentTable(mainTable);
        relationTable.setParentField(this.getFieldInfobyName(mainTable, mainKey));
        relationTable.setChildTable(subTable);
        relationTable.setChildField(this.getFieldInfobyName(subTable, subKey));
        mainTable.getChildTables().add(relationTable);
        subTable.getParentTables().add(relationTable);
      
    }
    public void buildTreekeyForTablesByFields(TableInfo treeTable,String mainKey,String subKey,String displayKey){
   	 //定义一个关联表
   	
    	treeTable.setTreePrimaryField(this.getFieldInfobyName(treeTable, mainKey));
    	treeTable.setTreePanrentField(this.getFieldInfobyName(treeTable, subKey));
    	treeTable.setTreeDisplayField(this.getFieldInfobyName(treeTable, displayKey));
     
     
   }
    /**
     * 构建表之间的父子关系(外键关系)
     * @param tables
     */
    public void buildForeignKeyForTables(List<TableInfo> tables) {
        try {
            for (int i = 0; i < tables.size(); i++) {
                //定位一个表
                TableInfo table = tables.get(i);
                ResultSet foreignSet = null;
                //primarySet=conn.getMetaData().getPrimaryKeys(DatabaseTableUtil.DATABASE_NAME,DatabaseTableUtil.SCHEMA_CATALOG,tableName);
                //找到这个表的所有外键
                foreignSet = conn.getMetaData().getImportedKeys(DatabaseTableUtil.DATABASE_NAME, DatabaseTableUtil.SCHEMA_CATALOG, table.getTableName());

                while (foreignSet.next()) {
                    if (this.getTableInfobyName(tables, foreignSet.getString("PKTABLE_NAME")) != null) {  //对应主表存在
                        //定义一个关联表
                        RelationTable relationTable = new RelationTable();
                        //获得主表
                        TableInfo parentTable = this.getTableInfobyName(tables, foreignSet.getString("PKTABLE_NAME"));

                        relationTable.setParentTable(parentTable);
                        relationTable.setParentField(this.getFieldInfobyName(parentTable, foreignSet.getString("PKCOLUMN_NAME")));
                        relationTable.setChildTable(table);
                        relationTable.setChildField(this.getFieldInfobyName(table, foreignSet.getString("FKCOLUMN_NAME")));
                        parentTable.getChildTables().add(relationTable);  //主表的从表列表中添加一个关联描述
                        table.getParentTables().add(relationTable);       //从表的主表列表中添加一个关联描述
                    }
                }
                foreignSet.close();
                foreignSet.getStatement().close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 根据表名称来获得表对象
     * @param tables
     * @param tableName
     * @return
     */
    private TableInfo getTableInfobyName(List<TableInfo> tables, String tableName) {
        for (int i = 0; i < tables.size(); i++) {
            if (tables.get(i).getTableName().equals(tableName)) {
                return tables.get(i);
            }
        }

        return null;
    }

    /**
     * 根据字段名称来获得字段对象
     * @param table
     * @param fieldName
     * @return
     */
    private FieldInfo getFieldInfobyName(TableInfo table, String fieldName) {
        for (int i = 0; i < table.getPrimaryKeys().size(); i++) {
            if (table.getPrimaryKeys().get(i).getFieldName().equals(fieldName)) {
                return table.getPrimaryKeys().get(i);
            }
        }
        for (int i = 0; i < table.getFields().size(); i++) {
            if (table.getFields().get(i).getFieldName().equals(fieldName)) {
                return table.getFields().get(i);
            }
        }
        return null;
    }

    /**
     * 构建表的信息对象及其对应的bean对象
     * @param tableName
     * @return
     * @throws java.lang.Exception
     */
    public TableInfo getTableInfo(String tableName)
            throws Exception {
        TableInfo tableInfo = new TableInfo();
        EntityBeanInfo entityBeanInfo = new EntityBeanInfo();
        tableInfo.setEntityBean(entityBeanInfo);
        entityBeanInfo.setTable(tableInfo);

        tableInfo.setTableName(tableName);
        tableInfo.setSchemaName(dBConnectionFactory.getDriverInfo().getDriverSchema());
        tableInfo.setTableComment(tableName);

        try {
            if (conn == null || conn.isClosed()) {
                conn = dBConnectionFactory.getConnection();
            }

            //====================查找主键==============================
            dm = conn.getMetaData();
            List primarykeys = new ArrayList();
            ResultSet primarySet = null;
            primarySet = conn.getMetaData().getPrimaryKeys(DatabaseTableUtil.DATABASE_NAME, DatabaseTableUtil.SCHEMA_CATALOG, tableName);
            while (primarySet.next()) {
                primarykeys.add(primarySet.getString("COLUMN_NAME"));
            }
            primarySet.close();
            primarySet.getStatement().close();

            //=====================获得表所有字段的注释===================== 
            HashMap hm_fields=new HashMap();  //用来封装非Oracle数据库的字段的注释
            if (dBConnectionFactory.getDriverInfo().getDatabaseType().equals(DatabaseType.ORACLE)) {  //如果是Oracle数据库，必须取schema
                ResultSet commentsSet = conn.prepareStatement("select * from  (SELECT A.TABLE_NAME,A.COMMENTS table_comments  FROM USER_TAB_COMMENTS A) where table_name='" + tableName + "'").executeQuery();
                while (commentsSet.next()) {
                    if (commentsSet.getString("TABLE_COMMENTS") != null && !commentsSet.getString("TABLE_COMMENTS").equals("")) {
                        tableInfo.setTableComment(commentsSet.getString("TABLE_COMMENTS"));
                    }
                }
                commentsSet.close();
                commentsSet.getStatement().close();
            } else {   //非Oracle数据库，统一取出字段注释
                ResultSet commentsSet = dm.getColumns(DatabaseTableUtil.DATABASE_NAME, DatabaseTableUtil.SCHEMA_CATALOG, tableName, "%");
                while (commentsSet.next()) {
                    //System.out.println(commentsSet.getString("COLUMN_NAME") + ":" + commentsSet.getString("REMARKS"));
                    hm_fields.put(commentsSet.getString("COLUMN_NAME"), commentsSet.getString("REMARKS"));
                }
                commentsSet.close();
                commentsSet.getStatement().close();
            }

            //=============================================================


            //======================查找字段对象=========================
            ResultSet resultSet = conn.prepareStatement("select * from " + tableName + " where 1=2").executeQuery();
            ResultSet columnSet = null;
            int mk = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= mk; i++) {
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setFieldName(resultSet.getMetaData().getColumnName(i));
                fieldInfo.setFieldLabel(resultSet.getMetaData().getColumnLabel(i));
                fieldInfo.setFieldTableName(resultSet.getMetaData().getTableName(i));
                fieldInfo.setFieldSchemaName(resultSet.getMetaData().getSchemaName(i));
                fieldInfo.setFieldClassName(resultSet.getMetaData().getColumnClassName(i));
                fieldInfo.setFieldType(resultSet.getMetaData().getColumnType(i));
                fieldInfo.setFieldTypeName(resultSet.getMetaData().getColumnTypeName(i));
                if(resultSet.getMetaData().getColumnTypeName(i).equals("NUMBER")){
                	String type ="";
                	type = resultSet.getMetaData().getScale(i) > 0 ? "float":"long";
                	fieldInfo.setFieldClassName(type);
                }
                fieldInfo.setFieldDisplaySize(resultSet.getMetaData().getColumnDisplaySize(i));
                fieldInfo.setIsNullable(resultSet.getMetaData().isNullable(i));
                fieldInfo.setComment(fieldInfo.getFieldLabel());

                if (dBConnectionFactory.getDriverInfo().getDatabaseType().equals(DatabaseType.ORACLE)) {   //如果是Oracle数据库
                    columnSet = conn.prepareStatement("select * from (SELECT A.TABLE_NAME,A.COMMENTS table_comments,B.COLUMN_NAME,B.COMMENTS col_comments  FROM USER_TAB_COMMENTS A,USER_COL_COMMENTS B WHERE A.TABLE_NAME=B.TABLE_NAME) where TABLE_NAME='" + tableName + "' AND COLUMN_NAME='" + fieldInfo.getFieldName() + "'").executeQuery();
                    while (columnSet.next()) {
                        if (columnSet.getString("COL_COMMENTS") != null && !columnSet.getString("COL_COMMENTS").equals("")) {
                            fieldInfo.setComment(columnSet.getString("COL_COMMENTS"));
                        }
                    }
                    columnSet.close();
                    columnSet.getStatement().close();
                }else{     //非Oracle数据库
                    if(hm_fields.get(fieldInfo.getFieldName())!=null && !((String)hm_fields.get(fieldInfo.getFieldName())).equals("") ){
                        fieldInfo.setComment((String)hm_fields.get(fieldInfo.getFieldName()));
                    }
                }



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

            resultSet.getStatement().close();
            resultSet.close();
            
            
        } catch (Exception e) {
            throw new Exception((new StringBuilder("枚举字段列表出错，错误信息为：")).append(e.getMessage()).toString());
        }
    
        return tableInfo;
        /*
        List<TableInfo> tblist = new ArrayList();
        tblist.add(tableInfo);
        buildForeignKeyForTables(tblist);
        TableInfo ti = tblist.get(0);
        return ti;
        */
        //return tableInfo;
         
        
    }

    
    public TableInfo getTableInfo(String tableName,CodeGenerationWizard codeWizard)
            throws Exception {
    	this.setCodeWizard(codeWizard);
        TableInfo tableInfo = new TableInfo();
        EntityBeanInfo entityBeanInfo = new EntityBeanInfo();
        tableInfo.setEntityBean(entityBeanInfo);
        entityBeanInfo.setTable(tableInfo);

        tableInfo.setTableName(tableName);
        tableInfo.setSchemaName(dBConnectionFactory.getDriverInfo().getDriverSchema());
        tableInfo.setTableComment(tableName);

        try {
            if (conn == null || conn.isClosed()) {
                conn = dBConnectionFactory.getConnection();
            }

            //====================查找主键==============================
            dm = conn.getMetaData();
            List primarykeys = new ArrayList();
            ResultSet primarySet = null;
            primarySet = conn.getMetaData().getPrimaryKeys(DatabaseTableUtil.DATABASE_NAME, DatabaseTableUtil.SCHEMA_CATALOG, tableName);
            while (primarySet.next()) {
                primarykeys.add(primarySet.getString("COLUMN_NAME"));
            }
            primarySet.close();
            primarySet.getStatement().close();

            //=====================获得表所有字段的注释===================== 
            HashMap hm_fields=new HashMap();  //用来封装非Oracle数据库的字段的注释
            if (dBConnectionFactory.getDriverInfo().getDatabaseType().equals(DatabaseType.ORACLE)) {  //如果是Oracle数据库，必须取schema
                ResultSet commentsSet = conn.prepareStatement("select * from  (SELECT A.TABLE_NAME,A.COMMENTS table_comments  FROM USER_TAB_COMMENTS A) where table_name='" + tableName + "'").executeQuery();
                while (commentsSet.next()) {
                    if (commentsSet.getString("TABLE_COMMENTS") != null && !commentsSet.getString("TABLE_COMMENTS").equals("")) {
                        tableInfo.setTableComment(commentsSet.getString("TABLE_COMMENTS"));
                    }
                }
                commentsSet.close();
                commentsSet.getStatement().close();
            } else {   //非Oracle数据库，统一取出字段注释
                ResultSet commentsSet = dm.getColumns(DatabaseTableUtil.DATABASE_NAME, DatabaseTableUtil.SCHEMA_CATALOG, tableName, "%");
                while (commentsSet.next()) {
                    //System.out.println(commentsSet.getString("COLUMN_NAME") + ":" + commentsSet.getString("REMARKS"));
                    hm_fields.put(commentsSet.getString("COLUMN_NAME"), commentsSet.getString("REMARKS"));
                }
                commentsSet.close();
                commentsSet.getStatement().close();
            }

            //=============================================================


            //======================查找字段对象=========================
            ResultSet resultSet = conn.prepareStatement("select * from " + tableName + " where 1=2").executeQuery();
            ResultSet columnSet = null;
            int mk = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= mk; i++) {
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setFieldName(resultSet.getMetaData().getColumnName(i));
                fieldInfo.setFieldLabel(resultSet.getMetaData().getColumnLabel(i));
                fieldInfo.setFieldTableName(resultSet.getMetaData().getTableName(i));
                fieldInfo.setFieldSchemaName(resultSet.getMetaData().getSchemaName(i));
                fieldInfo.setFieldClassName(resultSet.getMetaData().getColumnClassName(i));
                fieldInfo.setFieldType(resultSet.getMetaData().getColumnType(i));
                fieldInfo.setFieldTypeName(resultSet.getMetaData().getColumnTypeName(i));
                if(resultSet.getMetaData().getColumnTypeName(i).equals("NUMBER")){
                	String type ="";
                	type = resultSet.getMetaData().getScale(i) > 0 ? "float":"long";
                	fieldInfo.setFieldClassName(type);
                }
                fieldInfo.setFieldDisplaySize(resultSet.getMetaData().getColumnDisplaySize(i));
                fieldInfo.setIsNullable(resultSet.getMetaData().isNullable(i));
                fieldInfo.setComment(fieldInfo.getFieldLabel());

                if (dBConnectionFactory.getDriverInfo().getDatabaseType().equals(DatabaseType.ORACLE)) {   //如果是Oracle数据库
                    columnSet = conn.prepareStatement("select * from (SELECT A.TABLE_NAME,A.COMMENTS table_comments,B.COLUMN_NAME,B.COMMENTS col_comments  FROM USER_TAB_COMMENTS A,USER_COL_COMMENTS B WHERE A.TABLE_NAME=B.TABLE_NAME) where TABLE_NAME='" + tableName + "' AND COLUMN_NAME='" + fieldInfo.getFieldName() + "'").executeQuery();
                    while (columnSet.next()) {
                        if (columnSet.getString("COL_COMMENTS") != null && !columnSet.getString("COL_COMMENTS").equals("")) {
                            fieldInfo.setComment(columnSet.getString("COL_COMMENTS"));
                        }
                    }
                    columnSet.close();
                    columnSet.getStatement().close();
                }else{     //非Oracle数据库
                    if(hm_fields.get(fieldInfo.getFieldName())!=null && !((String)hm_fields.get(fieldInfo.getFieldName())).equals("") ){
                        fieldInfo.setComment((String)hm_fields.get(fieldInfo.getFieldName()));
                    }
                }



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

            resultSet.getStatement().close();
            resultSet.close();
            
            
        } catch (Exception e) {
            throw new Exception((new StringBuilder("枚举字段列表出错，错误信息为：")).append(e.getMessage()).toString());
        }
    
        return tableInfo;
        /*
        List<TableInfo> tblist = new ArrayList();
        tblist.add(tableInfo);
        buildForeignKeyForTables(tblist);
        TableInfo ti = tblist.get(0);
        return ti;
        */
        //return tableInfo;
         
        
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
    public static String buildStandParamName(String fieldName) {
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
        buildParamsFromFieldsNotPrimary(entityBeanInfo, entityBeanInfo.getTable().getFields());
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
            /**
             * 通用模式
             */
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
    /**
     * 根据字段列表来获得属性列表
     * @param entityBeanInfo
     * @param fields
     */
    private void buildParamsFromFieldsNotPrimary(EntityBeanInfo entityBeanInfo, List fields) {
        if (fields == null) {
            return;
        }
        for (Iterator i = fields.iterator(); i.hasNext();) {
            FieldInfo fieldInfo = (FieldInfo) i.next();
            if (fieldInfo == null) {
                continue;
            }
            /**
             * 通用模式
             */
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
        
        /**
         * 根据类型组装EntityParamInfo
         */
        Assemble(entityBeanInfo,fields,infoType.values());
    }
	private void Assemble(EntityBeanInfo entityBeanInfo, List fields,infoType[] infotype) {
		if(this.getCodeWizard()==null)
			return;
		/**
		 * 匹配tableview 中的序列 
		 */
		for(infoType type:infotype){
			System.out.println(type.name());
			List<EntityParamInfo> tempList = new ArrayList<EntityParamInfo>();
			for(int i =0;i<fields.size();i++){
				 FieldInfo fieldInfo = (FieldInfo) fields.get(i);
	            if (fieldInfo == null) {
	                continue;
	            }
	            entityBeanInfo.getTable().getTableName();
	    		//form表单 info信息
	            TableViewer table = switchTable(type,entityBeanInfo.getTable());
	    		ListDataField dataField = (ListDataField) table.getElementAt(i);
	    		if(dataField!=null){
	    			fieldInfo.setHidden(dataField.getHidden());
		    		fieldInfo.setSelectTypeFlag(dataField.getDateformat());
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
	            tempList.add(paramInfo);
	        }
			entityBeanInfo.getTypeParams().put(type.name(), tempList);
		}
		
		
	}

	private boolean switchHidden(int clstype) {
		switch (clstype) {
		case 1:
			return true;
		case 0:
			return false;
		default:
			break;
		}
		return false;
	}

	private TableViewer switchTable(infoType type,TableInfo info) {
		TableViewer talbe= null;
		if(type.equals(type.FORM)){
			if(getCodeWizard().templeteSelectPage.getIsMain()){
				if(getCodeWizard().getSubTableNames().get(0).equals(info.getTableName())){
					talbe = this.getCodeWizard().gridpage.columnCptFormSub.tv;
				}
			}else{
				talbe = this.getCodeWizard().gridpage.columnCptForm.tv;
			}
			
		}else if(type.equals(type.TABLE)){
			if(getCodeWizard().templeteSelectPage.getIsMain()){
				if(getCodeWizard().getSubTableNames().get(0).equals(info.getTableName())){
					talbe = this.getCodeWizard().gridpage.columnCptTableSub.tv;
				}
			}else{
				talbe = this.getCodeWizard().gridpage.columnCptTable.tv;
			}
			
		}else if(type.equals(type.SERATCH)){
			if(getCodeWizard().templeteSelectPage.getIsMain()){
				if(getCodeWizard().getSubTableNames().get(0).equals(info.getTableName())){
					talbe = this.getCodeWizard().gridpage.columnCptSerachSub.tv;
				}
			}else{
				talbe = this.getCodeWizard().gridpage.columnCptSerach.tv;
			}
			
		}else{
			if(getCodeWizard().templeteSelectPage.getIsMain()){
				if(getCodeWizard().getSubTableNames().get(0).equals(info.getTableName())){
					talbe = this.getCodeWizard().gridpage.columnCptFormSub.tv;
				}
			}else{
				talbe = this.getCodeWizard().gridpage.columnCptForm.tv;
			}
		}
		return talbe;
	}

	private void AssembleFileInfo(FieldInfo fieldInfo) {
		
		
		
		
		
		
	}

	public CodeGenerationWizard getCodeWizard() {
		return codeWizard;
	}

	public void setCodeWizard(CodeGenerationWizard codeWizard) {
		this.codeWizard = codeWizard;
	}
	
}
