package me.rqmses.aktiboom.commands;

import com.google.api.services.sheets.v4.model.ValueRange;
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
import static me.rqmses.aktiboom.handlers.SheetHandler.SPREADSHEET_ID;
import static me.rqmses.aktiboom.handlers.SheetHandler.sheetsService;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class CheckSECDrugsCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "checksecdrugs";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/checksecdrugs";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("checksecdrogen");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            ValueRange valueRange;

            String temppulver;
            try {
                valueRange = sheetsService.spreadsheets().values()
                        .get(SPREADSHEET_ID, "SEC-Drogen!H5")
                        .execute();
                temppulver = valueRange.getValues().get(0).get(0).toString();
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Das Pulver des" + TextFormatting.GOLD + "SEC" + TextFormatting.YELLOW + "konnte nicht erfasst werden."));
                return;
            }
            String tempkraeuter;
            try {
                valueRange = sheetsService.spreadsheets().values()
                        .get(SPREADSHEET_ID, "SEC-Drogen!I5")
                        .execute();
                tempkraeuter = valueRange.getValues().get(0).get(0).toString();
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Die Kr\u00e4uter des" + TextFormatting.GOLD + "SEC" + TextFormatting.YELLOW + "konnten nicht erfasst werden."));
                return;
            }

            int pulver = Integer.parseInt(temppulver.replace(" ", "").split("/")[0]);
            int kraeuter = Integer.parseInt(tempkraeuter.replace(" ", "").split("/")[0]);

            player.sendMessage(new TextComponentString(PREFIX + "Drogen des " + TextFormatting.GOLD + "SEC"));
            player.sendMessage(new TextComponentString(""));

            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Pulver: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Geholt: " + TextFormatting.YELLOW + "" + TextFormatting.BOLD + pulver));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   \u00dcbrig:  " + TextFormatting.YELLOW + (350 - pulver)));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Kr\u00e4uter: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Geholt: " + TextFormatting.YELLOW + "" + TextFormatting.BOLD + kraeuter));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   \u00dcbrig:  " + TextFormatting.YELLOW + (350 - kraeuter)));
            player.sendMessage(new TextComponentString(""));

            if (pulver < 350 || kraeuter < 350) {
                player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Drogen \u00fcbrig: " + TextFormatting.GREEN + "" + TextFormatting.BOLD + "Ja"));
            } else {
                player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Drogen \u00fcbrig: " + TextFormatting.RED + "" + TextFormatting.BOLD + "Nein"));
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
