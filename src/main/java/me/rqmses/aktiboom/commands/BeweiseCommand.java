package me.rqmses.aktiboom.commands;

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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("ALL")
public class BeweiseCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "beweise";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/beweise [Spot]";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("proof");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (args.length == 1) {
            targets = Arrays.asList("144", "127", "196", "189", "185", "340", "36", "Luigis", "433", "113", "347", "Triaden-HQ", "50", "Brauerei", "137");
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
                List<String> links = new ArrayList<>();
                switch (args[0].toLowerCase()) {
                    case "144":
                        links.add("https://imgur.com/a/54bCKmA");
                        links.add("https://youtu.be/800QWx1JwU0");
                        break;
                    case "127":
                        links.add("https://imgur.com/a/00AHtOq");
                        break;
                    case "196":
                        links.add("https://imgur.com/a/vWDDDCm");
                        break;
                    case "189":
                        links.add("https://imgur.com/a/W5oRm7P");
                        break;
                    case "185":
                        links.add("https://imgur.com/a/054rDBR");
                        break;
                    case "340":
                        links.add("https://imgur.com/a/YKdB6qL");
                        break;
                    case "433":
                        links.add("https://youtu.be/bzYSzDCtUlo");
                        links.add("https://youtu.be/9bA10WxhyAE");
                        break;
                    case "36":
                        links.add("https://youtu.be/8GcwIEPJkAo");
                        links.add("https://youtu.be/We7YHiL70CM");
                        break;
                    case "luigis":
                        links.add("https://youtu.be/bEtA4FkxEFU");
                        break;
                    case "113":
                        links.add("https://imgur.com/a/BFYNcSU");
                        break;
                    case "347":
                        links.add("https://imgur.com/a/OQ1Zk1O");
                        break;
                    case "triaden-hq":
                        links.add("https://imgur.com/a/AmOZpsk");
                        break;
                    case "50":
                        links.add("https://imgur.com/a/gEOemNy");
                        break;
                    case "brauerei":
                        links.add("https://youtu.be/DQxHKIcpcxg");
                        break;
                    case "137":
                        links.add("https://youtu.be/g6ke5IcXvZU");
                        links.add("https://youtu.be/eSdpPll3jE0");
                    default:
                        player.sendMessage(new TextComponentString(PREFIX + "Der Spot hat keine verf\u00fcgbaren Beweise."));
                        return;
                }
                player.sendMessage(new TextComponentString(PREFIX + "Beweise zum Spot " + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + ":"));
                for (String link : links) {
                    player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "      \u27A5 " + TextFormatting.BLUE + link, TextFormatting.DARK_AQUA + "Link", ClickEvent.Action.OPEN_URL, link));
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
