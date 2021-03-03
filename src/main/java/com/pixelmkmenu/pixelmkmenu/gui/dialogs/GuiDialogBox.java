package com.pixelmkmenu.pixelmkmenu.gui.dialogs;

import java.awt.Point;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.pixelmkmenu.pixelmkmenu.gui.AdvancedDrawGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;

public abstract class GuiDialogBox extends AdvancedDrawGui{
	public static int lastScreenWidth;
	public static int lastScreenHeight;
	protected int dialogX;
	protected int dialogY;
	protected int dialogWidth;
	protected int dialogHeight;
	protected int dialogTitleColour = -256;
	
	private GuiScreen parentScreen;
	
	protected GuiButton btnOk;
	protected GuiButton btnCancel;
	
	protected String dialogTitle;
	
	protected boolean centreTitle = true;
	protected boolean movable = false;
	protected boolean dragging = false;
	private boolean generateMouseDragEvents;
	
	protected Point dragOffset = new Point(0, 0);
	
	public DialogResult dialogResult = DialogResult.None;
	
	public enum DialogResult {
		None, OK, Cancel, Yes, No;
	}
	
	public GuiDialogBox(GuiScreen parentScreen, int width, int height, String windowTitle) {
		this.parentScreen = parentScreen;
		this.dialogWidth = width;
		this.dialogHeight = height;
		this.dialogTitle = windowTitle;
		this.generateMouseDragEvents = true;
	}
	
	protected void closeDialog() {
		this.mc.displayGuiScreen(getParentScreen());
	}
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.id == this.btnCancel.id) {
			this.dialogResult = DialogResult.Cancel;
			closeDialog();
		}
		if (guibutton.id == this.btnOk.id) {
			if (validateDialog()) {
				this.dialogResult = DialogResult.OK;
				onSubmit();
				closeDialog();
			}
		}
	}
	
	@Override
	protected final void keyTyped(char keyChar, int keyCode) {
		if (keyCode == 1) {
			actionPerformed(this.btnCancel);
		}else if (keyCode == 28) {
			actionPerformed(this.btnOk);
		}else {
			onKeyTyped(keyChar, keyCode);
		}
	}
	
	@Override
	protected final void mouseClicked(int mouseX, int mouseY, int button) {
		if (button == 0 && this.movable && mouseX > this.dialogX && mouseX < this.dialogX + this.dialogWidth &&
				mouseY > this.dialogY - 18 && mouseY < this.dialogY) {
			this.dragOffset = new Point(mouseX - this.dialogX, mouseY - this.dialogY);
			this.dragging = true;
		}else {
			mouseClickedEx(mouseX, mouseY, button);
		}
	}
	
	protected void mouseClickedEx(int mouseX, int mouseY, int button) {
		try {
			super.mouseClicked(mouseX, mouseY, button);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int button) {
		if(this.dragging) {
			this.dialogX = mouseX  - this.dragOffset.x;
			this.dialogY = mouseY - this.dragOffset.y;
			initGui();
		}
		if (button == 0 && this.dragging) {
			if (this.dialogX < 0) this.dialogX = 0;
			if (this.dialogX > this.width) this.dialogX = this.width - this.dialogWidth;
			if (this.dialogY < 9) this.dialogY = 18;
			if (this.dialogY > this.height) this.dialogY = this.height = this.dialogHeight;
			initGui();
			this.dragging = false;
			return;
		}
		super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public final void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		if (getParentScreen() != null) getParentScreen().initGui();
		if (!this.dragging) {
			this.dialogX = (this.width - this.dialogWidth) / 2;
			this.dialogY = (this.height - this.dialogHeight) / 2;
		}
		this.btnOk = new GuiButton(-1, this.dialogX + this.dialogWidth - 62, this.dialogY + this.dialogHeight - 22,
				60, 20, I18n.format("gui.done", new Object[0]));
		this.btnCancel = new GuiButton(-2, this.dialogX + this.dialogWidth - 124, this.dialogY + this.dialogHeight -22,
				60, 20, I18n.format("gui.cancel", new Object[0]));
		this.buttonList.clear();
		this.buttonList.add(this.btnOk);
		this.buttonList.add(this.btnCancel);
		lastScreenWidth = this.width;
		lastScreenHeight = this.height;
		onInitDialog();
	}
	
	public final void onGuiClosed() {
		onDialogClosed();
		Keyboard.enableRepeatEvents(false);
		super.onGuiClosed();
	}
	
	@Override
	public void setWorldAndResolution(Minecraft minecraft, int width, int height) {
		this.mc = minecraft;
        this.fontRenderer = minecraft.fontRenderer;
        this.width = width;
        this.height = height;
        if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Pre(this, this.buttonList)))
        {
            this.buttonList.clear();
            this.initGui();
        }
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post(this, this.buttonList));
		if(getParentScreen() != null) getParentScreen().setWorldAndResolution(minecraft, width, height);
	}
	
	@Override
	public final void drawScreen(int mouseX, int mouseY, float partialTick) {
		drawParentScreen(mouseX, mouseY, partialTick);
		int backColour = -1442840576;
		int backColour2 = -869059789;
		drawRect(this.dialogX, this.dialogY - 18, this.dialogX + this.dialogWidth, this.dialogY, backColour2);
		if(this.centreTitle) {
			drawCenteredString(this.mc.fontRenderer, this.dialogTitle, this.dialogX + this.dialogWidth / 2, this.dialogY - 13, this.dialogTitleColour);
		} else {
			drawString(this.mc.fontRenderer, this.dialogTitle, this.dialogX + 5, this.dialogY - 13, this.dialogTitleColour);
		}
		drawRect(this.dialogX, this.dialogY, this.dialogX + this.dialogWidth, this.dialogY + this.dialogHeight, backColour);
		drawDialog(mouseX, mouseY, partialTick);
		super.drawScreen(mouseX, mouseY, partialTick);
		postRender(mouseX, mouseY, partialTick);
	}
	
	@Override
	public void handleKeyboardInput() {
		try {
			if (getParentScreen() != null) getParentScreen().handleKeyboardInput();
			super.handleKeyboardInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void drawParentScreen(int mouseX, int mouseY, float partialTick) {
		if (getParentScreen() != null) {
			getParentScreen().drawScreen(0, 0, partialTick);
			drawRect(0, 0, this.width, this.height, -1442840576);
		} else if (this.mc.world == null){
			drawDefaultBackground();
		}
	}


	protected void drawDialog(int mouseX, int mouseY, float partialTick) {}

	protected void postRender(int mouseX, int mouseY, float partialTick) {}
	
	public abstract void onSubmit();
	
	public abstract boolean validateDialog();
	
	protected void onKeyTyped(char keyChar, int keyCode) {}
	
	protected void onInitDialog() {}
	
	protected void onDialogClosed() {}
	
	public GuiScreen getParentScreen() {
		return this.parentScreen;
	}
	
}
