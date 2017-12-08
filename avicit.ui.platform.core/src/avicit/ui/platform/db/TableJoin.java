package avicit.ui.platform.db;

public class TableJoin {
	private DBColumn primaryKey;
	private DBColumn foreignKey;

	public TableJoin (DBColumn primaryKey, DBColumn foreignKey) {
		this.primaryKey = primaryKey;
		this.foreignKey = foreignKey;
	}
	
	/**
	 * @return
	 */
	public DBColumn getForeignKey() {
		return foreignKey;
	}

	/**
	 * @param foreignKey
	 */
	public void setForeignKey(DBColumn foreignKey) {
		this.foreignKey = foreignKey;
	}

	/**
	 * @return
	 */
	public DBColumn getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * @param primaryKey
	 */
	public void setPrimaryKey(DBColumn primaryKey) {
		this.primaryKey = primaryKey;
	}
}