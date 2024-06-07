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

package io.github.pixelmk.pixelmkmenu;

import com.mojang.logging.LogUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.internal.BrandingControl;

/** Main class for client side operations. */
@OnlyIn(Dist.CLIENT)
public class PixelMKMenuClient {

  /** Shared mod instance. */
  private static final PixelMKMenuClient INSTANCE = new PixelMKMenuClient();

  /** NeoForge Brandings. */
  private static List<String> nfBrandings = new ArrayList<>();

  // public static Boolean modpackDefined;

  // private String modpackName;

  // private String modpackVersion;

  /**
   * Gets shared mod instance. Currently unneeded.
   *
   * @return Shared mod instance.
   */
  public static PixelMKMenuClient getInstance() {
    return INSTANCE;
  }

  private PixelMKMenuClient() {}

  /** Does nothing. */
  @Deprecated
  @SuppressWarnings("InlineMeSuggester")
  public void load() {
    LogUtils.getLogger().info("Loading Client");
  }

  /** Saves NeoForge branding and replaces with our own. */
  @SuppressWarnings({"unchecked", "PMD.AvoidAccessibilityAlteration"})
  public static final void replaceBranding() {
    Field nfBrandingField =
        ObfuscationReflectionHelper.findField(BrandingControl.class, "brandings");
    Method brandingMethod =
        ObfuscationReflectionHelper.findMethod(
            BrandingControl.class, "getBrandings", boolean.class, boolean.class);
    List<String> emptyList = new ArrayList<>();
    try {
      if (nfBrandingField == null) {
        throw new IllegalAccessException("Cannot access field brandings in BrandingControl.class");
      }
      nfBrandingField.setAccessible(true);
      if (brandingMethod != null && nfBrandings.isEmpty()) {
        nfBrandings = (List<String>) brandingMethod.invoke(null, true, false);
      }
      nfBrandingField.set(BrandingControl.class, emptyList);
    } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
      LogUtils.getLogger().error("Unable to remove and retrieve NF Brandings.");
      e.printStackTrace();
    }
  }

  /**
   * Get the branding list.
   *
   * @return brandings
   */
  public static List<String> getBrandings() {
    return new ArrayList<String>(nfBrandings);
  }
}
