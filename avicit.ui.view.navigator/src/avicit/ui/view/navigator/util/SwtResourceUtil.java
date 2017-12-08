package avicit.ui.view.navigator.util;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;
//import org.eclipse.ui.forms.widgets.Form;
//import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.keys.IBindingService;

import avicit.ui.view.navigator.ImageRepository;


public final class SwtResourceUtil
{

    private SwtResourceUtil()
    {
    }

    public static void dispose(Control r_Children[])
    {
        for(int i = 0; i < r_Children.length; i++)
        {
            Control t_Child = r_Children[i];
            dispose(t_Child);
        }

    }

    public static void dispose(Control r_Control)
    {
        if(r_Control != null && !r_Control.isDisposed())
            r_Control.dispose();
    }

    public static void dispose(Resource r_Resource)
    {
        if(r_Resource != null && !r_Resource.isDisposed())
            r_Resource.dispose();
    }

    public static boolean isValid(Resource r_Resource)
    {
        return r_Resource != null && !r_Resource.isDisposed();
    }

    public static boolean isValid(Widget r_Widget)
    {
        return r_Widget != null && !r_Widget.isDisposed();
    }

    public static KeyStroke getAssitantKey()
    {
        String t_TriggerKey = null;
        if(Workbench.getInstance() != null)
        {
            IBindingService t_BindingService = (IBindingService)PlatformUI.getWorkbench().getAdapter(IBindingService.class);
            org.eclipse.jface.bindings.TriggerSequence t_TriggerSequence = t_BindingService.getBestActiveBindingFor("org.eclipse.ui.edit.text.contentAssist.proposals");
            if(t_TriggerSequence != null)
                t_TriggerKey = t_TriggerSequence.toString();
        }
        if(t_TriggerKey == null)
            t_TriggerKey = "Alt+/";
        KeyStroke t_KeyStroke;
        try
        {
            t_KeyStroke = KeyStroke.getInstance(t_TriggerKey);
        }
        catch(Exception _ex)
        {
            t_KeyStroke = KeyStroke.getInstance(0x1000013);
        }
        return t_KeyStroke;
    }

    public static boolean equals(KeyStroke r_KeyStroke, KeyEvent r_KeyEvent)
    {
        if(r_KeyStroke.getModifierKeys() == 0 && r_KeyStroke.getNaturalKey() == r_KeyEvent.character)
            return true;
        return r_KeyStroke.getNaturalKey() == r_KeyEvent.keyCode && (r_KeyStroke.getModifierKeys() & r_KeyEvent.stateMask) == r_KeyStroke.getModifierKeys();
    }

    public static void setReadonly(Widget r_Control, boolean r_Readonly)
    {
        if(!isValid(r_Control))
            return;
        if(r_Control instanceof Text)
        {
            Text t_Text = (Text)r_Control;
            t_Text.setEditable(!r_Readonly);
            return;
        }
        if(r_Control instanceof StyledText)
        {
            StyledText t_Text = (StyledText)r_Control;
            t_Text.setEditable(!r_Readonly);
            return;
        }
        if(r_Control instanceof CCombo)
        {
            CCombo t_Combo = (CCombo)r_Control;
            t_Combo.setEditable(!r_Readonly);
            return;
        }
        if(r_Control instanceof Composite)
        {
            Composite t_Composite = (Composite)r_Control;
            Control t_Controls[] = t_Composite.getChildren();
            for(int i = 0; i < t_Controls.length; i++)
            {
                Control t_Control = t_Controls[i];
                setReadonly(((Widget) (t_Control)), r_Readonly);
            }

            return;
        } else
        {
            return;
        }
    }

    public static void setEnable(Control r_Control, boolean r_Enable)
    {
        if(!isValid(r_Control))
            return;
        if(r_Control instanceof Text)
        {
            Text t_Text = (Text)r_Control;
            t_Text.setEditable(r_Enable);
            updateColor(r_Control, r_Enable);
            return;
        }
        if(r_Control instanceof CCombo)
        {
            CCombo t_Combo = (CCombo)r_Control;
            t_Combo.setEditable(r_Enable);
            updateColor(r_Control, r_Enable);
            return;
        }
        if(r_Control instanceof Combo)
        {
            Combo t_Combo = (Combo)r_Control;
            t_Combo.setEnabled(r_Enable);
            updateColor(r_Control, r_Enable);
            return;
        }
        if(r_Control instanceof StyledText)
        {
            StyledText t_StyledText = (StyledText)r_Control;
            t_StyledText.setEditable(r_Enable);
            updateColor(r_Control, r_Enable);
            return;
        }
        if(r_Control instanceof Composite)
        {
            Composite r_Composite = (Composite)r_Control;
            Control t_Children[] = r_Composite.getChildren();
            if(t_Children != null)
            {
                for(int i = 0; i < t_Children.length; i++)
                {
                    Control t_Control = t_Children[i];
                    setEnable(t_Control, r_Enable);
                }

            }
            compositeClasses.contains(r_Control.getClass());
        } else
        if(!(r_Control instanceof Label))
            r_Control.setEnabled(r_Enable);
    }

    private static void updateColor(Control control, boolean flag)
    {
    }

    public static ImageDescriptor loadIcon(String r_BundleName, String r_ResourceName)
    {
        ImageRepository t_ImageRepository = ImageRepository.getImageRepository(r_BundleName);
        ImageDescriptor t_ImageDescriptor = t_ImageRepository.getImageDescriptor(r_ResourceName);
        if(t_ImageDescriptor == null)
            try
            {
                java.net.URL t_URL = SwtResourceUtil.class.getClassLoader().getResource(r_ResourceName);
                if(t_URL != null)
                    t_ImageDescriptor = ImageDescriptor.createFromURL(t_URL);
            }
            catch(Exception _ex) { }
        if(t_ImageDescriptor != null)
        {
            t_ImageRepository.setImageDescriptor(r_ResourceName, t_ImageDescriptor);
            return t_ImageRepository.getImageDescriptor(r_ResourceName);
        } else
        {
            return ImageDescriptor.getMissingImageDescriptor();
        }
    }

    public static Font BoldFont = new Font(Display.getCurrent(), new FontData[] {
        new FontData("Arial", 10, 1)
    });
    private static Set compositeClasses;

    static 
    {
        compositeClasses = new HashSet();
        compositeClasses.add(Composite.class);
        compositeClasses.add(StyledText.class);
        compositeClasses.add(Combo.class);
        compositeClasses.add(CCombo.class);
        compositeClasses.add(CTabFolder.class);
        compositeClasses.add(Browser.class);
        compositeClasses.add(TabFolder.class);
        compositeClasses.add(Group.class);
        compositeClasses.add(Form.class);
        compositeClasses.add(SashForm.class);
        compositeClasses.add(Sash.class);
        compositeClasses.add(Spinner.class);
    }
}