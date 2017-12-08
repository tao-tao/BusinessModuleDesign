package avicit.ui.runtime.core.action.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.IIDEHelpContextIds;
import org.eclipse.ui.model.WorkbenchContentProvider;

public class ExportDocumentWizardPage extends WizardPage {

	private IStructuredSelection selection;
	IProject project;

	protected ExportDocumentWizardPage(String pageName) {
		super(pageName);
	}

	protected ExportDocumentWizardPage(String pageName, IStructuredSelection selection){
		super(pageName);
		this.selection = selection;
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		
		Composite topLevel = new Composite(parent, SWT.NONE);
		topLevel.setLayout(new GridLayout());
		topLevel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
				| GridData.HORIZONTAL_ALIGN_FILL));
		topLevel.setFont(parent.getFont());
		PlatformUI.getWorkbench().getHelpSystem().setHelp(topLevel,
				IIDEHelpContextIds.NEW_FILE_WIZARD_PAGE);

		List input = new ArrayList<Object>();

        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

        for (int i = 0; i < projects.length; i++) {
            if (projects[i].isOpen()) {
				input.add(projects[i]);
            }
        }

        TreeViewer treeViewer = new TreeViewer(topLevel);
		treeViewer.setLabelProvider(createTreeLabelProvider());
		treeViewer.setContentProvider(createTreeContentProvider());
		treeViewer.setInput(projects);
		treeViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						ISelection selection = event.getSelection();
						if (!selection.isEmpty()) {
							IStructuredSelection sel = (IStructuredSelection) selection;
							project = (IProject) sel.getFirstElement();
						}

						setPageComplete(canFlipToNextPage());
					}
				});
	}
//        TreeViewer treeViewer = new TreeViewer(topLevel);

		private ILabelProvider createTreeLabelProvider()
		{
			return new LabelProvider() {
				@Override
				public String getText(Object element) {
					// IWorkbenchAdapter adapter = (IWorkbenchAdapter) Platform
					// .getAdapterManager().getAdapter(element,
					// IWorkbenchAdapter.class);
					// return adapter.getLabel(element);
					if (element instanceof IProject) {
						IProject pro = (IProject) element;
						return pro.getName();
					}

					return super.getText(element);
				}

				@Override
				public Image getImage(Object element) {
					// JavaElementImageProvider.getDecoratedImage(JavaPluginImages.DESC_MISC_PUBLIC,
					// 0, JavaElementImageProvider.SMALL_SIZE);
					// JavaUI.getSharedImages().getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_JAR);
					Image image = PlatformUI
							.getWorkbench()
							.getSharedImages()
							.getImage(
									org.eclipse.ui.ide.IDE.SharedImages.IMG_OBJ_PROJECT);
					return image;
				}
			};
		}

		private IContentProvider createTreeContentProvider() 
		{
			return new WorkbenchContentProvider() {
				@Override
				public Object[] getElements(Object inputElement) {
					if (inputElement instanceof List) {
						return ((List) inputElement).toArray();
					}

					return new Object[0];
				}

				@Override
				public boolean hasChildren(Object element) {
					return false;
				}
			};
		}

}
