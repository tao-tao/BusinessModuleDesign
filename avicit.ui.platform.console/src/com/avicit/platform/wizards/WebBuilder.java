
package com.avicit.platform.wizards;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.internal.ui.util.CoreUtility;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: tansun
 * </p>
 * 
 * @author 169
 * @version 1.0
 */
public class WebBuilder {
	public static final String SYS_CONFIG_DIR = "sysxml";

	public static final String USER_CONFIG_DIR = "uiilxml";

	public static void createFolder(IFolder folder, IProgressMonitor monitor)
			throws CoreException {
		CoreUtility.createFolder(folder, false, true, new SubProgressMonitor(
				monitor, 10));
	}
    public static String[] getFileName(IPath ipath)
    {
        String s = ipath.toString();
        File file = null;
        if(s != null && s.trim().length() > 0)
        {
            file = new File(s);
            if(!file.isDirectory())
                return new String[0];
            file = file.getAbsoluteFile();
        }
        String as[] = file.list();
        ArrayList arraylist = new ArrayList(as.length);
        for(int i = 0; i < as.length; i++)
        {
            IPath ipath1 = ipath.append(as[i]);
            arraylist.add(ipath1.toString());
        }
        return as;
    }
}