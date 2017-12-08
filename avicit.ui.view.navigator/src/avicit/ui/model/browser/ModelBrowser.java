package avicit.ui.model.browser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ModelBrowser {
	private Browser browser = null;
	
	public void configContents(Composite shell, String url){
		shell.setLayout(new GridLayout());
		browser = new Browser(shell,SWT.BORDER);
		browser.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		browser.setUrl(url);
		browser.forward();
	}

}
