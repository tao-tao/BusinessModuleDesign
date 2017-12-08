package com.jvisio.flowchart.diagram.custom.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

public class ConnectorFigure extends Shape {
	
	public ConnectorFigure() {
		setLineWidth(2);
	}

	protected void fillShape(Graphics graphics) {
		Rectangle r = getBounds();
		int x = r.x;
		int y = r.y;
		int w = r.width;
		int h = r.height;
		PointList points = new PointList(4);
		points.addPoint(x, y);
		points.addPoint(x + w, y);
		points.addPoint(x + w, y + Math.round(0.6f * h));
		points.addPoint(x + w/2, y + h);
		points.addPoint(x, y + Math.round(0.6f * h));
		graphics.fillPolygon(points);
	}

	protected void outlineShape(Graphics graphics) {
		Rectangle r = getBounds();
		int x = r.x + lineWidth / 2;
		int y = r.y + lineWidth / 2;
		int w = r.width - Math.max(1, lineWidth);
		int h = r.height - Math.max(1, lineWidth);
		PointList points = new PointList(4);
		points.addPoint(x, y);
		points.addPoint(x + w, y);
		points.addPoint(x + w, y + Math.round(0.6f * h));
		points.addPoint(x + w/2, y + h);
		points.addPoint(x, y + Math.round(0.6f * h));
		graphics.drawPolygon(points);
	}

}
