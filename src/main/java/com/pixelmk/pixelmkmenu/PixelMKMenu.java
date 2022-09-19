package com.pixelmk.pixelmkmenu;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.pixelmk.pixelmkmenu.interfaces.IPanoramaRenderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkConstants;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PixelMKMenu.MODID)
public class PixelMKMenu {
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "pixelmkmenu";

    public PixelMKMenu() {
        if (FMLEnvironment.dist == Dist.CLIENT)
            new PixelMKMenuClient().load();

        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class,
                () -> new DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }

    public static IPanoramaRenderer getPanoramaRenderer() {
        return null;
    }
}