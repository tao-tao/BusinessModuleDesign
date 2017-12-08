package com.jvisio.flowchart.diagram.part;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.util.IDEEditorFileCreator;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.DiagramFileCreator;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import org.eclipse.core.runtime.IPath;

/**
 * @generated
 */
public class FlowchartDiagramFileCreator extends IDEEditorFileCreator {

	/**
	 * @generated
	 */
	private static FlowchartDiagramFileCreator INSTANCE = new FlowchartDiagramFileCreator();

	/**
	 * @generated
	 */
	public static DiagramFileCreator getInstance() {
		return INSTANCE;
	}

	/**
	 * @generated
	 */
	public String getExtension() {
		return ".fcd"; //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	public String getUniqueFileName(IPath containerPath, String fileName) {
		int nFileNumber = 1;
		fileName = removeExtensionFromFileName(fileName);
		String newFileName = fileName;

		IPath diagramFilePath = containerPath
				.append(appendExtensionToFileName(newFileName));
		IPath modelFilePath = containerPath
				.append(appendExtensionToModelFileName(newFileName));
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();

		while (workspaceRoot.exists(diagramFilePath)
				|| workspaceRoot.exists(modelFilePath)) {
			nFileNumber++;
			newFileName = fileName + nFileNumber;
			diagramFilePath = containerPath
					.append(appendExtensionToFileName(newFileName));
			modelFilePath = containerPath
					.append(appendExtensionToModelFileName(newFileName));
		}
		return newFileName;
	}

	/**
	 * @generated
	 */
	private String removeExtensionFromFileName(String fileName) {
		if (fileName.endsWith(getExtension())) {
			return fileName.substring(0, fileName.length()
					- getExtension().length());
		}
		return fileName;
	}

	/**
	 * @generated
	 */
	private String appendExtensionToModelFileName(String fileName) {
		return fileName + ".fcm"; //$NON-NLS-1$
	}

}