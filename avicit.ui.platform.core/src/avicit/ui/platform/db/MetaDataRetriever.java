package avicit.ui.platform.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Session;

import avicit.ui.platform.common.data.CtxWrapper;
import avicit.ui.platform.common.util.CommonUtil;

public class MetaDataRetriever{

	private Container container;
	DatabaseMetaData metadata;
	Connection con;
	Session session;
	
	private CtxWrapper wrapper;

	public MetaDataRetriever(CtxWrapper wr) {
		this.wrapper = wr;
		container = new Container();
		this.session = wrapper.getDao().getHibernateTemplate().getSessionFactory().openSession();
		con = session.connection();
		try {
			metadata = con.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			//����ˢ��table��ʱ���ȡMap
			populateTableData();
		}
		catch (Exception e) {
//			errorHandler.onError(null, e);
		}
		finally {
			try {
				if (null != metadata && null != metadata.getConnection()) metadata.getConnection().close();
			}
			catch (SQLException sqle) {}
		}
	}

	public DBTable getDBTable(String tableName){
		try{
			if(container.getTables() == null)
				this.fectchTables();
			tableName = tableName.toUpperCase();
			DBTable dbtable = (DBTable) container.getTables().get(tableName);
			if(dbtable != null && dbtable.getColumns().size()==0)
			{
				String[] tablestr = new String[1];
				tablestr[0] = tableName;
				this.populateTableData(tablestr);
			}
			return dbtable;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public Map getDBTables(){
		try{
			if(container.getTables() == null)
				this.fectchTables();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return container.getTables();
	}
	
	/**
	 * Return the number of tables that match the schema pattern and table pattern given
	 * @param metadata
	 * @param schemaPattern
	 * @param tablePattern
	 * @return the number of tables
	 * @throws SQLException
	 */
	public int getTableCount() throws SQLException {
		String[] names = {"TABLE"};
		String _schema = null;
		String _table = null;
		ResultSet rs = null;
		try {
			int count = 0;
			rs = metadata.getTables(null, _schema, _table, names);
			String TABLE_TYPE = "TABLE_TYPE";
			String SYSTEM = "SYSTEM";
			while (rs.next()) { 
				String tableType = rs.getString(TABLE_TYPE);
				if (null == tableType || tableType.toUpperCase().indexOf(SYSTEM) < 0) {
					count++;
				}
			}
			return count;
		}
		finally {
			if (null != rs) rs.close();
		}
	}

	/**
	 * Load the container with table objects that only contain the name.
	 * Start the thread or call populateTableData to load the columns and keys.
	 * @param container
	 * @param metadata
	 * @param schemaPattern
	 * @param tablePattern
	 * @throws SQLException
	 */
	private void fectchTables () throws SQLException {
		String[] names = {"TABLE"};
		String _schema = metadata.getUserName();
		ResultSet rs = null;
		try {
			rs = metadata.getTables(null, _schema, null, names);
			Map tables = new HashMap();
			String TABLE_NAME = "TABLE_NAME";
			String TABLE_TYPE = "TABLE_TYPE";
			String SYSTEM = "SYSTEM";
			while (rs.next()) { 
				DBTable table = new DBTable(container, rs.getString(TABLE_NAME)); 
				String tableType = rs.getString(TABLE_TYPE);
				if (null == tableType || tableType.toUpperCase().indexOf(SYSTEM) < 0) {
					tables.put(table.getName(), table);
				}
				table.setParent(container);
			}
			container.setTables(tables);
		}
		finally {
			if (null != rs) rs.close();
		}
	}

	/**
	 * Populate the column and key information for the tables.
	 * @param metadata
	 * @param container the table container
	 * @throws SQLException
	 */
	public void populateTableData () throws SQLException {
		Map tables = container.getTables();
		for (Iterator i=tables.values().iterator(); i.hasNext(); ) {
			DBTable table = (DBTable) i.next();
			readTableColumns(metadata, table);
			readTableKeys(metadata, table, tables);
			table.init();
		}
		/*
		for (Iterator i=tables.values().iterator(); i.hasNext(); ) {
			DBTable table = (DBTable) i.next();
			readTableKeys(metadata, table, tables);
		}
		for (Iterator i=tables.values().iterator(); i.hasNext(); ) {
			DBTable table = (DBTable) i.next();
			table.init();
		}
		*/
		container.fullyLoaded = true;
	}

	//����populateTavleData������ֻ���ѡ�б�
	/**
	 * Populate the column and key information for the tables.
	 * @param metadata
	 * @param container the table container
	 * @throws SQLException
	 */
	public void populateTableData (String[] selectedTableNames) throws SQLException {
		Map tables = container.getTables();
		for (Iterator i=tables.values().iterator(); i.hasNext(); ) {
			//ȡ��table
			DBTable table = (DBTable) i.next();
			//ѭ�����е�ѡ��table
			for (int j=0; j<selectedTableNames.length; j++) {
				
				if (selectedTableNames[j].equals(table.getName())) {
					readTableColumns(metadata, table);
					readTableKeys(metadata, table, tables);
					table.init();
					//���ƥ���������ѭ��������ѭ��
					break;
				}
			}

		}
		/*
		for (Iterator i=tables.values().iterator(); i.hasNext(); ) {
			DBTable table = (DBTable) i.next();
			readTableKeys(metadata, table, tables);
		}
		for (Iterator i=tables.values().iterator(); i.hasNext(); ) {
			DBTable table = (DBTable) i.next();
			table.init();
		}
		*/
		container.fullyLoaded = true;
	}

	
	/**
	 * Read the columns from the DatabaseMetaData and notify the given table of the colums
	 * @param meta
	 * @param table
	 * @throws SQLException
	 */
	private void readTableColumns(DatabaseMetaData meta, DBTable table) throws SQLException { 
		ResultSet columns = null;
		try {
			columns = meta.getColumns(null, meta.getUserName(), table.getName(), "%"); 
			while (columns.next()) { 
				String columnName = columns.getString("COLUMN_NAME"); 
				String datatype = columns.getString("TYPE_NAME"); 
				int datasize = columns.getInt("COLUMN_SIZE"); 
				int digits = columns.getInt("DECIMAL_DIGITS"); 
				int nullable = columns.getInt("NULLABLE"); 
				DBColumn newColumn = new DBColumn(table, columnName, datatype, datasize, digits, nullable);
				table.notifyColumn(newColumn);
			}
		}
		finally {
			if (null != columns) columns.close();
		}
	}

	/**
	 * Read the primary and foreign keys from the DatabaseMetaData and notify the given table of the keys
	 * @param meta
	 * @param table
	 * @param tables
	 * @throws SQLException
	 */
	private static void readTableKeys(DatabaseMetaData meta, DBTable table, Map tables) throws SQLException {
		ResultSet keys = null;
		try {
			// primary keys
			keys = meta.getPrimaryKeys(null, null, table.getName());
			while (keys.next()) {
				String tableName = keys.getString("TABLE_NAME");
				String columnName = keys.getString("COLUMN_NAME");
				table = (DBTable) tables.get(checkName(tableName));
				table.notifyPrimaryKey(checkName(columnName));
			}

			/* foreign keys
			keys = meta.getImportedKeys(null, null, table.getName());
			List rels = new ArrayList();
			while (keys.next()) {
				String pkTableName = keys.getString("PKTABLE_NAME");
				pkTableName = keys.getString("PKTABLE_NAME");
				String pkColumnName = keys.getString("PKCOLUMN_NAME");
				String fkTableName = keys.getString("FKTABLE_NAME");
				String fkColumnName = keys.getString("FKCOLUMN_NAME");
				DBTable pkTable = (DBTable) tables.get(checkName(pkTableName));
				DBTable fkTable = (DBTable) tables.get(checkName(fkTableName));
				if (null != pkTable && null != fkTable) {
					DBColumn pkColumn = pkTable.getColumn(checkName(pkColumnName));
					if (null != pkColumn)
						table.notifyForeignKey(checkName(fkColumnName), pkColumn);
				}
			}
			*/
		}
		finally {
			if (null != keys) keys.close();
		}
	}
	
	public Connection getConnection()
	{
		return con;
	}
	
	
	private static String checkName (String s) {
		if (null == s) return null;
		s = CommonUtil.stringReplace(s, "`", "");
		return s;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

}