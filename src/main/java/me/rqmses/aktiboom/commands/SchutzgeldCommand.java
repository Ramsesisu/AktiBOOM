package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;
import org.apache.commons.lang3.time.DateUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("ALL")
public class SchutzgeldCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "schutzgeld";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/schutzgeld add/remove/rename [Name] ([Preis/Neuer Name]) ([Ablaufdatum])";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("protection");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (args.length == 1) {
            targets = Arrays.asList("add", "remove", "rename");
        }
        if (args.length == 2) {
            for (NetworkPlayerInfo playerInfo : Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfoMap()) {
                targets.add(String.valueOf(playerInfo.getGameProfile().getName()));
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("add")) {
                targets = Collections.singletonList("5000");
            }
            if (args[0].equalsIgnoreCase("rename")) {
                for (NetworkPlayerInfo playerInfo : Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfoMap()) {
                    targets.add(String.valueOf(playerInfo.getGameProfile().getName()));
                }
            }
        }
        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("add")) {
                targets = Collections.singletonList(new SimpleDateFormat("dd.MM.yy").format(DateUtils.addDays(new Date(), 7)));
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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "add":
                    if (args.length > 3) {
                        try {
                            SheetUtils.addValues("Auftr\u00e4ge", "M4:O54", new String[] {args[1], args[2], args[3]});
                            player.sendChatMessage("/f %INFO% :" + player.getName() + " hat ein Schutzgeld von &6&l" + args[1] + "&e eingetragen.");
                        } catch (IOException e) {
                            player.sendMessage(new TextComponentString(PREFIX + "Das Schutzgeld konnte nicht eingetragen werden."));
                        }
                    } else {
                        player.sendMessage(new TextComponentString(PREFIX + "/schutzgeld add [Name] [Preis] [Ablaufdatum]"));
                    }
                    break;
                case "remove":
                    if (args.length > 1) {
                        try {
                            int line = SheetUtils.searchLine("Auftr\u00e4ge", "M4:M54", args[1]) + 4;
                            List<Object> list = SheetUtils.getValueRange("Auftr\u00e4ge", "M"+line+":O"+line).getValues().get(0);
                            if (list.get(1).toString().equalsIgnoreCase("Name")) {
                                player.sendMessage(new TextComponentString(PREFIX + "Der Spieler hat kein Schutzgeld."));
                                return;
                            }
                            SheetUtils.clearValues("Auftr\u00e4ge", "M" + line + ":O" + line);
                            SheetUtils.sortRange("Auftr\u00e4ge", "M4:O54");
                            player.sendChatMessage("/f %INFO% :" + player.getName() + " hat das Schutzgeld von &6&l" + args[1] + "&e gel\u00f6scht.");
                        } catch (IOException e) {
                            player.sendMessage(new TextComponentString(PREFIX + "Das Schutzgeld konnte nicht gel\u00f6scht werden."));
                        }
                    } else {
                        player.sendMessage(new TextComponentString(PREFIX + "/schutzgeld remove [Name]"));
                    }
                    break;
                case "rename":
                    if (args.length > 2) {
                        try {
                            int line = SheetUtils.searchLine("Auftr\u00e4ge", "M4:M54", args[1]) + 4;
                            List<Object> list = SheetUtils.getValueRange("Auftr\u00e4ge", "M"+line+":O"+line).getValues().get(0);
                            if (list.get(1).toString().equalsIgnoreCase("Name")) {
                                player.sendMessage(new TextComponentString(PREFIX + "Der Spieler befindet sich nicht auf der Auslieferungsliste."));
                                return;
                            }
                            SheetUtils.setValues("Auftr\u00e4ge", "M" + line, new String[]{args[2]});
                            player.sendMessage(new TextComponentString(PREFIX + "Der Name von " + TextFormatting.GOLD + args[2] + TextFormatting.YELLOW + " wurde aktualisiert."));
                        } catch (IOException e) {
                            player.sendMessage(new TextComponentString(PREFIX + "Der Name konnte nicht aktualisiert werden."));
                        }
                    } else {
                        player.sendMessage(new TextComponentString(PREFIX + "/auftragsauslieferung rename [Name] [Neuer Name]"));
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
