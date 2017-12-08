package avicit.ui.view.navigator;


import org.eclipse.osgi.util.NLS;

public class SwtMessages extends NLS
{

    private SwtMessages()
    {
    }

    private static final String BUNDLE_NAME = SwtMessages.class.getName();
    public static String IMAGE_DESCRIPTOR_CAN_NOT_BE_NULL;
    public static String RESOURCE_CAN_NOT_BE_NULL;
    public static String SHELL_CAN_NOT_BE_NULL;
    public static String TOOL_TIP_CAN_NOT_BE_NULL;
    public static String TREE_NULL;
    public static String X_Y_CAN_NOT_BE_NEGATIVE;

    static 
    {
        NLS.initializeMessages(BUNDLE_NAME, SwtMessages.class);
    }
}