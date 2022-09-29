package io.github.pixelmk.pixelmkmenu.gui;

import net.minecraft.client.gui.screens.Screen;

public class CustomScreenEntry {
	private String panelName;
	private String screenText;

	private Class<? extends Screen> screenClass;

	public CustomScreenEntry(String panelName, Class<? extends Screen> screenClass, String screenText) {
		this.panelName = panelName;
		this.screenClass = screenClass;
		this.screenText = screenText;
	}

	public String getPanelName() {
		return this.panelName;
	}

	public Class<? extends Screen> getScreenClass(){
		return this.screenClass;
	}

	public String getScreenText() {
		return this.screenText;
	}

}
