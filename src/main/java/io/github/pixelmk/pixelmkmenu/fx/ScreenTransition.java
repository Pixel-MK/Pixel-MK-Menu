package io.github.pixelmk.pixelmkmenu.fx;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import io.github.pixelmk.pixelmkmenu.helpers.FBO;

public abstract class ScreenTransition {

	public abstract void render(FBO active, FBO last, int width, int height, float transitionPct);

	public abstract String getName();

	public abstract boolean isHighMotion();

	public abstract int getTransitionTime();

	public abstract TransitionType getTransitionType();

	public enum TransitionType {
		Linear, Sine, Cosine, Smooth;

		public float interpolate(float pct) {
			if (this == Sine)
				return (float) Math.min(1.0D, Math.sin(Math.PI * Math.min(0.5f, pct)));
			if (this == Cosine)
				return (float) Math.min(1.0D, 1.0D - Math.cos(Math.PI * Math.min(0.5f, pct)));
			if (this == Smooth)
				return (float) Math.min(1.0D, (-Math.cos((2 * Math.PI) * Math.min(0.5f, pct)) + 1.0D) * 0.5D);
			return Math.min(1.0f, pct);
		}
	}

	public static final void drawFBO(FBO fbo, int x, int y, int x2, int y2, int z, float alpha) {
		drawFBO(fbo, x, y, x2, y2, z, alpha, true);
	}

	public static final void drawFBO(FBO fbo, int x, int y, int x2, int y2, int z, float alpha, boolean blend) {
		RenderSystem.assertOnGameThreadOrInit();
		if (!RenderSystem.isInInitPhase()) {
			RenderSystem.recordRenderCall(() -> {
				drawFBO(fbo, x, y, x2, y2, z, alpha, blend, 0.0f, 0.0f, 1.0f, 1.0f);
			});
		} else {
			drawFBO(fbo, x, y, x2, y2, z, alpha, blend, 0.0f, 0.0f, 1.0f, 1.0f);
		}
	}

	public static void drawFBO(FBO fbo, int x, int y, int x2, int y2, int z, float alpha, boolean blend, float u,
			float v, float u2, float v2) {
		RenderSystem.assertOnRenderThread();
		if (blend) {
			RenderSystem.enableBlend();
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.0f);
			GL11.glAlphaFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		} else {
			RenderSystem.disableBlend();
			GL11.glDisable(GL11.GL_ALPHA_TEST);
		}
		//GL11.glShadeModel(GL11.GL_FLAT);
		fbo.draw(x, y, x2, y2, z, alpha, u, v, u2, v2);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		// GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		RenderSystem.disableBlend();
	}

}
