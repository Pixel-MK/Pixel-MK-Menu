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

package io.github.pixelmk.pixelmkmenu.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.pixelmk.pixelmkmenu.gui.components.ButtonPanel;
import io.github.pixelmk.pixelmkmenu.gui.components.TitleScreenButton;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;

/** Pixel MK Pause Screen. */
public class PauseScreen extends net.minecraft.client.gui.screens.PauseScreen {

  @SuppressWarnings("NullAway")
  private ButtonPanel buttonPanel;

  private int updateCounter;
  private boolean begunTweening;
  private float tweenTime;

  public PauseScreen() {
    this(true);
  }

  public PauseScreen(boolean showPauseMenu) {
    super(showPauseMenu);
  }

  /**
   * initialise the screeen and convert all buttons to the main menu buttons for the pause screen.
   */
  @Override
  protected void init() {
    super.init();
    this.buttonPanel =
        new ButtonPanel(
            ButtonPanel.AnchorType.BOTTOM_LEFT,
            22,
            20,
            150,
            100,
            16,
            this.width,
            this.height,
            "main",
            (buttonPanel) -> {});
    for (Renderable widget : this.renderables) {
      if (!(widget instanceof Button)) {
        continue;
      }
      Button button = (Button) widget;

      TitleScreenButton pauseMenuButton =
          new TitleScreenButton(button); // NOPMD - we need to instantiate here.
      this.buttonPanel.addButton(pauseMenuButton);
      pauseMenuButton.visible = button.visible;
      pauseMenuButton.active = button.active;
    }
    this.renderables.clear();
    this.children().clear();
    this.addRenderableWidget(this.buttonPanel);
  }

  /** Renders the screen using tweening and the pose stack. */
  @Override
  public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    if (!this.begunTweening) {
      this.tweenTime = -partialTicks;
      this.begunTweening = true;
    }
    float tweenPct = Math.min(0.5f, (this.updateCounter + partialTicks - this.tweenTime) / 20.0f);
    float tweenAmount = (float) Math.sin(tweenPct * Math.PI);
    int colour = (int) (192.0f * tweenAmount) << 24;
    PoseStack pose = guiGraphics.pose();
    pose.pushPose();
    pose.translate(-10.0f + tweenAmount * 20.0f, 0f, 0f);
    guiGraphics.fillGradient(10, 0, 184, this.height, colour, colour);
    guiGraphics.fill(10, 0, 11, this.height, -1);
    pose.translate(-10.0f + tweenAmount * 10.0f, 0.0f, 0.0f);
    pose.pushPose();
    pose.translate(27.0f, 40.0f, 0.0f);
    pose.scale(2.0f, 2.0f, 1.0f);
    guiGraphics.drawString(this.font, Component.translatable("menu.game"), 0, 0, 16777215);
    pose.popPose();
    this.buttonPanel.updateButtons(this.updateCounter, partialTicks, mouseX, mouseY);
    this.buttonPanel.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
    pose.popPose();
  }

  /** On every tick increment the <code>updateCounter</code>. */
  @Override
  public void tick() {
    super.tick();
    this.updateCounter++;
  }
}
