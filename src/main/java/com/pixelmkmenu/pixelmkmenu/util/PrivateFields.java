package com.pixelmkmenu.pixelmkmenu.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;

import com.pixelmkmenu.pixelmkmenu.ObfuscationMapping;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISoundEventAccessor;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Locale;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import paulscode.sound.SoundSystem;

public class PrivateFields<P, T> {
	public final Class<P> parentClass;
	
	private final String fieldName;
	
	private boolean errorReported;
	
	private PrivateFields(Class<P> owner, ObfuscationMapping mapping) {
		this.parentClass = owner;
		this.fieldName = mapping.getName();
	}
	
	public T get(P instance) {
		try {
			return ReflectionHelper.getPrivateValue(this.parentClass, instance, this.fieldName); //Reflection.getPrivateValue(this.parentClass, instance, this.fieldName);
		}catch (Exception ex) {
			if(!this.errorReported) {
				this.errorReported = true;
				ex.printStackTrace();
			}
			return null;
		}
	}
	
	public T set(P instance, T value) {
		try {
			ReflectionHelper.setPrivateValue(this.parentClass, instance, value, this.fieldName);
		} catch (Exception ex) {
			if (!this.errorReported) {
				this.errorReported = true;
				ex.printStackTrace();
			}
		}
		return value;
	}
	
	public static final class StaticFields<P, T> extends PrivateFields<P, T>{
		public StaticFields(Class<P> owner, ObfuscationMapping mapping) {
			super(owner, mapping);
		}
		public T get() {
			return get(null);
		}
		
		public void set(T value) {
			set(null, value);
		}
		
		public static final StaticFields<I18n, Locale> locale = new StaticFields((Class)I18n.class, ObfuscationMapping.currentLocale);
		public static final StaticFields<Gui, ResourceLocation> optionsBackground = new StaticFields((Class)Gui.class, ObfuscationMapping.optionsBackground);
	}
	
	public static final PrivateFields<GuiScreen, GuiButton> guiScreenSelectedButton = new PrivateFields((Class)GuiScreen.class, ObfuscationMapping.guiScreenSelectedButton);
	  
	  public static final PrivateFields<RenderGlobal, BufferBuilder[]> worldRenderers = new PrivateFields((Class)RenderGlobal.class, ObfuscationMapping.worldRenderers);
	  
	  public static final PrivateFields<WorldInfo, WorldType> worldType = new PrivateFields((Class)WorldInfo.class, ObfuscationMapping.worldType);
	  
	  public static final PrivateFields<GuiTextField, Integer> textFieldXPos = new PrivateFields((Class)GuiTextField.class, ObfuscationMapping.textFieldXPos);
	  
	  public static final PrivateFields<GuiTextField, Integer> textFieldYPos = new PrivateFields((Class)GuiTextField.class, ObfuscationMapping.textFieldYPos);
	  
	  public static final PrivateFields<GuiTextField, Integer> textFieldWidth = new PrivateFields((Class)GuiTextField.class, ObfuscationMapping.textFieldWidth);
	  
	  public static final PrivateFields<GuiTextField, Integer> textFieldHeight = new PrivateFields((Class)GuiTextField.class, ObfuscationMapping.textFieldHeight);
	  
	  public static final PrivateFields<FontRenderer, Float> fontRendererPosY = new PrivateFields((Class)FontRenderer.class, ObfuscationMapping.fontRendererPosY);
	  
	  public static final PrivateFields<GuiWorldSelection, Boolean> worldSelected = new PrivateFields((Class)GuiWorldSelection.class, ObfuscationMapping.worldSelected);
	  
	  public static final PrivateFields<GuiWorldSelection, GuiScreen> guiSelectWorldParent = new PrivateFields((Class)GuiWorldSelection.class, ObfuscationMapping.guiSelectWorldParent);
	  
	  public static final PrivateFields<RenderPlayer, ModelBiped> modelBipedMain = new PrivateFields((Class)RenderPlayer.class, ObfuscationMapping.modelBipedMain);
	  
	  public static final PrivateFields<RenderPlayer, ModelBiped> modelArmorChestplate = new PrivateFields((Class)RenderPlayer.class, ObfuscationMapping.modelArmorChestplate);
	  
	  public static final PrivateFields<RenderPlayer, ModelBiped> modelArmor = new PrivateFields((Class)RenderPlayer.class, ObfuscationMapping.modelArmor);
	  
	  public static final PrivateFields<TileEntityMobSpawner, MobSpawnerBaseLogic> spawnerLogic = new PrivateFields((Class)TileEntityMobSpawner.class, ObfuscationMapping.spawnerLogic);
	  
	  public static final PrivateFields<SoundManager, SoundSystem> soundSystem = new PrivateFields((Class)SoundManager.class, ObfuscationMapping.soundSystemThread);
	  
	  public static final PrivateFields<GuiSlot, Long> lastClicked = new PrivateFields((Class)GuiSlot.class, ObfuscationMapping.lastClicked);
	  
	  public static final PrivateFields<Minecraft, Timer> minecraftTimer = new PrivateFields((Class)Minecraft.class, ObfuscationMapping.minecraftTimer);
	  
	  public static final PrivateFields<EntityRenderer, Double> renderZoom = new PrivateFields((Class)EntityRenderer.class, ObfuscationMapping.renderZoom);
	  
	  public static final PrivateFields<EntityRenderer, Double> renderOfsetX = new PrivateFields((Class)EntityRenderer.class, ObfuscationMapping.renderOfsetX);
	  
	  public static final PrivateFields<EntityRenderer, Double> renderOfsetY = new PrivateFields((Class)EntityRenderer.class, ObfuscationMapping.renderOfsetY);
	  
	  public static final PrivateFields<AbstractClientPlayer, ThreadDownloadImageData> skinTexture = new PrivateFields((Class)AbstractClientPlayer.class, ObfuscationMapping.skinTexture);
	  
	  public static final PrivateFields<AbstractClientPlayer, ThreadDownloadImageData> cloakTexture = new PrivateFields((Class)AbstractClientPlayer.class, ObfuscationMapping.cloakTexture);
	  
	  public static final PrivateFields<AbstractClientPlayer, ResourceLocation> skinResource = new PrivateFields((Class)AbstractClientPlayer.class, ObfuscationMapping.skinResource);
	  
	  public static final PrivateFields<AbstractClientPlayer, ResourceLocation> cloakResource = new PrivateFields((Class)AbstractClientPlayer.class, ObfuscationMapping.cloakResource);
	  
	  public static final PrivateFields<GuiMainMenu, ResourceLocation> panoramaTexture = new PrivateFields((Class)GuiMainMenu.class, ObfuscationMapping.panoramaTexture);
	  
	  public static final PrivateFields<World, Float> rainingStrength = new PrivateFields((Class)World.class, ObfuscationMapping.rainingStrength);
	  
	  public static final PrivateFields<World, Float> thunderingStrength = new PrivateFields((Class)World.class, ObfuscationMapping.thunderingStrength);
	  
	  public static final PrivateFields<ThreadDownloadImageData, BufferedImage> downloadedImage = new PrivateFields((Class)ThreadDownloadImageData.class, ObfuscationMapping.downloadedImage);
	  
	  public static final PrivateFields<GuiMultiplayer, ServerList> internetServerList = new PrivateFields((Class)GuiMultiplayer.class, ObfuscationMapping.internetServerList);
	  
	  public static final PrivateFields<GuiMultiplayer, ServerSelectionList> serverSelectionList = new PrivateFields((Class)GuiMultiplayer.class, ObfuscationMapping.serverSelectionList);
	  
	  public static final PrivateFields<GuiScreenResourcePacks, GuiScreen> guiResourcePacksParentScreen = new PrivateFields((Class)GuiScreenResourcePacks.class, ObfuscationMapping.guiResourcePacksParentScreen);
	  
	  public static final PrivateFields<AbstractResourcePack, File> abstractResourcePackFile = new PrivateFields((Class)AbstractResourcePack.class, ObfuscationMapping.abstractResourcePackFile);
	  
	  public static final PrivateFields<Minecraft, Framebuffer> mcFramebuffer = new PrivateFields((Class)Minecraft.class, ObfuscationMapping.mcFramebuffer);
	  
	  public static final PrivateFields<World, WorldInfo> worldInfo = new PrivateFields((Class)World.class, ObfuscationMapping.worldInfo);
	  
	  public static final PrivateFields<ThreadDownloadImageData, String> imageUrl = new PrivateFields((Class)ThreadDownloadImageData.class, ObfuscationMapping.imageUrl);
	  
	  public static final PrivateFields<ThreadDownloadImageData, Thread> imageThread = new PrivateFields((Class)ThreadDownloadImageData.class, ObfuscationMapping.imageThread);
	  
	  public static final PrivateFields<ThreadDownloadImageData, IImageBuffer> imageBuffer = new PrivateFields((Class)ThreadDownloadImageData.class, ObfuscationMapping.imageBuffer);
	  
	  public static final PrivateFields<ThreadDownloadImageData, File> imageFile = new PrivateFields((Class)ThreadDownloadImageData.class, ObfuscationMapping.imageFile);
	  
	  public static final PrivateFields<SoundEventAccessor, List<ISoundEventAccessor>> eventSounds = new PrivateFields((Class)SoundEventAccessor.class, ObfuscationMapping.eventSounds);
	  
	  public static final PrivateFields<TextureManager, Map<ResourceLocation, ? extends ITextureObject>> resourceToTextureMap = new PrivateFields((Class)TextureManager.class, ObfuscationMapping.resourceToTextureMap);
}
