package avicit.ui.runtime.core.action;

import org.eclipse.ui.IActionBars;
import org.eclipse.ui.internal.navigator.resources.actions.OpenActionProvider;

public class EcOpenActionProvider extends OpenActionProvider
{

    public EcOpenActionProvider()
    {
    }

    public void fillActionBars(IActionBars theActionBars)
    {
        super.fillActionBars(theActionBars);
        theActionBars.setGlobalActionHandler("org.eclipse.ui.navigator.Open", null);
    }
}