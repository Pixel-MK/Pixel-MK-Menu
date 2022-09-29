package io.github.pixelmk.pixelmkmenu.compat;

import io.github.pixelmk.pixelmkmenu.compat.create.Create;
import io.github.pixelmk.pixelmkmenu.controls.ButtonPanel;
import net.minecraftforge.fml.ModList;

public class PixelMKMenuCompat {

    /**
     * Enum of mods that have built in compatibility
     */
    public static enum Mod {
        CREATE,
        QUARK;
    }

    /**
     * Checks to see if a particular mod in the enum is loaded
     * @param mod
     * @return
     */
    public static Boolean isModLoaded(Mod mod) {
        if (ModList.get().isLoaded(mod.toString().toLowerCase()))
            return true;
        return false;
    }

    /**
     * Checks to see if any mod in the enum is loaded
     * @return
     */
    public static Boolean isAnyModLoaded() {
        for (Mod mod : Mod.values()) {
            if (isModLoaded(mod))
                return true;
        }
        return false;
    }

    /**
     * Adds buttons from these mods into the right button panel
     * @param buttonPanel
     */
    public static void addButtons(ButtonPanel buttonPanel) {
        for (Mod mod : Mod.values()) {
            if (isModLoaded(mod)) {
                if (mod.toString() == "CREATE")
                    buttonPanel.addButton(Create.CreateMenuButton());
                else if (mod.toString() == "QUARK")
                    buttonPanel.addButton(Quark.CreateMenuButton());
            }
        }
    }
}