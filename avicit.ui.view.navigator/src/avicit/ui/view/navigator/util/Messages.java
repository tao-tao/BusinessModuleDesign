package avicit.ui.view.navigator.util;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{

    private Messages()
    {
    }

    private static final String BUNDLE_NAME = "com.genuitec.eclipse.desktop.messages";
    public static String AbstractCaptureAction_windowsonly_message;
    public static String AbstractCaptureAction_windowsonly_title;
    public static String CaptureDialog_Capture_Foreground_Window;
    public static String CaptureDialog_Capture_Desktop;
    public static String CaptureDialog_Capture_dialog;
    public static String CaptureDialog_Capture_Dialog;
    public static String CaptureDialog_Capture_Now;
    public static String CaptureDialog_Capture_Region;
    public static String CaptureDialog_Capture_Type;
    public static String CaptureDialog_Capture_Window;
    public static String CaptureDialog_Hide_MyEclipse_ImageEditor_window_during_capture_operations;
    public static String CaptureDialog_Options;
    public static String CaptureDialog_Seconds_delay_before_capture;
    public static String CaptureDialogComponent_Esc_Abort_operation;
    public static String CaptureDialogComponent_Keyboard;
    public static String CaptureDialogComponent_Mouse;
    public static String CaptureDialogComponent_Pixel_Color;
    public static String CaptureMainAction_message_feature_unavailable;
    public static String CaptureMainAction_title_feature_unavailable;
    public static String CaptureRegionAction_Left_Click_and_drag_to_select_an_area;
    public static String CaptureToolbarAction_Capture;
    public static String CaptureToolbarAction_Screen_capture;
    public static String CaptureToolbarAction_Settings;
    public static String CaptureWindowAction_Select_window_and_click;
    public static String ExploreFileActionDelegate_text_windowsOnlyMessage;
    public static String ExploreFileActionDelegate_title;
    public static String DesktopUtil_message_errorWarnDialog;
    public static String DesktopUtil_title_emailSubject;
    public static String DesktopUtil_title_emailSubject_plural;
    public static String DesktopUtil_title_errorWarnDialog;
    public static String ScreenCaptureAction_Capture_Desktop;
    public static String ScreenCaptureAction_Capture_Region;
    public static String ScreenCaptureAction_Capture_Window;

    static 
    {
        NLS.initializeMessages("com.tansun.ui.navigator.util.messages", Messages.class);
    }
}