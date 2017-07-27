package com.lianlian.utils.system;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourcesUtil {
	private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("properties/config");
	
	private static final String getKeyValue(String key) {
		try {
			return bundle.getString(key);
		}catch(MissingResourceException e) {
			return null;
		}
	}
	
	/**
	 * 获取base64密钥
	 * @param key
	 * @return
	 */
	public static final String getBase64SecreateKey() {
		return getKeyValue("base64SecurityKey");
	}
	
	public static void main(String[] args) {
		getKeyValue("base64SecurityKey");
		System.out.println(getBase64SecreateKey());
		System.out.println(getSessionCacheName());
	}

	public static String getSessionCacheName() {
		return getKeyValue("sessionCacheName");
	}
}
