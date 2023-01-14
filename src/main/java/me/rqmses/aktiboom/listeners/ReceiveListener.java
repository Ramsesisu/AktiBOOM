package me.rqmses.aktiboom.listeners;

import me.rqmses.aktiboom.handlers.ConfigHandler;
import me.rqmses.aktiboom.utils.FormatUtils;
import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.AktiBoom.SEC;
import static me.rqmses.aktiboom.handlers.ConfigHandler.secchatmessage;
import static me.rqmses.aktiboom.handlers.ConfigHandler.secchatprefix;

public class ReceiveListener {

    public static final HashMap<String, String> playerranks = new HashMap<>();

    @SubscribeEvent
    public void onMessage(ClientChatReceivedEvent event) {
        String message = event.getMessage().getUnformattedText();
        if (message.contains(": %SECCHAT% :")) {
            if (SEC) {
                String[] contents = message.split(":", 3);
                String name = contents[0].split(" ")[1].replace("[UC]", "");
                String text = contents[2];

                String secrankname;
                if (playerranks.containsKey(name)) {
                    secrankname = playerranks.get(name);
                } else {
                    String secrank = SheetUtils.getSECRank(name);
                    secrankname = secrank;
                    if (secrank.startsWith("E-")) {
                        secrankname = "Executive";
                    } else if (secrank.startsWith("C-")) {
                        secrankname = "Commander";
                    } else if (secrank.startsWith("G-")) {
                        secrankname = "General";
                    }
                    playerranks.putIfAbsent(name, secrankname);
                }

                event.setMessage(new TextComponentString(FormatUtils.getColor(secchatprefix) + "" + TextFormatting.ITALIC + "SEC " + FormatUtils.getColor(secchatprefix) + secrankname + " " + name + TextFormatting.DARK_GRAY + ":" + FormatUtils.getColor(secchatmessage) + text));
            } else {
                event.setCanceled(true);
            }
        }
        if (message.contains(": %INFO% :")) {
            String[] contents = message.split(":", 3);
            String text = contents[2];

            event.setMessage(new TextComponentString(PREFIX + TextFormatting.YELLOW + text));

            if (ConfigHandler.autorefresh) {
                JoinListener.refresh();
            }
        }

    }
}
