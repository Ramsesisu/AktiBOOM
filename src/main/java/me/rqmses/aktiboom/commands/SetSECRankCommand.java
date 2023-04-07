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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.AktiBoom.SECRANK;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class SetSECRankCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "setsecrank";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/setsecrank [Name] [Rang]";
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
            targets = Arrays.asList("Executive", "Commander", "Marshal");
        }
        for (String target : targets) {
            if (target.toUpperCase().startsWith(args[args.length-1].toUpperCase()))
                list.add(target);
        }
        Collections.sort(targets);
        return list;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            if (args.length > 1) {
                if (SECRANK.equals("Commander") || SECRANK.equals("Marshal")) {
                    String rank = args[1];
                    String oldrank = SheetUtils.getSECRank(args[0]);

                    if (rank.equals("Executive") || rank.equals("Commander") || rank.equals("Marshal")) {
                        if (!SheetUtils.setSECRank(args[0], rank)) {
                            player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " ist nicht in der Fraktion!"));
                        } else {
                            String email = SheetUtils.getEmail(args[0]);

                            if (rank.equals("Executive") && !oldrank.equals("Executive")) {
                                SheetUtils.removeEditor("SEC-Drogen", "SEC-Member", email);
                            } else if ((rank.equals("Commander") || rank.equals("Marshal")) && oldrank.equals("Executive")) {
                                SheetUtils.addEditor("SEC-Drogen", "SEC-Member", email);
                            }

                            player.sendMessage(new TextComponentString(PREFIX + "Der SEC-Rang von " + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " wurde zu " + TextFormatting.GOLD + rank + TextFormatting.YELLOW + " aktualisiert."));
                        }
                    } else {
                        player.sendMessage(new TextComponentString(PREFIX + "Gib einen g\u00fcltigen Rang an!"));
                    }
                } else {
                    player.sendMessage(new TextComponentString(PREFIX + "Du bist kein SEC-Commander oder General!"));
                }
            } else if (args.length == 1) {
                player.sendMessage(new TextComponentString(PREFIX + "Gib einen Rang an!"));
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