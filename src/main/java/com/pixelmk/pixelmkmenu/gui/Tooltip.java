package com.pixelmk.pixelmkmenu.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class Tooltip extends AbstractWidget {

    private Component TextLong;
    private ArrayList<Component> TextArray;
    private int yOffset;
    private int xSize;
    private int ySize;
    private int tweak;
    private Minecraft INSTANCE = Minecraft.getInstance();
    private int textWidth;

    public Tooltip(String textLong, String textShort, int xPos, int yPos, int yOffset) {
        super(xPos, yPos, 0, 0, new TranslatableComponent(textShort));
        this.setMessage(new TranslatableComponent(textShort));
        this.textWidth = INSTANCE.font.width(this.getMessage().getString());
        this.width = this.textWidth;
        this.height = INSTANCE.font.lineHeight;
        this.xSize = INSTANCE.font.width(textLong) + 18;
        this.ySize = INSTANCE.font.lineHeight + 3;
        this.TextLong = new TranslatableComponent(textLong);
        this.tweak = 0;
        this.yOffset = yOffset;
    }

    public Tooltip(List<String> list, String textShort, int xPos, int yPos, int xSize, int ySize, int yOffset) {
        super(xPos, yPos, 0, 0, new TranslatableComponent(textShort));
        this.setMessage(new TranslatableComponent(textShort));
        this.textWidth = INSTANCE.font.width(this.getMessage().getString());
        this.width = this.textWidth;
        this.height = INSTANCE.font.lineHeight;
        this.xSize = xSize;
        this.ySize = ySize;
        this.yOffset = yOffset;
        this.TextArray = new ArrayList<>();
        this.TextLong = null;
        for (String str : list)
            this.TextArray.add(new TranslatableComponent(str));
    }

    @Override
    public void renderButton(PoseStack pose, int mouseX, int mouseY, float partial) {
        drawString(pose, INSTANCE.font, this.getMessage(), this.x, this.y, 16777215);
        if (this.isHoveredOrFocused()) {
            if (TextLong != null) {
                drawTooltip(pose, mouseX, mouseY);
                drawString(pose, INSTANCE.font, this.TextLong, mouseX + 16, mouseY + 2 - yOffset, 16777215);
            } else {
                this.tweak = 45;
                int height = TextArray.size() * 8 + 6;
                this.ySize = height + 10;
                this.yOffset = 10;
                drawTooltip(pose, 80, (this.y - 10) - height + 2);
                int top = (this.y + 10) - height - 12;
                for (Component brand : this.TextArray) {
                    drawString(pose, INSTANCE.font, brand, 100 , top - yOffset, -1);
                    top += 10;
                }
            }
        }
    }

    private void drawTooltip(PoseStack pose, int mouseX, int mouseY) {
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bb = tessellator.getBuilder();
        float f3 = (float) (1610612736 >> 24 & 255) / 255.0F;
        float f = (float) (1610612736 >> 16 & 255) / 255.0F;
        float f1 = (float) (1610612736 >> 8 & 255) / 255.0F;
        float f2 = (float) (1610612736 & 255) / 255.0F;
        fill(pose, mouseX + 14, mouseY - yOffset, mouseX + xSize, mouseY + ySize - yOffset, 1610612736);
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE,
                DestFactor.ZERO);
        RenderSystem.setShaderColor(f, f1, f2, f3);
        bb.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION);
        bb.vertex(pose.last().pose(), (float) (mouseX + 14), (float) (mouseY + ySize - yOffset), 0.0f).endVertex();
		bb.vertex(pose.last().pose(), (float) (mouseX+14), (float) mouseY - yOffset, 0.0f).endVertex();
		bb.vertex(pose.last().pose(), (float) mouseX, (float) (mouseY + this.tweak), 0.0f).endVertex();
        tessellator.end();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    @Override
    public void updateNarration(NarrationElementOutput p_169152_) {
        // TODO Auto-generated method stub

    }

}
