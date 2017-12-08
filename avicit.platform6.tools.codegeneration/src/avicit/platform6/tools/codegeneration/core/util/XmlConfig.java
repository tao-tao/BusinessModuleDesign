/*
 * WFConfig.java
 *
 * Created on 2007年10月29日, 下午3:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package avicit.platform6.tools.codegeneration.core.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import avicit.platform6.tools.codegeneration.core.entity.ConfigFile;

import com.thoughtworks.xstream.XStream;

/**报表配置映射类
 * 为了保证报表定义入口的纯粹性，本例子设置为单例
 * @author lixin
 */
public final class XmlConfig{
    
    private ConfigFile config;
    private static String CONFIG_FILE_PATH="c:/NavGenerator_config.xml";
    private static XmlConfig xml_config;
    
    /** Creates a new instance of WFConfig */
    private XmlConfig() {
    }
    
    /**
     *获得单一实例
     */
    public static XmlConfig getXmlConfig(){
        if(xml_config == null) {
            xml_config = new XmlConfig();
        }
        return xml_config;
    }
    
    /**
     *加载报表定义文件，并做XML-Object映射
     */
    public void loadXmlDef(){
        try {
            XStream xstream=buildObjectMapping2XML();
            InputStream  is=new FileInputStream(CONFIG_FILE_PATH);
            config=(ConfigFile)xstream.fromXML(is);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }
    
    /**
     *写配置
     */
    public void writeXmlDef(){
        try {
            XStream xstream=buildObjectMapping2XML();
            xstream.toXML(config,new FileOutputStream(CONFIG_FILE_PATH));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     *创建对象－XML标签映射
     */
    private XStream buildObjectMapping2XML() {
        XStream xstream = new XStream();
        xstream.alias("config", ConfigFile.class);
        xstream.alias("driverJars",ArrayList.class);
        xstream.alias("driverType",java.lang.Integer.class);
        return xstream;
    }

    public static void main(String[] args) {
//        Reporter pr=XmlConfig.getXmlConfig().getReporterById("zhou");
//        //String str=((BPMNode)(pr.getNodes()).get(0)).getPre_action();
//        Integer str=((ReportParam)( pr.getQuary_params().get(0) )).getOrder_no();
//        System.out.println("**********"+str+"**********");
//
//        System.out.println("**********"+((ReportParam)( pr.getQuary_params().get(0) )).getOutputHtml()+"**********");
//        System.out.println("**********"+((ReportParam)( pr.getQuary_params().get(0) )).getOutputJava()+"**********");
        
        
        XmlConfig.getXmlConfig().writeXmlDef();
        
        
        
//        try {
//
//            Class c = Class.forName("java.lang.String");
//
//            Method m[] = c.getDeclaredMethods();
//            for(int i=0;i<m.length;i++) {
//                System.out.println(m[i].toString());
//            }
//
//        } catch (SecurityException ex) {
//            ex.printStackTrace();
//        } catch (ClassNotFoundException ex) {
//            ex.printStackTrace();
//        }
        
        
    }

    public ConfigFile getConfig() {
        return config;
    }

    public void setConfig(ConfigFile config) {
        this.config = config;
    }
    
}
