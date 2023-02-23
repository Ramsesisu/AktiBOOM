package me.rqmses.aktiboom.listeners;

import me.rqmses.aktiboom.commands.*;
import me.rqmses.aktiboom.handlers.ConfigHandler;
import me.rqmses.aktiboom.handlers.UpdateHandler;
import me.rqmses.aktiboom.utils.SheetUtils;
import me.rqmses.aktiboom.utils.TimeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.*;

public class PlayerJoinListener {

    public static List<List<Object>> kalender = new ArrayList<>();
    public static HashMap<String, String> scheduler = new HashMap<>();

    public static Timer timer = new Timer();

    @SubscribeEvent
    public void onJoin(ClientChatReceivedEvent event) {
        if (event.getMessage().getUnformattedText().equals("Willkommen zur\u00fcck!")) {
            refresh();
        }
    }

    public static void refresh() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        BombeCommand.planter = false;
        PlayerUpdateListener.bombpos = new BlockPos(0, -1, 0);
        PlayerUpdateListener.showdistance = false;
        PlayerJoinListener.timer.cancel();


        new Thread(() -> {
            try {
                String latest = UpdateHandler.getLatest();
                if (latest.contains("Release") && !latest.endsWith(VERSION)) {
                    player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + latest + TextFormatting.YELLOW + " ist nun verf\u00fcgbar!"));

                    if (!LATEST.equals(latest.replace("Release ", ""))) {
                        if (ConfigHandler.autoupdate) {
                            try {
                                UpdateHandler.update(latest);
                                LATEST = latest.replace("Release ", "");
                                player.sendMessage(new TextComponentString(PREFIX + "Die neueste Version wurde erfolgreich heruntergeladen."));
                            } catch (Exception e) {
                                player.sendMessage(new TextComponentString(PREFIX + "Die neueste Version konnte nicht heruntergeladen werden!"));
                            }
                        }
                    }
                }
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Die neueste Version konnte nicht erfasst werden!"));
            }
        }).start();


        new Thread(() -> {
            SEC = SheetUtils.isSEC(player.getName());
            RANK = SheetUtils.getRank(player.getName());
            SECRANK = SheetUtils.getSECRank(player.getName());
            if (SECRANK.startsWith("E-")) {
                SECRANK = "Executive";
            } else if (SECRANK.startsWith("C-")) {
                SECRANK = "Commander";
            } else if (SECRANK.startsWith("G-")) {
                SECRANK = "General";
            }

            try {
                List<List<Object>> values = SheetUtils.getValueRange("SEC-Drogen", "H13:I21").getValues();
                for (List<Object> value : values) {
                    String secrank = value.get(1).toString();
                    String secrankname = secrank;
                    if (secrank.startsWith("E-")) {
                        secrankname = "Executive";
                    } else if (secrank.startsWith("C-")) {
                        secrankname = "Commander";
                    } else if (secrank.startsWith("G-")) {
                        secrankname = "General";
                    }
                    ChatReceiveListener.playerranks.put(value.get(0).toString(), secrankname);
                }
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Die SEC-R\u00e4nge konnten nicht geladen werden!"));
            }
        }).start();


        new Thread(() -> {
            SchutzCommand.syncList();
            AuftraegeCommand.syncList();
            SprengisCommand.syncList();
            TuningsCommand.syncList();
        }).start();


        new Thread(() -> {
            try {
                scheduler = new HashMap<>();

                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);

                String daycolumn;

                switch (day) {
                    case Calendar.MONDAY:
                        daycolumn = "C";
                        break;
                    case Calendar.TUESDAY:
                        daycolumn = "D";
                        break;
                    case Calendar.WEDNESDAY:
                        daycolumn = "E";
                        break;
                    case Calendar.THURSDAY:
                        daycolumn = "F";
                        break;
                    case Calendar.FRIDAY:
                        daycolumn = "G";
                        break;
                    case Calendar.SATURDAY:
                        daycolumn = "H";
                        break;
                    case Calendar.SUNDAY:
                        daycolumn = "I";
                        break;
                    default:
                        player.sendMessage(new TextComponentString(PREFIX + "Der Kalender konnte nicht vollst\u00e4ndig geladen werden!"));
                        return;
                }

                if (Integer.parseInt(new SimpleDateFormat("HH").format(new Date())) <= 2) {
                    daycolumn = String.valueOf((char) (daycolumn.charAt(0) - 1)).toUpperCase();
                }

                kalender = SheetUtils.getValueRange("Kalender", daycolumn + "4:" + daycolumn + "41").getValues();

                for (List<Object> list : kalender) {
                    if (list.size() > 0) {
                        String value = list.get(0).toString();
                        if (!value.equalsIgnoreCase("----")) {
                            String date = TimeUtils.lineToDate(kalender.indexOf(Collections.singletonList(value)));
                            kalender.set(kalender.indexOf(Collections.singletonList(value)), Collections.singletonList(""));
                            scheduler.put(date, value);
                        }
                    }
                }

                for (String key : scheduler.keySet()) {
                    // 0 Minuten
                    calendar.setTime(new SimpleDateFormat("dd.MM.yy HH:mm").parse(new SimpleDateFormat("dd.MM.yy").format(new Date()) + " " + key));
                    if (Integer.parseInt(new SimpleDateFormat("HH").format(calendar.getTime())) <= 2 && Integer.parseInt(new SimpleDateFormat("HH").format(new Date())) > 2) {
                        calendar.add(Calendar.HOUR, 24);
                    }
                    calendar.add(Calendar.MINUTE, -0);
                    if (calendar.getTime().after(new Date())) {
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (scheduler.get(key).split(" ").length == 0) {
                                    player.sendMessage(new TextComponentString(PREFIX + "Es findet nun eine " + TextFormatting.GOLD + scheduler.get(key).split(" ")[0] + "-Aktivit\u00e4t" + TextFormatting.YELLOW + " statt."));
                                } else {
                                    player.sendMessage(new TextComponentString(PREFIX + "Es findet nun eine " + TextFormatting.GOLD + scheduler.get(key).split(" ")[0] + "-Aktivit\u00e4t" + TextFormatting.YELLOW + ", geleitet von " + TextFormatting.GOLD + scheduler.get(key).split(" ")[1] + TextFormatting.YELLOW + ", statt."));
                                }
                            }
                        }, calendar.getTime());
                    }
                    // 10 Minuten
                    calendar.setTime(new SimpleDateFormat("dd.MM.yy HH:mm").parse(new SimpleDateFormat("dd.MM.yy").format(new Date()) + " " + key));
                    if (Integer.parseInt(new SimpleDateFormat("HH").format(calendar.getTime())) <= 2 && Integer.parseInt(new SimpleDateFormat("HH").format(new Date())) > 2) {
                        calendar.add(Calendar.HOUR, 24);
                    }
                    calendar.add(Calendar.MINUTE, -10);
                    if (calendar.getTime().after(new Date())) {
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (scheduler.get(key).split(" ").length == 0) {
                                    player.sendMessage(new TextComponentString(PREFIX + "Um " + TextFormatting.GOLD + "" + TextFormatting.BOLD + key + " Uhr" + TextFormatting.YELLOW + " findet eine " + TextFormatting.GOLD + scheduler.get(key).split(" ")[0] + "-Aktivit\u00e4t" + TextFormatting.YELLOW + " statt."));
                                } else {
                                    player.sendMessage(new TextComponentString(PREFIX + "Um " + TextFormatting.GOLD + "" + TextFormatting.BOLD + key + " Uhr" + TextFormatting.YELLOW + " findet eine " + TextFormatting.GOLD + scheduler.get(key).split(" ")[0] + "-Aktivit\u00e4t" + TextFormatting.YELLOW + ", geleitet von " + TextFormatting.GOLD + scheduler.get(key).split(" ")[1] + TextFormatting.YELLOW + ", statt."));
                                }
                            }
                        }, calendar.getTime());
                    }
                    // 30 Minuten
                    calendar.setTime(new SimpleDateFormat("dd.MM.yy HH:mm").parse(new SimpleDateFormat("dd.MM.yy").format(new Date()) + " " + key));
                    if (Integer.parseInt(new SimpleDateFormat("HH").format(calendar.getTime())) <= 2 && Integer.parseInt(new SimpleDateFormat("HH").format(new Date())) > 2) {
                        calendar.add(Calendar.HOUR, 24);
                    }
                    calendar.add(Calendar.MINUTE, -30);
                    if (calendar.getTime().after(new Date())) {
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (scheduler.get(key).split(" ").length == 0) {
                                    player.sendMessage(new TextComponentString(PREFIX + "Um " + TextFormatting.GOLD + "" + TextFormatting.BOLD + key + " Uhr" + TextFormatting.YELLOW + " findet eine " + TextFormatting.GOLD + scheduler.get(key).split(" ")[0] + "-Aktivit\u00e4t" + TextFormatting.YELLOW + " statt."));
                                } else {
                                    player.sendMessage(new TextComponentString(PREFIX + "Um " + TextFormatting.GOLD + "" + TextFormatting.BOLD + key + " Uhr" + TextFormatting.YELLOW + " findet eine " + TextFormatting.GOLD + scheduler.get(key).split(" ")[0] + "-Aktivit\u00e4t" + TextFormatting.YELLOW + ", geleitet von " + TextFormatting.GOLD + scheduler.get(key).split(" ")[1] + TextFormatting.YELLOW + ", statt."));
                                }
                            }
                        }, calendar.getTime());
                    }
                    // 90 Minuten
                    calendar.setTime(new SimpleDateFormat("dd.MM.yy HH:mm").parse(new SimpleDateFormat("dd.MM.yy").format(new Date()) + " " + key));
                    if (Integer.parseInt(new SimpleDateFormat("HH").format(calendar.getTime())) <= 2 && Integer.parseInt(new SimpleDateFormat("HH").format(new Date())) > 2) {
                        calendar.add(Calendar.HOUR, 24);
                    }
                    calendar.add(Calendar.MINUTE, -90);
                    if (calendar.getTime().after(new Date())) {
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (scheduler.get(key).split(" ").length == 0) {
                                    player.sendMessage(new TextComponentString(PREFIX + "Um " + TextFormatting.GOLD + "" + TextFormatting.BOLD + key + " Uhr" + TextFormatting.YELLOW + " findet eine " + TextFormatting.GOLD + scheduler.get(key).split(" ")[0] + "-Aktivit\u00e4t" + TextFormatting.YELLOW + " statt."));
                                } else {
                                    player.sendMessage(new TextComponentString(PREFIX + "Um " + TextFormatting.GOLD + "" + TextFormatting.BOLD + key + " Uhr" + TextFormatting.YELLOW + " findet eine " + TextFormatting.GOLD + scheduler.get(key).split(" ")[0] + "-Aktivit\u00e4t" + TextFormatting.YELLOW + ", geleitet von " + TextFormatting.GOLD + scheduler.get(key).split(" ")[1] + TextFormatting.YELLOW + ", statt."));
                                }
                            }
                        }, calendar.getTime());
                    }
                }

                if (ConfigHandler.autorefresh) {
                    calendar.setTime(new SimpleDateFormat("dd.MM.yy HH:mm").parse(new SimpleDateFormat("dd.MM.yy").format(new Date()) + " 23:59"));
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            refresh();
                        }
                    }, calendar.getTime());
                }
            } catch (IOException | ParseException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Der Kalender konnte nicht geladen werden!"));
            }
        }).start();
    }
}
