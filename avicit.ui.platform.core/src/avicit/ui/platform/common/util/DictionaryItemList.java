package avicit.ui.platform.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Combo;

import com.tansun.easycare.common.db.DbHandleDao;

public class DictionaryItemList {

	// begin private methods.
	static class TimeMap {
		long syncTime;
		List result;

		TimeMap(long time, List res) {
			this.syncTime = time;
			this.result = res;
		}
	}

	public static DbHandleDao dao;

	public static DbHandleDao getEcDAO() {
		return dao;
	}

	public static Map dictionaryMap = new HashMap();
	static long SYNCTIMEOUT = 1800000L;

	private static Logger logger = Logger.getLogger(DictionaryItemList.class);

	private static List getCacheList(String key) {
		if (dictionaryMap.get(key) != null) {
			TimeMap timeMap = (TimeMap) dictionaryMap.get(key);
			if (timeMap.syncTime + SYNCTIMEOUT > System.currentTimeMillis())
				return timeMap.result;
		}
		return null;
	}

	private static void putCacheList(String key, List result) {
		synchronized (dictionaryMap) {
			dictionaryMap.put(key, new TimeMap(System.currentTimeMillis(),
					result));
		}
	}

	public static List makeList(String hql) {
		return makeList(hql, hql);
	}

	public static List makeList(String key, String sql) {
		List oldresult = getCacheList(key);
		if (oldresult != null)
			return oldresult;
		// List result = getCacheList(key);
		logger.debug("DictionaryItemList -> " + key + ":" + sql);
		List list = dao.viewList(sql);
		List result = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			SelectItem item = new SelectItem();
			item.setValue(((Object[]) list.get(i))[0]);
			item.setLabel((String) ((Object[]) list.get(i))[1]);
			result.add(item);
		}
		putCacheList(key, result);
		// putCacheList(key,result);
		logger.debug(key + ":" + result.size());
		return result;

	}

	public DbHandleDao getDao() {
		return dao;
	}

	public static void setEcDao(DbHandleDao daohandle) {
		dao = daohandle;
	}

	// End private util methods.

	// Begin the customer methods.
	public static List getDepartmentTypeList() {
		String Str = "select id, Title from DepartmentType";
		return makeList("departmentTypeList", Str);
	}

	public static List getDepartmentLevelList() {
		String Str = "select id, Title from DepartmentLevel";
		return makeList("departmentLevelList", Str);
	}

	public static String GENDER = "genderList";

	public static List getGenderList() {
		if (dictionaryMap.get(GENDER) != null) {
			TimeMap timeMap = (TimeMap) dictionaryMap.get(GENDER);
			return timeMap.result;
		}
		List result = new ArrayList();
		SelectItem item = new SelectItem();
		item.setValue(new Integer(0));
		item.setLabel("��");
		result.add(item);
		item = new SelectItem();
		item.setValue(new Integer(1));
		item.setLabel("Ů");
		result.add(item);

		putCacheList(GENDER, result);
		return result;
	}

	public static List getDepartmentList() {
		//String Str = "select Id, DepName from MinDepartment";
		String Str = "select id, name from Department";
		return makeList("departmentList", Str);
	}
	
	public static List getRoleList() {
		//String Str = "select Id, RoleName from MinRole";
		String Str = "select id, name from Role";
		return makeList(Str);
	}

	public List getCountryList() {
		String Str = "select Id, Detail from Country";
		return makeList("countryList", Str);
	}

	// public static List getProvinceListByCountryID(Integer cid)
	// {
	// String Str = "select Id, Name from Province as p where p.Countryid = " +
	// cid;
	// return makeList("getProvinceListByCountryID",Str);
	// }
	//	
	// public static List getCityListByProvinceID(Integer pid)
	// {
	// String Str = "select Id, Name from City as c where c.Provinceid = " +
	// pid;
	// return makeList("getCityListByProvinceID",Str);
	// }

	public List getProvinceList() {
		String Str = "select Id, Name from Province";
		return makeList("provinceList", Str);
	}

	public List getCityList() {
		String Str = "select Id, Name from City";
		return makeList("cityList", Str);
	}

	public List getDistrictList() {
		String Str = "select Id, Detail from District";
		return makeList("districtList", Str);
	}

	public List getUrlList() {
		String Str = "select Id, Url from ResourceUrl";
		return makeList("urlList", Str);
	}

	public List getSecondManagerRoleList() {
		String Str = "select Id, RoleName from MinRole where RoleType='depAdmin'";
		return makeList("secondManagerRoleList", Str);
	}

	public static String BIZAPPLICATION = "applicationList";

	public List getApplicationList() {
		List result = getCacheList(BIZAPPLICATION);
		if (result != null)
			return result;

		logger
				.debug("DictionaryItemList -> getApplicationList(): select Id, Detail from District.");
		String Str = "select Id, SystemName from BizApplication";
		List list = dao.viewList(Str);
		result = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			SelectItem item = new SelectItem();
			item.setLabel((String) ((Object[]) list.get(i))[1]);
			/* item.setValue((Integer)((Object[]) list.get(i))[0]); */
			item.setValue(new String(((Object[]) list.get(i))[0].toString()));
			result.add(item);
		}
		putCacheList(BIZAPPLICATION, result);
		logger.debug("getApplicationList():" + result.size());
		return result;
	}

	public static void initRoleCombo(Combo combo){
		List rolelist = getRoleList();
		String[] rolename = new String[rolelist.size()];
		for (int i = 0; i < rolename.length; i++) {
			rolename[i] = (String) ((SelectItem) rolelist.get(i)).getLabel();
		}
		combo.setItems(rolename);
	}

	public static void initDeptCombo(Combo combo){
		List deptlist = getDepartmentList();
		String[] deptname = new String[deptlist.size()];
		for (int i = 0; i < deptname.length; i++) {
			deptname[i] = (String) ((SelectItem) deptlist.get(i)).getLabel();
		}
		combo.setItems(deptname);
	
	}

}
