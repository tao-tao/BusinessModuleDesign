package avicit.ui.runtime.core;


import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.runtime.core.node.AbstractNode;

public interface IModelCreator {
	AbstractNode getOrCreateNode(IFileDelegate file, boolean forceCreate);
}
