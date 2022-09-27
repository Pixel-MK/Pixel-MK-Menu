package com.pixelmk.pixelmkmenu.controls;

import com.pixelmk.pixelmkmenu.gui.PixelMKMenuScreen;
import com.pixelmk.pixelmkmenu.gui.dialogboxes.DialogBoxFavouriteServer;
import com.pixelmk.pixelmkmenu.helpers.PixelMKMenuConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

public class ConnectToButton extends GuiButtonMainMenu {

    private PixelMKMenuScreen parentScreen;

    public ConnectToButton(String langKey, PixelMKMenuScreen parentScreen) {
        super(langKey, (button) -> {
        });
        this.parentScreen = parentScreen;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.active && this.visible) {
            if (pButton == 1) {
                boolean flag = this.clicked(pMouseX, pMouseY);
                if (flag) {
                    this.playDownSound(Minecraft.getInstance().getSoundManager());
                    Minecraft.getInstance()
                            .setScreen(new DialogBoxFavouriteServer(parentScreen,
                                    PixelMKMenuConfig.CLIENT.customServerName.get(),
                                    PixelMKMenuConfig.CLIENT.customServerIP.get()));
                    return true;
                }
            } else if (pButton == 0) {
                boolean flag = this.clicked(pMouseX, pMouseY);
                if (flag) {
                    this.playDownSound(Minecraft.getInstance().getSoundManager());
                    ServerData data = new ServerData(PixelMKMenuConfig.CLIENT.customServerName.get(),
						PixelMKMenuConfig.CLIENT.customServerIP.get(), false);
				    ConnectScreen.startConnecting(parentScreen, Minecraft.getInstance(), ServerAddress.parseString(data.ip), data);
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

}
