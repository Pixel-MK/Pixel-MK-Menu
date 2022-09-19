package com.pixelmk.pixelmkmenu.helpers;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.pixelmk.pixelmkmenu.controls.GuiButtonMainMenu;

import net.minecraft.resources.ResourceLocation;

public class ButtonManager {

    protected Map<ResourceLocation, GuiButtonMainMenu> buttons = new TreeMap<>();

    public Collection<GuiButtonMainMenu> getButtons() {
		return this.buttons.values();
	}

}
