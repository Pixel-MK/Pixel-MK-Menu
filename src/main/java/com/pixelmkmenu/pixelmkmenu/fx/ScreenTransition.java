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
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.0f);
			GL11.glAlphaFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		} else {
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
		}
		GL11.glShadeModel(GL11.GL_FLAT);
		fbo.draw(x, y, x2, y2, z, alpha, u, v, u2, v2);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		GL11.glDisable(GL11.GL_BLEND);
	}

}
