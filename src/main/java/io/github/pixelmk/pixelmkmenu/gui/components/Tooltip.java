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
import io.github.pixelmk.pixelmkmenu.gui.renderer.PixelMKRenderType;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor.ARGB32;
import org.joml.Matrix4f;

/** Creates a rendered GUI string with a hoverable tooltip. */
public class Tooltip extends AbstractWidget {

  private static final Minecraft INSTANCE = Minecraft.getInstance();
  private int textWidth;
  private int tooltipWidth;
  private int tooltipHeight;
  private int verticalOffset;
  private ArrayList<Component> textArray = new ArrayList<>();
  private Component tooltipText;
  private int tweak;

  /**
   * Constructs a tooltip using the given parameters.
   *
   * @param tooltipLines lines to display in the tooltip
   * @param textShort rendered text
   * @param positionX position on the horizontal
   * @param positionY position on the vertical
   * @param width width of the tooltip
   * @param height height of the tooltip
   * @param verticalOffset vertical offset of the tooltip
   */
  public Tooltip(
      List<String> tooltipLines,
      String textShort,
      int positionX,
      int positionY,
      int width,
      int height,
      int verticalOffset) {
    super(positionX, positionY, 0, 0, Component.translatable(textShort));
    this.setMessage(Component.translatable(textShort));
    this.textWidth = INSTANCE.font.width(this.getMessage().getString());
    this.width = this.textWidth;
    this.height = INSTANCE.font.lineHeight;
    this.tooltipWidth = 0;
    this.tooltipHeight = height;
    this.verticalOffset = verticalOffset;
    this.tooltipText = Component.empty();
    for (String str : tooltipLines) {
      Component line = Component.translatable(str);
      this.textArray.add(line);
      if (this.tooltipWidth < INSTANCE.font.width(line) + 18) {
        this.tooltipWidth = INSTANCE.font.width(line) + 18;
      }
    }
  }

  /**
   * Creates a tooltip using the given parameters.
   *
   * @param tooltipText tooltip text
   * @param textShort text displayed
   * @param positionX where is the text on the horizontal
   * @param positionY where is the text on the vertical
   * @param verticalOffset how far away is the tooltip
   */
  public Tooltip(
      String tooltipText, String textShort, int positionX, int positionY, int verticalOffset) {
    super(positionX, positionY, 0, 0, Component.translatable(textShort));
    this.setMessage(Component.translatable(textShort));
    this.textWidth = INSTANCE.font.width(this.getMessage().getString());
    this.width = this.textWidth;
    this.height = INSTANCE.font.lineHeight;
    this.tooltipWidth = INSTANCE.font.width(tooltipText) + 18;
    this.tooltipHeight = INSTANCE.font.lineHeight + 3;
    this.tooltipText = Component.translatable(tooltipText);
    this.tweak = 0;
    this.verticalOffset = verticalOffset;
  }

  @Override
  protected void renderWidget(
      @Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    guiGraphics.drawString(INSTANCE.font, this.getMessage(), this.getX(), this.getY(), 16777215);
    if (this.isHovered()) {
      if (!this.tooltipText.getString().equals("")) {
        drawTooltip(guiGraphics, mouseX, mouseY);
        guiGraphics.drawString(
            INSTANCE.font,
            this.tooltipText,
            mouseX + 16,
            mouseY + 2 - this.verticalOffset,
            16777215);
      } else {
        this.tweak = 45;
        int height = textArray.size() * 8 + 6;
        this.tooltipHeight = height + 10;
        this.verticalOffset = 10;
        drawTooltip(guiGraphics, 80, (this.getY() - 10) - height + 2);
        int top = (this.getY() + 10) - height - 12;
        for (Component brand : this.textArray) {
          guiGraphics.drawString(INSTANCE.font, brand, 100, top - this.verticalOffset, -1);
          top += 10;
        }
      }
    }
  }

  @Override
  public final void setMessage(@Nonnull Component message) {
    super.setMessage(message);
  }

  @Override
  public final Component getMessage() {
    return super.getMessage();
  }

  private void drawTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    int colour = 1610612736;
    guiGraphics.fill(
        mouseX + 14,
        mouseY - this.verticalOffset,
        mouseX + this.tooltipWidth + 14,
        mouseY + this.tooltipHeight - this.verticalOffset,
        colour);
    float alpha = (float) ARGB32.alpha(colour) / 255.0F;
    float red = (float) ARGB32.red(colour) / 255.0F;
    float green = (float) ARGB32.green(colour) / 255.0F;
    float blue = (float) ARGB32.blue(colour) / 255.0F;
    BufferSource bufferSource = guiGraphics.bufferSource();
    VertexConsumer vertexConsumer = bufferSource.getBuffer(PixelMKRenderType.tooltip());
    Matrix4f matrix4f = guiGraphics.pose().last().pose();
    // mouseX, mouseY
    vertexConsumer
        .vertex(matrix4f, (float) mouseX, (float) (mouseY + this.tweak), 0.0f)
        .color(red, green, blue, alpha)
        .endVertex();
    // mouseX + 14, mouseY + tooltip height - vertical offset
    vertexConsumer
        .vertex(
            matrix4f,
            (float) (mouseX + 14),
            (float) (mouseY + this.tooltipHeight - this.verticalOffset),
            0.0f)
        .color(red, green, blue, alpha)
        .endVertex();
    // mouseX + 14, mouseY - vertical offset
    vertexConsumer
        .vertex(matrix4f, (float) (mouseX + 14), (float) mouseY - this.verticalOffset, 0.0f)
        .color(red, green, blue, alpha)
        .endVertex();
    guiGraphics.flush();
  }

  @Override
  protected void updateWidgetNarration(@Nonnull NarrationElementOutput narrationElementOutput) {
    this.defaultButtonNarrationText(narrationElementOutput);
  }
}
