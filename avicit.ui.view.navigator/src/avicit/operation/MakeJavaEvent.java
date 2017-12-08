package avicit.operation;


import org.eclipse.core.resources.IFile;

public class MakeJavaEvent {
	public int state;
	public String projectPath;
	public String hibernatePath;
	public String javaPath;
	public String getJavaPath() {
		return javaPath;
	}
	public void setJavaPath(String javaPath) {
		this.javaPath = javaPath;
	}
	public String hbmPath;
	public String getProjectPath() {
		return projectPath;
	}
	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}
	public String getHibernatePath() {
		return hibernatePath;
	}
	public void setHibernatePath(String hibernatePath) {
		this.hibernatePath = hibernatePath;
	}
	public String getHbmPath() {
		return hbmPath;
	}
	public void setHbmPath(String hbmPath) {
		this.hbmPath = hbmPath;
	}
	public String name;
	public String path;
	public String hpath;
	
	
	public IFile file; 
	public IFile getFile() {
		return file;
	}
	public void setFile(IFile file) {
		this.file = file;
	}
	public String getHpath() {
		return hpath;
	}
	public void setHpath(String hpath) {
		this.hpath = hpath;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
