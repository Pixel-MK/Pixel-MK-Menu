package com.pixelmkmenu.pixelmkmenu.login;

import net.minecraft.client.gui.GuiScreen;

public class GuiConnectingScreen extends GuiScreen implements CustomServerDataListener{
	public void onReceivedServerData(String serverIP, CustomServerDataManager manager) {}
	
	public synchronized void setServerData(String serverIP, String httpData) {}
}
