package avicit.platform6.tools.codegeneration.core.util;

public class DatabaseType {

    public static final String MYSQL = "0";
    public static final String MSSQLSERVER = "1";
    public static final String MSSQLSERVER2005 = "2";
    public static final String ORACLE = "3";
    public static final String NULL = "4";
    private static final String VALUES[];

    public static String[] values() {
        return VALUES;
    }

    static {
        VALUES = (new String[]{
            MYSQL, MSSQLSERVER, MSSQLSERVER2005, ORACLE, NULL
        });
    }
}
