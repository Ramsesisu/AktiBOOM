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

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("ALL")
public class TuningsCommand extends CommandBase implements IClientCommand {

    public static List<String> tunings = new ArrayList<>();

    @Override
    @Nonnull
    public String getName() {
        return "tunings";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/tunings";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Arrays.asList("autobomben", "specialtunings", "carbombs");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            player.sendMessage(new TextComponentString(PREFIX + "Aktuelle Autobomben:"));
            player.sendMessage(new TextComponentString(""));

            List<List<Object>> values;
            try {
                values = SheetUtils.getValueRange("Auftr\u00e4ge", "Q4:T54").getValues();
            } catch (IOException e) {
                return;
            }

            if (values == null) {
                return;
            }

            for (List<Object> list : values) {
                player.sendMessage(new TextComponentString("  " + TextFormatting.GOLD + list.get(1).toString() + TextFormatting.DARK_GRAY + " | " + TextFormatting.YELLOW + list.get(3).toString() + TextFormatting.DARK_GRAY + " | " + TextFormatting.DARK_GRAY + list.get(2).toString() + " (" + list.get(0).toString() + ")"));
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

    public static void syncList() {
        new Thread(() -> {
            tunings = new ArrayList<>();
            List<List<Object>> values;
            try {
                values = SheetUtils.getValueRange("Auftr\u00e4ge", "Q4:T54").getValues();
            } catch (IOException e) {
                return;
            }
            if (values == null) {
                return;
            }
            for (List<Object> list : values) {
                tunings.add(list.get(1).toString());
            }
        }).start();
    }
}
