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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.AktiBoom.RANK;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class RenameMemberCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "renamemember";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/renamemember [Name] [Neuer-Name]";
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (args.length == 2) {
            for (NetworkPlayerInfo playerInfo : Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfoMap()) {
                targets.add(String.valueOf(playerInfo.getGameProfile().getName()));
            }
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
                if (RANK >= 5) {
                    try {
                        if (!SheetUtils.getValueRange("\u00dcbersicht", "B4:B27").getValues().toString().contains(args[0])) {
                            player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " befindet sich nicht im Aktivit\u00e4tsnachweis!"));
                            return;
                        }

                        int line = SheetUtils.searchLine("\u00dcbersicht", "B4:B27", args[0]) + 4;
                        SheetUtils.setValues("\u00dcbersicht", "B" + line, new String[]{args[1]});

                        line = SheetUtils.searchLine("Equiplog", "C36:C60", args[0]) + 36;
                        SheetUtils.setValues("Equiplog", "C" + line, new String[]{args[1]});

                        line = SheetUtils.searchLine("SEC-Drogen", "H13:H21", args[0]) + 13;
                        if (line > 12) {
                            SheetUtils.setValues("SEC-Drogen", "H" + line, new String[]{args[1]});
                        }

                        SheetUtils.renameSheet(SheetUtils.getSheetID(args[0]), args[1]);
                    } catch (IOException e) {
                        player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " konnte nicht zu " + TextFormatting.GOLD + args[1] + TextFormatting.YELLOW + " umbenannt werden!"));
                        return;
                    }

                    player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " wurde zu " + TextFormatting.GOLD + args[1] + TextFormatting.YELLOW + " umbenannt."));
                } else {
                    player.sendMessage(new TextComponentString(PREFIX + "Du bist kein Leader!"));
                }
            } else if (args.length > 0) {
                player.sendMessage(new TextComponentString(PREFIX + "Gib einen neuen Namen an!"));
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