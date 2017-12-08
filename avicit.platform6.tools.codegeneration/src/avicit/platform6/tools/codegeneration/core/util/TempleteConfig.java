/*
 * WFConfig.java
 *
 * Created on 2007年10月29日, 下午3:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package avicit.platform6.tools.codegeneration.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import avicit.platform6.tools.codegeneration.core.entity.Property;
import avicit.platform6.tools.codegeneration.core.entity.Templete;
import avicit.platform6.tools.codegeneration.core.entity.TempleteFile;
import avicit.platform6.tools.codegeneration.log.AvicitLogger;

import com.thoughtworks.xstream.XStream;

/**
 * 报表配置映射类 为了保证报表定义入口的纯粹性，本例子设置为单例
 *
 * @author lixin
 */
public final class TempleteConfig {

    private TempleteFile config;
    private static String CONFIG_FILE = "TempleteConfig.xml";
    private static TempleteConfig templete_config;

    /**
     * Creates a new instance of WFConfig
     */
    private TempleteConfig() {
    }

    /**
     * 获得单一实例
     */
    public static TempleteConfig getTempleteConfig() {
        if (templete_config == null) {
            templete_config = new TempleteConfig();
        }
        return templete_config;
    }

    /**
     * 加载报表定义文件，并做XML-Object映射
     */
    public void loadTempleteDef(String templeteDir) {
        try {
            XStream xstream = buildObjectMapping2XML();
            InputStream is = new FileInputStream(new File(templeteDir, CONFIG_FILE));
            config = (TempleteFile) xstream.fromXML(is);
        } catch (Exception ex) {
          //  JOptionPane.showMessageDialog(null,"系统加载模板配置文件时出错，请检查模板路径是否正确:\r\n"+ ex.getMessage(), "错误", 0);
            ex.printStackTrace();
        }
    }
    /**
     * 加载报表定义文件，并做XML-Object映射
     */
    public void loadTempleteDef(File templeteDir) {
        try {
        	
            XStream xstream = buildObjectMapping2XML();
            InputStream is = new FileInputStream(new File(templeteDir, CONFIG_FILE));
            config = (TempleteFile) xstream.fromXML(is);
            
        } catch (Exception ex) {
        	AvicitLogger.openError("系统加载模板配置文件时出错，请检查模板路径是否正确:\r\n"+ ex.getMessage(), ex);
        	AvicitLogger.logError(ex);
        	ex.printStackTrace();
        	
            
        }
    }

    /**
     * 创建对象－XML标签映射
     */
    private XStream buildObjectMapping2XML() {
        XStream xstream = new XStream();
        xstream.alias("root", TempleteFile.class);
        xstream.alias("templetes", ArrayList.class);
        xstream.alias("templete", Templete.class);
        xstream.alias("properties", ArrayList.class);
        xstream.alias("property", Property.class);
        xstream.alias("dynafilename", Boolean.class);
        return xstream;
    }

    public TempleteFile getConfig() {
        return config;
    }

    public void setConfig(TempleteFile config) {
        this.config = config;
    }
}
