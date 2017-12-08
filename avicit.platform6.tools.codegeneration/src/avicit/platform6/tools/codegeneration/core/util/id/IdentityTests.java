package avicit.platform6.tools.codegeneration.core.util.id;

/**
 * 使用的具体的测试类
 *
 * @author zhaody@gmail.com
 * @version 1.0
 */
public class IdentityTests {


    public static void main(String[] args) {
        Identity identity = IdentityFactory.getInstance();
        long i= System.currentTimeMillis();
        // 获得9999个
        String[] ids = identity.getIdentitys(9999);
        i=System.currentTimeMillis()-i;
    }
}
