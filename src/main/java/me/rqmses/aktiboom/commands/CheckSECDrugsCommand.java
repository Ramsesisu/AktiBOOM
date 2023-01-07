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
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        ValueRange valueRange;

        String tempkoks;
        try {
            valueRange = sheetsService.spreadsheets().values()
                    .get(SPREADSHEET_ID, "SEC-Drogen!H5")
                    .execute();
            tempkoks = valueRange.getValues().get(0).get(0).toString();
        } catch (IOException e) {
            player.sendMessage(new TextComponentString(PREFIX + "Das Kokain des" + TextFormatting.GOLD + "SEC" + TextFormatting.YELLOW + "konnte nicht erfasst werden."));
            return;
        }
        String tempgras;
        try {
            valueRange = sheetsService.spreadsheets().values()
                    .get(SPREADSHEET_ID, "SEC-Drogen!I5")
                    .execute();
            tempgras = valueRange.getValues().get(0).get(0).toString();
        } catch (IOException e) {
            player.sendMessage(new TextComponentString(PREFIX + "Das Gras des" + TextFormatting.GOLD + "SEC" + TextFormatting.YELLOW + "konnte nicht erfasst werden."));
            return;
        }

        int koks = Integer.parseInt(tempkoks.replace(" ", "").split("/")[0]);
        int gras = Integer.parseInt(tempgras.replace(" ", "").split("/")[0]);

        player.sendMessage(new TextComponentString(PREFIX + "Drogen des " + TextFormatting.GOLD + "SEC"));
        player.sendMessage(new TextComponentString(""));

        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "Kokain: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Geholt: " + TextFormatting.YELLOW + "" + TextFormatting.BOLD + koks));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   \u00dcbrig:  " + TextFormatting.YELLOW + (250-koks)));
        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "Gras: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Geholt: " + TextFormatting.YELLOW + "" + TextFormatting.BOLD + gras));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   \u00dcbrig:  " + TextFormatting.YELLOW + (250-gras)));
        player.sendMessage(new TextComponentString(""));

        if (koks < 250 || gras < 250) {
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Drogen \u00fcbrig: " + TextFormatting.GREEN + "" + TextFormatting.BOLD + "Ja"));
        } else {
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Drogen \u00fcbrig: " + TextFormatting.RED + "" + TextFormatting.BOLD + "Nein"));
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
