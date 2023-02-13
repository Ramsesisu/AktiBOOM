package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.enums.InformationType;
import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.io.IOException;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class GlobalCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "global";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/global [Nachricht]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            if (args.length > 0) {
                try {
                    if (SheetUtils.getValueRange(InformationType.GLOBAL_PERMISSION.getSheet(), InformationType.GLOBAL_PERMISSION.getRange()).toString().contains(player.getName())) {
                        StringBuilder text = new StringBuilder();
                        for (String arg : args) {
                            text.append(" ").append(arg);
                        }
                        player.sendChatMessage("/f %INFO% :" + text.toString().replaceFirst(" ", ""));
                    } else {
                        player.sendMessage(new TextComponentString(PREFIX + "Du hast nicht die ben\u00f6tigten Rechte!"));
                    }
                } catch (IOException e) {
                    player.sendMessage(new TextComponentString(PREFIX + "Die Global-Rechte konnten nicht erfasst werden!"));
                }
            } else {
                player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
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