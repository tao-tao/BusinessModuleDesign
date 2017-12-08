package avicit.platform6.tools.codegeneration.core.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Miscellaneous collection utility methods.
 * Mainly for internal use within the framework.
 *
 */
public abstract class CollectionUtil {

    /**
     * is Empty
     * @param collection Collection
     * @return boolean
     */
    public static boolean isEmpty(Collection collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * Return <code>true</code> if the supplied Map is <code>null</code>
     * or empty. Otherwise, return <code>false</code>.
     * @param map the Map to check
     */
    public static boolean isEmpty(Map map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 判断集合之中是否有该对象
     * @param collection the Collection to check
     * @param element the element to look for
     * @return <code>true</code> if found, <code>false</code> else
     */
    public static boolean containsInstance(Collection collection, Object element) {
        if (collection != null) {
            for (Iterator it = collection.iterator(); it.hasNext(); ) {
                Object candidate = it.next();
                if (candidate == element) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check whether the given Iterator contains the given element.
     * @param iterator the Iterator to check
     * @param element the element to look for
     * @return <code>true</code> if found, <code>false</code> else
     */
    public static boolean contains(Iterator iterator, Object element) {
        if (iterator != null) {
            while (iterator.hasNext()) {
                Object candidate = iterator.next();
                if (ObjectUtil.nullSafeEquals(candidate, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check whether the given Enumeration contains the given element.
     * @param enumeration the Enumeration to check
     * @param element the element to look for
     * @return <code>true</code> if found, <code>false</code> else
     */
    public static boolean contains(Enumeration enumeration, Object element) {
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                Object candidate = enumeration.nextElement();
                if (ObjectUtil.nullSafeEquals(candidate, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determine whether the given Collection only contains a single unique object.
     * @param collection the Collection to check
     * @return <code>true</code> if the collection contains a single reference or
     * multiple references to the same instance, <code>false</code> else
     */
    public static boolean hasUniqueObject(Collection collection) {
        if (isEmpty(collection)) {
            return false;
        }
        boolean hasCandidate = false;
        Object candidate = null;
        for (Iterator it = collection.iterator(); it.hasNext(); ) {
            Object elem = it.next();
            if (!hasCandidate) {
                hasCandidate = true;
                candidate = elem;
            } else if (candidate != elem) {
                return false;
            }
        }
        return true;
    }

    /**
     * Find a value of the given type in the given Collection.
     * @param collection the Collection to search
     * @param type the type to look for
     * @return a value of the given type found, or <code>null</code> if none
     * @throws IllegalArgumentException if more than one value of the given type found
     */
    public static Object findValueOfType(Collection collection, Class type) throws IllegalArgumentException {
        if (isEmpty(collection)) {
            return null;
        }
        Class typeToUse = (type != null ? type : Object.class);
        Object value = null;
        for (Iterator it = collection.iterator(); it.hasNext(); ) {
            Object obj = it.next();
            
            if (typeToUse.isInstance(obj)) {
                if (value != null) {
                    throw new IllegalArgumentException("More than one value of type [" + typeToUse.getName() +
                            "] found");
                }
                value = obj;
            }
        }
        return value;
    }

    /**
     * Find a value of one of the given types in the given Collection:
     * searching the Collection for a value of the first type, then
     * searching for a value of the second type, etc.
     * @param collection the collection to search
     * @param types the types to look for, in prioritized order
     * @return a of one of the given types found, or <code>null</code> if none
     * @throws IllegalArgumentException if more than one value of the given type found
     */
    public static Object findValueOfType(Collection collection, Class[] types) throws IllegalArgumentException {
        if (isEmpty(collection) || ObjectUtil.isEmpty(types)) {
            return null;
        }
        for (int i = 0; i < types.length; i++) {
            Object value = findValueOfType(collection, types[i]);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * Convert the supplied array into a List. A primitive array gets
     * converted into a List of the appropriate wrapper type.
     * <p>A <code>null</code> source value will be converted to an
     * empty List.
     * @param source the (potentially primitive) array
     * @return the converted List result
     * @see ObjectUtils#toObjectArray(Object)
     */
    public static List arrayToList(Object source) {
        return Arrays.asList(ObjectUtil.toObjectArray(source));
    }

    /**
     * Merge the given Properties instance into the given Map,
     * copying all properties (key-value pairs) over.
     * <p>Uses <code>Properties.propertyNames()</code> to even catch
     * default properties linked into the original Properties instance.
     * @param props the Properties instance to merge (may be <code>null</code>)
     * @param map the target Map to merge the properties into
     */
    public static void mergePropertiesIntoMap(Properties props, Map map) {
        if (map == null) {
            throw new IllegalArgumentException("Map must not be null");
        }
        if (props != null) {
            for (Enumeration en = props.propertyNames(); en.hasMoreElements(); ) {
                String key = (String) en.nextElement();
                map.put(key, props.getProperty(key));
            }
        }
    }

    /**
     * Return <code>true</code> if any element in '<code>candidates</code>' is
     * contained in '<code>source</code>'; otherwise returns <code>false</code>.
     */
    public static boolean containsAny(Collection source, Collection candidates) {
        if (isEmpty(source) || isEmpty(candidates)) {
            return false;
        }
        for (Iterator it = candidates.iterator(); it.hasNext(); ) {
            if (source.contains(it.next())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the first element in '<code>candidates</code>' that is contained in
     * '<code>source</code>'. If no element in '<code>candidates</code>' is present in
     * '<code>source</code>' returns <code>null</code>. Iteration order is
     * {@link Collection} implementation specific.
     */
    public static Object findFirstMatch(Collection source, Collection candidates) {
        if (isEmpty(source) || isEmpty(candidates)) {
            return null;
        }
        for (Iterator it = candidates.iterator(); it.hasNext(); ) {
            Object candidate = it.next();
            if (source.contains(candidate)) {
                return candidate;
            }
        }
        return null;
    }

}
