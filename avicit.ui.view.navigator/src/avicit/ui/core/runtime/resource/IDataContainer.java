package avicit.ui.core.runtime.resource;


public interface IDataContainer
{

    public abstract Object getData();

    public abstract Object getData(String s);

    public abstract void setData(Object obj);

    public abstract void setData(String s, Object obj);
}