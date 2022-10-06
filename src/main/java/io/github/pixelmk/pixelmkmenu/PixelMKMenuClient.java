package io.github.pixelmk.pixelmkmenu;

import java.io.File;
import java.io.FileReader;

import com.google.gson.stream.JsonReader;

import io.github.pixelmk.pixelmkmenu.compat.PixelMKMenuCompat;
import io.github.pixelmk.pixelmkmenu.compat.create.Create;
import io.github.pixelmk.pixelmkmenu.gui.PixelMKMenuScreen;
import io.github.pixelmk.pixelmkmenu.gui.PixelMKPauseScreen;
import io.github.pixelmk.pixelmkmenu.helpers.ButtonManager;
import io.github.pixelmk.pixelmkmenu.helpers.PixelMKMenuConfig;
import io.github.pixelmk.pixelmkmenu.helpers.PixelMKMenuSoundEvents;
import io.github.pixelmk.pixelmkmenu.helpers.PixelMKMusicManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

public class PixelMKMenuClient {

    /**
     * <p>
     * Are we in a modpack?
     * </p>
     * <p>
     * <code>true</code> = we are in a modpack
     * </p>
     * <p>
     * <code>false</code> = we are not in a modpack
     * </p>
     */
    public static Boolean inModpack;

    /**
     * The name of the modpack
     */
    private String ModpackName;

    /**
     * The version number of the modpack
     */
    private String ModpackVer;

    /**
     * The instance of the mod
     */
    public static PixelMKMenuClient instance;

    /**
     * The button manager
     */
    public static final ButtonManager BUTTON_MANAGER = new ButtonManager();

    /**
     *
     * Entry point for the Client class.
     *
     */
    public void load() {
        instance = this;
        MinecraftForge.EVENT_BUS.addListener(this::hijackMenu);
        MinecraftForge.EVENT_BUS.addListener(this::runMusicManager);
        if (PixelMKMenuCompat.isModLoaded(PixelMKMenuCompat.Mod.CREATE)) {
            MinecraftForge.EVENT_BUS.addListener(Create::hijackMenu);
        }
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        PixelMKMenuSoundEvents.SOUNDS.register(modEventBus);
        checkIfInModpack();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PixelMKMenuConfig.CLIENT_SPEC);
    }

    /**
     *
     * Event subscription that hijacks both the main menu
     * and pause screens to replace them with the new screens.
     *
     * @param e the event
     */
    @SubscribeEvent
    public final void hijackMenu(ScreenOpenEvent e) {
        if (e.getScreen() != null && e.getScreen().getClass() == TitleScreen.class) {
            e.setScreen(new PixelMKMenuScreen());
        } else if (e.getScreen() != null && e.getScreen().getClass() == PauseScreen.class) {
            e.setScreen(new PixelMKPauseScreen(true));
        }
    }

    /**
     *
     * Event to override run the music manager so no vanilla sounds get played unless configured
     *
     * @param e
     */
    @SubscribeEvent
    public final void runMusicManager(ClientTickEvent e) {
        if (Minecraft.getInstance().screen != null && Minecraft.getInstance().screen.getClass() == PixelMKMenuScreen.class)
            PixelMKMusicManager.tick();
    }

    /**
     *
     * Returns name of the modpack if the mod is in one and has a modpack.json
     *
     * @return String | null
     */
    public String getModpackName() {
        return ModpackName;
    }

    /**
     * Returns version of the modpack if the mod is in one and has a modpack.json
     *
     * @return String | null
     */
    public String getModpackVersion() {
        return ModpackVer;
    }

    /**
     * <p>
     * Checks if the mod is in a modpack and has been supplied with a
     * <code>modpack.json</code>.
     * </p>
     *
     * <p>
     * Technically the mod doesn't have to be in a modpack but instead just needs to
     * be
     * supplied with a <code>modpack.json</code> file.
     * </p>
     *
     * <p>
     * Updates the static boolean <code>inModpack</code> to true if we are in a
     * modpack.
     * </p>
     */
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
