package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.enums.InformationType;
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

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class AwayCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "away";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/away [Member]";
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> targets = new ArrayList<>();
        if (args.length == 1) {
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


            if (args.length > 0) {
                try {
                    if (SheetUtils.getValueRange(InformationType.AWAY_PERMISSION.getSheet(), InformationType.AWAY_PERMISSION.getRange()).toString().contains(player.getName())) {
                        player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " wird offline geschickt."));

                        player.sendChatMessage("/f %AWAY% : " + args[0]);
                    } else {
                        player.sendMessage(new TextComponentString(PREFIX + "Du hast nicht die ben\u00f6tigten Rechte!"));
                    }
                } catch (IOException e) {
                    player.sendMessage(new TextComponentString(PREFIX + "Die Away-Rechte konnten nicht erfasst werden!"));
                }
            } else {
                player.sendMessage(new TextComponentString(PREFIX + "Gib einen Member an!"));
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