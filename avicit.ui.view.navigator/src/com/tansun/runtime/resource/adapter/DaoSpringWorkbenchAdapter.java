package com.tansun.runtime.resource.adapter;

import avicit.ui.runtime.core.node.DaoSpringNode;

public class DaoSpringWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

    private DaoSpringWorkbenchAdapter()
    {
    }

    public static DaoSpringWorkbenchAdapter getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	DaoSpringNode node = (DaoSpringNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	/*SpringNode node = (SpringNode)object;
    	SpringConfigParser parser = new SpringConfigParser((IFileDelegate) node.getResource());
    	parser.parse();
    	return parser.getBeanDefinitionRegistry().getBeanDefinitionNames();
    	*/
    	DaoSpringNode node = (DaoSpringNode)object;
    	return node.getModelChildren().toArray();
    }

    @Override
	public boolean hasChildren(Object element) {
		return true;
	}

	private static final DaoSpringWorkbenchAdapter instance = new DaoSpringWorkbenchAdapter();

}