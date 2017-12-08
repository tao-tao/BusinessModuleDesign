package avicit.ui.runtime.core.node;

import org.eclipse.core.runtime.PlatformObject;

import avicit.ui.runtime.core.INode;

public abstract class AbstractNode extends PlatformObject implements INode {

	public AbstractNode() {
	}

	public final int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getDisplayName() {
		return getName();
	}

	private int order = 100;
	
	public String getConfigPath() {
		return this.getResource().getProjectRelativePath();
	}
/*
	public boolean handles(java.lang.String arg0, java.lang.String arg1){
		return true;
	}

	public boolean isInstantiated(){
		return true;
	}

	public boolean isDeclaringPluginActive(){
		return true;
	}

	public org.eclipse.core.expressions.IPropertyTester instantiate() throws org.eclipse.core.runtime.CoreException{
		return this;
	}

	public boolean test(java.lang.Object arg0, java.lang.String arg1, java.lang.Object[] arg2, java.lang.Object arg3){
		return true;
	}*/
}