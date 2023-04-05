package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.EncryptionUtils;
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

import static me.rqmses.aktiboom.AktiBoom.*;

@SuppressWarnings("ALL")
public class SECChatCommand extends CommandBase implements IClientCommand {
    @Override
    @Nonnull
    public String getName() {
        return "secchat";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/sf [Nachricht]";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("sf");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (args.length > 0) {
            if (SEC) {
                StringBuilder text = new StringBuilder();
                for (String arg : args) {
                    text.append(" ").append(arg);
                }

                player.sendChatMessage("/f %SECCHAT% :" + EncryptionUtils.encode(text.toString(), KEYS.get(0)));
            } else {
                player.sendMessage(new TextComponentString(PREFIX + "Du bist nicht im SEC."));
            }
        } else {
            player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
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
