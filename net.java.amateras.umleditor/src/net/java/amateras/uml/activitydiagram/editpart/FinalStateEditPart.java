package net.java.amateras.uml.activitydiagram.editpart;


import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.editpart.AbstractUMLEntityEditPart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;

/**
 * 
 * @author Naoki Takezoe
 */
public class FinalStateEditPart extends AbstractUMLEntityEditPart {
	
	protected IFigure createFigure() {
		Label label = new Label();
		label.setIcon(UMLPlugin.getImageDescriptor("icons/final_state.png")
				.createImage());
		return label;
	}

}
