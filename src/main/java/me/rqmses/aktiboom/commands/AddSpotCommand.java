package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.enums.InformationType;
import me.rqmses.aktiboom.utils.LocationUtils;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.*;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class AddSpotCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "addspot";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/addspot [Ort] [Leicht/Mittel/Schwer] [Offensiv/Defensiv] [Flug/Boden] [Explosivwaffen/Standard] [Link] ([Beschreibung...])";
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (args.length == 1) {
            targets = LocationUtils.getLocs();
        }
        if (args.length == 2) {
            targets = Arrays.asList("Leicht", "Mittel", "Schwer");
        }
        if (args.length == 3) {
            targets = Arrays.asList("Defensiv", "Offensiv");
        }
        if (args.length == 4) {
            targets = Arrays.asList("Flug", "Boden");
        }
        if (args.length == 5) {
            targets = Arrays.asList("Standard", "Explosiv");
        }
        if (args.length == 6) {
            targets = Collections.singletonList("-");
        }
        for (String target : targets) {
            if (target.toUpperCase().startsWith(args[args.length-1].toUpperCase()))
                list.add(target);
        }
        return list;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            if (RANK > 3 || SEC) {
                if (args.length > 5) {
                    StringBuilder taktik = new StringBuilder();
                    if (args.length > 6) {
                        for (int i = 6; i < args.length; i++) {
                            taktik.append(args[i]);
                        }
                    }

                    try {
                        SheetUtils.addValues(InformationType.SPOTS.getSheet(), InformationType.SPOTS.getRange(), new String[]{args[0], args[1], args[2], args[3], args[4], args[5], String.valueOf(taktik)});
                        player.sendMessage(new TextComponentString(PREFIX + "Der Spot " + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " wurde in die Spot\u00fcbersicht erg\u00e4nzt."));
                    } catch (IOException e) {
                        player.sendMessage(new TextComponentString(PREFIX + "Der Spot " + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " konnte nicht in die \u00dcbersicht eingetragen werden!"));
                    }
                } else {
                    player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
                }
            } else {
                player.sendMessage(new TextComponentString(PREFIX + "Du bist kein Rang-4 oder Teil des SECs!"));
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