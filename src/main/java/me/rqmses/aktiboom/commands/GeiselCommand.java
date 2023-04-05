package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.SheetUtils;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("ALL")
public class GeiselCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "geisel";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/geisel [Name] ([Fraktion]) ([Rang])";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("hostage");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (args.length == 1) {
            for (NetworkPlayerInfo playerInfo : Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfoMap()) {
                targets.add(String.valueOf(playerInfo.getGameProfile().getName()));
            }
        }
        if (args.length == 2) {
            targets = Arrays.asList("Zivilist", "Medic", "Cop", "Agent", "Zivi", "Rettungsdienst", "Polizei", "UCMD", "UCPD", "FBI", "SWAT", "HRT");
        }
        if (args.length == 3 && !args[0].startsWith("Zivi")) {
            targets = Arrays.asList("0", "1", "2", "3", "4", "5", "6");
        }
        Collections.sort(targets);
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

            if (args.length > 0) {
                String fraktion;

                try {
                    List<List<Object>> list = SheetUtils.getValueRange("Auftr\u00e4ge", "C61:C100").getValues();

                    if (list != null) {
                        int index = 0;
                        for (List<Object> values : list) {
                            if (values.get(0).toString().equals(args[0])) {
                                String line = String.valueOf(index + 61);

                                SheetUtils.clearValues("Auftr\u00e4ge", "C" + line + ":E" + line);
                                player.sendChatMessage("/f %INFO% :&6" + player.getName() + "&e hat &6&l" + args[0] + "&e aus der Geisel-Liste entfernt!");

                                SheetUtils.sortRange("Auftr\u00e4ge", "C61:E100");
                                return;
                            }

                            index++;
                        }
                    }


                    if (args.length > 1) {
                        if (args.length > 2 || args[1].toLowerCase().startsWith("zivi")) {
                            int rank;
                            if (args[1].toLowerCase().startsWith("zivi")) {
                                rank = 0;
                            } else {
                                rank = Integer.parseInt(args[2]);
                            }
                            if (rank >= 0 && rank <= 6) {
                                switch (args[1].toLowerCase()) {
                                    case "zivi":
                                    case "zivilist":
                                        fraktion = "Zivilist";
                                        break;
                                    case "medic":
                                    case "rettungsdienst":
                                    case "ucmd":
                                        fraktion = "Rettungsdienst";
                                        break;
                                    case "cop":
                                    case "polizei":
                                    case "ucpd":
                                        fraktion = "Polizei";
                                        break;
                                    case "agent":
                                    case "fbi":
                                        fraktion = "FBI";
                                        break;
                                    case "swat":
                                        fraktion = "SWAT";
                                        break;
                                    case "hrt":
                                        fraktion = "HRT";
                                        break;
                                    default:
                                        player.sendMessage(new TextComponentString(PREFIX + "Die Fraktion " + TextFormatting.GOLD + args[1] + TextFormatting.YELLOW + " ist ung\u00fcltig!"));
                                        return;
                                }
                                SheetUtils.addValues("Auftr\u00e4ge", "C61:E100", new String[]{args[0], fraktion, String.valueOf(rank)});

                                player.sendChatMessage("/f %INFO% :&6" + player.getName() + "&e hat &6&l" + args[0] + "&e zur Geisel-Liste hinzugef\u00fcgt!");
                            } else {
                                player.sendMessage(new TextComponentString(PREFIX + "Rang-" + TextFormatting.GOLD + args[2] + TextFormatting.YELLOW + " ist ung\u00fcltig!"));
                            }
                        } else {
                            player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
                        }
                    } else {
                        player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
                    }
                } catch (IOException e) {
                    player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zur Geisel-Liste hergestellt werden!"));
                }
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
}
