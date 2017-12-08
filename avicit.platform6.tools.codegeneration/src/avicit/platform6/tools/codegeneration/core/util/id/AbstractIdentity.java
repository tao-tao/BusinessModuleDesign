package avicit.platform6.tools.codegeneration.core.util.id;


public abstract class AbstractIdentity
    implements Identity {
    
    
    public AbstractIdentity() {
    }
    
    public String getIdentity() {
        return getIdentity("STYLE_DEFAULT");
    }
    
    public String[] getIdentitys(int length) {
        return getIdentitys("STYLE_DEFAULT", length);
    }
    
    public String[] getIdentitys(String identity_STYLE, int length) {
        if(length < 0 || length > 10000) {
            System.err.println("生成的请求的长度超长或超短");
            return new String[0];
        }
        String result[] = new String[length];
        for(int i = 0; i < result.length; i++) {
            result[i] = getIdentity(identity_STYLE);
        }
        
        return result;
    }

}
