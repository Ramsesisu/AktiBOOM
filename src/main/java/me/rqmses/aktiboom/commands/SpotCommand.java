package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.enums.InformationType;
import me.rqmses.aktiboom.utils.LocationUtils;
import me.rqmses.aktiboom.utils.SheetUtils;
import me.rqmses.aktiboom.utils.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class SpotCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "spot";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/spot ([Suchparameter 1]) ([Suchparameter 2]) ([...])";
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>(Arrays.asList("Leicht", "Mittel", "Schwer", "Defensiv", "Offensiv", "Boden", "Flug", "Standard", "Explosiv"));
        if (args.length == 1) {
            targets.addAll(LocationUtils.getLocs());
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

            List<List<Object>> spots;
            try {
                spots = SheetUtils.getValueRange(InformationType.SPOTS.getSheet(), "C9:I299").getValues();
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Die Spot\u00fcbersicht konnte nicht erfasst werden!"));
                return;
            }

            List<String> removed = new ArrayList<>();
            for (List<Object> spot : spots) {
                List<String> stats = spot.stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());
                stats.replaceAll(String::toLowerCase);
                for (String arg : args) {
                    if (!stats.contains(arg.toLowerCase())) {
                        removed.add(spot.get(0).toString());
                        break;
                    }
                }
            }

            player.sendMessage(new TextComponentString(PREFIX + "Ergebnis der Spot-Suche:"));
            for (List<Object> spot : spots) {
                if (!removed.contains(spot.get(0).toString())) {
                    player.sendMessage(new TextComponentString(TextFormatting.GOLD + "  " + TextFormatting.BOLD + spot.get(0).toString()));
                    player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "   Schwierigkeit: " + TextFormatting.GRAY + spot.get(1).toString()));
                    player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "   Aufstellung: " + TextFormatting.GRAY + spot.get(2).toString()));
                    player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "   Zugang: " + TextFormatting.GRAY + spot.get(3).toString()));
                    player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "   Ausr\u00fcstung: " + TextFormatting.GRAY + spot.get(4).toString()));
                    String proof = spot.get(5).toString();
                    if (proof.length() > 1) {
                        player.sendMessage(TextUtils.clickable(TextFormatting.YELLOW, "   Beweis: " + TextFormatting.GRAY + proof, TextFormatting.DARK_AQUA + "Beweis", ClickEvent.Action.OPEN_URL, proof));
                    }
                    if (spot.size() > 6) {
                        player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "   Taktik: " + TextFormatting.GRAY + spot.get(6).toString()));
                    }
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