package avicit.ui.runtime.util;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.util.IClassFileReader;
import org.eclipse.jdt.core.util.ICodeAttribute;
import org.eclipse.jdt.core.util.ILocalVariableAttribute;
import org.eclipse.jdt.core.util.ILocalVariableTableEntry;
import org.eclipse.jdt.core.util.IMethodInfo;
import org.eclipse.jdt.internal.core.SourceType;

import avicit.ui.platform.common.util.ProjectClassLoader;
import avicit.ui.platform.common.util.ProjectFinder;
import avicit.ui.runtime.resource.EclipseResourceManager;


public class JdtUtil
{

    private static void loadTypes()
    {
        PrimitiveTypes.put("Boolean", "java.lang.Boolean");
        PrimitiveTypes.put("Integer", "java.lang.Integer");
        PrimitiveTypes.put("Float", "java.lang.Float");
        PrimitiveTypes.put("Double", "java.lang.Double");
        PrimitiveTypes.put("Long", "java.lang.Long");
        PrimitiveTypes.put("Short", "java.lang.Short");
        PrimitiveTypes.put("String", "java.lang.String");
        PrimitiveTypes.put("Byte", "java.lang.Byte");
    }

    private JdtUtil()
    {
    }

    public static IType getType(IProject project, String type)
    {
    	if(StringUtils.isEmpty(type))
    		return null;
    	
		try {
			IJavaProject javaproject = JavaCore.create(project);
			IType type1 = javaproject.findType(type);
			if(type1 != null && type1.exists())
			{
				return type1;
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
        return null;
    }

    public static ICompilationUnit getCompilationUnit(IProject project, String classPathName)
    {
    	if(StringUtils.isEmpty(classPathName))
    		return null;
    	
		try {
			IJavaProject javaproject = JavaCore.create(project);
			IPackageFragmentRoot[] roots;
			IFile javaFile = null;
			roots = javaproject.getAllPackageFragmentRoots();
			
			int index = classPathName.lastIndexOf(".");
			String packageName = index>0?classPathName.substring(0, index):"";
			String className = (index>0?classPathName.substring(index + 1):classPathName) + ".java";
			
			for (int i = 0; i < roots.length; i++) {
				if (roots[i].isArchive())
					continue;
				IPath rootpath = roots[i].getPath();
				IPackageFragment pf = roots[i].getPackageFragment(packageName);
				if(pf != null && pf.exists())
				{
					ICompilationUnit cu = pf.getCompilationUnit(className);
					if(cu != null && cu.exists())
						return cu;
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
        return null;
    }

    public static IFile getClassPathFile(IProject p, String pathName, String ext)
    {
		try {
			IJavaProject project = JavaCore.create(p);
			IPackageFragmentRoot[] roots;
			IFile javaFile = null;
			roots = project.getAllPackageFragmentRoots();

			for (int i = 0; i < roots.length; i++) {
				if (roots[i].isArchive())
					continue;
				IPath rootpath = roots[i].getPath();
				String rp = roots[i].getPath().removeFirstSegments(1).toPortableString();
				IFile file = p.getFile(new Path(rp + "/" + pathName.replace(".", "/") + "." + ext));
				if(file != null && file.exists())
					return file;
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
        return null;
    }

    
    public static boolean isJavaFile(IFile file)
    {
        if(EclipseResourceManager.isValidResource(file))
            return "java".equals(file.getFileExtension());
        else
            return false;
    }

    public static IFile getSourceFile(IFile file)
    {
        IFolder sourceFolder = EclipseResourceManager.getSourceFolderByOutputResource(file);
        if(sourceFolder == null)
            return null;
        String relativePath = EclipseResourceManager.getOutputRelativePath(file);
        if(StringUtils.contains(relativePath, "$"))
            relativePath = StringUtils.substringBefore(relativePath, "$");
        else
            relativePath = FilenameUtils.removeExtension(relativePath);
        relativePath = (new StringBuilder(String.valueOf(relativePath))).append(".java").toString();
        return sourceFolder.getFile(relativePath);
    }

    public static IFile[] computeOutputResources(ICompilationUnit unit)
        throws CoreException
    {
        if(unit == null)
            return EclipseResourceManager.EMPTY_FILES;
        IFile file = (IFile)unit.getResource();
        IFolder outputFolder = EclipseResourceManager.getOutputFolder(file);
        if(outputFolder == null)
            return EclipseResourceManager.EMPTY_FILES;
        String relativePath = EclipseResourceManager.getSourceRelativePath(file.getParent());
        outputFolder = outputFolder.getFolder(relativePath);
        IType types[] = unit.getTypes();
        if(ArrayUtils.isEmpty(types))
            return EclipseResourceManager.EMPTY_FILES;
        String names[] = new String[types.length];
        for(int i = 0; i < types.length; i++)
        {
            IType type = types[i];
            names[i] = (new StringBuilder(String.valueOf(type.getElementName()))).append("*.class").toString();
        }

        return EclipseResourceManager.getFiles(outputFolder, names);
    }

    public static IFile[] computeOutputResources(IFile file)
        throws CoreException
    {
        if(!EclipseResourceManager.isValidResource(file))
            return EclipseResourceManager.EMPTY_FILES;
        if(!"java".equals(file.getFileExtension()))
            return EclipseResourceManager.EMPTY_FILES;
        IJavaElement element = JavaCore.create(file);
        if(element instanceof ICompilationUnit)
            return computeOutputResources((ICompilationUnit)element);
        else
            return EclipseResourceManager.EMPTY_FILES;
    }

    public static String getDeclaringTypeName(IField field)
        throws JavaModelException
    {
        if(field == null)
        {
            return null;
        } else
        {
            IType type = field.getDeclaringType();
            return getFullName(type, field.getTypeSignature());
        }
    }

    public static final String getClassName(IJavaElement element)
    {
        IClassFile classFile;
        IType type;
        if(element instanceof IMember)
        {
            IMember member = (IMember)element;
            return member.getDeclaringType().getFullyQualifiedName('$');
        }
        if(element instanceof ICompilationUnit)
        {
            ICompilationUnit unit = (ICompilationUnit)element;
            type = unit.findPrimaryType();
            return type.getFullyQualifiedName('$');
        }
        if(element instanceof IClassFile)
        {
	        classFile = (IClassFile)element;
	        type = classFile.getType();
	        return type.getFullyQualifiedName('$');
        }
        return null;
    }

    public static String getReturnType(IMethod method)
        throws JavaModelException
    {
        if(method == null)
            return null;
        else
            return getFullName(method.getDeclaringType(), method.getReturnType());
    }

    public static String[] getParameterTypes(IMethod method)
        throws JavaModelException
    {
        if(method == null)
            return null;
        else
            return getReadableParameterTypes(method);
    }

    public static List getAllSuperClasses(IType type)
    {
        List list = new ArrayList();
        for(IType newType = type; newType != null;)
        {
            newType = getSuperClasses(newType);
            if(newType != null && !list.contains(newType))
                list.add(newType);
        }

        return list;
    }

    public static List getAllSuperInterfaces(IType type)
    {
        List list = new ArrayList();
        List superTypeList = getAllSuperClasses(type);
        superTypeList.add(0, type);
        for(int i = 0; i < superTypeList.size(); i++)
        {
            IType classType = (IType)superTypeList.get(i);
            getSuperInterfaces(classType, list, true);
        }

        return list;
    }

    public static IType getSuperClasses(IType type)
    {
        if(type == null)
            return null;
        try{
	        String superClassName;
	        superClassName = type.getSuperclassTypeSignature();
	        superClassName = getFullName(type, superClassName);
	        if(StringUtils.isEmpty(superClassName))
	            return null;
	        IJavaProject project = (IJavaProject)type.getAncestor(2);
	        return project.findType(superClassName);
        }
        catch(Exception e ){
        	
        }
        return null;
    }

    private static void getSuperInterfaces(IType type, List interfaces, boolean recursive)
    {
        try
        {
            String names[] = type.getSuperInterfaceTypeSignatures();
            if(!ArrayUtils.isEmpty(names))
            {
                for(int i = 0; i < names.length; i++)
                {
                    String superClassName = names[i];
                    superClassName = getFullName(type, superClassName);
                    if(!StringUtils.isEmpty(superClassName))
                    {
                        if(superClassName.indexOf('<') > 0)
                            superClassName = StringUtils.substringBefore(superClassName, "<");
                        superClassName = superClassName.replace('$', '.');
                        IJavaProject project = (IJavaProject)type.getAncestor(2);
                        IType newType = project.findType(superClassName);
                        if(newType != null)
                        {
                            if(recursive)
                                getSuperInterfaces(newType, interfaces, recursive);
                            if(!interfaces.contains(newType))
                                interfaces.add(newType);
                        }
                    }
                }

            }
        }
        catch(JavaModelException _ex) { }
    }


    public static String getTypeBindingName(ITypeBinding r_typeBinding)
    {
        Assert.isNotNull(r_typeBinding, "the typeBinding must not be null!");
        ITypeBinding typeBinding = r_typeBinding;
        String array = "";
        String name = "";
        if(typeBinding.isArray())
        {
            typeBinding = typeBinding.getComponentType();
            name = getTypeBindingName(typeBinding);
            array = "[]";
        } else
        {
            name = typeBinding.getErasure().getBinaryName();
            name = getSignature(name);
            ITypeBinding typeArguments[] = typeBinding.getTypeArguments();
            if(typeArguments != null && typeArguments.length > 0)
            {
                name = (new StringBuilder(String.valueOf(name))).append("<").toString();
                for(int i = 0; i < typeArguments.length; i++)
                {
                    ITypeBinding temp = typeArguments[i];
                    name = (new StringBuilder(String.valueOf(name))).append(getTypeBindingName(temp)).append(",").toString();
                }

                name = name.substring(0, name.length() - 1);
                name = (new StringBuilder(String.valueOf(name))).append(">").toString();
            }
        }
        return (new StringBuilder(String.valueOf(name))).append(array).toString();
    }

    public static String[] getReadableParameterTypes(IMethod method)
    {
        String orignalParameterTypess[] = method.getParameterTypes();
        String readableParameterTypess[] = new String[orignalParameterTypess.length];
        for(int i = 0; i < readableParameterTypess.length; i++)
            try
            {
                readableParameterTypess[i] = getFullName(method.getDeclaringType(), orignalParameterTypess[i]);
            }
            catch(JavaModelException _ex) { }

        return readableParameterTypess;
    }

    public static String getFullName(IType type, String signatureParameter)
        throws JavaModelException
    {
        if(signatureParameter == null)
            return null;
        if(signatureParameter.equals(""))
            return "";
        char parameter[] = signatureParameter.toCharArray();
        int count = 0;
        String array = "";
        for(; parameter[count] == '['; count++)
            array = (new StringBuilder(String.valueOf(array))).append("[]").toString();

        int genericIndex = signatureParameter.indexOf('<');
        String fullName = null;
        if(genericIndex > 0)
            fullName = getResolvedTypeName(signatureParameter.substring(0, genericIndex), type);
        else
            fullName = getResolvedTypeName(signatureParameter, type);
        if(genericIndex > 0)
        {
            String genericType = StringUtils.substringBeforeLast(StringUtils.substringAfter(signatureParameter, "<"), ">");
            String multiParam[] = splitSemicolon(genericType);
            String centerStr = "";
            String as[] = multiParam;
            int i = 0;
            for(int j = as.length; i < j; i++)
            {
                String temp = as[i];
                if(temp != null && !temp.trim().equals(""))
                    centerStr = (new StringBuilder(String.valueOf(centerStr))).append(",").append(getFullName(type, (new StringBuilder(String.valueOf(temp))).append(";").toString())).toString();
            }

            centerStr = StringUtils.substringAfter(centerStr, ",");
            fullName = (new StringBuilder(String.valueOf(fullName))).append("<").append(centerStr).append(">").append(array).toString();
        } else
        {
            fullName = (new StringBuilder(String.valueOf(fullName))).append(array).toString();
        }
        return fullName;
    }

    private static String doGetResolvedTypeName(IType declaringType, String name)
        throws JavaModelException
    {
        ICompilationUnit compilationUnit = declaringType.getCompilationUnit();
        if(compilationUnit != null)
        {
            IImportDeclaration imports[] = compilationUnit.getImports();
            for(int i = 0; i < imports.length; i++)
            {
                IImportDeclaration declaration = imports[i];
                String fullImportName = declaration.getElementName();
                String importName = StringUtils.substringAfterLast(fullImportName, ".");
                if(name.equals(importName))
                    return fullImportName;
            }

            String newTypeName = (String)PrimitiveTypes.get(name);
            return newTypeName;
        } else
        {
            return null;
        }
    }

    public static String getResolvedTypeName(String refTypeSig, IType declaringType)
        throws JavaModelException
    {
        int arrayCount = Signature.getArrayCount(refTypeSig);
        String name = gettypeName(refTypeSig, arrayCount);
        char type = refTypeSig.charAt(arrayCount);
        if(type == 'Q')
        {
            String typeName = doGetResolvedTypeName(declaringType, name);
            if(typeName != null)
                return typeName;
            String resolvedNames[][] = declaringType.resolveType(name);
            if(!ArrayUtils.isEmpty(resolvedNames))
            {
                String typeNames = resolvedNames[0][1].replace('.', '$');
                typeNames = concatenateName(resolvedNames[0][0], typeNames);
                return typeNames;
            }
            ITypeParameter typeParameters[] = declaringType.getTypeParameters();
            for(int i = 0; i < typeParameters.length; i++)
                if(name.equals(typeParameters[i].getElementName()))
                {
                    String bounds[] = typeParameters[i].getBounds();
                    if(bounds != null && bounds.length > 0)
                    {
                        typeName = doGetResolvedTypeName(declaringType, name);
                        if(typeName != null)
                            return typeName;
                        String paramResovedNames[][] = declaringType.resolveType(bounds[0]);
                        if(paramResovedNames != null && paramResovedNames.length > 0)
                        {
                            String typeNames = paramResovedNames[0][1].replace('.', '$');
                            return concatenateName(paramResovedNames[0][0], typeNames);
                        }
                    } else
                    {
                        return Object.class.getName();
                    }
                }

            return null;
        }
        if(type == 'T')
        {
            ITypeParameter typeParameters[] = declaringType.getTypeParameters();
            for(int i = 0; i < typeParameters.length; i++)
                if(name.equals(typeParameters[i].getElementName()))
                {
                    String bounds[] = typeParameters[i].getBounds();
                    if(bounds != null && bounds.length == 1)
                        return getSignature(bounds[0]);
                    else
                        return Object.class.getName();
                }

            return null;
        } else
        {
            return getSignature(refTypeSig.substring(arrayCount));
        }
    }

    static String gettypeName(String refTypeSig, int arrayCount)
    {
        String name = "";
        int bracket = refTypeSig.indexOf('<', arrayCount + 1);
        if(bracket > 0)
        {
            name = refTypeSig.substring(arrayCount + 1, bracket);
        } else
        {
            int semi = refTypeSig.indexOf(';', arrayCount + 1);
            if(semi == -1)
                if(refTypeSig.length() > arrayCount + 1)
                    return refTypeSig.substring(arrayCount + 1, refTypeSig.length());
                else
                    return refTypeSig;
            name = refTypeSig.substring(arrayCount + 1, semi);
        }
        return name;
    }

    public static String getSignature(String name)
    {
        String jdtSignature = name;
        try
        {
            jdtSignature = Signature.toString(name);
        }
        catch(Exception _ex)
        {
            try
            {
                jdtSignature = Signature.toString((new StringBuilder(String.valueOf(name))).append(";").toString());
            }
            catch(Exception _ex2)
            {
                return name;
            }
        }
        int dolCount = 0;
        if(name.indexOf('$') > 1 && name.indexOf('$') < name.length())
        {
            for(int i = 0; i < name.length(); i++)
                if(i > 0 && i < name.length() - 1 && name.charAt(i) == '$')
                    dolCount++;

        }
        for(int i = jdtSignature.length() - 1; i >= 0; i--)
        {
            if(dolCount <= 0)
                break;
            if(jdtSignature.charAt(i) == '.')
            {
                jdtSignature = (new StringBuilder(String.valueOf(jdtSignature.substring(0, i)))).append("$").append(jdtSignature.substring(i + 1, jdtSignature.length())).toString();
                dolCount--;
            }
        }

        return jdtSignature;
//        return name;
    }

    public static String concatenateName(String name1, String name2)
    {
        StringBuffer buf = new StringBuffer();
        if(name1 != null && name1.length() > 0)
            buf.append(name1);
        if(name2 != null && name2.length() > 0)
        {
            if(buf.length() > 0)
                buf.append('.');
            buf.append(name2);
        }
        return buf.toString();
    }

    public static String[] splitSemicolon(String str)
    {
        List string = new ArrayList();
        char chars[] = str.toCharArray();
        int strIndex = 0;
        int count = 0;
        boolean colseBracket = false;
        int countBracket = 0;
        for(int i = 0; i < chars.length; i++)
            if(colseBracket || chars[i] == '<')
            {
                colseBracket = true;
                if(chars[i] == '<')
                    countBracket++;
                else
                if(chars[i] == '>')
                    countBracket--;
                if(countBracket == 0)
                    colseBracket = false;
            } else
            if(chars[i] == ';')
            {
                string.add(str.substring(count, i));
                count = i + 1;
                strIndex++;
            }

        if(count < chars.length - 1)
            string.add(str.substring(count));
        return (String[])string.toArray(new String[string.size()]);
    }

    public static String getReadableParameterType(IMethod method, String originalName)
    {
        String fullName = originalName;
        try
        {
            IType iType = method.getDeclaringType();
            fullName = getFullName(iType, originalName);
        }
        catch(JavaModelException _ex) { }
        return fullName;
    }

	public static IMethod getMethod(ICompilationUnit cu, String selector, String[] parameterTypeSignatures) throws JavaModelException {
		if(cu == null)
			return null;
		IType[] types = cu.getTypes();
		SourceType sm = null;
		for(int i=0; i<types.length; i++)
			if(types[i] instanceof SourceType)
				sm = (SourceType)types[i];
		
		IMethod m = null;
		
		if(parameterTypeSignatures == null)
		{
			IMethod[] ms = sm.getMethods();
			for(int i=0; i<ms.length; i++)
			{
				if(selector.equals(ms[i].getElementName()))
						return ms[i];
			}
		}
		else
			m = sm.getMethod(selector, parameterTypeSignatures);
		return m;
	}
	
	public static IMethod getMethod(IType cu, String selector, String[] parameterTypeSignatures) throws JavaModelException {
		if(cu == null)
			return null;
		
		IMethod m = null;
		if(parameterTypeSignatures == null)
		{
			IMethod[] ms = cu.getMethods();
			for(int i=0; i<ms.length; i++)
			{
				if(selector.equals(ms[i].getElementName()))
						return ms[i];
			}
		}
		else
			m = cu.getMethod(selector, parameterTypeSignatures);
		return m;
	}

	public static IMethod[] getAllPublicMethods(IType cu) throws JavaModelException {

		IMethod[] ms = cu.getMethods();
		List methods = new ArrayList();
		Class theInterfaceClass = null;
		ProjectClassLoader projectClassLoader = null;
		try {
			projectClassLoader = new ProjectClassLoader(
					ProjectFinder.getCurrentJavaProject());
			theInterfaceClass = projectClassLoader.loadClass(cu.getFullyQualifiedName());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e.getMessage());
		} catch (CoreException e) {
			throw new RuntimeException(e.getMessage());
		}
		for(int i=0; i<ms.length; i++)
		{	//�����public�Ļ��ߵ�ǰ������ǽӿ�
			if(Flags.isPublic(ms[i].getFlags())||theInterfaceClass.isInterface()){
				if(!ms[i].isConstructor()){
					methods.add(ms[i]);
				}
			}
		}
		return (IMethod[]) methods.toArray(new IMethod[methods.size()]);
	}
	
	public static IMethod[] getAllStaticMethods(IType cu) throws JavaModelException {

		IMethod[] ms = cu.getMethods();
		List methods = new ArrayList();
		for(int i=0; i<ms.length; i++)
		{
			if(Flags.isPublic(ms[i].getFlags()) && Flags.isStatic(ms[i].getFlags())){
				
				if(!ms[i].isConstructor()){
					methods.add(ms[i]);
				}
			}
		}
		return (IMethod[]) methods.toArray(new IMethod[methods.size()]);
	}

	public static IField[] getAllPublicFields(ICompilationUnit cu, IType type) throws JavaModelException {

		IField[] ms = type.getFields();
		List methods = new ArrayList();
		for(int i=0; i<ms.length; i++)
		{
//			if(Flags.isPublic(ms[i].getFlags()))
			String fieldName = ms[i].getElementName();
			fieldName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
			if(getMethod(cu, fieldName, null) != null)
			{
				methods.add(ms[i]);
			}
		}
		return (IField[]) methods.toArray(new IField[methods.size()]);
	}

	public static IMethod[] getAllPublicMethods(ICompilationUnit cu) throws JavaModelException {
		IType[] types = cu.getTypes();
		IType sm = null;
		for(int i=0; i<types.length; i++)
			if(types[i] instanceof SourceType)
				sm = (IType)types[i];
		if(sm == null)
			sm = types[0];
		return getAllPublicMethods(sm);
	}

	public static Method[] getAllPublicMethods(Class cu) throws JavaModelException {
		Method[] ms = cu.getMethods();
		List methods = new ArrayList();
		for(int i=0; i<ms.length; i++)
		{
			if((ms[i].getModifiers()|Method.PUBLIC) >0)
			{
				methods.add(ms[i]);
			}
		}
		return (Method[]) methods.toArray(new IMethod[methods.size()]);
	}

	public static IField[] getAllPublicFields(ICompilationUnit cu) throws JavaModelException {
		IType[] types = cu.getTypes();
		IType sm = null;
		for(int i=0; i<types.length; i++)
			if(types[i] instanceof SourceType)
				sm = (IType)types[i];
		if(sm == null)
			sm = types[0];
		return getAllPublicFields(cu, sm);
	}
	
	

	public static String[] getMethodParamNames(IMethod method) throws JavaModelException {
		Assert.isNotNull(method, "the method must not be null!");
		if (method.getNumberOfParameters() < 1)
			return new String[0];
		if (!method.isBinary())
			return method.getRawParameterNames();
		IClassFileReader classFileReader = ToolFactory.createDefaultClassFileReader(method.getClassFile(), 65535);
		IMethodInfo methodInfos[] = classFileReader.getMethodInfos();
		if (methodInfos == null)
			return method.getParameterNames();
		for (int i = 0; i < methodInfos.length; i++) {
			IMethodInfo info = methodInfos[i];
			if (methodEqual(method, info)) {
				char descriptor[] = info.getDescriptor();
				ICodeAttribute codeAttribute = info.getCodeAttribute();
				int accessFlags = info.getAccessFlags();
				char paramNames[][] = getParameterNames(descriptor, codeAttribute, accessFlags);
				String names[] = new String[paramNames.length];
				for (int j = 0; j < paramNames.length; j++) {
					char cs[] = paramNames[j];
					names[j] = new String(cs);
				}
				return names;
			}
		}

		return method.getRawParameterNames();
	}

	private static boolean methodEqual(IMethod method, IMethodInfo methodInfo) {
		String infoName = new String(methodInfo.getName());
		String name = method.getElementName();
		boolean equal = false;
		if (infoName.equals(name)) {
			int number = method.getNumberOfParameters();
			char descriptor[] = methodInfo.getDescriptor();
			int len = Signature.getParameterCount(descriptor);
			if (number == len) {
				String types[] = method.getParameterTypes();
				char infoTypes[][] = Signature.getParameterTypes(descriptor);
				for (int i = 0; i < infoTypes.length; i++) {
					char cs[] = infoTypes[i];
					if (types[i].equals((new String(cs)).replace('/', '.'))) {
						equal = true;
						continue;
					}
					equal = false;
					break;
				}

			}
		}
		return equal;
	}

	private static char[][] getParameterNames(char methodDescriptor[], ICodeAttribute codeAttribute, int accessFlags) {
		int paramCount = Signature.getParameterCount(methodDescriptor);
		char parameterNames[][] = new char[paramCount][];
		if (codeAttribute != null) {
			ILocalVariableAttribute localVariableAttribute = codeAttribute.getLocalVariableAttribute();
			if (localVariableAttribute != null) {
				ILocalVariableTableEntry entries[] = localVariableAttribute.getLocalVariableTable();
				int startingIndex = (accessFlags & 8) == 0 ? 1 : 0;
				for (int i = 0; i < paramCount; i++) {
					ILocalVariableTableEntry searchedEntry = getEntryFor(getLocalIndex(startingIndex, i, methodDescriptor), entries);
					if (searchedEntry != null)
						parameterNames[i] = searchedEntry.getName();
					else
						parameterNames[i] = CharOperation.concat("arg".toCharArray(), Integer.toString(i).toCharArray());
				}

			} else {
				for (int i = 0; i < paramCount; i++)
					parameterNames[i] = CharOperation.concat("arg".toCharArray(), Integer.toString(i).toCharArray());

			}
		} else {
			for (int i = 0; i < paramCount; i++)
				parameterNames[i] = CharOperation.concat("arg".toCharArray(), Integer.toString(i).toCharArray());

		}
		return parameterNames;
	}

	private static int getLocalIndex(int startingSlot, int index, char methodDescriptor[]) {
		int slot = startingSlot;
		char types[][] = Signature.getParameterTypes(methodDescriptor);
		for (int i = 0; i < index; i++) {
			char type[] = types[i];
			switch (type.length) {
			case 1: // '\001'
				switch (type[0]) {
				case 68: // 'D'
				case 74: // 'J'
					slot += 2;
					break;

				default:
					slot++;
					break;
				}
				break;

			default:
				slot++;
				break;
			}
		}

		return slot;
	}

	private static ILocalVariableTableEntry getEntryFor(int index, ILocalVariableTableEntry entries[]) {
		int i = 0;
		for (int max = entries.length; i < max; i++) {
			ILocalVariableTableEntry entry = entries[i];
			if (index == entry.getIndex())
				return entry;
		}

		return null;
	}

	private static final String ARG = "arg";
    private static final Map PrimitiveTypes = new HashMap();

    static 
    {
        loadTypes();
    }
}