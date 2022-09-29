package com.pixelmk.pixelmkmenu.helpers;

import com.pixelmk.pixelmkmenu.PixelMKMenu;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PixelMKMenuSoundEvents {

  public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
      PixelMKMenu.MODID);

  public static final RegistryObject<SoundEvent> MUSIC_MENU = register("music_pixelmkmenu");

  private static RegistryObject<SoundEvent> register(String name) {
    return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(PixelMKMenu.MODID, name)));
  }

}
