package avicit.ui.runtime.core.usecase.common;

import com.tansun.runtime.resource.adapter.AbstractModelWorkbenchAdapter;

/**
 * @author Tao Tao
 *
 */
public class UseCaseCommonNodeAdapter extends AbstractModelWorkbenchAdapter {

	private UseCaseCommonNodeAdapter() {
	}

	public static UseCaseCommonNodeAdapter getInstance() {
		return instance;
	}

	public String getLabel(Object object) {
		UseCaseCommonNode node = (UseCaseCommonNode) object;
		return node.getDisplayName();
	}

	public Object[] getChildren(Object object) {
		return new Object[0];
	}

	public boolean hasChildren(Object element) {
		return false;
	}

	private static final UseCaseCommonNodeAdapter instance = new UseCaseCommonNodeAdapter();

}