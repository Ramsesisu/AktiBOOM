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
import java.util.Arrays;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.handlers.SheetHandler.checkConnection;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class InfoCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "info";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/info";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Arrays.asList("information", "informationen");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        String name;
        if (args.length > 0) {
            name = args[0];
        } else {
            name = player.getName();
        }

        player.sendMessage(new TextComponentString(PREFIX + "Informationen \u00fcber " + TextFormatting.DARK_GRAY + TextFormatting.GOLD + player.getName()));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Name: " + TextFormatting.YELLOW + player.getName()));

        int rank;
        try {
            rank = SheetUtils.getRank(name);
        } catch (IOException e) {
            rank = 0;
        }

        String rankname = "Rekrut";
        switch (rank) {
            case 1:
                rankname = "Feldwebel";
                break;
            case 2:
                rankname = "Leutnant";
                break;
            case 3:
                rankname = "Hauptmann";
                break;
            case 4:
                rankname = "Major";
                break;
            case 5:
                rankname = "General";
                break;
            case 6:
                rankname = "Kommandant";
                break;
        }
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Rang: " + TextFormatting.YELLOW + rankname + " (" + rank + ")"));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "SEC: " + TextFormatting.YELLOW + "Unbekannt")); // Wird in Zukunft implementiert
        if (checkConnection()) {
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Status: " + TextFormatting.GREEN + "Verbunden"));
        } else {
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Status: " + TextFormatting.RED + "Getrennt"));
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