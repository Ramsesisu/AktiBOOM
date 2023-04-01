package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.LocationUtils;
import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.AktiBoom.RANK;

@SuppressWarnings("ALL")
public class StatistikCommand extends CommandBase implements IClientCommand {
    @Override
    @Nonnull
    public String getName() {
        return "statistik";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/statistik (win/lose) ([Ort])";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Arrays.asList("statistic", "stat");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (args.length == 1) {
            targets = Arrays.asList("win", "lose");
        }
        if (args.length == 2) {
            targets = LocationUtils.getLocs();
        }
        for (String target : targets) {
            if (target.toUpperCase().startsWith(args[args.length-1].toUpperCase()))
                list.add(target);
        }
        Collections.sort(list);
        return list;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            if (args.length > 0) {
                if (args.length > 1) {
                    if (RANK > 3) {
                        if (args[0].equalsIgnoreCase("win")) {
                            try {
                                int line = SheetUtils.getValueRange("Win/Lose Statistik", "C6:D64").getValues().size() + 1;
                                SheetUtils.setValues("Win/Lose Statistik", "C" + (line + 7), new String[]{args[1] + " (" + new SimpleDateFormat("dd.MM").format(new Date()) + ")"});
                                player.sendMessage(new TextComponentString(PREFIX + "Der Sieg wurde erfolgreich eingetragen."));
                            } catch (IOException e) {
                                player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden."));
                            }
                            return;
                        }
                        if (args[0].equalsIgnoreCase("lose")) {
                            try {
                                int line = SheetUtils.getValueRange("Win/Lose Statistik", "E6:F64").getValues().size() + 1;
                                SheetUtils.setValues("Win/Lose Statistik", "E" + (line + 7), new String[]{args[1] + " (" + new SimpleDateFormat("dd.MM").format(new Date()) + ")"});
                                player.sendMessage(new TextComponentString(PREFIX + "Die Niederlage wurde erfolgreich eingetragen."));
                            } catch (IOException e) {
                                player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden."));
                            }
                            return;
                        }
                        player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
                    } else {
                        player.sendMessage(new TextComponentString(PREFIX + "Du musst Rang-4 sein um in die Statistik eintragen zu k\u00f6nnen!"));
                    }
                } else {
                    player.sendMessage(new TextComponentString(PREFIX + "Du musst einen Ort angeben."));
                }
            } else {
                try {
                    player.sendMessage(new TextComponentString(PREFIX + "Win/Lose-Statistik:"));
                    player.sendMessage(new TextComponentString(""));

                    List<List<Object>> wins = SheetUtils.getValueRange("Win/Lose Statistik", "C7:C64").getValues();
                    player.sendMessage(new TextComponentString(TextFormatting.GREEN + "" + TextFormatting.ITALIC + "Gewonnen"));
                    if (wins != null) {
                        for (List<Object> value : wins) {
                            player.sendMessage(new TextComponentString("   " + TextFormatting.GRAY + value.get(0).toString()));
                        }
                    } else {
                        player.sendMessage(new TextComponentString("   " + TextFormatting.GRAY + "-"));
                    }
                    List<List<Object>> loses = SheetUtils.getValueRange("Win/Lose Statistik", "E7:E64").getValues();
                    player.sendMessage(new TextComponentString(TextFormatting.RED + "" + TextFormatting.ITALIC + "Verloren"));
                    if (loses != null) {
                        for (List<Object> value : loses) {
                            player.sendMessage(new TextComponentString("   " + TextFormatting.GRAY + value.get(0).toString()));
                        }
                    } else {
                        player.sendMessage(new TextComponentString("   " + TextFormatting.GRAY + "-"));
                    }
                    player.sendMessage(new TextComponentString(""));

                    String win = SheetUtils.getValueRange("Win/Lose Statistik", "H12:I12").getValues().get(0).get(0).toString();
                    Float w = Float.valueOf(Integer.parseInt(win));
                    player.sendMessage(new TextComponentString(TextFormatting.GRAY + "" + TextFormatting.BOLD + "Gewonnen: " + TextFormatting.GREEN + win));
                    String lose = SheetUtils.getValueRange("Win/Lose Statistik", "J12:K12").getValues().get(0).get(0).toString();
                    Float l = Float.valueOf(Integer.parseInt(lose));
                    player.sendMessage(new TextComponentString(TextFormatting.GRAY + "" + TextFormatting.BOLD + "Verloren: " + TextFormatting.RED + lose));
                    player.sendMessage(new TextComponentString(""));

                    player.sendMessage(new TextComponentString(TextFormatting.GRAY + "" + TextFormatting.BOLD + "Rate: " + TextFormatting.BLUE + String.valueOf(Math.round((w/l) * 100.0)) + "%"));
                } catch (IOException e) {
                    player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden."));
                }
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
}
