/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package avicit.platform6.tools.codegeneration.core.entity;

/**
 * 模板中附加的属性值
 * @author LongRiver
 */
public class Property {

    private String key;
    private String value;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
