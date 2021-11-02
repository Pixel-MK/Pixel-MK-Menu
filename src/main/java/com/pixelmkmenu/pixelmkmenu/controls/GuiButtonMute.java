package com.pixelmkmenu.pixelmkmenu.controls;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class GuiButtonMute extends GuiButton {
	public boolean muted = false;

	//TODO Texture not showing???
	private static final ResourceLocation speaker = new ResourceLocation("pixelmkmenu", "textures/speaker.png");

	public GuiButtonMute(int ButtonId, int x, int y) {
		super(ButtonId, x, y, "");
		this.width = 20;
		this.height = 32;
	}

	@Override
	public void drawButton(Minecraft mc, int par2, int par3, float partialTicks) {
		mc.getTextureManager().bindTexture(speaker);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		float f = 0.3125f;
		drawTexturedModalRect(this.x, this.y, this.x + this.width, this.y + this.height, this.muted ? f : 0.0F, 0.0F, f + (this.muted ? f : 0.0F), 1.0F);
		//Fixes random overlay glitch
		GL11.glDisable(GL11.GL_BLEND);
	}


	public void drawTexturedModalRect(int x, int y, int x2, int y2, float u, float v, float u2, float v2) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bb = tessellator.getBuffer();
		bb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bb.pos(x, y2, 0.0D).tex(u, v2).endVertex();
		bb.pos(x2, y2, 0.0D).tex(u2, v2).endVertex();
		bb.pos(x2, y, 0.0D).tex(u2, v).endVertex();
		bb.pos(x, y, 0.0D).tex(u, v).endVertex();
		tessellator.draw();
	}
}
