package avicit.ui.runtime.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import avicit.ui.runtime.resource.EclipseResourceManager;


public class NameSpaceUtil
{

    private NameSpaceUtil()
    {
    }

    public static String getNameSpace(Object obj, boolean isReal)
    {
        if(obj instanceof IJavaElement)
            return getNameSpace4JavaElement((IJavaElement)obj);
        if(obj instanceof IFile)
        {
            String ns = EclipseResourceManager.getSourceRelativePath((IFile)obj);
            if(ns == null)
                return "";
            else
                return ns.replace("/", ".");
        }
        return "";
    }

    public static String getNameSpace(Object obj)
    {
        return getNameSpace(obj, false);
    }

    public static String getNameSpace4JavaElement(IJavaElement javaElement)
    {
        IClassFile classFile;
        if(javaElement instanceof IType)
            return getNameSpace4JavaType((IType)javaElement);
        if(javaElement instanceof IMethod)
        {
            IMethod method = (IMethod)javaElement;
            return (new StringBuilder(String.valueOf(getNameSpace4JavaType(method.getDeclaringType())))).append(".").append(method.getElementName()).toString();
        }
        if(javaElement instanceof IField)
        {
            IField field = (IField)javaElement;
            return (new StringBuilder(String.valueOf(getNameSpace4JavaType(field.getDeclaringType())))).append(".").append(field.getElementName()).toString();
        }
        if(!(javaElement instanceof IClassFile))
        {
	        classFile = (IClassFile)javaElement;
	        return (new StringBuilder(String.valueOf(getNameSpace4JavaType(classFile.getType())))).append(".").append(classFile.getElementName()).toString();
        }
        if(javaElement instanceof IImportDeclaration)
        {
            return "";
        } else
        {
            String path = EclipseResourceManager.getSourceRelativePath(javaElement.getResource());
            return path != null ? path.replace("/", ".") : "";
        }
    }

    public static String getNameSpace4JavaType(IType javaType)
    {
        return javaType.getFullyQualifiedName();
    }


    private static final String DOT = ".";
}