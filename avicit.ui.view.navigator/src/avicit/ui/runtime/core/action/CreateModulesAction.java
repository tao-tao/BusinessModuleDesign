package avicit.ui.runtime.core.action;
/*package com.tansun.runtime.core.action;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import com.tansun.runtime.core.INode;
import com.tansun.runtime.core.node.ComponentNode;
import com.tansun.runtime.resource.EclipseFolderDelegate;
import com.tansun.runtime.resource.inf.IFileDelegate;
import com.tansun.ui.navigator.NavigatorMessages;

public class CreateModulesAction extends SelectionListenerAction {

	private Shell shell;

	public CreateModulesAction(String text, Shell shell) {
		super(text);
		this.shell = shell;
	}

	public CreateModulesAction(String text) {
		super(text);
	}

	public void run() {

		ComponentNode node = null;
		Object obj = getStructuredSelection().getFirstElement();
		if (obj instanceof ComponentNode)
			node = (ComponentNode) obj;
		UpdateComponentDialog dialog = new UpdateComponentDialog(shell, node, obj);
		int code = dialog.open();
		if (code == Dialog.OK) {
			ConfigVO vo = dialog.vo;
			if (obj instanceof IPackageFragmentRoot) {
				IPackageFragmentRoot pr = (IPackageFragmentRoot) obj;
				IFolder folder = (IFolder) pr.getResource();
				IFile file = folder.getFile(INode.CONFIG_FILE);
				if(vo.enable)
				{
					IFolder metaf = folder.getFolder("META-INF");
					try {
						if(!metaf.exists())
						metaf.create(true, true, null);
						
						if (!file.exists()) {
							file.create(new ByteArrayInputStream(createInitial(vo).getBytes("UTF-8")), true, null);
								file.setCharset("UTF-8", null);
						}
					} catch (JavaModelException e) {
						e.printStackTrace();
					} catch (CoreException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				else
				{
					if(file.exists())
						try {
							file.delete(true, null);
						} catch (CoreException e) {
							e.printStackTrace();
						}
						
				}
			} 
			else if (node != null) 
			{
				IFileDelegate file = node.getEcfile();
				if(vo.enable)
				{
					try {
						IFile f = (IFile) file.getResource();
						if(f.exists())
							f.setContents(new ByteArrayInputStream(createInitial(vo).getBytes("UTF-8")), true, true, null);
						else
							f.create(new ByteArrayInputStream(createInitial(vo).getBytes("UTF-8")), true, null);
					} catch (JavaModelException e) {
						e.printStackTrace();
					} catch (CoreException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				else
				{
					if(file.exists())
						try {
							((IFile) file.getResource()).refreshLocal(1, null);
							((IFile) file.getResource()).delete(true, null);
						} catch (CoreException e) {
							e.printStackTrace();
						}
						
				}
			}
		}
		super.run();
	}

	protected boolean updateSelection(IStructuredSelection selection) {
		boolean flag = super.updateSelection(selection);
		if (flag)
			flag = getStructuredSelection().toList().size() == 1;
		return flag;
	}

	public static String createInitial(ConfigVO vo) throws JavaModelException {

		StringBuffer buffer = new StringBuffer();
		buffer.append("component.id=" + vo.id).append('\n');
		buffer.append("component.name=" + vo.name).append('\n');
		buffer.append("component.version=" + vo.version).append('\n');
		buffer.append("component.type=" + vo.type).append('\n');
		buffer.append("component.modules=" + vo.modules).append('\n');
		buffer.append("component.service=" + vo.service).append('\n');
		buffer.append("component.dao=" + vo.dao).append('\n');
		buffer.append("component.auto=" + vo.auto).append('\n');
		return buffer.toString();
	}

	static class UpdateComponentDialog extends MessageDialog {

		private Button checkbox_enable;
		private Button checkbox_pres;
		private Button checkbox_cont;
		private Button checkbox_serv;
		private Button checkbox_dao;
		private Button checkbox_data;
		//private Button checkbox_proc;
		private Text text_version;
		private Text text_id;
		private Text text_name;
		private Text text_path;
		private Text text_service;
		private Text text_dao;
		private Combo combo_type;
		ComponentNode node;
		Object selection;
		private Button checkbox_auto;

		UpdateComponentDialog(Shell parentShell, ComponentNode node, Object selection) {
			super(parentShell, NavigatorMessages.OPEN_CONFIG, null, "Ec Feature", 3, new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL }, 0);
			this.node = node;
			this.selection = selection;
		}

		protected Control createCustomArea(Composite parent) {
			Composite composite = new Composite(parent, SWT.NONE);
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			composite.setLayout(layout);

			Label namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("当前路径: ");
			GridData gd = new GridData();
			gd.widthHint = convertWidthInCharsToPixels(15);
			namelabel.setLayoutData(gd);
			text_path = new Text(composite, SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			text_path.setLayoutData(gd);
			//text_path.setEditable(false);
			
			namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("平台模块属性: ");
			gd = new GridData();
			gd.widthHint = convertWidthInCharsToPixels(15);
			namelabel.setLayoutData(gd);
			checkbox_enable = new Button(composite, SWT.CHECK);
			checkbox_enable.setText("是");
			gd = new GridData(GridData.FILL_HORIZONTAL);
			checkbox_enable.setLayoutData(gd);

			namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("模块id: ");
			gd = new GridData();
			gd.widthHint = convertWidthInCharsToPixels(15);
			namelabel.setLayoutData(gd);
			text_id = new Text(composite, SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			text_id.setLayoutData(gd);

			namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("模块名称: ");
			gd = new GridData();
			gd.widthHint = convertWidthInCharsToPixels(15);
			namelabel.setLayoutData(gd);
			text_name = new Text(composite, SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			text_name.setLayoutData(gd);

			namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("版本号: ");
			gd = new GridData();
			gd.widthHint = convertWidthInCharsToPixels(15);
			namelabel.setLayoutData(gd);
			text_version = new Text(composite, SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			text_version.setLayoutData(gd);

			namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("类型: ");
			gd = new GridData();
			gd.widthHint = convertWidthInCharsToPixels(15);
			namelabel.setLayoutData(gd);
			combo_type = new Combo(composite, SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			combo_type.setLayoutData(gd);
			combo_type.setItems(new String[] { "Web", "Comp" });

			namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("模块特性: ");
			gd = new GridData();
			gd.widthHint = convertWidthInCharsToPixels(15);
			namelabel.setLayoutData(gd);
			Composite composite_1 = new Composite(composite, SWT.NONE);
			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.numColumns = 5;
			composite_1.setLayout(gridLayout_1);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			composite_1.setLayoutData(gd);
			//String[] str=new String[]{"集成层","控制层","业务逻辑层","展示层"};
			
			checkbox_pres = new Button(composite_1, SWT.CHECK);
			checkbox_pres.setText("展示层");
			gd = new GridData();
			checkbox_pres.setLayoutData(gd);

			checkbox_cont = new Button(composite_1, SWT.CHECK);
			checkbox_cont.setText("控制层");
			gd = new GridData();
			checkbox_cont.setLayoutData(gd);

			checkbox_serv = new Button(composite_1, SWT.CHECK);
			checkbox_serv.setText("业务逻辑层");
			gd = new GridData();
			checkbox_serv.setLayoutData(gd);

			checkbox_dao = new Button(composite_1, SWT.CHECK);
			checkbox_dao.setText("集成层");
			gd = new GridData();
			checkbox_dao.setLayoutData(gd);
			
			checkbox_data = new Button(composite_1, SWT.CHECK);
			checkbox_data.setText("规则模版");
			gd = new GridData();
			checkbox_data.setLayoutData(gd);

			checkbox_proc = new Button(composite_1, SWT.CHECK);
			checkbox_proc.setText("流程层");
			gd = new GridData();
			checkbox_proc.setLayoutData(gd);

			namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("展现层自动转换Anno:");
			gd = new GridData();
			gd.widthHint = convertWidthInCharsToPixels(15);
			namelabel.setLayoutData(gd);
			checkbox_auto = new Button(composite, SWT.CHECK);
			checkbox_auto.setText("是");
			gd = new GridData(GridData.FILL_HORIZONTAL);
			checkbox_auto.setLayoutData(gd);

			namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("服务层组装文件: ");
			gd = new GridData();
			gd.widthHint = convertWidthInCharsToPixels(15);
			namelabel.setLayoutData(gd);
			text_service = new Text(composite, SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			text_service.setLayoutData(gd);

			namelabel = new Label(composite, SWT.NONE);
			namelabel.setText("DAO层组装文件: ");
			gd = new GridData();
			gd.widthHint = convertWidthInCharsToPixels(15);
			namelabel.setLayoutData(gd);
			text_dao = new Text(composite, SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			text_dao.setLayoutData(gd);

			//updateData(node);
			return composite;
		}

		private void updateData(ComponentNode node) {
			
			if(selection != null && selection instanceof PackageFragmentRoot)
				text_path.setText(((PackageFragmentRoot)selection).getPath().toString());
			
			if (node == null) {
				return;
			}
			
			text_path.setText(node.getFolder().getFullPath());

			String id = node.getProperty("component.id");
			if (id != null)
				text_id.setText(id);

			String service = node.getProperty("component.service");
			if (id != null)
				text_service.setText(service);

			String dao = node.getProperty("component.dao");
			if (id != null)
				text_dao.setText(dao);
			
			String name = node.getDisplayName();
			if (name != null)
				text_name.setText(name);
			
			name = node.getIncludeModules();
			if (name != null) {
				if (name.indexOf("zhanshi") >= 0)
					this.checkbox_pres.setSelection(true);

				if (name.indexOf("controller") >= 0)
					this.checkbox_cont.setSelection(true);

				if (name.indexOf("ywc") >= 0)
					this.checkbox_serv.setSelection(true);

				if (name.indexOf("jcc") >= 0)
					this.checkbox_dao.setSelection(true);

				if (name.indexOf("gzmb") >= 0)
					this.checkbox_data.setSelection(true);

				if (name.indexOf("process") >= 0)
					this.checkbox_proc.setSelection(true);
			}
			String version = node.getProperty("component.version");
			if (version != null) {
				this.text_version.setText(version);
			}
			String type = node.getProperty("component.type");
			if (type != null) {
				this.combo_type.setText(type);
			}

			this.checkbox_enable.setSelection(true);
			String auto = node.getProperty("component.auto");
			this.checkbox_auto.setSelection("true".equals(auto)?true:false);
		}

		@Override
		protected void buttonPressed(int buttonId) {
			if(StringUtils.isEmpty(text_name.getText()))
			{
				MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "提示", "请输入模块名称.");
				return;
			}
			vo.enable = this.checkbox_enable.getSelection();
			vo.id = this.text_id.getText();
			vo.name = this.text_name.getText();
			vo.service = this.text_service.getText();
			vo.dao = this.text_dao.getText();
			StringBuffer m = new StringBuffer();
			m.append(checkbox_pres.getSelection() ? "zhanshi," : "");
			m.append(checkbox_cont.getSelection() ? "controller," : "");
			m.append(checkbox_serv.getSelection() ? "ywc," : "");
			m.append(checkbox_dao.getSelection() ? "jcc," : "");
			m.append(checkbox_data.getSelection() ? "gzmb," : "");
			//m.append(checkbox_proc.getSelection() ? "process" : "");
			vo.modules = m.toString();
			vo.version = this.text_version.getText();
			vo.type = this.combo_type.getText();
			vo.auto = this.checkbox_auto.getSelection();
			super.buttonPressed(buttonId);
		}

		public ConfigVO vo = new ConfigVO();
	}

	static class ConfigVO {
		public ConfigVO() {
		}
		public String id;
		public boolean enable;
		public String name;
		public String modules;
		public String version;
		public String type;
		public String service;
		public String dao;
		public boolean auto=false;
	}

}*/