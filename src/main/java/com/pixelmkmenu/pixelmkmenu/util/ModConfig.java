package com.pixelmkmenu.pixelmkmenu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;

import com.pixelmkmenu.pixelmkmenu.PixelMKMenuCore;
import com.pixelmkmenu.pixelmkmenu.interfaces.IPixelMKMenuPropertyProviderBoolean;
import com.pixelmkmenu.pixelmkmenu.interfaces.IPixelMKMenuPropertyProviderFloat;
import com.pixelmkmenu.pixelmkmenu.interfaces.IPixelMKMenuPropertyProviderInteger;

import net.minecraftforge.fml.common.Loader;

public abstract class ModConfig implements IPixelMKMenuPropertyProviderFloat, IPixelMKMenuPropertyProviderInteger, IPixelMKMenuPropertyProviderBoolean {
	protected final Properties defaults = new Properties();

	protected Properties config;

	protected final String modName;
	protected final String propertiesFileName;

	protected File propertiesFile;

	public ModConfig(String modName, String propertiesFileName) {
		this.modName = modName;
		this.propertiesFileName = propertiesFileName;
		setDefaults();
		PixelMKMenuCore.WriteLogInfo("Attempting to load/create the config.");
		loadConfig();
	}

	protected abstract void setDefaults();

	protected void loadConfig() {
		this.config = new Properties(this.defaults);
		try {
			this.propertiesFile = new File(Loader.instance().getConfigDir(), propertiesFileName);
			if (this.propertiesFile.exists()) {
				PixelMKMenuCore.WriteLogInfo("Config file found, loading...");
				this.config.load(new FileInputStream(this.propertiesFile));
			} else {
				PixelMKMenuCore.WriteLogInfo("No config file foung, creating config");
				createConfig();
			}
		}catch(Exception e){
			PixelMKMenuCore.WriteLogWarn(e.toString());
		}
	}

	protected void createConfig() {
		try {
			this.config.putAll(this.defaults);
			this.config.store(new FileWriter(this.propertiesFile), (String)null);
		} catch (Exception e) {
			PixelMKMenuCore.WriteLogWarn(e.toString());
		}
	}

	/*
	 * Setters 
	 */

	/**
	 * Set float property
	 * 
	 * @param propertyName Name of the property to set
	 * @param value The value to set to the property
	 */
	public void setProperty(String propertyName, float value) {
		this.config.setProperty(propertyName, String.valueOf(value));
		saveConfig();
	}

	/**
	 * Set int property
	 * 
	 * @param propertyName Name of the property to set
	 * @param value The value to set to the property
	 */
	public void setProperty(String propertyName, int value) {
		this.config.setProperty(propertyName, String.valueOf(value));
		saveConfig();
	}

	/**
	 * Set boolean property
	 * 
	 * @param propertyName Name of the property to set
	 * @param value The value to set to the property
	 */
	public void setProperty(String propertyName, boolean value) {
		this.config.setProperty(propertyName, String.valueOf(value));
		saveConfig();
	}

	/**
	 * Set string property
	 * 
	 * @param propertyName Name of the property to set
	 * @param value The value to set to the property
	 */
	public void setProperty(String propertyName, String value) {
		this.config.setProperty(propertyName, value);
		saveConfig();
	}


	/*
	 * Getters
	 */

	/**
	 * Gets value of a property as a String
	 * 
	 * @param propertyName Name of the property to retrieve value of
	 * @return The value of the string property
	 */
	public String getStringProperty(String propertyName) {
		return this.config.getProperty(propertyName);
	}

	/**
	 * Gets value of a property as a float
	 * 
	 * @param propertyName Name of the property to retrieve value of
	 * @return The value of the float property
	 */
	public float getFloatProperty(String propertyName) {
		return Float.parseFloat(this.config.getProperty(propertyName));
	}

	/**
	 * Gets value of a property as a float between two values
	 * 
	 * @param propertyName Name of the property to retrieve value of
	 * @param min Lowest value in range
	 * @param max Highest value in range
	 * @return The value of the float property
	 */
	public float getClampedFloatProperty(String propertyName, float min, float max) {
		float value = getFloatProperty(propertyName);
		return Math.min(Math.max(value, min), max);
	}

	/**
	 * Gets value of a property as a integer
	 * 
	 * @param propertyName Name of the property to retrieve value of
	 * @return The value of the integer property
	 */
	public int getIntProperty(String propertyName) {
		return Integer.parseInt(this.config.getProperty(propertyName));
	}

	/**
	 * Gets value of a property as a boolean
	 * 
	 * @param propertyName Name of the property to retrieve value of
	 * @return The value of the boolean property
	 */
	public boolean getBoolProperty(String propertyName) {
		return Boolean.parseBoolean(this.config.getProperty(propertyName));
	}

	/**
	 * Gets default value of a property as a string
	 * 
	 * @param propertyName Name of the property to retrieve the default value of
	 * @return The default value of the string property
	 */
	public String getDefaultPropertyValue(String propertyName) {
		return this.defaults.getProperty(propertyName);
	}

	/**
	 * Gets default value of a property as a float
	 * 
	 * @param propertyName Name of the property to retrieve the default value of
	 * @return The default value of the String property
	 */
	public float getDefaultFloatProperty(String propertyName) {
		return Float.parseFloat(this.defaults.getProperty(propertyName));
	}

	/**
	 * Gets default value of a property as a integer
	 * 
	 * @param propertyName Name of the property to retrieve the default value of
	 * @return The default value of the integer property
	 */
	public int getDefaultIntProperty(String propertyName) {
		return Integer.parseInt(this.defaults.getProperty(propertyName));
	}

	/**
	 * Gets default value of a property as a boolean
	 * 
	 * @param propertyName Name of the property to retrieve the default value of
	 * @return The default value of the boolean property
	 */
	public boolean getDefaultBoolProperty(String propertyName) {
		return Boolean.parseBoolean(this.defaults.getProperty(propertyName));
	}
	
	public void saveConfig() {
		try {
			this.config.store(new FileWriter(this.propertiesFile), (String)null);
		} catch (Exception e) {
			PixelMKMenuCore.WriteLogWarn(e.toString());
		}
	}
	
	public void toggleOption(String propertyName) {
		setProperty(propertyName, !getBoolProperty(propertyName));
	}
	
	public String getOptionDisplayString(String propertyName) {
		return "";
	}
}
