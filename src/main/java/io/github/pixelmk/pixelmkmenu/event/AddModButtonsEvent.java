package io.github.pixelmk.pixelmkmenu.event;

import io.github.pixelmk.pixelmkmenu.controls.ButtonPanel;
import io.github.pixelmk.pixelmkmenu.controls.GuiButtonMainMenu;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.Event;

public class AddModButtonsEvent extends Event {

    private ButtonPanel buttonPanel;

    public AddModButtonsEvent(ButtonPanel pButtonPanel) {
        this.buttonPanel = pButtonPanel;
    }

    public ButtonPanel getButtonPanel() {
        return this.buttonPanel;
    }

    public GuiButtonMainMenu createButton(String message, OnPress onPress) {
        return this.buttonPanel.addButton(message, onPress);
    }

    public GuiButtonMainMenu createButton(Component message, OnPress onPress) {
        return this.buttonPanel.addButton(new GuiButtonMainMenu(message, onPress));
    }

    public GuiButtonMainMenu addButton(Button pButton) {
        GuiButtonMainMenu guiButtonMainMenu = new GuiButtonMainMenu(pButton.getMessage(), pButton.onPress);
        guiButtonMainMenu.visible = pButton.visible;
        guiButtonMainMenu.active = pButton.active;
        return this.buttonPanel.addButton(guiButtonMainMenu);
    }

    public GuiButtonMainMenu addButton(GuiButtonMainMenu pButton) {
        return this.buttonPanel.addButton(pButton);
    }

}
