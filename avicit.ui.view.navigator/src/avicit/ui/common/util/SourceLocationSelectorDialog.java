package avicit.ui.common.util;


import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Joe Hudson
 */
public class SourceLocationSelectorDialog extends Dialog {

	private Combo sourceLocations;
	private List potentialRoots;
	private IProject project;
	private IPackageFragmentRoot selectedRoot;
	
	private String name;
	
	public SourceLocationSelectorDialog(Shell shell, List potentialRoots, IProject project) {
		super(shell);
		this.potentialRoots = potentialRoots;
		this.project = project;
	}

	/**
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		newShell.setText("Please select the source location");
		super.configureShell(newShell);
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));
		
		sourceLocations = new Combo(container, SWT.NONE);
		String selection = null;
		for (int i=0; i<potentialRoots.size(); i++) {
		    String s = ((IPackageFragmentRoot) potentialRoots.get(i)).getPath().toOSString();
		    sourceLocations.add(s);
		    if (null != s) selection = s;
		}
		sourceLocations.setText(selection);
		return container;
	}
	
	/**
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
	    String s = sourceLocations.getText();
		for (int i=0; i<potentialRoots.size(); i++) {
		    IPackageFragmentRoot root = (IPackageFragmentRoot) potentialRoots.get(i);
		    if (root.getPath().toOSString().equals(s)) selectedRoot = root;
		}
	   super.okPressed();
	}
	
	public IPackageFragmentRoot getPackageFragmentRoot () {
	    return selectedRoot;
	}
}