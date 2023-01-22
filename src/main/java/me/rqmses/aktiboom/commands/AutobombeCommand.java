package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

public class AutobombeCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "autobombe";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/autobombe add/done/rename [Name] ([Auftraggeber/Neuer Name]) ([Preis])";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Arrays.asList("specialtuning", "tuning", "carbomb");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (args.length == 1) {
            targets = Arrays.asList("add", "done", "rename");
        }
        if (args.length == 2 || args.length == 3) {
            for (NetworkPlayerInfo playerInfo : Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfoMap()) {
                targets.add(String.valueOf(playerInfo.getGameProfile().getName()));
            }
        }
        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("add")) {
                targets = Arrays.asList("3750");
            }
        }
        for (String target : targets) {
            if (target.toUpperCase().startsWith(args[args.length-1].toUpperCase()))
                list.add(target);
        }
        Collections.sort(targets);
        return list;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "add":
                    if (args.length > 2) {
                        String price = "3750";
                        if (args.length > 3) {
                            price = args[3];
                        }
                        try {
                            SheetUtils.addValues("Auftr\u00e4ge", "Q4:T54", new String[] {new SimpleDateFormat("dd.MM.yy").format(new Date()), args[1], args[2], price});
                            player.sendChatMessage("/f %INFO% :" + player.getName() + " hat eine Autobombe f\u00fcr " + args[1] + " eingetragen.");
                        } catch (IOException e) {
                            player.sendMessage(new TextComponentString(PREFIX + "Die Autobombe konnte nicht eingetragen werden."));
                        }
                    } else {
                        player.sendMessage(new TextComponentString(PREFIX + "/autobombe add [Name] [Auftraggeber] ([Preis])"));
                    }
                    break;
                case "done":
                    if (args.length > 1) {
                        try {
                            int line = SheetUtils.searchLine("Auftr\u00e4ge", "R4:R54", args[1]) + 4;
                            List<Object> list = SheetUtils.getValueRange("Auftr\u00e4ge", "Q"+line+":T"+line).getValues().get(0);
                            if (list.get(1).toString().equalsIgnoreCase("Opfer")) {
                                player.sendMessage(new TextComponentString(PREFIX + "Der Spieler hat keine offene Autobombe."));
                                return;
                            }
                            SheetUtils.clearValues("Auftr\u00e4ge", "Q" + line + ":T" + line);
                            SheetUtils.sortRange("Auftr\u00e4ge", "Q4:T54");
                            player.sendChatMessage("/f %INFO% :" + player.getName() + " hat die Autobombe f\u00fcr " + args[1] + " platziert.");
                        } catch (IOException e) {
                            player.sendMessage(new TextComponentString(PREFIX + "Die Autobombe konnte nicht gel\u00f6scht werden."));
                        }
                    } else {
                        player.sendMessage(new TextComponentString(PREFIX + "/autobombe done [Name]"));
                    }
                    break;
                case "rename":
                    if (args.length > 2) {
                        try {
                            int line = SheetUtils.searchLine("Auftr\u00e4ge", "R4:R54", args[1]) + 4;
                            List<Object> list = SheetUtils.getValueRange("Auftr\u00e4ge", "Q"+line+":T"+line).getValues().get(0);
                            if (list.get(1).toString().equalsIgnoreCase("Opfer")) {
                                player.sendMessage(new TextComponentString(PREFIX + "Der Spieler hat keine offene Autobombe."));
                                return;
                            }
                            SheetUtils.setValues("Auftr\u00e4ge", "R" + line, new String[]{args[2]});
                            player.sendMessage(new TextComponentString(PREFIX + "Der Name von " + TextFormatting.GOLD + args[2] + TextFormatting.YELLOW + " wurde aktualisiert."));
                        } catch (IOException e) {
                            player.sendMessage(new TextComponentString(PREFIX + "Der Name konnte nicht aktualisiert werden."));
                        }
                    } else {
                        player.sendMessage(new TextComponentString(PREFIX + "/autobombe rename [Name] [Neuer Name]"));
                    }
                    break;
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
