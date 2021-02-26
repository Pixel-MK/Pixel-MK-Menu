package com.pixelmkmenu.pixelmkmenu.interfaces;

import net.minecraft.util.ResourceLocation;

public interface IAdvancedDrawGui {
	
	public void drawTessellatedModalBorderRect(ResourceLocation texture, int textureSize, int x, int y, int x2, int y2,
			int u, int v, int u2, int v2, int borderSize);
	
	public void drawTessellatedModalBorderRect(ResourceLocation texture, int textureSize, int x, int y, int x2, int y2,
			int u, int v, int u2, int v2, int borderSize, boolean setColour);
	
	public void drawTexturedModalRect(ResourceLocation texture, int x, int y, int x2, int y2, int u, int v, int u2, int v2);
	
	public void drawTexturedModalRect(int x, int y, int x2, int y2, int u, int v, int u2, int v2);

}
