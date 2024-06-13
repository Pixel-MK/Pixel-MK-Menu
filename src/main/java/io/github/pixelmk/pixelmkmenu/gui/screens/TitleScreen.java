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

import io.github.pixelmk.pixelmkmenu.PixelMKMenuClient;
import io.github.pixelmk.pixelmkmenu.gui.components.ButtonPanel;
import io.github.pixelmk.pixelmkmenu.gui.components.TitleScreenMuteButton;
import io.github.pixelmk.pixelmkmenu.gui.components.Tooltip;
import io.github.pixelmk.pixelmkmenu.music.MenuMusic;
import javax.annotation.Nonnull;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.Music;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.gui.ModListScreen;

/**
 * Pixel MK Menu Title Screen.
 *
 * <p>This is the namesake and main feature of this mod, replacing the title screen.
 */
@OnlyIn(Dist.CLIENT)
public class TitleScreen extends net.minecraft.client.gui.screens.TitleScreen {

  @SuppressWarnings("NullAway")
  private ButtonPanel buttonPanelLeft;

  @SuppressWarnings("NullAway")
  private ButtonPanel buttonPanelRight;

  private int updateCounter;

  /**
   * Replaces brandings.
   *
   * @param unusedFade fade boolean.
   */
  public TitleScreen(boolean unusedFade) {
    this.minecraft = Minecraft.getInstance();
    PixelMKMenuClient.replaceBranding();
  }

  /** Default Constructor. */
  public TitleScreen() {
    this(true);
  }

  @Override
  protected void init() {
    super.init();
    this.renderables.clear();
    this.children().clear();
    this.addDefaultButtons();
    Tooltip brandingsTooltip =
        new Tooltip(
            PixelMKMenuClient.getBrandings(),
            "Minecraft " + SharedConstants.getCurrentVersion().getName(),
            2,
            this.height - 10,
            200,
            0,
            10);
    this.addRenderableWidget(brandingsTooltip);
  }

  /**
   * Render the screen by first updating buttons in the button panel and then calling the
   * supermethod.
   */
  @Override
  public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    this.buttonPanelLeft.updateButtons(this.updateCounter, partialTick, mouseX, mouseY);
    this.buttonPanelRight.updateButtons(this.updateCounter, partialTick, mouseX, mouseY);
    super.render(guiGraphics, mouseX, mouseY, partialTick);
  }

  /** On every tick update the counter, update server info and tick the music manager. */
  @SuppressWarnings("null")
  @Override
  public void tick() {
    super.tick();
    ++this.updateCounter;
    if (this.minecraft.screen != null // NOPMD - trust screen
        && !this.minecraft.screen.equals(this)) { // NOPMD - trust screen
      return;
    }
    // this.updateServerInfo();
    // if (this.serverPinger != null) this.serverPinger.tick();
  }

  @Override
  public Music getBackgroundMusic() {
    return new Music(MenuMusic.MENU_MUSIC.getDelegate(), 20, 600, true);
  }

  /** Adds the button panels, initialises them and adds the mute button. */
  private void addDefaultButtons() {

    if (this.buttonPanelLeft == null) {
      this.buttonPanelLeft =
          new ButtonPanel(
              ButtonPanel.AnchorType.BOTTOM_LEFT,
              12,
              20,
              150,
              100,
              16,
              this.width,
              this.height,
              "left",
              (button) -> {});
      this.buttonPanelRight =
          new ButtonPanel(
              ButtonPanel.AnchorType.BOTTOM_RIGHT,
              12,
              20,
              150,
              100,
              16,
              this.width,
              this.height,
              "right",
              (button) -> {});
      initPanelButtons();
    } else {
      this.buttonPanelLeft.updatePosition(this.width, this.height);
      this.buttonPanelRight.updatePosition(this.width, this.height);
    }
    this.addRenderableWidget(this.buttonPanelLeft);
    this.addRenderableWidget(this.buttonPanelRight);

    TitleScreenMuteButton btnMute = new TitleScreenMuteButton(this.width - 24, 4);
    this.addRenderableWidget(btnMute);
  }

  @SuppressWarnings("null")
  private void initPanelButtons() {
    if (this.minecraft != null && this.minecraft.isDemo()) {
      this.buttonPanelLeft.addButton(
          "menu.playdemo",
          (button) -> {
            this.minecraft
                .createWorldOpenFlows()
                .createFreshLevel(
                    "Demo_World",
                    MinecraftServer.DEMO_SETTINGS,
                    WorldOptions.DEMO_OPTIONS,
                    WorldPresets::createNormalWorldDimensions,
                    this);
          });
    } else {
      this.buttonPanelLeft.addButton(
          "menu.singleplayer",
          (button) -> {
            this.minecraft.setScreen(new SelectWorldScreen(this));
          });
      this.buttonPanelRight.addButton(
          "menu.multiplayer",
          (button) -> {
            Screen screen =
                this.minecraft.options.skipMultiplayerWarning // NOPMD - trust options
                    ? new JoinMultiplayerScreen(this)
                    : new SafetyScreen(this);
            this.minecraft.setScreen((Screen) screen);
          });
      //   String buttonText =
      //       "Connect to "
      //           + ((PixelMKMenuConfig.CLIENT.customServerName.get() != null
      //                   && PixelMKMenuConfig.CLIENT.customServerName.get().length() > 0)
      //               ? PixelMKMenuConfig.CLIENT.customServerName.get()
      //               : "...");
      //   this.buttonPanelLeft.addButton(new ConnectToButton(buttonText, this));
    }
    this.buttonPanelLeft.addButton(
        "menu.options",
        (button) -> {
          this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
        });
    this.buttonPanelLeft.addButton(
        "menu.quit",
        (button) -> {
          this.minecraft.stop();
        });
    // if (PixelMKMenuCompat.isAnyModLoaded()) {
    //    PixelMKMenuCompat.addButtons(this.buttonPanelRight);
    // }
    // NeoForge.EVENT_BUS.post(new AddModButtonsEvent(this.buttonPanelRight));
    this.buttonPanelRight.addButton(
        "fml.menu.mods",
        (button) -> {
          this.minecraft.setScreen(new ModListScreen(this));
        });
    this.buttonPanelRight.addButton(
        "options.language",
        (button) -> {
          this.minecraft.setScreen(
              new LanguageSelectScreen(
                  this, this.minecraft.options, this.minecraft.getLanguageManager()));
        });
  }
}
