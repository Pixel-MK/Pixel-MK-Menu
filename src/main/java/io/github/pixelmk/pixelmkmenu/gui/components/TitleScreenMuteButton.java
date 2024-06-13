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

import io.github.pixelmk.pixelmkmenu.PixelMKMenu;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Mute button present on the title screen.
 *
 * <p>Mutes the title screen music, can also be used to skip the currently playing track.
 */
@OnlyIn(Dist.CLIENT)
public class TitleScreenMuteButton extends Button {

  private static boolean muted;
  private static final ResourceLocation SPEAKER =
      ResourceLocation.tryParse(PixelMKMenu.MODID + ":textures/gui/speaker.png");

  /**
   * Create a mute button at the supplied co'ordinates.
   *
   * @param positionX position on the X axis
   * @param positionY position on the Y axis
   */
  public TitleScreenMuteButton(int positionX, int positionY) {
    super(
        positionX,
        positionY,
        20,
        20,
        Component.translatable("Mute"),
        (button) -> {
          ((TitleScreenMuteButton) button).toggleMute();
        },
        Button.DEFAULT_NARRATION);
  }

  /** Toggle the muted state of the button. */
  public void toggleMute() {
    this.setMuted(!muted);
  }

  /**
   * Provided that the music volume is not zero, set the muted state of the button.
   *
   * @param muted should the Title Screen music be muted?
   */
  public void setMuted(boolean muted) {
    if (isMusicEnabled()) {
      TitleScreenMuteButton.muted = muted;
    }
    if (TitleScreenMuteButton.muted) {
      Minecraft.getInstance().getMusicManager().stopPlaying(); // NOPMD - trust get music manager
    } else {
      Minecraft.getInstance() // NOPMD - trust get instance
          .getMusicManager() // NOPMD - trust get music manager
          .startPlaying(
              Minecraft.getInstance() // // NOPMD - trust get instance
                  .getSituationalMusic()); // NOPMD - trust get sdituational music
    }
  }

  public boolean getMuted() {
    return muted;
  }

  /**
   * Checks to see if the volume for music is above 0.
   *
   * @return <code>true</code> if volume is above 0.
   */
  @SuppressWarnings("resource")
  private boolean isMusicEnabled() {
    return Minecraft.getInstance() // NOPMD - trust get instance
            .options // NOPMD - trust options.
            .getSoundSourceVolume(SoundSource.MUSIC)
        > 0.0f;
  }

  /**
   * Render the mute button. This is a simple blit.
   *
   * <p>Because this runs on every tick, the muted property of the mute button is also set here.
   */
  @Override
  public void renderWidget(
      @Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    if (!isMusicEnabled()) {
      muted = true;
    }
    guiGraphics.blit(SPEAKER, this.getX(), this.getY(), muted ? 20 : 0, 0, this.width, this.height);
  }
}
