package com.tansun.data.db.wizard;

import java.io.InputStream;


import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import com.tansun.data.db.DBPlugin;
import com.tansun.data.db.dialect.DialectProvider;
import com.tansun.data.db.visual.editor.VisualDBSerializer;
import com.tansun.data.db.visual.model.RootModel;

public class NewDiagramWizardPage1 extends WizardNewFileCreationPage {
	
	private Label combo;
	private NewDiagramWizardPage2 page2;
	
	public NewDiagramWizardPage1(IStructuredSelection selection, NewDiagramWizardPage2 page2){
		super(DBPlugin.getResourceString("wizard.new.erd.title"), selection);
		setTitle(DBPlugin.getResourceString("wizard.new.erd.title"));
		setFileName("newfile.erd");
		this.page2 = page2;
	}
	
    public void createControl(Composite parent) {
		super.createControl(parent);
    	Composite composite = new Composite((Composite)getControl(), SWT.NULL);
    	GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
    	
    	composite.setLayout(layout);
    	composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    	
    	Label label = new Label(composite, SWT.NULL);
    	label.setText(DBPlugin.getResourceString("wizard.new.erd.dialect"));
    	
    	combo = new Label(composite, SWT.READ_ONLY);
    	String[] dialectNames = DialectProvider.getDialectNames();
    	/*for(int i=0;i<dialectNames.length;i++){
    		if(i==3)
    		combo.add(dialectNames[i]);
    	}*/
    	combo.setText(dialectNames[3]);
    	validatePage();
	}
    
    protected void createLinkTarget() {
    }
    
    protected boolean validatePage() {
    	boolean valid = super.validatePage();
    	if(valid){
    		String fileName = getFileName();
    		if(!fileName.endsWith(".erd")){
                setErrorMessage(DBPlugin.getResourceString("error.erd.extension"));
                valid = false;
    		}
    	}
    	if(valid){
			setMessage(DBPlugin.getResourceString("wizard.new.erd.message"));
    	}
    	return valid;
    }
    
    protected InputStream getInitialContents() {
    	RootModel root = new RootModel();
    	//System.out.println(combo.getText()+"????????????????");
    	root.setDialectName(combo.getText());
    	
    	try {
	    	page2.importTables(root);
	    	return VisualDBSerializer.serialize(root);
	    	
    	} catch(Exception ex){
    		DBPlugin.logException(ex);
    		return null;
    	}
    }

}
