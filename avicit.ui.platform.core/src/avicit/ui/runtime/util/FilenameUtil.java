package avicit.ui.runtime.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

public final class FilenameUtil
{

    private FilenameUtil()
    {
    }

    public static String getRelativePath(String r_FolderName, String r_ResourceName)
    {
        if(null == r_FolderName || null == r_ResourceName)
        {
            return null;
        } else
        {
            String t_FolderName = normalizeInUnixStyle(r_FolderName);
            String t_ResourceName = normalizeInUnixStyle(r_ResourceName);
            return StringUtil.remove(t_ResourceName, t_FolderName);
        }
    }

    public static String getRelativePath(File r_Folder, File r_Resource)
    {
        if(null == r_Folder || null == r_Resource)
            return null;
        else
            return getRelativePath(r_Folder.getAbsolutePath(), r_Resource.getAbsolutePath());
    }

    public static String normalizeInUnixStyle(String path)
    {
        path = FilenameUtils.normalize(path);
        return FilenameUtils.separatorsToUnix(path);
    }

    public static String toPathInUnixStyle(String r_PackageName)
    {
        String t_Path = StringUtil.replace(r_PackageName, ".", "/");
        t_Path = normalizeInUnixStyle(t_Path);
        if(null != t_Path && t_Path.startsWith("/"))
            t_Path = StringUtil.removeStart(t_Path, "/");
        if(null != t_Path && t_Path.endsWith("/"))
            t_Path = StringUtil.removeEnd(t_Path, "/");
        return t_Path;
    }

    public static String toPackage(String r_Path)
    {
        String t_Path = normalizeInUnixStyle(r_Path);
        t_Path = StringUtil.replace(t_Path, "/", ".");
        if(null != t_Path && t_Path.startsWith("."))
            t_Path = StringUtil.removeStart(t_Path, ".");
        if(null != t_Path && t_Path.endsWith("."))
            t_Path = StringUtil.removeEnd(t_Path, ".");
        return t_Path;
    }

    public static String toPackageWithoutExtension(String r_Path)
    {
        String t_Path = FilenameUtils.removeExtension(r_Path);
        t_Path = normalizeInUnixStyle(t_Path);
        t_Path = StringUtil.replace(t_Path, "/", ".");
        if(null != t_Path && t_Path.startsWith("."))
            t_Path = StringUtil.removeStart(t_Path, ".");
        return t_Path;
    }

    public static String getPackagePath(Class clazz)
    {
        return clazz != null ? (new StringBuilder()).append(clazz.getPackage().getName().replace(".", "/")).append("/").toString() : null;
    }

    public static String getFileName(String className)
    {
        return (new StringBuilder()).append("/").append(StringUtil.replace(className, ".", File.separator)).toString();
    }

    public static String getFileName(Class clazz)
    {
        return clazz != null ? (new StringBuilder()).append("/").append(StringUtil.replace(clazz.getName(), ".", File.separator)).toString() : null;
    }

    public static String getAbsoluteFilePath(Class clazz, String fileName)
    {
        String path = getPackagePath(clazz);
        if(path == null)
            return null;
        else
            return clazz.getClassLoader().getResource((new StringBuilder()).append(path).append(fileName).toString()).getFile();
    }

    public static String getAbsoluteClassPath(Class clazz)
    {
        if(clazz == null)
            clazz = FilenameUtil.class;
        String qnamePath = clazz.getName().replace('.', '/');
        String currentPath = UrlUtil.getURL(qnamePath.concat(".class")).getFile();
        int index = currentPath.lastIndexOf(qnamePath);
        return currentPath.substring(0, index);
    }

    public static String[] getAllFileNames(File file)
    {
        List fileList = new ArrayList();
        getAllFileNames(file, fileList);
        return (String[])fileList.toArray(new String[0]);
    }

    public static void getAllFileNames(File file, List fileNameList)
    {
        if(file.isFile())
        {
            fileNameList.add(file.getAbsolutePath());
            return;
        }
        File arr$[] = file.listFiles();
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            File f = arr$[i$];
            getAllFileNames(f, fileNameList);
        }

    }

    public static boolean isPrefixOf(File folder, File file)
    {
        if(null == folder || null == file)
            return false;
        else
            return isPrefixOf(folder.getAbsolutePath(), file.getAbsolutePath());
    }

    public static boolean isPrefixOf(String folderName, String fileName)
    {
        if(null == folderName || null == fileName)
        {
            return false;
        } else
        {
            String t_FolderName = normalizeInUnixStyle(folderName);
            String t_ResourceName = normalizeInUnixStyle(fileName);
            return t_ResourceName.startsWith(t_FolderName);
        }
    }

    public static boolean isAbsolutePath(String filename)
    {
        int len = filename.length();
        if(len == 0)
            return false;
        char sep = File.separatorChar;
        filename = filename.replace('/', sep).replace('\\', sep);
        char c = filename.charAt(0);
        if(!onDos && !onNetWare)
            return c == sep;
        if(c == sep)
        {
            if(!onDos || len <= 4 || filename.charAt(1) != sep)
            {
                return false;
            } else
            {
                int nextsep = filename.indexOf(sep, 2);
                return nextsep > 2 && nextsep + 1 < len;
            }
        } else
        {
            int colon = filename.indexOf(':');
            return Character.isLetter(c) && colon == 1 && filename.length() > 2 && filename.charAt(2) == sep || onNetWare && colon > 0;
        }
    }

    public static boolean onNetWare = OsUtil.isFamily("netware");
    public static boolean onDos = OsUtil.isFamily("dos");

}