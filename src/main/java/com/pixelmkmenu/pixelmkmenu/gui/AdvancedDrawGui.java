package com.pixelmkmenu.pixelmkmenu.gui;

import org.lwjgl.opengl.GL11;

import com.pixelmkmenu.pixelmkmenu.interfaces.IAdvancedDrawGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public abstract class AdvancedDrawGui extends GuiScreen implements IAdvancedDrawGui{
	
	public static float texMapScale = 0.00390625f;
	
	public void drawTessellatedModalBorderRect(ResourceLocation texture, int textureSize, int x, int y, int x2, int y2,
			int u, int v, int u2, int v2, int borderSize) {
		drawTessellatedModalBorderRect(texture, textureSize, x, y, x2, y2, u, v, u2, v2, borderSize, true);
	}

	public void drawTessellatedModalBorderRect(ResourceLocation texture, int textureSize, int x, int y, int x2, int y2,
			int u, int v, int u2, int v2, int borderSize, boolean setColour) {
		int tileSize = Math.min((u2 - u) / 2 - 1, (v2 - v) / 2 - 1);
		int ul = u + tileSize, ur = u2 - tileSize, vt = v + tileSize, vb = v2 - tileSize;
		int xl = x + borderSize, xr = x2 - borderSize, yt = y + borderSize, yb = y2 - borderSize;
		setTexMapSize(textureSize);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		if(setColour) GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		drawTexturedModalRect(x, y, xl, yt, u, v, ul, vt);
	    drawTexturedModalRect(xl, y, xr, yt, ul, v, ur, vt);
	    drawTexturedModalRect(xr, y, x2, yt, ur, v, u2, vt);
	    drawTexturedModalRect(x, yb, xl, y2, u, vb, ul, v2);
	    drawTexturedModalRect(xl, yb, xr, y2, ul, vb, ur, v2);
	    drawTexturedModalRect(xr, yb, x2, y2, ur, vb, u2, v2);
	    drawTexturedModalRect(x, yt, xl, yb, u, vt, ul, vb);
	    drawTexturedModalRect(xr, yt, x2, yb, ur, vt, u2, vb);
	    drawTexturedModalRect(xl, yt, xr, yb, ul, vt, ur, vb);
	}
	
	public void drawTexturedModalRect(ResourceLocation texture, int x, int y, int x2, int y2, int u, int v, int u2, int v2) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(x, y, x2, y2, u, v, u2, v2);
	}
	
	public void drawTexturedModalRect(int x, int y, int x2, int y2, int u, int v, int u2, int v2) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bb = tessellator.getBuffer();
		bb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bb.pos(x, y2, this.zLevel).tex((u * texMapScale), (v2 * texMapScale)).endVertex();
		bb.pos(x2, y2, this.zLevel).tex((u2 * texMapScale), (v2 * texMapScale)).endVertex();
		bb.pos(x2, y, this.zLevel).tex((u2 * texMapScale), (v * texMapScale)).endVertex();
		bb.pos(x, y, this.zLevel).tex((u * texMapScale), (v * texMapScale)).endVertex();
		tessellator.draw();
	}
	
	public void drawDepthRect(int x1, int y1, int x2, int y2, int colour) {
		if (x1 < x2) {
			int xTemp = x1;
			x1 = x2;
			x2 = xTemp;
		}
		if (y1 < y2) {
			int yTemp = y1;
			y1 = y2;
			y2 = yTemp;
		}
		float alpha = (colour >> 24 & 0xFF) / 255.0f;
		float red = (colour >> 16 & 0xFF) / 255.0f;
		float green  = (colour >> 8 & 0xFF) / 255.0f;
		float blue = (colour & 0xFF) / 255.0f;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bb = tessellator.getBuffer();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(red, green, blue, alpha);
		bb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		bb.pos(x1, y2, this.zLevel).endVertex();
		bb.pos(x2, y2, this.zLevel).endVertex();
		bb.pos(x2, y1, this.zLevel).endVertex();
		bb.pos(x1, y1, this.zLevel).endVertex();
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	protected void setTexMapSize(int textureSize) {
		texMapScale = 1.0f / textureSize;
	}

}
