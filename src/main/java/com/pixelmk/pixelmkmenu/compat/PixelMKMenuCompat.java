package com.pixelmk.pixelmkmenu.compat;

import com.pixelmk.pixelmkmenu.compat.create.Create;
import com.pixelmk.pixelmkmenu.controls.ButtonPanel;

import net.minecraftforge.fml.ModList;

public class PixelMKMenuCompat {

    public static enum Mod {
        CREATE,
        QUARK;
    }

    public static Boolean isModLoaded(Mod mod) {
        if (ModList.get().isLoaded(mod.toString().toLowerCase()))
            return true;
        return false;
    }

    public static Boolean isAnyModLoaded() {
        for (Mod mod : Mod.values()) {
            if (isModLoaded(mod))
                return true;
        }
        return false;
    }

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