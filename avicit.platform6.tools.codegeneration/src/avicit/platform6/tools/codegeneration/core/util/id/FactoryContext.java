package avicit.platform6.tools.codegeneration.core.util.id;

/**
 * 工厂模式的上下文
 *
 * @author zhaody@gmail.com
 * @version 1.0
 */
public interface FactoryContext {
    
    // 用于单态的锁的对象
    public static Object lock = new Object();
    
//    public static Object getInstance();
    /**
     * 私有的构造函数
     */
//    private FactoryContext() {}
    
    /**
     * 工厂的重载
     */
//    public static void refresh() {}
    
    //初始化的静态模式
//    protected static void initialize() {}
    
}
