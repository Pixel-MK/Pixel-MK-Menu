package com.pixelmkmenu.pixelmkmenu;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.pixelmkmenu.pixelmkmenu.fx.ScreenTransition;
import com.pixelmkmenu.pixelmkmenu.util.ModConfig;

import net.minecraftforge.fml.common.Loader;

public class PixelMKMenuConfig extends ModConfig {

	public static String UPGRADED = "upgraded";

	public static String MENUVOLUME = "Menu_Volume";

	public static String SHOWTPONMENU = "Show_Resource_Packs_On_Main_Menu";

	public static String USEPRETTYTPSCREEN = "Pretty_Resource_Pack_Menu";

	public static String USEPRETTYINGAMEMENU = "Fancy_Pause_Menu";

	public static String SERVERTEXT = "Favourite_Server_Name";

	public static String SERVERIP = "serverIP";

	public static String MUTE = "Mute_Main_Menu";

	public static String TRANSITIONS = "Enable_Transitions";

	public static String FUNKY = "Use_All_Transitions";

	public static String HIMOTION = "Use_Hi-motion_Transitions";

	public static String TRANSITIONRATE = "Transition_Rate";

	public static String ENABLED_TRANSITIONS = "Enabled_Transitions";

	public static String ENABLED_HIMOTION_TRANSITIONS = "Enabled_Hi-motion_transitions";

	public PixelMKMenuConfig() {
		super("PixelMKMenu", "pixelmkmenu.cfg");
		if (!getBoolProperty(UPGRADED)) {
			handleUpgradeSettings();
			setProperty(UPGRADED, true);
		} 
	}

	protected void handleUpgradeSettings() {
		try {
			File settingsPath = new File(Loader.instance().getConfigDir(), "PixelMKMenu");
			if(settingsPath.exists()) {
				try {
					File serverPropertiesFile = new File(settingsPath, "VoxelServer.properties");
					if (serverPropertiesFile.exists()) {
						Properties serverProperties = new Properties();
						serverProperties.load(new FileReader(serverPropertiesFile));
						setProperty(SERVERTEXT, serverProperties.getProperty("Text", getStringProperty(SERVERTEXT)));
						setProperty(SERVERIP, serverProperties.getProperty("IP", getStringProperty(SERVERIP)));
					}
				} catch (IOException ex) {}
				try {
					File menuPropertiesFile = new File(settingsPath, "PixelMKMenu.cfg");
					if (menuPropertiesFile.exists()) {
						Properties menuProperties = new Properties();
						menuProperties.load(new FileReader(menuPropertiesFile));
						setProperty(SHOWTPONMENU, "true".equals(menuProperties.getProperty("ShowTexturePacks", getStringProperty(SHOWTPONMENU))));
						setProperty(USEPRETTYINGAMEMENU, "true".equals(menuProperties.getProperty("CustomInGameMenu", getStringProperty(USEPRETTYINGAMEMENU))));
						setProperty(USEPRETTYTPSCREEN, "true".equals(menuProperties.getProperty("CustomTexturePacksScreen", getStringProperty(USEPRETTYTPSCREEN))));
						setProperty(MENUVOLUME, menuProperties.getProperty("Volume", getStringProperty(MENUVOLUME)));
					}
				} catch (IOException e) {}
			}
		} catch (Exception e) {e.printStackTrace();}
	}

	public boolean enableHiMotion() {
		return getBoolProperty(HIMOTION);
	}

	public boolean isTransitionEnabled(ScreenTransition transition) {
		return isTransitionEnabled(transition.isHighMotion(), transition.getName());
	}

	public boolean isTransitionEnabled(boolean highMotion, String transitionName) {
		if (highMotion) return (enableHiMotion() && getStringProperty(ENABLED_HIMOTION_TRANSITIONS).toLowerCase().contains(transitionName));
		return getStringProperty(ENABLED_TRANSITIONS).toLowerCase().contains(transitionName);
	}

	protected void setDefaults() {
		this.defaults.put(UPGRADED, "false");
		this.defaults.put(SERVERTEXT, "");
		this.defaults.put(SERVERIP, "");
		this.defaults.put(MENUVOLUME, "0.5");
		this.defaults.put(SHOWTPONMENU, "false");
		this.defaults.put(USEPRETTYTPSCREEN, "true");
		this.defaults.put(USEPRETTYINGAMEMENU, "true");
		this.defaults.put(MUTE, "false");
		this.defaults.put(TRANSITIONS, "true");
		this.defaults.put(FUNKY, "false");
		this.defaults.put(HIMOTION, "false");
		this.defaults.put(TRANSITIONRATE, "1.0");
		this.defaults.put(ENABLED_TRANSITIONS, "fadebasic,fadegrow,fadedrop,fadeslide");
		this.defaults.put(ENABLED_HIMOTION_TRANSITIONS, "drop,slide,tiles1,tiles2,rotate,blinds,ripple,spiral");
	}
}
