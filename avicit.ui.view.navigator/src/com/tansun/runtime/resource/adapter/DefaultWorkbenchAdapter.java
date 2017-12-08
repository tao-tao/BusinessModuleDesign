package com.tansun.runtime.resource.adapter;

public final class DefaultWorkbenchAdapter extends AbstractWorkbenchAdapter
{

    private DefaultWorkbenchAdapter()
    {
    }

    public static DefaultWorkbenchAdapter getInstance()
    {
        return instance;
    }

    private static final DefaultWorkbenchAdapter instance = new DefaultWorkbenchAdapter();

}