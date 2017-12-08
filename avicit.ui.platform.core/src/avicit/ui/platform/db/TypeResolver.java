package avicit.ui.platform.db;

import java.util.Enumeration;
import java.util.Properties;

public class TypeResolver {

	public static final Properties typeMap = new Properties();

	static {
		typeMap.setProperty("float", Float.class.getName());
		typeMap.setProperty("float unsigned zerofill", Float.class.getName());
		typeMap.setProperty("double", Double.class.getName());
		typeMap.setProperty("decimal", "big_decimal");
		typeMap.setProperty("money", "big_decimal");
		typeMap.setProperty("currency", "big_decimal");
		typeMap.setProperty("int", "integer");
		typeMap.setProperty("uniqueidentifier", "integer");
		typeMap.setProperty("nvarchar", "string");
		typeMap.setProperty("tinyint", Byte.class.getName());
		typeMap.setProperty("smallint", Short.class.getName());
		typeMap.setProperty("mediumint", "integer");
		typeMap.setProperty("int2", "integer");
		typeMap.setProperty("int4", Long.class.getName());
		typeMap.setProperty("int8", Long.class.getName());
		typeMap.setProperty("integer", "integer");
		typeMap.setProperty("int identity", "integer");
		typeMap.setProperty("bigint", Long.class.getName());
		typeMap.setProperty("numeric", "integer");
		typeMap.setProperty("number", "integer");
		typeMap.setProperty("serial", "integer");
		typeMap.setProperty("int unsigned", "integer");
		typeMap.setProperty("smallint", Short.class.getName());
		typeMap.setProperty("mediumint", "integer");
		typeMap.setProperty("timestamp", "timestamp");
		typeMap.setProperty("timestamptz", "timestamp");
		typeMap.setProperty("typesystimestamp", "timestamp");
		typeMap.setProperty("smalldatetime", "date");
		typeMap.setProperty("interval", "timestamp");
		typeMap.setProperty("time", "time");
		typeMap.setProperty("date", "date");
		typeMap.setProperty("datetime", "timestamp");
		typeMap.setProperty("smalldate", "date");
		typeMap.setProperty("ntext", "string");
		typeMap.setProperty("text", "string");
		typeMap.setProperty("mediumtext", "string");
		typeMap.setProperty("mediumblob", "binary");
		typeMap.setProperty("longtext", "string");
		typeMap.setProperty("varchar", "string");
		typeMap.setProperty("varchar2", "string");
		typeMap.setProperty("char", "string");
		typeMap.setProperty("bpchar", "string");
		typeMap.setProperty("clob", "string");
		typeMap.setProperty("blob", "binary");
		typeMap.setProperty("bit", "boolean");
		typeMap.setProperty("bool", "boolean");
		typeMap.setProperty("varbinary", "binary");
		typeMap.setProperty("image", "binary");
		typeMap.setProperty("byte", "binary");
	}
	
	public static String resolveType (String type, boolean useDefault) {
		if (null == type) return null;
		String s = typeMap.getProperty(type.toLowerCase());
		if (null == s) {
			if (useDefault) return String.class.getName();
			else {
				for (Enumeration e=typeMap.keys(); e.hasMoreElements(); ) {
					String key = e.nextElement().toString();
					if (type.toLowerCase().startsWith(key.toLowerCase()))
						return typeMap.getProperty(key);
				}
				return null;
			}
		}
		else {
			return s;
		}
	}
}