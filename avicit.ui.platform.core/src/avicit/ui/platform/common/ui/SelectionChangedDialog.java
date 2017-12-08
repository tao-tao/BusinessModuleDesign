package avicit.ui.platform.common.ui;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import avicit.ui.platform.common.util.AT;

public class SelectionChangedDialog extends TitleAreaDialog {

	private class TreeSelectionChangedCpt extends Composite {

		Button button_tip;
		Button button_autosave;

		public TreeSelectionChangedCpt(Composite parent, int style) {
			super(parent, style);
			setLayout(new GridLayout());

			button_tip = new Button(this, SWT.CHECK);
			button_tip.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					refreshUI();
				}
			});
			button_tip.setText("�л�ʱ��ʾ��(�Ƿ���Ҫ����?)");

			button_autosave = new Button(this, SWT.CHECK);
			button_autosave.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					refreshUI();

				}
			});
			button_autosave.setText("����ʾʱ�Զ�����");

			initData();

		}

		protected void initData() {

			Object[] data = handler.getInitData();
			AT.isTrue(2 == data.length);
			this.button_tip.setSelection((Boolean) data[0]);
			this.button_autosave.setSelection((Boolean) data[1]);

		};

		protected void refreshUI() {

			Object[] data = new Object[] {this.button_tip.getSelection(), this.button_autosave.getSelection()
					 };
			handler.refreshData(data);
		};

	}

	private final SelectionChangeConfigHandler handler;

	public SelectionChangedDialog(Shell parentShell,
			SelectionChangeConfigHandler handler) {
		super(parentShell);
		this.handler = handler;
		AT.isNotNull(handler);

	}

	protected Control createDialogArea(Composite parent) {
		Composite pcpt = (Composite) super.createDialogArea(parent);

		this.setTitle("��ݸ�Ŀ���δ���档������ڱ���,����[ȷ��]");

		new TreeSelectionChangedCpt(pcpt, SWT.NONE);

		return pcpt;
	}

}