package avicit.ui.runtime.core.requirement.analysis.usecase;

import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

public class FunctionUseCaseNodeAdapter extends AbstractModelWorkbenchAdapter {

	public FunctionUseCaseNodeAdapter() {
	}


	public static FunctionUseCaseNodeAdapter getInstance() {
		return instance;
	}

	public String getLabel(Object object) {
		FunctionUseCaseNode node = (FunctionUseCaseNode) object;
		return node.getDisplayName();
	}

	public Object[] getChildren(Object object) {
		return new Object[0];
	}

	public boolean hasChildren(Object element) {
		return false;
	}

	private static final FunctionUseCaseNodeAdapter instance = new FunctionUseCaseNodeAdapter();
}
