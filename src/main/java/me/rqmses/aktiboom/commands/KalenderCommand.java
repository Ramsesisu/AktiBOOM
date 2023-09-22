package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.SheetUtils;
import me.rqmses.aktiboom.utils.TimeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nullable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.*;

@SuppressWarnings("ALL")
public class KalenderCommand extends CommandBase implements IClientCommand {
    @Override
    public String getName() {
        return "kalender";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/kalender [Aktivit\u00e4t] [Leiter] [Tag] [Uhrzeit]";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("calender");
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> targets = new ArrayList<>();
        if (args.length < 5) {
            for (NetworkPlayerInfo playerInfo : Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfoMap()) {
                targets.add(String.valueOf(playerInfo.getGameProfile().getName()));
            }
        }
        if (args.length == 1) {
            targets = new ArrayList<>(Arrays.asList("Geiselnahme", "Bombe", "Training", "Event", "Sonstiges"));
        }
        if (args.length == 3) {
            targets = new ArrayList<>(Arrays.asList("Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag", new SimpleDateFormat("dd.MM.").format(new Date())));
        }
        if (args.length == 4) {
            targets = new ArrayList<>(Collections.singletonList(new SimpleDateFormat("HH:mm").format(TimeUtils.roundHalf(new Date()))));
        }
        for (String target : targets) {
            if (target.toUpperCase().startsWith(args[args.length-1].toUpperCase()))
                list.add(target);
        }
        return list;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            if (args.length > 3) {
                if (MEMBER.get(player.getName()) > 2) {
                    String column;
                    String line;
                    if (args[2].contains(".")) {
                        int weekday;
                        try {
                            weekday = TimeUtils.getWeekDay(args[2]);
                        } catch (IOException e) {
                            player.sendMessage(new TextComponentString(PREFIX + "Ung\u00fcltiges Datum!"));
                            return;
                        }
                        switch (weekday) {
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
                                player.sendMessage(new TextComponentString(PREFIX + "Ung\u00fcltiges Datum!"));
                                return;
                        }
                    } else {
                        switch (args[2].toLowerCase()) {
                            case "sonntag":
                                column = "I";
                                break;
                            case "montag":
                                column = "C";
                                break;
                            case "dienstag":
                                column = "D";
                                break;
                            case "mittwoch":
                                column = "E";
                                break;
                            case "donnerstag":
                                column = "F";
                                break;
                            case "freitag":
                                column = "G";
                                break;
                            case "samstag":
                                column = "H";
                                break;
                            default:
                                player.sendMessage(new TextComponentString(PREFIX + "Ung\u00fcltiger Wochentag!"));
                                return;
                        }
                    }

                    switch (args[3]) {
                        case "02:30":
                            player.sendMessage(new TextComponentString(PREFIX + "Der Zeitraum von 02:00-07:30 Uhr ist f\u00fcr Aktivit\u00e4ten gesperrt"));
                            return;
                        case "08:00":
                            line = "5";
                            break;
                        case "08:30":
                            line = "6";
                            break;
                        case "09:00":
                            line = "7";
                            break;
                        case "09:30":
                            line = "8";
                            break;
                        case "10:00":
                            line = "9";
                            break;
                        case "10:30":
                            line = "10";
                            break;
                        case "11:00":
                            line = "11";
                            break;
                        case "11:30":
                            line = "12";
                            break;
                        case "12:00":
                            line = "13";
                            break;
                        case "12:30":
                            line = "14";
                            break;
                        case "13:00":
                            line = "15";
                            break;
                        case "13:30":
                            line = "16";
                            break;
                        case "14:00":
                            line = "17";
                            break;
                        case "14:30":
                            line = "18";
                            break;
                        case "15:00":
                            line = "19";
                            break;
                        case "15:30":
                            line = "20";
                            break;
                        case "16:00":
                            line = "21";
                            break;
                        case "16:30":
                            line = "22";
                            break;
                        case "17:00":
                            line = "23";
                            break;
                        case "17:30":
                            line = "24";
                            break;
                        case "18:00":
                            line = "25";
                            break;
                        case "18:30":
                            line = "26";
                            break;
                        case "19:00":
                            line = "27";
                            break;
                        case "19:30":
                            line = "28";
                            break;
                        case "20:00":
                            line = "29";
                            break;
                        case "20:30":
                            line = "30";
                            break;
                        case "21:00":
                            line = "31";
                            break;
                        case "21:30":
                            line = "32";
                            break;
                        case "22:00":
                            line = "33";
                            break;
                        case "22:30":
                            line = "34";
                            break;
                        case "23:00":
                            line = "35";
                            break;
                        case "23:30":
                            line = "36";
                            break;
                        case "00:00":
                            line = "37";
                            break;
                        case "00:30":
                            line = "38";
                            break;
                        case "01:00":
                            line = "39";
                            break;
                        case "01:30":
                            line = "40";
                            break;
                        case "02:00":
                            line = "41";
                            break;
                        default:
                            player.sendMessage(new TextComponentString(PREFIX + "Ung\u00fcltige Uhrzeit!"));
                            return;
                    }

                    if (Integer.parseInt(new SimpleDateFormat("HH").format(new Date())) >= 0 && Integer.parseInt(new SimpleDateFormat("HH").format(new Date())) <= 2) {
                        column = String.valueOf((char) (column.charAt(0) - 1)).toUpperCase();
                    }

                    if (column.equals("B") || column.equals("J")) {
                        player.sendMessage(new TextComponentString(PREFIX + "Der Inhalt passt nicht in die aktuelle Tabelle."));
                        return;
                    }

                    if (args[3].length() == 4) {
                        args[3] = "0" + args[3];
                    }

                    try {
                        SheetUtils.setLine("Kalender", column + line, new String[]{args[0] + " " + args[1]});
                    } catch (IOException e) {
                        player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden."));
                        return;
                    }
                    player.sendChatMessage("/f %INFO% :" + player.getName() + " hat eine &6&l" + args[0] + "&e-Aktivit\u00e4t, geleitet von &6" + args[1] + "&e, zum &6" + args[2] + "&e um &6" + args[3] + " Uhr&e eingetragen.");
                } else {
                    player.sendMessage(new TextComponentString(PREFIX + "Du musst Rang-3 sein um in den Kalender eintragen zu k\u00f6nnen!"));
                }
            } else if (args.length == 0) {
                List<List<Object>> kalender;
                player.sendMessage(new TextComponentString(PREFIX + "Kalender der aktuellen Woche:"));
                player.sendMessage(new TextComponentString(""));

                try {
                    kalender = SheetUtils.getValueRange("Kalender", "C4:C41").getValues();
                } catch (IOException e) {
                    player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden."));
                    return;
                }
                player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Montag: "));
                loopDays(kalender);

                try {
                    kalender = SheetUtils.getValueRange("Kalender", "D4:D41").getValues();
                } catch (IOException e) {
                    player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden."));
                    return;
                }
                player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Dienstag: "));
                loopDays(kalender);

                try {
                    kalender = SheetUtils.getValueRange("Kalender", "E4:E41").getValues();
                } catch (IOException e) {
                    player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden."));
                    return;
                }
                player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Mittwoch: "));
                loopDays(kalender);

                try {
                    kalender = SheetUtils.getValueRange("Kalender", "F4:F41").getValues();
                } catch (IOException e) {
                    player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden."));
                    return;
                }
                player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Donnerstag: "));
                loopDays(kalender);

                try {
                    kalender = SheetUtils.getValueRange("Kalender", "G4:G41").getValues();
                } catch (IOException e) {
                    player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden."));
                    return;
                }
                player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Freitag: "));
                loopDays(kalender);

                try {
                    kalender = SheetUtils.getValueRange("Kalender", "H4:H41").getValues();
                } catch (IOException e) {
                    player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden."));
                    return;
                }
                player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Samstag: "));
                loopDays(kalender);

                try {
                    kalender = SheetUtils.getValueRange("Kalender", "I4:I41").getValues();
                } catch (IOException e) {
                    player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden."));
                    return;
                }
                player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Sonntag: "));
                loopDays(kalender);

            } else {
                player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
            }
        }).start();
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }

    public static void loopDays(List<List<Object>> kalender) {
        for (List<Object> list : kalender) {
            if (list.size() > 0) {
                String value = list.get(0).toString();
                if (!value.equalsIgnoreCase("----")) {
                    String date = TimeUtils.lineToDate(kalender.indexOf(Collections.singletonList(value)));
                    kalender.set(kalender.indexOf(Collections.singletonList(value)), Collections.singletonList(""));
                    TextFormatting formatting = TextFormatting.GRAY;
                    if (value.toLowerCase().startsWith("bombe")) {
                        formatting = TextFormatting.GREEN;
                    }
                    if (value.toLowerCase().startsWith("geiselnahme")) {
                        formatting = TextFormatting.RED;
                    }
                    if (value.toLowerCase().startsWith("event")) {
                        formatting = TextFormatting.LIGHT_PURPLE;
                    }
                    if (value.toLowerCase().startsWith("training")) {
                        formatting = TextFormatting.AQUA;
                    }
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   " + date + ":  " + formatting + value));
                }
            }
        }
    }
}
