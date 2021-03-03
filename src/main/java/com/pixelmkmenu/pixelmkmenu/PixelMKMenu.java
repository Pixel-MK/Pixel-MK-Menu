package com.pixelmkmenu.pixelmkmenu;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = PixelMKMenu.MODID, name = "Pixel MK Menu", version = PixelMKMenu.VERSION, acceptedMinecraftVersions = "[1.12,1.12.2]")
public class PixelMKMenu {
	
	public static final String MODID = "pixelmkmenu";
    public static final String VERSION = "$version";
    public PixelMKMenuCore mod = new PixelMKMenuCore();
    
	public String getVersion() {
		return VERSION;
	}
	
	public String getName() {
		return MODID;
	}
	
	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event) {
		this.mod.onInit();
	}
	
	@Mod.EventHandler
	public void onPostInit(FMLPostInitializationEvent event) {
		this.mod.onPostInit();
	}

}
