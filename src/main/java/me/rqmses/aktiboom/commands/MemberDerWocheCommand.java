package me.rqmses.aktiboom.commands;

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
import java.util.Collections;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class MemberDerWocheCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "memberderwoche";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/memberderwoche";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("memberoftheweek");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            List<List<Object>> list;
            try {
                 list = SheetUtils.getValueRange("\u00dcbersicht", "E44:K44").getValues();
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Die \u00dcbersicht konnte nicht geladen werden!"));
                return;
            }

            player.sendMessage(new TextComponentString(PREFIX + "Aktuelle Member der Woche:"));

            player.sendMessage(new TextComponentString(TextFormatting.GOLD + " " + TextFormatting.BOLD + "Gesamtaktivit\u00e4ten"));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   " + list.get(0).get(0).toString()));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + " " + TextFormatting.BOLD + "Gewinn"));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   " + list.get(0).get(2).toString()));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + " " + TextFormatting.BOLD + "RolePlay"));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   " + list.get(0).get(5).toString()));
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