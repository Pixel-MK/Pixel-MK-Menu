package io.github.pixelmk.pixelmkmenu.gui.components;

import com.mojang.logging.LogUtils;
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

  private boolean muted;
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
    LogUtils.getLogger().info(SPEAKER.getPath());
  }

  /** Toggle the muted state of the button. */
  public void toggleMute() {
    this.setMuted(!this.muted);
  }

  /**
   * Provided that the music volume is not zero, set the muted state of the button.
   *
   * @param muted should the Title Screen music be muted?
   */
  public void setMuted(boolean muted) {
    if (isMusicEnabled()) {
      this.muted = muted;
    }
  }

  /**
   * Checks to see if the volume for music is above 0.
   *
   * @return <code>true</code> if volume is above 0.
   */
  private boolean isMusicEnabled() {
    return Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.MUSIC) > 0.0f;
  }

  @Override
  public void renderWidget(
      @Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    if (!isMusicEnabled()) {
      this.muted = true;
    }
    guiGraphics.blit(
        SPEAKER, this.getX(), this.getY(), this.muted ? 20 : 0, 0, this.width, this.height);
  }
}
