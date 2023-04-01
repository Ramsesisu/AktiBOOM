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
public class SprengisCommand extends CommandBase implements IClientCommand {

    public static List<String> drohungen = new ArrayList<>();

    @Override
    @Nonnull
    public String getName() {
        return "sprengis";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/sprengis";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Arrays.asList("sprengg\u00fcrtelauftr\u00e4ge", "sprengiauftr\u00e4ge");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            player.sendMessage(new TextComponentString(PREFIX + "Aktuelle Sprengg\u00fcrtelauftr\u00e4ge:"));
            player.sendMessage(new TextComponentString(""));

            List<List<Object>> values;
            try {
                values = SheetUtils.getValueRange("Auftr\u00e4ge", "G4:K54").getValues();
            } catch (IOException e) {
                return;
            }

            if (values == null) {
                return;
            }

            for (List<Object> list : values) {
                player.sendMessage(new TextComponentString(TextFormatting.GOLD + list.get(1).toString() + TextFormatting.DARK_GRAY + " | " + TextFormatting.YELLOW + list.get(3).toString() + " bis " + list.get(4) + TextFormatting.DARK_GRAY + " | " + TextFormatting.GRAY + list.get(2).toString() + TextFormatting.GRAY + " (" + list.get(0).toString() + ")"));
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
        drohungen = new ArrayList<>();
        List<List<Object>> values;
        try {
            values = SheetUtils.getValueRange("Auftr\u00e4ge", "G4:K54").getValues();
        } catch (IOException e) {
            return;
        }
        if (values == null) {
            return;
        }
        for (List<Object> list : values) {
            drohungen.add(list.get(1).toString());
        }
    }
}
