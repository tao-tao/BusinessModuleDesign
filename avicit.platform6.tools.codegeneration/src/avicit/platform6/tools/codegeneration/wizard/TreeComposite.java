package avicit.platform6.tools.codegeneration.wizard;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 
 * Title		: TreeComposite.java
 * 类描述       :xxxxxx 
 * 作者         : lidong@avicit.com  
 * 创建时间     : 2015-7-26 下午5:27:09
 * 版本         : 1.00
 * 
 * 修改记录: 
 * 版本            修改人          修改时间                  修改内容描述
 * ----------------------------------------
 * 1.00     xx          2015-7-26 下午5:27:09
 * ----------------------------------------
 */
public class TreeComposite extends SashForm  {

	
	TreeViewer treeViewer;
	Menu me;
	private Tree tree;
	public TreeComposite(Composite parent, int style) {
		super(parent, style);
		Composite testcomposite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 1420;
		testcomposite.setLayoutData(data);
		testcomposite.setLayout(gl);
		
		treeViewer = new TreeViewer(testcomposite, SWT.BORDER);
		
		tree = treeViewer.getTree();

		org.eclipse.jface.action.MenuManager ma = new org.eclipse.jface.action.MenuManager();
		
		ma.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager arg0) {
				arg0.removeAll();
				TreeItem[] items = tree.getSelection();
				if (items != null && items.length > 0) {
					Object o = items[0].getData();
					
				}
			}
		});
		
		me = ma.createContextMenu(tree);
		tree.setMenu(me);

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
//				onTreeSelected();
			}
		});
		
		treeViewer.setContentProvider(new TreeContentProvider());
		treeViewer.setLabelProvider(new TreeLableProvider());
		treeViewer.setInput(new Object());//

	
//		this.setWeights(new int[] { 25, 75 });
	}

}
