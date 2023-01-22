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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class CheckTuningCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "checktuning";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/checktuning [Name]";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Arrays.asList("checkautobombe", "checkcarbomb", "checkspecialtuning");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> targets = new ArrayList<>();
        if (args.length == 1) {
            for (NetworkPlayerInfo playerInfo : Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfoMap()) {
                targets.add(String.valueOf(playerInfo.getGameProfile().getName()));
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
            String name = args[0];
            try {
                int line = SheetUtils.searchLine("Auftr\u00e4ge", "R4:R54", name) + 4;
                List<Object> list = SheetUtils.getValueRange("Auftr\u00e4ge", "Q"+line+":T"+line).getValues().get(0);
                if (list.get(1).toString().equalsIgnoreCase("Opfer")) {
                    player.sendMessage(new TextComponentString(PREFIX + "Der Spieler befindet sich nicht auf der Autobombenliste."));
                    return;
                }
                player.sendMessage(new TextComponentString(PREFIX + "Autobombe f\u00fcr " + TextFormatting.GOLD + name));
                player.sendMessage(new TextComponentString(TextFormatting.GOLD + list.get(1).toString() + TextFormatting.DARK_GRAY + " | " + TextFormatting.YELLOW + list.get(3).toString() + TextFormatting.DARK_GRAY + " | " + TextFormatting.GRAY + list.get(2).toString() + TextFormatting.GRAY + " (" + list.get(0).toString() + ")"));
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Der Spieler befindet sich nicht auf der Autobombenliste."));
            }
        } else {
            player.sendMessage(new TextComponentString(PREFIX + "Du musst einen Spieler angeben!"));
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