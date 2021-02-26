package com.pixelmkmenu.pixelmkmenu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import com.pixelmkmenu.pixelmkmenu.gui.GuiPixelMKMainMenu;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.ServerPinger;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.TextFormatting;

public class ThreadMainMenuInfo extends Thread {
	private final GuiPixelMKMainMenu mainMenu;
	
	private final URL updateServerURL;
	private final String ServerName;
	private final String ServerAddress;
	
	public ThreadMainMenuInfo(GuiPixelMKMainMenu guiPixelMKMainMenu, String serverName, String serverAddress) {
		this.mainMenu = guiPixelMKMainMenu;
		this.updateServerURL = null;
		this.ServerName = serverName;
		this.ServerAddress = serverAddress;
		setName("Server query Thread");
	}
	
	public ThreadMainMenuInfo(GuiPixelMKMainMenu guiPixelMKMainMenu, URL updateServerURL, String serverName, String serverAddress) {
		this.mainMenu = guiPixelMKMainMenu;
		this.updateServerURL = updateServerURL;
		this.ServerName = serverName;
		this.ServerAddress = serverAddress;
		setName("Server query Thread");
	}
	
	public void run() {
		Version serverVersion = new Version();
		try {
			if(this.updateServerURL != null) {
				URLConnection urlconnection = this.updateServerURL.openConnection();
				InputStreamReader inputStreamReader = new InputStreamReader(urlconnection.getInputStream());
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				while (true) {
					String readline = bufferedReader.readLine();
					if (readline == null) break;
					serverVersion = new Version(readline);
				}
				this.mainMenu.handleServerVersion(serverVersion);
			}
			if (this.ServerAddress != null) {
				ServerPinger serverPinger = new ServerPinger();
				ServerData serverData = new ServerData(this.ServerName, this.ServerAddress, false);
				this.mainMenu.handleServerData(serverPinger, serverData);
				try {
					serverPinger.ping(serverData);
				}catch (UnknownHostException var2) {
					serverData.pingToServer = -1L;
					serverData.serverMOTD = TextFormatting.DARK_RED + "Can't resolve hostname";
				}catch (Exception var3) {
					serverData.pingToServer = -1L;
					serverData.serverMOTD = TextFormatting.DARK_RED + "Can't connect to server";
				}
			}else {
				this.mainMenu.handleServerData(null, null);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String sanitiseResponse(String response) {
		char[] responseParts = response.toCharArray();
		for(int charIndex = 0; charIndex< responseParts.length; charIndex++) {
			if (responseParts[charIndex] != '\u00a7' && responseParts[charIndex] != '\000' && ChatAllowedCharacters.isAllowedCharacter(responseParts[charIndex])) {
				responseParts[charIndex] = '?';
			}
		}
		response = new String(responseParts);
		return response;
	}
}
