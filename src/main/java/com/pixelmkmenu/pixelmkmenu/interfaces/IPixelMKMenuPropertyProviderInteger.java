package com.pixelmkmenu.pixelmkmenu.interfaces;

public interface IPixelMKMenuPropertyProviderInteger extends IPixelMKMenuPropertyProvider {
	void setProperty(String name, int value);
	int getIntProperty(String name);
}
