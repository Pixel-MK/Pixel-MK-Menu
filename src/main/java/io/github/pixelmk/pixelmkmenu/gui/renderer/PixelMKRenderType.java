/*
* Copyright 2024 Joe Targett, Pixel MK Group
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
* associated documentation files (the “Software”), to deal in the Software without restriction,
* including without limitation the rights to use, copy, modify, merge, publish, distribute,
* sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all copies or
* substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
* NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
* DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package io.github.pixelmk.pixelmkmenu.gui.renderer;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard.DepthTestStateShard;
import net.minecraft.client.renderer.RenderStateShard.ShaderStateShard;
import net.minecraft.client.renderer.RenderStateShard.TransparencyStateShard;
import net.minecraft.client.renderer.RenderType;

/** Class containing static rendertypes where needed. */
@SuppressWarnings({"PMD.AvoidFieldNameMatchingMethodName"})
public class PixelMKRenderType {

  private static final RenderType TOOLTIP;

  /** Default Constructor. */
  PixelMKRenderType() {}

  static {
    TOOLTIP =
        net.minecraft.client.renderer.RenderType.create(
            "tooltip",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.TRIANGLES,
            786432,
            false,
            false,
            net.minecraft.client.renderer.RenderType.CompositeState.builder()
                .setShaderState(new ShaderStateShard(GameRenderer::getRendertypeGuiShader))
                .setTransparencyState(
                    new TransparencyStateShard(
                        "translucent_transparency",
                        () -> {
                          RenderSystem.enableBlend();
                          RenderSystem.blendFuncSeparate(
                              SourceFactor.SRC_ALPHA,
                              DestFactor.ONE_MINUS_SRC_ALPHA,
                              SourceFactor.ONE,
                              DestFactor.ONE_MINUS_SRC_ALPHA);
                        },
                        () -> {
                          RenderSystem.disableBlend();
                          RenderSystem.defaultBlendFunc();
                        }))
                .setDepthTestState(new DepthTestStateShard("<=", 515))
                .createCompositeState(false));
  }

  /**
   * Get tooltip RenderType.
   *
   * <ul>
   *   <li>Default Vertex Format - Position/Color
   *   <li>Mode - Triangles
   *   <li>Does not affect crumbling or sort on upload
   *   <li>Uses same state as GUI <code>RenderType</code>.
   * </ul>
   *
   * @return tooltip <code>RenderType</code>
   */
  public static RenderType tooltip() {
    return TOOLTIP;
  }
}
