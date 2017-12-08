package com.tansun.data.db.visual.editpart;


import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.tansun.data.db.DBPlugin;
import com.tansun.data.db.validator.DiagramErrors;

public class TableFigure extends Figure {

//	private static Color COLOR_BACKGROUND = new Color(Display.getDefault(), 255, 255, 206);
//	private static Color COLOR_LINKED = new Color(Display.getDefault(), 230, 230, 230);
	
	private Label name;
	private ColumnLayoutFigure columnFigure;
	private CompartmentFigure columnNameFigure;
	private CompartmentFigure columnTypeFigure;
	private CompartmentFigure notNullFigure;
	private int flag = 0;

	public TableFigure(){
		this.name = new Label();
		this.name.setBorder(new MarginBorder(2, 2, 0, 2));

		this.columnNameFigure = new CompartmentFigure();
		this.columnTypeFigure = new CompartmentFigure();
		this.notNullFigure = new CompartmentFigure();
		this.columnFigure = new ColumnLayoutFigure();

		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
//		setBackgroundColor(new Color(Display.getDefault(), 255, 255, 206));
		//setBorder(new LineBorder(ColorConstants.black, 1));
		setOpaque(true);

		add(this.name);
		add(this.columnFigure);
		
			this.setPreferredSize(200, 200);
		
		
		this.columnFigure.add(columnNameFigure);
		this.columnFigure.add(columnTypeFigure);
		this.columnFigure.add(notNullFigure);
		
	}

	public void setTableName(String tableName){
		this.name.setText(tableName);
	}

	public void setErrorMessage(String message){
		//System.out.println("set message.................."+message);
		if(message == null || message.length()==0){
			this.name.setIcon(null);
			this.name.setToolTip(null);
		} else {
			if(message.indexOf(DiagramErrors.ERROR_PREFIX) >= 0){
				this.name.setIcon(DBPlugin.getImage(DBPlugin.ICON_ERROR));
				setBorder(new LineBorder(ColorConstants.red, 3));
			} else {
				this.name.setIcon(DBPlugin.getImage(DBPlugin.ICON_WARNING));
				setBorder(new LineBorder(ColorConstants.red, 3));
			}
			this.name.setToolTip(new Label(message));
		}
	}
	public void setBorders(int i){
		
		switch(i){
		case 1:
			setBorder(new LineBorder(ColorConstants.red, 3) );
			break;
		case 0:
			setBorder(new LineBorder(ColorConstants.black, 1) );
			break;
		}
	}
	
	public void setLinkedTable(boolean linked){
		//System.out.println(getBorder().g);
		if(linked){
			setForegroundColor(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
			//((LineBorder) getBorder()).setColor(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		} else {
			setForegroundColor(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			//((LineBorder) getBorder()).setColor(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		}
	}

	public void add(IFigure figure, Object constraint, int index) {
		if(figure instanceof ColumnFigure){
			if(flag == 0){
				columnNameFigure.add(figure);
				flag = 1;
			} else if(flag == 1){
				columnTypeFigure.add(figure);
				flag = 2;
			} else {
				notNullFigure.add(figure);
				flag = 0;
			}
		} else {
			super.add(figure, constraint, index);
		}
		
	}

	public void remove(IFigure figure) {
		if(figure instanceof  ColumnFigure){
			columnNameFigure.remove(figure);
			columnTypeFigure.remove(figure);
			notNullFigure.remove(figure);
		} else {
			super.remove(figure);
		}
	}

	public void removeAllColumns(){
		columnNameFigure.removeAll();
		columnTypeFigure.removeAll();
		notNullFigure.removeAll();
	}

	public Label getLabel(){
		return name;
	}

	private class ColumnLayoutFigure extends Figure {
		public ColumnLayoutFigure(){
			ToolbarLayout layout = new ToolbarLayout(true);
			layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
			layout.setStretchMinorAxis(false);
			layout.setSpacing(2);
			setLayoutManager(layout);
			setBorder(new CompartmentFigureBorder());
		}
		
	}

	public class CompartmentFigureBorder extends AbstractBorder {
		public Insets getInsets(IFigure figure) {
			return new Insets(1, 0, 2, 0);
		}
		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(), tempRect.getTopRight());
		}
	}
	/*public void paintFigure(Graphics g) {
		Rectangle r = bounds;
		g.drawArc(r.x + r.width / 8, r.y, r.width / 4, r.height - 1, 90, 180);
		g.drawLine(r.x + r.width / 4, r.y, r.x + 3 * r.width / 4, r.y);
		g.drawLine(r.x + r.width / 4, r.y + r.height - 1,
				r.x + 3 * r.width / 4, r.y + r.height - 1);
		g.drawArc(r.x + 5 * r.width / 8, r.y, r.width / 4, r.height - 1, 270,
				180);
		g.drawText(message, r.x + 3 * r.width / 8, r.y + r.height / 8);
	}*/
}
