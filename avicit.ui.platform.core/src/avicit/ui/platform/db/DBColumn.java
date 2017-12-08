package avicit.ui.platform.db;

import java.util.Properties;

import avicit.ui.platform.common.util.CommonUtil;


public class DBColumn {
	private DBTable table;
	private String name;
	private String dataType;
	private int size;
	private int digits;
	private int nullable;
	private String metaData;
	boolean primaryKey;
	DBColumn fkParentKey;
	
	private String fkPropName;
	private String propName ;
	private String hibernateType;

	public DBColumn() {};
	
	public DBColumn (DBTable table, String name, String dataType, int size, int digits, int nullable) {
		setName(name);
		setDataType(dataType);
		if (size <= 1024) {
			setSize(size);
		}
		setDigits(digits);
		setNullable(nullable);
	}

	/**
	 * Return true if this column represents a primary key and false if not
	 */
	public boolean isPrimaryKey () {
		return primaryKey;
	}

	/**
	 * Return true if this column represents a foreign key and false if not
	 */
	public boolean isForeignKey () {
		return null != fkParentKey;
	}
	
	public DBTable getParentTable () {
		if (null == fkParentKey) return null;
		else return fkParentKey.getTable();
	}

	protected Properties getProperties () {
		return table.getProperties();
	}

	/**
	 * Set the Hibernate property name for this column
	 * @param propName
	 */
	public void setPropName (String propName) {
		this.propName = propName;
		this.fkPropName = propName;
	}

	/**
	 * Return the Hibernate property name for this column
	 */
	public String getPropName () {
		return getPropName (true);
	}
	
	public String getPropName (boolean checkFK) {
		boolean firstLetterUpper = false;//true;  modified by wy 070926
		if (null != getProperties() && null != getProperties().getProperty("StartLowerCase")) firstLetterUpper = false;
		if (isPrimaryKey() && getTable().getPkColumns().size() == 1) {
			if (checkFK && isForeignKey()) return getFKPropName();
			else if (firstLetterUpper) return propName = CommonUtil.firstLetterLower(CommonUtil.getJavaNameCap(getName().toLowerCase()));
			else return propName = CommonUtil.firstLetterLower(CommonUtil.getJavaNameCap(getName().toLowerCase()));
		}
		if (checkFK && isForeignKey()) return getFKPropName();
		else {
			if (null == propName) {
				if (getName().toUpperCase().equals(getName())) {
					if (!firstLetterUpper)
						propName = CommonUtil.firstLetterLower(CommonUtil.getJavaNameCap(getName().toLowerCase()));
					else
						propName = CommonUtil.firstLetterUpper(CommonUtil.getJavaNameCap(getName().toLowerCase()));
				}
				else {
					if (!firstLetterUpper)
						propName = CommonUtil.firstLetterLower(CommonUtil.getJavaNameCap(getName()));
					else
						propName = CommonUtil.firstLetterUpper(CommonUtil.getJavaNameCap(getName()));
				}
				if (((propName.toUpperCase().startsWith("IS") && propName.length() > 2 && Character.isUpperCase(propName.toCharArray()[2]))
						|| (propName.toUpperCase().startsWith("HAS") && propName.length() > 3 && Character.isUpperCase(propName.toCharArray()[3])))
						&& getSize() == 1) {
					int size = 2;
					if (propName.toUpperCase().startsWith("HAS")) size = 3;
					if (!firstLetterUpper)
						propName = CommonUtil.firstLetterLower(propName.substring(size, propName.length()));
					else
						propName = CommonUtil.firstLetterUpper(propName.substring(size, propName.length()));
					hibernateType = "true_false";
				}
			}
		}
		return propName;
	}

	private String getFKPropName () {
		boolean firstLetterUpper = true;
		if (null != getProperties().getProperty("StartLowerCase")) firstLetterUpper = false;
		if (null == fkPropName) {
			String propName = getPropName(false);
			if (propName.endsWith("Id")) {
				if (!firstLetterUpper)
					fkPropName = CommonUtil.firstLetterLower(propName.substring(0, propName.length() - 2));
				else
					fkPropName = CommonUtil.firstLetterUpper(propName.substring(0, propName.length() - 2));
			}
			else if (propName.endsWith("Oid")) {
				if (!firstLetterUpper)
					fkPropName = CommonUtil.firstLetterLower(propName.substring(0, propName.length() - 3));
				else
					fkPropName = CommonUtil.firstLetterUpper(propName.substring(0, propName.length() - 3));
			}
			else fkPropName = getPropName();
		}
		return fkPropName;
	}

	/**
	 * Return true if this column can be null and false if not
	 */
	public boolean isNull () {
		return nullable == 1;
	}

	/**
	 * Return the Hibernate type attribute represented by this column's data type
	 */
	public String getHibernateType () {
		if (null == hibernateType) {
			hibernateType = TypeResolver.resolveType(getDataType(), true);
			if (null != hibernateType) {
			    if (isNumber(hibernateType)) {
			        if (digits > 0) hibernateType = "big_decimal";
			    }
			}
			if (null == hibernateType) hibernateType = getDataType();
		}
		return hibernateType;
	}

	/**
	 * Return true if the type given represents some kind of a numeric value and false if not
	 * @param type
	 */
	private boolean isNumber (String type) {
	    if (null == type) return false;
	    else if (type.equals(Float.class.getName())) return true;
	    else if (type.equals(Double.class.getName())) return true;
	    else if (type.equals("big_decimal")) return true;
	    else if (type.equals("integer")) return true;
	    else if (type.equals(Short.class.getName())) return true;
	    else if (type.equals(Long.class.getName())) return true;
	    else return false;
	}

	/**
	 * Return true if this column's type can be resolved and false if not
	 */
	public boolean isTypeResolved () {
		return (null != TypeResolver.resolveType(getDataType(), false));
	}
	
	/**
	 * @return Returns the Hibernate type attribute
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * @param dataType The dataType to set.
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return Returns the digits.
	 */
	public int getDigits() {
		return digits;
	}

	/**
	 * @param digits The digits to set.
	 */
	public void setDigits(int digits) {
		this.digits = digits;
	}

	/**
	 * Return the column name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the column name
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set the nullable value
	 * @param nullable The nullable to set.
	 */
	public void setNullable(int nullable) {
		this.nullable = nullable;
	}

	/**
	 * Return the size of the column
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Set the size of the column
	 * @param size The size to set.
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Return the table that this column belongs to
	 */
	public DBTable getTable() {
		return table;
	}

	/**
	 * Set the table that this column belongs to
	 * @param table The table to set.
	 */
	public void setTable(DBTable table) {
		this.table = table;
	}

	public String toString() {
		return getName() + " (" + getDataType() + ")";
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

	public boolean equals (Object obj) {
		if (null == obj || !(obj instanceof DBColumn)) return false;
		if (null == getTable().getName() || null == getName()) return false;
		DBColumn col = (DBColumn) obj;
		if (null == col.getTable().getName() || null == col.getName()) return false;
		return (col.getTable().getName().equals(getTable().getName()) && col.getName().equals(getName()));
	}
	
	private Integer hashCode;
	public int hashCode () {
		if (null == hashCode) {
			if (null == getTable().getName() || null == getName()) return super.hashCode();
			hashCode = new Integer(new String(getTable().getName() + ":" + getName()).hashCode());
		}
		return hashCode.intValue();
	}
}