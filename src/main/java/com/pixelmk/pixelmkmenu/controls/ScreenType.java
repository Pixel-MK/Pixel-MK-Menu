package com.pixelmk.pixelmkmenu.controls;

import java.util.function.Function;

import com.pixelmk.pixelmkmenu.gui.dialogboxes.DialogBoxFavouriteServer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraftforge.client.gui.ModListScreen;

public enum ScreenType implements Function<Screen, Screen> {
    SINGLEPLAYER(SelectWorldScreen::new),
    MULTIPLAYER(JoinMultiplayerScreen::new),
    MODS(ModListScreen::new),
    LANGUAGE(
            m -> new LanguageSelectScreen(m, Minecraft.getInstance().options,
                    Minecraft.getInstance().getLanguageManager())),
    OPTIONS(m -> new OptionsScreen(m, Minecraft.getInstance().options)),
    ACCESSIBILITY(m -> new AccessibilityOptionsScreen(m, Minecraft.getInstance().options));

    protected Function<Screen, Screen> supplier;

    private ScreenType(Function<Screen, Screen> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Screen apply(Screen t) {
        return this.supplier.apply(t);
    }

}