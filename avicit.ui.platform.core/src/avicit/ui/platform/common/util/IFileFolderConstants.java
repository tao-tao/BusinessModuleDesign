package avicit.ui.platform.common.util;

public interface IFileFolderConstants {
	public static final String STAR = "*";

	public static final String DOT = ".";

	public static final String PATH_SEPARATOR = "/";

	/** file extensions */
	public static final String EXT_APP = "app";

	public static final String EXT_CLASS = "class";

	public static final String EXT_DGM = "dgm";

	public static final String EXT_GIF = "gif";

	public static final String EXT_HTM = "htm";

	public static final String EXT_HTML = "html";

	public static final String EXT_JAR = "jar";

	public static final String EXT_JAVA = "java";

	public static final String EXT_JPEG = "jpeg";

	public static final String EXT_JPG = "jpg";

	public static final String EXT_JSF = "jsf";

	public static final String EXT_JSP = "jsp";

	public static final String EXT_JSV = "jsv";

	public static final String EXT_JTPL = "jtpl";

	public static final String EXT_JSPX = "jspx";

	public static final String EXT_PAGEFLOW = "pageflow";

	public static final String EXT_PBD = "pbd";

	public static final String EXT_PBL = "pbl";

	public static final String EXT_PNG = "png";

	public static final String EXT_PSR = "psr";

	public static final String EXT_PROPERTIES = "properties";

	public static final String EXT_PUL = "pul";

	public static final String EXT_SRD = "srd";

	public static final String EXT_TAGLIB = "tld";

	public static final String EXT_TMPL = "tmpl";

	public static final String EXT_WAR = "war";

	public static final String EXT_XML = "xml";

	public static final String EXT_ZIP = "zip";

	public static final String EXT_WSDL = "wsdl";

	/** files */
	public static final String FILE_BUILD_PROPERTIES = "build.properties";

	public static final String FILE_FACES_CONFIG_XML = "faces-config.xml";

	public static final String FILE_MANIFEST_MF = "MANIFEST.MF";

	public static final String FILE_WEB_APP_23_DTD = "web-app_2_3.dtd";

	public static final String FILE_WEB_FACESCONFIG_10_DTD = "web-facesconfig_1_0.dtd";

	public static final String FILE_WEB_XML = "web.xml";

	public static final String FILE_SYBASE_EASERVER_CONFIG_XML = "sybase-easerver-config.xml";

	/** folders */
	public static final String FOLDER_BIN = "bin";

	public static final String FOLDER_CLASS = "classes";

	public static final String FOLDER_DTD = "dtd";

	public static final String FOLDER_GENERATED = "generated";

	public static final String FOLDER_ICONS = "icons";

	public static final String FOLDER_IMAGES = "images";

	public static final String FOLDER_LIB = "lib";

	public static final String FOLDER_METAINF = "META-INF";

	public static final String FOLDER_PAGEFLOW = "pageflows";

	public static final String FOLDER_PB = "pb";

	public static final String FOLDER_SOURCE = "src";

	public static final String FOLDER_TEMPLATES = "templates";

	public static final String FOLDER_TAGLIB = "tld";

	public static final String FOLDER_WEBINF = "WEB-INF";

	public static final String FOLDER_WEBROOT = "WebContent";

	public static final String FOLDER_WIZARDS = "wizards";

	public static final String FOLDER_WSDL = "wsdl";

	/** the webroot folder depth relative to the project */
	public static final int WEBROOT_FOLDER_DEPTH = 2;

	/**
	 * @deprecated
	 */
	public static String DEFAULT_FACES_CONFIG_FILE_PATH = IFileFolderConstants.PATH_SEPARATOR
			+ IFileFolderConstants.FOLDER_WEBROOT
			+ IFileFolderConstants.PATH_SEPARATOR
			+ IFileFolderConstants.FOLDER_WEBINF
			+ IFileFolderConstants.PATH_SEPARATOR
			+ IFileFolderConstants.FILE_FACES_CONFIG_XML;

}
