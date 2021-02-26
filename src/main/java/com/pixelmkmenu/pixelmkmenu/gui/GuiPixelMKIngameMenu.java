package com.pixelmkmenu.pixelmkmenu.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.pixelmkmenu.pixelmkmenu.CustomScreenEntry;
import com.pixelmkmenu.pixelmkmenu.controls.GuiButtonCustomScreen;
import com.pixelmkmenu.pixelmkmenu.controls.GuiButtonMainMenu;
import com.pixelmkmenu.pixelmkmenu.controls.GuiButtonPanel;
import com.pixelmkmenu.pixelmkmenu.interfaces.IMouseEventListener;
import com.pixelmkmenu.pixelmkmenu.interfaces.IMouseEventProvider;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.resources.I18n;

public class GuiPixelMKIngameMenu extends GuiIngameMenu implements IMouseEventProvider {
	
	private static List<CustomScreenEntry> customScreenClasses = new ArrayList<CustomScreenEntry>();
	protected GuiButtonPanel buttonPanel;
	private List<GuiButtonCustomScreen> customScreenButtons = new ArrayList<GuiButtonCustomScreen>();
	protected GuiButtonMainMenu btnTexturePacks;
	
	protected boolean begunTweening;
	
	protected float tweenTime;
	protected int updateCounter;
	
	private List<IMouseEventListener> mouseListeners = new LinkedList<IMouseEventListener>();
	
	public GuiPixelMKIngameMenu() {
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonPanel = new GuiButtonPanel(100, GuiButtonPanel.AnchorType.BottomLeft, 22, 20, 150, 100, 16, this.width, this.height, "main");
		this.btnTexturePacks = this.buttonPanel.addButton(I18n.format("options.resourcepack"));
		for (CustomScreenEntry customScreen : customScreenClasses) {
			GuiButtonCustomScreen newBtn = this.buttonPanel.addButton(customScreen);
			this.customScreenButtons.add(newBtn);
		}
		while (this.buttonList.size() > 0) {
			GuiButton button = this.buttonList.remove(this.buttonList.size() -1);
			GuiButtonMainMenu guiButtonMainMenu = this.buttonPanel.addButton(button.displayString, button.id);
			((GuiButton)guiButtonMainMenu).enabled = button.enabled;
		}
		this.buttonList.clear();
		this.buttonList.add(this.buttonPanel);
	}
	
	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		GuiButtonMainMenu guiButtonMainMenu = this.buttonPanel.getPressedButton();
		try {
			super.actionPerformed((GuiButton)guiButtonMainMenu);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(((GuiButton)guiButtonMainMenu).id == this.btnTexturePacks.id) this.mc.displayGuiScreen((GuiScreen)new GuiScreenResourcePacks((GuiScreen)this));
		if (guiButtonMainMenu instanceof GuiButtonCustomScreen) ((GuiButtonCustomScreen)guiButtonMainMenu).invoke(this.mc);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if(!this.begunTweening) {
			this.tweenTime = -partialTicks;
			this.begunTweening = true;
		}
		float tweenPct = Math.min(0.5f, (this.updateCounter + partialTicks - this.tweenTime) / 20.0f);
		float tweenAmount = (float)Math.sin(tweenPct * Math.PI);
		int colour = (int)(192.0f * tweenAmount) << 24;
		GL11.glPushMatrix();
		GL11.glTranslatef(-10.0f + tweenAmount * 20.0f, 0.0f, 0.0f);
		drawGradientRect(10, 0, 184, this.height, colour, colour);
		Gui.drawRect(10, 0, 11, this.height, -1);
		GL11.glTranslatef(-10.0f + tweenAmount * 10.0f, 0.0f, 0.0f);
		GL11.glPushMatrix();
		GL11.glScalef(2.0f, 2.0f, 1.0f);
		drawString(this.fontRenderer, I18n.format("menu.game", new Object[0]), 0, 0, 16777215);
		GL11.glPopMatrix();
		this.buttonPanel.updateButtons(this.updateCounter, partialTicks, mouseX, mouseY);
		this.buttonPanel.drawButton(this.mc, mouseX, mouseY, partialTicks);
		GL11.glPopMatrix();
	}
	
	@Override
	public void updateScreen(){
		super.updateScreen();
		this.updateCounter++;
	}
	
	@Override
	public void handleMouseInput() {
		try {
			super.handleMouseInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(this.mouseListeners.size() > 0 && Mouse.getEventButton() == -1) {
			int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
	        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
	        for (IMouseEventListener listener : this.mouseListeners) listener.mouseMoved(this, mouseX, mouseY);
		}
	}
	
	public void registerMouseListener(IMouseEventListener listener) {
		if(!this.mouseListeners.contains(listener)) {
			this.mouseListeners.add(listener);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		for (IMouseEventListener listener : this.mouseListeners) listener.mousePressed(this, mouseX, mouseY, button);
		try {
			super.mouseClicked(mouseX, mouseY, button);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int button) {
		for (IMouseEventListener listener : this.mouseListeners) listener.mouseReleased(this, mouseX, mouseY, button);
		super.mouseReleased(mouseX, mouseY, button);
	}
	
	public static void registerCustomScreen(String panelName, Class<? extends GuiScreen> customScreenClass, String customScreenText) {
		customScreenClasses.add(new CustomScreenEntry(panelName, customScreenClass, customScreenText));
	}
	
	
}
