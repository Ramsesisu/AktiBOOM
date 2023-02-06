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
import static me.rqmses.aktiboom.AktiBoom.SECRANK;

@SuppressWarnings("ALL")
public class SECCommand extends CommandBase implements IClientCommand {
    @Override
    @Nonnull
    public String getName() {
        return "sec";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/sec (invite/uninvite) ([Name])";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("specialelitecommando");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (args.length == 1) {
            targets = Arrays.asList("invite", "uninvite");
        }
        if (args.length == 2) {
            for (NetworkPlayerInfo playerInfo : Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfoMap()) {
                targets.add(String.valueOf(playerInfo.getGameProfile().getName()));
            }
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
            if (args.length > 0) {
                if (args.length > 1) {
                    if (SECRANK.startsWith("C") || SECRANK.startsWith("G")) {
                        SheetUtils.tobecontinued = false;
                        SheetUtils.returnvalues = 0;

                        SheetUtils.addEditorRequests = new ArrayList<>();
                        SheetUtils.removeEditorRequests = new ArrayList<>();

                        String email = SheetUtils.getEmail(args[1]);

                        if (args[0].equalsIgnoreCase("invite")) {
                            try {
                                SheetUtils.addValues("SEC-Drogen", "H13:I21", new String[]{args[1], "E-0" + SheetUtils.getRank(args[1])});

                                SheetUtils.addEditor("SEC-Drogen", "SEC-Drogen", email);
                                SheetUtils.addEditor("Spot\u00fcbersicht", "Spots", email);

                                player.sendChatMessage("/f %INFO% :&6&l" + args[1] + "&e wurde von " + player.getName() + " in das SEC invitet.");
                            } catch (IOException e) {
                                player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden."));
                            }
                        }
                        if (args[0].equalsIgnoreCase("uninvite")) {
                            try {
                                int line = SheetUtils.searchLine("SEC-Drogen", "H13:H21", args[1]) + 13;
                                List<Object> list = SheetUtils.getValueRange("SEC-Drogen", "H" + line + ":I" + line).getValues().get(0);
                                if (list.get(1).toString().equalsIgnoreCase("Name")) {
                                    player.sendMessage(new TextComponentString(PREFIX + "Der Spieler befindet sich nicht im SEC."));
                                    return;
                                }
                                SheetUtils.clearValues("SEC-Drogen", "H" + line + ":I" + line);
                                SheetUtils.sortRange("SEC-Drogen", "H13:I21");

                                SheetUtils.removeEditor("SEC-Drogen", "SEC-Drogen", email);
                                SheetUtils.removeEditor("Spot\u00fcbersicht", "Spots", email);

                                player.sendChatMessage("/f %INFO% :&6&l" + args[1] + "&e wurde von " + player.getName() + " aus dem SEC geworfen.");
                            } catch (IOException e) {
                                player.sendMessage(new TextComponentString(PREFIX + "Der Spieler befindet sich nicht im SEC."));
                            }
                        }

                        if (SheetUtils.tobecontinued) {
                            player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + "/continue" + TextFormatting.YELLOW + " um die verbleibenden Prozesse zu beenden."));
                            SheetUtils.tobecontinued = false;
                            SheetUtils.returnvalues = 0;
                        }
                    } else {
                        player.sendMessage(new TextComponentString(PREFIX + "Du bist kein SEC-Commander oder General."));
                    }
                } else {
                    player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
                }
            } else {
                try {
                    player.sendMessage(new TextComponentString(PREFIX + "Aktuelle SEC-Mitglieder:"));
                    player.sendMessage(new TextComponentString(""));

                    for (List<Object> list : SheetUtils.getValueRange("SEC-Drogen", "H13:I21").getValues()) {
                        if (!list.get(1).toString().contains("Tester")) {
                            player.sendMessage(new TextComponentString(TextFormatting.GRAY + list.get(0).toString() + TextFormatting.DARK_GRAY + " | " + TextFormatting.GRAY + list.get(1).toString()));
                        }
                    }
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
