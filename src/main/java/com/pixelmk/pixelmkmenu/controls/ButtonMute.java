package com.pixelmk.pixelmkmenu.controls;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

public class ButtonMute extends Button {

    private boolean muted;
    private static final ResourceLocation speaker = new ResourceLocation("pixelmkmenu", "textures/gui/speaker.png");

    public ButtonMute(int xPos, int yPos, ActionInstance handler) {
        super(xPos, yPos, 20, 32, new TranslatableComponent("Mute"), handler, Button.NO_TOOLTIP);
        handler.source = this;
    }

    public void setMute(boolean muted) {
        if (isVolumeAbove0())
            this.muted = muted;
    }

    public boolean getMute() {
        return muted;
    }

    private static boolean isVolumeAbove0() {
        return Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.MUSIC) > 0.0f;
    }

    @Override
    public void renderButton(PoseStack pose, int mouseX, int mouseY, float partialTicks) {
        if (!isVolumeAbove0())
            muted = true;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, speaker);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 0.3125f;
        this.drawTexturedModalRect(pose, this.x, this.y, this.x + this.width, this.y + this.height, this.muted ? f : 0.0F, 0.0F, f + (this.muted ? f : 0.0F), 1.0F);
        // Fixes random overlay glitch
        RenderSystem.disableBlend();
    }

    @Override
    public void onPress() {
        this.setMute(!muted);
    }

    private void drawTexturedModalRect(PoseStack pose, int x, int y, int x2, int y2, float u, float v, float u2, float v2) {
        Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bb = tessellator.getBuilder();
		bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bb.vertex(pose.last().pose(), (float)x, (float)y2, 0.0f).uv(u, v2).endVertex();
		bb.vertex(pose.last().pose(), (float)x2, (float)y2, 0.0f).uv(u2, v2).endVertex();
		bb.vertex(pose.last().pose(), (float)x2, (float)y, 0.0f).uv(u2, v).endVertex();
		bb.vertex(pose.last().pose(), (float)x, (float)y, 0.0f).uv(u, v).endVertex();
		tessellator.end();
    }
}
