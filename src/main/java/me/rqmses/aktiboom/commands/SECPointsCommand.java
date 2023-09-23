package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.enums.InformationType;
import me.rqmses.aktiboom.utils.InformationUtils;
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
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.*;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class SECPointsCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "secpoints";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/secpoints [Member] ([Anzahl])";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("secpunkte");
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
            targets = Arrays.asList("0", "30");
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

            int secrank = SECMEMBER.get(player.getName());
            if (args.length == 1) {
                player.sendMessage(new TextComponentString(PREFIX + "SEC-Punkte von " + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + ":"));

                String points;
                try {
                    int line = SheetUtils.searchLine(InformationType.SECNAMES.getSheet(), InformationType.SECNAMES.getRange(), args[0]) + 13;
                    points = SheetUtils.getValueRange(InformationType.SECPOINTS.getSheet(), "J" + line).getValues().get(0).get(0).toString();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                player.sendMessage(new TextComponentString("   " + TextFormatting.GRAY + InformationUtils.getSECRankName(SECMEMBER.get(args[0])) + " " + TextFormatting.GOLD + args[0] + TextFormatting.GRAY + ":" + TextFormatting.YELLOW + " " + TextFormatting.BOLD + points + TextFormatting.YELLOW + " Punkte"));
            } else if (args.length > 1) {
                if (secrank > 1) {
                    String error = PREFIX + "Die SEC-Punkte von " + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " konnten nicht gesetzt werden!";
                    int line;
                    try {
                        line = SheetUtils.searchLine(InformationType.SECNAMES.getSheet(), InformationType.SECNAMES.getRange(), args[0]) + 13;
                    } catch (IOException e) {
                        return;
                    }
                    if (line < 13) {
                        player.sendMessage(new TextComponentString(error));
                        return;
                    }
                    try {
                        SheetUtils.setLine(InformationType.SECPOINTS.getSheet(), "J" + line, new String[]{args[1]});
                    } catch (IOException e) {
                        player.sendMessage(new TextComponentString(error));
                        return;
                    }
                    player.sendMessage(new TextComponentString(PREFIX + "Die SEC-Punkte von " + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " wurden auf " + TextFormatting.GOLD + "" + TextFormatting.BOLD + args[1] + TextFormatting.YELLOW + " gesetzt."));
                } else {
                    player.sendMessage(new TextComponentString(PREFIX + "Du bist kein Teil der SEC-Leitung!"));
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