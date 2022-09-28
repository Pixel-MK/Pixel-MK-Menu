package com.pixelmk.pixelmkmenu.compat.create;

import com.pixelmk.pixelmkmenu.controls.GuiButtonMainMenu;
import com.simibubi.create.foundation.config.ui.OpenCreateMenuButton;
import com.simibubi.create.foundation.gui.CreateMainMenuScreen;

import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class Create {

    public static GuiButtonMainMenu CreateMenuButton() {
        return new GuiButtonMainMenu("Create config", OpenCreateMenuButton::click);
    }

    @SubscribeEvent
    public static void hijackMenu(ScreenOpenEvent e) {
        if (e.getScreen() != null && e.getScreen().getClass() == CreateMainMenuScreen.class) {
            e.setScreen(new PixelMKMenuCreateMenuScreen((ObfuscationReflectionHelper
                    .getPrivateValue(CreateMainMenuScreen.class, (CreateMainMenuScreen) e.getScreen(), "parent"))));
        }
    }
}
