package io.github.pixelmk.pixelmkmenu.compat.create;

import com.simibubi.create.foundation.config.ui.OpenCreateMenuButton;
import com.simibubi.create.foundation.gui.CreateMainMenuScreen;

import io.github.pixelmk.pixelmkmenu.controls.GuiButtonMainMenu;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

/**
 * Create compatibility class
 */
public class Create {

    /**
     * Creates the menu button on the main menu.
     *
     * @return
     */
    public static GuiButtonMainMenu CreateMenuButton() {
        return new GuiButtonMainMenu("Create config", OpenCreateMenuButton::click);
    }

    /**
     * Hijacks the <code>CreateMainMenuScreen</code> class to override it with
     * <code>PixelMKMenuCreateMenuScreen</code>
     *
     * @param e
     */
    @SubscribeEvent
    public static void hijackMenu(ScreenOpenEvent e) {
        if (e.getScreen() != null && e.getScreen().getClass() == CreateMainMenuScreen.class) {
            e.setScreen(new PixelMKMenuCreateMenuScreen((ObfuscationReflectionHelper
                    .getPrivateValue(CreateMainMenuScreen.class, (CreateMainMenuScreen) e.getScreen(), "parent"))));
        }
    }
}
