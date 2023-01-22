package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
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
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + list.get(1).toString() + TextFormatting.DARK_GRAY + " | " + TextFormatting.YELLOW + list.get(3).toString() + TextFormatting.DARK_GRAY + " | " + TextFormatting.GRAY + list.get(2).toString() + TextFormatting.GRAY + " (" + list.get(0).toString() + ")"));
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

    public static void syncList() {
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
    }
}