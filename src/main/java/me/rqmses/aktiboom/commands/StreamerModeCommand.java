package me.rqmses.aktiboom.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class StreamerModeCommand extends CommandBase implements IClientCommand {

    public static boolean streamermode = false;

    @Override
    @Nonnull
    public String getName() {
        return "streamermode";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/streamermode";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (streamermode) {
            streamermode = false;
            player.sendMessage(new TextComponentString(PREFIX + "Streaming-Modus " + TextFormatting.RED + "deaktiviert" + TextFormatting.YELLOW + "."));
        } else {
            streamermode = true;
            player.sendMessage(new TextComponentString(PREFIX + "Streaming-Modus " + TextFormatting.GREEN + "aktiviert" + TextFormatting.YELLOW + "."));
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