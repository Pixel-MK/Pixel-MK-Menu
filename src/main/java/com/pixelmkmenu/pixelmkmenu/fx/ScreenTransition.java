package com.pixelmkmenu.pixelmkmenu.fx;

import org.lwjgl.opengl.GL11;

import com.pixelmkmenu.pixelmkmenu.gl.FBO;

public abstract class ScreenTransition {
	
	public abstract void render(FBO active, FBO last, int width, int height, float transitionPct);
	
	public abstract String getName();
	
	public abstract boolean isHighMotion();
	
	public abstract int getTransitionTime();
	
	public abstract TransitionType getTransitionType();
	
	public enum TransitionType{
		Linear, Sine, Cosine, Smooth;
		
		public float interpolate(float pct) {
			if(this == Sine) return (float)Math.min(1.0D, Math.sin(Math.PI * Math.min(0.5f, pct)));
			if(this == Cosine) return (float)Math.min(1.0D, 1.0D - Math.cos(Math.PI * Math.min(0.5f, pct)));
			if(this == Smooth) return (float)Math.min(1.0D, (-Math.cos((2*Math.PI) * Math.min(0.5f, pct)) +1.0D) *0.5D);
			return Math.min(1.0f, pct);
		}
	}
	
	public static final void drawFBO(FBO fbo, int x, int y, int x2, int y2, int z, float alpha) {
		drawFBO(fbo, x, y, x2, y2, z, alpha, true);
	}
	
	public static final void drawFBO(FBO fbo, int x, int y, int x2, int y2, int z, float alpha, boolean blend) {
		drawFBO(fbo, x, y, x2, y2, z, alpha, blend, 0.0d, 0.0d, 1.0d, 1.0d);
	}
	
	public static void drawFBO(FBO fbo, int x, int y, int x2, int y2, int z, float alpha, boolean blend, double u, double v, double u2, double v2) {
		if(blend) {
			GL11.glEnable(3042);
			GL11.glEnable(3008);
			GL11.glAlphaFunc(516, 0.0f);
			GL11.glAlphaFunc(770, 771);
		} else {
			GL11.glDisable(3042);
			GL11.glDisable(3008);
		}
		GL11.glShadeModel(7424);
		fbo.draw(x, y, x2, y2, z, alpha, u, v, u2, v2);
		GL11.glEnable(3008);
		GL11.glAlphaFunc(516, 0.1f);
		GL11.glDisable(3042);
	}

}
