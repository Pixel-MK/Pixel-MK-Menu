package com.pixelmkmenu.pixelmkmenu.login;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pixelmkmenu.pixelmkmenu.util.PrivateFields;

import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;

public class CustomServerDataManager {
	private Pattern customDataPattern = Pattern.compile("\\xa7c\\xa7b\\xa7a((\\xa7[0-9a-f])+)\\xa7c\\xa7b\\xa7a", 2);
	
	private Map<String, HashMap<String, String>> serverCustomData = new HashMap<String, HashMap<String, String>>();
	
	private Set<CustomServerDataListener> listeners = new HashSet<CustomServerDataListener>();
	
	public void addListener(CustomServerDataListener listener) {
		this.listeners.add(listener);
	}
	
	public void updateCustomData(GuiMultiplayer currentScreen) {
		ServerList internetServerList = (ServerList)PrivateFields.internetServerList.get(currentScreen);
		for (int num = 0; num < internetServerList.countServers(); num++) {
			ServerData serverData = internetServerList.getServerData(num);
			if (serverData != null && serverData.serverMOTD != null) {
				Matcher dataMatcher = this.customDataPattern.matcher(serverData.serverMOTD);
				if (dataMatcher.find() && !this.serverCustomData.containsKey(serverData.serverIP)) {
					String data = decodeHex(dataMatcher.group(1).replace("§", ""));
					if(!this.serverCustomData.containsKey(serverData.serverIP)) 
						this.serverCustomData.put(serverData.serverIP, new HashMap<String, String>());
					String[] entries = data.split(";");
					for (String entry : entries) {
						if (entry.matches("^([a-z0-9_]+)={.*)$")) {
							System.out.println(entry);
							String[] kv = entry.split("=", 2);
							((HashMap<String, String>)this.serverCustomData.get(serverData.serverIP)).put(kv[0], kv[1]);
						}
					}
					onReceivedServerData(serverData.serverIP);
				}
			}
		}
	}
	
	protected void onReceivedServerData(String serverIP) {
		for (CustomServerDataListener listener : this.listeners)
			listener.onReceivedServerData(serverIP, this);
	}
	
	public String getValue(String serverIP, String key, String defaultValue) {
		if (this.serverCustomData.containsKey(serverIP))
			return ((HashMap)this.serverCustomData.get(serverIP)).containsKey(key) ? (String)((HashMap)this.serverCustomData.get(serverIP)).get(key) : defaultValue;
		return defaultValue;
	}
	
	public static String decodeHex(String hexString) {
		StringBuilder sb = new StringBuilder();
		for(int pos = 0; pos < hexString.length(); pos += 2) sb.append((char)Integer.parseInt(hexString.substring(pos, pos+2), 16));
		return sb.toString();
	}
}
