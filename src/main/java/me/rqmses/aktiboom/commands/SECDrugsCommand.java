package me.rqmses.aktiboom.commands;

import com.google.api.services.sheets.v4.model.ValueRange;
import me.rqmses.aktiboom.enums.InformationType;
import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.AktiBoom.SECMEMBER;
import static me.rqmses.aktiboom.handlers.SheetHandler.SPREADSHEET_ID;
import static me.rqmses.aktiboom.handlers.SheetHandler.sheetsService;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class SECDrugsCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "secdrugs";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/secdrogen [Pulver-Menge]/clear [Kr\u00e4uter-Menge]";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("secdrogen");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.add("clear");
        }
        if (args.length < 3 && !args[0].equalsIgnoreCase("clear")) {
            list.add("0");
        }
        return list;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("clear")) {
                    if (SECMEMBER.get(player.getName()) > 1) {
                        if (args.length > 1) {
                            if (args[1].equals(player.getName())) {
                                try {
                                    SheetUtils.clearValues(InformationType.SECDRUGS.getSheet(), InformationType.SECDRUGS.getRange());

                                    player.sendChatMessage("/f %INFO% :&6" + player.getName() + "&e hat die SEC-Drogen zur\u00fcckgesetzt!");
                                } catch (IOException e) {
                                    player.sendMessage(new TextComponentString(PREFIX + "Die SEC-Drogen konnten nicht zur\u00fcckgesetzt werden!"));
                                }
                            } else {
                                player.sendMessage(new TextComponentString(PREFIX + "Ein falscher Name wurde zur Best\u00e4tigung angegeben!"));
                            }
                        } else {
                            player.sendMessage(new TextComponentString(PREFIX + "Gib sicherheitshalber deinen eigenen Namen zur Best\u00e4tigung an!"));
                        }
                    } else {
                        player.sendMessage(new TextComponentString(PREFIX + "Du bist kein Teil der SEC-Leitung!"));
                    }
                } else {
                    if (SECMEMBER.containsKey(player.getName())) {
                        if (args.length > 1) {
                            boolean success;
                            try {
                                success = SheetUtils.addSECDrugs(new String[]{new SimpleDateFormat("dd.MM.yy").format(new Date()), player.getName(), args[0], args[1]});
                            } catch (IOException e) {
                                player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden."));
                                return;
                            }

                            if (success) {
                                player.sendMessage(new TextComponentString(PREFIX + "Die SEC-Drogen wurden erfolgreich eingetragen."));
                            } else {
                                player.sendMessage(new TextComponentString(PREFIX + "Die entsprechende Kategorie ist \u00fcberf\u00fcllt."));
                            }
                        } else {
                            player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
                        }
                    } else {
                        player.sendMessage(new TextComponentString(PREFIX + "Du bist kein Mitglied im SEC."));
                    }
                }
            } else {
                ValueRange valueRange;
                String temppulver;
                try {
                    valueRange = sheetsService.spreadsheets().values()
                            .get(SPREADSHEET_ID, "SEC!H5")
                            .execute();
                    temppulver = valueRange.getValues().get(0).get(0).toString();
                } catch (IOException e) {
                    player.sendMessage(new TextComponentString(PREFIX + "Das Pulver des" + TextFormatting.GOLD + " SEC " + TextFormatting.YELLOW + "konnte nicht erfasst werden."));
                    return;
                }
                String tempkraeuter;
                try {
                    valueRange = sheetsService.spreadsheets().values()
                            .get(SPREADSHEET_ID, "SEC!I5")
                            .execute();
                    tempkraeuter = valueRange.getValues().get(0).get(0).toString();
                } catch (IOException e) {
                    player.sendMessage(new TextComponentString(PREFIX + "Die Kr\u00e4uter des" + TextFormatting.GOLD + " SEC " + TextFormatting.YELLOW + "konnten nicht erfasst werden."));
                    return;
                }

                int pulver = Integer.parseInt(temppulver.replace(" ", "").split("/")[0]);
                int kraeuter = Integer.parseInt(tempkraeuter.replace(" ", "").split("/")[0]);

                player.sendMessage(new TextComponentString(PREFIX + "Drogen des " + TextFormatting.GOLD + "SEC" + TextFormatting.YELLOW + ":"));
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
