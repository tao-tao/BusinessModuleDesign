package avicit.platform6.tools.codegeneration.core.entity;

import java.sql.Driver;
import java.util.List;

public class DriverInfo {
    
    private String databaseType;
    private String driverUrl;
    private String driverClassName;
    private String driverUser;
    private String driverPassword;
    private String driverSchema;
    private Driver driver;
    private List driverJars;
    
    public DriverInfo(String databaseType, String driverUrl, String driverClassName, String driverUser, String driverPassword, Driver driver, List driverJars) {
        this.databaseType = databaseType;
        this.driverUrl = driverUrl;
        this.driverClassName = driverClassName;
        this.driverUser = driverUser;
        this.driverPassword = driverPassword;
        this.driver = driver;
        this.driverJars = driverJars;
    }
    
    public DriverInfo(String databaseType, String driverUrl, String driverClassName, String driverUser, String driverPassword, Driver driver) {
        this.databaseType = databaseType;
        this.driverUrl = driverUrl;
        this.driverClassName = driverClassName;
        this.driverUser = driverUser;
        this.driverPassword = driverPassword;
        this.driver = driver;
    }
    
    public DriverInfo(String databaseType, String driverUrl, String driverUser, String driverPassword, Driver driver) {
        this.databaseType = databaseType;
        this.driverUrl = driverUrl;
        this.driverUser = driverUser;
        this.driverPassword = driverPassword;
        this.driver = driver;
    }
    
    public String getDriverUrl() {
        return driverUrl;
    }
    
    public void setDriverUrl(String driverUrl) {
        this.driverUrl = driverUrl;
    }
    
    public String getDriverClassName() {
        return driverClassName;
    }
    
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
    
    public String getDriverUser() {
        return driverUser;
    }
    
    public void setDriverUser(String driverUser) {
        this.driverUser = driverUser;
    }
    
    public String getDriverPassword() {
        return driverPassword;
    }
    
    public void setDriverPassword(String driverPassword) {
        this.driverPassword = driverPassword;
    }
    
    public Driver getDriver() {
        return driver;
    }
    
    public void setDriver(Driver driver) {
        this.driver = driver;
    }
    
    public List getDriverJars() {
        return driverJars;
    }
    
    public void setDriverJars(List driverJars) {
        this.driverJars = driverJars;
    }
    
    public String getDatabaseType() {
        return databaseType;
    }
    
    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }
    
    public String getDriverSchema() {
        return driverSchema;
    }
    
    public void setDriverSchema(String driverSchema) {
        this.driverSchema = driverSchema;
        if(this.driverSchema!=null && this.driverSchema.equals(""))
            this.driverSchema=null;
    }
}
