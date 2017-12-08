package avicit.ui.runtime.core.action;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.eclipse.ui.dialogs.PreferencesUtil;

import avicit.ui.runtime.core.node.AbstractNode;

public class PropertyAction extends SelectionListenerAction {

	public PropertyAction(String text) {
		super(text);
	}

	@SuppressWarnings("static-access")
	@Override
	public void run() {
		super.run();

		AbstractNode node = (AbstractNode) getStructuredSelection().getFirstElement();
		IResource resource = node.getResource().getResource();

		PreferenceDialog dialog = new PreferencesUtil().createPropertyDialogOn(Display.getDefault().getActiveShell(), resource, "org.eclipse.ui.propertypages.info.file", null, null);

		dialog.open();
	}
}
