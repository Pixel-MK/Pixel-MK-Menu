/**
 * 
 */
package com.pixelmkmenu.pixelmkmenu.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import org.lwjgl.opengl.GL11;

import com.pixelmkmenu.pixelmkmenu.CustomScreenEntry;
import com.pixelmkmenu.pixelmkmenu.ForgeHandler;
import com.pixelmkmenu.pixelmkmenu.ObfuscationMapping;
import com.pixelmkmenu.pixelmkmenu.PixelMKMenuConfig;
import com.pixelmkmenu.pixelmkmenu.PixelMKMenuCore;
import com.pixelmkmenu.pixelmkmenu.ThreadMainMenuInfo;
import com.pixelmkmenu.pixelmkmenu.Version;
import com.pixelmkmenu.pixelmkmenu.controls.GuiButtonCustomScreen;
import com.pixelmkmenu.pixelmkmenu.controls.GuiButtonMainMenu;
import com.pixelmkmenu.pixelmkmenu.controls.GuiButtonMute;
import com.pixelmkmenu.pixelmkmenu.controls.GuiButtonPanel;
import com.pixelmkmenu.pixelmkmenu.gui.dialogs.GuiDialogBoxFavouriteServer;
import com.pixelmkmenu.pixelmkmenu.interfaces.IPanoramaRenderer;
import com.pixelmkmenu.pixelmkmenu.sound.SoundEffect;
import com.pixelmkmenu.pixelmkmenu.util.PrivateMethods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.ServerPinger;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Joe Targett
 *
 */
@SideOnly(Side.CLIENT)
public class GuiPixelMKMainMenu extends GuiMainMenu implements IPanoramaRenderer{
	
	private static final ResourceLocation titleTexture = new ResourceLocation("textures/gui/title/minecraft.png");
	
	private GuiButtonMainMenu btnResetDemo;
	private GuiButtonMainMenu btnConnectToServer;
	private GuiButtonMainMenu btnAboutForgeMods;
	private GuiButtonMainMenu btnTexturePack;
	private GuiButtonMainMenu btnSinglePlayer;
	private GuiButtonMainMenu btnLanguage;
	private GuiButtonMainMenu btnMultiplayer;
	private GuiButtonMainMenu btnOptions;
	
	private GuiButtonMute btnMute;
	
	private List<GuiButtonCustomScreen> customScreenButtons = new ArrayList<GuiButtonCustomScreen>();
	
	private String ModpackText = "Pixel MK modpack version 3.0.0.1";
	private String favouriteServerName;
	private String favouriteServerIP;
	private static String minecraftVersion = "";
	
	private GuiButtonPanel buttonPanelLeft;
	private GuiButtonPanel buttonPanelRight;
	
	private ServerPinger serverPinger;
	private ServerData favouriteServerData;
	
	private static List<CustomScreenEntry> customScreenClasses = new ArrayList<CustomScreenEntry>();
	
	private int updateCounter = 0;
	private static int untilNextMusicCheck = 0;
	
	private static ISound currentlyPlayingMusic;
	private ResourceLocation menuTracks = PixelMKMenuCore.MUSIC_STANDARD;
	
	static {
		try {
			Callable<String> mcVer = (Callable<String>)Class.forName(ObfuscationMapping.CallableMinecraftVersion.getName()).newInstance();
			minecraftVersion = "Minecraft " + (String)mcVer.call();
		} catch (Exception ex) {
			minecraftVersion = "Minecraft " + ForgeVersion.mcVersion;
		}
		try {
			ForgeHandler.init(minecraftVersion);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}
	
	public GuiPixelMKMainMenu(PixelMKMenuCore mod) {
		this.mc = Minecraft.getMinecraft();
		this.favouriteServerName = PixelMKMenuCore.getConfig().getStringProperty(PixelMKMenuConfig.SERVERTEXT);
		this.favouriteServerIP = PixelMKMenuCore.getConfig().getStringProperty(PixelMKMenuConfig.SERVERIP);
		updateServerInfo();
		IPanoramaRenderer previousRenderer = PixelMKMenuCore.getPanoramaRenderer();
		if (previousRenderer != null) this.updateCounter = previousRenderer.getUpdateCounter();
		
	}
	
	protected void updateModpackInfo() {
		
	}
	
	protected void updateServerInfo() {
		if(this.favouriteServerIP != null) {
			ThreadMainMenuInfo infoFetchThread = new ThreadMainMenuInfo(this, this.favouriteServerName, this.favouriteServerIP);
			infoFetchThread.start();
		}
	}
	
	public void setCustomServerIP(String name, String IP) {
		this.favouriteServerName = name;
		this.favouriteServerIP = IP;
		PixelMKMenuCore.getConfig().setProperty(PixelMKMenuConfig.SERVERTEXT, name);
		PixelMKMenuCore.getConfig().setProperty(PixelMKMenuConfig.SERVERIP, IP);
		this.btnConnectToServer.displayString = "Connect to " + ((name != null && name.length() > 0) ? name : "...");
		updateServerInfo();
	}
	
	public synchronized void handleServerData(ServerPinger serverPinger, ServerData serverData) {
		this.serverPinger = serverPinger;
		this.favouriteServerData = serverData;
	}
	
	public void tryPlayMusic() {
		SoundHandler soundHandler = this.mc.getSoundHandler();
		if(untilNextMusicCheck < 1 && !soundHandler.isSoundPlaying(currentlyPlayingMusic) && 
				PixelMKMenuCore.isMusicEnabled() && PixelMKMenuCore.hasMenuMusic()) {
			untilNextMusicCheck = 200;
			currentlyPlayingMusic = (ISound)new SoundEffect(this.menuTracks, PixelMKMenuCore.getConfig().getFloatProperty(PixelMKMenuConfig.MENUVOLUME), 1.0f);
			soundHandler.playSound(currentlyPlayingMusic);
		}
		if(!PixelMKMenuCore.isMusicEnabled() && currentlyPlayingMusic != null && soundHandler.isSoundPlaying(currentlyPlayingMusic)) {
			soundHandler.stopSound(currentlyPlayingMusic);
			currentlyPlayingMusic = null;
		}
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		this.updateCounter++;
		if(untilNextMusicCheck > 0) untilNextMusicCheck--;
		if (this.mc.currentScreen != this) return;
		if (this.serverPinger != null) this.serverPinger.pingPendingNetworks();
		tryPlayMusic();
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		if(this.buttonPanelLeft == null) {
			this.buttonPanelLeft = new GuiButtonPanel(100, GuiButtonPanel.AnchorType.BottomLeft, 12,
					20, 150, 100, 16, this.width, this.height, "left");
			this.buttonPanelRight = new GuiButtonPanel(200, GuiButtonPanel.AnchorType.BottomRight, 12,
					20, 150, 100, 16, this.width, this.height, "right");
			initPanelButtons();
		}else {
			this.buttonPanelLeft.updatePosition(this.width, this.height);
			this.buttonPanelRight.updatePosition(this.width, this.height);
		}
		this.buttonList.add(this.buttonPanelLeft);
		this.buttonList.add(this.buttonPanelRight);
		this.buttonList.add(this.btnMute = new GuiButtonMute(300, this.width- 24, 4));
		this.btnMute.muted = !PixelMKMenuCore.isMusicEnabled();
		this.btnTexturePack.visible = PixelMKMenuCore.getConfig().getBoolProperty(PixelMKMenuConfig.SHOWTPONMENU);
		this.btnAboutForgeMods.visible = true;
		PixelMKMenuCore.setPanoramaRenderer(this);
	}
	
	
	protected void initPanelButtons() {
		if (this.mc.isDemo()) {
			this.buttonPanelLeft.addButton(I18n.format("menu.playdemo", new Object[0]), 11);
			this.btnResetDemo = this.buttonPanelLeft.addButton(I18n.format("menu.resetdemo", new Object[0]), 12);
			ISaveFormat var10 = this.mc.getSaveLoader();
			WorldInfo var5 = var10.getWorldInfo("Demo_World");
			if(var5 == null) this.btnResetDemo.enabled = false;
		}else {
			this.btnSinglePlayer = this.buttonPanelLeft.addButton(I18n.format("menu.singleplayer", new Object[0]));
			this.btnMultiplayer = this.buttonPanelRight.addButton(I18n.format("menu.multiplayer", new Object[0]));
			this.buttonPanelLeft.addButton(I18n.format("menu.online", new Object[0]), 14);
			String buttonText = "Connect to " + ((this.favouriteServerName != null && this.favouriteServerName.length() > 0) ? this.favouriteServerName : "...");
			this.btnConnectToServer = this.buttonPanelLeft.addButton(buttonText);
		}
		this.btnOptions = this.buttonPanelLeft.addButton(I18n.format("menu.options", new Object[0]));
		this.buttonPanelLeft.addButton(I18n.format("menu.quit", new Object[0]), 4);
		for (CustomScreenEntry customScreen : customScreenClasses) {
			if (this.buttonPanelLeft.tagMatches(customScreen.getPanelName())) {
				this.customScreenButtons.add(this.buttonPanelLeft.addButton(customScreen));
				continue;
			}
			if (this.buttonPanelRight.tagMatches(customScreen.getPanelName())) {
				this.customScreenButtons.add(this.buttonPanelRight.addButton(customScreen));
			}
		}
		this.btnAboutForgeMods = this.buttonPanelRight.addButton(I18n.format("fml.menu.mods", new Object[0]));
		this.btnTexturePack = this.buttonPanelRight.addButton(I18n.format("options.resourcepack", new Object[0]));
		this.btnLanguage = this.buttonPanelRight.addButton(I18n.format("options.language", new Object[0]));
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		try {
			super.mouseClicked(mouseX, mouseY, button);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(button == 1 && this.buttonPanelLeft.mousePressed(this.mc, mouseX, mouseY) &&
				(this.buttonPanelLeft.getPressedButton()).id == this.btnConnectToServer.id)
			this.mc.displayGuiScreen((GuiScreen)new GuiDialogBoxFavouriteServer(this, this.favouriteServerName, this.favouriteServerIP));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		GuiButtonMainMenu guiButtonMainMenu;
		if ((guiButton).id == this.btnMute.id) {
			PixelMKMenuCore.setMusicState(this.btnMute.muted);
			this.btnMute.muted = !this.btnMute.muted;
			if(!this.btnMute.muted) untilNextMusicCheck = 0;
			PixelMKMenuCore.getConfig().setProperty(PixelMKMenuConfig.MUTE, this.btnMute.muted);
		}
		if(guiButton.id == this.buttonPanelLeft.id) {
			guiButtonMainMenu = this.buttonPanelLeft.getPressedButton();
		} else if (guiButton.id == this.buttonPanelRight.id) {
			guiButtonMainMenu = this.buttonPanelRight.getPressedButton();
		} else {
			return;
		}
		try {
			super.actionPerformed((GuiButton)guiButtonMainMenu);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (((GuiButton)guiButtonMainMenu).id == this.btnSinglePlayer.id)
			this.mc.displayGuiScreen((GuiScreen)new GuiWorldSelection((GuiScreen)this));
		if (((GuiButton)guiButtonMainMenu).id == this.btnMultiplayer.id)
			this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
		if (this.btnTexturePack != null && ((GuiButton)guiButtonMainMenu).id == this.btnTexturePack.id)
			this.mc.displayGuiScreen((GuiScreen)new GuiScreenResourcePacks((GuiScreen)this));
		if (((GuiButton)guiButtonMainMenu).id == this.btnOptions.id)
			this.mc.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.mc.gameSettings));
		if (((GuiButton)guiButtonMainMenu).id == this.btnLanguage.id) 
			this.mc.displayGuiScreen((GuiScreen)new GuiLanguage((GuiScreen)this, this.mc.gameSettings, this.mc.getLanguageManager()));
		if (((GuiButton)guiButtonMainMenu).id == this.btnConnectToServer.id) {
			if (this.favouriteServerIP != null && this.favouriteServerIP.length() > 0) {
				ServerData serverData = new ServerData(this.favouriteServerName, this.favouriteServerIP, false);
				if(!ForgeHandler.connectToServer((GuiScreen)this, this.mc, serverData)) {
					this.mc.displayGuiScreen((GuiScreen)new GuiConnecting((GuiScreen)this, this.mc, serverData));
				}
			}else {
					this.mc.displayGuiScreen((GuiScreen)new GuiDialogBoxFavouriteServer(this,
							this.favouriteServerName, this.favouriteServerIP));
			}
		}
		if (this.btnAboutForgeMods != null && ((GuiButton)guiButtonMainMenu).id == this.btnAboutForgeMods.id) {
			this.mc.displayGuiScreen((GuiScreen)new GuiModList((GuiScreen)this));
		}
		if (guiButtonMainMenu instanceof GuiButtonCustomScreen) {
			GuiButtonCustomScreen customScreenButton = (GuiButtonCustomScreen)guiButtonMainMenu;
			customScreenButton.invoke(this.mc);
		}
	}
	
	@Override
	public void confirmClicked(boolean result, int id) {
		if(id == 0) this.mc.displayGuiScreen((GuiScreen)this);
	}
	
	public void drawToolTip(int mouseX, int mouseY, int yOffset, int xSize, int ySize, String text) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bb = tessellator.getBuffer();
		drawRect(mouseX + 14, mouseY, mouseX + xSize, mouseY + ySize, 1610612736);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		bb.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
		bb.pos((mouseX + 14), (mouseY + ySize), 0.0d).endVertex();
		bb.pos((mouseX+14), mouseY, 0.0d).endVertex();
		bb.pos(mouseX, (mouseY + yOffset), 0.0d).endVertex();
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		if (text != null) this.fontRenderer.drawString(text, mouseX + 16, mouseY + 4, 16777215);
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.buttonPanelLeft.updateButtons(this.updateCounter, partialTicks, mouseX, mouseY);
		this.buttonPanelRight.updateButtons(this.updateCounter, partialTicks, mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (this.favouriteServerData != null && this.btnConnectToServer != null && 
				this.buttonPanelLeft.isMouseOver(this.btnConnectToServer, this.mc, mouseX, mouseY)) {
			String text = this.favouriteServerData.serverMOTD;
			if(this.favouriteServerData.pingToServer > -1L && this.favouriteServerData.populationInfo != null) {
				text = "Player Count: " + this.favouriteServerData.populationInfo;
			}
			drawToolTip(this.buttonPanelLeft.getAdjustedXPosition(this.btnConnectToServer) +150,
					this.buttonPanelLeft.getAdjustedYPosition(this.btnConnectToServer) + 10, -4,
					this.fontRenderer.getStringWidth(text) + 18, 16, text);
		}
		List<String> fmlBrandings = ForgeHandler.getBrandings();
		if (fmlBrandings != null && fmlBrandings.size() > 0 && mouseX < 80 && mouseY > this.height - 16) {
			int height = fmlBrandings.size() * 10 + 6;
			drawToolTip(80, this.height - height - 16, height + 10, 200, height, (String)null);
			int top = this.height - height - 12;
			for (String brand : fmlBrandings) {
				drawString(this.fontRenderer, brand, 100, top, -1);
				top += 10;
			}
		}
		if(PixelMKMenuCore.inModpack) {
			//Modpack branding in top left
			drawString(this.fontRenderer, PixelMKMenuCore.getModpackName(), 2, 2, 16777215);
			if(mouseX < 87 && mouseY < 9) {
				String NameAndVer = PixelMKMenuCore.getModpackName() + " version " + PixelMKMenuCore.getModpackVer();
				drawToolTip(mouseX+4, mouseY+13, -7, (int)(this.fontRenderer.getStringWidth(NameAndVer) * 1.2), 16, NameAndVer);
			}
		}
	}
	
	public static void registerCustomScreen(String panelName, Class<? extends GuiScreen> customScreenClass, String customScreenText) {
		if (!panelName.equalsIgnoreCase("left") && 
				!panelName.equalsIgnoreCase("right")) throw new IllegalArgumentException("Invalid panel name specified in registerCustomScreen");
		customScreenClasses.add(new CustomScreenEntry(panelName, customScreenClass, customScreenText));
	}
	
	public void drawTexturedModalRect(int x, int y, int x2, int y2, int u, int v, int u2, int v2) {
		float texMapScale = 9.765625E-4f;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bb = tessellator.getBuffer();
		bb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bb.pos(x, y2, 0.0d).tex((u * texMapScale), (v2 * texMapScale)).endVertex();
		bb.pos(x2, y2, 0.0d).tex((u2 * texMapScale), (v2 * texMapScale)).endVertex();
		bb.pos(x2, y, 0.0d).tex((u2 * texMapScale), (v * texMapScale)).endVertex();
		bb.pos(x, y, 0.0d).tex((u * texMapScale), (v * texMapScale)).endVertex();
		tessellator.draw();
	}
	
	@Override
	public boolean renderPanorama(int mouseX, int mouseY, float partialTicks) {
		try {
			PrivateMethods.mainMenuRenderSkyBox.invoke(this, new Object[] { Integer.valueOf(mouseX), Integer.valueOf(mouseY), Float.valueOf(partialTicks)});
			drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
			drawGradientRect(0, 0, this.width, this.height, 0, -2147483648);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void setPanoramaResolution(Minecraft minecraft, int width, int height) {
		setWorldAndResolution(minecraft, width, height);
	}

	
	@Override
	public void initPanoramaRenderer() {
		initGui();
	}

	@Override
	public void updatePanorama() {
		if (this.mc.currentScreen != null && (this.mc.currentScreen.width != this.width || this.mc.currentScreen.height != this.height)) {
			setWorldAndResolution(this.mc, this.mc.currentScreen.width, this.mc.currentScreen.height);
			initPanoramaRenderer();
		}
		updateScreen();
	}
	
	@Override
	public int getUpdateCounter() {
		return this.updateCounter;
	}

	//TODO implement auto-update
	public void handleServerVersion(Version serverVersion) {}
	
	public String getMinecraftVersion() {
		return minecraftVersion.replace("Minecraft ", "");
	}
	
	

}
