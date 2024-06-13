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
    TopLeft,
    /** Anchor to bottom left of screen. */
    BottomLeft,
    /** Anchor to top right of screen. */
    TopRight,
    /** Anchor to bottom right of screen. */
    BottomRight;
  }

  /** Where to anchor the panel to the screen. */
  protected AnchorType anchorType = AnchorType.BottomLeft;

  /** List of buttons in the panel. */
  protected List<TitleScreenButton> buttons = new ArrayList<TitleScreenButton>();

  /** Previously/currently pressed button in the panel. */
  @Nullable protected TitleScreenButton pressedButton;

  /** Offset on the horizontal. */
  private int offsetX;

  /** Offset on the vertical. */
  private int offsetY;

  /** Spacing between each button. */
  private int buttonSpacing = 16;

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
      AnchorType anchorType,
      int offsetX,
      int offsetY,
      int width,
      int height,
      int buttonSpacing,
      int containerWidth,
      int containerHeight,
      String tag,
      OnPress onPress) {
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
  public final void updatePosition(int containerWidth, int containerHeight) {
    this.setY(
        (this.anchorType == AnchorType.TopRight || this.anchorType == AnchorType.TopLeft)
            ? this.offsetY
            : (containerHeight - this.height - this.offsetY));
    this.setX(
        (this.anchorType == AnchorType.TopLeft || this.anchorType == AnchorType.BottomLeft)
            ? this.offsetX
            : (containerWidth - this.width - this.offsetX));
    updateButtonPositions();
  }

  @Override
  public final void setY(int y) {
    super.setY(y);
  }

  @Override
  public final void setX(int x) {
    super.setX(x);
  }

  /**
   * Adds a button to the panel using <code>displayText</code> and <code>ActionInstance</code>.
   *
   * @param displayText Button text
   * @param onPress Action taken by button on press
   * @return Created button
   */
  public TitleScreenButton addButton(String displayText, OnPress onPress) {
    TitleScreenButton button =
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
  public void addButton(TitleScreenButton button) {
    this.buttons.add(button);
    updateButtonPositions();
  }

  /** Updates the position of the buttons. */
  private void updateButtonPositions() {
    if (this.anchorType == AnchorType.TopLeft || this.anchorType == AnchorType.BottomLeft) {
      int buttonHorizontalPosition = 0;
      for (TitleScreenButton button : this.buttons) {
        button.rightAlign = false;
        button.setX(buttonHorizontalPosition);
      }
    } else {
      for (TitleScreenButton button : this.buttons) {
        button.rightAlign = true;
        button.setX(this.width - button.getWidth());
      }
    }
  }

  /** Renders the panel and also renders the children of the panel iteratively. */
  @Override
  public void renderWidget(
      @Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    mouseX -= this.getX();
    mouseY -= this.getY();
    PoseStack pose = guiGraphics.pose();
    int buttonVerticalPosition = 0;
    int visibleButtonCount = 0;
    for (TitleScreenButton button : this.buttons) {
      if (button.visible) {
        ++visibleButtonCount;
      }
    }
    if (this.anchorType == AnchorType.BottomLeft || this.anchorType == AnchorType.BottomRight) {
      buttonVerticalPosition = this.height - this.buttonSpacing * visibleButtonCount;
    }
    pose.pushPose();
    pose.translate(this.getX(), this.getY(), 0.0f);
    for (TitleScreenButton button : this.buttons) {
      if (button.visible) {
        button.setY(buttonVerticalPosition + button.offsetY);
        buttonVerticalPosition += this.buttonSpacing;
      }
      button.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
    }
    pose.popPose();
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int buttonNum) {
    mouseX -= this.getX();
    mouseY -= this.getY();
    for (TitleScreenButton button : this.buttons) {
      if (button.mouseClicked(mouseX, mouseY, buttonNum)) {
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
      TitleScreenButton button, Minecraft minecraft, int mouseX, int mouseY) {
    if (!this.buttons.contains(button)) {
      return false;
    }
    mouseX -= this.getX();
    mouseY -= this.getY();
    return button.mouseClicked(mouseX, mouseY, 0);
  }

  /**
   * Update each button in order.
   *
   * @param updateCounter Current tick.
   * @param partialTick partial tick.
   * @param mouseX location of mouse on the horizontal.
   * @param mouseY location of mouse on the vertical.
   */
  public void updateButtons(int updateCounter, float partialTick, int mouseX, int mouseY) {
    mouseX -= this.getX();
    mouseY -= this.getY();
    for (TitleScreenButton button : this.buttons) {
      button.updateButton(updateCounter, partialTick, mouseX, mouseY);
    }
  }
}
