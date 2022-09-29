package com.pixelmk.pixelmkmenu.controls;

import com.pixelmk.pixelmkmenu.gui.CustomScreenEntry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class GuiButtonCustomScreen extends GuiButtonMainMenu{
	private Class<? extends Screen> screenClass;

	public GuiButtonCustomScreen(CustomScreenEntry customScreenEntry, ActionInstance onPress) {
		super(customScreenEntry.getScreenText(), onPress);
		this.screenClass = customScreenEntry.getScreenClass();
	}

	public void invoke(Minecraft minecraft) {
		try {
			Screen customScreen = this.screenClass.getDeclaredConstructor().newInstance();
			minecraft.setScreen(customScreen);
		}catch (Exception ex) {}
	}

}
