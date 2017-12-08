package avicit.ui.view.navigator;


import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

//import com.tansun.runtime.ui.ConsoleFactory;
public class AvicitPerspectiveFactory implements IPerspectiveFactory {
	
	    public AvicitPerspectiveFactory()
	    {
	    }

	    public void createInitialLayout(IPageLayout layout)
	    {
	        String editorArea = layout.getEditorArea();
	        IFolderLayout folder = layout.createFolder("left", 1, 0.25F, editorArea);
	        folder.addView("com.avicit.platform");
	        folder.addView("org.eclipse.jdt.ui.PackageExplorer");
	        folder.addView("org.eclipse.jdt.ui.TypeHierarchy");
	        folder.addView("org.eclipse.ui.views.ContentOutline");
	        folder.addPlaceholder("org.eclipse.ui.views.ResourceNavigator");
	        IFolderLayout outputfolder = layout.createFolder("bottom", 4, 0.75F, editorArea);
	        outputfolder.addView("org.eclipse.ui.views.ProblemView");
	        outputfolder.addView("org.eclipse.jdt.ui.JavadocView");
	        outputfolder.addView("org.eclipse.jdt.ui.SourceView");
	        outputfolder.addPlaceholder("org.eclipse.search.ui.views.SearchView");
	        outputfolder.addPlaceholder("org.eclipse.ui.console.ConsoleView");
	        outputfolder.addPlaceholder("org.eclipse.ui.views.BookmarkView");
	        outputfolder.addPlaceholder("org.eclipse.ui.views.ProgressView");
	        layout.addActionSet("org.eclipse.debug.ui.launchActionSet");
	        layout.addActionSet("org.eclipse.jdt.ui.JavaActionSet");
	        layout.addActionSet("org.eclipse.jdt.ui.JavaElementCreationActionSet");
	        layout.addActionSet("org.eclipse.ui.NavigateActionSet");
	        layout.addShowViewShortcut("org.eclipse.jdt.ui.PackageExplorer");
	        layout.addShowViewShortcut("org.eclipse.jdt.ui.TypeHierarchy");
	        layout.addShowViewShortcut("org.eclipse.jdt.ui.SourceView");
	        layout.addShowViewShortcut("org.eclipse.jdt.ui.JavadocView");
	        layout.addShowViewShortcut("org.eclipse.search.ui.views.SearchView");
	        layout.addShowViewShortcut("org.eclipse.ui.console.ConsoleView");
	        layout.addShowViewShortcut("org.eclipse.ui.views.ContentOutline");
	        layout.addShowViewShortcut("org.eclipse.ui.views.ProblemView");
	        layout.addShowViewShortcut("org.eclipse.ui.views.ResourceNavigator");
	        layout.addShowViewShortcut("org.eclipse.ui.views.TaskList");
	        layout.addShowViewShortcut("org.eclipse.ui.views.ProgressView");
	        layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.JavaProjectWizard");
	        layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewPackageCreationWizard");
	        layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewClassCreationWizard");
	        layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewInterfaceCreationWizard");
	        layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewEnumCreationWizard");
	        layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewAnnotationCreationWizard");
	        layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewSourceFolderCreationWizard");
	        layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewSnippetFileCreationWizard");
	        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
	        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");
	        layout.addNewWizardShortcut("org.eclipse.ui.editors.wizards.UntitledTextFileWizard");
	        
	       /* ConsoleFactory consoleFactory = new ConsoleFactory();  
	        consoleFactory.openConsole();  */

	}

}
