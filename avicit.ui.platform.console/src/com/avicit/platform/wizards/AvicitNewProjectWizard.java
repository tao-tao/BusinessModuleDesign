package com.avicit.platform.wizards;

import org.eclipse.swt.widgets.Composite;

public class AvicitNewProjectWizard extends AvicitPage {

	
	protected AvicitNewProjectWizard(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite composite) {
		this.createMainPage(composite);
	}

	@Override
	public void createEcPage(Composite parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void change() {
		// TODO Auto-generated method stub
		
	}

}
