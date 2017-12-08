package avicit.ui.common.util;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;


public class CollectionUtil
{

    private CollectionUtil()
    {
    }

    public static void addAllQuietly(Collection r_Collection, Object r_Objects[])
    {
        if(null == r_Objects)
            return;
        for(int i = 0; i < r_Objects.length; i++)
            if(null != r_Objects[i])
                r_Collection.add(r_Objects[i]);

    }

    public static boolean hasType(Collection collection, Class clazz)
    {
        return hasType(collection, clazz, false);
    }

    public static boolean hasType(Collection collection, Class clazz, boolean allowSuperType)
    {
label0:
        {
            if(CollectionUtils.isEmpty(collection))
                return false;
            if(null == clazz)
                return false;
            Iterator iterator = collection.iterator();
            Object object;
label1:
            do
            {
                do
                {
                    do
                    {
                        if(!iterator.hasNext())
                            break label0;
                        object = iterator.next();
                    } while(null == object);
                    if(!allowSuperType)
                        continue label1;
                } while(!clazz.isAssignableFrom(object.getClass()));
                return true;
            } while(clazz != object.getClass());
            return true;
        }
        return false;
    }

    public static Map newIdentityMap()
    {
        return new InternalMap();
    }
    static class InternalMap  extends HashMap
    {

        public boolean equals(Object object)
        {
            return object == this;
        }

        public int hashCode()
        {
            return System.identityHashCode(this);
        }

        private static final long serialVersionUID = 1L;

        InternalMap()
        {
        }
    }

}