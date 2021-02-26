package com.pixelmkmenu.pixelmkmenu;

import net.minecraft.client.gui.GuiScreen;

public class CustomScreenEntry {
	private String panelName;
	private String screenText;
	
	private Class<? extends GuiScreen> screenClass;
	
	public CustomScreenEntry(String panelName, Class<? extends GuiScreen> screenClass, String screenText) {
		this.panelName = panelName;
		this.screenClass = screenClass;
		this.screenText = screenText;
	}
	
	public String getPanelName() {
		return this.panelName;
	}
	
	public Class<? extends GuiScreen> getScreenClass(){
		return this.screenClass;
	}
	
	public String getScreenText() {
		return this.screenText;
	}

}
