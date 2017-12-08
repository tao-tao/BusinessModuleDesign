package avicit.platform6.tools.codegeneration.core.exception;

import java.io.IOException;

public class TemplateFileNotFoundException extends IOException {
    
    private String fileName;
    
    public TemplateFileNotFoundException(String message, String fileName) {
        super((new StringBuilder(String.valueOf(message))).append(": ").append(fileName).toString());
        this.fileName = fileName;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
