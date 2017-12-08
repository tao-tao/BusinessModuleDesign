package avicit.ui.runtime.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.PrimitiveType.Code;
import org.eclipse.jdt.internal.corext.dom.ASTNodeFactory;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.TextEdit;

/**
 * <p>
 * <b>���봴��������</b>
 * </p>
 */
public class JavaCodeEngine {
	private static Map all = new HashMap();
	
	private WorkingCopyProvider wp = null;
	private ICompilationUnit iUnit = null;
	private CompilationUnit astRoot = null;
	private ASTParser parser = null;
	private TypeDeclaration sourceContainer = null;
	private AST ast = null;
	private DWTSourceVisitor visitor = null;

	public JavaCodeEngine(IFile input) {
		try {
			initialize(input);
		} catch (JavaModelException e) {
		}
	}

	public static JavaCodeEngine newInstance(IFile input){
		JavaCodeEngine engine = (JavaCodeEngine) all.get(input);
		if(engine == null)
		{
			engine = new JavaCodeEngine(input);
			all.put(input, engine);
		}
		return engine;
	}
	
	public static void closeAll()
	{
		Iterator itor = all.values().iterator();
		while(itor.hasNext())
		{
			JavaCodeEngine engine = (JavaCodeEngine) itor.next();
			engine.close();
		}
	}

	/**
	 * @brief ��ʼ��
	 * 
	 * @param input
	 * @throws JavaModelException
	 */
	public void initialize(IFile input) throws JavaModelException {
		if(wp == null)
		{
			if(input != null)
				wp = new WorkingCopyProvider(input);
		}
		
		iUnit = wp.getWorkingCopy(true);
		if(parser == null)
			parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(iUnit.getSource().toCharArray());
		astRoot = (CompilationUnit) parser.createAST(null);
		astRoot.recordModifications();
		if(astRoot.types().size() >0)
			sourceContainer = (TypeDeclaration) astRoot.types().get(0);
		
		ast = astRoot.getAST();
		visitor = new DWTSourceVisitor();
	}
	
	private void refresh(){
		try {
			parser.setSource(iUnit.getSource().toCharArray());astRoot = (CompilationUnit) parser.createAST(null);
			astRoot.recordModifications();
			if(astRoot.types().size() >0)
			{
				sourceContainer = (TypeDeclaration) astRoot.types().get(0);
				ast = astRoot.getAST();
			}
		} catch (JavaModelException e) {
		}
		
	}

	/**
	 * 
	 * @brief ��������д��༭��
	 * 
	 * @throws BadLocationException
	 * @throws JavaModelException
	 */
	public void save() throws BadLocationException, CoreException {
		astRoot.rewrite(wp.getDocument(), iUnit.getJavaProject().getOptions(true));
		TextEdit edits = astRoot.rewrite(wp.getDocument(), null);
		edits.apply(wp.getDocument());
		wp.getDocumentProvider().saveDocument(null, wp.getEditor(), wp.getDocument(), true);
		String newSource = wp.getDocument().get();
		iUnit.getBuffer().setContents(newSource);
		iUnit.save(null, true);
//		try {
//			initialize(null);
//		} catch (InitializeException e) {
//			e.printStackTrace();
//		}
	}
	
	
	public void closeAndOpen(){
		close();
		try {
			initialize(null);
		} catch (JavaModelException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 
	 * @brief �ر��ĵ����ұ���
	 * 
	 */
	public void close(){
		wp.dispose();
	}
	
	public boolean isClose(){
		return wp.isFdisconnected();
	}
	
	/**
	 * 
	 * @brief �жϷ����Ƿ����
	 * 
	 * @param methodName
	 * @return
	 */
	public MethodDeclaration getMethodDeclare(String methodName) {
		
		MethodDeclaration[] methods = sourceContainer.getMethods();
		for(int i=0; i< methods.length; i++)
		{
			if(methods[i].getName().getIdentifier().equals(methodName))
				return methods[i];
		}
		return null;
	}
	
	public MethodDeclaration[] getAllMethodDeclare() {
		refresh();
		return sourceContainer.getMethods();
	}
	
	public Map getAllClassFields(int modifiers) {
		
		Map clsFields = new HashMap();
		FieldDeclaration[] fields = sourceContainer.getFields();
		for(int i=0; i< fields.length; i++)
		{
			if((fields[i].getModifiers()&modifiers) !=0)
			{
				JavaClassField clsField = new JavaClassField();
				clsField.field = fields[i];
				//clsField.getter
				String fieldName = ((VariableDeclarationFragment)fields[i].fragments().get(0)).getName().getIdentifier();
				if (fieldName.equalsIgnoreCase("serialVersionUID")) continue;
				if (fieldName.equalsIgnoreCase("hashCode")) continue;
				String methodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
				clsField.getter = getMethodDeclare(methodName);
				methodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
				clsField.setter = getMethodDeclare(methodName);				
				clsFields.put(fieldName, clsField);				
			}
		}
		return clsFields;
	}

	private Type getProperType(String typeStr)
	{
		Code type = PrimitiveType.toCode(typeStr);
		if(type == null)
			return ast.newSimpleType(ast.newName(typeStr));
		else
			return ast.newPrimitiveType(type);
	}
	
	/**
	 * 
	 * @brief ����һ���ֶ�
	 * 
	 * @param fieldName
	 *            �ֶ���
	 * @param fieldType
	 *            �ֶ�����
	 * @param modifiers
	 *            Modifier.PUBLIC;
	 * 
	 * @return
	 */
	public FieldDeclaration addField(String fieldName, String fieldType, int modifiers, int getSetter) {
		VariableDeclarationFragment vdf = ast.newVariableDeclarationFragment();
		vdf.setName(ast.newSimpleName(fieldName));
		FieldDeclaration fd = ast.newFieldDeclaration(vdf);
		fd.setType(getProperType(fieldType));
		
		fd.modifiers().addAll(ast.newModifiers(modifiers));
		if (fd != null) {
			sourceContainer.bodyDeclarations().add(fd);
		}
		if((getSetter & 1) >0)
		{
			String methodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
			MethodDeclaration md = this.generateMethod(methodName, Modifier.PUBLIC, fieldType);
			Block methodBlock = ast.newBlock();
			md.setBody(methodBlock);
			ReturnStatement rs = ast.newReturnStatement();
			FieldAccess fa = ast.newFieldAccess();
			fa.setExpression(ast.newThisExpression());
			fa.setName(ast.newSimpleName(fieldName));
			rs.setExpression(fa);
			methodBlock.statements().add(rs);
		}
		if((getSetter & 2) >0)
		{
			String methodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
			MethodDeclaration md = this.generateMethod(methodName, Modifier.PUBLIC, "void");
			SingleVariableDeclaration variableDeclaration = ast.newSingleVariableDeclaration();
			variableDeclaration.setType(getProperType(fieldType));
			variableDeclaration.setName(ast.newSimpleName("via"));
			md.parameters().add(variableDeclaration);
			Block methodBlock = ast.newBlock();
			md.setBody(methodBlock);
			Assignment a = ast.newAssignment();
			a.setOperator(Assignment.Operator.ASSIGN);
			methodBlock.statements().add(ast.newExpressionStatement(a)); 
			
			FieldAccess fa = ast.newFieldAccess();
			fa.setExpression(ast.newThisExpression());
			fa.setName(ast.newSimpleName(fieldName));
			a.setLeftHandSide(fa);
			a.setRightHandSide(ast.newSimpleName("via"));
		}
		return fd;
	}

	public FieldDeclaration modifyField(String oldFieldName, String newFieldName, String fieldType, int modifiers, int getSetter) {
		FieldDeclaration[] fieldList = sourceContainer.getFields();
		if (fieldList != null && fieldList.length > 0) {
			int index = -1;
			for (int i = 0; i < fieldList.length; i++) {
				VariableDeclarationFragment vdf = (VariableDeclarationFragment)fieldList[i].fragments().get(0);
				String name = vdf.getName().getIdentifier();
				if(name.equals(oldFieldName))
				{
					index = i;
					break;
				}
//				visitor.setFieldVarName(oldFieldName);
//				sourceContainer.accept(visitor);
//				if (visitor.getFieldDeclare() != null
//						&& fieldList[i] == visitor.getFieldDeclare()) {
//					index = i;
//					break;
//				}
			}
			if (index != -1) {
				FieldDeclaration fd = (sourceContainer.getFields()[index]);
				VariableDeclarationFragment vdf = ast.newVariableDeclarationFragment();
				vdf.setName(ast.newSimpleName(newFieldName));
				fd.setType(getProperType(fieldType));
				fd.fragments().remove(0);
				fd.fragments().add(0, vdf);
				fd.modifiers().clear();
				fd.modifiers().addAll(ast.newModifiers(modifiers));
				
				String methodName = "get" + Character.toUpperCase(oldFieldName.charAt(0)) + oldFieldName.substring(1);
				MethodDeclaration[] methods = sourceContainer.getMethods();
				if (methods != null && methods.length > 0) {
					MethodDeclaration element = null;
					for (int i = 0; i < methods.length; i++) {
						if (methods[i].getName().getIdentifier().equals(methodName)) {
							element = methods[i];
						}
					}
					if (element != null) {
						element.delete();
					}
				}
				methodName = "set" + Character.toUpperCase(oldFieldName.charAt(0)) + oldFieldName.substring(1);
				if (methods != null && methods.length > 0) {
					MethodDeclaration element = null;
					for (int i = 0; i < methods.length; i++) {
						if (methods[i].getName().getIdentifier().equals(methodName)) {
							element = methods[i];
						}
					}
					if (element != null) {
						element.delete();
					}
				}
				if((getSetter & 1) >0)
				{					
					methodName = "get" + Character.toUpperCase(newFieldName.charAt(0)) + newFieldName.substring(1);					
					MethodDeclaration md = this.generateMethod(methodName, Modifier.PUBLIC, fieldType);
					Block methodBlock = ast.newBlock();
					md.setBody(methodBlock);
					ReturnStatement rs = ast.newReturnStatement();
					FieldAccess fa = ast.newFieldAccess();
					fa.setExpression(ast.newThisExpression());
					fa.setName(ast.newSimpleName(newFieldName));
					rs.setExpression(fa);
					methodBlock.statements().add(rs);
				}
				if((getSetter & 2) >0)
				{
					methodName = "set" + Character.toUpperCase(newFieldName.charAt(0)) + newFieldName.substring(1);					
					MethodDeclaration md = this.generateMethod(methodName, Modifier.PUBLIC, "void");
					SingleVariableDeclaration variableDeclaration = ast.newSingleVariableDeclaration();
					variableDeclaration.setType(getProperType(fieldType));
					variableDeclaration.setName(ast.newSimpleName("via"));
					md.parameters().add(variableDeclaration);
					Block methodBlock = ast.newBlock();
					md.setBody(methodBlock);
					Assignment a = ast.newAssignment();
					a.setOperator(Assignment.Operator.ASSIGN);
					methodBlock.statements().add(ast.newExpressionStatement(a)); 
					
					FieldAccess fa = ast.newFieldAccess();
					fa.setExpression(ast.newThisExpression());
					fa.setName(ast.newSimpleName(newFieldName));
					a.setLeftHandSide(fa);
					a.setRightHandSide(ast.newSimpleName("via"));
				}
				
				return fd;
			}
		}
		return null;
	}
	
	public void deleteField(String fieldVarName) {
		FieldDeclaration[] fieldList = sourceContainer.getFields();
		if (fieldList != null && fieldList.length > 0) {
			int index = -1;
			for (int i = 0; i < fieldList.length; i++) {
				visitor.setFieldVarName(fieldVarName);
				sourceContainer.accept(visitor);
				if (visitor.getFieldDeclare() != null
						&& fieldList[i] == visitor.getFieldDeclare()) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				(sourceContainer.getFields()[index]).delete();				
			}
		}
		String methodName = "get" + Character.toUpperCase(fieldVarName.charAt(0)) + fieldVarName.substring(1);
		MethodDeclaration[] methods = sourceContainer.getMethods();
		if (methods != null && methods.length > 0) {
			MethodDeclaration element = null;
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().getIdentifier().equals(methodName)) {
					element = methods[i];
				}
			}
			if (element != null) {
				element.delete();
			}
		}
		methodName = "set" + Character.toUpperCase(fieldVarName.charAt(0)) + fieldVarName.substring(1);
		if (methods != null && methods.length > 0) {
			MethodDeclaration element = null;
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().getIdentifier().equals(methodName)) {
					element = methods[i];
				}
			}
			if (element != null) {
				element.delete();
			}
		}
	}
	
	/**
	 * 
	 * @brief ����һ���ֶ�
	 * 
	 * @param fieldName
	 *            �ֶ���
	 * @param fieldType
	 *            �ֶ�����
	 * @param modifiers
	 *            Modifier.PUBLIC;
	 * 
	 * @return
	 */
	public FieldDeclaration generateField(String fieldName, String fieldType, int modifiers) {
		// ����һ���ֶ�
		VariableDeclarationFragment vdf = ast.newVariableDeclarationFragment();
		vdf.setName(ast.newSimpleName(fieldName));
		FieldDeclaration fd = ast.newFieldDeclaration(vdf);
		Code type = PrimitiveType.toCode(fieldType);
		if(type == null)
			fd.setType(ast.newSimpleType(ast.newName(fieldType)));
		else
			fd.setType(ast.newPrimitiveType(type));
		
		fd.modifiers().addAll(ast.newModifiers(modifiers));
		if (fd != null) {
			sourceContainer.bodyDeclarations().add(fd);
		}
		return fd;
	}
	
	/**
	 * 
	 * @brief ��ɷ����е�һ������
	 * 
	 * @param parent
	 *            �ô������������
	 * @return
	 */
	public Block generateBlock(MethodDeclaration parent) {
		Block block = ast.newBlock();
		parent.setBody(block);
		return block;
	}

	/**
	 * 
	 * @brief ��ɹ��캯��ķ���
	 * 
	 * @param methodName
	 *            ���캯�������
	 * @param modiferList
	 *            �ؼ��� ����:List publicModifier = ASTNodeFactory.newModifiers(ast,
	 *            Modifier.PUBLIC);
	 * @param argList
	 *            �����б�
	 * @return
	 */
	public MethodDeclaration generateConstructMethod(String methodName, int modifers) {
		return generateMethod(methodName, modifers, null);
	}

	/**
	 * 
	 * @brief ��ɺ���
	 * 
	 * @param methodName
	 *            ��ɵĺ�����
	 * @param modifierList
	 *            �ؼ��� ����:List publicModifier = ASTNodeFactory.newModifiers(ast,
	 *            Modifier.PUBLIC);
	 * @param typeCode
	 *            ����ֵ�Ĺؼ��� ����: Code typeCode = PrimitiveType.VOID;
	 * @param argList
	 * @return
	 */
	public MethodDeclaration generateMethod(String methodName, int modifiers, String type) {
		// ��ӷ������ļ�
		MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
		methodDeclaration.modifiers().addAll(ast.newModifiers(modifiers));
		methodDeclaration.setName(ast.newSimpleName(methodName));
		Code typeCode = PrimitiveType.toCode(type);
		
		if (typeCode != null) {
			methodDeclaration.setConstructor(false);
			methodDeclaration.setReturnType2(ast.newPrimitiveType(typeCode));
		} else if(type != null){
			methodDeclaration.setConstructor(false);
			methodDeclaration.setReturnType2(ast.newSimpleType(ast.newName(type)));
		} else {
			methodDeclaration.setConstructor(false);
		}
		
		if (methodDeclaration != null) {
			sourceContainer.bodyDeclarations().add(methodDeclaration);
		}
		return methodDeclaration;
	}

	/**
	 * 
	 * @brief ��ɵ���������
	 * 
	 * @param classType
	 *            ����İ��е�����
	 * @param classType
	 *            ����İ��ǰ׺����
	 * @return
	 */
	public ImportDeclaration generateImport(final String[] packageNames,
			String classType) {
		// ��Ӱ�ĵ���
		ImportDeclaration importDeclaration = ast.newImportDeclaration();
		Name name = ast.newSimpleName(packageNames[0]);
		for (int i = 1; i < packageNames.length; i++) {
			name = ast.newQualifiedName(name, ast
					.newSimpleName(packageNames[i]));
		}
		QualifiedName name_2 = ast.newQualifiedName(name, ast
				.newSimpleName(classType));
		importDeclaration.setName(name_2);
		importDeclaration.setOnDemand(false);
		if (importDeclaration != null) {
			astRoot.imports().add(importDeclaration);
		}
		return importDeclaration;
	}

	/**
	 * 
	 * @brief ��ɹؼ���
	 * 
	 * @param modifier
	 *            �ؼ������� ����: Modifier.PUBLIC
	 * @return
	 */
	public List generateModifier(int modifier) {
		return ASTNodeFactory.newModifiers(ast, modifier);
	}

	/**
	 * 
	 * @brief ��ɹ��캯��������
	 * 
	 * @param parent
	 *            ������һ�����
	 * @param expressionList
	 *            �����б�
	 * @return
	 */
	public Statement generateSuperMethodInvocationStatement(Block parent,
			Expression[] expressionList) {
		SuperConstructorInvocation stmt = ast.newSuperConstructorInvocation();
		for (int i = 0; i < expressionList.length; i++) {
			Expression expression = expressionList[i];
			stmt.arguments().add(expression);
		}
		if (parent != null) {
			parent.statements().add(stmt);
		}
		return stmt;
	}

	/**
	 * 
	 * @brief ���һ�����ñ���������Statement
	 * 
	 * @param parent
	 *            ��ɵ��������Ŀ������
	 * @param Expression
	 * @return
	 */
	public Statement generateMethodinvokeStatement(Block parent,
			Expression methodInvocation) {
		ExpressionStatement expressionStatement = ast
				.newExpressionStatement(methodInvocation);
		if (parent != null) {
			parent.statements().add(expressionStatement);
		}
		return expressionStatement;
	}

	/**
	 * 
	 * @brief ����ֶ�ʵ�������
	 * 
	 * @param parent
	 *            ���������Ŀ�
	 * @param fieldName
	 *            �ֶ���
	 * @param fieldType
	 *            �ֶ�����
	 * @param argExpressionList
	 *            ʵ��Ķ��󶼹��캯��Ĳ���
	 * @return
	 */
	public Statement generateMethodinvokeAssignmentStatement(Block parent,
			String fieldName, String fieldType, Expression[] argExpressionList) {
		Assignment a = ast.newAssignment();
		a.setOperator(Assignment.Operator.ASSIGN);
		SimpleName varName = ast.newSimpleName(fieldName);
		a.setLeftHandSide(varName);
		ClassInstanceCreation cc = ast.newClassInstanceCreation();
		cc.setType(ast.newSimpleType(ast.newSimpleName(fieldType)));
		if (argExpressionList != null) {
			for (int i = 0; i < argExpressionList.length; i++) {
				cc.arguments().add(argExpressionList[i]);
			}
		}
		a.setRightHandSide(cc);
		Statement statment = ast.newExpressionStatement(a);
		if (parent != null) {
			parent.statements().add(statment);
		}
		return statment;
	}

	public Statement generateAssignmentStatement(Block parent, String leftType,
			String varName, String rightType, Expression[] argExpressionList) {
		Assignment a = ast.newAssignment();
		a.setOperator(Assignment.Operator.ASSIGN);
		// ����һ���ֶ�
		VariableDeclarationFragment vdf = ast.newVariableDeclarationFragment();
		vdf.setName(ast.newSimpleName(varName));
		VariableDeclarationExpression v = ast
				.newVariableDeclarationExpression(vdf);
		v.setType(ast.newSimpleType(ast.newSimpleName(leftType)));
		a.setLeftHandSide(v);
		ClassInstanceCreation cc = ast.newClassInstanceCreation();
		cc.setType(ast.newSimpleType(ast.newSimpleName(rightType)));
		if (argExpressionList != null) {
			for (int i = 0; i < argExpressionList.length; i++) {
				cc.arguments().add(argExpressionList[i]);
			}
		}
		a.setRightHandSide(cc);
		Statement statment = ast.newExpressionStatement(a);
		if (parent != null) {
			parent.statements().add(statment);
		}
		return statment;
	}

	/**
	 * 
	 * @brief ���ע�ʹ���
	 * 
	 * @param bodyDeclare
	 *            ע�ʹ���ŵ�λ��
	 * @param commentContent
	 *            ע������
	 * @param author
	 *            ����(�����пո�)
	 * @return
	 */
	public Javadoc generateComment(BodyDeclaration bodyDeclare,
			String commentContent, String author) {
		Javadoc jc = ast.newJavadoc();
		TagElement tag = ast.newTagElement();
		TextElement te = ast.newTextElement();
		tag.fragments().add(te);
		te.setText(commentContent);
		jc.tags().add(tag);
		tag = ast.newTagElement();
		if (author != null && !"".equals(author)) {
			tag.setTagName(TagElement.TAG_AUTHOR);
			tag.fragments().add(ast.newSimpleName(author));
			jc.tags().add(tag);
		}
		bodyDeclare.setJavadoc(jc);
		return jc;
	}

	/**
	 * 
	 * @brief ���һ�������������õĴ���
	 * 
	 * @param fieldName
	 *            ������
	 * @param fieldMethodname
	 *            �������õķ�����
	 * @param argExpressionList
	 *            �����������õĲ����б�
	 * @return
	 */
	public MethodInvocation generateMethodInvocation(String fieldName,
			String fieldMethodname, Expression[] argExpressionList) {
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		SimpleName varName = ast.newSimpleName(fieldName);
		methodInvocation.setExpression(varName);
		methodInvocation.setName(ast.newSimpleName(fieldMethodname));
		if (argExpressionList != null) {
			for (int i = 0; i < argExpressionList.length; i++) {
				Expression array_element = argExpressionList[i];
				methodInvocation.arguments().add(array_element);
			}
		}
		return methodInvocation;
	}

	/**
	 * 
	 * @brief ���this��������
	 * 
	 * @param methodName
	 *            ������
	 * @param argExpressionList
	 *            this�������õĲ����б�
	 * @return
	 */
	public MethodInvocation generateMethodInvocation(String methodName,
			Expression[] argExpressionList) {
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		SimpleName varName = ast.newSimpleName(methodName);
		methodInvocation.setName(varName);
		if (argExpressionList != null) {
			for (int i = 0; i < argExpressionList.length; i++) {
				Expression array_element = argExpressionList[i];
				methodInvocation.arguments().add(array_element);
			}
		}
		return methodInvocation;
	}

	/**
	 * 
	 * @brief ��ɼ򵥵ķ��ַ����
	 * 
	 * @param argName
	 * @return
	 */
	public Expression generateSimplename(String argName) {
		SimpleName name = ast.newSimpleName(argName);
		return name;
	}

	/**
	 * 
	 * @brief ����ɵ��÷���������ַ�������
	 * 
	 * @param argName
	 * @return
	 */
	public Expression generateArgsString(String argName) {
		StringLiteral literal = ast.newStringLiteral();
		literal.setLiteralValue(argName);
		return literal;
	}

	/**
	 * 
	 * @brief ����ɵ��÷��������м������ֲ���
	 * 
	 * @param argNumber
	 * @return
	 */
	public Expression generateNumberExpression(String argNumber) {
		NumberLiteral number = ast.newNumberLiteral();
		number.setToken(argNumber);
		return number;
	}

	/**
	 * 
	 * @brief ������������еĲ���
	 * 
	 * @param methodDeclare
	 *            �����ķ���
	 * @param argType
	 *            ��������
	 * @param argName
	 *            ��������
	 * @return
	 */
	public SingleVariableDeclaration generateMethodArgs(
			MethodDeclaration methodDeclare, String argType, String argName) {
		SingleVariableDeclaration variableDeclaration = ast
				.newSingleVariableDeclaration();
		variableDeclaration.setType(ast.newSimpleType(ast
				.newSimpleName(argType)));
		variableDeclaration.setName(ast.newSimpleName(argName));
		methodDeclare.parameters().add(variableDeclaration);
		return variableDeclaration;
	}

	/**
	 * 
	 * @brief ��ɿղ���Ĵ���
	 * 
	 * @return
	 */
	public Expression generateNULLArgs() {
		NullLiteral nullExp = ast.newNullLiteral();
		return nullExp;
	}

	/** ************************************************�жϺ�ɾ���ļ�������********************************************************** */
	/**
	 * 
	 * @brief �жϵ���İ��Ƿ����
	 * 
	 * @param importDeclare
	 * @return
	 */
	public boolean importDeclareExists(String importName) {
		List importList = astRoot.imports();
		if (importList != null && importList.size() > 0) {
			for (Iterator iter = importList.iterator(); iter.hasNext();) {
				ImportDeclaration element = (ImportDeclaration) iter.next();
				QualifiedName name = (QualifiedName) element.getName();
				SimpleName simpleName = name.getName();
				if (simpleName.getIdentifier().equals(importName)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @brief �жϷ����Ƿ����
	 * 
	 * @param methodName
	 * @return
	 */
	public boolean methodDeclareExists(String methodName) {
		refresh();
		visitor.setMethodName(methodName);
		sourceContainer.accept(visitor);
		if (visitor.existsMethod()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @brief �ж��ֶ������Ƿ����
	 * 
	 * @param fieldType
	 * @return
	 */
	public boolean fieldDeclareExists(String fieldVarName) {
		refresh();
		visitor.setFieldVarName(fieldVarName);
		sourceContainer.accept(visitor);
		if (visitor.existsFieldVarName()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @brief ɾ���ֶ�����
	 * 
	 * @param fieldVarName
	 *            �ֶα���������
	 */
	public void deleteFieldDeclare(String fieldVarName) {
		FieldDeclaration[] fieldList = sourceContainer.getFields();
		if (fieldList != null && fieldList.length > 0) {
			int index = -1;
			for (int i = 0; i < fieldList.length; i++) {
				visitor.setFieldVarName(fieldVarName);
				sourceContainer.accept(visitor);
				if (visitor.getFieldDeclare() != null
						&& fieldList[i] == visitor.getFieldDeclare()) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				(sourceContainer.getFields()[index]).delete();
			}
		}
	}
	
	public FieldDeclaration getFieldDeclare(String fieldVarName) {
		refresh();
		visitor.setFieldVarName(fieldVarName);
		visitor.reset();
		sourceContainer.accept(visitor);
		return visitor.getFieldDeclare();
	}
	
	public FieldDeclaration[] getAllFieldDeclare() {
		refresh();
		return sourceContainer.getFields();
	}

	/**
	 * 
	 * @brief ɾ��
	 * 
	 * @param methodName
	 *            ������
	 */
	public void deleteMethodDeclare(String methodName) {
		MethodDeclaration[] methods = sourceContainer.getMethods();
		if (methods != null && methods.length > 0) {
			MethodDeclaration element = null;
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().getIdentifier().equals(methodName)) {
					element = methods[i];
				}
			}
			if (element != null) {
				element.delete();
			}
		}
	}
	
	public void deleteModule() {
		List methods = sourceContainer.modifiers();
		List dec = sourceContainer.bodyDeclarations();
		ChildListPropertyDescriptor o = sourceContainer.getModifiersProperty();
		if(methods != null && methods.size() > 0) {
			for (int i = 0; i < methods.size(); i++) {
				Object f = methods.get(i);
				if(f instanceof NormalAnnotation)
				{
					((NormalAnnotation)f).delete();
				}
			}
		}
	}

	public static void main(String[] args)
	{
/*		IFile buttonFile = new getProject().getFile("/src/model/TbDisposal.java");
		if(buttonFile.exists())
		{
			JavaCodeEngine engine = JavaCodeEngine.newInstance(buttonFile);
			
			int modifiers = Modifier.PUBLIC;
			JavaClassField[] fields = engine.getAllClassFields(Modifier.PRIVATE);
			engine.addField("www", "Integer", modifiers, 1|2);			
			engine.modifyField("currentOpinion", "sss", "Integer", modifiers, 1|2);
			engine.deleteField("currentOtherinfo");
			try {
				engine.save();
				engine.close();
			} catch (JavaModelException e) {
				e.printStackTrace();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}catch (CoreException e) {
				e.printStackTrace();
			}
		}
*/	}
}
