package com.pixelmkmenu.pixelmkmenu.gui.dialogs;

import com.pixelmkmenu.pixelmkmenu.gui.GuiPixelMKMainMenu;
import com.pixelmkmenu.pixelmkmenu.gui.GuiSelectServerFrom;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class GuiDialogBoxFavouriteServer extends GuiDialogBox {
	
	private GuiPixelMKMainMenu mainMenu;
	
	private String serverName;
	private String serverIP;
	
	private GuiTextField txtServerName;
	private GuiTextField txtServerIP;
	private GuiButton btnPick;
	
	public GuiDialogBoxFavouriteServer(GuiPixelMKMainMenu parentScreen, String serverName, String serverIP) {
		super((GuiScreen)parentScreen, 300, 110, I18n.format("dialog.favourite.title", new Object[0]));
		this.serverName = serverName;
		this.serverIP = serverIP;
		this.mainMenu = parentScreen;
	}

	public void setCustomServerIP(String serverName, String serverIP) {
		this.serverName = serverName;
		this.serverIP = serverIP;
		this.txtServerName.setText(serverName);
		this.txtServerIP.setText(serverIP);
	}
	
	protected void onInitDialog() {
		super.onInitDialog();
		this.btnPick = new GuiButton(1, this.dialogX +2, this.dialogY + this.dialogHeight -22, 60, 20, I18n.format("dialog.favourite.pick", new Object[0]));
		this.buttonList.add(this.btnPick);
		this.txtServerName = new GuiTextField(1, this.mc.fontRenderer, this.dialogX + 100, this.dialogY + 36, this.dialogWidth - 100, 16);
		this.txtServerName.setMaxStringLength(32);
		this.txtServerName.setFocused(true);
		if (this.serverName != null) this.txtServerName.setText(this.serverName);
		this.txtServerIP = new GuiTextField(2, this.mc.fontRenderer, this.dialogX + 100, this.dialogY + 56, this.dialogWidth - 100, 16);
		this.txtServerIP.setMaxStringLength(128);
		if (this.serverIP != null) this.txtServerIP.setText(this.serverIP);
	}
	
	public void onSubmit() {
		this.mainMenu.setCustomServerIP(this.txtServerName.getText(), this.txtServerIP.getText());
	}
	
	public boolean validateDialog() {
		return true;
	}
	
	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		if (guibutton.id == this.btnPick.id) this.mc.displayGuiScreen((GuiScreen)new GuiSelectServerFrom(this));
	}
	
	public void handleKeyboardInput() {
		super.handleKeyboardInput();
		this.txtServerName.updateCursorCounter();
		this.txtServerIP.updateCursorCounter();
	}
	
	protected void mouseClickedEx(int mouseX, int mouseY, int button) {
		super.mouseClickedEx(mouseX, mouseY, button);
		this.txtServerName.mouseClicked(mouseX, mouseY, button);
		this.txtServerIP.mouseClicked(mouseX, mouseY, button);
	}
	
	protected void onKeyTyped(char keyChar, int keyCode) {
		this.txtServerName.textboxKeyTyped(keyChar, keyCode);
		this.txtServerIP.textboxKeyTyped(keyChar, keyCode);
		if(keyCode == 15) {
			boolean flip = this.txtServerName.isFocused();
			this.txtServerIP.setFocused(flip);
			this.txtServerName.setFocused(!flip);
		}
	}
	
	protected void drawDialog(int mouseX, int mouseY, float f) {
		drawString(this.mc.fontRenderer, I18n.format("dialog.favourite.prompt", new Object[0]), this.dialogX + 10, this.dialogY + 16, -22016);
		drawString(this.mc.fontRenderer, I18n.format("dialog.favourite.servername", new Object[0]), this.dialogX + 10, this.dialogY + 40, -256);
		drawString(this.mc.fontRenderer, I18n.format("dialog.favourite.serveraddress", new Object[0]), this.dialogX + 10, this.dialogY + 60, - 256);
		this.txtServerName.drawTextBox();
		this.txtServerIP.drawTextBox();
	}
}
