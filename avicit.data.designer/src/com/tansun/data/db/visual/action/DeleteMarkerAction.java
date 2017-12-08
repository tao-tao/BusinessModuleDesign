package com.tansun.data.db.visual.action;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

import com.tansun.data.db.DBPlugin;
import com.tansun.data.db.util.UIUtils;
import com.tansun.data.db.visual.model.AbstractDBEntityModel;
import com.tansun.data.db.visual.model.RootModel;
import com.tansun.data.db.visual.model.TableModel;

/**
 * 
 * @author Naoki Takezoe
 */
public class DeleteMarkerAction extends Action {

    private GraphicalViewer viewer;

    public DeleteMarkerAction(GraphicalViewer viewer) {
        super(DBPlugin.getResourceString("action.validation.deleteMarkers"));
        this.viewer = viewer;
    }

    @Override public void run() {
        CommandStack stack = viewer.getEditDomain().getCommandStack();
        stack.execute(new Command("Delete markers") {
            @Override public void execute() {
                RootModel model = (RootModel) viewer.getRootEditPart().getContents().getModel();
                for (AbstractDBEntityModel entity : model.getChildren()) {
                    if (entity instanceof TableModel) {
                        ((TableModel) entity).setError("");
                    }
                }
            }

            @Override public boolean canUndo() {
                return false;
            }
        });

        IEditorInput input = UIUtils.getActiveEditor().getEditorInput();
        if (input instanceof IFileEditorInput) {
            IFile file = ((IFileEditorInput) input).getFile();
            try {
                file.deleteMarkers(IMarker.PROBLEM, false, 0);
            } catch (CoreException ex) {
                DBPlugin.logException(ex);
            }
        }
    }

}
