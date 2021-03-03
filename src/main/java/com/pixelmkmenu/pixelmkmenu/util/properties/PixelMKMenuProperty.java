package com.pixelmkmenu.pixelmkmenu.util.properties;

import com.pixelmkmenu.pixelmkmenu.interfaces.IAdvancedDrawGui;
import com.pixelmkmenu.pixelmkmenu.interfaces.IPixelMKMenuPropertyProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public abstract class PixelMKMenuProperty<PropertyType extends IPixelMKMenuPropertyProvider> extends Gui {
	protected FontRenderer fontRenderer;
	protected PropertyType propertyProvider;
	
	protected String propertyBinding;
	protected String displayText;
	
	protected Minecraft mc = Minecraft.getMinecraft();
	
	protected int xPos;
	protected int yPos;
	protected int cursorCounter;
	
	protected boolean focused;
	protected boolean visible = true;
	
	public PixelMKMenuProperty(IPixelMKMenuPropertyProvider propertyProvider, String binding, String displayText, int xPos, int yPos) {
		try {
			this.propertyProvider = (PropertyType)propertyProvider;
		} catch (ClassCastException e) {
			throw new RuntimeException(String.format("Can't create Pixel MK Property for binding %s for panel %s", new Object[] {binding, propertyProvider.getClass().getSimpleName()}));
		}
		this.fontRenderer = this.mc.fontRenderer;
		this.propertyBinding = binding;
		this.displayText = displayText;
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public void updateCursorCounter() {
		this.cursorCounter++;
	}
	
	public abstract void draw(IAdvancedDrawGui panel, int mouseX, int mouseY);
	
	public abstract void mouseClicked(int mouseX, int mouseY);
	
	public abstract void keyTyped(char key, int keyCode);
	
	public void onClosed() {}
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isFocusable() {
		return false;
	}
	
	public boolean isFocused() {
		return false;
	}
	
	public void setFocused(boolean focus) {}
	
	public void playClickSound(SoundHandler soundHandler) {
		soundHandler.playSound((ISound)PositionedSoundRecord.getMasterRecord(new SoundEvent(new ResourceLocation("gui.button.press")), 1.0f));
	}
	
}
