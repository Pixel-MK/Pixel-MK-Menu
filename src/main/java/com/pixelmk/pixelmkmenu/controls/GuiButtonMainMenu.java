package com.pixelmk.pixelmkmenu.controls;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.pixelmk.pixelmkmenu.gui.PixelMKMenuScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class GuiButtonMainMenu extends Button {

    private final Minecraft INSTANCE = Minecraft.getInstance();
    private int textWidth;
    private int lastTickNumber;

    private float lastPartialTick;
    private float alphaFalloff = 0.08f;
    protected float alpha = 0.0f;

    private Component hoverMessage;
    public boolean rightAlign;
    private String langKey;
    public int yOffset;

    public GuiButtonMainMenu(int xPos, int yPos, String langKey,
            ActionInstance handler, boolean rightAlign) {
        super(xPos, yPos, 150, 16, new TranslatableComponent(langKey), handler, Button.NO_TOOLTIP);
        handler.source = this;
        this.setMessage(new TranslatableComponent(langKey));
        this.textWidth = INSTANCE.font.width(this.getMessage().getString());
        this.rightAlign = rightAlign;
        this.langKey = langKey;
    }

    public GuiButtonMainMenu(String langKey, ActionInstance onPress) {
		this(0, 0, langKey, onPress, false);
	}

    public GuiButtonMainMenu(String langKey, OnPress onPress) {
        super(0, 0, 150, 16, new TranslatableComponent(langKey), onPress, Button.NO_TOOLTIP);
        this.setMessage(new TranslatableComponent(langKey));
        this.textWidth = INSTANCE.font.width(this.getMessage().getString());
        this.rightAlign = false;
        this.langKey = langKey;
    }

    @Override
    public Component getMessage() {
        if (this.isHovered)
            return this.hoverMessage;
        return super.getMessage();
    }

    public void updateButton(int updateCounter, float partialTicks, int mouseX, int mouseY) {
        if (!this.visible)
            return;
        float deltaTime = (updateCounter - this.lastTickNumber) + partialTicks - this.lastPartialTick;
        this.lastTickNumber = updateCounter;
        this.lastPartialTick = partialTicks;
        boolean mouseOver = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
                && mouseY < this.y + this.height);
        if (mouseOver) {
            if (this.alpha < 0.4f)
                this.alpha += this.alphaFalloff * deltaTime;
            if (this.alpha > 0.4f)
                this.alpha = 0.4f;
        } else if (this.alpha > 0.0f) {
            this.alpha -= this.alphaFalloff * deltaTime;
        }
        if (this.alpha < 0.0f)
            this.alpha = 0.0f;
    }

    @Override
    public void renderButton(PoseStack stack, int mouseX, int mouseY, float partial) {
        if (!this.visible)
            return;
        boolean mouseOver = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
                && mouseY < this.y + this.height);
        if (this.alpha > 0.0f) {
            int hlAlpha = mouseOver ? 1711276032 : (((int) (255.0f * this.alpha) & 0xFF) << 24);
            int hlGBCol = mouseOver ? ((int) (500.0f * (0.4f - this.alpha)) & 0xFF) : 0;
            drawButtonBackground(stack, mouseOver, hlAlpha, hlGBCol);
        }
        drawButtonText(mouseOver, stack);
    }

    public void drawButtonText(boolean mouseOver, PoseStack stack) {
    	int textColour = 16777215;
    	if(!this.active) {
    		textColour = 10526880;
    	}else if (mouseOver) {
    		textColour = 5308415;
    	}
    	int textIndent = 4 + (int)(10.0f * this.alpha);
    	if (this.rightAlign) {
    		drawString(stack, INSTANCE.font, this.getMessage(), this.x + this.width - textIndent - this.textWidth, this.y + (this.height -8)/2, textColour);
    	}else {
    		drawString(stack, INSTANCE.font, this.getMessage(), this.x + textIndent, this.y + (this.height - 8)/2, textColour);
    	}
    }

    public void drawButtonBackground(PoseStack pose, Boolean mouseOver, int hlAlpha, int hlGBCol) {
		int w = mouseOver ? Math.min(this.width, (int)(this.width * 2.5F * this.alpha)) : this.width;
		if(this.rightAlign) {
			drawRect(pose, this.x + this.width - w, this.y, this.x + this.width, this.y + this.height, hlAlpha | hlGBCol << 8 | hlGBCol, 4);
		}
		else {
			drawRect(pose, this.x, this.y, this.x + w, this.y + this.height, hlAlpha | hlGBCol << 8 | hlGBCol, 10);
		}
	}

    public static void drawRect(PoseStack pose, int x1, int y1, int x2, int y2, int colour, int offset) {
		if (x1 < x2) {
            x1 = x1 ^ x2;
            x2 = x1 ^ x2;
            x1 = x1 ^ x2;
		}
		if (y1 < y2) {
			y1 = y1 ^ y2;
            y2 = y1 ^ y2;
            y1 = y1 ^ y2;
		}
		float var10 = (colour >> 24 & 0xFF) / 255.0F;
	    float var6 = (colour >> 16 & 0xFF) / 255.0F;
	    float var7 = (colour >> 8 & 0xFF) / 255.0F;
	    float var8 = (colour & 0xFF) / 255.0F;
        Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder BufferBuilder = tessellator.getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(var6, var7, var8, var10);
		BufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        BufferBuilder.vertex(pose.last().pose(), (float)(x1 + offset), (float)y2, 0.0f).endVertex();
		BufferBuilder.vertex(pose.last().pose(), (float)x2, (float)y2, 0.0f).endVertex();
		BufferBuilder.vertex(pose.last().pose(), (float)(x2 - offset), (float)y1, 0.0f).endVertex();
		BufferBuilder.vertex(pose.last().pose(), (float)x1, (float)y1, 0.0f).endVertex();
        tessellator.end();
		RenderSystem.enableTexture();
	    RenderSystem.disableBlend();
	}

    public int getWidth() {
	    return this.width;
	}

    public GuiButtonMainMenu setup(PixelMKMenuScreen pixelMKMenuScreen) {
        this.setMessage(new TranslatableComponent(this.langKey));
		return this;
    }

    @Override
    public void playDownSound(SoundManager p_93665_) {
        //p_93665_.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
     }

}
