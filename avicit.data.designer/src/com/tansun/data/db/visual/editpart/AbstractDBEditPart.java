package com.tansun.data.db.visual.editpart;

import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.tansun.data.db.visual.model.AbstractDBModel;

public abstract class AbstractDBEditPart extends AbstractGraphicalEditPart 
implements PropertyChangeListener, IDoubleClickSupport {

	public void activate() {
		super.activate();
		((AbstractDBModel) getModel()).addPropertyChangeListener(this);
		
		
		
	}
	

	public void deactivate() {
		super.deactivate();
		((AbstractDBModel) getModel()).removePropertyChangeListener(this);
	}
	
	public void doubleClicked(){
		
	}

}
