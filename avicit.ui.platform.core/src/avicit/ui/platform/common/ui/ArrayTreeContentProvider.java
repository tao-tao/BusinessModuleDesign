package avicit.ui.platform.common.ui;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ArrayTreeContentProvider implements ITreeContentProvider {
		public ArrayTreeContentProvider() {
			super();
		}

		public void dispose() {
		}

		public Object[] getChildren(Object parentElement) {
	        if (parentElement instanceof Object[]) {
				return (Object[]) parentElement;
			}
	        if (parentElement instanceof Collection) {
				return ((Collection) parentElement).toArray();
			}
	        return new Object[0];
		}

		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		
	}