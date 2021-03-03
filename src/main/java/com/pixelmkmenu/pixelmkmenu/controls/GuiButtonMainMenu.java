package com.pixelmkmenu.pixelmkmenu.controls;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class GuiButtonMainMenu extends GuiButton{
	public boolean rightAlign;
	
	public int textWidth;
	public int yOffset = 0;
	private int lastTickNumber;
	
	private float lastPartialTick;
	private float alphaFalloff = 0.08f;
	protected float alpha = 0.0f;
	
	public GuiButtonMainMenu(int buttonId, String displayString) {
		this(buttonId, 0, 0, displayString, false);
	}

	public GuiButtonMainMenu(int buttonId, int xPos, int yPos, String displayString, boolean rightAlign) {
		super(buttonId, xPos, yPos, displayString);
		this.height = 16;
		this.width = 150;
		this.textWidth = (Minecraft.getMinecraft().fontRenderer.getStringWidth(displayString));
		this.rightAlign = rightAlign;
	}
	
	/**
	 * Changes the button's alpha value depending on how much time has passed since the mouse has hovered over.
	 * 
	 * @param updateCounter
	 * @param partialTicks
	 * @param mouseX
	 * @param mouseY
	 */
	public void updateButton(int updateCounter, float partialTicks, int mouseX, int mouseY) {
		if (this.visible) {
			float deltaTime = (updateCounter- this.lastTickNumber) + partialTicks - this.lastPartialTick;
			this.lastTickNumber = updateCounter;
			this.lastPartialTick = partialTicks;
			boolean mouseOver = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x +this.width && mouseY < this.y + this.height);
			if (mouseOver) {
				if(this.alpha < 0.4f) this.alpha += this.alphaFalloff * deltaTime;
				if (this.alpha > 0.4f) this.alpha = 0.4f;
			}else if (this.alpha > 0.0f) {
				this.alpha -= this.alphaFalloff * deltaTime;
			}
			if(this.alpha < 0.0f) this.alpha = 0.0f;
		}
	}
	
	@Override
    public void drawButton(Minecraft parlMinecraft, int mouseX, int mouseY, float partialTicks)
    {
    	if(this.visible) {
    		boolean mouseOver = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x +this.width && mouseY < this.y + this.height);
    		if(this.alpha > 0.0f) {
    			int hlAlpha = mouseOver ? 1711276032 : (((int)(255.0f * this.alpha) & 0xFF) << 24);
    			int hlGBCol = mouseOver ? ((int)(500.0f * (0.4f - this.alpha)) & 0xFF) : 0;
    			drawButtonBackground(mouseOver, hlAlpha, hlGBCol);
    		}
    		mouseDragged(parlMinecraft, mouseX, mouseY);
    		drawButtonText(mouseOver, parlMinecraft);
    	}
    }
    
    public void drawButtonText(boolean mouseOver, Minecraft parlMinecraft) {
    	int textColour = 16777215;
    	if(!this.enabled) {
    		textColour = 10526880;
    	}else if (mouseOver) {
    		textColour = 5308415;
    	}
    	int textIndent = 4 + (int)(10.0f * this.alpha);
    	if (this.rightAlign) {
    		drawString(parlMinecraft.fontRenderer, this.displayString, this.x + this.width - textIndent - this.textWidth, this.y + (this.height -8)/2, textColour);
    	}else {
    		drawString(parlMinecraft.fontRenderer, this.displayString, this.x + textIndent, this.y + (this.height - 8)/2, textColour);
    	}
    }
	
    //TODO fix Background rear in middle of Screen instead of behind button. Look at reference images
    /**
     * Draws trapezium over button gradually.
     * 
     * @param mouseOver is mouse over button
     * @param hlAlpha
     * @param hlGBCol
     */
	public void drawButtonBackground(Boolean mouseOver, int hlAlpha, int hlGBCol) {
		int w = mouseOver ? Math.min(this.width, (int)(this.width * 2.5F * this.alpha)) : this.width;
		if(this.rightAlign) {
			drawRect(this.x + this.width - w, this.y, this.x + this.width, this.y + this.height, hlAlpha | hlGBCol << 8 | hlGBCol, 4);
		}
		else {
			drawRect(this.x, this.y, this.x + w, this.y + this.height, hlAlpha | hlGBCol << 8 | hlGBCol, 4);
		}
	}
	
	//TODO fix Background rear in middle of Screen instead of behind button. Look at reference images
	/**
	 *  Positions rectangle background over the button.
	 * 
	 * @param x1 parameter 1 for width of bounding rectangle. The absolute value of the difference between x1 and x2 would be the width.
	 * @param y1 parameter 1 for height of bounding rectangle. The absolute value of the difference between y1 and y2 would be the height.
	 * @param x2 parameter 2 for width of bounding rectangle. The absolute value of the difference between x1 and x2 would be the width.
	 * @param y2 parameter 1 for height of bounding rectangle. The absolute value of the difference between y1 and y2 would be the height.
	 * @param colour Colour of the background as a combined RBGA value
	 * @param offset x-offset for trapezium to be rendered correctly
	 */
	public static void drawRect(int x1, int y1, int x2, int y2, int colour, int offset) {
		if (x1 < x2) {
			int var5 = x1;
			x1 = x2;
			x2 = var5;
		} 
		if (y1 < y2) {
			int var5 = y1;
			y1 = y2;
			y2 = var5;
		}
		float var10 = (colour >> 24 & 0xFF) / 255.0F;
	    float var6 = (colour >> 16 & 0xFF) / 255.0F;
	    float var7 = (colour >> 8 & 0xFF) / 255.0F;
	    float var8 = (colour & 0xFF) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder BufferBuilder = tessellator.getBuffer();
		GL11.glEnable(3042);
	    GL11.glDisable(3553);
	    GL11.glBlendFunc(770, 771);
	    GL11.glColor4f(var6, var7, var8, var10);
		BufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		BufferBuilder.pos((x1 + offset), y2, 0.0d).endVertex();
		BufferBuilder.pos(x2, y2, 0.0d).endVertex();
		BufferBuilder.pos((x2 - offset), y1, 0.0d).endVertex();
		BufferBuilder.pos(x1, y1, 0.0d).endVertex();
		tessellator.draw();
		GL11.glEnable(3553);
	    GL11.glDisable(3042);
	}
	
	public int getWidth() {
	    return this.width;
	}
}
