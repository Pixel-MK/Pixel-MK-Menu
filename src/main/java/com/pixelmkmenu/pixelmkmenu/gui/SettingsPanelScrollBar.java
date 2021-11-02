package com.pixelmkmenu.pixelmkmenu.gui;
/*
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.util.ResourceLocation;

public class SettingsPanelScrollBar {
	public boolean mouseHeld = false;
	
	private final int xPos;
	private final int yPos;
	private final int width;
	private int height;
	private int handleY;
	
	public SettingsPanelScrollBar(int x, int y, int width, int height, int startingValue) {
		this.xPos = x;
		this.yPos = y;
		this.width = width;
		this.height = height;
		moveHandle(startingValue);
	}
	
	public double getValue() {
		return (this.handleY - this.yPos - 3.0d) / this.height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public void moveHandle(int mouseY) {
		this.handleY = mouseY - this.width/2;
		if (this.handleY < this.yPos + 3) this.handleY = this.yPos + 3;
		if (this.handleY > this.yPos + height - this.width - 3) this.handleY = this.yPos + height - this.width - 3;
	}
	
	public boolean mouseIn(int mouseX, int mouseY) {
		return (mouseX > this.xPos && mouseX < this.xPos + this.width && mouseY > this.yPos && mouseY < this.yPos + this.height);
	}
	
	private void renderHandle(GuiPixelMKSettingsPanel panel) {
		panel.drawTessellatedModalBorderRect(new ResourceLocation("pixelmkmenu", "textures/gui/guiparts.png"), 256, this.xPos - 2, this.handleY - 1, this.xPos + this.width + 2, this.handleY + this.width + 1, 17, 33, 31, 47, 3);
		panel.drawTessellatedModalBorderRect(new ResourceLocation("pixelmkmenu", "textures/gui/guiparts.png"), 256, this.xPos - 2, this.handleY, this.xPos + this.width + 2, this.handleY + this.width, 0, 121, 128, 128, 3);
	}
	
	private void renderBar(GuiPixelMKSettingsPanel panel) {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		panel.drawTessellatedModalBorderRect(new ResourceLocation("pixelmkmenu", "textures/gui/guiparts.png"), 256, this.xPos, this.yPos,
				this.xPos + this.width, this.yPos + this.height, 0, 16, 16, 32, 4);
		panel.zDrop();
		panel.drawDepthRect(this.xPos + 1, this.yPos + 1, this.yPos + this.width - 1, this.yPos + this.height - 1, -2147483648);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	public void render(GuiPixelMKSettingsPanel panel, int mouseY) {
		if(Mouse.isButtonDown(0)) {
			if (this.mouseHeld) moveHandle(mouseY);
		} else {
			this.mouseHeld = false;
		}
		renderBar(panel);
		renderHandle(panel);
	}

}
*/