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
        super(langKey, $ -> {
        });
        this.parentScreen = parentScreen;
    }

    /**
     * Opens the <code>DialogBoxFavouriteServer</code>
     *
     * @param wasClicked
     * @return
     */
    private Boolean openDialog(boolean wasClicked) {
        if (!wasClicked)
            return false;
        Minecraft.getInstance()
                .setScreen(new DialogBoxFavouriteServer(parentScreen,
                        PixelMKMenuConfig.CLIENT.customServerName.get(),
                        PixelMKMenuConfig.CLIENT.customServerIP.get()));
        return true;
    }

    /**
     * Connects to the favourite server.
     *
     * @param wasClicked
     * @return
     */
    private Boolean connectToServer(boolean wasClicked) {
        if (!wasClicked)
            return false;
        ServerData data = new ServerData(PixelMKMenuConfig.CLIENT.customServerName.get(),
                PixelMKMenuConfig.CLIENT.customServerIP.get(), false);
        ConnectScreen.startConnecting(parentScreen, Minecraft.getInstance(),
                ServerAddress.parseString(data.ip), data);
        return true;
    }

    /**
     * <p>
     * A method to handle both right and left clicks.
     * </p>
     * <p>
     * If it is left clicked it will try to connect to a server.
     * </p>
     * <p>
     * If it is right clicked it will open the configuration menu to configure the
     * button.
     * </p>
     */
    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (!(this.active && this.visible))
            return false;
        if (pButton == 1) {
            return this.openDialog(this.clicked(pMouseX, pMouseY));
        } else if (pButton == 0) {
            if (ServerAddress.isValidAddress(PixelMKMenuConfig.CLIENT.customServerIP.get()))
                return connectToServer(this.clicked(pMouseX, pMouseY));
            return this.openDialog(this.clicked(pMouseX, pMouseY));
        }
        return false;
    }

}
