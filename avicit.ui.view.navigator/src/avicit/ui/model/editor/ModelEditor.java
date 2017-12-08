package avicit.ui.model.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import avicit.ui.model.browser.ModelBrowser;
import avicit.ui.runtime.core.requirement.analysis.epc.EpcNode;

public class ModelEditor extends EditorPart {
	
	private ModelBrowser modelBrowser = new ModelBrowser();
	
	public ModelEditor(){
		super();
	}

	@Override
	public void doSave(IProgressMonitor arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.setInput(input);
		this.setSite(site);		
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		String url = EpcNode.MODLE_EDITOR_URL;
		modelBrowser.configContents(parent, url);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
