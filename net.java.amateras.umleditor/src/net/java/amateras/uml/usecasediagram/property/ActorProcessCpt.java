package net.java.amateras.uml.usecasediagram.property;

import java.util.ArrayList;
import java.util.List;

import net.java.amateras.uml.usecasediagram.model.UsecaseActorModel;
import net.java.amateras.uml.usecasediagram.model.UsecaseModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;




public class ActorProcessCpt extends Composite {
	public Button button_cancel;

	public Button button_apply;

	private CTabItem tabItem;
	private CTabItem tabItem_1;

	private Composite composite;
	
	private CTabFolder tabFolder;

	private ProcessGeneralCpt cpt_general;

	private UsecaseActorModel model;
	
	private InputCpt text_input;
	
	private Label label_1;
	private Label label_2;
	private Label label_3;
	private Label label_4;
	private Label label_5;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Button button_1;
	private Button button_3;
	private Button button_4;
	private List<UseCaseActorComposite> content=new ArrayList();
	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public void addTab(UseCaseActorComposite parent){
		content.add(parent);
	}
	public ActorProcessCpt(Composite parent, int style,UsecaseActorModel model) {
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
		tabItem.setText("基本信息");

		cpt_general = new ProcessGeneralCpt(tabFolder, SWT.NONE);
		tabItem.setControl(cpt_general);
		
		//tabItem_1 = new CTabItem(tabFolder, SWT.NONE);
		//tabItem_1.setText("fdfd");
		addTab(new UseCaseActorComposite(tabFolder, style, UseCaseActorComposite.ISDEAL, model));
		//text_input = new InputCpt(tabFolder, SWT.NONE,model);
		//tabItem_1.setControl(text_input);
		
		
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
			gridLayout.numColumns = 3;

			this.setLayout(gridLayout);
			label_2 = new Label(this, SWT.NONE);
			label_2.setText("名称");
			
			text_2 = new Text(this, SWT.BORDER);
			GridData gd_text_name = new GridData(SWT.FILL, SWT.CENTER, true, false,2,1);
			text_2.setLayoutData(gd_text_name);
			text_2.setText(model.getName());
			label_1 = new Label(this, SWT.NONE);
			label_1.setText("类别");

			text_1 = new Text(this, SWT.BORDER);
			 gd_text_name = new GridData(SWT.FILL, SWT.CENTER, true, false,2,1);
			text_1.setLayoutData(gd_text_name);
			text_1.setText(model.getCata());
		
			label_3 = new Label(this, SWT.NONE);
			label_3.setText("备注");
			
			text_3 = new Text(this, SWT.MULTI |SWT.BORDER);
			gd_text_name = new GridData(SWT.FILL, SWT.FILL, true, true,2,1);
			text_3.setLayoutData(gd_text_name);
			
			text_3.setText(model.getOtherinfo());
			
		}
		
		public boolean applySelectedNode(UsecaseActorModel model){
			
			
			
			return true;
		}
	}

	public boolean onBnClick_Apply() {
		if (null != this.model) {
			cpt_general.applySelectedNode(model);
			//text_input.applySelectedNode(model);
			model.setName(text_2.getText());
			model.setCata(text_1.getText());
			model.setOtherinfo(text_3.getText());
			for(int i=0;i<content.size();i++){
				content.get(i).applySelectedNode();
			}
		}
		return true;
	}
	
}

