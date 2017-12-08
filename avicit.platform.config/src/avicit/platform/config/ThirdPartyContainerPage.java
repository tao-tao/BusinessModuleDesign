package avicit.platform.config;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ThirdPartyContainerPage extends WizardPage implements IClasspathContainerPage, IClasspathContainerPageExtension {
    
    private IJavaProject _proj;
    private IPath _path;
    private Text _dirText;
    private Button _dirBrowseButton;

    /**
     * Default Constructor - sets title, page name, description
     */
    public ThirdPartyContainerPage() {
        super(Messages.PageName, Messages.PageTitle, null);
        setDescription(Messages.PageDesc);
        setPageComplete(true);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension#initialize(org.eclipse.jdt.core.IJavaProject, org.eclipse.jdt.core.IClasspathEntry[])
     */
    public void initialize(IJavaProject project, IClasspathEntry[] currentEntries) {
        _proj = project;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
                | GridData.HORIZONTAL_ALIGN_FILL));
        composite.setFont(parent.getFont());
        
        createDirGroup(composite);
        
        setControl(composite);    
    }
    
    /**
     * Creates the directory label, combo, and browse button
     * 
     * @param parent the parent widget
     */
    private void createDirGroup(Composite parent) {
        Composite dirSelectionGroup = new Composite(parent, SWT.NONE);
        GridLayout layout= new GridLayout();
        layout.numColumns = 2;
        dirSelectionGroup.setLayout(layout);
        dirSelectionGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));

        new Label(dirSelectionGroup, SWT.NONE).setText(Messages.DirLabel);

        _dirText = new Text(dirSelectionGroup, SWT.BORDER);
        _dirText.setText(getInitDir());
        GridData gd = new GridData(SWT.FILL, SWT.TOP, true, true);
        _dirText.setLayoutData(gd);

//        _dirBrowseButton= new Button(dirSelectionGroup, SWT.PUSH);
//        _dirBrowseButton.setText( Messages.Browse ); 
//        _dirBrowseButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
//        _dirBrowseButton.addSelectionListener(new SelectionAdapter() {
//            public void widgetSelected(SelectionEvent e) {
//                handleDirBrowseButtonPressed();
//           }
//        });    
        setControl(dirSelectionGroup);
    }
    
    /**
     * Creates a directory dialog 
     */
    protected void handleDirBrowseButtonPressed() {
        DirectoryDialog dialog = new DirectoryDialog(getContainer().getShell(), SWT.SAVE);
        dialog.setMessage(Messages.DirSelect);
        dialog.setFilterPath(getDirValue());
        String dir = dialog.open();
        if (dir != null) {
        	_dirText.setText(dir);            
        }            
    }
    
    /**
     * @return the current directory
     */
    protected String getDirValue() {
        return _dirText.getText();
    }

    protected String getInitDir() {
    	String dir = this._proj.getProject().getName();
    	if(_path != null)
    	{
    		String p = _path.removeFirstSegments(1).toString();
    		if(!ThirdPartyContainer.ROOT_DIR.equals(p))
    			dir = dir+"/"+p;
    	}
        return dir;
    }
    
    /**
     * @return directory relative to the parent project
     */
    protected String getRelativeDirValue() {
        String proName = _proj.getProject().getName();
    	String dir = getDirValue();
    	int i = dir.indexOf("/");
    	if(i>0)
    	{
    		if(dir.substring(0, i).equals(proName))
    		{
    			dir = dir.substring(i+1);
    			return dir;
    		}
    	}
        return "";
    }
    
    /**
     * Checks that the directory is a subdirectory of the project being configured
     * 
     * @param dir a directory to validate
     * @return true if the directory is valid
     */
    private boolean isDirValid(String dir) {
    	String d = dir;
    	int i = dir.indexOf("/");
    	if(i>0)
    		d = d.substring(0,i);
        return _proj.getElementName().equals(d);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPage#finish()
     */
    public boolean finish() {
        if(!isDirValid(getDirValue())) {
            setErrorMessage( NLS.bind(Messages.DirErr, _proj.getProject().getName()));            
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPage#getSelection()
     */
    public IClasspathEntry getSelection() {
        String dir = getRelativeDirValue();
        if(dir.equals("")) {
            dir = ThirdPartyContainer.ROOT_DIR;
        }
        IPath containerPath = ThirdPartyContainer.ID.append( "/" + dir );
        return JavaCore.newContainerEntry(containerPath);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPage#setSelection(org.eclipse.jdt.core.IClasspathEntry)
     */
    public void setSelection(IClasspathEntry containerEntry) {
    	if(containerEntry != null)
    	{
	    	IPath path = containerEntry.getPath();
	    	_path = path;
    	}
    }    
}
