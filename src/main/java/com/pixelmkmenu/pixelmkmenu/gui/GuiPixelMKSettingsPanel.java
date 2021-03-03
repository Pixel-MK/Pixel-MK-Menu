package com.pixelmkmenu.pixelmkmenu.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.pixelmkmenu.pixelmkmenu.interfaces.IAdvancedDrawGui;
import com.pixelmkmenu.pixelmkmenu.util.ModConfig;
import com.pixelmkmenu.pixelmkmenu.util.properties.PixelMKMenuProperty;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public abstract class GuiPixelMKSettingsPanel extends AdvancedDrawGui{
	protected ModConfig config;
	protected ArrayList<PixelMKMenuProperty<?>> properties = new ArrayList<PixelMKMenuProperty<?>>();
	
	protected static int PANEL_WIDTH = 330;
	protected static int PANEL_LEFT = 97;
	protected static int PANEL_TOP = 0;
	protected static int PANEL_HEIGHT = 220;
	protected static int TAB_SPACING = 2;
	protected int contentHeight = 240;
	
	protected boolean overCloseButton = false;
	
	protected SettingsPanelManager panelManager = SettingsPanelManager.getInstance();
	
	protected boolean isPanel = false;
	
	public ModConfig getConfig() {
		return this.config;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		setTexMapSize(256);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		int right = PANEL_LEFT + PANEL_WIDTH;
		int bottom = PANEL_TOP + PANEL_HEIGHT;
		this.zLevel = -50.0f;
		drawTabs(mouseX, mouseY, partialTicks, true);
		drawPanel(right, bottom);
		drawTabs(mouseX, mouseY, partialTicks, false);
		this.zLevel = 0.0f;
		drawCloseButton(mouseX, mouseY, right - 4, PANEL_TOP +20);
		for (PixelMKMenuProperty<?> property : this.properties) {
			if(property.isVisible()) property.draw((IAdvancedDrawGui)this, mouseX, mouseY);
		}
	}
	
	protected void drawPanel(int right, int bottom) {
		PANEL_HEIGHT = Math.max(220, this.height - PANEL_TOP - 2);
		this.zLevel = -100.0f;
		GL11.glEnable(2929);
		drawTessellatedModalBorderRect(new ResourceLocation("pixelmkmenu", "textures/gui/guiparts.png"), 256, PANEL_LEFT, PANEL_TOP, right, bottom, 0, 16, 16, 32, 4);
		zDrop();
		drawDepthRect(PANEL_LEFT + 1, PANEL_TOP + 1, right - 1, bottom - 1, -2147483648);
		GL11.glDisable(2929);
	}
	
	protected void drawTabs(int mouseX, int mouseY, float partialTicks, boolean mask) {
		zDrop();
		this.panelManager.renderTabs(this, mouseX, mouseY, partialTicks, PANEL_LEFT, PANEL_TOP, TAB_SPACING, mask);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (this.overCloseButton) {
			onClosed();
			this.mc.displayGuiScreen(null);
			return;
		}
		if (!this.panelManager.mouseClicked(this, mouseX, mouseY, mouseButton, PANEL_LEFT, PANEL_TOP, TAB_SPACING)) {
			try {
				super.mouseClicked(mouseX, mouseY, mouseButton);
			} catch (IOException e) {
				e.printStackTrace();
			}
			for(PixelMKMenuProperty<?> property : this.properties) {
				if (property.isVisible()) property.mouseClicked(mouseX, mouseY);
			}
		}
	}
	
	protected void onClosed() {
		for (PixelMKMenuProperty<?> property : this.properties) property.onClosed();
	}
	
	@Override
	protected void keyTyped(char keyChar, int keyCode) {
		try {
			super.keyTyped(keyChar, keyCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
		keyPressed(keyChar, keyCode);
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		for (PixelMKMenuProperty<?> property : this.properties) property.updateCursorCounter();
	}
	
	public void zDrop() {
		this.zLevel--;
	}
	
	protected boolean mouseIn(int mouseX, int mouseY, int x, int y, int x2, int y2) {
		return (mouseX > x && mouseX < x2 && mouseY > y && mouseY < y2);
	}
	
	protected void drawCloseButton(int mouseX, int mouseY, int right, int top) {
		this.overCloseButton = mouseIn(mouseX, mouseY, right - 15, top - 16, right, top - 1);
		int v = this.overCloseButton ? 32 : 0;
		drawDepthRect(right - 1, top - 2, right - 14, top - 15, -2147483648);
		drawTessellatedModalBorderRect(new ResourceLocation("pixelmkmenu", "textures/gui/guiparts.png"), 256, this.overCloseButton ? (right - 16) : (right - 15),
				this.overCloseButton ? (top - 17) : (top - 16), this.overCloseButton ? (right + 1) : right,
						this.overCloseButton ? top : (top - 1), 0, v, 16, 16 + v, 4);
		drawString(this.mc.fontRenderer, "x", right - 10, top - 13, this.overCloseButton ? 5636095 : 11184810);
	}

	public int getContentHeight() {
		return this.contentHeight;
	}
	
	public void onPanelShown() {
		this.mc = Minecraft.getMinecraft();
		this.isPanel = true;
		try {
			handleInput();
		} catch (Exception e) {}
	}
	
	public void onPanelResize() {
		try {
			handleInput();
		}catch (Exception e) {}
	}
	
	public void onPanelHidden() {}
	
	@SubscribeEvent
	public void onTick(TickEvent event) {
		updateScreen();
	}
	
	public void drawPanel(int mouseX, int mouseY, float partialTicks) {
		GL11.glTranslatef(-PANEL_LEFT, 0.0f, 0.0f);
		for (PixelMKMenuProperty<?> property : this.properties) {
			if (property.isVisible()) property.draw((IAdvancedDrawGui)this, mouseX + PANEL_LEFT, mouseY);
		}
	}
	
	public void mousePressed(int mouseX, int mouseY, int mouseButton) {
		try {
			super.mouseClicked(mouseX + PANEL_LEFT, mouseY, mouseButton);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (PixelMKMenuProperty<?> property : this.properties) {
			if (property.isVisible()) property.mouseClicked(mouseX + PANEL_LEFT, mouseY);
		}
	}
	
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		mouseClickMove(mouseX + PANEL_LEFT, mouseY, mouseButton, 0l);
	}
	
	public void mouseMoved(int mouseX, int mouseY) {
		mouseClickMove(mouseX + PANEL_LEFT, mouseY, -1, 0l);
	}
	
	public void keyPressed( char keyChar, int keyCode) {
		if (keyCode == 1) {
			return;
		} 
		if (keyCode == 15) {
			PixelMKMenuProperty<?> focused = null;
			PixelMKMenuProperty<?> next = null;
			PixelMKMenuProperty<?> before = null;
			for (PixelMKMenuProperty<?> property : this.properties) {
				if (property.isFocusable() && next == null)
					next = property; 
				if (property.isFocused() && focused == null) {
					focused = property;
					before = next;
					next = null;
				} 
			} 
			if (next == null)
				next = before; 
			if (focused != null && next != null && next != focused) {
				focused.setFocused(false);
				next.setFocused(true);
				return;
			} 
		} 
		for (PixelMKMenuProperty<?> property : this.properties) {
			if (property.isVisible())
				property.keyTyped(keyChar, keyCode); 
		} 
	}
}
