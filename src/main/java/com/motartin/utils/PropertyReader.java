package com.motartin.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

import static com.motartin.application.Constants.App.FALLBACK_PROPERTY_FILE_NAME;
import static com.motartin.application.PictureFrame.getAppLocationPath;

@Slf4j
public class PropertyReader {

	/**
	 * Reads properties from a file.
	 * Checks: 	first the project root for a file with the given name,
	 * 			second the resource folder for a file with the given name
	 * 			third the resource folder for the default file.
	 * @param configFileName name of the properties file
	 * @return Properties contained in the file with the given name or empty properties if no file with specified name was found.
	 */
	public static Properties readConfig(String configFileName) {
		Properties theProperties = new Properties();

		try (FileInputStream streamFromAppRoot = new FileInputStream(Paths.get(getAppLocationPath(), configFileName).toFile())) {
			theProperties.load(streamFromAppRoot);
		} catch (Exception rootException) {
			log.debug("Could not load properties from {} in app root", configFileName, rootException);
			try (InputStream streamFromResources = PropertyReader.class.getResourceAsStream("/" + configFileName)) {
				theProperties.load(streamFromResources);
			} catch (Exception resourcesException) {
				log.debug("Could not load properties from {} in resources folder", configFileName, resourcesException);
				try (InputStream defaultStream = PropertyReader.class.getResourceAsStream("/" + FALLBACK_PROPERTY_FILE_NAME)) {
					theProperties.load(defaultStream);
				} catch (Exception noDefault) {
					log.error("Could not load any properties.", noDefault);
				}
			}
		}

		return theProperties;
	}

	private PropertyReader() {
		throw new AssertionError(getClass().getSimpleName() + " cannot be instantiated.");
	}
}
