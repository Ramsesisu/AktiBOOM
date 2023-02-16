package me.rqmses.aktiboom.utils;

import me.rqmses.aktiboom.listeners.ChatReceiveListener;
import net.minecraft.client.Minecraft;

public class InformationUtils {
    public static void getStats(String prefix) {
        ChatReceiveListener.hide = true;
        ChatReceiveListener.searchprefix = prefix;

        Minecraft.getMinecraft().player.sendChatMessage("/stats");
    }
}
