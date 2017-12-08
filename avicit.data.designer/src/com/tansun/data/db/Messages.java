package com.tansun.data.db;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static ResourceBundle resource = ResourceBundle.getBundle(DBPlugin.class.getName());

	public static String getResourceString(String key) {
		try {
			return resource.getString(key);
		} catch (MissingResourceException ex) {
			return key;
		}
	}

}
