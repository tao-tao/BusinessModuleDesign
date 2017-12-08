package com.jvisio.flowchart.diagram.part;

import java.util.List;

import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.ui.IWorkbenchPage;

public class SaveImageAction extends DiagramAction{

	public SaveImageAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Request createTargetRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isSelectionListener() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public List getOperationSetTemp(){
		return super.getOperationSet();
	}
	
	public DiagramEditPart getDiagramEditPart() {
		IDiagramWorkbenchPart part = getDiagramWorkbenchPart();
		return part != null ? part.getDiagramEditPart()
			: null;
	}
}
