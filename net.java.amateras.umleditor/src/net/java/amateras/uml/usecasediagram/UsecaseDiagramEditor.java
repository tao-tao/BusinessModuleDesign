package net.java.amateras.uml.usecasediagram;

import net.java.amateras.uml.DiagramEditor;
import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.classdiagram.figure.UMLClassFigure;
import net.java.amateras.uml.model.AnchorModel;
import net.java.amateras.uml.model.NoteModel;
import net.java.amateras.uml.model.RootModel;
import net.java.amateras.uml.usecasediagram.action.CopyAction;
import net.java.amateras.uml.usecasediagram.action.OpenSourceAction;
import net.java.amateras.uml.usecasediagram.action.PasteAction;
import net.java.amateras.uml.usecasediagram.edit.UsecaseEditPartFactory;
import net.java.amateras.uml.usecasediagram.model.SystemModel;
import net.java.amateras.uml.usecasediagram.model.UsecaseActorModel;
import net.java.amateras.uml.usecasediagram.model.UsecaseExtendModel;
import net.java.amateras.uml.usecasediagram.model.UsecaseGeneralizationModel;
import net.java.amateras.uml.usecasediagram.model.UsecaseIncludeModel;
import net.java.amateras.uml.usecasediagram.model.UsecaseModel;
import net.java.amateras.uml.usecasediagram.model.UsecaseRelationModel;
import net.java.amateras.uml.usecasediagram.model.UsecaseRootModel;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;

/**
 * GEF���g�p����UML�i���[�X�P�[�X�_�C�A�O�����j�G�f�B�^�B
 * 
 * @author Takahiro Shida.
 */
public class UsecaseDiagramEditor extends DiagramEditor {
	
	private CopyAction copyAction;
	private PasteAction pasteAction;
	private OpenSourceAction open;
	
	protected RootModel createInitializeModel() {
		UsecaseRootModel model = new UsecaseRootModel();
		model.setShowIcon(true);
		model.setBackgroundColor(UMLClassFigure.classColor.getRGB());
		model.setForegroundColor(ColorConstants.black.getRGB());
		return model;
	}

	protected String getDiagramType() {
		return "usecase";
	}

	protected EditPartFactory createEditPartFactory() {
		return new UsecaseEditPartFactory();
	}

	protected void createDiagramAction(GraphicalViewer viewer) {
		
	}

	protected void fillDiagramPopupMenu(MenuManager manager) {
		manager.add(new Separator("copy"));
		manager.add(copyAction);
		manager.add(pasteAction);
		manager.add(open);
	}

	protected void updateDiagramAction(ISelection selection) {
		
	}

	protected void createActions() {
		super.createActions();
		pasteAction = new PasteAction(this);
		getActionRegistry().registerAction(pasteAction);
		getSelectionActions().add(pasteAction.getId());
		
		copyAction = new CopyAction(this, pasteAction);
		getActionRegistry().registerAction(copyAction);
		getSelectionActions().add(copyAction.getId());
		
		pasteAction = new PasteAction(this);
		getActionRegistry().registerAction(pasteAction);
		getSelectionActions().add(pasteAction.getId());
		
		open = new OpenSourceAction(this);
		getActionRegistry().registerAction(open);
		getSelectionActions().add(open.getId());
		
	}
	
	protected PaletteRoot getPaletteRoot() {
		PaletteRoot root = new PaletteRoot();
		UMLPlugin plugin = UMLPlugin.getDefault();

		// ���f���쐬�ȊO�̃c�[�����i�[����O���[�v
		PaletteGroup tools = new PaletteGroup(plugin
				.getResourceString("palette.tool"));
		// '�I��' �c�[���̍쐬�ƒǉ�
		ToolEntry tool = new SelectionToolEntry();
		tools.add(tool);
		root.setDefaultEntry(tool);
		// '�͂ݘg' �c�[���̍쐬�ƒǉ�
		tool = new MarqueeToolEntry();
		tools.add(tool);

		PaletteDrawer common = new PaletteDrawer(plugin
				.getResourceString("palette.common"));
		common.add(createEntityEntry(plugin
				.getResourceString("palette.common.note"), NoteModel.class,
				"icons/note.gif"));
		common.add(createConnectionEntry(plugin
				.getResourceString("palette.common.anchor"), AnchorModel.class,
				"icons/anchor.gif"));

		// ���f���̍쐬���s���c�[�����i�[����O���[�v
		PaletteDrawer entities = new PaletteDrawer(plugin
				.getResourceString("palette.entity"));
		entities.add(createEntityEntry(plugin
				.getResourceString("palette.usecase.actor"), UsecaseActorModel.class,
				"icons/actor16.gif"));
		entities.add(createEntityEntry(plugin
				.getResourceString("palette.usecase.usecase"),
				UsecaseModel.class, "icons/usecase.gif"));

		entities.add(createEntityEntry(plugin
				.getResourceString("palette.usecase.system"),
				SystemModel.class, "icons/system.gif"));

		PaletteDrawer relations = new PaletteDrawer(plugin
				.getResourceString("palette.relation"));
		relations.add(createConnectionEntry(plugin
				.getResourceString("palette.usecase.relation"),
				UsecaseRelationModel.class, "icons/relation.gif"));
		relations.add(createConnectionEntry(plugin
				.getResourceString("palette.usecase.generalization"),
				UsecaseGeneralizationModel.class, "icons/generalization.gif"));
		relations.add(createConnectionEntry(plugin
				.getResourceString("palette.usecase.include"),
				UsecaseIncludeModel.class, "icons/dependency.gif"));
		relations.add(createConnectionEntry(plugin
				.getResourceString("palette.usecase.extend"),
				UsecaseExtendModel.class, "icons/dependency.gif"));

		// �쐬����3�̃O���[�v�����[�g�ɒǉ�
		root.add(tools);
		root.add(common);
		root.add(entities);
		root.add(relations);

		return root;
	}
}
