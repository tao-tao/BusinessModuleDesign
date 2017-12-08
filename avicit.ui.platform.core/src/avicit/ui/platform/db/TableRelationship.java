package avicit.ui.platform.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import avicit.ui.platform.common.util.CommonUtil;


public class TableRelationship {

	private DBTable parentTable;
	private DBTable childTable;
	private List joins;
	private Container container;

	public TableRelationship (DBTable parentTable, DBTable childTable, Container container) {
		this.parentTable = parentTable;
		this.childTable = childTable;
		this.container = container;
	}

	// cache
	private Boolean parentTableRelationship;
	private DBColumn manyToManyChildColumn;
	private DBColumn manyToManyAltChildColumn;
	
	public DBTable getChildTable() {
		return childTable;
	}
	public void setChildTable(DBTable childTable) {
		this.childTable = childTable;
	}
	public List getJoins() {
		if (null == joins) joins = new ArrayList();
		return joins;
	}
	public void addJoin(TableJoin join) {
		getJoins().add(join);
	}
	public void setJoins(List joins) {
		this.joins = joins;
	}
	public DBTable getParentTable() {
		return parentTable;
	}
	public void setParentTable(DBTable parentTable) {
		this.parentTable = parentTable;
	}

	/**
	 * Return true if the number of joins associated with the relationship equal the amount
	 * of primary keys contained in the parent table.
	 */
	public boolean isComplete() {
		return parentTable.getPkColumns().size() == joins.size();
	}

	/**
	 * Return true if this relation represents a subclass relationshp and false if not
	 */
	public boolean isParentTableRelationship() {
		if (parentTable.getPkColumns().size() == childTable.getPkColumns().size()) {
			HashMap matchedColumns = new HashMap();
			for (Iterator i=joins.iterator(); i.hasNext(); ) {
				TableJoin join = (TableJoin) i.next();
				if (join.getForeignKey().isPrimaryKey()) {
					matchedColumns.put(join.getPrimaryKey().getName(), join.getForeignKey());
				}
			}
			boolean allColumnsMatch = true;
			for (Iterator i=parentTable.getPkColumns().iterator(); i.hasNext(); ) {
				DBColumn column = (DBColumn) i.next();
				if (null == matchedColumns.get(column.getName())) {
					allColumnsMatch = false;
					break;
				}
			}
			parentTableRelationship = new Boolean(allColumnsMatch);
		}
		return parentTableRelationship.booleanValue();
	}

	/**
	 * Return true if this relationship reprents a many-to-many relationship and false otherwise
	 */
	public boolean isManyToManyRelationship () {
		DBColumn column = null;
		int count = 0;
		for (Iterator i=getChildTable().getPkColumns().iterator(); i.hasNext(); ) {
			column = (DBColumn) i.next();
			if (column.isForeignKey()) count++;
			else return false;
		}
		return count == 2;
	}

	/**
	 * Return true if this relationship reprents a one-to-many relationship and false otherwise
	 */
	public boolean isOneToManyRelationship () {
		return (!isManyToManyRelationship());
	}

	/**
	 * Return the opposite end of the many-to-many from the parent table of this relationshp
	 * or null if this relationship is not a many-to-many
	 */
	public DBTable getManyToManyTable () {
		if (null != getManyToManyAltChildColumn()) return getManyToManyAltChildColumn().fkParentKey.getTable();
		else return null;
	}

	/**
	 * Return the child column pointing to the opposite end of the many-to-many relationship
	 */
	public DBColumn getManyToManyChildColumn () {
		if (null == manyToManyChildColumn) {
			if (!isManyToManyRelationship()) return null;
			for (Iterator i=childTable.getPkColumns().iterator(); i.hasNext(); ) {
				DBColumn col = (DBColumn) i.next();
				if (col.isForeignKey() && col.fkParentKey.getTable().getName().equals(parentTable.getName())) {
					manyToManyChildColumn = col;
					break;
				}
			}
		}
		return manyToManyChildColumn;
	}

	public DBColumn getManyToManyAltChildColumn () {
		if (null == manyToManyAltChildColumn) {
			if (!isManyToManyRelationship()) return null;
			for (Iterator i=childTable.getPkColumns().iterator(); i.hasNext(); ) {
				DBColumn col = (DBColumn) i.next();
				if (col.isForeignKey() && !col.fkParentKey.getTable().getName().equals(parentTable.getName())) {
					manyToManyAltChildColumn = col;
					break;
				}
			}
		}
		return manyToManyAltChildColumn;
	}

	/**
	 * Return the property name that should be associated with this list
	 */
	public String getListName () {
		if (isManyToManyRelationship()) return getManyToManyListName();
		else return getOneToManyListName();
	}

	private String getOneToManyListName () {
		boolean firstLetterUpper = true;
		if (null != container.getProperty("StartLowerCase")) firstLetterUpper = false;
		int count = 0;
		for (Iterator i=parentTable.getOneToManyRelationships().iterator(); i.hasNext(); ) {
			if (((TableRelationship) i.next()).getChildTable().getName().equals(childTable.getName())) {
				count ++;
			}
		}
		String name = null;
		if (count == 1) {
			name = CommonUtil.firstLetterUpper(getListName(childTable.getClassName()));
		}
		else {
			StringBuffer sb = new StringBuffer();
			for (Iterator i=joins.iterator(); i.hasNext(); ) {
				if (sb.length() > 0) sb.append("And");
				TableJoin join = (TableJoin) i.next();
				sb.append(join.getForeignKey().getPropName());
			}
			name = CommonUtil.firstLetterUpper(getListName(childTable.getClassName())) + "By" + sb.toString();
		}
		String javaName = CommonUtil.getJavaName(childTable.getName());
		if (name.startsWith(javaName) && Character.isUpperCase(name.toCharArray()[javaName.length()])) {
			name = CommonUtil.firstLetterUpper(getListName(name.substring(javaName.length(), name.length())));
		}
		if (!firstLetterUpper) {
			name = CommonUtil.firstLetterLower(name);
		}
		return name;
	}

	private String getManyToManyListName () {
		boolean firstLetterUpper = true;
		if (null != container.getProperty("StartLowerCase")) firstLetterUpper = false;
		int count = 0;
		for (Iterator i=parentTable.getManyToManyRelationships().iterator(); i.hasNext(); ) {
			TableRelationship rel = (TableRelationship) i.next();
			if (rel.getManyToManyTable().getName().equals(getManyToManyTable().getName())) {
				count ++;
			}
		}
		String name = null;
		if (count == 1) {
			name = CommonUtil.firstLetterUpper(getManyToManyTable().getClassName());
		}
		else {
			if (getChildTable().getHibernateClassName().toLowerCase().startsWith(getParentTable().getHibernateClassName().toLowerCase())) {
				name = CommonUtil.firstLetterUpper(getChildTable().getHibernateClassName().substring(getParentTable().getHibernateClassName().length(), getChildTable().getHibernateClassName().length()));
			}
			else if (getChildTable().getHibernateClassName().toLowerCase().endsWith(getParentTable().getHibernateClassName().toLowerCase())) {
				name = CommonUtil.firstLetterUpper(getChildTable().getHibernateClassName().substring(0, getChildTable().getHibernateClassName().length() - getParentTable().getHibernateClassName().length()));
			}
			else {
				name = CommonUtil.firstLetterUpper(getChildTable().getHibernateClassName());
			}
		}
		name = getListName(name);
		if (!firstLetterUpper) {
			name = CommonUtil.firstLetterLower(name);
		}
		return name;
	}

	private String getListName (String name) {
		if (null == name) return null;
		else if (name.endsWith("y")) {
			return name.substring(0, name.length()-1) + "ies";
		}
		else if (name.endsWith("s")) {
			return name;
		}
		else return name + "s";
	}
}