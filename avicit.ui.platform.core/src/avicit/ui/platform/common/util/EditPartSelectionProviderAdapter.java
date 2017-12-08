package avicit.ui.platform.common.util;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.*;

public class EditPartSelectionProviderAdapter implements ISelectionProvider {

	public EditPartSelectionProviderAdapter(
			ISelectionProvider iselectionprovider) {
		target = iselectionprovider;
	}

	public void addSelectionChangedListener(
			ISelectionChangedListener iselectionchangedlistener) {
	}

	public ISelection getSelection() {
		ISelection iselection = target.getSelection();
		if (iselection != null && (iselection instanceof StructuredSelection)) {
			Object obj = ((StructuredSelection) iselection).getFirstElement();
			return new StructuredSelection(((EditPart) obj).getModel());
		} else {
			return target.getSelection();
		}
	}

	public void removeSelectionChangedListener(
			ISelectionChangedListener iselectionchangedlistener) {
	}

	public void setSelection(ISelection iselection) {
	}

	ISelectionProvider target;
}
