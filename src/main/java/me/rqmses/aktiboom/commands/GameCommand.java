package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.GameUtils;
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
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

public class GameCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "game";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/game [Spiel]/end [Mitspieler-1] ([Mitspieler-2]) ...";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("spiel");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (args.length == 1) {
            if (GameUtils.party) {
                targets = Collections.singletonList("end");
            } else {
                targets = Arrays.asList("Schach", "TicTacToe");
            }
        }
        if (args.length > 1) {
            if (!GameUtils.party) {
                for (NetworkPlayerInfo playerInfo : Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfoMap()) {
                    targets.add(String.valueOf(playerInfo.getGameProfile().getName()));
                }
            }
        }
        for (String target : targets) {
            if (target.toUpperCase().startsWith(args[args.length-1].toUpperCase()))
                list.add(target);
        }
        Collections.sort(list);
        return list;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("end")) {
                player.sendChatMessage(": %PARTY% : end : " + GameUtils.players.toString().replace("[", "").replace("]", "").replace(",", ""));
                return;
            }
            if (args.length > 1) {
                String category;
                switch (args[0].toLowerCase()) {
                    case "schach":
                        category = "Schach";
                        break;
                    case "tictactoe":
                        category = "TicTacToe";
                        break;
                    default:
                        player.sendMessage(new TextComponentString(PREFIX + "Das Spiel " + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " wurde nicht gefunden!"));
                        return;
                }

                StringBuilder players = new StringBuilder().append(player.getName());
                for (String arg : Arrays.asList(args).subList(1, args.length)) {
                    players.append(" ").append(arg);
                }

                player.sendChatMessage(": %PARTY% : " + category + " : " + players);
            } else {
                player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
            }
        } else {
            if (GameUtils.party) {
                GameUtils.display();
            } else {
                player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
            }
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
