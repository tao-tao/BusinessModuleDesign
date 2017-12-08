/**
 * 
 */
package net.java.amateras.uml.classdiagram.figure;

import net.java.amateras.uml.classdiagram.model.CompositeModel;
import net.java.amateras.uml.figure.PresentationFigure;
import net.java.amateras.uml.model.AbstractUMLModel;

import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.PointList;

/**
 * @author Takahiro Shida.
 *
 */
public class CompositeConnectionFigure extends PolylineConnection implements
		PresentationFigure {
	
	private Label labelStereoType;
	private Label labelFromMultiplicity;
	private Label labelToMultiplicity;
	
	public CompositeConnectionFigure(CompositeModel model) {
		labelStereoType = new Label();
		if(!model.getStereoType().equals("")){
			labelStereoType.setText("<<" + model.getStereoType() + ">>");
		}
		
		labelFromMultiplicity = new Label();
		labelFromMultiplicity.setText(model.getFromMultiplicity());
		
		labelToMultiplicity = new Label();
		labelToMultiplicity.setText(model.getToMultiplicity());
		
		add(labelStereoType, new ConnectionLocator(this, ConnectionLocator.MIDDLE));
		
		ConnectionEndpointLocator sourceEndpointLocator = new ConnectionEndpointLocator(this, false);
		sourceEndpointLocator.setVDistance(15);
		add(labelFromMultiplicity, sourceEndpointLocator);
		
		ConnectionEndpointLocator targetEndpointLocator = new ConnectionEndpointLocator(this, true);
		targetEndpointLocator.setVDistance(15);
		add(labelToMultiplicity, targetEndpointLocator);
		
		PolygonDecoration decoration = new PolygonDecoration();
		PointList decorationPointList = new PointList();
		decorationPointList.addPoint( 0, 0);
		decorationPointList.addPoint(-2, 2);
		decorationPointList.addPoint(-4, 0);
		decorationPointList.addPoint(-2,-2);
		decoration.setTemplate(decorationPointList);
		setTargetDecoration(decoration);
		
	}
	
	public void update(CompositeModel model) {
		labelFromMultiplicity.setText(model.getFromMultiplicity());
		labelToMultiplicity.setText(model.getToMultiplicity());
		
		if(!model.getStereoType().equals("")){
			labelStereoType.setText("<<" + model.getStereoType() + ">>");
		} else {
			labelStereoType.setText("");
		}		
	}
	
	public Label getStereoTypeLabel() {
		return labelStereoType;
	}
	/* (non-Javadoc)
	 * @see net.java.amateras.uml.figure.PresentationFigure#updatePresentation(net.java.amateras.uml.model.AbstractUMLModel)
	 */
	public void updatePresentation(AbstractUMLModel model) {
		labelStereoType.setForegroundColor(model.getForegroundColor());
		labelFromMultiplicity.setForegroundColor(model.getForegroundColor());
		labelToMultiplicity.setForegroundColor(model.getForegroundColor());
		setForegroundColor(model.getForegroundColor());
	}

}
