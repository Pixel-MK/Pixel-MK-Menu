package com.pixelmk.pixelmkmenu.controls;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.RealmsMainScreen;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.WorldGenSettings;

public enum ButtonAction {
    CONNECT_TO_SERVER(
        ai -> { // Data: Server IP (String)
            Minecraft mc = Minecraft.getInstance();
            ServerData data = getOrCreateServerData((String) ai.getData());
            ServerAddress addr = ServerAddress.parseString((String) ai.getData());
            ConnectScreen.startConnecting(mc.screen, mc, addr, data);
        },
        j -> j.get("data").getAsString()
    ),

    LOAD_WORLD(
        ai -> { // Data: World Name (String)
        },
        j -> j.get("data").getAsString()
    ),

    DEMO_WORLD (
        ai -> {
            //Data: is demo (bool)
            Minecraft mc = Minecraft.getInstance();
            if ((boolean) ai.getData()) {
                mc.loadLevel("Demo_World");
             } else {
                RegistryAccess registryaccess = RegistryAccess.BUILTIN.get();
                mc.createLevel("Demo_World", MinecraftServer.DEMO_SETTINGS, registryaccess, WorldGenSettings.demoSettings(registryaccess));
             }
        },
        j -> j.get("data").getAsBoolean()
    ),

    REALMS(
        ai -> {
            Minecraft.getInstance().setScreen(new RealmsMainScreen(Minecraft.getInstance().screen));
        },
        j -> null
    ),

    RELOAD(
        ai -> { // Data: null
            Minecraft.getInstance().reloadResourcePacks();
        },
        j -> null
    ),

    OPEN_GUI(
        ai -> { // Data: ScreenType (String)
            Minecraft.getInstance().setScreen(((ScreenType) ai.getData()).apply(Minecraft.getInstance().screen));
        },
        j -> ScreenType.valueOf(ScreenType.class, j.get("data").getAsString().toUpperCase(Locale.ROOT))
    ),

    OPEN_URL(
        ai -> { // Data: Link (URI)
            Util.getPlatform().openUri((URI) ai.getData());
        },
        j -> {
            try {
                return new URI(j.get("data").getAsString());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    ),

    QUIT(
        ai -> { // Data: null
            Minecraft.getInstance().stop();
        },
        j -> null
    ),

    NONE(ai -> {}, j -> null);

    private Consumer<ActionInstance> action;
    private Function<JsonObject, Object> reader;

    /**
     * Creates a button action.
     *
     * @param action The action instance containing this action. It is assumed have
     *               this action as it's specified action
     *               and any related data required in the data object.
     */
    ButtonAction(Consumer<ActionInstance> action, Function<JsonObject, Object> reader) {
        this.action = action;
        this.reader = reader;
    }

    public void onPress(ActionInstance button) {
        this.action.accept(button);
    }

    public Object readData(JsonObject json) {
        return this.reader.apply(json);
    }

    public static ServerData getOrCreateServerData(String ip) {
        JoinMultiplayerScreen MultiplayerScreen = new JoinMultiplayerScreen(Minecraft.getInstance().screen);
        MultiplayerScreen.init(Minecraft.getInstance(), 0, 0);
        ServerList list = MultiplayerScreen.getServers();
        for (int i = 0; i < list.size(); i++) {
            ServerData data = list.get(i);
            if (data.ip.equals(ip))
                return data;
        }
        ServerData data = new ServerData("Packmenu Managed Server", ip, false);
        list.add(data);
        list.save();
        return data;
    }

}
