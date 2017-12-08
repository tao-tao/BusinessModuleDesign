package avicit.ui.runtime.core.action;

import org.eclipse.core.internal.preferences.DefaultPreferences;
import org.eclipse.ui.actions.SelectionListenerAction;

public class ResourcePropertyOpenAction extends SelectionListenerAction {

	public ResourcePropertyOpenAction(String text) {
		super(text);
	}

	public void run(){
		DefaultPreferences pre = new DefaultPreferences();
		pre.flush();
	}
}
