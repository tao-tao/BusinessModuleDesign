package avicit.ui.common.util;


public class StringMapEntry     implements ICloneable
{

    public StringMapEntry()
    {
    }

    public StringMapEntry(String r_Key, Object r_Value)
    {
        key = r_Key;
        value = r_Value;
    }

    public boolean equals(Object r_Value)
    {
        if(r_Value == null)
            return false;
        if(this == r_Value)
            return true;
        if(!(r_Value instanceof StringMapEntry))
            return false;
        StringMapEntry t_Value = (StringMapEntry)r_Value;
        return (getKey() != null ? getKey().equals(t_Value.getKey()) : t_Value.getKey() == null) && (getValue() != null ? getValue().equals(t_Value.getValue()) : t_Value.getValue() == null);
    }

    public int hashCode()
    {
        return (getKey() != null ? getKey().hashCode() : 0) ^ (getValue() != null ? getValue().hashCode() : 0);
    }

    public String getKey()
    {
        return key;
    }

    public Object getValue()
    {
        return value;
    }

    public void setKey(String r_Key)
    {
        key = r_Key;
    }

    public void setValue(Object r_Value)
    {
        value = r_Value;
    }

    public Object clone()
    {
        StringMapEntry t_Entry = new StringMapEntry();
        t_Entry.key = key;
        if(value instanceof ICloneable)
            t_Entry.value = ((ICloneable)value).clone();
        else
            t_Entry.value = value;
        return t_Entry;
    }

    public String toString()
    {
        return (new StringBuilder(String.valueOf(key))).append("=").append(value).toString();
    }

    private String key;
    private Object value;
}