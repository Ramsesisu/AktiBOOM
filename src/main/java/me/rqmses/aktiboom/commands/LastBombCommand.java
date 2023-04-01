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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class LastBombCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "lastbomb";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/lastbomb";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("letztebombe");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            List<Object> list;
            try {
                list = SheetUtils.getValueRange("Win/Lose Statistik", "H34:K34").getValues().get(0);
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Die letzte Bombe konnte nicht erfasst werden!"));
                return;
            }

            player.sendMessage(new TextComponentString(PREFIX + "Letzte Bombe:"));

            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "  Datum: " + TextFormatting.YELLOW + list.get(0)));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "  Uhrzeit: " + TextFormatting.YELLOW + list.get(1) + TextFormatting.DARK_GRAY + " (" + TextFormatting.GRAY + LocalTime.parse(list.get(1).toString(), formatter).plusHours(4).format(formatter) + TextFormatting.DARK_GRAY + ")"));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "  Ort: " + TextFormatting.YELLOW + list.get(2)));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "  Leger: " + TextFormatting.YELLOW + list.get(3)));
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