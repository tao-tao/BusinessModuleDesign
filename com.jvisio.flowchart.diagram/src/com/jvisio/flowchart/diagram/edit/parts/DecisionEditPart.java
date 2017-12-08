package com.jvisio.flowchart.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

import com.jvisio.flowchart.diagram.edit.policies.DecisionCanonicalEditPolicy;
import com.jvisio.flowchart.diagram.edit.policies.DecisionGraphicalNodeEditPolicy;
import com.jvisio.flowchart.diagram.edit.policies.DecisionItemSemanticEditPolicy;
import com.jvisio.flowchart.diagram.part.FlowchartVisualIDRegistry;

/**
 * @generated
 */
public class DecisionEditPart extends ShapeNodeEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 1003;

	/**
	 * @generated
	 */
	protected IFigure contentPane;

	/**
	 * @generated
	 */
	protected IFigure primaryShape;

	/**
	 * @generated
	 */
	public DecisionEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new DecisionItemSemanticEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new DecisionGraphicalNodeEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new DecisionCanonicalEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
	}

	/**
	 * @generated
	 */
	protected LayoutEditPolicy createLayoutEditPolicy() {
		LayoutEditPolicy lep = new LayoutEditPolicy() {

			protected EditPolicy createChildEditPolicy(EditPart child) {
				EditPolicy result = child
						.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
				if (result == null) {
					result = new NonResizableEditPolicy();
				}
				return result;
			}

			protected Command getMoveChildrenCommand(Request request) {
				return null;
			}

			protected Command getCreateCommand(CreateRequest request) {
				return null;
			}
		};
		return lep;
	}

	/**
	 * @generated
	 */
	protected IFigure createNodeShape() {
		DecisionFigure figure = new DecisionFigure();
		return primaryShape = figure;
	}

	/**
	 * @generated
	 */
	public DecisionFigure getPrimaryShape() {
		return (DecisionFigure) primaryShape;
	}

	/**
	 * @generated
	 */
	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof DecisionNameEditPart) {
			((DecisionNameEditPart) childEditPart).setLabel(getPrimaryShape()
					.getFigureDecisionNameFigure());
			return true;
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected boolean removeFixedChild(EditPart childEditPart) {
		return false;
	}

	/**
	 * @generated
	 */
	protected NodeFigure createNodePlate() {
		return new DefaultSizeNodeFigure(getMapMode().DPtoLP(40), getMapMode()
				.DPtoLP(40));
	}

	/**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model
	 * so you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated
	 */
	protected NodeFigure createNodeFigure() {
		NodeFigure figure = createNodePlate();
		figure.setLayoutManager(new StackLayout());
		IFigure shape = createNodeShape();
		figure.add(shape);
		contentPane = setupContentPane(shape);
		return figure;
	}

	/**
	 * Default implementation treats passed figure as content pane.
	 * Respects layout one may have set for generated figure.
	 * @param nodeShape instance of generated figure class
	 * @generated
	 */
	protected IFigure setupContentPane(IFigure nodeShape) {
		if (nodeShape.getLayoutManager() == null) {
			ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
			layout.setSpacing(getMapMode().DPtoLP(5));
			nodeShape.setLayoutManager(layout);
		}
		return nodeShape; // use nodeShape itself as contentPane
	}

	/**
	 * @generated
	 */
	public IFigure getContentPane() {
		if (contentPane != null) {
			return contentPane;
		}
		return super.getContentPane();
	}

	/**
	 * @generated
	 */
	public EditPart getPrimaryChildEditPart() {
		return getChildBySemanticHint(FlowchartVisualIDRegistry
				.getType(DecisionNameEditPart.VISUAL_ID));
	}

	/**
	 * @generated
	 */
	protected void addChildVisual(EditPart childEditPart, int index) {
		if (addFixedChild(childEditPart)) {
			return;
		}
		super.addChildVisual(childEditPart, -1);
	}

	/**
	 * @generated
	 */
	protected void removeChildVisual(EditPart childEditPart) {
		if (removeFixedChild(childEditPart)) {
			return;
		}
		super.removeChildVisual(childEditPart);
	}

	/**
	 * @generated
	 */
	public class DecisionFigure extends
			com.jvisio.flowchart.diagram.custom.figures.DecisionFigure {

		/**
		 * @generated
		 */
		public DecisionFigure() {

			org.eclipse.draw2d.BorderLayout myGenLayoutManager = new org.eclipse.draw2d.BorderLayout();

			this.setLayoutManager(myGenLayoutManager);

			this.setForegroundColor(org.eclipse.draw2d.ColorConstants.black

			);
			this.setBackgroundColor(DECISIONFIGURE_BACK

			);
			this.setPreferredSize(getMapMode().DPtoLP(100), getMapMode()
					.DPtoLP(40));
			this.setMinimumSize(new org.eclipse.draw2d.geometry.Dimension(
					getMapMode().DPtoLP(100), getMapMode().DPtoLP(40)));

			createContents();
		}

		/**
		 * @generated
		 */
		private void createContents() {
			org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel fig_0 = new org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel();

			fig_0.setBorder(new org.eclipse.draw2d.MarginBorder(getMapMode()
					.DPtoLP(5), getMapMode().DPtoLP(10),
					getMapMode().DPtoLP(5), getMapMode().DPtoLP(10)));

			setFigureDecisionNameFigure(fig_0);

			Object layData0 = org.eclipse.draw2d.BorderLayout.CENTER;

			this.add(fig_0, layData0);
		}

		/**
		 * @generated
		 */
		private org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel fDecisionNameFigure;

		/**
		 * @generated
		 */
		public org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel getFigureDecisionNameFigure() {
			return fDecisionNameFigure;
		}

		/**
		 * @generated
		 */
		private void setFigureDecisionNameFigure(
				org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel fig) {
			fDecisionNameFigure = fig;
		}

		/**
		 * @generated
		 */
		private boolean myUseLocalCoordinates = false;

		/**
		 * @generated
		 */
		protected boolean useLocalCoordinates() {
			return myUseLocalCoordinates;
		}

		/**
		 * @generated
		 */
		protected void setUseLocalCoordinates(boolean useLocalCoordinates) {
			myUseLocalCoordinates = useLocalCoordinates;
		}

	}

	/**
	 * @generated
	 */
	public static final org.eclipse.swt.graphics.Color DECISIONFIGURE_BACK = new org.eclipse.swt.graphics.Color(
			null, 255, 240, 180);

}
