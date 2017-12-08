/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package avicit.platform6.tools.codegeneration.core.entity;

import java.util.List;




/**
 *
 * @author Administrator
 */
public class TempleteFile {
    private String company;
    private String author;
    private String copyright;
    private String description;
    //文件名是否自动设定为
    private Boolean dynafilename;
    
    private String base_package;
    private String base_src;
    private String base_page;
    
    private List<Templete> templetes;
    private List<Property> properties;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDynafilename() {
        return dynafilename;
    }

    public void setDynafilename(Boolean dynafilename) {
        this.dynafilename = dynafilename;
    }



    public String getBase_package() {
        return base_package;
    }

    public void setBase_package(String base_package) {
        this.base_package = base_package;
    }

    public String getBase_src() {
        return base_src;
    }

    public void setBase_src(String base_src) {
        this.base_src = base_src;
    }

    public String getBase_page() {
        return base_page;
    }

    public void setBase_page(String base_page) {
        this.base_page = base_page;
    }

    public List<Templete> getTempletes() {
        return templetes;
    }

    public void setTempletes(List<Templete> templetes) {
        this.templetes = templetes;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
