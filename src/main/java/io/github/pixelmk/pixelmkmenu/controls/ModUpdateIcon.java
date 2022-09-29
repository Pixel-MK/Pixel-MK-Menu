package io.github.pixelmk.pixelmkmenu.controls;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.NotificationModUpdateScreen;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.versions.forge.ForgeVersion;

/**
 * Class to eliminate/move the mod update icon diamond, allowing it to be tied to a <code>GuiButtonMainMenu</code>
 */
public class ModUpdateIcon extends NotificationModUpdateScreen {

    private static final ResourceLocation VERSION_CHECK_ICONS = new ResourceLocation(ForgeVersion.MOD_ID,
            "textures/gui/version_check_icons.png");
    private VersionChecker.Status showNotification = null;
    private ButtonPanel panel;
    private Button modButton;

    public ModUpdateIcon(Button modButton) {
        super(modButton);
        this.modButton = modButton;
    }

    public void init(ButtonPanel panel)
    {
        super.init();
        this.panel = panel;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick)
    {
        if (showNotification == null || !showNotification.shouldDraw() || !FMLConfig.runVersionCheck())
        {
            return;
        }

        RenderSystem.setShaderTexture(0, VERSION_CHECK_ICONS);

        int x = panel.getAdjustedXPosition((GuiButtonMainMenu)modButton);
        int y = panel.getAdjustedYPosition((GuiButtonMainMenu)modButton);
        int w = modButton.getWidth();
        int h = modButton.getHeight();

        blit(poseStack, x + w - (h / 2 + 4), y + (h / 2 - 4), showNotification.getSheetOffset() * 8, (showNotification.isAnimated() && ((System.currentTimeMillis() / 800 & 1) == 1)) ? 8 : 0, 8, 8, 64, 16);
    }

    public static NotificationModUpdateScreen init(TitleScreen guiMainMenu, Button modButton, ButtonPanel panel)
    {
        ModUpdateIcon notificationModUpdateScreen = new ModUpdateIcon(modButton);
        notificationModUpdateScreen.resize(guiMainMenu.getMinecraft(), guiMainMenu.width, guiMainMenu.height);
        notificationModUpdateScreen.init(panel);
        return notificationModUpdateScreen;
    }

}
