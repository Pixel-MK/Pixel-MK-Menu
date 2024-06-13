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

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/** Button panels for organising buttons on the menu. */
@OnlyIn(Dist.CLIENT)
public class ButtonPanel extends Button {

  /** Where the anchor for the button panel is located. */
  public enum AnchorType {
    /** Anchor to top left of screen. */
    TOP_LEFT,
    /** Anchor to bottom left of screen. */
    BOTTOM_LEFT,
    /** Anchor to top right of screen. */
    TOP_RIGHT,
    /** Anchor to bottom right of screen. */
    BOTTOM_RIGHT;
  }

  /** Where to anchor the panel to the screen. */
  protected AnchorType anchorType;

  /** List of buttons in the panel. */
  protected List<TitleScreenButton> buttons = new ArrayList<>();

  /** Previously/currently pressed button in the panel. */
  @Nullable protected TitleScreenButton pressedButton;

  /** Offset on the horizontal. */
  private final int offsetX;

  /** Offset on the vertical. */
  private final int offsetY;

  /** Spacing between each button. */
  private final int buttonSpacing;

  /**
   * Construct a button panel.
   *
   * @param anchorType where to place the button panel.
   * @param offsetX How far from the side of the button panel should a button be placed.
   * @param offsetY How far from the vertical of the panel should a button be placed.
   * @param width How wide the button panel is
   * @param height How tall the button panel is
   * @param buttonSpacing How far apart from each other should each button be
   * @param containerWidth How wide the inside of the panel should be
   * @param containerHeight How tall the inside of the panel sould be
   * @param tag Narratable name
   * @param onPress action taken on press.
   */
  public ButtonPanel(
      final AnchorType anchorType,
      final int offsetX,
      final int offsetY,
      final int width,
      final int height,
      final int buttonSpacing,
      final int containerWidth,
      final int containerHeight,
      final String tag,
      final OnPress onPress) {
    super(0, 0, width, height, Component.translatable(tag), onPress, DEFAULT_NARRATION);
    this.anchorType = anchorType;
    this.offsetX = offsetX;
    this.offsetY = offsetY;
    this.buttonSpacing = buttonSpacing;
    updatePosition(containerWidth, containerHeight);
  }

  /**
   * Updates the position of the panel and then the buttons.
   *
   * @param containerWidth width of the button panel
   * @param containerHeight height of the button panel
   */
  public final void updatePosition(final int containerWidth, final int containerHeight) {
    this.setY(
        (this.anchorType == AnchorType.TOP_RIGHT || this.anchorType == AnchorType.TOP_LEFT)
            ? this.offsetY
            : (containerHeight - this.height - this.offsetY));
    this.setX(
        (this.anchorType == AnchorType.TOP_LEFT || this.anchorType == AnchorType.BOTTOM_LEFT)
            ? this.offsetX
            : (containerWidth - this.width - this.offsetX));
    updateButtonPositions();
  }

  @Override
  public final void setY(final int y) {
    super.setY(y);
  }

  @Override
  public final void setX(final int x) {
    super.setX(x);
  }

  /**
   * Adds a button to the panel using <code>displayText</code> and <code>ActionInstance</code>.
   *
   * @param displayText Button text
   * @param onPress Action taken by button on press
   * @return Created button
   */
  public TitleScreenButton addButton(final String displayText, final OnPress onPress) {
    final TitleScreenButton button =
        new TitleScreenButton(
            TitleScreenButton.builder(Component.translatable(displayText), onPress));
    this.buttons.add(button);
    updateButtonPositions();
    return button;
  }

  /**
   * Adds a button to the panel using <code>displayText</code> and <code>ActionInstance</code>.
   *
   * @param button button to add
   */
  public void addButton(final TitleScreenButton button) {
    this.buttons.add(button);
    updateButtonPositions();
  }

  /** Updates the position of the buttons. */
  private void updateButtonPositions() {
    if (this.anchorType == AnchorType.TOP_LEFT || this.anchorType == AnchorType.BOTTOM_LEFT) {
      final int btnHorizontalPosition = 0;
      for (final TitleScreenButton button : this.buttons) {
        button.rightAlign = false;
        button.setX(btnHorizontalPosition);
      }
    } else {
      for (final TitleScreenButton button : this.buttons) {
        button.rightAlign = true;
        button.setX(this.width - button.getWidth());
      }
    }
  }

  /** Renders the panel and also renders the children of the panel iteratively. */
  @Override
  public void renderWidget(
      final @Nonnull GuiGraphics guiGraphics,
      final int mouseX,
      final int mouseY,
      final float partialTicks) {
    int mouseHorizontal = mouseX;
    int mouseVertical = mouseY;
    mouseHorizontal -= this.getX();
    mouseVertical -= this.getY();
    final PoseStack pose = guiGraphics.pose();
    int buttonVerticalPosition = 0;
    int visibleButtonCount = 0;
    for (final TitleScreenButton button : this.buttons) {
      if (button.visible) {
        ++visibleButtonCount;
      }
    }
    if (this.anchorType == AnchorType.BOTTOM_LEFT || this.anchorType == AnchorType.BOTTOM_RIGHT) {
      buttonVerticalPosition = this.height - this.buttonSpacing * visibleButtonCount;
    }
    pose.pushPose();
    pose.translate(this.getX(), this.getY(), 0.0f);
    for (final TitleScreenButton button : this.buttons) {
      if (button.visible) {
        button.setY(buttonVerticalPosition + button.offsetY);
        buttonVerticalPosition += this.buttonSpacing;
      }
      button.renderWidget(guiGraphics, mouseHorizontal, mouseVertical, partialTicks);
    }
    pose.popPose();
  }

  @Override
  public boolean mouseClicked(final double mouseX, final double mouseY, final int buttonNum) {
    double mouseHorizontal = mouseX;
    double mouseVertical = mouseY;
    mouseHorizontal -= this.getX();
    mouseVertical -= this.getY();
    for (TitleScreenButton button : this.buttons) {
      if (button.mouseClicked(mouseHorizontal, mouseVertical, buttonNum)) {
        this.pressedButton = button;
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the mouse is over a particular button.
   *
   * @param button button to check
   * @param minecraft minecraft instance
   * @param mouseX mouse position on the horizontal
   * @param mouseY mouse position on the vertical
   * @return if the mouse is over the button.
   */
  public boolean isMouseOver(
      final TitleScreenButton button,
      final Minecraft minecraft,
      final int mouseX,
      final int mouseY) {
    if (!this.buttons.contains(button)) {
      return false;
    }
    int mouseHorizontal = mouseX;
    int mouseVertical = mouseY;
    mouseHorizontal -= this.getX();
    mouseVertical -= this.getY();
    return button.mouseClicked(mouseHorizontal, mouseVertical, 0);
  }

  /**
   * Update each button in order.
   *
   * @param updateCounter Current tick.
   * @param partialTick partial tick.
   * @param mouseX location of mouse on the horizontal.
   * @param mouseY location of mouse on the vertical.
   */
  public void updateButtons(
      final int updateCounter, final float partialTick, int mouseX, int mouseY) {
    int mouseHorizontal = mouseX;
    int mouseVertical = mouseY;
    mouseHorizontal -= this.getX();
    mouseVertical -= this.getY();
    for (TitleScreenButton button : this.buttons) {
      button.updateButton(updateCounter, partialTick, mouseHorizontal, mouseVertical);
    }
  }
}
