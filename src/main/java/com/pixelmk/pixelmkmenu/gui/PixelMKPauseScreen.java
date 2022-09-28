package com.pixelmk.pixelmkmenu.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pixelmk.pixelmkmenu.controls.ActionInstance;
import com.pixelmk.pixelmkmenu.controls.ButtonAction;
import com.pixelmk.pixelmkmenu.controls.ButtonPanel;
import com.pixelmk.pixelmkmenu.controls.GuiButtonMainMenu;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.TranslatableComponent;

public class PixelMKPauseScreen extends PauseScreen {

    private ButtonPanel buttonPanel;
    private int updateCounter;
    private boolean begunTweening;
    private float tweenTime;

    public PixelMKPauseScreen(boolean showPauseMenu) {
        super(showPauseMenu);
    }

    /**
     * initialise the screeen and convert all buttons to the main menu buttons for the pause screen.
     */
    @Override
	protected void init() {
		super.init();
		this.buttonPanel = new ButtonPanel(ButtonPanel.AnchorType.BottomLeft, 22, 20, 150, 100, 16, this.width, this.height, "main", new ActionInstance(ButtonAction.NONE, null));
        for (Widget widget : this.renderables) {
            Button button = (Button)widget;
                OnPress onPress = button.onPress;
                GuiButtonMainMenu guiButtonMainMenu = this.buttonPanel.addButton(button.getMessage().getString(), onPress);
                guiButtonMainMenu.visible = button.visible;
                guiButtonMainMenu.active = button.active;
        }
		this.renderables.clear();
        this.children().clear();
        this.addRenderableWidget(this.buttonPanel);
	}

    /**
     * Renders the screen using tweening and the pose stack.
     */
    @Override
	public void render(PoseStack pose, int mouseX, int mouseY, float partialTicks) {
		if(!this.begunTweening) {
			this.tweenTime = -partialTicks;
			this.begunTweening = true;
		}
		float tweenPct = Math.min(0.5f, (this.updateCounter + partialTicks - this.tweenTime) / 20.0f);
		float tweenAmount = (float)Math.sin(tweenPct * Math.PI);
		int colour = (int)(192.0f * tweenAmount) << 24;
        pose.pushPose();
        pose.translate(-10.0F + tweenAmount * 20.0F, 0.0F, 0.0F);
        fillGradient(pose, 10, 0, 184, this.height, colour, colour);
        fill(pose, 10, 0, 11, this.height, -1);
        pose.translate(-10.0F + tweenAmount * 10.0F, 0.0F, 0.0F);
        pose.pushPose();
        pose.translate(27.0F, 40.0F, 0.0F);
        pose.scale(2.0f, 2.0f, 1.0f);
        drawString(pose, this.font, new TranslatableComponent("menu.game"), 0, 0, 16777215);
		pose.popPose();
		this.buttonPanel.updateButtons(this.updateCounter, partialTicks, mouseX, mouseY);
		this.buttonPanel.renderButton(pose, mouseX, mouseY, partialTicks);
		pose.popPose();
	}

    /**
     * On every tick increment the <code>updateCounter</code>
     */
    @Override
    public void tick(){
		super.tick();
		this.updateCounter++;
	}

}
