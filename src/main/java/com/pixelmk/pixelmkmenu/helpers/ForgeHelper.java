package com.pixelmk.pixelmkmenu.helpers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.internal.BrandingControl;

public class ForgeHelper {
    private static List<String> FMLBrandings = new ArrayList<>();

    public static void init() {
        replaceBranding();
    }

    @SuppressWarnings("unchecked")
    public static void replaceBranding() {
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

    public static List<String> getBrandings() {
        return FMLBrandings;
    }
}
