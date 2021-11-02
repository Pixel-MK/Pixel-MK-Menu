package com.pixelmkmenu.pixelmkmenu.gui;
/*
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class SettingsPanelMenuTab implements Comparable<SettingsPanelMenuTab> {
	private Minecraft mc = Minecraft.getMinecraft();
	
	private final String label;
	private final int priority;
	private final int width;
	private int xPos;
	private int yPos;
	private boolean active = false;
	
	public SettingsPanelMenuTab(String label, int xPos, int priority) {
		this.label = label;
		this.priority = priority;
		this.width = this.mc.fontRenderer.getStringWidth(label);
		this.xPos = xPos - this.width - 2;
	}
	
	public boolean isActive() {
		return this.active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	public void setXPos(int xPos) {
		this.xPos = xPos - this.width - 3;
	}
	
	public boolean isMouseOver(int tabMenuWidth, int mouseX, int mouseY) {
		int newX = GuiPixelMKSettingsPanel.PANEL_LEFT - tabMenuWidth;
		return (mouseX> newX && mouseX < newX + tabMenuWidth && mouseY > this.yPos - 4 && mouseY < this.yPos + 12);
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public void renderTab(GuiPixelMKSettingsPanel panel, int tabMenuWidth, int mouseX, int mouseY, int y, boolean mask) {
		this.yPos = y;
		int tabX = GuiPixelMKSettingsPanel.PANEL_LEFT - tabMenuWidth;
		int tabY = this.yPos - 4;
		int tabRight = GuiPixelMKSettingsPanel.PANEL_LEFT + 4;
		int tabBottom = this.yPos + 12;
		if (mask) {
			if (this.active) renderTabMask(panel, tabY, tabRight, tabBottom);
		} else {
			boolean mouseOver = isMouseOver(tabMenuWidth, mouseX, mouseY);
			int v = mouseOver ? 32 : (this.active ? 16: 0);
			panel.drawTessellatedModalBorderRect(new ResourceLocation("pixelmkmenu", "textures/gui/guiparts.png"), 256, tabX, tabY, tabRight, tabBottom, 0, 0 + v, 16, 16 + v, 4);
			panel.zDrop();
			panel.drawDepthRect(tabX + 1 , tabY + 1, tabRight - 1, tabBottom - 1, -2147483648);
			this.mc.fontRenderer.drawString(this.label, this.xPos, y, isMouseOver(tabMenuWidth, mouseX, mouseY) ? 5636095 : (this.active ? 16777045 : 11184810));
		}
	}
	
	private void renderTabMask(GuiPixelMKSettingsPanel panel, int tabY, int tabRight, int tabBottom) {
		panel.drawDepthRect(tabRight - 4, tabY + 1, tabRight - 3, tabBottom - 1, -2147483648);
	    panel.drawDepthRect(tabRight - 3, tabY, tabRight - 2, tabBottom, -2147483648);
	}
	
	public int compareTo(SettingsPanelMenuTab other) {
	    if (other == null) return 0; 
	    return (this.priority == other.priority) ? -1 : (this.priority - other.priority);
	  }

}
*/