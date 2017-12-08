package avicit.platform6.tools.codegeneration.core.common;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * <p>金航数码科技有限责任公司</p>
 * <p>作者：dingrc</p>
 * <p>邮箱：dingrc@avicit.com</p>
 * <p>创建时间：2012-12-10</p>
 *
 * <p>类说明：数据库表工具</p>
 * <p>修改记录：</p>
 */
public class DatabaseTableUtil {

    public static String DATABASE_NAME = "";
    public static String SCHEMA_CATALOG = "";
    private DBConnectionFactory dBConnectionFactory;
    private Connection conn;
    private DatabaseMetaData dm;

    public DatabaseTableUtil() {
        dBConnectionFactory = DBConnectionFactory.getInstance();
    }

    public DatabaseTableUtil(Connection conn)
            throws Exception {
        dBConnectionFactory = DBConnectionFactory.getInstance();
        dBConnectionFactory = DBConnectionFactory.getInstance();
        if (conn == null) {
            throw new Exception("数据库连接为空");
        } else {
            this.conn = conn;
            return;
        }
    }

    /**
     *获得所有的数据库表的名称
     */
    public List<String> getAllTableName(String schemaOrCatalog, String databaseName)
            throws Exception {
        List<String> list;
        try {
            if (conn == null || conn.isClosed()) {
                conn = dBConnectionFactory.getConnection();
            }
            dm = conn.getMetaData();
            
            String types[] = {
                "TABLE","VIEW"
            };
            System.out.println(dm.getDriverVersion());
            System.out.println(dm.getUserName());
            System.out.println(dm.getDatabaseProductName());
            //ResultSet rs = dm.getTables("PT6_FORMAL", "dbo", "%", types);
            ResultSet rs = dm.getTables(databaseName, schemaOrCatalog, "%", types);
            list = new ArrayList<String>();
            for (; rs.next(); list.add(rs.getString(3))) {
            }
            
            DATABASE_NAME = databaseName;
            SCHEMA_CATALOG = schemaOrCatalog;
            //rs.getStatement().close();
            rs.close();
            
        } catch (Exception e) {
            throw new Exception((new StringBuilder("获取数据库表名出错，错误信息为：")).append(e.getMessage()).toString());
        }
        return list;
    }
}
