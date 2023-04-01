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
public class CheckAktisCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "checkaktis";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/checkaktis ([Name])";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Arrays.asList("checkaktivit\u00e4ten", "aktis", "aktivit\u00e4ten");
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

            int rank = SheetUtils.getRank(name);

            int minamount = 0;
            int minincome = 0;
            int minroleplay = 0;
            int minleading = 0;
            switch (rank) {
                case -1:
                    player.sendMessage(new TextComponentString(PREFIX + "Dieser Spieler ist nicht in der Fraktion."));
                    return;
                case 0:
                    minamount = 4;
                    minincome = 4000;
                    minroleplay = 3;
                    break;
                case 1:
                    minamount = 5;
                    minincome = 6000;
                    minroleplay = 3;
                    break;
                case 2:
                    minamount = 5;
                    minincome = 8000;
                    minroleplay = 3;
                    minleading = 1;
                    break;
                case 3:
                    minamount = 6;
                    minincome = 10000;
                    minroleplay = 3;
                    minleading = 1;
                    break;
                case 4:
                    minincome = 12000;
                    break;
            }

            if (SheetUtils.isSEC(name) && rank < 4) {
                minleading = 3;
                minroleplay = 0;
            }

            List<List<Object>> values;
            try {
                values = SheetUtils.getValueRange(name, "B4:D27").getValues();
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden."));
                return;
            }

            player.sendMessage(new TextComponentString(PREFIX + "Aktivit\u00e4ten von " + TextFormatting.DARK_GRAY + TextFormatting.GOLD + name + TextFormatting.YELLOW + ":"));
            player.sendMessage(new TextComponentString(""));

            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Gebietseinahmen: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + values.get(2).get(1)));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + values.get(2).get(2)));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Entf\u00fchrungen: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + values.get(3).get(1)));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + values.get(3).get(2)));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Geiselnahmen: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + values.get(4).get(1)));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + values.get(4).get(2)));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Bomben: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + values.get(5).get(1)));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + "-"));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Sprengg\u00fcrtel: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + values.get(6).get(1)));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + values.get(6).get(2)));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Menschenh./Ausraub: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + values.get(7).get(1)));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + values.get(7).get(2)));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Autobomben: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + values.get(8).get(1)));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + values.get(8).get(2)));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Trainings: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + values.get(9).get(1)));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + "-"));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Sonstiges: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + values.get(10).get(1)));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + values.get(10).get(2)));
            player.sendMessage(new TextComponentString(TextFormatting.DARK_GRAY + "-------------------------"));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "" + TextFormatting.BOLD + "Gesamt: "));
            String tempamount = values.get(17).get(0).toString();
            String tempincome = values.get(14).get(0).toString();
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + tempamount));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + tempincome));
            player.sendMessage(new TextComponentString(""));
            int amount = Integer.parseInt(tempamount);
            int income = Integer.parseInt(tempincome.replace(".", "").replace("$", ""));

            String temproleplay = values.get(23).get(0).toString();
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "RolePlays: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + temproleplay));
            int roleplay = Integer.parseInt(temproleplay);
            String templeading = values.get(20).get(0).toString();
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Leitungen: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + templeading));
            int leading = Integer.parseInt(templeading);
            player.sendMessage(new TextComponentString(""));

            if (amount >= minamount && income >= minincome && roleplay >= minroleplay && leading >= minleading) {
                player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Mindestaktivit\u00e4ten: " + TextFormatting.GREEN + "" + TextFormatting.BOLD + "Ja"));
            } else {
                player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Mindestaktivit\u00e4ten: " + TextFormatting.RED + "" + TextFormatting.BOLD + "Nein"));
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