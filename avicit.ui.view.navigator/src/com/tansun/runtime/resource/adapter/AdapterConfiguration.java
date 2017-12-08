package com.tansun.runtime.resource.adapter;


import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;

public class AdapterConfiguration {

	AdapterConfiguration() {
		adapters = new TreeSet();
	}

	public IAdapterFactory getAdapterFactory() {
		return adapterFactory;
	}

	public void setAdapterFactory(IAdapterFactory r_AdapterFactory) {
		adapterFactory = r_AdapterFactory;
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression r_Expression) {
		expression = r_Expression;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int r_Priority) {
		priority = r_Priority;
	}

	public boolean isAdapterable(Object r_Object, String r_TargetClassName)
	    {
	        if(!adapters.contains(r_TargetClassName))
	            return false;
	        if(expression == null)
	            return true;
	        EvaluationResult t_Result;
			try {
				t_Result = expression.evaluate(new EvaluationContext(null, r_Object));
		        if(EvaluationResult.TRUE == t_Result)
		            return true;
			} catch (CoreException e) {
			}
	        return false;
	    }

	public void addAdapter(String r_ClassName) {
		if (r_ClassName != null)
			adapters.add(r_ClassName);
	}

	public void removeAdapter(String r_ClassName) {
		if (r_ClassName != null)
			adapters.remove(r_ClassName);
	}

	private IAdapterFactory adapterFactory;
	private int priority;
	private Expression expression;
	private Set adapters;
}
