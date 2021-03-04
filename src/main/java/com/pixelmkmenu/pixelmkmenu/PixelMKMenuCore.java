package com.pixelmkmenu.pixelmkmenu;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import com.pixelmkmenu.pixelmkmenu.events.PixelMKMainMenuEvent;
import com.pixelmkmenu.pixelmkmenu.fx.FrameBufferProxy;
import com.pixelmkmenu.pixelmkmenu.fx.ScreenTransition;
import com.pixelmkmenu.pixelmkmenu.fx.Transitions.ScreenTransitionFade;
import com.pixelmkmenu.pixelmkmenu.gl.FBO;
import com.pixelmkmenu.pixelmkmenu.interfaces.IPanoramaRenderer;
import com.pixelmkmenu.pixelmkmenu.login.CustomServerDataManager;
import com.pixelmkmenu.pixelmkmenu.util.PrivateFields;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.client.resources.IReloadableResourceManager;


public class PixelMKMenuCore extends GuiScreen implements ISelectiveResourceReloadListener{
	private static final Logger LOGGER = LogManager.getLogger("pixelmkmenu");
	
	public static final ResourceLocation MUSIC_STANDARD = new ResourceLocation("pixelmkmenu", "music.pixelmkmenu");
	
	public static PixelMKMenuCore mod;
	private boolean fboEnabled = false;
	
	private ScreenTransition defaultTransition = (ScreenTransition)new ScreenTransitionFade();
	private List<ScreenTransition> availableTransitions = new ArrayList<ScreenTransition>();
	private List<ScreenTransition> activeTransitions = new ArrayList<ScreenTransition>();
	private ScreenTransition transition = null;
	
	public static boolean ingame = false;
	protected boolean swapped;
	protected boolean handleRecursion;
	protected volatile boolean drawingChildScreen;
	private boolean previousWasScreen;
	private static boolean hasMenuMusic = true;
	private static boolean enableMenuMusic = true;
	
	protected GuiScreen swappedScreen;
	private GuiScreen transitionScreen;
		
	private FBO[] transitionFBOs = new FBO[2];
	private FrameBufferProxy proxy = new FrameBufferProxy();
	
	
	private long transitionBeginTime = 0L;
	
	private float transitionPct = 0.0f;
	private float transitionRate = 1.0f;
	private static float lastPartialTicks;
	
	private int currentFBO = 0;
	private int transitionFrames = 2;
	
	private static Random rand = new Random();
	
	private static CustomServerDataManager serverDataManager = new CustomServerDataManager();
	
	private static IPanoramaRenderer panoramaRenderer;
	
	public static PixelMKMenuConfig configOpts = new PixelMKMenuConfig();
	
	public PixelMKMenuCore() {
		mod = this;
	}
	
	private static CustomServerDataManager getServerDataManager() {
		return serverDataManager;
	}
	
	public static void WriteLogInfo(String message) {
		LOGGER.info(message);
	}
	
	public static void WriteLogWarn(String message) {
		LOGGER.warn(message);
	}
	
	
	public void onInit() {
		this.mc = Minecraft.getMinecraft();
		this.fboEnabled = FBO.detectFBOCapabilities();
		registerTransition((ScreenTransition)new ScreenTransitionFade());
		MinecraftForge.EVENT_BUS.register(new PixelMKMainMenuEvent());
		updateRegisteredTransitions();
		LOGGER.info("Initialisation Complete");
	}
	
	public void updateRegisteredTransitions() {
		this.activeTransitions.clear();
		for (ScreenTransition transition : this.availableTransitions) {
			if (configOpts.isTransitionEnabled(transition)) addTransition(transition);
		}
	}
	
	public void onPostInit() {
		if(this.fboEnabled) {
			LOGGER.info("FBO capability is supported, enabling transitions.");
			this.transitionFBOs[0] = new FBO();
			this.transitionFBOs[1] = new FBO();
		} else {
			LOGGER.info("FBO capability is not supported, transitions will not be enabled");
		}
		IResourceManager resourceManager = this.mc.getResourceManager();
		((IReloadableResourceManager)resourceManager).registerReloadListener(this);
		enableMenuMusic = !getConfig().getBoolProperty(PixelMKMenuConfig.MUTE);
	}
	
	@SubscribeEvent
	public void OnResourceManagerReload(IResourceManager resourceManager) {
		if(hasMenuMusic) {
			SoundEventAccessor accessor = this.mc.getSoundHandler().getAccessor(mc.getAmbientMusicType().getMusicLocation().getSoundName());
			//TODO soundPool field fix
			if (accessor != null) ((List)PrivateFields.eventSounds.get(accessor)).clear();
		}
	}
	
	public static void setMusicState(boolean enabled) {
		enableMenuMusic = enabled;
	}
	
	public static boolean isMusicEnabled() {
		return enableMenuMusic;
	}
	
	public static boolean hasMenuMusic() {
		return hasMenuMusic;
	}
	
	@SubscribeEvent
	public void onTick(TickEvent event, Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
		ingame = inGame;
		if (clock) {
			if(minecraft.currentScreen != null && minecraft.currentScreen != panoramaRenderer && panoramaRenderer != null) {
				panoramaRenderer.updatePanorama();
				lastPartialTicks = partialTicks;
			}
		}
	}
	
	@SubscribeEvent
	public void onRenderWorld(RenderWorldLastEvent event) {}
	
	@SubscribeEvent
	public void onRenderGui(GuiScreenEvent event, GuiScreen currentScreen) {
		if(event.getGui() == null && this.mc.gameSettings.keyBindPlayerList.isKeyDown()) return;
		if(!getConfig().getBoolProperty(PixelMKMenuConfig.TRANSITIONS)) return;
		if(!this.fboEnabled) return;
		if(this.mc.world != null) {
			this.transitionFBOs[0].dispose();
			this.transitionFBOs[1].dispose();
			return;
		}
		if(checkBeginTransition(currentScreen)) {
			if(this.transitionFrames > 1) {
				this.currentFBO = 1 - this.currentFBO;
				this.previousWasScreen = (this.transitionScreen != null);
				this.transitionPct = 0.0f;
				this.transitionBeginTime = Minecraft.getSystemTime();
				this.transitionRate = getConfig().getClampedFloatProperty(PixelMKMenuConfig.TRANSITIONRATE, 0.1F, 10.0F);
			}
			this.transitionScreen = currentScreen;
			this.transitionFrames = 0;
			this.transition = createTransition();
		} else {
			this.transitionFrames++;
			if(this.transition != null) {
				long deltaTime = Minecraft.getSystemTime() - this.transitionBeginTime;
				float pct = (float)deltaTime / this.transition.getTransitionTime() * 2.0f * 1.0f / this.transitionRate;
				this.transitionPct = this.transition.getTransitionType().interpolate(pct);
				if(this.transitionPct >= 1.0f) this.transition = null;
			}
		}
		if(!this.swapped && event.getGui() != this) {
			this.swapped = true;
			this.swappedScreen = event.getGui();
		}
		this.mc.currentScreen = this;
	}
	
	public boolean checkBeginTransition(GuiScreen currentScreen) {
		if(this.mc.world != null) return false;
		if(currentScreen == this.transitionScreen && this.handleRecursion) {
			this.handleRecursion = false;
			this.mc.displayGuiScreen(null);
		}
		if(currentScreen != this.transitionScreen) {
			if(currentScreen != null) {
				if(currentScreen.getClass().getName().contains("DialogBox")) return false;
			}
			return true;
		}
		return true;
	}

	public void registerTransition(ScreenTransition transition) {
		for (ScreenTransition other : this.availableTransitions) {
			if(other.getClass().equals(transition.getClass())) return;
		}
		this.availableTransitions.add(transition);
	}
	
	public List<ScreenTransition> getAvailableTransitions(){
		return this.availableTransitions;
	}
	
	private void addTransition(ScreenTransition transition) {
		this.activeTransitions.add(transition);
	}
	
	private ScreenTransition createTransition() {
		if(configOpts.getBoolProperty(PixelMKMenuConfig.FUNKY) && this.activeTransitions.size() > 0) {
			int random = rand.nextInt(this.activeTransitions.size());
			return this.activeTransitions.get(random);
		}
		return this.defaultTransition;
	}
	
	@SubscribeEvent
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if(this.drawingChildScreen) {
			if(this.swappedScreen != null && this.swappedScreen != this)
				this.swappedScreen.drawScreen(mouseX, mouseY, partialTicks);
			this.handleRecursion = true;
			return;
		}
		if (this.swapped && this.swappedScreen != this) {
			this.swapped = false;
			if(this.mc.currentScreen == this) this.mc.currentScreen = null;
		}
		ScaledResolution resolution = new ScaledResolution(this.mc);
		this.height = resolution.getScaledHeight();
		this.width = resolution.getScaledWidth();
		FBO activeFBO = this.transitionFBOs[this.currentFBO];
		if(this.mc.currentScreen != null && activeFBO != null) {
			GL11.glPushAttrib(1048575);
			this.mc.getFramebuffer().unbindFramebuffer();
			this.proxy.attach(this.mc, activeFBO);
			activeFBO.begin(this.mc.displayWidth, this.mc.displayHeight);
			this.mc.entityRenderer.setupOverlayRendering();
			clearScreen(this.mc.displayWidth, this.mc.displayHeight, 999.0f);
			this.drawingChildScreen = true;
			this.mc.currentScreen.drawScreen(mouseX, mouseY, partialTicks);
			this.drawingChildScreen = false;
			GL11.glAlphaFunc(516, 0.1f);
			activeFBO.end();
			this.proxy.release(this.mc);
			this.mc.getFramebuffer().bindFramebuffer(true);
			if((this.mc.getFramebuffer()).useDepth) EXTFramebufferObject.glBindRenderbufferEXT(36161, (this.mc.getFramebuffer()).depthBuffer);
			GL11.glPopAttrib();
		}
		this.mc.entityRenderer.setupOverlayRendering();
		if(this.transitionPct < 1.0f && this.transition != null) {
			FBO active = (this.mc.currentScreen != null) ? activeFBO : null;
			FBO last = this.previousWasScreen ? this.transitionFBOs[1-this.currentFBO] : null;
			this.transition.render(active, last, this.width, this.height, this.transitionPct);
		} else if (this.mc.currentScreen != null) {
			ScreenTransition.drawFBO(activeFBO, 0, 0, this.width, this.height, 0, 1.0f, false);
		}
	}
	
	protected void clearScreen(int displayWidth, int displayHeight, float zDepth) {
		GL11.glClearDepth(999.0d);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glClear(16640);
		GL11.glDisable(3553);
		GL11.glEnable(3008);
		GL11.glAlphaFunc(518, 0.0f);
		GL11.glBlendFunc(1, 0);
		GL11.glShadeModel(7424);
		GL11.glEnable(2929);
		GL11.glDepthMask(true);
		GL11.glDepthFunc(519);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bb = tessellator.getBuffer();
		bb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
		bb.color(0.0f, 0.0f, 0.0f, 1.0f);
		bb.pos(displayWidth, 0.0d, -zDepth).endVertex();
		bb.pos(0.0d, 0.0d, -zDepth).endVertex();
		bb.pos(0.0d, displayHeight, -zDepth).endVertex();
		bb.pos(displayWidth, displayHeight, -zDepth).endVertex();
		tessellator.draw();
		GL11.glDepthFunc(515);
		GL11.glEnable(3008);
		GL11.glEnable(3553);
		GL11.glDepthMask(true);
	}
	
	public void onSetupCameraTransform() {}
	
	public static PixelMKMenuConfig getConfig() {
		return configOpts;
	}
	
	public static void setPanoramaRenderer(IPanoramaRenderer panoramaRenderer) {
		PixelMKMenuCore.panoramaRenderer = panoramaRenderer;
	}
	
	public static IPanoramaRenderer getPanoramaRenderer() {
		return panoramaRenderer;
	}
	
	public static void enableClipping(GuiSlot slot) {}
	
	public static void drawSlotBackground(GuiSlot slot, int x1, int y1, int x2, int y2) {
		Gui.drawRect(x1, y1, x2, y2, -2147483648);
		glEnableClipping(y2, y1);
	}
	
	public static void drawSlotBorders(GuiSlot slot, int i, int j, int k, int l) {
		glDisableClipping();
	}
	
	@SubscribeEvent
	public static void drawBackground(GuiScreen screen, int updateCounter) {
		Minecraft mc = Minecraft.getMinecraft();
		boolean inGame = (mc.world != null);;
		if (screen == null) return;
		if(inGame) {
			mod.drawGradientRect(0, 0, screen.width, screen.height, -1072689136, -804253680);
		}else if(panoramaRenderer != null){
			panoramaRenderer.renderPanorama(0, 0, lastPartialTicks);
			mod.drawGradientRect(0, 0, screen.width, screen.height, 1611665424, -1609560048);
		} else {
			GL11.glDisable(2896);
			GL11.glDisable(2912);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bb = tessellator.getBuffer();
			mc.getTextureManager().bindTexture(GuiScreen.OPTIONS_BACKGROUND);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			float var3 = 32.0f;
			bb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
			bb.putColorRGB_F4(4210752 >> 16 & 0xFF, 4210752 >> 8 & 255, 4210752 &255);
			bb.pos(0.0d, screen.height, 0.0d).tex(0.0d, (screen.height / var3 + 0.0f)).endVertex();
			bb.pos(screen.width, screen.height, 0.0d).tex((screen.width / var3), (screen.height / var3 + 0.0f)).endVertex();
			bb.pos(screen.width, 0.0d, 0.0d).tex((screen.width / 3), 0.0d).endVertex();
			bb.pos(0.0d, 0.0d, 0.0d).tex(0.0d, 0.0d).endVertex();
			tessellator.draw();
		}
	}
	
	private static DoubleBuffer doubleBuffer = BufferUtils.createByteBuffer(64).asDoubleBuffer();
	
	static final void glEnableClipping(int yTop, int yBottom) {
		if(yTop != -1) {
			doubleBuffer.clear();
			doubleBuffer.put(0.0d).put(1.0d).put(0.0d).put(-yTop).flip();
			GL11.glClipPlane(12292, doubleBuffer);
			GL11.glEnable(12292);
		}
		if(yBottom != -1) {
			doubleBuffer.clear();
			doubleBuffer.put(0.0d).put(-1.0d).put(0.0d).put(yBottom).flip();
			GL11.glClipPlane(12293, doubleBuffer);
			GL11.glEnable(12293);
		}
	}
	
	static final void glDisableClipping() {
		GL11.glDisable(12293);
		GL11.glDisable(12292);
		GL11.glDisable(12291);
		GL11.glDisable(12290);
	}
	
	@SubscribeEvent
	public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {}
}
