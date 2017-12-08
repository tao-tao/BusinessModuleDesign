package avicit.ui.runtime.core.action.wizard;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import net.java.amateras.uml.UMLPlugin;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class NewJspWizardPage extends WizardNewFileCreationPage {
	
	public NewJspWizardPage(ISelection selection) {
		super("wizardPage",(IStructuredSelection)selection);
		setTitle(UMLPlugin.getDefault().getResourceString("wizard.newClassDiagram.title"));
		setDescription(UMLPlugin.getDefault().getResourceString("wizard.newClassDiagram.description"));
	}
	
	public void createControl(Composite parent) {
		super.createControl(parent);
		this.setFileName("newfile.jsp");
	}
	
	protected InputStream getInitialContents() {
		try {
			StringBuilder html = new StringBuilder();
			html.append("<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>");
			html.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
			html.append("<html>\n");
			html.append("<head>\n");
			html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
			html.append("<title>Insert title here</title>\n");
			html.append("</head>\n");
			html.append("<body>\n");

			html.append("</body>\n");
			html.append("</html>");
			InputStream htmlStream = new ByteArrayInputStream(html.toString().getBytes("UTF-8"));
			return htmlStream;
		} catch(UnsupportedEncodingException ex){
			return null;
		}
	}


}
