package io.github.pixelmk.pixelmkmenu.compat.create;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.config.ui.BaseConfigScreen;
import com.simibubi.create.foundation.gui.CreateMainMenuScreen;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.ponder.ui.PonderTagIndexScreen;

import io.github.pixelmk.pixelmkmenu.controls.ActionInstance;
import io.github.pixelmk.pixelmkmenu.controls.ButtonAction;
import io.github.pixelmk.pixelmkmenu.controls.ButtonPanel;
import io.github.pixelmk.pixelmkmenu.controls.GuiButtonMainMenu;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class PixelMKMenuCreateMenuScreen extends CreateMainMenuScreen {

    private ButtonPanel buttonPanelLeft;
    private ButtonPanel buttonPanelRight;
    private int updateCounter;

    public PixelMKMenuCreateMenuScreen(Screen parent) {
        super(parent);
    }

    @Override
    protected void init() {
        super.init();
        this.renderables.clear();
        this.children().clear();
        this.addButtons();
    }

    protected void addButtons() {
        if (this.buttonPanelLeft == null) {
            this.buttonPanelLeft = new ButtonPanel(ButtonPanel.AnchorType.BottomLeft, 12,
                    20, 150, 100, 16, this.width, this.height, "left", new ActionInstance(ButtonAction.NONE, null));
            this.buttonPanelRight = new ButtonPanel(ButtonPanel.AnchorType.BottomRight, 12,
                    20, 150, 100, 16, this.width, this.height, "right", new ActionInstance(ButtonAction.NONE, null));
            initPanelButtons();
        } else {
            this.buttonPanelLeft.updatePosition(this.width, this.height);
            this.buttonPanelRight.updatePosition(this.width, this.height);
        }
        this.addRenderableWidget(this.buttonPanelLeft);
        this.addRenderableWidget(this.buttonPanelRight);
    }

    protected void initPanelButtons() {
        this.buttonPanelLeft.addButton(
                new GuiButtonMainMenu(new TranslatableComponent("create.menu.configure"),
                        $ -> linkTo(BaseConfigScreen.forCreate(this))));

        var gettingStarted = new GuiButtonMainMenu(new TranslatableComponent("create.menu.ponder_index"),
                $ -> linkTo(new PonderTagIndexScreen()));
        gettingStarted.active = !(parent instanceof TitleScreen);
        this.buttonPanelRight.addButton(gettingStarted);

        this.buttonPanelRight.addButton(new GuiButtonMainMenu(
                new TextComponent("CurseForge").withStyle(s -> s.withColor(0xFC785C).withBold(true)),
                b -> linkTo(CURSEFORGE_LINK)));
        this.buttonPanelRight.addButton(new GuiButtonMainMenu(
                new TextComponent("Modrinth").withStyle(s -> s.withColor(0x3FD32B).withBold(true)),
                b -> linkTo(MODRINTH_LINK)));

        this.buttonPanelRight.addButton(new GuiButtonMainMenu(new TranslatableComponent("create.menu.report_bugs"),
                $ -> linkTo(ISSUE_TRACKER_LINK)));
        this.buttonPanelLeft.addButton(new GuiButtonMainMenu(new TranslatableComponent("create.menu.support"),
                $ -> linkTo(SUPPORT_LINK)));
        this.buttonPanelLeft.addButton(
                new GuiButtonMainMenu(new TranslatableComponent("create.menu.return"), $ -> this.linkTo(this.parent)));
    }

    @Override
    public void tick() {
        super.tick();
        ++this.updateCounter;
    }

    @Override
    public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        this.buttonPanelLeft.updateButtons(this.updateCounter, partialTicks, mouseX, mouseY);
        this.buttonPanelRight.updateButtons(this.updateCounter, partialTicks, mouseX, mouseY);
        super.render(ms, mouseX, mouseY, partialTicks);
    }

    private void linkTo(Screen screen) {
        returnOnClose = false;
        ScreenOpener.open(screen);
    }

    private void linkTo(String url) {
        returnOnClose = false;
        ScreenOpener.open(new ConfirmLinkScreen((p_213069_2_) -> {
            if (p_213069_2_)
                Util.getPlatform()
                        .openUri(url);
            this.minecraft.setScreen(this);
        }, url, true));
    }
}
