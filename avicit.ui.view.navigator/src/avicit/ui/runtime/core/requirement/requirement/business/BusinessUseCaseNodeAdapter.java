package avicit.ui.runtime.core.requirement.requirement.business;

import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

/**
 * @author TaoTao
 *
 */
public class BusinessUseCaseNodeAdapter extends AbstractModelWorkbenchAdapter {

	public BusinessUseCaseNodeAdapter() {
	}


	public static BusinessUseCaseNodeAdapter getInstance() {
		return instance;
	}

	public String getLabel(Object object) {
		BusinessUseCaseNode node = (BusinessUseCaseNode) object;
		return node.getDisplayName();
	}

	public Object[] getChildren(Object object) {
		return new Object[0];
	}

	public boolean hasChildren(Object element) {
		return false;
	}

	private static final BusinessUseCaseNodeAdapter instance = new BusinessUseCaseNodeAdapter();
}
