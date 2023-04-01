package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.listeners.ClientTickListener;
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


public class JumpBombeCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "jumpbombe";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/jumpbombe";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("jumpbomb");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            ClientTickListener.posY = player.posY;

            player.sendMessage(new TextComponentString(PREFIX + "Springe nun um die Bombe zu legen!"));
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