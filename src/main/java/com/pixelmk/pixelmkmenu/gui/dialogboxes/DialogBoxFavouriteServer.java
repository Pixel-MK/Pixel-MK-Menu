package com.pixelmk.pixelmkmenu.gui.dialogboxes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pixelmk.pixelmkmenu.gui.PixelMKMenuScreen;
import com.pixelmk.pixelmkmenu.gui.SelectServerScreen;
import com.pixelmk.pixelmkmenu.helpers.PixelMKMenuConfig;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.TranslatableComponent;

public class DialogBoxFavouriteServer extends PixelMKMenuDialogBox {

    private Button btnPick;

    /**
     * Data for the server currently chosen/being entered
     */
    private ServerData editingServer;

    /**
     * Box for entering the name of the server
     */
    private EditBox nameEdit;

    /**
     * Box for entering ip of the server
     */
    private EditBox ipEdit;

    public DialogBoxFavouriteServer(PixelMKMenuScreen parentScreen, String serverName, String serverIP) {
        super((Screen) parentScreen, 300, 110, "dialog.favourite.title");
        this.editingServer = new ServerData(PixelMKMenuConfig.CLIENT.customServerName.get(),
                PixelMKMenuConfig.CLIENT.customServerIP.get(), false);
    }

    /**
     * Method to call tick methods on renderables.
     */
    @Override
    public void tick() {
        this.nameEdit.tick();
        this.ipEdit.tick();
    }

    /**
     * Creates the necessary buttons and widgets for use in the dialog box. Also
     * configures the <code>EditBox</code>es.
     */
    @Override
    protected void onInitDialog() {
        super.onInitDialog();
        this.btnPick = new Button(this.dialogX + 2, this.dialogY + this.dialogHeight - 22, 60, 20,
                new TranslatableComponent("dialog.favourite.pick"), (open_gui) -> {
                    this.minecraft.setScreen(new SelectServerScreen(this));
                });
        this.nameEdit = new EditBox(this.minecraft.font, this.dialogX + 100, this.dialogY + 36, this.dialogWidth - 100,
                16, new TranslatableComponent("addServer.enterName"));
        this.nameEdit.setMaxLength(32);
        this.nameEdit.setFocus(true);
        this.nameEdit.setValue(this.editingServer.name);
        this.nameEdit.setResponder((p_169304_) -> {
            this.updateAddButtonStatus();
        });
        this.ipEdit = new EditBox(this.minecraft.font, this.dialogX + 100, this.dialogY + 56, this.dialogWidth - 100,
                16, new TranslatableComponent("addServer.enterIp"));
        this.ipEdit.setMaxLength(128);
        this.ipEdit.setValue(this.editingServer.ip);
        this.ipEdit.setResponder((p_169302_) -> {
            this.updateAddButtonStatus();
        });
        this.addRenderableWidget(this.btnPick);
        this.addRenderableWidget(this.nameEdit);
        this.addRenderableWidget(this.ipEdit);
        this.updateAddButtonStatus();
    }

    /**
     * Function to be used on the submit button as it's <code>onClick</code> method.
     */
    @Override
    protected void onSubmit() {
        this.setCustomServerIP(this.editingServer);
    }

    /**
     * Draws the required strings and renders the <code>nameEdit</code> and
     * <code>ipEdit</code> widgets
     */
    @Override
    protected void drawDialog(PoseStack pose, int mouseX, int mouseY, float partialTicks) {
        drawString(pose, this.minecraft.font, new TranslatableComponent("dialog.favourite.prompt"), this.dialogX + 10,
                this.dialogY + 16, -22016);
        drawString(pose, this.minecraft.font, new TranslatableComponent("dialog.favourite.servername"),
                this.dialogX + 10, this.dialogY + 40, -256);
        drawString(pose, this.minecraft.font, new TranslatableComponent("dialog.favourite.serveraddress"),
                this.dialogX + 10, this.dialogY + 60, -256);
        this.nameEdit.render(pose, mouseX, mouseY, partialTicks);
        this.ipEdit.render(pose, mouseX, mouseY, partialTicks);
    }

    /**
     * Sets the information in the config and updates the <code>editingServer</code>
     * variable
     *
     * @param serverData The data to take in.
     */
    public void setCustomServerIP(ServerData serverData) {
        PixelMKMenuConfig.CLIENT.customServerIP.set(serverData.ip);
        PixelMKMenuConfig.CLIENT.customServerName.set(serverData.name);
        this.editingServer = serverData;
    }

    /**
     * Checks to see if the ip is valid and there
     * is some information in the name value to activate the add button.
     */
    private void updateAddButtonStatus() {
        this.btnDone.active = ServerAddress.isValidAddress(this.ipEdit.getValue())
                && !this.nameEdit.getValue().isEmpty();
    }

}
