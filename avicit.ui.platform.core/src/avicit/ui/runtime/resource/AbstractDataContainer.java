package avicit.ui.runtime.resource;

import avicit.ui.runtime.util.IDataContainer;

public class AbstractDataContainer extends AbstractAdaptableObject
    implements IDataContainer
{

    public AbstractDataContainer()
    {
    }

    public Object getData()
    {
        return (state & 4) == 0 ? data : ((Object[])data)[0];
    }

    public Object getData(String r_Key)
    {
        if(r_Key == null)
            return null;
        if((state & 4) != 0)
        {
            Object t_Table[] = (Object[])data;
            for(int i = 1; i < t_Table.length; i += 2)
                if(r_Key.equals(t_Table[i]))
                    return t_Table[i + 1];

        }
        return null;
    }

    public void setData(Object r_Data)
    {
        if((state & 4) != 0)
            ((Object[])data)[0] = r_Data;
        else
            data = r_Data;
    }

    public void setData(String r_Key, Object r_Data)
    {
        if(r_Key == null)
            return;
        int t_Index = 1;
        Object t_Table[] = (Object[])null;
        if((state & 4) != 0)
            for(t_Table = (Object[])data; t_Index < t_Table.length; t_Index += 2)
                if(r_Key.equals(t_Table[t_Index]))
                    break;

        if(r_Data != null)
        {
            if((state & 4) != 0)
            {
                if(t_Index == t_Table.length)
                {
                    Object newTable[] = new Object[t_Table.length + 2];
                    System.arraycopy(((Object) (t_Table)), 0, ((Object) (newTable)), 0, t_Table.length);
                    data = ((Object) (t_Table = newTable));
                }
            } else
            {
                t_Table = new Object[3];
                t_Table[0] = data;
                data = ((Object) (t_Table));
                state |= 4;
            }
            t_Table[t_Index] = r_Key;
            t_Table[t_Index + 1] = r_Data;
        } else
        if((state & 4) != 0 && t_Index != t_Table.length)
        {
            int t_Length = t_Table.length - 2;
            if(t_Length == 1)
            {
                data = t_Table[0];
                state &= -5;
            } else
            {
                Object newTable[] = new Object[t_Length];
                System.arraycopy(((Object) (t_Table)), 0, ((Object) (newTable)), 0, t_Index);
                System.arraycopy(((Object) (t_Table)), t_Index + 2, ((Object) (newTable)), t_Index, t_Length - t_Index);
                data = ((Object) (newTable));
            }
        }
    }

    private int state;
    private Object data;
    static final int KEYED_DATA = 4;
}