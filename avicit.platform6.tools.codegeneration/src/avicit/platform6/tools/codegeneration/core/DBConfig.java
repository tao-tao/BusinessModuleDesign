package avicit.platform6.tools.codegeneration.core;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.List;

import avicit.platform6.tools.codegeneration.core.common.DBConnectionFactory;
import avicit.platform6.tools.codegeneration.core.common.DatabaseTableUtil;
import avicit.platform6.tools.codegeneration.core.common.DatabaseUtil;
import avicit.platform6.tools.codegeneration.core.entity.DriverInfo;
import avicit.platform6.tools.codegeneration.core.util.DatabaseType;

public class DBConfig {
	private DriverInfo driverInfo;

	public List<String> getAllTable(String driverSchema, String databaseName) {
		DatabaseTableUtil dbTable = new DatabaseTableUtil();
		try {
			if (!testConnection()) {
				throw new Exception("数据库连接出错！");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (databaseName == null) {
			// JOptionPane.showMessageDialog(null, "请选择一个数据库!", "错误", 0);
			return null;
		}
		try {
			List<String> list = dbTable.getAllTableName(driverSchema,
					databaseName);
			return list;
			// selectedTableFrame.updateTableList(list);
			// configTabbedPane.setEnabledAt(2,true);
			// configTabbedPane.setSelectedIndex(2);
		} catch (Exception e) {
			// configTabbedPane.setEnabledAt(2,false);
			// configTabbedPane.setSelectedIndex(1);
			// JOptionPane.showMessageDialog(null, "连接数据库出错!", "错误", 0);
			// e.printStackTrace();
		}
		return null;
	}

	public boolean initDriverInfo(Driver driver, String driverUrl,
			String driverClassName, String driverUser, String driverSchema,
			String driverPassword, int type) {

		String driverType = DatabaseType.NULL;
		String adatabasetype[];
		int j = (adatabasetype = DatabaseType.values()).length;
		for (int i = 0; i < j; i++) {
			String dt = adatabasetype[i];

			if (dt.equals(String.valueOf(type))) {
				driverType = dt;
			}
		}

		if (type == driverTypeArray.length) { // 选择最后一个空
			// JOptionPane.showMessageDialog(null, "请选择数据库类型!", "错误", 0);
			// driverTypeBox.requestFocus();
			return false;
		}
		if (driverUrl == null || driverUrl.trim().equals("")) {
			// JOptionPane.showMessageDialog(null, "请输入正确的数据连接URL!", "错误", 0);
			// driverUrlText.requestFocus();
			return false;
		}
		if (driverClassName == null || driverClassName.trim().equals("")) {
			// JOptionPane.showMessageDialog(null, "请输入正确的数据库驱动类路径!", "错误", 0);
			// driverClassNameText.requestFocus();
			return false;
		}
		if (driverUser == null || driverUser.trim().equals("")) {
			// JOptionPane.showMessageDialog(null, "请输入正确的数据库用户名!", "错误", 0);
			// driverUserText.requestFocus();
			return false;
		}
		// if (jarFileList.size() < 1) {
		// JOptionPane.showMessageDialog(null, "请添加数据库驱动Jar文件!", "错误", 0);
		// return false;
		// }
		try {
			driverInfo = new DriverInfo(driverType, driverUrl, driverClassName,
					driverUser, driverPassword, null);
			driverInfo.setDriverSchema(driverSchema);
			driverInfo.setDriver(driver);
			loadJarFiles(driverInfo);
		} catch (ClassNotFoundException e) {
			// JOptionPane.showMessageDialog(null, (new
			// StringBuilder("载入数据库驱动错误!\n")).append(e.getMessage()).toString(),
			// "错误", 0);
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// JOptionPane.showMessageDialog(null, "载入数据库驱动错误!", "错误", 0);
			e.printStackTrace();
			return false;
		} catch (InstantiationException e) {
			// JOptionPane.showMessageDialog(null, "载入数据库驱动后无法实例化!", "错误", 0);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean initDriverInfo(List jarFileList, String driverUrl,
			String driverClassName, String driverUser, String driverSchema,
			String driverPassword, int dbtype) {
		Driver d = null;
		initDriverInfo(d, driverUrl, driverClassName, driverUser, driverSchema,
				driverPassword, dbtype);
		driverInfo.setDriverJars(jarFileList);
		try {
			loadJarFiles(driverInfo);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return true;
	}

	/** 测试数据库连接 */
	public boolean testConnection() throws Exception {
		DBConnectionFactory dBConnectionFactory = DBConnectionFactory
				.getInstance();
		// if (!dBConnectionFactory.isInit()) {
		dBConnectionFactory.init(driverInfo);
		// }
		return dBConnectionFactory.connect();
	}

	/**
	 * 动态加载jar包
	 */
	private Driver loadJarFiles(DriverInfo driverInfo) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		List jarFileList = driverInfo.getDriverJars();
		if (jarFileList != null && jarFileList.size() > 0) {
			URL jarUrls[] = new URL[jarFileList.size()];
			for (int i = 0; i < jarFileList.size(); i++) {
				jarUrls[i] = new URL((new StringBuilder("file:\\")).append(
						((String) jarFileList.get(i)).replaceAll("\\\\", "/"))
						.toString());
			}

			URLClassLoader loader = new URLClassLoader(jarUrls);
			Driver driver = (Driver) Class.forName(
					driverInfo.getDriverClassName(), true, loader)
					.newInstance();
			driverInfo.setDriver(driver);
			return driver;
		}
		return null;
	}

	private String driverTypeArray[] = { "mysql", "MSSQlserver",
			"MSSqlserver2005", "Oracle", "                   " };

	/**
	 * 加载库
	 */
	public List getDatabaseList() {// GEN-FIRST:event_btnLoadDBActionPerformed
	// TODO 将在此处添加您的处理代码：
	// if (!checkDatabaseParameter()) {
	// return;
	// }
		DatabaseUtil db = new DatabaseUtil(driverInfo.getDatabaseType());
		try {
			if (testConnection()) {
				List list = db.getAllDatabaseName();
				// selectedDataBaseFrame.updateDatabaseList(list);
				// configTabbedPane.setEnabledAt(1,true);
				// configTabbedPane.setSelectedIndex(1);
				return list;
			}
		} catch (Exception e) {
			// JOptionPane.showMessageDialog(null, "连接数据库出错!", "错误", 0);
			// configTabbedPane.setEnabledAt(1,false);
			// configTabbedPane.setSelectedIndex(0);
			e.printStackTrace();
		}
		return null;
	}// GEN-LAST:e
}
