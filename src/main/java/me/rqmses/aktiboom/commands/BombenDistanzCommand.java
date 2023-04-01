package me.rqmses.aktiboom.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.listeners.PlayerUpdateListener.bombpos;
import static me.rqmses.aktiboom.listeners.PlayerUpdateListener.showdistance;

@SuppressWarnings("ALL")
public class BombenDistanzCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "bombendistanz";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/bombendistanz";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("bombdistance");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (bombpos.getY() == -1) {
            player.sendMessage(new TextComponentString(PREFIX + "Es ist gerade keine Bombe gelegt!"));
            return;
        }

        showdistance = !showdistance;
        if (showdistance) {
            player.sendMessage(new TextComponentString(PREFIX + "Die Bomben-Distanz wird nun angezeigt."));
        } else {
            player.sendMessage(new TextComponentString(PREFIX + "Die Bomben-Distanz wird nun ausgeblendet."));
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
