package com.pixelmkmenu.pixelmkmenu.interfaces;

public interface IPixelMKMenuPropertyProviderBoolean extends IPixelMKMenuPropertyProvider {
	void setProperty(String name, boolean value);
	boolean getBoolProperty(String name);
}
