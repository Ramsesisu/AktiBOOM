package me.rqmses.aktiboom.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.AktiBoom.SEC;

public class SECChatCommand extends CommandBase implements IClientCommand {
    @Override
    @Nonnull
    public String getName() {
        return "secchat";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/sf [Nachricht]";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Arrays.asList("sf");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (args.length > 0) {
            if (SEC) {
                StringBuilder text = new StringBuilder();
                for (String arg : args) {
                    text.append(" ").append(arg);
                }
                player.sendChatMessage("/f %SECCHAT% :" + text);
            } else {
                player.sendMessage(new TextComponentString(PREFIX + "Du bist nicht im SEC."));
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
