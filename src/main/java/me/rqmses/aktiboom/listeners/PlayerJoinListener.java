package me.rqmses.aktiboom.listeners;

import me.rqmses.aktiboom.commands.*;
import me.rqmses.aktiboom.enums.InformationType;
import me.rqmses.aktiboom.handlers.ConfigHandler;
import me.rqmses.aktiboom.handlers.UpdateHandler;
import me.rqmses.aktiboom.utils.FormatUtils;
import me.rqmses.aktiboom.utils.InformationUtils;
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

            if (ConfigHandler.showaktis) {
                new Thread(() -> {
                    String column;

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());

                    switch (TimeUtils.calenderDay(calendar)) {
                        case 1:
                            column = "I";
                            break;
                        case 2:
                            column = "C";
                            break;
                        case 3:
                            column = "D";
                            break;
                        case 4:
                            column = "E";
                            break;
                        case 5:
                            column = "F";
                            break;
                        case 6:
                            column = "G";
                            break;
                        case 7:
                            column = "H";
                            break;
                        default:
                            return;
                    }

                    List<List<Object>> list;

                    try {
                        list = SheetUtils.getValueRange("Kalender", column + "4:" + column + "41").getValues();
                    } catch (IOException e) {
                        return;
                    }

                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(PREFIX + "Heutige Aktivit\u00e4ten:"));
                    KalenderCommand.loopDays(list);
                }).start();
            }
        }
    }

    public static void refresh() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        AFK = false;

        BombeCommand.planter = false;
        PlayerUpdateListener.bombpos = new BlockPos(0, -1, 0);
        PlayerUpdateListener.showdistance = false;

        PlayerJoinListener.timer.cancel();
        PlayerJoinListener.timer = new Timer();

        PREFIX = TextFormatting.DARK_GRAY + "[" + FormatUtils.getColor(ConfigHandler.prefix) + "AktiBOOM" + TextFormatting.DARK_GRAY + "] " + TextFormatting.YELLOW;

        new Thread(() -> {
            try {
                List<List<Object>> keys = SheetUtils.getValueRange(InformationType.KEYS.getSheet(), InformationType.KEYS.getRange()).getValues();

                for (List<Object> key : keys) {
                    KEYS.add(Integer.parseInt(key.get(0).toString()));
                }
            } catch (IOException e) {
                KEYS.add(0);
                KEYS.add(0);
            }
        }).start();

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
            List<List<Object>> members = new ArrayList<>();
            try {
                members = SheetUtils.getValueRange(InformationType.MEMBER.getSheet(), InformationType.MEMBER.getRange()).getValues();
            } catch (IOException ignored) {
            }

            for (List<Object> member : members) {
                MEMBER.put(member.get(1).toString(), Integer.parseInt(member.get(0).toString()));
            }
        }).start();

        new Thread(() -> {
            if (SheetUtils.getRank(player.getName()) >= 5) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!AFK) {
                            InformationUtils.getEquip(TimeUtils.hoursSinceMeeting());
                        }
                    }
                }, 0, 60 * 30 * 1000);
            }

            List<List<Object>> values;
            try {
                values = SheetUtils.getValueRange(InformationType.SECRANKNAMES.getSheet(), InformationType.SECRANKNAMES.getRange()).getValues();
                SECRANKS.put(0, "-");
                for (List<Object> value : values) {
                    String secrankname = value.get(0).toString();
                    SECRANKS.put(Integer.parseInt(value.get(1).toString()), secrankname);
                }

                values = SheetUtils.getValueRange(InformationType.SECMEMBER.getSheet(), InformationType.SECMEMBER.getRange()).getValues();
                for (List<Object> value : values) {
                    String secrank = value.get(1).toString();
                    SECMEMBER.put(value.get(0).toString(), Integer.parseInt(secrank));
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
            } catch (IOException | ParseException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Der Kalender konnte nicht geladen werden!"));
            }
        }).start();
    }
}
