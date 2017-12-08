package com.jvisio.flowchart.diagram.custom.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

public class DecisionFigure extends Shape {
	
	public DecisionFigure() {
		setLineWidth(2);
	}

	public boolean containsPoint(int x, int y) {
		if (!super.containsPoint(x, y))
			return false;
		return true;
	}

	protected void fillShape(Graphics graphics) {
		Rectangle r = getBounds();
		PointList points = new PointList(4);
		points.addPoint(r.x + r.width / 2, r.y);
		points.addPoint(r.x + r.width, r.y + r.height / 2);
		points.addPoint(r.x + r.width / 2, r.y + r.height);
		points.addPoint(r.x, r.y + r.height / 2);
		graphics.fillPolygon(points);
	}

	protected void outlineShape(Graphics graphics) {
		Rectangle r = getBounds();
		int x = r.x + lineWidth / 2;
		int y = r.y + lineWidth / 2;
		int w = r.width - Math.max(1, lineWidth);
		int h = r.height - Math.max(1, lineWidth);
		PointList points = new PointList(4);
		points.addPoint(x + w / 2, y);
		points.addPoint(x + w, y + h / 2);
		points.addPoint(x + w / 2, y + h);
		points.addPoint(x, y + h / 2);
		graphics.drawPolygon(points);
	}

}
