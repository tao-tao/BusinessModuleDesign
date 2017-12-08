package com.tansun.runtime.resource.adapter;

import avicit.ui.runtime.core.node.SpringNode;

public class SpringWorkbenchAdapter extends AbstractModelWorkbenchAdapter
{

    private SpringWorkbenchAdapter()
    {
    }

    public static SpringWorkbenchAdapter getInstance()
    {
        return instance;
    }

    public String getLabel(Object object)
    {
    	SpringNode node = (SpringNode)object;
        return node.getDisplayName();
    }

    public Object[] getChildren(Object object)
    {
    	/*SpringNode node = (SpringNode)object;
    	SpringConfigParser parser = new SpringConfigParser((IFileDelegate) node.getResource());
    	parser.parse();
    	return parser.getBeanDefinitionRegistry().getBeanDefinitionNames();
    	*/
    	SpringNode node = (SpringNode)object;
    	return node.getModelChildren().toArray();
    }

    @Override
	public boolean hasChildren(Object element) {
		return true;
	}

	private static final SpringWorkbenchAdapter instance = new SpringWorkbenchAdapter();

}