package avicit.ui.runtime.util;

public class MapEntry implements ICloneable {

	public MapEntry() {
	}

	public MapEntry(Object r_Key, Object r_Value) {
		key = r_Key;
		value = r_Value;
	}

	public boolean equals(Object r_Value) {
		if (r_Value == null)
			return false;
		if (this == r_Value)
			return true;
		if (!(r_Value instanceof MapEntry))
			return false;
		MapEntry t_Value = (MapEntry) r_Value;
		return (getKey() != null ? getKey().equals(t_Value.getKey()) : t_Value
				.getKey() == null)
				&& (getValue() != null ? getValue().equals(t_Value.getValue())
						: t_Value.getValue() == null);
	}

	public int hashCode() {
		return (getKey() != null ? getKey().hashCode() : 0)
				^ (getValue() != null ? getValue().hashCode() : 0);
	}

	public Object getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public void setKey(Object r_Key) {
		key = r_Key;
	}

	public void setValue(Object r_Value) {
		value = r_Value;
	}

	public Object clone() {
		MapEntry t_Entry = new MapEntry();
		if (key instanceof ICloneable)
			t_Entry.key = ((ICloneable) key).clone();
		else
			t_Entry.key = key;
		if (value instanceof ICloneable)
			t_Entry.value = ((ICloneable) value).clone();
		else
			t_Entry.value = value;
		return t_Entry;
	}

	public String toString() {
		return (new StringBuilder()).append(key).append(" = ").append(value)
				.toString();
	}

	private Object key;
	private Object value;
}