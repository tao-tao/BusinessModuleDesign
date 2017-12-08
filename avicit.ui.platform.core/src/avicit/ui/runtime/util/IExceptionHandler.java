package avicit.ui.runtime.util;

public interface IExceptionHandler
{

    public abstract String getLocalizedMessage(Throwable throwable);

    public abstract void handleException(Throwable throwable);

    public abstract String getLocalizedMessage(int i);

    public abstract void handleException(int i);

    public abstract boolean isLogEnable();

    public abstract void setLogEnable(boolean flag);

    public abstract String getLogLayout();

    public abstract void setLogLayout(String s);

    public abstract int getPriority();

    public abstract void setPriority(int i);

    public static final String LOG_ENABLE = "logEnable";
    public static final String LOG_LAYOUT = "logLayout";
}