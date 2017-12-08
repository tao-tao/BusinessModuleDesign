package avicit.ui.runtime.core.action.providers;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

public class EditActionProvider extends CommonActionProvider {

	public EditActionProvider() {
	}

	public void init(ICommonActionExtensionSite anActionSite) {
		site = anActionSite;
		editGroup = new EditActionGroup(site);
	}

	public void dispose() {
		editGroup.dispose();
	}

	public void fillActionBars(IActionBars actionBars) {
		editGroup.fillActionBars(actionBars);
	}

	public void fillContextMenu(IMenuManager menu) {
		editGroup.fillContextMenu(menu);
	}

	public void setContext(ActionContext context) {
		editGroup.setContext(context);
	}

	public void updateActionBars() {
		editGroup.updateActionBars();
	}

	private EditActionGroup editGroup;
	private ICommonActionExtensionSite site;
}