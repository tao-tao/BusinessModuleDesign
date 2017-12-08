package avicit.ui.platform.common.data;

public class SystemConfig {
	
	String driver = null;
	String url = null;
//	String username = null;
//	String password = null;
	String bizUsername = null;
	String bizPassword = null;
	
	String webFolder = "WebRoot";
	String webContext = "";
	String springFolder = webFolder + "/WEB-INF";
	String facesFolder = webFolder + "/WEB-INF/faces-configs";
	String hbmFolder = "/WEB-INF/src-Application";
	String queryFolder = webFolder + "/WEB-INF/query-configs";
	String batchFolder = webFolder + "/WEB-INF/batch-configs";
	String srcFolder = "src-Application";

	String processDir = "";

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

//	public String getUsername() {
//		return username;
//	}

//	public void setUsername(String username) {
//		this.username = username;
//	}

//	public String getPassword() {
//		return password;
//	}

//	public void setPassword(String password) {
//		this.password = password;
//	}

	public String getBizUsername() {
		return bizUsername;
	}

	public void setBizUsername(String bizUsername) {
		this.bizUsername = bizUsername;
	}

	public String getBizPassword() {
		return bizPassword;
	}

	public void setBizPassword(String bizPassword) {
		this.bizPassword = bizPassword;
	}

	public String getWebFolder() {
		return webFolder;
	}

	public void setWebFolder(String webFolder) {
		this.webFolder = webFolder;
	}

	public String getWebContext() {
		return webContext;
	}

	public void setWebContext(String webContext) {
		this.webContext = webContext;
	}

	public String getSpringFolder() {
		return springFolder;
	}

	public void setSpringFolder(String springFolder) {
		this.springFolder = springFolder;
	}

	public String getFacesFolder() {
		return facesFolder;
	}

	public void setFacesFolder(String facesFolder) {
		this.facesFolder = facesFolder;
	}

	public String getHbmFolder() {
		return hbmFolder;
	}

	public void setHbmFolder(String hbmFolder) {
		this.hbmFolder = hbmFolder;
	}

	public String getQueryFolder() {
		return queryFolder;
	}

	public void setQueryFolder(String queryFolder) {
		this.queryFolder = queryFolder;
	}

	public String getSrcFolder() {
		return srcFolder;
	}

	public void setSrcFolder(String srcFolder) {
		this.srcFolder = srcFolder;
	}

	public String getProcessDir() {
		return processDir;
	}

	public void setProcessDir(String processDir) {
		this.processDir = processDir;
	}

	public String getBatchFolder() {
		return batchFolder;
	}

	public void setBatchFolder(String batchFolder) {
		this.batchFolder = batchFolder;
	}
}
