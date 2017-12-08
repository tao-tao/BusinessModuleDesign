package net.java.amateras.uml.usecasediagram.property;

import net.java.amateras.uml.usecasediagram.model.UsecaseActorModel;
import net.java.amateras.uml.usecasediagram.model.UsecaseModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class UseCaseActorComposite extends Composite {

	private Text text_path;
	
	private Label label_2;

	private Label label_1;

	private Text text_viriables;

	private Button text_tx;
	private int type;
	public static final int ISINPUT=1;
	public static final int ISDEAL=2;
	public static final int ISEXCEPTION=3;
	public static final int ISDEALFLOW=4;
	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public UsecaseActorModel model;
	public void createComposite(){
		
	}
	public UseCaseActorComposite(CTabFolder folder, int style,int ctype,UsecaseActorModel model) {
		super(folder, style);
		this.model=model;
		this.type=ctype;
		this.setLayout(new FillLayout());
		text_viriables = new Text(this,  SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL|SWT.WRAP);
		CTabItem	tabItem = new CTabItem(folder, SWT.NONE);
		tabItem.setControl(this);
		switch(ctype){
		case ISDEAL:
			tabItem.setText("描述");
			text_viriables.setText(model.getDesc());
			break;
		}
		
	}
	
	public boolean applySelectedNode(){
		
		switch(type){
		case ISDEAL:
			
			model.setDesc(text_viriables.getText());
			break;
		}
		
		return true;
	}

}
