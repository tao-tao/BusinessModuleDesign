package avicit.ui.platform.common.ui;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import avicit.ui.platform.common.data.ManagementInfo;

public class ProjectManagerCpt extends Composite {

	private Text text_author;

	private DateTime text_updateTime;

	private Text text_version;

	private Text text_progress;

	private Text text_percent;

	private DateTime text_start;

	private DateTime text_complete;

	private Text text_function;

	public ProjectManagerCpt(Composite parent, int style, CTabFolder tabFolder) {
		super(tabFolder, style);

		CTabItem tabItem_3 = new CTabItem(tabFolder, SWT.NONE);
		tabItem_3.setText("��Ŀ����");
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;

		setLayout(gridLayout);
		
		Label label_2 = new Label(this, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		label_2.setText("��������");

		text_function = new Text(this, SWT.BORDER);
		text_function.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
		
		label_2 = new Label(this, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		label_2.setText("������");

		text_author = new Text(this, SWT.BORDER);
		text_author.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		label_2 = new Label(this, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		label_2.setText("�汾��");

		text_version = new Text(this, SWT.BORDER);
		text_version.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		label_2 = new Label(this, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		label_2.setText("��ʼʱ��(y-m-d)");

		text_start = new DateTime(this, SWT.BORDER);
		text_start.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));

		label_2 = new Label(this, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		label_2.setText("Ԥ�����ʱ��(y-m-d)");

		text_complete = new DateTime(this, SWT.BORDER);
		text_complete.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false));

		label_2 = new Label(this, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		label_2.setText("��ǰ���(%)");

		text_percent = new Text(this, SWT.BORDER);
		text_percent.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		Label label_1 = new Label(this, SWT.NONE);
		label_1.setText("����޸�ʱ��");

		text_updateTime = new DateTime(this, SWT.BORDER);
		final GridData gd_text_createTime = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		text_updateTime.setLayoutData(gd_text_createTime);

		label_2 = new Label(this, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 4, 1));
		label_2.setText("�޸ļ�¼");

		text_progress = new Text(this, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_text_desc = new GridData(SWT.FILL, SWT.FILL, true, true,4,1);
		text_progress.setLayoutData(gd_text_desc);
		
		tabItem_3.setControl(this);
		
	}
	
	SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
	
	public ManagementInfo fetchData() {
		ManagementInfo info = new ManagementInfo();
		info.author = this.text_author.getText();
		info.fName = this.text_function.getText();
		info.version = this.text_version.getText();
		info.percent = this.text_percent.getText();
		info.startDate = f.format(new Date(this.text_start.getYear(),this.text_start.getMonth(), this.text_start.getDay())); 
		info.completeDate = f.format(new Date(this.text_complete.getYear(),this.text_complete.getMonth(), this.text_complete.getDay())); 
		info.updateDate = f.format(new Date(this.text_updateTime.getYear(),this.text_updateTime.getMonth(), this.text_updateTime.getDay())); 
		info.log = text_progress.getText();
		return info;
	}
	
	public void updateData(ManagementInfo mInfo) {
		if(!StringUtils.isEmpty(mInfo.author))
			text_author.setText(mInfo.author);
		if(!StringUtils.isEmpty(mInfo.fName))
			text_function.setText(mInfo.fName);
		if(!StringUtils.isEmpty(mInfo.percent))
			text_percent.setText(mInfo.percent);
		if(!StringUtils.isEmpty(mInfo.version))
			text_version.setText(mInfo.version);
		if(!StringUtils.isEmpty(mInfo.startDate))
		{
			try{
				Date d = f.parse(mInfo.startDate);
				text_start.setYear(d.getYear());
				text_start.setMonth(d.getMonth());
				text_start.setDay(d.getDate());
			}catch(Exception e){}
		}
		if(!StringUtils.isEmpty(mInfo.updateDate))
		{
			try{
				Date d = f.parse(mInfo.updateDate);
				text_updateTime.setYear(d.getYear());
				text_updateTime.setMonth(d.getMonth());
				text_updateTime.setDay(d.getDate());
			}catch(Exception e){}
		}
		if(!StringUtils.isEmpty(mInfo.completeDate))
		{
			try{
				Date d = f.parse(mInfo.completeDate);
				text_complete.setYear(d.getYear());
				text_complete.setMonth(d.getMonth());
				text_complete.setDay(d.getDate());
			}catch(Exception e){}
		}
		if(!StringUtils.isEmpty(mInfo.log))
			text_progress.setText(mInfo.log);
		
	}

}
