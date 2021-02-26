package com.pixelmkmenu.pixelmkmenu.interfaces;

import net.minecraft.client.Minecraft;

public interface IPanoramaRenderer {
	void setPanoramaResolution(Minecraft minecraft, int x, int y);
	void initPanoramaRenderer();
	void updatePanorama();
	boolean renderPanorama(int mouseX, int mouseY, float partialTicks);
	int getUpdateCounter();
}
