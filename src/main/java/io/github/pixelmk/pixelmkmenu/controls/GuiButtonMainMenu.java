package io.github.pixelmk.pixelmkmenu.controls;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import io.github.pixelmk.pixelmkmenu.gui.PixelMKMenuScreen;
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

    /**
     *
     * Creates a button instance using the position of the button, a lang key, an
     * <code>ActionInstance</code> and whether it is right aligned or not.
     *
     * @param xPos
     * @param yPos
     * @param langKey
     * @param handler
     * @param rightAlign
     */
    public GuiButtonMainMenu(int xPos, int yPos, String langKey,
            ActionInstance handler, boolean rightAlign) {
        super(xPos, yPos, 150, 16, new TranslatableComponent(langKey), handler, Button.NO_TOOLTIP);
        handler.source = this;
        this.setMessage(new TranslatableComponent(langKey));
        this.textWidth = INSTANCE.font.width(this.getMessage().getString());
        this.rightAlign = rightAlign;
        this.langKey = langKey;
    }

    /**
     * Creates a button instance from a lang key and an <code>ActionInstance</code>
     *
     * @param langKey
     * @param onPress
     */
    public GuiButtonMainMenu(String langKey, ActionInstance onPress) {
        this(0, 0, langKey, onPress, false);
    }

    /**
     * Creates a button instance from a lang key and the native onPress
     *
     * @param langKey
     * @param onPress
     */
    public GuiButtonMainMenu(String langKey, OnPress onPress) {
        super(0, 0, 150, 16, new TranslatableComponent(langKey), onPress, Button.NO_TOOLTIP);
        this.setMessage(new TranslatableComponent(langKey));
        this.textWidth = INSTANCE.font.width(this.getMessage().getString());
        this.rightAlign = false;
        this.langKey = langKey;
    }

    /**
     * Creates a button instance from a message component and the native onPress
     *
     * @param message
     * @param onPress
     */
    public GuiButtonMainMenu(Component message, OnPress onPress) {
        super(0, 0, 150, 16, message, onPress, Button.NO_TOOLTIP);
        this.textWidth = INSTANCE.font.width(this.getMessage().getString());
        this.rightAlign = false;
    }

    /**
     * Returns the apropriate message of the button
     */
    @Override
    public Component getMessage() {
        if (this.isHovered)
            return this.hoverMessage;
        if (super.getMessage() != null)
            return super.getMessage();
        else
            return new TranslatableComponent("NONE");
    }

    /**
     * Updates the button,
     * Updates alpha values
     *
     * @param updateCounter
     * @param partialTicks
     * @param mouseX
     * @param mouseY
     */
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

    /**
     * Renders the button. Checks if mouse is over the button and calculates colours
     * accordingly.
     */
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

    /**
     *
     * Draws and colours text of the button.
     *
     * @param mouseOver
     * @param stack
     */
    public void drawButtonText(boolean mouseOver, PoseStack stack) {
        int textColour = 16777215;
        if (!this.active) {
            textColour = 10526880;
        } else if (mouseOver) {
            textColour = 5308415;
        }
        int textIndent = 4 + (int) (10.0f * this.alpha);
        if (this.rightAlign) {
            drawString(stack, INSTANCE.font, this.getMessage(), this.x + this.width - textIndent - this.textWidth,
                    this.y + (this.height - 8) / 2, textColour);
        } else {
            drawString(stack, INSTANCE.font, this.getMessage(), this.x + textIndent, this.y + (this.height - 8) / 2,
                    textColour);
        }
    }

    /**
     * Wrapper to configure params passed to <code>drawRect</code>
     *
     * @param pose
     * @param mouseOver is the mouse over the button
     * @param hlAlpha
     * @param hlGBCol
     */
    public void drawButtonBackground(PoseStack pose, Boolean mouseOver, int hlAlpha, int hlGBCol) {
        int w = mouseOver ? Math.min(this.width, (int) (this.width * 2.5F * this.alpha)) : this.width;
        if (this.rightAlign) {
            drawRect(pose, this.x + this.width - w, this.y, this.x + this.width, this.y + this.height,
                    hlAlpha | hlGBCol << 8 | hlGBCol, 4);
        } else {
            drawRect(pose, this.x, this.y, this.x + w, this.y + this.height, hlAlpha | hlGBCol << 8 | hlGBCol, 10);
        }
    }

    /**
     * Draws the parallelogram for the button.
     *
     * @param pose
     * @param x1     bottom left corner x
     * @param y1     bottom left corner y
     * @param x2     top right corner x
     * @param y2     top right corner y
     * @param colour colour of the background
     * @param offset how slanted do you want the parallelogram
     */
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
        float alpha = (colour >> 24 & 0xFF) / 255.0F;
        float red = (colour >> 16 & 0xFF) / 255.0F;
        float green = (colour >> 8 & 0xFF) / 255.0F;
        float blue = (colour & 0xFF) / 255.0F;
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder BufferBuilder = tessellator.getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(red, green, blue, alpha);
        BufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        BufferBuilder.vertex(pose.last().pose(), (float) (x1 + offset), (float) y2, 0.0f).endVertex();
        BufferBuilder.vertex(pose.last().pose(), (float) x2, (float) y2, 0.0f).endVertex();
        BufferBuilder.vertex(pose.last().pose(), (float) (x2 - offset), (float) y1, 0.0f).endVertex();
        BufferBuilder.vertex(pose.last().pose(), (float) x1, (float) y1, 0.0f).endVertex();
        tessellator.end();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    /**
     * Getter for <code>width</code>
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Setup wrapper to set the message of the button
     *
     * @param pixelMKMenuScreen
     * @return
     */
    public GuiButtonMainMenu setup(PixelMKMenuScreen pixelMKMenuScreen) {
        this.setMessage(new TranslatableComponent(this.langKey));
        return this;
    }

    /**
     * Empty method to remove the sound of clicking a button
     */
    @Override
    public void playDownSound(SoundManager soundManager) {
    }

}
