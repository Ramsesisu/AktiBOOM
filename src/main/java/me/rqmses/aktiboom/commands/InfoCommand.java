package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.enums.InformationType;
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

import static me.rqmses.aktiboom.AktiBoom.*;

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
        return "/info ([Name])";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Arrays.asList("information", "informationen");
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
            int rank;
            String secrank;
            if (args.length > 0) {
                name = args[0];
                rank = SheetUtils.getRank(args[0]);
                secrank = SheetUtils.getSECRank(args[0]);
            } else {
                name = player.getName();
                rank = RANK;
                secrank = SECRANK;
            }

            List<String> sperren = new ArrayList<>();
            try {
                if (SheetUtils.getValueRange(InformationType.SPRENGGUERTEL_BAN.getSheet(), InformationType.SPRENGGUERTEL_BAN.getRange().replaceFirst("2", "1")).getValues().toString().contains(name)) {
                    sperren.add("Sprengg\u00fcrtel");
                }
                if (SheetUtils.getValueRange(InformationType.RPG_7_BAN.getSheet(), InformationType.RPG_7_BAN.getRange().replaceFirst("2", "1")).getValues().toString().contains(name)) {
                    sperren.add("RPG-7");
                }
            } catch (IOException ignored) {
            }

            player.sendMessage(new TextComponentString(PREFIX + "Informationen \u00fcber " + TextFormatting.GOLD + name + TextFormatting.YELLOW + ":"));

            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "  Name: " + TextFormatting.YELLOW + name));

            String rankname = "Rekrut";
            switch (rank) {
                case -1:
                    rankname = "Nicht in der Fraktion";
                    break;
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

            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "  Rang: " + TextFormatting.YELLOW + rankname + " (" + rank + ")"));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "  SEC: " + TextFormatting.YELLOW + secrank));

            StringBuilder stringsperren = new StringBuilder();
            if (sperren.size() > 0) {
                for (int i = 0; i < sperren.size(); i++) {
                    if (i == 0) {
                        stringsperren.append(TextFormatting.YELLOW).append(sperren.get(0));
                    } else {
                        stringsperren.append(TextFormatting.GRAY).append(", ").append(TextFormatting.YELLOW).append(sperren.get(i));
                    }
                }
            } else {
                stringsperren.append(TextFormatting.YELLOW).append("-");
            }

            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "  Sperren: " + stringsperren));
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