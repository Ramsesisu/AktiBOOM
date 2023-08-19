package me.rqmses.aktiboom.commands;

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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.AktiBoom.RANK;

@SuppressWarnings("ALL")
public class GeiselnCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "geiseln";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/geiseln (clear)";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Arrays.asList("hostages", "geiselliste");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (args.length == 1) {
            targets = Collections.singletonList("clear");
        }
        Collections.sort(targets);
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

            if (args.length > 0) {
                if (args[0].toLowerCase().equals("clear")) {
                    if (RANK >= 3) {
                        try {
                            SheetUtils.clearValues("Auftr\u00e4ge", "C61:E100");
                            player.sendChatMessage("/f %INFO% :&6" + player.getName() + "&e hat die Geisel-Liste zur\u00fcckgesetzt!");
                        } catch (IOException e) {
                            player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zur Geisel-Liste hergestellt werden!"));
                        }
                    } else {
                        player.sendMessage(new TextComponentString(PREFIX + "Du musst mindestens Rang-3 sein, um die Geisel-Liste zur\u00fcckzusetzen!"));
                    }
                    return;
                }
            }

            try {
                player.sendMessage(new TextComponentString(PREFIX + "Gefangene Geiseln:"));

                List<List<Object>> list = SheetUtils.getValueRange("Auftr\u00e4ge", "C61:E100").getValues();

                int costs = 0;
                for (List<Object> values : list) {
                    int cost = 0;
                    String stringrank = TextFormatting.GRAY + " [" + TextFormatting.YELLOW + values.get(2).toString() + TextFormatting.GRAY + "]";
                    switch (values.get(1).toString().toLowerCase()) {
                        case "rettungsdienst":
                            switch (values.get(2).toString().toLowerCase()) {
                                case "0":
                                    cost += 5000;
                                    break;
                                case "1":
                                    cost += 6000;
                                    break;
                                case "2":
                                    cost += 7000;
                                    break;
                                case "3":
                                    cost += 10000;
                                    break;
                                case "4":
                                    cost += 14000;
                                    break;
                                case "5":
                                    cost += 18000;
                                    break;
                                case "6":
                                    cost += 22000;
                                    break;
                            }
                            break;
                        case "swat":
                            cost += 4000;
                        case "polizei":
                            switch (values.get(2).toString().toLowerCase()) {
                                case "0":
                                    cost += 6000;
                                    break;
                                case "1":
                                    cost += 7000;
                                    break;
                                case "2":
                                    cost += 8000;
                                    break;
                                case "3":
                                    cost += 11000;
                                    break;
                                case "4":
                                    cost += 16000;
                                    break;
                                case "5":
                                    cost += 22000;
                                    break;
                                case "6":
                                    cost += 26000;
                                    break;
                            }
                            break;
                        case "hrt":
                            cost += 4000;
                        case "fbi":
                            switch (values.get(2).toString().toLowerCase()) {
                                case "0":
                                    cost += 6000;
                                    break;
                                case "1":
                                    cost += 7000;
                                    break;
                                case "2":
                                    cost += 8000;
                                    break;
                                case "3":
                                    cost += 12000;
                                    break;
                                case "4":
                                    cost += 18000;
                                    break;
                                case "5":
                                    cost += 24000;
                                    break;
                                case "6":
                                    cost += 28000;
                                    break;
                            }
                            break;
                        default:
                            cost += 4000;
                            stringrank = "";
                    }

                    costs += cost;

                    player.sendMessage(new TextComponentString(TextFormatting.GOLD + "   " + values.get(0).toString() + TextFormatting.DARK_GRAY + " | " + TextFormatting.YELLOW + values.get(1).toString() + stringrank + TextFormatting.DARK_GRAY + " | " + TextFormatting.GRAY + String.valueOf(cost) + "$"));
                }
                player.sendMessage(new TextComponentString(""));

                TextFormatting formatting;
                if (costs < 12000) {
                    formatting = TextFormatting.RED;
                } else {
                    formatting = TextFormatting.GREEN;
                }
                player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Gewinn:" + formatting + " " + TextFormatting.BOLD + costs + "$"));
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zur Geisel-Liste hergestellt werden!"));
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
