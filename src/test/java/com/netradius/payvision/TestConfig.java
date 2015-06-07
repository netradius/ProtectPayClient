package com.netradius.payvision;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Erik R. Jensen
 */
@Slf4j
public class TestConfig {

	public static final String[] CONFIG_LOCATIONS = {
			"C:\\payvision-client.properties",
			"/etc/payvision-client.properties",
			System.getProperty("user.home") + "/payvision-client.properties",
			System.getProperty("user.dir") + "/payvision-client.properties"};

	private Properties properties;

	private Properties loadConfig() {
		for (String location : CONFIG_LOCATIONS) {
			File file = new File(location);
			if (file.exists() && file.canRead()) {
				try (InputStream in = new FileInputStream(file)) {
					Properties props = new Properties();
					props.load(in);
					return props;
				} catch (IOException x) {
					log.error("Error loading config file [" + location + "]: " + x.getMessage(), x);
				}
			}
		}
		throw new IllegalStateException("Failed to load configuration for Payvsion tests");
	}

	public String getUsername() {
		if (properties == null) {
			properties = loadConfig();
		}
		return properties.getProperty("username");
	}

	public String getPassword() {
		if (properties == null) {
			properties = loadConfig();
		}
		return properties.getProperty("password");
	}
}
