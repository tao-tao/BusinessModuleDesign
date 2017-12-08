package avicit.ui.view.create;


import org.eclipse.core.internal.resources.Project;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ComponentManagerCreateAction extends ModulCreate {

	public ComponentManagerCreateAction(String text, Project obj, int flag) {
		super(text, obj, flag);
		// TODO Auto-generated constructor stub
		//Pmodel model = com.tansun.ec.create.ECproject.getConfig();
		//String str = model.getMentypes();
		//if (str.contains("component")) {
			this.setDialogTitle("新建组件");
			this.setImages(new Image(Display.getCurrent(), getClass()
					.getResourceAsStream("/icons/eview16/component_dialog.png")));
//		} else {
//			this.showMessage("��֧�ִ���������ģ��");
//		}
	}

}