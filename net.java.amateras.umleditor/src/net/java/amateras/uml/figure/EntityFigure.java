package net.java.amateras.uml.figure;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * ���x�����ݒ�ł���Figure.
 * @author Takahiro Shida.
 *
 */
public interface EntityFigure extends IFigure{

	Label getLabel();
	
	Rectangle getCellEditorRectangle();
	
}
