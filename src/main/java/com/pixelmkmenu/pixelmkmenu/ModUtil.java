package com.pixelmkmenu.pixelmkmenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

public abstract class ModUtil {
	private static boolean fmlDetected = true;
	private static boolean seargeNames = false;
	
	static {
		fmlDetected = fmlIsPresent();
		try {
			Minecraft.class.getDeclaredField("running");
		}catch (SecurityException ex) {
		}catch (NoSuchFieldException ex) {
			seargeNames = true;
		}
	}
	
	public static boolean fmlIsPresent() {
		for(IClassTransformer transformer : Launch.classLoader.getTransformers()) {
			if (transformer.getClass().getName().contains("fml")) return true;
		}
		return false;
	}
	
	public static String getObfuscatedFieldName(String fieldName, String obfuscatedFieldName, String seargeFieldName) {
		boolean deobfuscated = Tessellator.class.getSimpleName().equals("Tessellator");
		return deobfuscated ? (seargeNames ? obfuscatedFieldName : fieldName) : (fmlDetected ? seargeFieldName : obfuscatedFieldName);
	}
}
