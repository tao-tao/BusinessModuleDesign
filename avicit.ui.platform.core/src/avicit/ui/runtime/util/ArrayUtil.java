package avicit.ui.runtime.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;

public final class ArrayUtil
{

    private ArrayUtil()
    {
    }

	public static List clone(List source) {
		if(source == null)
			return null;
		List dest = new ArrayList();
		for (int x = 0; x < source.size(); x++) {
			Object obj = source.get(x);
			if(obj instanceof ICloneable)
			{
				dest.add(((ICloneable)obj).clone());
			}
		}
		return dest;	
	}
	
    public static void copyArray(Object sourceObjects[], Object targetObjects[])
        throws IllegalArgumentException
    {
        if(ArrayUtils.isEmpty(sourceObjects) || ArrayUtils.isEmpty(targetObjects))
            return;
        if(sourceObjects.length != targetObjects.length)
        {
            throw new IllegalArgumentException("The length of the two arrays must be equal");
        } else
        {
            System.arraycopy(((Object) (sourceObjects)), 0, ((Object) (targetObjects)), 0, targetObjects.length);
            return;
        }
    }

    public static String[] getStringArrayValues(Object value)
    {
        if(value instanceof Collection)
        {
            String stringArray[] = new String[((Collection)value).size()];
            Iterator iterator = ((Collection)value).iterator();
            for(int index = 0; iterator.hasNext(); index++)
            {
                Object t_Object = iterator.next();
                stringArray[index] = ObjectUtils.toString(t_Object, null);
            }

            return stringArray;
        }
        if(value instanceof Object[])
        {
            Object objects[] = (Object[])(Object[])value;
            return getStringArrayValues(objects);
        } else
        {
            return NULL_STRINGS;
        }
    }

    public static String[] getStringArrayValues(Object value[])
    {
        if(ArrayUtils.isEmpty(value))
            return NULL_STRINGS;
        String stringArray[] = new String[value.length];
        for(int i = 0; i < value.length; i++)
        {
            Object object = value[i];
            stringArray[i] = ObjectUtils.toString(object, null);
        }

        return stringArray;
    }

    public static Object[] getArrayValues(Object value)
    {
        if(value instanceof Object[])
            return (Object[])(Object[])value;
        if(value instanceof Collection)
            return ((Collection)value).toArray();
        else
            return null;
    }

    public static boolean hasType(Object objects[], Class clazz)
    {
        return hasType(objects, clazz, false);
    }

    public static boolean hasType(Object objects[], Class clazz, boolean allowSuperType)
    {
        if(null == clazz)
            return false;
        if(ArrayUtils.isEmpty(objects))
            return false;
        for(int i = 0; i < objects.length; i++)
        {
            Object object = objects[i];
            if(null == object)
                continue;
            if(allowSuperType)
            {
                if(clazz.isAssignableFrom(object.getClass()))
                    return true;
                continue;
            }
            if(clazz == object.getClass())
                return true;
        }

        return false;
    }

    public static Object[] sort(Object array[])
    {
        if(ArrayUtils.isEmpty(array))
        {
            return array;
        } else
        {
            Arrays.sort(array);
            return array;
        }
    }

    public static boolean isArray(Object r_Object)
    {
        if(null == r_Object)
        {
            return false;
        } else
        {
            Class t_Class = r_Object.getClass();
            return t_Class.isArray();
        }
    }

    public static boolean isEmpty(Object array[])
    {
        return array == null || array.length == 0;
    }

    public static String NULL_STRINGS[] = new String[0];
    public static Object NULL_OBJECTS[] = new Object[0];
    public static Class NULL_CLASSES[] = new Class[0];

}