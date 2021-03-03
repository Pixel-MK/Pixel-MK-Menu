package com.pixelmkmenu.pixelmkmenu.gui;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class SettingsPanelManager {

	private static int TAB_WIDTH = 72;
	private static final int TAB_HEIGHT = 16;
	private static SettingsPanelManager instance;
	private Minecraft mc;
	private Map<String, Class<? extends GuiPixelMKSettingsPanel>> panels = new TreeMap<String, Class<? extends GuiPixelMKSettingsPanel>>();
	private TreeSet<SettingsPanelMenuTab> tabs = new TreeSet<SettingsPanelMenuTab>();
	private SettingsPanelScrollBar scrollbar = new SettingsPanelScrollBar(6, 4, 10, 220, 0);
	private Class<? extends GuiPixelMKSettingsPanel> firstPanel = null;
	public static KeyBinding guiKeyBinding = new KeyBinding("Pixel MK Menu Config", 0, "Pixel MK Menu");
	
	public static SettingsPanelManager getInstance() {
		if(instance == null) {
			instance = new SettingsPanelManager();
			ClientRegistry.registerKeyBinding(guiKeyBinding);
		}
		return instance;
	}
	
	private SettingsPanelManager() {
		this.mc = Minecraft.getMinecraft();
	}
	
	public static void addSettingsPanel(String panelName, Class<? extends GuiPixelMKSettingsPanel> panel) {
		getInstance().addPanel(panelName, panel);
	}
	
	public static void removeSettingsPanel(String panelName) {
		getInstance().removePanel(panelName);
	}
	
	public static void displaySettings() {
		getInstance().displaySettings(Minecraft.getMinecraft());
	}
	
	public static boolean hasOptions() {
		return ((getInstance()).panels.size() > 0);
	}
	
	public void addPanel(String panelName, Class<? extends GuiPixelMKSettingsPanel> panel) {
		if (panel != null) {
			this.panels.put(panelName, panel);
			this.tabs.add(new SettingsPanelMenuTab(panelName, GuiPixelMKSettingsPanel.PANEL_LEFT, 0));
		}
		updateTabs();
	}
	
	public void addPanel(String panelName, Class<? extends GuiPixelMKSettingsPanel> panel, int priority) {
		if (panel != null) {
			this.panels.put(panelName, panel);
			this.tabs.add(new SettingsPanelMenuTab(panelName, 62, priority));
		}
		updateTabs();
	}
	
	public void removePanel(String panelName) {
		this.panels.remove(panelName);
		updateTabs();
	}
	
	protected void updateTabs() {
		Iterator<String> iter = this.panels.keySet().iterator();
		if(iter.hasNext()) this.firstPanel = this.panels.get(iter.next());
		int largest = 0;
		for (SettingsPanelMenuTab tab : this.tabs) {
			if (this.mc.fontRenderer.getStringWidth(tab.getLabel()) > largest) largest = this.mc.fontRenderer.getStringWidth(tab.getLabel());
		}
		if (16 * this.tabs.size() < GuiPixelMKSettingsPanel.PANEL_HEIGHT) {
			TAB_WIDTH = largest + 6;
		} else {
			TAB_WIDTH = largest + 18 + this.scrollbar.getWidth();
		}
		for (SettingsPanelMenuTab tab : this.tabs) tab.setXPos(TAB_WIDTH);
		GuiPixelMKSettingsPanel.PANEL_LEFT = TAB_WIDTH;
	}
	
	@SubscribeEvent
	public void onTick(TickEvent event, Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
		if(minecraft.currentScreen == null && guiKeyBinding.isKeyDown() && this.firstPanel != null) displaySettings(minecraft);
	}
	
	public void displaySettings(Minecraft minecraft) {
		try {
			GuiPixelMKSettingsPanel panel = this.firstPanel.newInstance();
			minecraft.displayGuiScreen((GuiScreen)panel);
		}catch(InstantiationException e) {
		}catch (IllegalAccessException e) {}
	}
	
	public boolean mouseClicked(GuiPixelMKSettingsPanel panel, int mouseX, int mouseY, int button, int xPos, int yPos, int spacing) {
		if (button == 0) {
			int newWidth = TAB_WIDTH;
			if (16 * this.tabs.size() > GuiPixelMKSettingsPanel.PANEL_HEIGHT) newWidth -= 18 + this.scrollbar.getWidth();
			for (SettingsPanelMenuTab tab : this.tabs) {
				if (tab.isMouseOver(newWidth, mouseX, mouseY)) {
					try {
						GuiPixelMKSettingsPanel newPanel = ((Class<GuiPixelMKSettingsPanel>)this.panels.get(tab.getLabel())).newInstance();
						this.mc.displayGuiScreen((GuiScreen)newPanel);
						return true;
					} catch (InstantiationException e) {
					} catch (IllegalAccessException e) {}
				}
			}
			boolean intersects = this.scrollbar.mouseIn(mouseX, mouseY);
			if (intersects) {
				this.scrollbar.mouseHeld = true;
				return true;
			}
			this.scrollbar.mouseHeld = false;
			return false;
		}
		return false;
	}
	
	public void renderTabs(GuiPixelMKSettingsPanel panel, int mouseX, int mouseY, float partialTicks, int xPos, int yPos, int spacing, boolean mask) {
		int tabYPosition = yPos;
		GL11.glEnable(2929);
		updateTabs();
		int newWidth = TAB_WIDTH;
		int newY = 0;
		if (19 * this.tabs.size() > GuiPixelMKSettingsPanel.PANEL_HEIGHT) {
			newWidth -= 12 + this.scrollbar.getWidth();
			newY = (int)((16 * this.tabs.size()) * this.scrollbar.getValue());
		}
		tabYPosition += 8;
		tabYPosition -= newY;
		for (SettingsPanelMenuTab tab : this.tabs) {
			tab.setActive(panel.getClass().equals(this.panels.get(tab.getLabel())));
			tab.renderTab(panel, newWidth, mouseX, mouseY, tabYPosition, mask);
			tabYPosition += 16;
		} 
		if (16 * this.tabs.size() > GuiPixelMKSettingsPanel.PANEL_HEIGHT) {
			this.scrollbar.setHeight(GuiPixelMKSettingsPanel.PANEL_HEIGHT - 8);
			this.scrollbar.render(panel, mouseY);
		} 
		GL11.glDisable(2929);
	}
	
	public void init(File f) {}
	
	public String getName() {
		return null;
	}
	
	public String getVersion() {
		return null;
	}
	
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {}
}
