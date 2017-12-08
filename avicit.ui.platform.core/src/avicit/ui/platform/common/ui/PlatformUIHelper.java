package avicit.ui.platform.common.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import avicit.ui.platform.common.IPlatformConstants;
import avicit.ui.platform.common.util.WebrootUtil;
import avicit.ui.platform.common.util.WebrootUtil.Field;

public class PlatformUIHelper {

	public static List listDataInTable(Table table) {
		List data = new ArrayList();

		TableItem[] ti = table.getItems();
		for (int i = 0; i < ti.length; i++) {
			TableItem it = ti[i];
			data.add(it.getData());
		}
		return data;
	}

	public static void moveUpTableSelection(TableViewer tv) {
		Table ta = tv.getTable();
		if(ta.getSelectionCount()==0) return;
		if(ta.getItemCount()<2) return;
		
		int si = ta.getSelectionIndex();
		if(si==0) return;
		
		TableItem sitem = ta.getItem(si);
		Object data = sitem.getData();
		TableItem beforeitem = ta.getItem(si-1);
		
		swapTableItem(tv,sitem,beforeitem);
		
		tv.setSelection(new StructuredSelection(data));
		

	}

	private static void swapTableItem(TableViewer tv,TableItem sitem, TableItem beforeitem) {

		Object sdata = sitem.getData();
		Object bdata = beforeitem.getData();
		sitem.setData(bdata);
		
		beforeitem.setData(sdata);
		
		tv.refresh(sdata);
		tv.refresh(bdata);
		
		
	}

	public static void moveDownTableSelection(TableViewer tv) {
		Table ta = tv.getTable();
		if(ta.getSelectionCount()==0) return;
		if(ta.getItemCount()<2) return;
		
		int si = ta.getSelectionIndex();
		if(si==ta.getItemCount()-1) return;
		
		TableItem sitem = ta.getItem(si);
		Object data = sitem.getData();
		TableItem after = ta.getItem(si+1);
		
		swapTableItem(tv,sitem,after);
		
		tv.setSelection(new StructuredSelection(data));
		
	}
	
	
	public static void updateRowdataInTable(TableViewer tv,int index,Object data){
		TableItem item = tv.getTable().getItem(index);
		item.setData(data);
		tv.refresh(data);
	}

	public static void onFormName_Select(Text text) {
		IProject project = WebrootUtil.getProject();
		WebPageSelectionDialog dlg = new WebPageSelectionDialog(
				text.getShell(), project);
		dlg.setTitle("ѡ��һ��Web�ļ�");
		if (dlg.open() == Window.OK) {
			IFile selectedFile = dlg.getResultFile();
			if (null == selectedFile)
				return;
			String result = "";
			IPath path = selectedFile.getProjectRelativePath();
			int rcount = IPlatformConstants.Path_WebPage.segmentCount();
			path = path.removeFirstSegments(rcount);
			result = path.toString();
			text.setText(result);
		}
	}

	public static boolean onPageResource_Select(IFolder container, Text text,String[] suffixs) {
		WebPageSelectionDialog dlg = new WebPageSelectionDialog(
				text.getShell(), container, SWT.NONE);
		if(null!=suffixs){
			dlg.setSuffixs(suffixs);
		}
		
		dlg.setTitle("ѡ��һ���ļ�");
		if (dlg.open() == Window.OK) {
			IFile selectedFile = dlg.getResultFile();
			if (null == selectedFile)
				return false;
			String result = "";
			IPath path = selectedFile.getProjectRelativePath();

			int rcount = container.getProjectRelativePath().segmentCount();
			path = path.removeFirstSegments(rcount);
			result = path.toString();
			text.setText(result);
			return true;
		}else{
			return false;
		}
	}

	public static Object openDialogBox(Control parent, String typeName,
			String title, String message) {
		try {
			ChooseClassDialog dialog = new ChooseClassDialog(parent.getShell(),
					typeName, title, message);
			return dialog.openDialog();
		} catch (Exception e) {
			throw new RuntimeException(e);
			// return null;
		}
	}

	public static void onPojoClass_Select(Text text) {
		Object result = openDialogBox(text, "", "ҵ����", "��ѡ��һ��ҵ����");
		if (result != null)
			text.setText(result.toString());
	}
	
	public static void removeTalbeSelections(TableViewer tv) {
		StructuredSelection ss = (StructuredSelection) tv.getSelection();
		if (ss != null && !ss.isEmpty()) {
			tv.remove(ss.toArray());
		}
	}

	public static void onActionButtons_Select(Text text_buttons) {
		ActionButtonSelectionDialog dia = new ActionButtonSelectionDialog(text_buttons.getShell(),null,SWT.NONE);
		if(dia.open()==IDialogConstants.OK_ID){
			Object[] dataa = dia.getResult();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < dataa.length; i++) {
				Field fie=(Field) dataa[i];
				String str = fie.name;
				if(i==0)
					sb.append(str);
				else
					sb.append(",").append(str);
			}
			String te = text_buttons.getText();
			if(te.equals("") || te.endsWith(","))
				text_buttons.setText(te + sb.toString());
			else
				text_buttons.setText(te + "," + sb.toString());
		}		
	}
}
