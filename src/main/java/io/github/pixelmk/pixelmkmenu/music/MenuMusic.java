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

package io.github.pixelmk.pixelmkmenu.music;

import io.github.pixelmk.pixelmkmenu.PixelMKMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Utility class used for creating the Pixel MK Menu music registry and registering the music. */
public class MenuMusic {
  /** Sound event register. */
  public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
      DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, PixelMKMenu.MODID);

  /** Menu Music holder. */
  public static final DeferredHolder<SoundEvent, SoundEvent> MENU_MUSIC =
      SOUND_EVENTS.register(
          "music_pixelmkmenu",
          () ->
              SoundEvent.createVariableRangeEvent(
                  new ResourceLocation(PixelMKMenu.MODID, "music_pixelmkmenu")));
}
