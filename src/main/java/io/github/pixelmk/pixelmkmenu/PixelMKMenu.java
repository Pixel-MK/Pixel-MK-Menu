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

package io.github.pixelmk.pixelmkmenu;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
import sounds.music.MenuMusic;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
/** Mod Entrypoint. */
@Mod(PixelMKMenu.MODID)
public class PixelMKMenu {

  /** Client Event class. */
  @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
  public static class ClientModEvents {

    /** Default Constructor. */
    ClientModEvents() {}

    /**
     * Client set-up super function.
     *
     * @param event event that fires upon client setup.
     */
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
      // Some client setup code
      PixelMKMenuClient.getInstance().load();
    }
  }

  /** ModID for the mod. */
  public static final String MODID = "pixelmkmenu";

  /** Shared logger for the mod. */
  private static final Logger LOGGER = LogUtils.getLogger();

  /**
   * Mod Constructor, loaded by NeoForged, first thing that executes when mod is loaded.
   *
   * @param modEventBus event bus instance for the mod
   * @param modContainer mod container
   */
  public PixelMKMenu(IEventBus modEventBus, ModContainer modContainer) {
    // Do not add this line if there are no @SubscribeEvent-annotated functions in
    // this class, like onServerStarting() below.
    NeoForge.EVENT_BUS.register(this);
    MenuMusic.SOUND_EVENTS.register(modEventBus);

    // Register our mod's ModConfigSpec so that FML can create and load the config
    // file for us
    // modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
  }

  /**
   * Event handler for non-client. Gives warning about mod presence.
   *
   * @param event ServerStartingEvent
   */
  @SubscribeEvent
  public void onServerStarting(ServerStartingEvent event) {
    // Do something when the server starts
    LOGGER.info("This mod is not used on server, please remove this mod.");
  }
}
