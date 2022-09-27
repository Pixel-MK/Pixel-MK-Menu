package com.pixelmk.pixelmkmenu;

import java.io.File;
import java.io.FileReader;

import com.google.gson.stream.JsonReader;
import com.pixelmk.pixelmkmenu.gui.PixelMKMenuScreen;
import com.pixelmk.pixelmkmenu.gui.PixelMKPauseScreen;
import com.pixelmk.pixelmkmenu.helpers.ButtonManager;
import com.pixelmk.pixelmkmenu.helpers.PixelMKMenuConfig;
import com.pixelmk.pixelmkmenu.helpers.PixelMKMenuSoundEvents;

import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

public class PixelMKMenuClient {

    public static Boolean inModpack;
    private String ModpackName;
    private String ModpackVer;

    public static PixelMKMenuClient instance;

    public static final ButtonManager BUTTON_MANAGER = new ButtonManager();

    public void load() {
        instance = this;
        MinecraftForge.EVENT_BUS.addListener(this::hijackMenu);
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        PixelMKMenuSoundEvents.SOUNDS.register(modEventBus);
        checkIfInModpack();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PixelMKMenuConfig.CLIENT_SPEC);
    }

    @SubscribeEvent
    public void hijackMenu(ScreenOpenEvent e) {
        if (e.getScreen() != null && e.getScreen().getClass() == TitleScreen.class) {
            e.setScreen(new PixelMKMenuScreen());
        } else if (e.getScreen() != null && e.getScreen().getClass() == PauseScreen.class) {
            e.setScreen(new PixelMKPauseScreen(true));
        }
    }

    public String getModpackName() {
        return ModpackName;
    }

    public String getModpackVersion() {
        return ModpackVer;
    }

    public void checkIfInModpack() {
        File modpackJSON = FMLPaths.GAMEDIR.get().resolve("modpack.json").toFile();
        if (modpackJSON.exists() && modpackJSON.canRead()) {
            inModpack = true;
            try {
                JsonReader jsonReader = new JsonReader(new FileReader(modpackJSON));
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String next = jsonReader.nextName();
                    if (next.equals("Name")) {
                        this.ModpackName = jsonReader.nextString();
                    } else if (next.equals("Version")) {
                        this.ModpackVer = jsonReader.nextString();
                    } else {
                        PixelMKMenu.LOGGER.warn("Unknown modpack arg: " + jsonReader.nextString() + ". SKIPPED");
                    }
                }
                jsonReader.endObject();
                return;
            } catch (Exception e) {
            }
        }
        PixelMKMenu.LOGGER.info("No modpack file detected, assuming not in modpack.");
        inModpack = false;

    }
}
