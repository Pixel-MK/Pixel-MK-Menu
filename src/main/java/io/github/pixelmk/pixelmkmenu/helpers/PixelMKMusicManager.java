package io.github.pixelmk.pixelmkmenu.helpers;

import java.util.Random;

import javax.annotation.Nullable;

import io.github.pixelmk.pixelmkmenu.gui.PixelMKMenuScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.util.Mth;

public class PixelMKMusicManager {

    private static final Minecraft minecraft = Minecraft.getInstance();
    private static final MusicManager musicManager = Minecraft.getInstance().getMusicManager();
    private static final Random random = new Random();
    @Nullable
    private static SoundInstance currentMusic;
    private static int nextSongDelay = 1;

    /**
     * <p>Runs on every tick.</p>
     * <p>If the config is set to not play any Pixel MK Music it get's turned off/skipped.</p>
     * <p>Otherwise check if any minecraft music is playing, cancel it and replace it with Pixel MK Music</p>
     */
    public static void tick() {
        if (!PixelMKMenuConfig.CLIENT.playPixelMKMenuMusic.get())
            return;
        Music minecraftMusic = minecraft.getSituationalMusic();
        Music PixelMKMusic = checkForReplacements(minecraftMusic);
        if (currentMusic != null) {
            boolean shouldStopVanilla = true;
            if (PixelMKMusic != null) {
                if (!PixelMKMusic.getEvent().getLocation().equals(currentMusic.getLocation())
                        && PixelMKMusic.replaceCurrentMusic()) {
                    minecraft.getSoundManager().stop(currentMusic);
                    nextSongDelay = Mth.nextInt(random, 0, PixelMKMusic.getMinDelay() / 2);
                }
                if (!minecraft.getSoundManager().isActive(currentMusic)) {
                    currentMusic = null;
                    nextSongDelay = Math.min(nextSongDelay,
                            Mth.nextInt(random, PixelMKMusic.getMinDelay(), PixelMKMusic.getMaxDelay()));
                    shouldStopVanilla = false;
                }
            } else {
                if (!minecraftMusic.getEvent().getLocation().equals(currentMusic.getLocation())
                        && minecraftMusic.replaceCurrentMusic()) {
                    minecraft.getSoundManager().stop(currentMusic);
                    nextSongDelay = Mth.nextInt(random, 0, minecraftMusic.getMinDelay() / 2);
                    shouldStopVanilla = false;
                }
                if (!minecraft.getSoundManager().isActive(currentMusic)) {
                    currentMusic = null;
                    nextSongDelay = Math.min(nextSongDelay,
                            Mth.nextInt(random, minecraftMusic.getMinDelay(), minecraftMusic.getMaxDelay()));
                    shouldStopVanilla = false;
                }
            }
            if (shouldStopVanilla) {
                // Stops vanilla music from playing.
                stopVanillaMusic();
            }
        }
        nextSongDelay--;
        if (PixelMKMusic != null) {
            nextSongDelay = Math.min(nextSongDelay, PixelMKMusic.getMaxDelay());
            boolean vanillaPlaying = musicManager.isPlayingMusic(minecraftMusic);
            if (vanillaPlaying) {
                stopVanillaMusic();
            }
            if (PixelMKMenuScreen.btnMute.getMute() || !PixelMKMenuConfig.CLIENT.playMenuMusic.get()) {
                stopPlaying();
            }
            if (currentMusic == null) {
                if (vanillaPlaying) {
                    nextSongDelay = 0;
                }
                if (nextSongDelay <= 0 && !PixelMKMenuScreen.btnMute.getMute()) {
                    startPlaying(PixelMKMusic);
                }
            }
        }
    }

    /**
     * Stop playing any/all vanilla music
     */
    private static void stopVanillaMusic() {
        musicManager.stopPlaying();
    }

    /**
     * Stop playing all sounds
     */
    private static void stopPlaying() {
        minecraft.getSoundManager().stop();
    }

    /**
     * Start playing supplied music.
     *
     * @param music The music to play
     */
    private static void startPlaying(Music music) {
        currentMusic = SimpleSoundInstance.forMusic(music.getEvent());
        if (currentMusic.getSound() != SoundManager.EMPTY_SOUND) {
            minecraft.getSoundManager().play(currentMusic);
        }

        nextSongDelay = Integer.MAX_VALUE;
    }

    /**
     * Check to see if there is a suitable replacement/equivalent to the vanilla music
     * @param minecraftMusic The music to compare against
     * @return <code>Music</code> the music that is equivalent | <code>null</code>
     */
    private static Music checkForReplacements(Music minecraftMusic) {
        if ((minecraftMusic == Musics.MENU))
            return PixelMKMenuScreen.MENU_MUSIC;
        return null;
    }

}
