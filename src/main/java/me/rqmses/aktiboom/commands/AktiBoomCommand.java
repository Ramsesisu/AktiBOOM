package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.AktiBoom.VERSION;
import static me.rqmses.aktiboom.handlers.SheetHandler.checkConnection;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class AktiBoomCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "aktiboom";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/aktiboom";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            boolean connection = checkConnection();
            player.sendMessage(new TextComponentString(PREFIX + "Version " + VERSION + TextFormatting.DARK_GRAY + " - " + TextFormatting.DARK_RED + "Ramses"));
            player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "            \u27A5 " + TextFormatting.RED + "Code", TextFormatting.DARK_AQUA + "GitHub", ClickEvent.Action.OPEN_URL, "https://github.com/Ramsesisu/AktiBOOM"));
            player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "            \u27A5 " + TextFormatting.BLUE + "Help", TextFormatting.DARK_GREEN + "Leitfaden", ClickEvent.Action.SUGGEST_COMMAND, "/leitfaden"));
            player.sendMessage(new TextComponentString(""));
            if (connection) {
                player.sendMessage(new TextComponentString(TextFormatting.GRAY + "  Status: " + TextFormatting.GREEN + "Verbunden"));
            } else {
                player.sendMessage(new TextComponentString(TextFormatting.GRAY + "  Status: " + TextFormatting.RED + "Getrennt"));
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