/*
* Copyright 2024 Joe Targett, Pixel MK Group
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
* associated documentation files (the “Software”), to deal in the Software without restriction,
* including without limitation the rights to use, copy, modify, merge, publish, distribute,
* sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all copies or
* substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
* NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
* DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package io.github.pixelmk.pixelmkmenu.gui.components;

import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

/** Button that appears on the Pixel MK title screen. */
@OnlyIn(Dist.CLIENT)
public class TitleScreenButton extends Button {

  /** Offset on the vertical. */
  public final int offsetY;

  /** Is this button aligned to the right of the screen. */
  public boolean rightAlign;

  /** Message on hover. */
  private final Component hoverMessage;

  /** Text width. */
  private final int textWidth;

  /** Previous tick. */
  private int lastTickNumber;

  /** Previous partial tick. */
  private float lastPartialTick;

  /** Alpha falloff rate. */
  private static final float ALPHA_FALLOFF = 0.08f;

  /** Button Builder. */
  public static class Builder extends Button.Builder {
    private final boolean rightAlign;

    Builder(Component message, OnPress onPress) {
      super(message, onPress);
      this.rightAlign = false;
    }

    Builder(Component message, OnPress onPress, boolean rightAlign) {
      super(message, onPress);
      this.rightAlign = rightAlign;
    }
  }

  /**
   * Get a button builder from just the message component and the onPress action.
   *
   * @param message message component to be displayed.
   * @param onPress action on press.
   * @return the button builder.
   */
  public static Builder builder(Component message, OnPress onPress) {
    Builder builder = new Builder(message, onPress);
    builder.size(150, 16);
    return builder;
  }

  /**
   * Get a button builder from just the message component and the onPress action.
   *
   * @param message message component to be displayed.
   * @param onPress action on press.
   * @param rightAlign is this button aligned to the right of the screen?
   * @return the button builder.
   */
  public static Builder builder(Component message, OnPress onPress, boolean rightAlign) {
    Builder builder = new Builder(message, onPress, rightAlign);
    builder.size(150, 16);
    return builder;
  }

  /**
   * Draws the parallelogram for the button.
   *
   * @param guiGraphics GUI draw helper
   * @param x1 bottom left corner x
   * @param y1 bottom left corner y
   * @param x2 top right corner x
   * @param y2 top right corner y
   * @param colour colour of the background
   * @param offset how slanted do you want the parallelogram
   */
  @SuppressWarnings("PMD.AvoidReassigningParameters")
  public static void drawRect(
      GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int colour, int offset) {
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
    final BufferSource bufferSource = guiGraphics.bufferSource();
    final VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.gui());
    final Matrix4f matrix4f = guiGraphics.pose().last().pose();
    vertexConsumer
        .vertex(matrix4f, (float) (x1 + offset), (float) y2, 0.0f)
        .color(colour)
        .endVertex();
    vertexConsumer.vertex(matrix4f, (float) x2, (float) y2, 0.0f).color(colour).endVertex();
    vertexConsumer
        .vertex(matrix4f, (float) (x2 - offset), (float) y1, 0.0f)
        .color(colour)
        .endVertex();
    vertexConsumer.vertex(matrix4f, (float) x1, (float) y1, 0.0f).color(colour).endVertex();
  }

  /**
   * Takes in a button builder to build a <code>TitleScreenButton</code>.
   *
   * @param builder Button builder
   */
  protected TitleScreenButton(Builder builder) {
    super(builder);
    this.rightAlign = builder.rightAlign;
    Minecraft instance = Minecraft.getInstance(); // NOPMD - this is not applicable here.
    this.textWidth = instance.font.width(this.getMessage().getString()); // NOPMD - trust get font.
    this.hoverMessage = Component.empty();
    this.alpha = 0.0f;
    this.offsetY = 0;
  }

  public TitleScreenButton(Button button) {
    this(builder(button.getMessage(), button.onPress));
  }

  /**
   * Wrapper to configure params passed to <code>drawRect</code>.
   *
   * @param guiGraphics GUI Graphics Helper
   * @param mouseOver is the mouse over the button
   * @param hightlightAlpha alpha transparency
   * @param highlightRGBCol RGB hex.
   */
  public void drawButtonBackground(
      GuiGraphics guiGraphics, Boolean mouseOver, int hightlightAlpha, int highlightRGBCol) {
    int w = mouseOver ? Math.min(this.width, (int) (this.width * 2.5F * this.alpha)) : this.width;
    if (this.rightAlign) {
      drawRect(
          guiGraphics,
          this.getX() + this.width - w,
          this.getY(),
          this.getX() + this.width,
          this.getY() + this.height,
          hightlightAlpha | highlightRGBCol << 8 | highlightRGBCol,
          10);
    } else {
      drawRect(
          guiGraphics,
          this.getX(),
          this.getY(),
          this.getX() + w,
          this.getY() + this.height,
          hightlightAlpha | highlightRGBCol << 8 | highlightRGBCol,
          10);
    }
  }

  /** Empty method to remove the sound of clicking a button. */
  @Override
  public void playDownSound(@Nonnull SoundManager soundManager) {}

  /** Returns the apropriate message of the button. */
  @Override
  public final Component getMessage() {
    if (this.isHovered) {
      return this.hoverMessage;
    }
    return super.getMessage();
  }

  @Override
  protected void renderWidget(
      @Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    if (!this.visible) {
      return;
    }
    final float drawBackgroundThreshold = 0.0f;
    boolean mouseOver =
        (mouseX >= this.getX()
            && mouseY >= this.getY()
            && mouseX < this.getX() + this.width
            && mouseY < this.getY() + this.height);
    if (this.alpha > drawBackgroundThreshold) {
      int hightlightAlpha = mouseOver ? 1711276032 : (((int) (255.0f * this.alpha) & 0xFF) << 24);
      int highlightRGBCol = mouseOver ? ((int) (500.0f * (0.4f - this.alpha)) & 0xFF) : 0;
      drawButtonBackground(guiGraphics, mouseOver, hightlightAlpha, highlightRGBCol);
    }
    drawButtonText(mouseOver, guiGraphics);
  }

  /**
   * Draws and colours text of the button.
   *
   * @param mouseOver is mouse over this widget
   * @param guiGraphics graphics helper
   */
  public void drawButtonText(boolean mouseOver, GuiGraphics guiGraphics) {
    Minecraft minecraft = Minecraft.getInstance(); // NOPMD - this is not applicable here
    int textColour = 16777215;
    if (!this.active) {
      textColour = 10526880;
    } else if (mouseOver) {
      textColour = 5308415;
    }
    int textIndent = 4 + (int) (10.0f * this.alpha);
    if (this.rightAlign) {
      guiGraphics.drawString(
          minecraft.font,
          this.getMessage(),
          this.getX() + this.width - textIndent - this.textWidth,
          this.getY() + (this.height - 8) / 2,
          textColour);
    } else {
      guiGraphics.drawString(
          minecraft.font,
          this.getMessage(),
          this.getX() + textIndent,
          this.getY() + (this.height - 8) / 2,
          textColour);
    }
  }

  /**
   * Updates the button's propertys per tick.
   *
   * <p>Updates and sets the following fields:
   *
   * <ul>
   *   <li><code>lastTickNumber</code>
   *   <li><code>lastPartialTick</code>
   *   <li><code>alpha</code>
   * </ul>
   *
   * <p><code>alpha</code> is the most useful of these as it controls the state of the button.
   *
   * @param updateCounter the update counter
   * @param partialTick current partial tick
   * @param mouseX mouse position on the horizontal
   * @param mouseY mouse position on the vertical
   */
  public void updateButton(int updateCounter, float partialTick, int mouseX, int mouseY) {
    if (!this.visible) {
      return;
    }
    float deltaTime = (updateCounter - this.lastTickNumber) + partialTick - this.lastPartialTick;
    this.lastTickNumber = updateCounter;
    this.lastPartialTick = partialTick;
    boolean mouseOver =
        (mouseX >= this.getX()
            && mouseY >= this.getY()
            && mouseX < this.getX() + this.width
            && mouseY < this.getY() + this.height);
    final float atRestAlpha = 0.0f;
    final float mouseOverUpperBound = 0.4f;
    if (mouseOver) {
      if (this.alpha < mouseOverUpperBound) {
        this.alpha += ALPHA_FALLOFF * deltaTime;
      }
      if (this.alpha > mouseOverUpperBound) {
        this.alpha = 0.4f;
      }
    } else if (this.alpha > atRestAlpha) {
      this.alpha -= ALPHA_FALLOFF * deltaTime;
    }
    if (this.alpha < atRestAlpha) {
      this.alpha = 0.0f;
    }
  }
}
