package me.rqmses.aktiboom.listeners;

import me.rqmses.aktiboom.handlers.ConfigHandler;
import me.rqmses.aktiboom.utils.FormatUtils;
import me.rqmses.aktiboom.utils.GameUtils;
import me.rqmses.aktiboom.utils.SheetUtils;
import me.rqmses.aktiboom.utils.guis.GameGui;
import me.rqmses.aktiboom.utils.guis.containers.ChessContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.AktiBoom.SEC;
import static me.rqmses.aktiboom.handlers.ConfigHandler.*;

public class ChatReceiveListener {

    public static final HashMap<String, String> playerranks = new HashMap<>();

    @SubscribeEvent
    public void onMessage(ClientChatReceivedEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
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
        if (message.contains(": %PARTY% :")) {
            event.setCanceled(true);
            String[] contents = message.split(":", 4);
            String players = contents[3];
            if (!GameUtils.party) {
                if (Arrays.asList(players.split(" ")).contains(player.getName())) {
                    GameUtils.players = new ArrayList<>(Arrays.asList(players.split(" ")));
                    GameUtils.players.removeAll(Collections.singleton(""));
                    switch (contents[2].toLowerCase().replaceAll(" ", "")) {
                        case "schach":
                            GameUtils.category = contents[2].replaceAll(" ", "").toLowerCase();
                            GameUtils.turn = -1;
                            ChessContainer.mate = false;
                            ChessContainer.draw = false;
                            ChessContainer.moves = new ArrayList<>();
                            ChessContainer.selectedindex = -1;
                            GameUtils.stringboard = "Rb!Nb!Bb!Qb!Kb!Bb!Nb!Rb!Tc!" +
                                                    "Pb!Pb!Pb!Pb!Pb!Pb!Pb!Pb!Tc!" +
                                                    "Ac!Ac!Ac!Ac!Ac!Ac!Ac!Ac!Tc!" +
                                                    "Ac!Ac!Ac!Ac!Ac!Ac!Ac!Ac!Tc!" +
                                                    "Ac!Ac!Ac!Ac!Ac!Ac!Ac!Ac!Tc!" +
                                                    "Ac!Ac!Ac!Ac!Ac!Ac!Ac!Ac!Tc!" +
                                                    "Pw!Pw!Pw!Pw!Pw!Pw!Pw!Pw!Tc!" +
                                                    "Rw!Nw!Bw!Qw!Kw!Bw!Nw!Rw!Tc";
                            break;
                        case "tictactoe":
                            GameUtils.category = contents[2].replaceAll(" ", "").toLowerCase();
                            GameUtils.turn = -1;
                            GameUtils.win = false;
                            GameUtils.stringboard = "Tb!Tb!Tb!Ab!Ab!Ab!Tb!Tb!Tb!" +
                                                    "Tb!Tb!Tb!Ab!Ab!Ab!Tb!Tb!Tb!" +
                                                    "Tb!Tb!Tb!Ab!Ab!Ab!Tb!Tb!Tb!";
                            break;
                        default:
                            return;
                    }
                    GameUtils.party = true;
                    player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + GameUtils.players.get(0) + TextFormatting.YELLOW + " hat eine " + TextFormatting.GOLD + contents[2].replaceAll(" ", "") + TextFormatting.YELLOW + "-Party gestartet."));
                    player.sendMessage(new TextComponentString(TextFormatting.GRAY + "      \u27A5 " + TextFormatting.YELLOW + "Gib /game ein, um das Spielbrett zu \u00f6ffnen."));
                    GameUtils.handleTurn();
                }
            } else {
                if (Arrays.asList(players.split(" ")).contains(player.getName())) {
                    if (contents[2].replaceAll(" ", "").equalsIgnoreCase("end")) {
                        GameUtils.party = false;
                        if (Minecraft.getMinecraft().currentScreen instanceof GameGui) {
                            player.closeScreen();
                        }
                        player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + contents[0].split(" ")[1] + TextFormatting.YELLOW + " hat die Party beendet."));
                        return;
                    }
                }
            }
        }
        if (message.contains(": %GAME% :")) {
            event.setCanceled(true);
            String[] contents = message.split(":", 4);
            String players = contents[2];
            if (GameUtils.party) {
                if (Arrays.asList(players.split(" ")).contains(player.getName())) {
                    GameUtils.stringboard = contents[3];
                    GameUtils.handleTurn();
                    if (Minecraft.getMinecraft().currentScreen instanceof GameGui) {
                        GameUtils.display();
                    }
                }
            }
        }
    }
}
