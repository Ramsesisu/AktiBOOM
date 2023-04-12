package me.rqmses.aktiboom.utils;

import me.rqmses.aktiboom.enums.InformationType;
import me.rqmses.aktiboom.listeners.ChatReceiveListener;
import net.minecraft.client.Minecraft;

import java.io.IOException;

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

    public static void clearOperation() throws IOException {
        SheetUtils.clearValues(InformationType.MATESHOTS.getSheet(), InformationType.MATESHOTS.getRange());
        SheetUtils.clearValues(InformationType.KILLS.getSheet(), InformationType.KILLS.getRange());
        SheetUtils.clearValues(InformationType.KILLS_LOG.getSheet(), InformationType.KILLS_LOG.getRange());
    }
}
