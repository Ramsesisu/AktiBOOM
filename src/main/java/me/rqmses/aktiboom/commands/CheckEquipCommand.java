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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class CheckEquipCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "checkequip";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/checkequip ([Name])";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("equipment");
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
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            String name;
            if (args.length > 0) {
                name = args[0];
            } else {
                name = player.getName();
            }

            if (SheetUtils.getRank(name) == -1) {
                player.sendMessage(new TextComponentString(PREFIX + "Dieser Spieler ist nicht in der Fraktion."));
                return;
            }

            List<List<Object>> values;
            try {
                values = SheetUtils.getValueRange(name, "F4:H18").getValues();
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden."));
                return;
            }

            player.sendMessage(new TextComponentString(PREFIX + "EquipLog von " + TextFormatting.DARK_GRAY + TextFormatting.GOLD + name + TextFormatting.YELLOW + ":"));
            player.sendMessage(new TextComponentString(""));

            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "MP5: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl: " + TextFormatting.YELLOW + values.get(2).get(1)));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Kosten: " + TextFormatting.YELLOW + values.get(2).get(2)));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Sprengg\u00fcrtel: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl: " + TextFormatting.YELLOW + values.get(3).get(1)));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Kosten: " + TextFormatting.YELLOW + values.get(3).get(2)));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Pistole: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl: " + TextFormatting.YELLOW + values.get(4).get(1)));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Kosten: " + TextFormatting.YELLOW + values.get(4).get(2)));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Kevlar: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl: " + TextFormatting.YELLOW + values.get(5).get(1)));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Kosten: " + TextFormatting.YELLOW + values.get(5).get(2)));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Schwere Kevlar: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl: " + TextFormatting.YELLOW + values.get(6).get(1)));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Kosten: " + TextFormatting.YELLOW + values.get(6).get(2)));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "RPG-7: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl: " + TextFormatting.YELLOW + values.get(7).get(1)));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Kosten: " + TextFormatting.YELLOW + values.get(7).get(2)));
            player.sendMessage(new TextComponentString(TextFormatting.DARK_GRAY + "-------------------------"));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "" + TextFormatting.BOLD + "Gesamt: "));
            String tempdifferenz = values.get(10).get(2).toString();
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Equipkosten: " + TextFormatting.YELLOW + values.get(8).get(2)));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Eingezahlt:  " + TextFormatting.YELLOW + values.get(9).get(2)));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Differenz:   " + TextFormatting.YELLOW + tempdifferenz));
            player.sendMessage(new TextComponentString(""));

            if (Integer.parseInt(tempdifferenz.replace(".", "").replace("$", "")) <= 0) {
                player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Nachzahlungen: " + TextFormatting.GREEN + "" + TextFormatting.BOLD + "Nein"));
            } else {
                player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Nachzahlungen: " + TextFormatting.RED + "" + TextFormatting.BOLD + "Ja"));
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