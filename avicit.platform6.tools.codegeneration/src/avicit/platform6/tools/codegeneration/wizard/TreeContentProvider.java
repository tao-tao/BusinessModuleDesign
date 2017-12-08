package avicit.platform6.tools.codegeneration.wizard;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import avicit.platform6.tools.codegeneration.CodeGenerationActivator;
import avicit.platform6.tools.codegeneration.define.ConstDefine;
import avicit.platform6.tools.codegeneration.log.AvicitLogger;
import avicit.platform6.tools.codegeneration.util.SvnFilenameFilter;

public class TreeContentProvider implements ITreeContentProvider{

	
	 /**
     * 由这个方法决定树的顶级显示那些对象。在此方法里生成了三个Country对象
     * @param inputElement  用tv.setInput()方法输入的那个对象，在这里没有使用这个对象
     */
    public Object[] getElements(Object inputElement) {
    	
    	/**
    	 * 从属性文件中读取
    	 */
    	List<TreeDataConfigObject> treeList = new ArrayList<TreeDataConfigObject>();
    	
    	URL tplUrl = CodeGenerationActivator.getDefault().getBundle().getEntry(ConstDefine.TEMPLATE_BASE_PATH);
    	
    	String[] tpls = null;
		try {
			tplUrl = FileLocator.toFileURL(tplUrl);
			File tplFile = new File(tplUrl.toString().substring(6));
			tpls = tplFile.list(new SvnFilenameFilter());
		} catch (IOException e) {
			AvicitLogger.openError(e);
		}
		for(String name:tpls){
			TreeDataConfigObject chiledObject = new TreeDataConfigObject(name,name);
			treeList.add(chiledObject);
		}
		TreeDataConfigObject chiledObject = new TreeDataConfigObject("singleTabletree", "singleTabletree");
		treeList.add(chiledObject);
    	TreeDataConfigObject parentObject = new TreeDataConfigObject("parent", "模板", treeList);
    	TreeDataConfigObject[] obj = new TreeDataConfigObject[1];
        obj[0] = parentObject;
        return obj;
    }

    /**
     * 由这个方法决定结点应该显示那些子结点。在这里也不管父结点是什么，每个结点都统一有三个字结点
     * @param parentElement　被点击的结点(父结点)
     */
    public Object[] getChildren(Object parentElement) {
    	TreeDataConfigObject parentObject = (TreeDataConfigObject) parentElement;
        return parentObject.getChildenTree().toArray();
    }

    /**
     * 判断某结点是否有子结点。在这里不管3721，全返回
     * 真，即都有子结点。这时结点前都有一个“＋”号图标
      * @param element 需要判断是否有子的结点
     */
    
        public boolean hasChildren(Object element) {
           
         return true;
        }

        /**
         * 取得某结点的父结点。极少需要实现此方法
         */
        public Object getParent(Object element) {
            return null;
        }

        /**
         * 当TreeViewer被销毁时将执行这个方法。极少需要实现此方法
         */
        public void dispose() {}

        /**
         * 当tv.setInput()发生变化时调用此方法。极少需要实现此方法
         */
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}


}
