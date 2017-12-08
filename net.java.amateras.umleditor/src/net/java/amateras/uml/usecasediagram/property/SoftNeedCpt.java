package net.java.amateras.uml.usecasediagram.property;

import net.java.amateras.uml.usecasediagram.model.UsecaseActorModel;
import net.java.amateras.uml.usecasediagram.model.UsecaseModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SoftNeedCpt extends Composite {
	
	private Label label_need;
	private Label label_flg;
	private Label label_title;
	private Label label_grade;
	private Label label_first;
	private Text text_need;
	private Text text_title;
	private Combo combo_flg;
	private Combo combo_grade;
	private Combo combo_first;
	

	public SoftNeedCpt(Composite parent, int style,UsecaseModel model) {
		super(parent, style);

		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;

		this.setLayout(gridLayout);
		
		label_need = new Label(this, SWT.NONE);
		label_need.setText("金航");
		
		text_need = new Text(this, SWT.BORDER);
		GridData gd_text_name = new GridData(SWT.FILL, SWT.CENTER, true, false,2,1);
		text_need.setLayoutData(gd_text_name);
		text_need.setText(model.getUserNeed()==null?"":model.getUserNeed());
		
		label_title = new Label(this, SWT.NONE);
		label_title.setText("金航");
		
		text_title = new Text(this, SWT.BORDER);
		gd_text_name = new GridData(SWT.FILL, SWT.CENTER, true, false,2,1);
		text_title.setLayoutData(gd_text_name);
		text_title.setText(model.getUserTitle()==null?"":model.getUserTitle());
		
		label_flg = new Label(this, SWT.NONE);
		label_flg.setText("金航");
		
		combo_flg = new Combo(this, SWT.BORDER);
		gd_text_name = new GridData(SWT.LEFT, SWT.CENTER, true, false,2,1);
		gd_text_name.widthHint =100;
		combo_flg.setLayoutData(gd_text_name);
		combo_flg.setItems(new String[] { "ԭʼ", "�޸�","����","ɾ��" });
		combo_flg.setText(model.getUserFlg()==null?"":model.getUserFlg());
		
		label_grade = new Label(this, SWT.NONE);
		label_grade.setText("金航");
		
		combo_grade = new Combo(this, SWT.NONE);
		gd_text_name = new GridData(SWT.LEFT, SWT.CENTER, false, false,2,1);
		gd_text_name.widthHint =100;
		combo_grade.setLayoutData(gd_text_name);
		combo_grade.setItems(new String[] { "A", "B","C" });
		combo_grade.setText(model.getUserGrade()==null?"":model.getUserGrade());
		
		label_first = new Label(this, SWT.NONE);
		label_first.setText("金航");
		
		combo_first = new Combo(this, SWT.NONE);
		gd_text_name = new GridData(SWT.LEFT, SWT.CENTER, false, false,2,1);
		gd_text_name.widthHint =100;
		combo_first.setLayoutData(gd_text_name);
		combo_first.setItems(new String[] {"1", "2","3"});
		combo_first.setText(model.getUserFirst()==null?"":model.getUserFirst());
	
		
		
	}
	
	public boolean applySelectedNode(UsecaseModel model){
		
		model.setUserNeed(text_need.getText());
		model.setUserTitle(text_title.getText());
		model.setUserFlg(combo_flg.getText());
		model.setUserGrade(combo_grade.getText());
		model.setUserFirst(combo_first.getText());
		
		
		return true;
	}
}
