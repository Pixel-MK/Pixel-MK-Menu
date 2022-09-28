package com.pixelmk.pixelmkmenu.helpers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.internal.BrandingControl;

public class ForgeHelper {
    /**
     * The list of FML brandings
     */
    private static List<String> FMLBrandings = new ArrayList<>();

    public static final void init() {
        replaceBranding();
    }

    /**
     * Get the brandings for FML/Minecraft and replace them with an empty list.
     * This is to remove the brandings from the bottom left-hand corner of the main menu
     */
    @SuppressWarnings("unchecked")
    public static final void replaceBranding() {
        Field FMLBrandingField = ObfuscationReflectionHelper.findField(BrandingControl.class, "brandings");
        FMLBrandingField.setAccessible(true);
        Method BrandingMethod = ObfuscationReflectionHelper.findMethod(BrandingControl.class, "getBrandings",
                boolean.class, boolean.class);
        List<String> EmptyList = new ArrayList<>();
        try {
            if (FMLBrandingField != null && BrandingMethod != null && FMLBrandings.isEmpty())
                FMLBrandings = (List<String>) BrandingMethod.invoke(null, true, false);
            FMLBrandingField.set(BrandingControl.class, EmptyList);
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Get the branding list
     * @return brandings
     */
    public static final List<String> getBrandings() {
        return FMLBrandings;
    }
}
