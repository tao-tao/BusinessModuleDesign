package avicit.ui.core.runtime.resource;


import java.util.Properties;

public interface IMessageCaller
{

    public abstract void error(String s, Properties properties);

    public abstract void info(String s, Properties properties);

    public abstract void clear();

    public abstract void warn(String s, Properties properties);

    public abstract boolean hasError();
}