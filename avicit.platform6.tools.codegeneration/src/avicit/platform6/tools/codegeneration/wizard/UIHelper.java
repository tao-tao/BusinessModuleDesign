package avicit.platform6.tools.codegeneration.wizard;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class UIHelper {

	public static void removeTalbeSelection(TableViewer tv) {
		StructuredSelection ss = (StructuredSelection) tv.getSelection();
		if (ss != null && !ss.isEmpty()) {
			Object objs[] = ss.toArray();
			for(int i=0; i<objs.length; i++)
				tv.remove(objs[i]);
		}
	}
	public static List<ListDataField> getDatasetFromTableData(Table table) {
		List data = new ArrayList();
		TableItem[] its = table.getItems();
		for (int i = 0; i < its.length; i++) {
			ListDataField mp = (ListDataField) its[i].getData();
			data.add(mp);
		}
		return data;
	}

}
