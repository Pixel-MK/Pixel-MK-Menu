package com.pixelmk.pixelmkmenu.gui;

import com.pixelmk.pixelmkmenu.PixelMKMenu;
import com.pixelmk.pixelmkmenu.gui.dialogboxes.DialogBoxFavouriteServer;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList.OnlineServerEntry;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.TranslatableComponent;

public class SelectServerScreen extends JoinMultiplayerScreen {
    private DialogBoxFavouriteServer parentScreen;

    public SelectServerScreen(DialogBoxFavouriteServer parentScreen) {
        super((Screen) parentScreen);
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        super.init();
        this.renderables.clear();
        this.children().clear();
        this.addRenderableWidget(new Button(this.width / 2 - 104, this.height - 52, 100, 20,
                new TranslatableComponent("Use this server"), (method) -> {
                    try {
                        ServerSelectionList.OnlineServerEntry selectedServer = (OnlineServerEntry) this.serverSelectionList
                                .getSelected();
                        ServerData data = selectedServer.getServerData();
                        PixelMKMenu.LOGGER.debug(data.name + ", " + data.ip);
                        this.parentScreen.setCustomServerIP(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.minecraft.setScreen(this.parentScreen);
                }));
        this.addRenderableWidget(new Button(this.width / 2 + 4, this.height - 52, 100, 20, new TranslatableComponent("Cancel"), (method) -> {
            this.minecraft.setScreen(this.parentScreen);
        }));
        this.addWidget(this.serverSelectionList);
    }

}
