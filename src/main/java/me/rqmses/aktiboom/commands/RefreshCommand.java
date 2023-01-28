package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.listeners.JoinListener;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;
import java.util.Timer;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.listeners.JoinListener.timer;

@SuppressWarnings("ALL")
public class RefreshCommand extends CommandBase implements IClientCommand {
    @Override
    @Nonnull
    public String getName() {
        return "refresh";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/refresh";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        timer = new Timer();
        JoinListener.refresh();

        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(PREFIX + "Alle Daten wurden neu geladen."));
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
