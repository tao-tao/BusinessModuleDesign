package avicit.ui.runtime.util;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.internal.corext.dom.GenericVisitor;

/**
 * 
 * @brief
 * <p>
 * <b>����JDT��ʵ����</b>
 * </p>
 */
public class DWTSourceVisitor extends GenericVisitor {
	
	private String fieldVarName;
	private FieldDeclaration field = null;
	private String methodName;
	private boolean existsMethod = false;

	public DWTSourceVisitor() {
	}

	public void reset(){
		this.field = null;
	}
	public boolean visit(VariableDeclarationFragment node) {
		if (node != null) {
			SimpleName name = node.getName();
			if (name != null && fieldVarName != null) {
				if (name.getIdentifier().equals(fieldVarName)) {
					field = (FieldDeclaration) node.getParent();
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @brief ����Ҫ��ѯ���ֶα�����
	 * 
	 * @param fieldVarName
	 */
	public void setFieldVarName(String fieldVarName) {
		this.fieldVarName = fieldVarName;
	}

	/**
	 * 
	 * @brief ��������ָ���Ĳ�ѯ�ֶα������ֶ�����
	 * 
	 * @return
	 */
	public FieldDeclaration getFieldDeclare() {
		return field;
	}

	/**
	 * 
	 * @brief ����Ҫ��JDT���в�ѯ�ķ�����
	 * 
	 * @param methodName
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * 
	 * @brief �ж�JDT�����Ƿ���ڷ���
	 * 
	 * @return
	 */
	public boolean existsMethod() {
		if (existsMethod == true) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @brief �ж�JDT�����Ƿ�����ֶα���
	 * 
	 * @return
	 */
	public boolean existsFieldVarName() {
		if (field != null) {
			field = null;
			return true;
		} else {
			return false;
		}
	}
}