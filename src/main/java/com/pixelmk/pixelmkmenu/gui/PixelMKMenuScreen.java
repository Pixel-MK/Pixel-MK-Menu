package com.pixelmk.pixelmkmenu.gui;

import java.io.IOException;

import com.google.common.util.concurrent.Runnables;
import com.mojang.blaze3d.vertex.PoseStack;
import com.pixelmk.pixelmkmenu.PixelMKMenu;
import com.pixelmk.pixelmkmenu.PixelMKMenuClient;
import com.pixelmk.pixelmkmenu.controls.ActionInstance;
import com.pixelmk.pixelmkmenu.controls.ButtonAction;
import com.pixelmk.pixelmkmenu.controls.ButtonMute;
import com.pixelmk.pixelmkmenu.controls.ButtonPanel;
import com.pixelmk.pixelmkmenu.controls.GuiButtonMainMenu;
import com.pixelmk.pixelmkmenu.controls.ScreenType;
import com.pixelmk.pixelmkmenu.gui.dialogboxes.DialogBoxFavouriteServer;
import com.pixelmk.pixelmkmenu.helpers.ForgeHelper;
import com.pixelmk.pixelmkmenu.helpers.PixelMKMenuSoundEvents;
import com.pixelmk.pixelmkmenu.helpers.PixelMKMusicManager;
import com.pixelmk.pixelmkmenu.interfaces.IPanoramaRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.multiplayer.ServerStatusPinger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.versions.mcp.MCPVersion;

public class PixelMKMenuScreen extends TitleScreen {

	public static final ResourceLocation BACKGROUND = new ResourceLocation("textures/gui/background.png");
	private int updateCounter;
	private int untilNextMusicCheck;
	private ServerStatusPinger serverPinger;
	private Minecraft mc;
	private ButtonPanel buttonPanelLeft;
	private ButtonPanel buttonPanelRight;
	private GuiButtonMainMenu btnSinglePlayer;
	private GuiButtonMainMenu btnMultiplayer;
	private GuiButtonMainMenu btnOptions;
	private GuiButtonMainMenu btnAboutForgeMods;
	private GuiButtonMainMenu btnTexturePack;
	private GuiButtonMainMenu btnLanguage;
	private Tooltip modpackTooltip;
	private Tooltip BrandingsTooltip;
	private Music currentlyPlayingMusic;
	private String favouriteServerName;
	private GuiButtonMainMenu btnConnectToServer;
	public static ButtonMute btnMute;
	public static final Music MENU_MUSIC = new Music(PixelMKMenuSoundEvents.MUSIC_MENU.get(), 20, 600, true);

	public PixelMKMenuScreen(boolean fade) {
		this.mc = Minecraft.getInstance();
		ForgeHelper.init();
		// this.favouriteServerName =
		// PixelMKMenuCore.getConfig().getStringProperty(PixelMKMenuConfig.SERVERTEXT);
		// this.favouriteServerIP =
		// PixelMKMenuCore.getConfig().getStringProperty(PixelMKMenuConfig.SERVERIP);
		updateServerInfo();
		IPanoramaRenderer previousRenderer = PixelMKMenu.getPanoramaRenderer();
		if (previousRenderer != null)
			this.updateCounter = previousRenderer.getUpdateCounter();
	}

	public PixelMKMenuScreen() {
		this(true);
	}

	@Override
	protected void init() {
		super.init();
		this.renderables.clear();
		this.children().clear();
		if (PixelMKMenuClient.BUTTON_MANAGER.getButtons().isEmpty())
			this.addDefaultButtons();
		else
			PixelMKMenuClient.BUTTON_MANAGER.getButtons().forEach(button -> {
				this.addRenderableWidget(button).setup(this);
			});
		if (PixelMKMenuClient.inModpack) {
			this.modpackTooltip = new Tooltip(PixelMKMenuClient.instance.getModpackName() + " version "
					+ PixelMKMenuClient.instance.getModpackVersion(), PixelMKMenuClient.instance.getModpackName(), 2,
					2, -7);
			this.addRenderableWidget(this.modpackTooltip);
		}

		this.BrandingsTooltip = new Tooltip(ForgeHelper.getBrandings(), "Minecraft " + MCPVersion.getMCVersion(), 2,
				this.height - 10, 200, 0, 10);
		this.addRenderableWidget(this.BrandingsTooltip);

		this.minecraft.setConnectedToRealms(false);

		int txtWidth = this.font.width(COPYRIGHT_TEXT);
		int leftPos = this.width - txtWidth - 3;
		this.addRenderableWidget(
				new PlainTextButton(leftPos, this.height - 10, txtWidth, 10, COPYRIGHT_TEXT, (p_211790_) -> {
					this.minecraft.setScreen(new WinScreen(false, Runnables.doNothing()));
				}, this.font));
	}

	private void addDefaultButtons() {

		if (this.buttonPanelLeft == null) {
			this.buttonPanelLeft = new ButtonPanel(ButtonPanel.AnchorType.BottomLeft, 12,
					20, 150, 100, 16, this.width, this.height, "left", new ActionInstance(ButtonAction.NONE, null));
			this.buttonPanelRight = new ButtonPanel(ButtonPanel.AnchorType.BottomRight, 12,
					20, 150, 100, 16, this.width, this.height, "right", new ActionInstance(ButtonAction.NONE, null));
			initPanelButtons();
		} else {
			this.buttonPanelLeft.updatePosition(this.width, this.height);
			this.buttonPanelRight.updatePosition(this.width, this.height);
		}
		this.addRenderableWidget(this.buttonPanelLeft);
		this.addRenderableWidget(this.buttonPanelRight);

		btnMute = new ButtonMute(this.width - 24, 4, new ActionInstance(ButtonAction.NONE, null));
		this.addRenderableWidget(btnMute);
	}

	private boolean checkDemoWorldPresence() {
		try {
			LevelStorageSource.LevelStorageAccess levelstoragesource$levelstorageaccess = this.minecraft
					.getLevelSource().createAccess("Demo_World");

			boolean flag;
			try {
				flag = levelstoragesource$levelstorageaccess.getSummary() != null;
			} catch (Throwable throwable1) {
				if (levelstoragesource$levelstorageaccess != null) {
					try {
						levelstoragesource$levelstorageaccess.close();
					} catch (Throwable throwable) {
						throwable1.addSuppressed(throwable);
					}
				}

				throw throwable1;
			}

			if (levelstoragesource$levelstorageaccess != null) {
				levelstoragesource$levelstorageaccess.close();
			}

			return flag;
		} catch (IOException ioexception) {
			SystemToast.onWorldAccessFailure(this.minecraft, "Demo_World");
			PixelMKMenu.LOGGER.warn("Failed to read demo world data", (Throwable) ioexception);
			return false;
		}
	}

	protected void initPanelButtons() {
		if (this.mc.isDemo()) {
			this.buttonPanelLeft.addButton("menu.playdemo",
					new ActionInstance(ButtonAction.DEMO_WORLD, this.checkDemoWorldPresence()));
			// this.btnResetDemo =
			// this.buttonPanelLeft.addButton(I18n.format("menu.resetdemo", new Object[0]),
			// 12);
			// ISaveFormat var10 = this.mc.getSaveLoader();
			// WorldInfo var5 = var10.getWorldInfo("Demo_World");
			// if(var5 == null) this.btnResetDemo.enabled = false;
		} else {
			this.btnSinglePlayer = this.buttonPanelLeft.addButton("menu.singleplayer",
					new ActionInstance(ButtonAction.OPEN_GUI, ScreenType.SINGLEPLAYER));
			this.btnMultiplayer = this.buttonPanelRight.addButton("menu.multiplayer",
					new ActionInstance(ButtonAction.OPEN_GUI, ScreenType.MULTIPLAYER));
			String buttonText = "Connect to "
					+ ((this.favouriteServerName != null && this.favouriteServerName.length() > 0)
							? this.favouriteServerName
							: "...");
			this.btnConnectToServer = this.buttonPanelLeft.addButton(buttonText, (button) -> {
				this.minecraft.setScreen(new DialogBoxFavouriteServer(this, "", ""));
			});
		}
		this.btnOptions = this.buttonPanelLeft.addButton("menu.options",
				new ActionInstance(ButtonAction.OPEN_GUI, ScreenType.OPTIONS));
		this.buttonPanelLeft.addButton("menu.quit", new ActionInstance(ButtonAction.QUIT, null));
		// for (CustomScreenEntry customScreen : customScreenClasses) {
		// if (this.buttonPanelLeft.tagMatches(customScreen.getPanelName())) {
		// this.customScreenButtons.add(this.buttonPanelLeft.addButton(customScreen));
		// continue;
		// }
		// if (this.buttonPanelRight.tagMatches(customScreen.getPanelName())) {
		// this.customScreenButtons.add(this.buttonPanelRight.addButton(customScreen));
		// }
		// }
		this.btnAboutForgeMods = this.buttonPanelRight.addButton("fml.menu.mods",
				new ActionInstance(ButtonAction.OPEN_GUI, ScreenType.MODS));
		this.btnLanguage = this.buttonPanelRight.addButton("options.language",
				new ActionInstance(ButtonAction.OPEN_GUI, ScreenType.LANGUAGE));
	}

	@Override
	public void tick() {
		super.tick();
		++this.updateCounter;
		if (untilNextMusicCheck > 0)
			--untilNextMusicCheck;
		if (this.mc.screen != this)
			return;
		// if (this.serverPinger != null) this.serverPinger.pingPendingNetworks();
		PixelMKMusicManager.tick();
	}

	private void updateServerInfo() {
	}

	@Override
	public void render(PoseStack pose, int mouseX, int mouseY, float partialTicks) {
		this.buttonPanelLeft.updateButtons(this.updateCounter, partialTicks, mouseX, mouseY);
		this.buttonPanelRight.updateButtons(this.updateCounter, partialTicks, mouseX, mouseY);
		super.render(pose, mouseX, mouseY, partialTicks);
		// if (this.favouriteServerData != null && this.btnConnectToServer != null &&
		// this.buttonPanelLeft.isMouseOver(this.btnConnectToServer, this.mc, mouseX,
		// mouseY)) {
		// String text = this.favouriteServerData.serverMOTD;
		// if(this.favouriteServerData.pingToServer > -1L &&
		// this.favouriteServerData.populationInfo != null) {
		// text = "Player Count: " + this.favouriteServerData.populationInfo;
		// }
		// drawToolTip(this.buttonPanelLeft.getAdjustedXPosition(this.btnConnectToServer)
		// +150,
		// this.buttonPanelLeft.getAdjustedYPosition(this.btnConnectToServer) + 10, -4,
		// this.fontRenderer.getStringWidth(text) + 18, 16, text);
		// }
	}
}
