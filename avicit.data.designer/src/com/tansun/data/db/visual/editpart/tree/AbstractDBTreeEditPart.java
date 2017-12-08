package com.tansun.data.db.visual.editpart.tree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


import org.eclipse.gef.editparts.AbstractTreeEditPart;

import com.tansun.data.db.visual.model.AbstractDBEntityModel;
import com.tansun.data.db.visual.model.AbstractDBModel;

public class AbstractDBTreeEditPart extends AbstractTreeEditPart implements
		PropertyChangeListener {

	public void activate() {
		super.activate();
		if(getModel() instanceof AbstractDBModel){
			((AbstractDBModel) getModel()).addPropertyChangeListener(this);
		}
	}

    public void deactivate() {
        super.deactivate();
		if(getModel() instanceof AbstractDBModel){
			((AbstractDBModel) getModel()).removePropertyChangeListener(this);
		}
    }

	public void propertyChange(PropertyChangeEvent evt) {
		String propName = evt.getPropertyName();
	    if (AbstractDBEntityModel.P_SOURCE_CONNECTION.equals(propName)) { 
	        refreshChildren();
	    }
	}

}
