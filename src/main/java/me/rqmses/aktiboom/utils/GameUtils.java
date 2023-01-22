package me.rqmses.aktiboom.utils;

import me.rqmses.aktiboom.handlers.ConfigHandler;
import me.rqmses.aktiboom.listeners.ClientTickListener;
import me.rqmses.aktiboom.utils.guis.GameGui;
import me.rqmses.aktiboom.utils.guis.containers.ChessContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.inventory.Container;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

public class GameUtils {
    public static boolean party = false;
    public static List<String> players = new ArrayList<>();
    public static String playerturn = "";
    public static String stringboard =  "";
    public static String[] board = new String[0];
    public static String category = "";

    public static void display() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (party) {
            switch (category) {
                case "schach":
                    GameUtils.board = convertBoard(stringboard);
                    showBoard(new ChessContainer());
                    break;
            }
        } else {
            player.sendMessage(new TextComponentString(PREFIX + "Du befindest dich in keiner Party!"));
        }
    }

    private static void showBoard(Container container) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ClientTickListener.openedGuiNextTick = new GameGui(container);
            }
        }, 20);
    }

    private static String[] convertBoard(String stringboard) {
        switch (category) {
            case "schach":
                return stringboard.split("!");
        }
        return new String[0];
    }

    public static int turn = -1;

    public static void handleTurn() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (players.size() == ++turn) {
            turn = 0;
        }

        playerturn = players.get(turn);

        if (playerturn.contains(player.getName())) {
            player.sendMessage(new TextComponentString(PREFIX + "Du bist nun an der Reihe."));

            if (ConfigHandler.autoboard) {
                display();
            }
        } else {
            player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + playerturn + TextFormatting.YELLOW + " ist nun an der Reihe."));
        }
    }
}
