package com.tansun.runtime.resource.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import avicit.ui.common.util.IPlatformConstants;
import avicit.ui.core.runtime.resource.EclipseFileDelegate;
import avicit.ui.runtime.core.node.CommonChildNode;
import avicit.ui.runtime.core.node.CommonNode;

public class CommonWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

    private CommonWorkbenchAdapter()
    {
    }

    public static CommonWorkbenchAdapter getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	CommonNode node = (CommonNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
		CommonNode node = (CommonNode) object;
		List chidren = node.getChildren();
		if (chidren == null) {
			List presFiles = new ArrayList();
			IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(IPlatformConstants.IntroductionPageConfigID);
			if (elements.length > 0) {
				for (int i = 0; i < elements.length; i++) {
					IConfigurationElement[] children = elements[i].getChildren();
					for (int ii = 0; ii < children.length; ii++) {
						IConfigurationElement element = children[ii];
						String fileID = element.getAttribute("fileID"); //$NON-NLS-1$
						if(StringUtils.isEmpty(fileID))
							continue;
						String iconPath = element.getAttribute("icon16"); //$NON-NLS-1$
						String tip = element.getAttribute("text"); //$NON-NLS-1$
						String editorID = element.getAttribute("editorID"); //$NON-NLS-1$
						String name = element.getAttribute("hyperlink"); //$NON-NLS-1$
						String type = element.getAttribute("type");
						ImageDescriptor imageDescriptor = null;
						
						if (iconPath != null) {
							String iconName;
							if (iconPath.indexOf(IPath.SEPARATOR) != -1) {
								iconName = new Path(iconPath).lastSegment();
							} else {
								iconName = iconPath;
							}

							Plugin plugin = Platform.getPlugin(element.getDeclaringExtension().getContributor().getName());
							if (plugin instanceof AbstractUIPlugin) {
								ImageRegistry imageRegistry = ((AbstractUIPlugin) plugin).getImageRegistry();
								imageDescriptor = imageRegistry.getDescriptor(iconName);
								if (imageDescriptor == null) {
									imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(element.getDeclaringExtension().getContributor().getName(), iconPath);
									imageRegistry.put(iconName, imageDescriptor);
								}
							}
						}
						IFile file = node.getResource().getResource().getProject().getFile(new Path(fileID));
						CommonChildNode child = new CommonChildNode(new EclipseFileDelegate(file));
						child.setOrder(ii);
						child.setName(name);
						child.setTip(tip);
						child.setEditorID(editorID);
						child.setImage(imageDescriptor);
						child.setDbtype(type);
						presFiles.add(child);
					}
				}
			}
			node.setChildren(presFiles);
			return presFiles.toArray();
		}
		return chidren.toArray();
    }

	public boolean hasChildren(Object element) {
		return true;
	}

    private static final CommonWorkbenchAdapter instance = new CommonWorkbenchAdapter();
}