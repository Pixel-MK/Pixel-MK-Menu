package io.github.pixelmk.pixelmkmenu.gui.dialogboxes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.mojang.blaze3d.vertex.PoseStack;

import cpw.mods.modlauncher.api.INameMappingService;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class PixelMKMenuDialogBox extends Screen {

    private Screen parentScreen;
    protected int dialogHeight;
    protected int dialogWidth;
    protected int dialogX;
    protected int dialogY;
    protected Button btnDone;
	protected boolean centreTitle = true;
	protected boolean movable = false;
	protected boolean dragging = false;
	protected int dialogTitleColour = -256;

    public PixelMKMenuDialogBox(Screen parentScreen, int width, int height, String windowTitle) {
        super(new TranslatableComponent(windowTitle));
        this.parentScreen = parentScreen;
        this.dialogWidth = width;
        this.dialogHeight = height;
    }

    protected void closeDialog() {
        this.minecraft.setScreen(this.parentScreen);
    }

    public Screen getParentScreen() {
        return this.parentScreen;
    }

    /**
     * <p>Initialises the dialog box and calls init on the parent screen.</p>
     * <p>Clears the renderables and children.</p>
     * <p>Creates necessary buttons</p>
     */
    @Override
    protected void init() {
        super.init();
        if (this.parentScreen != null) {
            Method mInit = ObfuscationReflectionHelper.findMethod(Screen.class, ObfuscationReflectionHelper.remapName(INameMappingService.Domain.METHOD, "m_7856_"));
            mInit.setAccessible(true);
            try {
                mInit.invoke(getParentScreen());
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        if (!this.isDragging()) {
            this.dialogX = (this.width - this.dialogWidth) / 2;
            this.dialogY = (this.height - this.dialogHeight) / 2;
        }
        this.renderables.clear();
        this.children().clear();
        this.btnDone = new Button(this.dialogX + this.dialogWidth - 62, this.dialogY + this.dialogHeight - 22,
                60, 20, new TranslatableComponent("gui.done"),
                (string) -> {this.onSubmit(); this.minecraft.setScreen(this.getParentScreen());});
        this.addRenderableWidget(this.btnDone);
        this.addRenderableWidget(
                new Button(this.dialogX + this.dialogWidth - 124, this.dialogY + this.dialogHeight - 22,
                        60, 20, new TranslatableComponent("gui.cancel"),
                        (s) -> {this.minecraft.setScreen(this.getParentScreen());}));
        onInitDialog();
    }

    /**
     * Not implemented int this class, implemented in child classes
     */
    protected void onSubmit() {}

    /**
     * Renders the dialogbox
     */
    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float partialTick) {
        drawParentScreen(pose, 0, 0, partialTick);
        int backColour = -1442840576;
		int backColour2 = -869059789;
        fill(pose, this.dialogX, this.dialogY - 18, this.dialogX + this.dialogWidth, this.dialogY, backColour2);
        if(this.centreTitle) {
			drawCenteredString(pose, this.minecraft.font, this.title, this.dialogX + this.dialogWidth / 2, this.dialogY - 13, this.dialogTitleColour);
		} else {
			drawString(pose, this.minecraft.font, this.title, this.dialogX + 5, this.dialogY - 13, this.dialogTitleColour);
		}
        fill(pose, this.dialogX, this.dialogY, this.dialogX + this.dialogWidth, this.dialogY + this.dialogHeight, backColour);
        drawDialog(pose, mouseX, mouseY, partialTick);
        super.render(pose, mouseX, mouseY, partialTick);
    }

    /**
     * Draws the parent screen beneath the dialog box
     *
     * @param pose
     * @param mouseX
     * @param mouseY
     * @param partialTick
     */
    public void drawParentScreen(PoseStack pose, int mouseX, int mouseY, float partialTick) {
        if (getParentScreen() != null) {
            getParentScreen().render(pose, mouseX, mouseY, partialTick);
            fill(pose, 0, 0, this.width, this.height, -1442840576);
        } else if (this.minecraft.level == null) {
            this.renderBackground(pose);
        }
    }

    /**
     * Not implemented in this class, implemented in child-classes
     */
    protected void onInitDialog() {
    }

    /**
     * Not implemented in this class, used for child-classes
     *
     * @param pose
     * @param mouseX
     * @param mouseY
     * @param partialTick
     */
    protected void drawDialog(PoseStack pose, int mouseX, int mouseY, float partialTick) {}

}
