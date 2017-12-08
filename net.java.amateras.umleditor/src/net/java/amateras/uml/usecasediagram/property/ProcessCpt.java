package net.java.amateras.uml.usecasediagram.property;

import java.util.ArrayList;
import java.util.List;

import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.usecasediagram.model.DroolData;
import net.java.amateras.uml.usecasediagram.model.UsecaseModel;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;




public class ProcessCpt extends Composite {
	public Button button_cancel;

	public Button button_apply;

	private CTabItem tabItem;
	private CTabItem tabItem_1;
	private CTabItem tabItem_need;
	private CTabItem tabItem_drools;

	private CTabFolder tabFolder;

	private ProcessGeneralCpt cpt_general;

	private UsecaseModel model;
	
	private ProcessDataFieldCpt text_viriables;
	
	private SoftNeedCpt softCpt;
	
	private DroolDataFieldCpt text_drool;
	
	RGB rgb1;
	
	RGB rgb2;
	
	IFile file;
	
	private Label label_1;
	private Label label_2;
	private Label label_3;
	private Label label_4;
	private Label label_5;
	private Label label_id;
	
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Text text_5;
	private Text text_id;
	private Button button_1;
	private Button button_3;
	private Button button_4;
	private Button button_5;
	private Label label_status;
	private Label label_flg;
	private Label label_title;
	private Label label_grade;
	private Combo combo_status;
	private Text text_flg;
	private Text text_title;
	private Combo combo_grade;
	private List<TopUseComposite> content=new ArrayList();
	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public void addTab(TopUseComposite parent){
		content.add(parent);
	}
	public ProcessCpt(Composite parent, int style,UsecaseModel model) {
		super(parent, style);
		this.model=model;
		GridLayout gl = new GridLayout();
		gl.marginHeight = 0;
		setLayout(gl);
		setBackgroundMode(SWT.INHERIT_DEFAULT);

		tabFolder = new CTabFolder(this, SWT.BORDER | SWT.FLAT);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 420;
		tabFolder.setLayoutData(data);
		tabFolder.setSimple(false);

		tabItem = new CTabItem(tabFolder, SWT.NONE);
		tabItem.setText("用例基本信息");

		cpt_general = new ProcessGeneralCpt(tabFolder, SWT.NONE);
		tabItem.setControl(cpt_general);
		//update by lidong 
//		tabItem_1 = new CTabItem(tabFolder, SWT.NONE);
//		tabItem_1.setText("金航");
//
//		text_viriables = new ProcessDataFieldCpt(tabFolder, SWT.NONE);
//		tabItem_1.setControl(text_viriables);
//		
		addTab(new TopUseComposite(tabFolder, style, TopUseComposite.ISDEAL, model));
		addTab(new TopUseComposite(tabFolder, style, TopUseComposite.ISEXCEPTION, model));
		addTab(new TopUseComposite(tabFolder, style, TopUseComposite.ISDEALFLOW, model));
		addTab(new TopUseComposite(tabFolder, style, TopUseComposite.ISCONSTR, model));
		addTab(new TopUseComposite(tabFolder, style, TopUseComposite.ISDROOL, model));
		addTab(new TopUseComposite(tabFolder, style, TopUseComposite.OTHERROOL, model));
		
	}
	
	public class ProcessGeneralCpt extends Composite {

		/**
		 * Create the composite
		 * 
		 * @param parent
		 * @param style
		 */
		public ProcessGeneralCpt(Composite parent, int style) {
			super(parent, style);

			final GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 4;

			this.setLayout(gridLayout);

			label_id = new Label(this, SWT.NONE);
			label_id.setText("用例编号");
			
			text_id = new Text(this, SWT.BORDER);
			GridData gd_text_name = new GridData(SWT.FILL, SWT.CENTER, true, false,3,1);
			text_id.setLayoutData(gd_text_name);
			text_id.setText(model.getId()==null?"":model.getId());
			
			label_2 = new Label(this, SWT.NONE);
			label_2.setText("用例名称");
			
			text_2 = new Text(this, SWT.BORDER);
			gd_text_name = new GridData(SWT.FILL, SWT.CENTER, true, false,3,1);
			text_2.setLayoutData(gd_text_name);
			text_2.setText(model.getName()==null?"":model.getName());
			
			
			
			
			
			
			label_flg = new Label(this, SWT.NONE);
			label_flg.setText("用例描述");
			
			text_flg = new Text(this, SWT.BORDER);
			gd_text_name = new GridData(SWT.FILL, SWT.CENTER, true, false,3,1);
			text_flg.setLayoutData(gd_text_name);
			text_flg.setText(model.getSoftFlg()==null?"":model.getSoftFlg());
			
			label_title = new Label(this, SWT.NONE);
			label_title.setText("业务规则");
			
			text_title = new Text(this, SWT.BORDER);
			gd_text_name = new GridData(SWT.FILL, SWT.CENTER, true, false,3,1);
			text_title.setLayoutData(gd_text_name);
			text_title.setText(model.getSoftTitle()==null?"":model.getSoftTitle());
			
			label_grade = new Label(this, SWT.NONE);
			label_grade.setText("状态");
			combo_grade = new Combo(this, SWT.NONE);
			gd_text_name = new GridData(SWT.LEFT, SWT.CENTER, true, false,3,1);
			gd_text_name.widthHint =100;
			combo_grade.setLayoutData(gd_text_name);
			combo_grade.setItems(new String[] {"1", "2","3" });
			combo_grade.setText(model.getSoftGrade()==null?"":model.getSoftGrade());

//			
//			
			label_1 = new Label(this, SWT.NONE);
			label_1.setText("用例图颜色");

			text_1 = new Text(this, SWT.BORDER);
			 gd_text_name = new GridData(SWT.FILL, SWT.CENTER, true, false,2,1);
			text_1.setLayoutData(gd_text_name);
			//text_1.setText(model.getClass().getName());
			button_1 = new Button(this, SWT.NONE);
			button_1.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					ColorDialog dialog =new ColorDialog(getShell());
					rgb1 =dialog.open();
					if(rgb1 != null){
						text_1.setText(rgb1.toString());
					}
					
				}
			});
			button_1.setText("选择颜色");
			label_3 = new Label(this, SWT.NONE);
			label_3.setText("边框颜色");
			
			text_3 = new Text(this, SWT.BORDER);
			gd_text_name = new GridData(SWT.FILL, SWT.CENTER, true, false,2,1);
			text_3.setLayoutData(gd_text_name);
			//text_3.setText(model.getForegroundColor().toString());
			button_3 = new Button(this, SWT.NONE);
			button_3.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					ColorDialog dialog =new ColorDialog(getShell());
					rgb2 =dialog.open();
					if(rgb2 != null){
						text_3.setText(rgb2.toString());
					}
				}
			});
			button_3.setText("选择颜色");
		}
		
		public boolean applySelectedNode(UsecaseModel model){
			//update by lidong
//			DataField[] datas = text_viriables.fetchData();
//			model.getInput().setDatas(datas);
			
//			DroolData[] drools =text_drool.fetchData();
//			model.getInput().setDrools(drools);
			return true;
		}
	}
	
	

	public boolean onBnClick_Apply() {
		if (null != this.model) {
			cpt_general.applySelectedNode(model);
		//	softCpt.applySelectedNode(model);
			for(int i=0;i<content.size();i++){
				content.get(i).applySelectedNode();
			}
		    model.setBackgroundColor(rgb1);
			model.setName(text_2.getText());
			model.setId(text_id.getText());
			model.setForegroundColor(rgb2);
			//update by 李东
			model.setSoftFlg(text_flg.getText());
			model.setSoftTitle(text_title.getText());
			model.setSoftGrade(combo_grade.getText());
		}
		return true;
	}
	
}

