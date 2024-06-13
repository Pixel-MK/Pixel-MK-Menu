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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Config for the entire mod.
 *
 * <p>This is currently just the example config from the MDK repo.
 */
@EventBusSubscriber(modid = PixelMKMenu.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class Config {

  /** Default constructor. */
  private Config() {}

  private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

  private static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK =
      BUILDER.comment("Whether to log the dirt block on common setup").define("logDirtBlock", true);

  private static final ModConfigSpec.IntValue MAGIC_NUMBER =
      BUILDER.comment("A magic number").defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

  public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION =
      BUILDER
          .comment("What you want the introduction message to be for the magic number")
          .define("magicNumberIntroduction", "The magic number is... ");

  // a list of strings that are treated as resource locations for items
  private static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS =
      BUILDER
          .comment("A list of items to log on common setup.")
          .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

  static final ModConfigSpec SPEC = BUILDER.build();

  private static boolean logDirtBlock;
  private static int magicNumber;
  private static String magicNumberIntroduction;
  private static Set<Item> items;

  @SubscribeEvent
  /* default */ static void onLoad(final ModConfigEvent event) {
    logDirtBlock = LOG_DIRT_BLOCK.get();
    magicNumber = MAGIC_NUMBER.get();
    magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();

    // convert the list of strings into a set of items
    items =
        ITEM_STRINGS.get().stream()
            .map(itemName -> BuiltInRegistries.ITEM.get(new ResourceLocation(itemName)))
            .collect(Collectors.toSet());
  }

  private static boolean validateItemName(final Object obj) {
    return obj instanceof String itemName
        && BuiltInRegistries.ITEM.containsKey(new ResourceLocation(itemName));
  }
}
