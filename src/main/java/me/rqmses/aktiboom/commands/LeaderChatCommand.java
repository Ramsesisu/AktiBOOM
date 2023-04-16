package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.EncryptionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static me.rqmses.aktiboom.AktiBoom.*;

@SuppressWarnings("ALL")
public class LeaderChatCommand extends CommandBase implements IClientCommand {
    @Override
    @Nonnull
    public String getName() {
        return "leaderchat";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/lf [Nachricht]";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("lf");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> targets = new ArrayList<>();
        for (NetworkPlayerInfo playerInfo : Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfoMap()) {
            targets.add(String.valueOf(playerInfo.getGameProfile().getName()));
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
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (args.length > 0) {
            if (RANK >= 4) {
                StringBuilder text = new StringBuilder();
                for (String arg : args) {
                    text.append(" ").append(arg);
                }

                player.sendChatMessage("/f %LEADER% :" + EncryptionUtils.encode(text.toString(), KEYS.get(1)));
            } else {
                player.sendMessage(new TextComponentString(PREFIX + "Du bist nicht im Leaderteam."));
            }
        } else {
            player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
        }
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
