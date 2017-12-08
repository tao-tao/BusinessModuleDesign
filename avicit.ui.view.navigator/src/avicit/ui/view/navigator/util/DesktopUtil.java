package avicit.ui.view.navigator.util;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;

import avicit.ui.view.navigator.Activator;

import com.jniwrapper.win32.mapi.MapiException;
import com.jniwrapper.win32.mapi.MapiMessage;
import com.jniwrapper.win32.shell.Shell32;

public class DesktopUtil
{

    public DesktopUtil()
    {
    }

    public static void sendToMailRecipient(File theSelectedFile)
    {
        if(theSelectedFile == null)
        {
            return;
        } else
        {
            sendToMailRecipient(new Object[] {
                theSelectedFile
            });
            return;
        }
    }

    public static void open(File theSelectedFile)
    {
        if(theSelectedFile == null)
            return;
        try
        {
            Shell32.open(theSelectedFile);
        }
        catch(RuntimeException e)
        {
            warnException(e);
        }
    }

    public static void explore(File theSelectedFile)
    {
        if(theSelectedFile == null)
            return;
        File target = theSelectedFile;
        if(target.isFile())
        {
            target = target.getParentFile();
            if(target == null)
                return;
        }
        try {
			String realpath = FileLocator.resolve(Activator.getDefault().getBundle().getEntry("/")).getFile()+"/lib";
			String javaLibraryPath = System.getProperty("java.library.path") + ";" + realpath;
			System.out.println("before path : " + javaLibraryPath);
			javaLibraryPath = formatPluginPath(javaLibraryPath);
			System.out.println("after path : " + javaLibraryPath);
        	System.setProperty("java.library.path", javaLibraryPath);//重设本地路径,为指向本地路径用
        	System.out.println("avicit : target path is :" + target.getAbsolutePath());
            Shell32.shellExecute(null, "open", target.getAbsolutePath(), null, null, com.jniwrapper.win32.ui.Wnd.ShowWindowCommand.SHOWNORMAL);
        } catch(RuntimeException e) {
            warnException(e);
        } catch (IOException e) {
			e.printStackTrace();
		}
    }

    public static void warnException(Exception e)
    {
        MessageDialog.openError(null, Messages.DesktopUtil_title_errorWarnDialog, MessageFormat.format(Messages.DesktopUtil_message_errorWarnDialog, new Object[] {
            e.getMessage()
        }));
    }
    
    
	public static String formatPluginPath (String jarPath){
		String path = jarPath.toString();
		if(null!=path && path.indexOf("file:")!=-1){
			path = path.replaceAll("file:", ";");
		}
		return path;
		
	}
	
    public static void sendToMailRecipient(Object theSelectedFiles[])
    {
        if(theSelectedFiles == null)
            return;
        if(theSelectedFiles.length < 1)
            return;
        MapiMessage message = new MapiMessage();
        if(theSelectedFiles.length > 1)
        {
            StringBuffer filenames = new StringBuffer();
            ArrayList files = new ArrayList(theSelectedFiles.length);
            for(int i = 0; i < theSelectedFiles.length; i++)
                if(theSelectedFiles[i] instanceof File)
                {
                    File file = (File)theSelectedFiles[i];
                    files.add(file);
                    filenames.append(file.getName());
                    if(i + 1 < theSelectedFiles.length)
                        filenames.append(' ');
                }

            message.attachFiles(files);
            message.setSubject(MessageFormat.format(Messages.DesktopUtil_title_emailSubject_plural, new Object[] {
                filenames.toString()
            }));
        } else
        {
            File file = (File)theSelectedFiles[0];
            message.attachFile(file);
            message.setSubject(MessageFormat.format(Messages.DesktopUtil_title_emailSubject, new Object[] {
                file.getName()
            }));
        }
        try
        {
            message.send(true);
        }
        catch(MapiException e)
        {
        }
    }
}