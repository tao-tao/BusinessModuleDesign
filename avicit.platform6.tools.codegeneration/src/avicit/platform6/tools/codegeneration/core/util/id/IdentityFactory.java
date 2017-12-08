package avicit.platform6.tools.codegeneration.core.util.id;



public abstract class IdentityFactory implements FactoryContext {
    // log

    private IdentityFactory() {}

    private static Identity identity = null;

    /**
     * 获得具体的实现
     * @return Identity
     */
    public static Identity getInstance() {
        if (identity == null)
            initialize();
        return identity;
    }

    /**
     * 工厂的重载
     */
    public static void refresh() {
        identity = null;
        initialize();
    }

    //初始化的静态模式
    protected static void initialize() {
        synchronized (lock) {
            if (identity == null)
                identity = DefaultIdentity.getInstance();
        }
    }

}
