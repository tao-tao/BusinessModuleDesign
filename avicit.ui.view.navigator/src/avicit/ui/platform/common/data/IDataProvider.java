package avicit.ui.platform.common.data;

import org.eclipse.core.runtime.IAdaptable;

public interface IDataProvider
{
    public abstract String[] getKeys();

    public abstract String getKey(Object obj);

    public abstract Object getValue(String s);

    public abstract void build(IAdaptable iadaptable);
}