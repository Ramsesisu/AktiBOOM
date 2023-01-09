package me.rqmses.aktiboom.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class BombeCommand extends CommandBase implements IClientCommand {

    private static int x = 0;
    private static int y = 0;
    private static int z = 0;

    @Override
    @Nonnull
    public String getName() {
        return "bombe";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/bombe";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("bomb");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        Timer timer = new Timer();

        player.sendChatMessage("/bombe");

        x = player.getPosition().getX();
        y = player.getPosition().getY();
        z = player.getPosition().getZ();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.sendChatMessage("/navi " + x+"/"+y+"/"+z);
            }
        }, TimeUnit.SECONDS.toMillis(3));
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
