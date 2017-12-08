package avicit.platform6.tools.codegeneration.core.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import avicit.platform6.tools.codegeneration.core.entity.DriverInfo;
/**
 * <p>金航数码科技有限责任公司</p>
 * <p>作者：dingrc</p>
 * <p>邮箱：dingrc@avicit.com</p>
 * <p>创建时间：2012-12-10</p>
 *
 * <p>类说明：数据库连接</p>
 * <p>修改记录：</p>
 */
public class DBConnectionFactory {
    
    private static DBConnectionFactory instance;
    private DriverInfo driverInfo;
    private Connection conn;
    private boolean isInit;
    private boolean isConnected;
    private Properties properties;
    
    private DBConnectionFactory() {
        isInit = false;
        isConnected = false;
        properties = new Properties();
    }
    
    public static DBConnectionFactory getInstance() {
        if(instance == null) {
            instance = new DBConnectionFactory();
        }
        return instance;
    }
    
    public void init(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
        properties.put("user", driverInfo.getDriverUser());
        properties.put("password", driverInfo.getDriverPassword());
        isInit = true;
    }
    
    public boolean connect() throws Exception {
        if(!isInit) {
            throw new Exception("DBConnectionFactory未初始化");
        }
//        if(!isConnected) {
            tryConnect();
//        }
        return isConnected;
    }
    
    private void tryConnect() throws Exception {
        if(properties.get("user") == null || properties.get("password") == null) {
            throw new Exception("数据连接参数错误");
        }
        try {
            conn = driverInfo.getDriver().connect(driverInfo.getDriverUrl(), properties);
            if(conn == null) {
                isConnected = false;
                throw new Exception("无法建立连接!未知原因");
            }
            isConnected = true;
        } catch(SQLException e) {
            isConnected = false;
            e.printStackTrace();
            throw new Exception("数据库建立连接出错");
        } catch(Exception e) {
            isConnected = false;
            e.printStackTrace();
            throw new Exception("连接数据库时出现未知错误");
        }
    }
    
    public Connection getConnection() throws Exception {
        if(!instance.isConnected || instance.conn == null || instance.conn.isClosed()) {
            instance.tryConnect();
        }
        return instance.conn;
    }
    
    public boolean isInit() {
        return isInit;
    }

    public DriverInfo getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
    }
}
