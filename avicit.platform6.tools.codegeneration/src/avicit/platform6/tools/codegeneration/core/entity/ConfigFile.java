/*
 * ConfigFile.java
 *
 * Created on 2008年1月21日, 下午8:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package avicit.platform6.tools.codegeneration.core.entity;

import java.util.List;

/**
 *
 * @author Administrator
 */
public class ConfigFile {
    private String projectPath;
    private String templetePath;
    private String modeName;
    
    private Integer driverType;
    private String driverUrl;
    private String driverUser;
    private String driverPwd;
    private String driverSchema;
    private String driverClassName;
    
    private List driverJars;
    
    /** Creates a new instance of ConfigFile */
    public ConfigFile() {
    }
    
    public String getProjectPath() {
        return projectPath;
    }
    
    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }
    
    public String getTempletePath() {
        return templetePath;
    }
    
    public void setTempletePath(String templetePath) {
        this.templetePath = templetePath;
    }
    
    public String getModeName() {
        return modeName;
    }
    
    public void setModeName(String modeName) {
        this.modeName = modeName;
    }
    
    public Integer getDriverType() {
        return driverType;
    }
    
    public void setDriverType(Integer driverType) {
        this.driverType = driverType;
    }
    
    public String getDriverUrl() {
        return driverUrl;
    }
    
    public void setDriverUrl(String driverUrl) {
        this.driverUrl = driverUrl;
    }
    
    public String getDriverUser() {
        return driverUser;
    }
    
    public void setDriverUser(String driverUser) {
        this.driverUser = driverUser;
    }
    
    public String getDriverPwd() {
        return driverPwd;
    }
    
    public void setDriverPwd(String driverPwd) {
        this.driverPwd = driverPwd;
    }
    
    public String getDriverSchema() {
        return driverSchema;
    }
    
    public void setDriverSchema(String driverSchema) {
        this.driverSchema = driverSchema;
    }
    
    public List getDriverJars() {
        return driverJars;
    }
    
    public void setDriverJars(List driverJars) {
        this.driverJars = driverJars;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
    
}
