package avicit.ui.runtime.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

public class ExceptionUtil {

    private ExceptionUtil()
    {
    }

    public static ExceptionUtil getInstance()
    {
        return instance;
    }

    public synchronized void loadErrorHandlers()
    {
        IExtensionRegistry t_ExtensionRegistry = Platform.getExtensionRegistry();
        IExtensionPoint t_ExtensionPoint = t_ExtensionRegistry.getExtensionPoint("org.primeton.studio.core.exception.exceptionHandler");
        if(t_ExtensionPoint == null)
            return;
        IExtension t_Extensions[] = t_ExtensionPoint.getExtensions();
        for(int i = 0; i < t_Extensions.length; i++)
        {
            IExtension t_Extension = t_Extensions[i];
            IConfigurationElement t_ConfigurationElements[] = t_Extension.getConfigurationElements();
            for(int j = 0; j < t_ConfigurationElements.length; j++)
            {
                IConfigurationElement t_ConfigurationElement = t_ConfigurationElements[j];
                if("exceptionHandler".equals(t_ConfigurationElement.getName()))
                    loadExceptionHandler(t_ConfigurationElement);
                if("errorCodeHandler".equals(t_ConfigurationElement.getName()))
                    loadErrorCodeHandler(t_ConfigurationElement);
            }

        }

    }

    private void loadErrorCodeHandler(IConfigurationElement r_ConfigurationElement)
    {
        try
        {
            Object t_Object = r_ConfigurationElement.createExecutableExtension("className");
            if(t_Object instanceof IExceptionHandler)
            {
                IExceptionHandler t_Handler = (IExceptionHandler)t_Object;
                updateHandler(t_Handler, r_ConfigurationElement);
                String t_Name = r_ConfigurationElement.getAttribute("errorCode");
                Set t_Set = (Set)(Set)errorCodeHandlers.get(t_Name);
                if(t_Set == null)
                {
                    t_Set = buildSortSet();
                    errorCodeHandlers.put(t_Name, t_Set);
                }
                t_Set.add(t_Handler);
            }
        }
        catch(CoreException _ex) { }
    }

    private void loadExceptionHandler(IConfigurationElement r_ConfigurationElement)
    {
        try
        {
            Object t_Object = r_ConfigurationElement.createExecutableExtension("className");
            if(t_Object instanceof IExceptionHandler)
            {
                IExceptionHandler t_Handler = (IExceptionHandler)t_Object;
                updateHandler(t_Handler, r_ConfigurationElement);
                String t_Name = r_ConfigurationElement.getAttribute("exception");
                Set t_Set = (Set)(Set)exceptionHandlers.get(t_Name);
                if(t_Set == null)
                {
                    t_Set = buildSortSet();
                    exceptionHandlers.put(t_Name, t_Set);
                }
                t_Set.add(t_Handler);
            }
        }
        catch(CoreException _ex) { }
    }

    private Set buildSortSet()
    {
        ReverseComparator t_Comparator = new ReverseComparator(new PropertyComparator("priority"));
        return new TreeSet(t_Comparator);
    }

    private void updateHandler(IExceptionHandler r_Handler, IConfigurationElement r_ConfigurationElement)
    {
        String t_LogEnableString = r_ConfigurationElement.getAttribute("logEnable");
        boolean t_LogEnable = Boolean.valueOf(t_LogEnableString).booleanValue();
        String t_PriorityString = r_ConfigurationElement.getAttribute("priority");
        int t_Priority = NumberUtils.toInt(t_PriorityString);
        String t_LogLayout = r_ConfigurationElement.getAttribute("logLayout");
        r_Handler.setLogEnable(t_LogEnable);
        r_Handler.setLogLayout(t_LogLayout);
        r_Handler.setPriority(t_Priority);
    }

    public void handleException(Exception r_Exception)
    {
        handleException(new ExceptionAdapter(r_Exception));
    }

    public void handleException(ExceptionAdapter r_Exception)
    {
        if(r_Exception == null)
            return;
        boolean t_Flag = false;
        if(r_Exception.getErrorCode() != 0 && processExceptionByErrorCode(r_Exception))
            t_Flag = true;
        if(r_Exception.getCause() != null && processException(r_Exception.getCause()))
            t_Flag = true;
        if(!t_Flag)
            logException(r_Exception);
    }

    private boolean processExceptionByErrorCode(ExceptionAdapter r_Exception)
    {
        Set t_Set = (Set)(Set)errorCodeHandlers.get(Integer.toString(r_Exception.getErrorCode()));
        if(t_Set == null || t_Set.isEmpty())
            return false;
        boolean t_Flag = false;
        for(Iterator t_Iterator = t_Set.iterator(); t_Iterator.hasNext();)
        {
            IExceptionHandler t_ExceptionHandler = (IExceptionHandler)t_Iterator.next();
            try
            {
                t_ExceptionHandler.handleException(r_Exception.getErrorCode());
                t_Flag = true;
            }
            catch(Exception _ex) { }
            if(t_ExceptionHandler.isLogEnable())
                logException(r_Exception);
        }

        return t_Flag;
    }

    private boolean processException(Throwable r_Cause)
    {
        Set t_Set = (Set)(Set)errorCodeHandlers.get(r_Cause);
        if(t_Set == null || t_Set.isEmpty())
            return false;
        boolean t_Flag = false;
        for(Iterator t_Iterator = t_Set.iterator(); t_Iterator.hasNext();)
        {
            IExceptionHandler t_ExceptionHandler = (IExceptionHandler)t_Iterator.next();
            try
            {
                t_ExceptionHandler.handleException(r_Cause);
                t_Flag = true;
            }
            catch(Exception _ex) { }
            if(t_ExceptionHandler.isLogEnable())
                logException(r_Cause);
        }

        return t_Flag;
    }

    public void logException(Throwable r_Cause)
    {
        Status t_Status = new Status(4, "com.primeton.studio.core", 0, "Primeton Studio Exception", r_Cause);
//        if(Activator.getDefault() != null)
//        	Activator.getDefault().getLog().log(t_Status);
    }

    public static final String ERROR_HANDLER_ID = "org.primeton.studio.core.exception.exceptionHandler";
    public static final String EXCEPTION_HANDLER = "exceptionHandler";
    public static final String ERROR_CODE_HANDLER = "errorCodeHandler";
    public static final String EXCEPTION = "exception";
    public static final String ERROR_CODE = "errorCode";
    private final Map exceptionHandlers = new HashMap();
    private final Map errorCodeHandlers = new HashMap();
    private static ExceptionUtil instance = new ExceptionUtil();


}
