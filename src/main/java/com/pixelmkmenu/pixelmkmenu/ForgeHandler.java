package com.pixelmkmenu.pixelmkmenu;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.ServerPinger;

public class ForgeHandler {
	
	private static Class<?> forgeCommonHandlerClass;
	private static Class<?> forgeClientHandlerClass;
	private static Class guiModListClass;
	
	private static Field fmlBrandingsField;
	private static Field startupConnectionDataField;
	
	private static Method mGetCommonInstance;
	private static Method mGetClientInstance;
	private static Method mGetBrandings;
	private static Method mSetupServerList;
	private static Method mConnectToServer;
	
	private static Object fmlCommonHandler = null;
	private static Object fmlClientHandler = null;
	
	private static boolean forgeDetected = false;
	
	private static List<String> fmlBrandings = null;
	private static List<String> emptyBrandingsList = new ArrayList<String>();
	
	private static Constructor<GuiScreen> guiModListCtor;
	
	private static CountDownLatch startupConnectionData;
	
	public static void init(String minecraftVersion) throws SecurityException, NoSuchMethodException, IllegalArgumentException,
	IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchFieldException{
		forgeCommonHandlerClass = Class.forName("net.minecraftforge.fml.common.FMLCommonHandler");
		if (forgeCommonHandlerClass != null) {
			forgeDetected = true;
			fmlBrandingsField = forgeCommonHandlerClass.getDeclaredField("brandings");
			fmlBrandingsField.setAccessible(true);
			mGetCommonInstance = forgeCommonHandlerClass.getDeclaredMethod("instance", new Class[0]);
			mGetBrandings = forgeCommonHandlerClass.getDeclaredMethod("getBrandings", new Class[] {boolean.class});
			fmlCommonHandler = mGetCommonInstance.invoke(null, new Object[0]);
			emptyBrandingsList.add(minecraftVersion);
			guiModListClass = Class.forName("net.minecraftforge.fml.client.GuiModList");
			guiModListCtor = guiModListClass.getDeclaredConstructor(new Class[] {GuiScreen.class});
			if (fmlBrandingsField != null && mGetBrandings != null && fmlCommonHandler != null) {
				fmlBrandings = (List<String>)mGetBrandings.invoke(fmlCommonHandler, new Object[] {Boolean.valueOf(true)});
				fmlBrandingsField.set(fmlCommonHandler, emptyBrandingsList);
			}
			forgeClientHandlerClass = Class.forName("net.minecraftforge.fml.client.FMLClientHandler");
			mGetClientInstance = forgeClientHandlerClass.getDeclaredMethod("instance", new Class[0]);
			mConnectToServer = forgeClientHandlerClass.getDeclaredMethod("connectToServer", new Class[] {GuiScreen.class, ServerData.class});
			mSetupServerList = forgeClientHandlerClass.getDeclaredMethod("setupServerList", new Class[0]);
			startupConnectionDataField = forgeClientHandlerClass.getDeclaredField("startupConnectionData");
			startupConnectionDataField.setAccessible(true);
			startupConnectionData = (CountDownLatch)startupConnectionDataField.get((Object)null);
			fmlClientHandler = mGetClientInstance.invoke(null, new Object[0]);
		}
	}
	
	public static boolean isForgeDetected() {
		return forgeDetected;
	}
	
	public static boolean openModsList(Minecraft minecraft, GuiScreen parentScreen) {
		if (guiModListCtor != null) {
			try {
				GuiScreen modInfoScreen = guiModListCtor.newInstance(new Object[] {parentScreen});
				minecraft.displayGuiScreen(modInfoScreen);
				return true;
			} catch (Exception e) {}
		}
		return false;
	}
	
	public static List<String> getBrandings(){
		return fmlBrandings;
	}
	
	public static boolean connectToServer(GuiScreen parentScreen, Minecraft mc, ServerData serverData) {
		if (fmlClientHandler != null) {
			try {
				mSetupServerList.invoke(fmlClientHandler, new Object[0]);
				ServerPinger serverPinger = new ServerPinger();
				startupConnectionData.await(30L, TimeUnit.SECONDS);
				mConnectToServer.invoke(fmlClientHandler, new Object[] {parentScreen, serverData});
				return true;
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
		return false;
	}
}
