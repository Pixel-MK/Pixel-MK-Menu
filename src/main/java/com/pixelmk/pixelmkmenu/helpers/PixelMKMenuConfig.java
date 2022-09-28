package com.pixelmk.pixelmkmenu.helpers;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;

public final class PixelMKMenuConfig {
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final ClientConfig CLIENT;

    static {
        Pair<ClientConfig, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT = clientPair.getLeft();
        CLIENT_SPEC = clientPair.getRight();
    }

    private PixelMKMenuConfig() {
    }

    /**
     * The client config specification.
     */
    public static final class ClientConfig {
        public final ForgeConfigSpec.BooleanValue playMenuMusic;
        public final ForgeConfigSpec.BooleanValue playPixelMKMenuMusic;
        public final ForgeConfigSpec.ConfigValue<String> customServerIP;
        public final ForgeConfigSpec.ConfigValue<String> customServerName;
        public final ForgeConfigSpec.ConfigValue<Float> transitionRate;

        public ClientConfig(ForgeConfigSpec.Builder builder) {
            builder.push("options");
            playMenuMusic = builder
                    .comment("Should the main menu play menu music?")
                    .define("Play menu music", true);
            playPixelMKMenuMusic = builder
                    .comment("Should the main menu play the Pixel MK Menu menu music?")
                    .define("Play Pixel MK Menu menu music", true);
            builder.push("transitions");
            transitionRate = builder
                    .comment("Time in seconds it takes for a transition to elapse.")
                    .define("Transition rate", 1.0f);
            builder.pop();
            builder.push("favourite server");
            customServerIP = builder
                    .comment("The IP of your favourite server")
                    .define("Favourite server IP", "");
            customServerName = builder
                    .comment("The name of your favourite server")
                    .define("Favourite server name", "");
            builder.pop();
        }
    }
}