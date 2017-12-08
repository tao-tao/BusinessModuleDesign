package avicit.platform6.tools.codegeneration.core.util;

import java.io.File;

public class FileNameFilter   {
    
    private String ext;
    
    public FileNameFilter(String ext) {
        this.ext = ext;
    }
    
    public boolean accept(File dir) {
        if(dir.isDirectory()) {
            return true;
        } else {
            return dir.getName().endsWith(ext);
        }
    }
    
    public String getDescription() {
        if(ext.equals("txt")) {
            return "文本文档(*.txt)";
        }
        if(ext.equals("jar")) {
            return "jar文件(*.jar)";
        } else {
            return "所有文件(*.*)";
        }
    }
}
