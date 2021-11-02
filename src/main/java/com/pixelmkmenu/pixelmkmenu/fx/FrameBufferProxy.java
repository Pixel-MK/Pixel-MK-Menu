package com.pixelmkmenu.pixelmkmenu.fx;

import com.pixelmkmenu.pixelmkmenu.gl.FBO;
import com.pixelmkmenu.pixelmkmenu.util.PrivateFields;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;

public class FrameBufferProxy extends Framebuffer{
	
	private Framebuffer oldFramebuffer;
	private FBO fbo;
	
	public FrameBufferProxy() {
		super(0, 0, false);
	}
	
	public void attach(Minecraft mc, FBO fbo) {
		this.fbo = fbo;
		this.oldFramebuffer = mc.getFramebuffer();
		PrivateFields.mcFramebuffer.set(mc, this);
	}
	
	public void release(Minecraft mc) {
		PrivateFields.mcFramebuffer.set(mc, this.oldFramebuffer);
	}
	
	@Override
	public void createBindFramebuffer(int width, int height) {}
	
	@Override
	public void bindFramebuffer(boolean p_147610_1_) {
		this.fbo.bind();
	}
	
	@Override
	public void unbindFramebuffer() {
		this.fbo.end();
	}

}
