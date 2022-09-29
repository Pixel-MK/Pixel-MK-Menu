package io.github.pixelmk.pixelmkmenu.controls;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import io.github.pixelmk.pixelmkmenu.gui.CustomScreenEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;

public class ButtonPanel extends Button {
	public enum AnchorType {
		TopLeft, BottomLeft, TopRight, BottomRight;
	}

	protected AnchorType anchorType = AnchorType.BottomLeft;
	protected List<GuiButtonMainMenu> buttons = new ArrayList<GuiButtonMainMenu>();
	protected GuiButtonMainMenu pressedButton;

	protected int xOffset;
	protected int yOffset;
	protected int buttonSpacing = 16;
	protected String tag;

	/**
	 * Constructs the button panel.
	 *
	 * @param anchorType
	 * @param xOffset
	 * @param yOffset
	 * @param width
	 * @param height
	 * @param buttonSpacing
	 * @param containerWidth
	 * @param containerHeight
	 * @param tag
	 * @param onPress
	 */
	public ButtonPanel(AnchorType anchorType, int xOffset, int yOffset,
			int width, int height, int buttonSpacing, int containerWidth, int containerHeight, String tag,
			ActionInstance onPress) {
		super(0, 0, width, height, new TranslatableComponent(tag), onPress);
		this.anchorType = anchorType;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.buttonSpacing = buttonSpacing;
		this.tag = tag;
		updatePosition(containerWidth, containerHeight);
	}

	/**
	 * Adds a button to the panel using <code>displayText</code> and <code>ActionInstance</code>
	 *
	 * @param displayText
	 * @param onPress
	 * @return
	 */
	public GuiButtonMainMenu addButton(String displayText, ActionInstance onPress) {
		GuiButtonMainMenu button = new GuiButtonMainMenu(displayText, onPress);
		this.buttons.add(button);
		updateButtonPositions();
		return button;
	}

	/**
	 * Adds a button to the panel using <code>String</code> and native <code>OnPress</code>
	 *
	 * @param displayText
	 * @param onPress
	 * @return
	 */
	public GuiButtonMainMenu addButton(String displayText, OnPress onPress) {
		GuiButtonMainMenu button = new GuiButtonMainMenu(displayText, onPress);
		this.buttons.add(button);
		updateButtonPositions();
		return button;
	}

	/**
	 * Adds a button to the panel using <code>CustomScreenEntry</code> and <code>ActionInstance</code>.
	 *
	 * @param customScreenEntry
	 * @param onPress
	 * @return
	 */
	@Deprecated
	public GuiButtonCustomScreen addButton(CustomScreenEntry customScreenEntry, ActionInstance onPress) {
		GuiButtonCustomScreen button = new GuiButtonCustomScreen(customScreenEntry, onPress);
		this.buttons.add(button);
		updateButtonPositions();
		return button;
	}

	/**
	 * Adds a button to the panel using <code>GuiButtonMainMenu</code>
	 *
	 * @param button
	 * @return
	 */
	public GuiButtonMainMenu addButton(GuiButtonMainMenu button) {
		this.buttons.add(button);
		updateButtonPositions();
		return button;
	}

	/**
	 * Removes a particular button from the menu
	 *
	 * @param button
	 */
	public void removeButton(GuiButtonMainMenu button) {
		this.buttons.remove(button);
		updateButtonPositions();
	}

	/**
	 * Updates the position of the panel and then the buttons
	 *
	 * @param containerWidth
	 * @param containerHeight
	 */
	public void updatePosition(int containerWidth, int containerHeight) {
		this.y = (this.anchorType == AnchorType.TopRight || this.anchorType == AnchorType.TopLeft) ? this.yOffset
				: (containerHeight - this.height - this.yOffset);
		this.x = (this.anchorType == AnchorType.TopLeft || this.anchorType == AnchorType.BottomLeft) ? this.xOffset
				: (containerWidth - this.width - this.xOffset);
		updateButtonPositions();
	}

	/**
	 * Updates the position of the buttons
	 */
	private void updateButtonPositions() {
		if (this.anchorType == AnchorType.TopLeft || this.anchorType == AnchorType.BottomLeft) {
			int buttonXPosition = 0;
			for (GuiButtonMainMenu button : this.buttons) {
				button.rightAlign = false;
				button.x = buttonXPosition;
			}
		} else {
			for (GuiButtonMainMenu button : this.buttons) {
				button.rightAlign = true;
				button.x = this.width - button.getWidth();
			}
		}
	}

	/**
	 * Renders the panel and also renders the children of the panel iteratively.
	 */
	@Override
	public void renderButton(PoseStack pose, int mouseX, int mouseY, float partialTicks) {
		mouseX -= this.x;
		mouseY -= this.y;
		int buttonYPosition = 0;
		int visibleButtonCount = 0;
		for (GuiButtonMainMenu button : this.buttons) {
			if (button.visible)
				++visibleButtonCount;
		}
		if (this.anchorType == AnchorType.BottomLeft || this.anchorType == AnchorType.BottomRight)
			buttonYPosition = this.height - this.buttonSpacing * visibleButtonCount;
		pose.pushPose();
		pose.translate(this.x, this.y, 0.0f);
		for (GuiButtonMainMenu button : this.buttons) {
			if (button.visible) {
				button.y = buttonYPosition + button.yOffset;
				buttonYPosition += this.buttonSpacing;
			}
			button.renderButton(pose, mouseX, mouseY, partialTicks);
		}
		pose.popPose();
	}

	/**
	 *
	 * Updates buttons position using <code>GuiButtonMainMenu.updateButton</code>
	 *
	 * @param updateCounter
	 * @param partialTicks
	 * @param mouseX
	 * @param mouseY
	 */
	public void updateButtons(int updateCounter, float partialTicks, int mouseX, int mouseY) {
		mouseX -= this.x;
		mouseY -= this.y;
		for (GuiButtonMainMenu button : this.buttons)
			button.updateButton(updateCounter, partialTicks, mouseX, mouseY);
	}

	/**
	 * Returns the pressed button
	 *
	 * @return
	 */
	public GuiButtonMainMenu getPressedButton() {
		return this.pressedButton;
	}

	/**
	 * Sets the pressed button to the correct button when mouse is clicked
	 */
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
		mouseX -= this.x;
		mouseY -= this.y;
		for (GuiButtonMainMenu button : this.buttons) {
			if (button.mouseClicked(mouseX, mouseY, pButton)) {
				this.pressedButton = button;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int pButton) {
		if (this.pressedButton != null) {
			this.pressedButton.mouseReleased(mouseX, mouseY, pButton);
			this.pressedButton = null;
			return true;
		}
		return false;
	}

	/**
	 * Checks if the mouse is over a particular button
	 *
	 * @param button
	 * @param minecraft
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public boolean isMouseOver(GuiButtonMainMenu button, Minecraft minecraft, int mouseX, int mouseY) {
		if (!this.buttons.contains(button))
			return false;
		mouseX -= this.x;
		mouseY -= this.y;
		return button.mouseClicked(mouseX, mouseY, 0);
	}

	/**
	 * Gets the adjusted x position of a given button
	 *
	 * @param button
	 * @return
	 */
	public int getAdjustedXPosition(GuiButtonMainMenu button) {
		return (!this.buttons.contains(button) ? 0 : button.x) + this.x;
	}

	/**
	 * Gets the adjusted y position of a given button
	 *
	 * @param button
	 * @return
	 */
	public int getAdjustedYPosition(GuiButtonMainMenu button) {
		return (!this.buttons.contains(button) ? 0 : button.y) + this.y;
	}

	/**
	 * Gets the tag of this button panel
	 *
	 * @return
	 */
	public String getTag() {
		return this.tag;
	}

	/**
	 * Checks to see if 2 buttonPanels have the same tag
	 *
	 * @param tag
	 * @return
	 */
	public boolean tagMatches(String tag) {
		return (this.tag != null && this.tag.equalsIgnoreCase(tag));
	}
}
