package com.pixelmkmenu.pixelmkmenu.controls;

import com.pixelmkmenu.pixelmkmenu.CustomScreenEntry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiButtonCustomScreen extends GuiButtonMainMenu{
	private Class<? extends GuiScreen> screenClass;
	
	public GuiButtonCustomScreen(int buttonId, CustomScreenEntry customScreenEntry) {
		super(buttonId, customScreenEntry.getScreenText());
		this.screenClass = customScreenEntry.getScreenClass();
	}
	
	public void invoke(Minecraft minecraft) {
		try {
			GuiScreen customScreen = this.screenClass.newInstance();
			minecraft.displayGuiScreen(customScreen);
		}catch (Exception ex) {}
	}
	
}
