package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.enums.InformationType;
import me.rqmses.aktiboom.utils.SheetUtils;
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
import java.io.IOException;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class KillsCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "kills";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/kills";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            List<List<Object>> kills;
            try {
                kills = SheetUtils.getValueRange(InformationType.KILLS.getSheet(), InformationType.KILLS.getRange()).getValues();
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Die letzten Kills konnten nicht erfasst werden!"));
                return;
            }

            player.sendMessage(new TextComponentString(PREFIX + "Kills der letzten Bombe:"));

            for (List<Object> kill : kills) {
                player.sendMessage(new TextComponentString(TextFormatting.GOLD + "   \u271F " + TextFormatting.YELLOW + kill.get(1).toString() + TextFormatting.DARK_GRAY + " (" + TextFormatting.GRAY + kill.get(0).toString() + TextFormatting.DARK_GRAY + ")"));
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