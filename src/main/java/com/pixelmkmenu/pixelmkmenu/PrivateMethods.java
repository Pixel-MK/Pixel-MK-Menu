package com.pixelmkmenu.pixelmkmenu;

import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class PrivateMethods<P,R> {
	public final Class<?> parentClass;
	
	public final String methodName;
	
	public final Method method;
	
	public static class Void{}
	
	private PrivateMethods(Class<?> owner, ObfuscationMapping mapping, Class<?>... parameterTypes) {
		this.parentClass = owner;
		this.methodName = mapping.getName();
		Method method = null;
		try {
			method = this.parentClass.getDeclaredMethod(this.methodName, parameterTypes);
			method.setAccessible(true);
		} catch (SecurityException ex) {
			ex.printStackTrace();
		} catch (NoSuchMethodException ex) {
			ex.printStackTrace();
		}
		this.method = method;
	}
	
	public R invoke(P instance, Object... args) {
		try {
			return (R)this.method.invoke(instance, args);
		} catch (Exception e) {
			return null;
		}
	}
	
	public R invokeStatic(Object... args) {
		try {
			return (R)this.method.invoke((Object)null, args);
		}catch (Exception e) {
			return null;
		}
	}
	
	public void invokeVoid(P instance, Object... args) {
		try {
			this.method.invoke(instance, args);
		}catch (Exception e) {}
	}
	
	public void invokeStaticVoid(Object... args) {
		try {
			this.method.invoke((Object)null, args);
		} catch (Exception e) {}
	}
	
	public static final PrivateMethods<GuiContainer, Slot> containerGetSlotAtPosition = new PrivateMethods(GuiContainer.class, ObfuscationMapping.getSlotAtPosition, new Class[] { int.class, int.class });
	  
	  public static final PrivateMethods<GuiContainer, Void> containerHandleMouseClick = new PrivateMethods(GuiContainer.class, ObfuscationMapping.handleMouseClick, new Class[] { Slot.class, int.class, int.class, int.class });
	  
	  public static final PrivateMethods<GuiContainerCreative, Void> selectTab = new PrivateMethods(GuiContainerCreative.class, ObfuscationMapping.selectTab, new Class[] { CreativeTabs.class });
	  
	  public static final PrivateMethods<GuiMainMenu, Void> mainMenuRenderSkyBox = new PrivateMethods(GuiMainMenu.class, ObfuscationMapping.renderSkyBox, new Class[] { int.class, int.class, float.class });
	  
	  public static final PrivateMethods<Minecraft, Void> resizeMinecraft = new PrivateMethods(Minecraft.class, ObfuscationMapping.resize, new Class[] { int.class, int.class });
	  
	  public static final PrivateMethods<GuiScreen, Void> guiScreenMouseClicked = new PrivateMethods(GuiScreen.class, ObfuscationMapping.guiScreenMouseClicked, new Class[] { int.class, int.class, int.class });
	  
	  public static final PrivateMethods<GuiScreen, Void> guiScreenMouseMovedOrUp = new PrivateMethods(GuiScreen.class, ObfuscationMapping.guiScreenMouseMovedOrUp, new Class[] { int.class, int.class, int.class });
	  
	  public static final PrivateMethods<GuiScreen, Void> guiScreenKeyTyped = new PrivateMethods(GuiScreen.class, ObfuscationMapping.guiScreenKeyTyped, new Class[] { char.class, int.class });
	  
	  public static final PrivateMethods<Container, Void> scrollTo = new PrivateMethods(PrivateClasses.ContainerCreative.Class, ObfuscationMapping.scrollTo, new Class[] { float.class });

}
