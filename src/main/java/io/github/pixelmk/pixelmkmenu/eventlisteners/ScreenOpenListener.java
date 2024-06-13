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

package io.github.pixelmk.pixelmkmenu.eventlisteners;

import io.github.pixelmk.pixelmkmenu.PixelMKMenu;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

/** Listener for screen open events to hijack the title screen and pause screen. */
@EventBusSubscriber(modid = PixelMKMenu.MODID)
@OnlyIn(Dist.CLIENT)
public class ScreenOpenListener {

  /** Default Constructor. */
  ScreenOpenListener() {}

  /**
   * Hijack the title screen when title screen is opened.
   *
   * <p>This is done by checking that the ScreenEvent.Opening newScreen is an instance of the
   * TitleScreen object.
   *
   * @param event screen open event.
   */
  @SubscribeEvent
  public static void onTitleScreenOpen(ScreenEvent.Opening event) {
    if (event.getNewScreen() instanceof TitleScreen) {
      event.setNewScreen(new io.github.pixelmk.pixelmkmenu.gui.screens.TitleScreen());
    }
  }

  /**
   * Hijack the Pause Screen when the Pause Screen is opened.
   *
   * <p>This is done by checking the ScreenEvent.Opening newScreen is an instance of the PauseScreen
   * object.
   *
   * @param event screen open event.
   */
  @SubscribeEvent
  public static void onPauseScreenOpen(ScreenEvent.Opening event) {
    if (event.getNewScreen() instanceof PauseScreen) {
      event.setNewScreen(new io.github.pixelmk.pixelmkmenu.gui.screens.PauseScreen());
    }
  }
}
