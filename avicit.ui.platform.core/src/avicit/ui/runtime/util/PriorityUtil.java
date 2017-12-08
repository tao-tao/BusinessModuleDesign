package avicit.ui.runtime.util;

import java.util.HashMap;
import java.util.Map;

public class PriorityUtil {

    private PriorityUtil()
    {
    }

    public static int getPriority(String r_Priority)
    {
        if(r_Priority == null)
            return 0;
        Integer t_Value = (Integer)priorityMap.get(r_Priority.toLowerCase());
        if(t_Value == null)
            return 0;
        else
            return t_Value.intValue();
    }

    private static Map priorityMap;
    public static final String LOWEST = "lowest";
    public static final String LOWER = "lower";
    public static final String LOW = "low";
    public static final String HIGH = "high";
    public static final String HIGHER = "higher";
    public static final String HIGHEST = "highest";

    static 
    {
        priorityMap = new HashMap();
        priorityMap.put("lowest", new Integer(-100));
        priorityMap.put("lower", new Integer(-60));
        priorityMap.put("low", new Integer(-30));
        priorityMap.put("high", new Integer(30));
        priorityMap.put("higher", new Integer(60));
        priorityMap.put("highest", new Integer(100));
    }

}
