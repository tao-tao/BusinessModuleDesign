package net.java.amateras.uml.usecasediagram.property;

import net.java.amateras.uml.usecasediagram.model.UsecaseActorModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class InputCpt extends Composite {

	private Label label_1;
	private Label label_2;
	private Text text_1;
	private Text text_2;
	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public InputCpt(Composite parent, int style,UsecaseActorModel model) {
		super(parent, style);

		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;

		this.setLayout(gridLayout);
		label_1 = new Label(this, SWT.NONE);
		label_1.setText("功能需求");

		text_1 = new Text(this, SWT.MULTI |SWT.BORDER);
		GridData gd_text_name = new GridData(SWT.FILL, SWT.FILL, true, true,2,1);
		text_1.setLayoutData(gd_text_name);
		text_1.setText(model.getFdec()==null?"":model.getFdec());
		
		label_2 = new Label(this, SWT.NONE);
		label_2.setText("性能需求");
		
		text_2 = new Text(this, SWT.MULTI |SWT.BORDER);
		gd_text_name = new GridData(SWT.FILL, SWT.FILL, true, true,2,1);
		text_2.setLayoutData(gd_text_name);
		text_2.setText(model.getXdec()==null?"":model.getXdec());

	
		
	}
	
	public boolean applySelectedNode(UsecaseActorModel model){
		
		model.setFdec(text_1.getText());
		model.setXdec(text_2.getText());
		
		return true;
	}
	}