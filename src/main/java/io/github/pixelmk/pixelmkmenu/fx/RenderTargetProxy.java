package io.github.pixelmk.pixelmkmenu.fx;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;

import io.github.pixelmk.pixelmkmenu.helpers.FBO;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class RenderTargetProxy extends RenderTarget {

	private RenderTarget oldRenderTarget;
	private FBO fbo;

	public RenderTargetProxy() {
		super(false);
	}

	public void attach(RenderTarget mainRenderTarget, FBO fbo) {
		this.fbo = fbo;
		this.oldRenderTarget = mainRenderTarget;
		try {
			ObfuscationReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.getInstance(), this, "mainRenderTarget");
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	public void release(Minecraft mc) {
		try {
			ObfuscationReflectionHelper.setPrivateValue(Minecraft.class, mc, this.oldRenderTarget, "mainRenderTarget");
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createBuffers(int pWidth, int pHeight, boolean pClearError) {}

	@Override
	public void bindWrite(boolean p_147610_1_) {
		if (!RenderSystem.isOnRenderThread()) {
			RenderSystem.recordRenderCall(() -> {
				this.fbo.bind();
			});
		 } else {
			this.fbo.bind();
		 }

	}

	@Override
	public void unbindWrite() {
		if (!RenderSystem.isOnRenderThread()) {
			RenderSystem.recordRenderCall(() -> {
				this.fbo.end();
			});
		 } else {
			this.fbo.end();
		 }
	}

}
