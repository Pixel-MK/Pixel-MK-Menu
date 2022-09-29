package com.pixelmk.pixelmkmenu.helpers;

import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

public class FBO {
    private static boolean supported = false;

    private static boolean useARB = false;

    private boolean created;

    private boolean active;

    private int depthBuffer;

    private int frameBuffer;

    private DynamicTexture texture;

    private int frameBufferWidth;

    private int frameBufferHeight;

    public static boolean detectFBOCapabilities() {
        GLCapabilities capabilities = GL.getCapabilities();
        if (capabilities.GL_ARB_framebuffer_object) {
            supported = true;
            useARB = true;
            return true;
        }
        if (capabilities.GL_EXT_framebuffer_object) {
            supported = true;
            return true;
        }
        supported = false;
        return false;
    }

    public FBO() {
        detectFBOCapabilities();
    }

    public static boolean isSupported() {
        return supported;
    }

    public void begin(int width, int height) {
        if (!supported)
            return;
        if (width < 1 || height < 1)
            throw new IllegalArgumentException("Attempted to create FBO with zero/negative size.");
        if (this.created && (width != this.frameBufferWidth || height != this.frameBufferHeight))
            dispose();
        if (!this.created) {
            this.created = true;
            this.frameBufferWidth = width;
            this.frameBufferHeight = height;
            NativeImage textureImage = new NativeImage(this.frameBufferWidth, this.frameBufferHeight, true);
            this.texture = new DynamicTexture(textureImage);
            if (useARB) {
                this.frameBuffer = ARBFramebufferObject.glGenFramebuffers();
                this.depthBuffer = ARBFramebufferObject.glGenRenderbuffers();
                ARBFramebufferObject.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.frameBuffer);
                ARBFramebufferObject.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
                        GL11.GL_TEXTURE_2D, this.texture.getId(), 0);
                ARBFramebufferObject.glBindRenderbuffer(GL30.GL_RENDERBUFFER, this.depthBuffer);
                ARBFramebufferObject.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24,
                        this.frameBufferWidth, this.frameBufferHeight);
                ARBFramebufferObject.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
                        GL30.GL_RENDERBUFFER, this.depthBuffer);
                ARBFramebufferObject.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
                ARBFramebufferObject.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
            } else {
                this.frameBuffer = EXTFramebufferObject.glGenFramebuffersEXT();
                this.depthBuffer = EXTFramebufferObject.glGenRenderbuffersEXT();
                EXTFramebufferObject.glBindFramebufferEXT(GL30.GL_FRAMEBUFFER, this.frameBuffer);
                EXTFramebufferObject.glFramebufferTexture2DEXT(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
                        GL11.GL_TEXTURE_2D, this.texture.getId(), 0);
                EXTFramebufferObject.glBindRenderbufferEXT(GL30.GL_RENDERBUFFER, this.depthBuffer);
                EXTFramebufferObject.glRenderbufferStorageEXT(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24,
                        this.frameBufferWidth, this.frameBufferHeight);
                EXTFramebufferObject.glFramebufferRenderbufferEXT(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
                        GL30.GL_RENDERBUFFER, this.depthBuffer);
                EXTFramebufferObject.glBindFramebufferEXT(GL30.GL_FRAMEBUFFER, 0);
                EXTFramebufferObject.glBindRenderbufferEXT(GL30.GL_RENDERBUFFER, 0);
            }
        }
        bind();
    }

    private void _bind() {
        RenderSystem.assertOnRenderThread();
        if (!supported)
            return;
        if (this.created && checkFBO()) {
            if (useARB) {
                ARBFramebufferObject.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.frameBuffer);
                ARBFramebufferObject.glBindRenderbuffer(GL30.GL_RENDERBUFFER, this.depthBuffer);
            } else {
                EXTFramebufferObject.glBindFramebufferEXT(GL30.GL_FRAMEBUFFER, this.frameBuffer);
                EXTFramebufferObject.glBindRenderbufferEXT(GL30.GL_RENDERBUFFER, this.depthBuffer);
            }
        }
    }

    public void bind() {
        RenderSystem.assertOnGameThreadOrInit();
        if (!RenderSystem.isInInitPhase()) {
            RenderSystem.recordRenderCall(() -> {
                this._bind();
            });
        } else {
            this._bind();
        }
    }

    public void end() {
        RenderSystem.assertOnGameThreadOrInit();
        if (supported && this.active) {
            if (useARB) {
                ARBFramebufferObject.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
                ARBFramebufferObject.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
            } else {
                EXTFramebufferObject.glBindFramebufferEXT(GL30.GL_FRAMEBUFFER, 0);
                EXTFramebufferObject.glBindRenderbufferEXT(GL30.GL_RENDERBUFFER, 0);
            }
            GL11.glPopAttrib();
            this.active = false;
        }
    }

    public void dispose() {
        if (!supported)
            return;
        end();
        if (this.texture != null)
            GL11.glDeleteTextures(this.texture.getId());
        if (useARB) {
            ARBFramebufferObject.glDeleteRenderbuffers(this.depthBuffer);
            ARBFramebufferObject.glDeleteFramebuffers(this.frameBuffer);
        } else {
            EXTFramebufferObject.glDeleteRenderbuffersEXT(this.depthBuffer);
            EXTFramebufferObject.glDeleteFramebuffersEXT(this.frameBuffer);
        }
        this.depthBuffer = 0;
        this.texture = null;
        this.frameBuffer = 0;
        this.created = false;
    }

    private boolean checkFBO() {
        if (useARB) {
            ARBFramebufferObject.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.frameBuffer);
            ARBFramebufferObject.glBindRenderbuffer(GL30.GL_RENDERBUFFER, this.depthBuffer);
        } else {
            EXTFramebufferObject.glBindFramebufferEXT(GL30.GL_FRAMEBUFFER, this.frameBuffer);
            EXTFramebufferObject.glBindRenderbufferEXT(GL30.GL_RENDERBUFFER, this.depthBuffer);
        }
        int frameBufferStatus = useARB ? ARBFramebufferObject.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER)
                : EXTFramebufferObject.glCheckFramebufferStatusEXT(GL30.GL_FRAMEBUFFER);
        switch (frameBufferStatus) {
            case 36053:
                return true;
            case 36054:
            case 36055:
            case 36057:
            case 36058:
            case 36059:
            case 36060:
                return false;
        }
        throw new RuntimeException("Unexpected reply from glCheckFramebufferStatus: " + frameBufferStatus);
    }

    public void draw(int x, int y, int x2, int y2, int z, float alpha) {
        draw(x, y, x2, y2, z, alpha, 0.0f, 0.0f, 1.0f, 1.0f);
    }

    public void draw(double x, double y, double x2, double y2, double z, float alpha, float u, float v, float u2,
            float v2) {
        if (supported && this.created) {
            RenderSystem.enableTexture();
            RenderSystem.setShaderTexture(0, this.texture.getId());
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder bb = tessellator.getBuilder();
            bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bb.vertex(x, y2, z).uv(u, v).endVertex();
            bb.vertex(x2, y2, z).uv(u2, v).endVertex();
            bb.vertex(x2, y, z).uv(u2, v2).endVertex();
            bb.vertex(x, y, z).uv(u, v2).endVertex();
            tessellator.end();
        }
    }
}
