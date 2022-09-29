package io.github.pixelmk.pixelmkmenu.compat;

import io.github.pixelmk.pixelmkmenu.controls.GuiButtonMainMenu;
import vazkii.quark.base.client.config.screen.widgets.QButton;

/**
 * Class to add Quark <code>QButton</code> to main menu
 */
public class Quark {

    public static GuiButtonMainMenu CreateMenuButton(){
        return new GuiButtonMainMenu("Quark", QButton::click);
    }

}
