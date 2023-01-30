package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.LocationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ALL")
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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        Timer timer = new Timer();

        player.sendChatMessage("/bombe");

        x = player.getPosition().getX();
        y = player.getPosition().getY();
        z = player.getPosition().getZ();

        String pos = x+"/"+y+"/"+z;

        String nearestnavi = LocationUtils.getNearestNavi(player.getPosition());
        BlockPos nearestpos = LocationUtils.uclocs.get(nearestnavi);
        double distance = (double) Math.round(nearestpos.getDistance(x, y, z) * 10) / 10;
        boolean max = distance > 100;

        player.sendChatMessage("/f %INFO% :&6&l" + player.getName() + "&e hat eine Bombe bei " + pos + " gelegt.");
        if (max) {
            player.sendChatMessage("/f %INFO% :" + "Die Bombe ist zu weit vom n\u00e4chsten Navipunkt &6&l" + nearestnavi + "&7 (&l" + distance + "m&7) " + "&eentfernt.");
        } else {
            player.sendChatMessage("/f %INFO% :" + "Der n\u00e4chste Navipunkt zur Bombe ist: &6&l" + nearestnavi + "&7 (&l" + distance + "m&7)");
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.sendChatMessage("/f %NAVI% : " + pos);
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
