package net.java.amateras.uml.usecasediagram.property;

import net.java.amateras.uml.usecasediagram.model.UsecaseModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TopUseComposite extends Composite {

		
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
		public static final int ISCONSTR=5;
		public static final int ISPOINT=6;
		public static final int ISDROOL=7;
		public static final int OTHERROOL=8;
		/**
		 * Create the composite
		 * 
		 * @param parent
		 * @param style
		 */
		public UsecaseModel model;
		public void createComposite(){
			
		}
		public TopUseComposite(CTabFolder folder, int style,int ctype,UsecaseModel model) {
			super(folder, style);
			this.model=model;
			this.type=ctype;
			this.setLayout(new FillLayout());
			text_viriables = new Text(this,  SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL|SWT.WRAP);
			CTabItem	tabItem = new CTabItem(folder, SWT.NONE);
			tabItem.setControl(this);
			switch(ctype){
			case ISDEAL:
				tabItem.setText("业务规则");
				//System.out.println(model.getDeDeal()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				text_viriables.setText(model.getDeDeal());
				break;
			case ISEXCEPTION:
				tabItem.setText("权限描述");
				//System.out.println(model.getDeException()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				text_viriables.setText(model.getDeException());
				break;
			case ISDEALFLOW:
				tabItem.setText("参与者");
				//System.out.println(model.getdReg()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				text_viriables.setText(model.getdReg());
				break;
			case ISCONSTR:
				tabItem.setText("前置条件");
				//System.out.println(model.getdReg()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				text_viriables.setText(model.getConstri());
				break;
			case ISPOINT:
				tabItem.setText("后置条件");
				//System.out.println(model.getdReg()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				text_viriables.setText(model.getTestPoint());
				break;
			case ISDROOL:
				tabItem.setText("异常操作流程");
				//System.out.println(model.getdReg()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				text_viriables.setText(model.getApplyre());
				break;
			case OTHERROOL:
				tabItem.setText("非功能需求");
				//System.out.println(model.getdReg()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				text_viriables.setText(model.getNoRequirement());
				break;
			}
		}
		
		public boolean applySelectedNode(){
			
			switch(type){
			case ISDEAL:
				
				model.setDeDeal(text_viriables.getText());
				//System.out.println(text_viriables.getText()+"mmmmmm");
				break;
			case ISEXCEPTION:
				model.setDeException(text_viriables.getText());
				break;
			case ISDEALFLOW:
				model.setdReg(text_viriables.getText());
				break;
			case ISCONSTR:
				model.setConstri(text_viriables.getText());
				break;
			case ISPOINT:
				model.setTestPoint(text_viriables.getText());
				break;
			case ISDROOL:
				model.setApplyre(text_viriables.getText());
				break;
			
			}
			
			return true;
		}
	}