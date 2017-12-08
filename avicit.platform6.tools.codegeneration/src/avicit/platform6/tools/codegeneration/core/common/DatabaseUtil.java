package avicit.platform6.tools.codegeneration.core.common;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import avicit.platform6.tools.codegeneration.core.util.DatabaseType;
/**
 * <p>金航数码科技有限责任公司</p>
 * <p>作者：dingrc</p>
 * <p>邮箱：dingrc@avicit.com</p>
 * <p>创建时间：2012-12-10</p>
 *
 * <p>类说明：数据库工具</p>
 * <p>修改记录：</p>
 */
public class DatabaseUtil {

    private DBConnectionFactory dBConnectionFactory;
    private Connection conn;
    private DatabaseMetaData dm;
    private String currentDatabaseType;

    public DatabaseUtil(String databaseType) {
        dBConnectionFactory = DBConnectionFactory.getInstance();
        currentDatabaseType = databaseType;
    }

    public DatabaseUtil(Connection conn)
            throws Exception {
        dBConnectionFactory = DBConnectionFactory.getInstance();
        if (conn == null) {
            throw new Exception("数据库连接为空");
        } else {
            this.conn = conn;
            return;
        }
    }

    public List getAllDatabaseName()
            throws Exception {
        List list = null;
        try {
            if (conn == null || conn.isClosed()) {
                conn = dBConnectionFactory.getConnection();
            }
            dm = conn.getMetaData();
            ResultSet rs;
            if (currentDatabaseType.equals(DatabaseType.ORACLE)) {   //如果是Oracle数据库，必须取schema
                rs = dm.getSchemas();
            } else {     //如果是非Oracle数据库，必须取Catalogs（目录）
                rs = dm.getCatalogs();
            }
            list = new ArrayList();
            for (; rs.next(); list.add(rs.getString(1))) {
            }
            rs.close();
            rs.getStatement().close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception((new StringBuilder("枚举数据名出错，错误信息为：")).append(e.getMessage()).toString());
        }
        return list;
    }
}
