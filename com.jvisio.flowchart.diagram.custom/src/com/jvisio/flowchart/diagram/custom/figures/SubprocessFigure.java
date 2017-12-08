package com.jvisio.flowchart.diagram.custom.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;

public class SubprocessFigure extends RectangleFigure {
	
	public SubprocessFigure() {
		setLineWidth(2);
	}

	protected void outlineShape(Graphics graphics) {
		Rectangle r = getBounds();
		int x = r.x + lineWidth / 2;
		int y = r.y + lineWidth / 2;
		int w = r.width - Math.max(1, lineWidth);
		int h = r.height - Math.max(1, lineWidth);
		graphics.drawRectangle(x, y, w, h);
		graphics.drawLine(Math.round(x + 0.1f * w), y,
				Math.round(x + 0.1f * w), y + h);
		graphics.drawLine(Math.round(x + 0.9f * w), y,
				Math.round(x + 0.9f * w), y + h);
	}
}
