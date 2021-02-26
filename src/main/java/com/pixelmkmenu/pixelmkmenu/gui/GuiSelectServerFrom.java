package com.pixelmkmenu.pixelmkmenu.gui;

import java.io.IOException;

import com.pixelmkmenu.pixelmkmenu.PrivateFields;
import com.pixelmkmenu.pixelmkmenu.gui.dialogs.GuiDialogBoxFavouriteServer;

import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.gui.GuiButton;

public class GuiSelectServerFrom extends GuiMultiplayer {
	private GuiDialogBoxFavouriteServer parentScreen;
	
	public GuiSelectServerFrom(GuiDialogBoxFavouriteServer parentScreen) {
		super((GuiScreen)parentScreen);
		this.parentScreen = parentScreen;
	}
	
	public void createButtons() {
		super.createButtons();
		for (int i = 0; i < this.buttonList.size(); i++) {
			((GuiButton)this.buttonList.get(i)).y = -30;
			((GuiButton)this.buttonList.get(i)).enabled = false;
		}
		this.buttonList.add(new GuiButton(100, this.width / 2 -104, this.height - 52, 100, 20, "Use this server"));
		this.buttonList.add(new GuiButton(101, this.width / 2 +4, this.height - 52, 100, 20, "Cancel"));
	}
	
	protected void keyTyped(char typedChar, int keyCode) {
		if (typedChar == '\r') {
			selectServer();
			this.mc.displayGuiScreen((GuiScreen)this.parentScreen);
		}else {
			try {
				super.keyTyped(typedChar, keyCode);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void selectServer() {
		try {
			int selectedServer = ((ServerSelectionList)PrivateFields.serverSelectionList.get(this)).getSelected();
			ServerList serverList = (ServerList)PrivateFields.internetServerList.get(this);
			ServerData data = serverList.getServerData(selectedServer);
			this.parentScreen.setCustomServerIP(data.serverName, data.serverIP);
		} catch (Exception ex) {}
	}
	
	public void connectToSelected() {}
	
	protected void actionPerformed(GuiButton button) {
		if (button.id == 100) {
			selectServer();
			this.mc.displayGuiScreen((GuiScreen)this.parentScreen);
		} else if (button.id == 101) {
			this.mc.displayGuiScreen((GuiScreen)this.parentScreen);
		}
	}
	
	
}
