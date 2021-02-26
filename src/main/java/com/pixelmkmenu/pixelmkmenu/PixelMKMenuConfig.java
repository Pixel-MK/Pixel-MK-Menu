package com.pixelmkmenu.pixelmkmenu;

import com.pixelmkmenu.pixelmkmenu.fx.ScreenTransition;

import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.config.Configuration;
import scala.actors.threadpool.Arrays;;

public class PixelMKMenuConfig {
	
	public boolean SHOWTPONMENU = false;
	public boolean USEPRETTYINGAMEMENU = true;
	public boolean USEPRETTYTPSCREEN = true;
	public boolean MUTE = false;
	public boolean TRANSITIONS = true;
	public boolean FUNKY = false;
	public boolean HIMOTION = false;
	public float TRANSITIONRATE = 1.0f;
	public float MENUVOLUME = 0.5f;
	public String SERVERTEXT = "";
	public String SERVERIP = "";
	public String[] ENABLED_TRANSITIONS = {"fadebasic"};
	public String[] ENABLED_HIMOTION_TRANSITIONS = {};
	
	public void init(Configuration config) {
		this.SHOWTPONMENU = config.getBoolean("Show Resource packs on menu", "all", false, "If true, Resouce pack button will appear on the main menu");
		this.USEPRETTYINGAMEMENU = config.getBoolean("Pause menu", "all", true, "If false, default pause menu will be used");
		this.USEPRETTYTPSCREEN = config.getBoolean("Pretty Resource pack menu", "all", true, "If false, default resourcepack menu will be used");
		this.MUTE = config.getBoolean("Mute main menu", "all", false, "Set to true to stop music from playing");
		this.TRANSITIONS = config.getBoolean("Enable transitions", "all", true, "If false, no transitions will be played between menues");
		this.FUNKY = config.getBoolean("Use all transitions", "all", false, "Enable all transitions, except Hi-motion");
		this.HIMOTION = config.getBoolean("Use Hi-motion transitions", "all", false, "Enable Hi-motion transitions, not recommened for people with epilepsy");
		this.TRANSITIONRATE = config.getFloat("Transition rate", "all", 1.0f, 0.1f, 10.0f, "Rate which transitions move. Between 0.1-10.0");
		this.MENUVOLUME = config.getFloat("Menu Volume", "all", 0.5f, 0.0f, 1.0f, "The volume of the menu as a multiplier of its original volume. Between 1.0 and 0.0");
		this.SERVERTEXT = config.getString("Favourite Server Name", "favourite server", "", "Name of your favourite server, will appear on main menu");
		this.SERVERIP = config.getString("Favourite Server IP", "favourite server", "", "IP address of your favourite server");
		String[] DefaultTransitions = {"fadebasic"};
		String[] DefaultHiMotion = {}; 
		this.ENABLED_TRANSITIONS = config.getStringList("Enabled transitions", "all", DefaultTransitions, "List of enabled transitions");
		this.ENABLED_HIMOTION_TRANSITIONS = config.getStringList("Enabled Hi-motion transitions", "all", DefaultHiMotion, "Enabled Hi-motion transitions");
	}
	
	public boolean isTransitionEnabled(ScreenTransition transition) {
		return isTransitionEnabled(transition.isHighMotion(), transition.getName());
	}
	
	public boolean isTransitionEnabled(boolean highMotion, String transitionName) {
		if (highMotion) return (HIMOTION && Arrays.asList(ENABLED_HIMOTION_TRANSITIONS).contains(transitionName));
		return Arrays.asList(ENABLED_TRANSITIONS).contains(transitionName);
	}
}
