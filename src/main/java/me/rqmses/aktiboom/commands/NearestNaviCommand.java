package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.LocationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

public class NearestNaviCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "nearestnavi";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/nearestnavi";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        String nearestnavi = LocationUtils.getNearestNavi(player.getPosition());
        BlockPos nearestpos = LocationUtils.uclocs.get(nearestnavi);
        BlockPos playerpos = player.getPosition();
        double distance = (double) Math.round(nearestpos.getDistance(playerpos.getX(), playerpos.getY(), playerpos.getZ()) * 10) / 10;

        player.sendMessage(new TextComponentString(PREFIX + "Der n\u00e4chste Navipunkt ist: " + TextFormatting.GOLD + "" + TextFormatting.BOLD + nearestnavi + TextFormatting.GRAY + " (" + TextFormatting.BOLD + distance + "m" + TextFormatting.GRAY + ")"));
        player.sendChatMessage("/navi " + nearestpos.getX()+"/"+nearestpos.getY()+"/"+nearestpos.getZ());
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
