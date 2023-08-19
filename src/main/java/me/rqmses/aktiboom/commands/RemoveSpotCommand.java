package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.enums.InformationType;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.*;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class RemoveSpotCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "removespot";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/removespot [Ort]";
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (args.length == 1) {
            targets = LocationUtils.getLocs();
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

            if (RANK > 3 || SEC) {
                if (args.length > 0) {
                    new Thread(() -> {
                        int line;
                        try {
                            line = SheetUtils.searchLine(InformationType.SPOTS.getSheet(), "C9:C299", args[0]) + 9;
                        } catch (IOException e) {
                            player.sendMessage(new TextComponentString(PREFIX + "Der Spot " + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " steht nicht in der \u00dcbersicht!"));
                            return;
                        }

                        try {
                            SheetUtils.clearValues(InformationType.SPOTS.getSheet(), "C" + line + ":I" + line);
                            SheetUtils.sortRange(InformationType.SPOTS.getSheet(), InformationType.SPOTS.getRange());
                            player.sendMessage(new TextComponentString(PREFIX + "Der Spot " + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " wurde erfolgreich entfernt."));
                        } catch (IOException e) {
                            player.sendMessage(new TextComponentString(PREFIX + "Der Spot " + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " konnte nicht gel\u00f6scht werden!"));
                        }
                    }).start();
                } else {
                    player.sendMessage(new TextComponentString(PREFIX + "Gib einen Spot zum L\u00f6schen an!"));
                }
            } else {
                player.sendMessage(new TextComponentString(PREFIX + "Du bist kein Rang-4 oder Teil des SECs!"));
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