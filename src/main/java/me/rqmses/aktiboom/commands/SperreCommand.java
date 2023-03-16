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

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class SperreCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "sperre";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/sperre [Kategorie] [Member]";
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (args.length == 1) {
            targets = Arrays.asList("Sprengg\u00fcrtel", "RPG-7");
        }
        if (args.length == 2) {
            for (NetworkPlayerInfo playerInfo : Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfoMap()) {
                targets.add(String.valueOf(playerInfo.getGameProfile().getName()));
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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            if (args.length > 1) {
                try {
                    if (SheetUtils.getValueRange(InformationType.SPERRE_PERMISSION.getSheet(), InformationType.SPERRE_PERMISSION.getRange()).toString().contains(player.getName())) {
                        InformationType informationType;

                        switch (args[0].toLowerCase()) {
                            case "sprengg\u00fcrtel":
                                informationType = InformationType.SPRENGGUERTEL_BAN;
                                break;
                            case "rpg-7":
                                informationType = InformationType.RPG_7_BAN;
                                break;
                            default:
                                player.sendMessage(new TextComponentString(PREFIX + "Eine " + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + "-Sperre existiert nicht!"));
                                return;
                        }

                        try {
                            if (SheetUtils.getValueRange(informationType.getSheet(), informationType.getRange()).toString().contains(args[1])) {
                                int line = SheetUtils.searchLine(informationType.getSheet(), informationType.getRange(), args[1]) + 2;
                                String column = String.valueOf(informationType.getRange().charAt(0));
                                SheetUtils.clearValues(informationType.getSheet(), column + line + ":" + column + line);

                                player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " wurde f\u00fcr" + TextFormatting.GOLD + " " + TextFormatting.BOLD + args[1] + TextFormatting.YELLOW + " wieder entsperrt."));

                                SheetUtils.sortRange(informationType.getSheet(), informationType.getRange());
                            } else {
                                SheetUtils.addValues(informationType.getSheet(), informationType.getRange(), new String[]{args[1]});

                                player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " wurde f\u00fcr" + TextFormatting.GOLD + " " + TextFormatting.BOLD + args[1] + TextFormatting.YELLOW + " gesperrt."));
                            }
                        } catch (IOException e) {
                            player.sendMessage(new TextComponentString(PREFIX + "Die Sperre konnten nicht hinzugef\u00fcgt werden!"));
                        }
                    } else {
                        player.sendMessage(new TextComponentString(PREFIX + "Du hast nicht die ben\u00f6tigten Rechte!"));
                    }
                } catch (IOException e) {
                    player.sendMessage(new TextComponentString(PREFIX + "Die Sperre-Rechte konnten nicht erfasst werden!"));
                }
            } else {
                player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
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