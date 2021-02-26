package com.pixelmkmenu.pixelmkmenu;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventSharedConfig {

	    @SubscribeEvent
	    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
	        if (event.getModID().equalsIgnoreCase(PixelMKMenuCore.MODID)) {
	        	PixelMKMenuCore.syncConfig();
	        }
	    }

}
