package com.pixelmk.pixelmkmenu.compat;

import com.pixelmk.pixelmkmenu.controls.GuiButtonMainMenu;

import vazkii.quark.base.client.config.screen.widgets.QButton;

public class Quark {

    public static GuiButtonMainMenu CreateMenuButton(){
        return new GuiButtonMainMenu("Quark", QButton::click);
    }

}
