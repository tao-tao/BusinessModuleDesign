package avicit.platform6.tools.codegeneration;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class CodeGenerationActivator extends AbstractUIPlugin {
	
	public static final String VISIBLE = "显示";
	public static final String HIDDEN = "隐藏";
	public static final String EDITABLE = "可编辑";
	public static final String UNEDITABLE = "不可编辑";
	
	public static final String SOURCE_FOLDER_PATH = "src";

	// The plug-in ID
	public static final String PLUGIN_ID = "avicit.platform6.tools.codegeneration"; 

	// The shared instance
	private static CodeGenerationActivator plugin;
	
	/**
	 * The constructor
	 */
	public CodeGenerationActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
//		Properties props = new Properties();
//		  InputStream is = CodeGenerationActivator.class.getResourceAsStream("/log4j.properties");
//		  props.load(is);
//		  PropertyConfigurator.configure(props);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static CodeGenerationActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
