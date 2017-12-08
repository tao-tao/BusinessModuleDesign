package com.jvisio.flowchart.diagram.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeConnectionTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeCreationTool;
import org.eclipse.jface.resource.ImageDescriptor;

import com.jvisio.flowchart.diagram.providers.FlowchartElementTypes;

/**
 * @generated
 */
public class FlowchartPaletteFactory {

	/**
	 * @generated
	 */
	public void fillPalette(PaletteRoot paletteRoot) {
		paletteRoot.add(createflowchart1Group());
	}

	/**
	 * @generated
	 */
	private PaletteContainer createflowchart1Group() {
		PaletteContainer paletteContainer = new PaletteGroup("flowchart");
		paletteContainer.add(createTerminator1CreationTool());
		paletteContainer.add(createAction2CreationTool());
		paletteContainer.add(createDecision3CreationTool());
		paletteContainer.add(createSubprocess4CreationTool());
		paletteContainer.add(createConnector5CreationTool());
		paletteContainer.add(createWaitbox6CreationTool());
		paletteContainer.add(createTransition7CreationTool());
//		paletteContainer.add(createTransition8CreationTool());
		return paletteContainer;
	}

	/**
	 * @generated
	 */
	private ToolEntry createTerminator1CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = FlowchartElementTypes
				.getImageDescriptor(FlowchartElementTypes.Terminator_1001);

		largeImage = smallImage;

		final List elementTypes = new ArrayList();
		elementTypes.add(FlowchartElementTypes.Terminator_1001);
		ToolEntry result = new NodeToolEntry("Terminator",
				"Create new Terminator", smallImage, largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createAction2CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = FlowchartElementTypes
				.getImageDescriptor(FlowchartElementTypes.Action_1002);

		largeImage = smallImage;

		final List elementTypes = new ArrayList();
		elementTypes.add(FlowchartElementTypes.Action_1002);
		ToolEntry result = new NodeToolEntry("Action", "Create new Action",
				smallImage, largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createDecision3CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = FlowchartElementTypes
				.getImageDescriptor(FlowchartElementTypes.Decision_1003);

		largeImage = smallImage;

		final List elementTypes = new ArrayList();
		elementTypes.add(FlowchartElementTypes.Decision_1003);
		ToolEntry result = new NodeToolEntry("Decision", "Create new Decision",
				smallImage, largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createSubprocess4CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = FlowchartElementTypes
				.getImageDescriptor(FlowchartElementTypes.Subprocess_1004);

		largeImage = smallImage;

		final List elementTypes = new ArrayList();
		elementTypes.add(FlowchartElementTypes.Subprocess_1004);
		ToolEntry result = new NodeToolEntry("Subprocess",
				"Create new Subprocess", smallImage, largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createConnector5CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = FlowchartElementTypes
				.getImageDescriptor(FlowchartElementTypes.Connector_1005);

		largeImage = smallImage;

		final List elementTypes = new ArrayList();
		elementTypes.add(FlowchartElementTypes.Connector_1005);
		ToolEntry result = new NodeToolEntry("Connector",
				"Create new Connector", smallImage, largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createWaitbox6CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = FlowchartElementTypes
				.getImageDescriptor(FlowchartElementTypes.Wait_1006);

		largeImage = smallImage;

		final List elementTypes = new ArrayList();
		elementTypes.add(FlowchartElementTypes.Wait_1006);
		ToolEntry result = new NodeToolEntry("Waitbox", "Create new Waitbox",
				smallImage, largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createTransition7CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = FlowchartElementTypes
				.getImageDescriptor(FlowchartElementTypes.Transition_3001);

		largeImage = smallImage;

		final List relationshipTypes = new ArrayList();
		relationshipTypes.add(FlowchartElementTypes.Transition_3001);
		ToolEntry result = new LinkToolEntry("Transition",
				"Create new Transition", smallImage, largeImage,
				relationshipTypes);

		return result;
	}

//	private ToolEntry createTransition8CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = FlowchartElementTypes
//				.getImageDescriptor(FlowchartElementTypes.SwimLane_3002);
//
//		largeImage = smallImage;
//
//		final List relationshipTypes = new ArrayList();
//		relationshipTypes.add(FlowchartElementTypes.SwimLane_3002);
//		ToolEntry result = new LinkToolEntry("SwimLane",
//				"Create new SwimLane", smallImage, largeImage,
//				relationshipTypes);
//
//		return result;
//	}
	/**
	 * @generated
	 */
	private static class NodeToolEntry extends ToolEntry {

		/**
		 * @generated
		 */
		private final List elementTypes;

		/**
		 * @generated
		 */
		private NodeToolEntry(String title, String description,
				ImageDescriptor smallIcon, ImageDescriptor largeIcon,
				List elementTypes) {
			super(title, description, smallIcon, largeIcon);
			this.elementTypes = elementTypes;
		}

		/**
		 * @generated
		 */
		public Tool createTool() {
			Tool tool = new UnspecifiedTypeCreationTool(elementTypes);
			tool.setProperties(getToolProperties());
			return tool;
		}
	}

	/**
	 * @generated
	 */
	private static class LinkToolEntry extends ToolEntry {

		/**
		 * @generated
		 */
		private final List relationshipTypes;

		/**
		 * @generated
		 */
		private LinkToolEntry(String title, String description,
				ImageDescriptor smallIcon, ImageDescriptor largeIcon,
				List relationshipTypes) {
			super(title, description, smallIcon, largeIcon);
			this.relationshipTypes = relationshipTypes;
		}

		/**
		 * @generated
		 */
		public Tool createTool() {
			Tool tool = new UnspecifiedTypeConnectionTool(relationshipTypes);
			tool.setProperties(getToolProperties());
			return tool;
		}
	}
}
