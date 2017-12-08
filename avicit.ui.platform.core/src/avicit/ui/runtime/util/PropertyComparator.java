package avicit.ui.runtime.util;

import java.util.Comparator;

import ognl.Ognl;

import org.apache.commons.lang.ObjectUtils;

public class PropertyComparator     implements Comparator
{

    public PropertyComparator(String r_PropertyName)
    {
        propertyName = r_PropertyName;
    }

    public int compare(Object r_FirstObject, Object r_SecondObject)
    {
        if(ObjectUtils.equals(r_FirstObject, r_SecondObject))
            return 0;
        int t_Result;
        try{
	        Object t_FirstObject = Ognl.getValue(propertyName, r_FirstObject);
	        Object t_SecondObject = Ognl.getValue(propertyName, r_SecondObject);
	        if((t_FirstObject instanceof Comparable) && (t_SecondObject instanceof Comparable))
	        {
	            t_Result = ((Comparable)t_FirstObject).compareTo(t_SecondObject);
	        } else
	        {
	            String t_FirstString = ObjectUtils.toString(t_FirstObject);
	            String t_SecondString = ObjectUtils.toString(t_SecondObject);
	            t_Result = t_FirstString.compareTo(t_SecondString);
	        }
	        if(t_Result == 0)
	            return 1;
	        return t_Result;
        }catch(Exception e){
        	return -1;
        }
    }

    private final transient String propertyName;
}
