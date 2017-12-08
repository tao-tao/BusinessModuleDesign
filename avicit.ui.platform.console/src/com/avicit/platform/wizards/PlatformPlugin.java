package com.avicit.platform.wizards;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class PlatformPlugin extends AbstractUIPlugin {
	
	static {
		System.setProperty("hibernate.cglib.use_reflection_optimizer", "true");
	}
	//The shared instance.
	private static PlatformPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	/**
	 * The constructor.
	 */
	public PlatformPlugin() {
		super();
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle("com.tansun.platform.perspective.PerspectivePluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static PlatformPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = PlatformPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	
	 public static IPath getPluginOSPath() {
		Path path = null;
		try {
			plugin.getBundle().getLocation();
			URL url = plugin.getDescriptor().getInstallURL();
			URL url2 = Platform.resolve(url);
			path = new Path(url2.getFile());
		} catch (IOException _ex) {
			path = null;
		}
		return path;
	}
	 public static IWorkbench getWorkbench1(){
	 	return plugin.getWorkbench();
	 }
		/**
		 * Returns a shared image for the given name
		 * <p>
		 * Note: Images returned from this method will be automitically disposed of
		 * when this plug-in shuts down. Callers must not dispose of these images
		 * themselves.
		 * </p>
		 * 
		 * @param name
		 *            the image name found in /icons (with extension)
		 * @return the image, null on error or not found.
		 */
		public Image getImage(String name) {
			if (name == null) {
				return null;
			}

			ImageRegistry images = getImageRegistry();
			Image image = images.get(name);
			if (image == null) {
				try {
					ImageDescriptor id = ImageDescriptor.createFromURL(new URL(
							getBundle().getEntry("/"), "icons/" + name));
					images.put(name, id);

					image = images.get(name);
				} catch (MalformedURLException ee) {
					// log.EditorPlugin.image.error=Image {0} not found.
				}
			}
			return image;
		}

}
