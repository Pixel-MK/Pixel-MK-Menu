package com.pixelmkmenu.pixelmkmenu.interfaces;

public interface IPixelMKMenuPropertyProvider {
	
	String getStringProperty(String name);
	
	String getOptionDisplayString(String name);
	
	void toggleOption(String name);
	
	String getDefaultPropertyValue(String name);

}
