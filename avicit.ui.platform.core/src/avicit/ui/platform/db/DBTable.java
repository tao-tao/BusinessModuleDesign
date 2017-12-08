package avicit.ui.platform.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import avicit.ui.platform.common.util.CommonUtil;


public class DBTable implements Comparable {
	
	private Container parent;
	private String name;
	private String hibernateClassName;
	private String metaData;
	private Container container;

	private List columns = new ArrayList();
	private List childRelationships = new ArrayList();
	private List parentRelationships = new ArrayList();
	private List subClasses = new ArrayList();
	
	// cache
	private List oneToManyRelationships = new ArrayList();
	private List manyToManyRelationships = new ArrayList();
	private Map pKeys;
	private Map fKeys;
	private Map columnMap = new HashMap();
	private TableRelationship parentTableRelationship;
	private Boolean hasParentTable;

	
	public void removeColumn (String columnName) {
		DBColumn column = getColumn(columnName);
		pKeys.remove(column);
		fKeys.remove(column);
		columnMap.remove(column.getName());
		columns.remove(column);
	}
	
	public DBTable (Container container) {
		this.container = container;
	};

	/**
	 * Constructor
	 * @param name the name of the table
	 */
	public DBTable (Container container, String name) {
		setName(name);
		this.container = container;
	}

	/**
	 * Constructor
	 * @param name the name of the table
	 */
	public DBTable (String name, Container container) {
		setName(name);
		this.container = container;
	}

	/**
	 * This should be called after all tables have been loaded.
	 */
	public void init () {
		if (null == pKeys) pKeys = new HashMap();
		if (null == fKeys) fKeys = new HashMap();
		loadRelationships();
		getParentTableRelationship();
		for (Iterator i=childRelationships.iterator(); i.hasNext(); ) {
			TableRelationship relationship = (TableRelationship) i.next();
			relationship.getParentTable().notifyChildRelationship(relationship);
		}
	}

	/**
	 * Load all of the relationships based on the foreign keys within this table
	 */
	private void loadRelationships () {
		Map usedKeys = new HashMap(fKeys.size());
		// load the single keys
		for (Iterator i=fKeys.values().iterator(); i.hasNext(); ) {
			DBColumn column = (DBColumn) i.next();
			if (column.fkParentKey.getTable().getPkColumns().size() == 1) {
				TableRelationship tr = new TableRelationship(column.fkParentKey.getTable(), this, container);
				tr.addJoin(new TableJoin(column.fkParentKey, column));
				childRelationships.add(tr);
				usedKeys.put(column.getName(), column);
			}
		}
		// load the clustered keys
		if (usedKeys.size() != fKeys.size()) {
			// TODO: implement the clustered FK functionality
		}
	}

	/**
	 * Set the container for this table
	 */
	public void setContainer (Container container) {
		this.container = container;
	}

	/**
	 * Return the TableRelationship associated with the parent class in a subclass relationship
	 * or null if N/A.
	 * @return the parent TableRelationship
	 */
	public TableRelationship getParentTableRelationship () {
		if (null == hasParentTable) {
			boolean abc = true;
			TableRelationship keyRel = null;
			for (Iterator i=childRelationships.iterator(); i.hasNext(); ) {
				TableRelationship tableRelationship = (TableRelationship) i.next();
				if (tableRelationship.getJoins().size() == 1
						&& ((TableJoin) tableRelationship.getJoins().get(0)).getForeignKey().isPrimaryKey()) {
					if (null == keyRel) keyRel = tableRelationship;
					else {
						keyRel = null;
						break;
					}
				}
			}
			if (null == keyRel) hasParentTable = Boolean.FALSE;
			else hasParentTable = Boolean.TRUE;
			parentTableRelationship = keyRel;
		}
		return parentTableRelationship;
	}

	/**
	 * Notify this table of a primary key
	 */
	public void notifyPrimaryKey (String columnName) {
		DBColumn column = (DBColumn) columnMap.get(columnName);
		if (null != column) {
			if (null == pKeys) 
				pKeys = new HashMap();
			pKeys.put(column.getName(), column);
			column.primaryKey = true;
		}
	}

	/**
	 * Notify this table of a column to be added
	 */
	public void notifyColumn (DBColumn column) {
		column.setTable(this);
		columns.add(column);
		columnMap.put(column.getName(), column);
	}

	/**
	 * Notify this table of a column that is a foreign key to another table
	 * @param columnName the foreign key column name on the child table
	 * @param parentKey the primary key column on the parent table
	 */
	public void notifyForeignKey (String columnName, DBColumn parentKey) {
		DBColumn column = (DBColumn) columnMap.get(columnName);
		if (null != column) {
			column.fkParentKey = parentKey;
			if (null == fKeys) fKeys = new HashMap();
			fKeys.put(column.getName(), column);
		}
	}

	/**
	 * Notify the parent table of the child relationship which will result in some type of collection
	 * @param relationship the table relationship
	 */
	public void notifyChildRelationship (TableRelationship relationship) {
		if (null != relationship.getChildTable().getParentTableRelationship()
				&& relationship.equals(relationship.getChildTable().getParentTableRelationship())) return;
		if (relationship.isOneToManyRelationship()) {
			oneToManyRelationships.add(relationship);
		}
		else if (relationship.isManyToManyRelationship()) {
			manyToManyRelationships.add(relationship);
		}
	}

	/**
	 * Notify the parent table of a child subclass to the parent table
	 * @param subclassRelationship
	 */
	public void notifySubclass (TableRelationship subclassRelationship) {
		subClasses.add(subclassRelationship);
	}

	/**
	 * Return a List of TableRelationships representing the subclasses of this class 
	 * @return a List of TableRelationships
	 */
	public List getSubClasses() {
		return subClasses;
	}

	/**
	 * Convienance method to see if this table has any subclasses
	 */
	public boolean hasSubClasses () {
		return getSubClasses().size() > 0;
	}

	/**
	 * Return the properties associated with this action
	 */
	protected Properties getProperties () {
		return container.getProperties();
	}

	/**
	 * Convienance method to determine if this table has a composite key
	 */
	public boolean hasCompositeKey () {
		return getPkColumns().size() > 1;
	}

	/**
	 * Convienance method to determine if this table has a composite key and no other fields
	 */
	public boolean isCompositeKeyOnly () {
		return (getPkColumns().size() > 1 && getPkColumns().size() == getColumns().size());
	}

	/**
	 * Return true if the column is a foreign key and false if not
	 * @param column
	 * @return
	 */
	public boolean isForeignKey (DBColumn column) {
		if (null == fKeys) return false;
		else return (null != fKeys.get(column.getName()));
	}

	/**
	 * Return a List of foreign key columns
	 * @return a List of DBColumn objects
	 */
	public Collection getFKColumns () {
		if (null == fKeys) return new ArrayList(0);
		return fKeys.values();
	}
	
	/**
	 * Return the relationship associates with the given column or null if N/A
	 * @param column
	 * @return
	 */
	public TableRelationship getRelationship (DBColumn column) {
		for (Iterator i = oneToManyRelationships.iterator(); i.hasNext(); ) {
			TableRelationship rel = (TableRelationship) i.next();
			for (Iterator j = rel.getJoins().iterator(); j.hasNext(); ) {
				TableJoin join = (TableJoin) j.next();
				if (join.getForeignKey().getName().equals(column.getName())) return rel;
			}
		}
		for (Iterator i = manyToManyRelationships.iterator(); i.hasNext(); ) {
			TableRelationship rel = (TableRelationship) i.next();
			for (Iterator j = rel.getJoins().iterator(); j.hasNext(); ) {
				TableJoin join = (TableJoin) j.next();
				if (join.getForeignKey().getName().equals(column.getName())) return rel;
			}
		}
		return null;
	}

	/**
	 * Return all of the columns associated with the primary key
	 * @return a list of DBColumn objects
	 */
	public Collection getPkColumns () {
		if (null == pKeys) return new ArrayList(0);
		return pKeys.values();
	}

	/**
	 * Return all of the one-to-many relationships
	 * @Return a List of TableRelationship objects
	 */
	public List getOneToManyRelationships () {
		return oneToManyRelationships;
	}

	/**
	 * Return all of the many-to-many relationships
	 * @Return a List of TableRelationship objects
	 */
	public List getManyToManyRelationships () {
		return manyToManyRelationships;
	}
	
	
	
/*
	public String packageStr;
	public Template template;
	public String getSubClassMapping() {
		try {
			VelocityContext context = new VelocityContext();
			context.put("packageName", packageStr);
			context.put("isSubClass", Boolean.TRUE);
			context.put("table", this);
			for (Iterator i2=properties.entrySet().iterator(); i2.hasNext(); ) {
				Map.Entry entry = (Map.Entry) i2.next();
				context.put((String) entry.getKey(), entry.getValue());
			}
			StringWriter sw = new StringWriter();
			if (null != props.get("UseProxies")) {
				StringWriter proxyClassName = new StringWriter();
				VelocityContext subCntext = new VelocityContext();
				subCntext.put("className", getHibernateClassName());
				Constants.templateGenerator.evaluate(
						subCntext,
						proxyClassName,
						Velocity.class.getName(),
						props.getProperty("ProxyClassName"));
				context.put("proxyClass", proxyClassName.toString());
			}
			template.merge(context, sw);
			return sw.toString();
		}
		catch (Exception e) {
//			Plugin.logError(e);
			throw new RuntimeException(e);
		}
	}
	*/

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	
	public String getClassName() {
		return CommonUtil.getJavaNameCap(getName());
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return all columns that belong to this table
	 * @return a list of DBColumn objects
	 */
	public List getColumns () {
		return columns;
	}

	/**
	 * Return the column matching the given name or null if no match if found
	 * @param columnName the column name
	 */
	public DBColumn getColumn (String columnName) {
		return (DBColumn) columnMap.get(columnName);
	}

	/**
	 * Return all child (foreign key) relationships
	 * @return a list of TableRelationship objects
	 */
	public List getChildRelationships () {
		return childRelationships;
	}

	public void setHibernateClassName (String hibernateClassName) {
		this.hibernateClassName = hibernateClassName;
	}
	
	public String getHibernateClassName () {
		if (null != hibernateClassName) return hibernateClassName;
		else {
			String s = CommonUtil.getPropName(getName())+"BO";
			if (s.equals("Class") ||
					s.equals("Interface") ||
					s.equals("Public") ||
					s.equals("Protected") ||
					s.equals("Private") ||
					s.equals("Extends") ||
					s.equals("Implements") ||
					s.equals("Void") ||
					s.equals("Static")) {
				s = s + "Instance";
				this.hibernateClassName = s;
			}
			return s;
		}
	}

	/**
	 * @return Returns the metaData.
	 */
	public String getMetaData() {
		return metaData;
	}
	/**
	 * @param metaData The metaData to set.
	 */
	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}

	public int compareTo(Object arg0) {
		if (null == arg0 || !(arg0 instanceof DBTable)) return -1;
		else return getName().compareTo(((DBTable) arg0).getName());
	}

	public void setColumns(List columns) {
		this.columns = columns;
	}

	public Container getParent() {
		return parent;
	}

	public void setParent(Container parent) {
		this.parent = parent;
	}
}