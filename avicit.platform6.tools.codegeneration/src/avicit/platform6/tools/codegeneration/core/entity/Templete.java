/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package avicit.platform6.tools.codegeneration.core.entity;

/**
 *
 * @author Administrator
 */
public class Templete {

    private String file_name;
    private String pack_name;
    private String file_postfix;
    private String file_prefix;
    private String encoding;
    private String dir_type;

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getPack_name() {
        return pack_name;
    }

    public void setPack_name(String pack_name) {
        this.pack_name = pack_name;
    }

    public String getFile_postfix() {
        return file_postfix;
    }

    public void setFile_postfix(String file_postfix) {
        this.file_postfix = file_postfix;
    }

    public String getFile_prefix() {
        return file_prefix;
    }

    public void setFile_prefix(String file_prefix) {
        this.file_prefix = file_prefix;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getDir_type() {
        return dir_type;
    }

    public void setDir_type(String dir_type) {
        this.dir_type = dir_type;
    }
}
