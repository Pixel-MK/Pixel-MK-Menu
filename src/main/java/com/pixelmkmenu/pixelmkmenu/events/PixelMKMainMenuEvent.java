package com.pixelmkmenu.pixelmkmenu.events;

import com.pixelmkmenu.pixelmkmenu.PixelMKMenuConfig;
import com.pixelmkmenu.pixelmkmenu.PixelMKMenuCore;
import com.pixelmkmenu.pixelmkmenu.gui.GuiPixelMKIngameMenu;
import com.pixelmkmenu.pixelmkmenu.gui.GuiPixelMKMainMenu;

import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PixelMKMainMenuEvent {
	@SubscribeEvent
	public void onGuiOpened(GuiOpenEvent event) {
		PixelMKMenuCore core = PixelMKMenuCore.mod;
		if(event.getGui() instanceof GuiMainMenu && !(event.getGui() instanceof GuiPixelMKMainMenu)) {
			core.OnResourceManagerReload((IResourceManager)null);
			event.setGui((GuiScreen)new GuiPixelMKMainMenu(core));
		}
		if (event.getGui() instanceof GuiIngameMenu && !event.getGui().getClass().getSimpleName().startsWith("Delegate") 
				&& core.getConfig().getBoolProperty(PixelMKMenuConfig.USEPRETTYINGAMEMENU) && !(event.getGui() instanceof GuiPixelMKIngameMenu)) {
			event.setGui((GuiScreen)new GuiPixelMKIngameMenu());
		}
	}
}
