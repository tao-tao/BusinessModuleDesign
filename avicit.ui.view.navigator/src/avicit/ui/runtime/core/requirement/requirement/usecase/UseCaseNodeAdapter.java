package avicit.ui.runtime.core.requirement.requirement.usecase;

import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

/**
 * @author TaoTao
 *
 */
public class UseCaseNodeAdapter extends AbstractModelWorkbenchAdapter {

	private UseCaseNodeAdapter() {
	}

	public static UseCaseNodeAdapter getInstance() {
		return instance;
	}

	public String getLabel(Object object) {
		UseCaseNode node = (UseCaseNode) object;
		return node.getDisplayName();
	}

	public Object[] getChildren(Object object) {
		return new Object[0];
	}

	public boolean hasChildren(Object element) {
		return false;
	}

	private static final UseCaseNodeAdapter instance = new UseCaseNodeAdapter();

}