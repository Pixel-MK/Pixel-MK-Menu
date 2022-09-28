package com.pixelmk.pixelmkmenu.compat;

import com.pixelmk.pixelmkmenu.controls.GuiButtonMainMenu;

import vazkii.quark.base.client.config.screen.widgets.QButton;

/**
 * Class to add Quark <code>QButton</code> to main menu
 */
public class Quark {

    public static GuiButtonMainMenu CreateMenuButton(){
        return new GuiButtonMainMenu("Quark", QButton::click);
    }

}
