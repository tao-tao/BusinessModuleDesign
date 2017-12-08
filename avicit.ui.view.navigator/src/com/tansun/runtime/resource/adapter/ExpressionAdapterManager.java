package com.tansun.runtime.resource.adapter;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.eclipse.core.expressions.ElementHandler;
import org.eclipse.core.expressions.ExpressionConverter;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import avicit.ui.common.util.PriorityUtil;
import avicit.ui.common.util.PropertyComparator;
import avicit.ui.common.util.StringMapEntry;

public class ExpressionAdapterManager {

    public ExpressionAdapterManager()
    {
        adapterTypes = new HashMap();
        load();
    }

    public void load()
    {
        IExtensionRegistry t_ExtensionRegistry = Platform.getExtensionRegistry();
        IExtensionPoint t_ExtensionPoint = t_ExtensionRegistry.getExtensionPoint("avicit.ui.view.navigator.adapter");
        IExtension t_Extensions[] = t_ExtensionPoint.getExtensions();
        for(int i = 0; i < t_Extensions.length; i++)
        {
            IExtension t_Extension = t_Extensions[i];
            IConfigurationElement t_ConfigurationElements[] = t_Extension.getConfigurationElements();
            for(int j = 0; j < t_ConfigurationElements.length; j++)
            {
                IConfigurationElement t_ConfigurationElement = t_ConfigurationElements[j];
                doLoad(t_ConfigurationElement);
            }
        }
    }

    private void doLoad(IConfigurationElement r_ConfigurationElement)
    {
        String t_AdaptableType = r_ConfigurationElement.getAttribute("adaptableType");
        try
        {
            Object t_Object = r_ConfigurationElement.createExecutableExtension("class");
            if(t_Object instanceof IAdapterFactory)
            {
                StringMapEntry t_Entry = (StringMapEntry)adapterTypes.get(t_AdaptableType);
                if(t_Entry == null)
                {
                    t_Entry = new StringMapEntry();
                    t_Entry.setKey(t_AdaptableType);
                    t_Entry.setValue(newSet());
                    adapterTypes.put(t_AdaptableType, t_Entry);
                }
                int t_Priority = PriorityUtil.getPriority(r_ConfigurationElement.getAttribute("priority"));
                AdapterConfiguration t_Configuration = new AdapterConfiguration();
                t_Configuration.setAdapterFactory((IAdapterFactory)t_Object);
                t_Configuration.setPriority(t_Priority);
                IConfigurationElement t_ChildrenConfigurationElements[] = r_ConfigurationElement.getChildren();
                if(t_ChildrenConfigurationElements.length >0)
                {
                    for(int i = 0; i < t_ChildrenConfigurationElements.length; i++)
                    {
                        IConfigurationElement t_Element = t_ChildrenConfigurationElements[i];
                        if("adapter".equals(t_Element.getName()))
                            t_Configuration.addAdapter(t_Element.getAttribute("class"));
                        if("enablement".equals(t_Element.getName()))
                        {
                            org.eclipse.core.expressions.Expression t_Expression = ElementHandler.getDefault().create(ExpressionConverter.getDefault(), t_Element);
                            t_Configuration.setExpression(t_Expression);
                        }
                    }

                }
                Collection t_Collection = (Collection)t_Entry.getValue();
                t_Collection.add(t_Configuration);
            }
        }
        catch(CoreException _ex)
        {
            return;
        }
    }

    public static final ExpressionAdapterManager getInstance()
    {
        return instance;
    }

    private Set newSet()
    {
        return new TreeSet(new PropertyComparator("priority"));
    }

    public Object getAdapter(Object r_Object, Class r_Adaper)
    {
        if(r_Object == null || r_Adaper == null)
            return null;
        StringMapEntry t_Entry = (StringMapEntry)adapterTypes.get(r_Object.getClass().getName());
        if(t_Entry == null)
            return null;
        Collection t_Collection = (Collection)t_Entry.getValue();
        for(Iterator t_Iterator = t_Collection.iterator(); t_Iterator.hasNext();)
        {
            AdapterConfiguration t_Configuration = (AdapterConfiguration)t_Iterator.next();
            if(t_Configuration.isAdapterable(r_Object, r_Adaper.getName()))
            {
                Object adapter = t_Configuration.getAdapterFactory().getAdapter(r_Object, r_Adaper);
                if(adapter != null)
                    return adapter;
            }
        }

        return null;
    }
    private static final ExpressionAdapterManager instance = new ExpressionAdapterManager();
    private Map adapterTypes;

}
