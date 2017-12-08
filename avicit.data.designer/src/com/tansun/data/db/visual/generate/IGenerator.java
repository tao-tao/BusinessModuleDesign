package com.tansun.data.db.visual.generate;


import org.eclipse.core.resources.IFile;
import org.eclipse.gef.GraphicalViewer;

import com.tansun.data.db.visual.model.RootModel;

public interface IGenerator {

	public String getGeneratorName();

	public void execute(IFile erdFile, RootModel root, GraphicalViewer viewer);

}
