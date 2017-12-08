package avicit.platform6.tools.codegeneration.core.entity;

import java.util.List;
/**
 * <p>金航数码科技有限责任公司</p>
 * <p>作者：dingrc</p>
 * <p>邮箱：dingrc@avicit.com</p>
 * <p>创建时间：2012-12-10</p>
 *
 * <p>类说明：TableBusinessObject实体类</p>
 * <p>修改记录：</p>
 */
public class TableBusinessObject {
	List<BusinessObjectProperty> boList=null;
	String tableName;
	public List<BusinessObjectProperty> getBoList() {
		return boList;
	}
	public void setBoList(List<BusinessObjectProperty> boList) {
		this.boList = boList;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}
