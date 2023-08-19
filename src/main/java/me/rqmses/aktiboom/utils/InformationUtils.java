package me.rqmses.aktiboom.utils;

import me.rqmses.aktiboom.enums.InformationType;
import me.rqmses.aktiboom.listeners.ChatReceiveListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.text.TextComponentString;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

public class InformationUtils {
    public static void getStats(String prefix) {
        ChatReceiveListener.hide = true;
        ChatReceiveListener.searchprefix = prefix;

        Minecraft.getMinecraft().player.sendChatMessage("/stats");
    }

    public static void getEquip(int hours) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        NetHandlerPlayClient netHandlerPlayClient = Minecraft.getMinecraft().getConnection();
        if (netHandlerPlayClient != null) {
            if (netHandlerPlayClient.getNetworkManager().channel().remoteAddress().toString().toLowerCase().contains("unicacity.de")) {
                ChatReceiveListener.hide = true;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ChatReceiveListener.hide = false;
                    }
                }, 5 * 1000);

                player.sendChatMessage("/equiplog " + hours);
                player.sendMessage(new TextComponentString(PREFIX + "Der Equiplog wird aktualisiert."));
            }
        }
    }

    public static void clearOperation() throws IOException {
        SheetUtils.clearValues(InformationType.MATESHOTS.getSheet(), InformationType.MATESHOTS.getRange());
    }
}
