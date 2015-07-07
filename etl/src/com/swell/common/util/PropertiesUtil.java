package com.swell.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	public static Properties load(String propertiesFile) throws IOException,
			FileNotFoundException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(propertiesFile));
		return properties;
	}

	/**
	 * 
	 * @param propertiesFile
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static Properties loadFromClasspath(String propertiesFile)
			throws IOException, FileNotFoundException {
		Properties properties = new Properties();
		ClassLoader classLoader = PropertiesUtil.class.getClassLoader();

		InputStream inputStream = null;

		try {
			inputStream = classLoader.getResourceAsStream(propertiesFile);
			properties.load(inputStream);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}

		return properties;
	}

}
