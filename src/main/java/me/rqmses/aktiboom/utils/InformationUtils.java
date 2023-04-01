package me.rqmses.aktiboom.utils;

import me.rqmses.aktiboom.listeners.ChatReceiveListener;
import net.minecraft.client.Minecraft;

public class InformationUtils {
    public static void getStats(String prefix) {
        ChatReceiveListener.hide = true;
        ChatReceiveListener.searchprefix = prefix;

        Minecraft.getMinecraft().player.sendChatMessage("/stats");
    }
    public static void getEquip(int hours) {
        ChatReceiveListener.hide = true;

        Minecraft.getMinecraft().player.sendChatMessage("/equiplog " + hours);
    }
}
