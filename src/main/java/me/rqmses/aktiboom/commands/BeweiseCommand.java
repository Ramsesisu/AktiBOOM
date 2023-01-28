package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
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
            targets = Arrays.asList("144", "127", "196", "189", "185", "340", "36", "Luigis", "433", "133", "347", "Triaden-HQ", "50");
        }
        Collections.sort(targets);
        for (String target : targets) {
            if (target.toUpperCase().startsWith(args[args.length-1].toUpperCase()))
                list.add(target);
        }
        return list;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "144":
                    player.sendMessage(new TextComponentString(PREFIX + "Beweise zum Spot " + args[0] + ":"));
                    player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "      \u27A5 " + TextFormatting.BLUE + "https://imgur.com/a/54bCKmA", TextFormatting.DARK_AQUA + "Link", ClickEvent.Action.OPEN_URL, "https://imgur.com/a/54bCKmA"));
                    player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "      \u27A5 " + TextFormatting.BLUE + "https://youtu.be/800QWx1JwU0", TextFormatting.DARK_AQUA + "Link", ClickEvent.Action.OPEN_URL, "https://youtu.be/800QWx1JwU0"));
                    break;
                case "127":
                    player.sendMessage(new TextComponentString(PREFIX + "Beweise zum Spot " + args[0] + ":"));
                    player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "      \u27A5 " + TextFormatting.BLUE + "https://imgur.com/a/00AHtOq", TextFormatting.DARK_AQUA + "Link", ClickEvent.Action.OPEN_URL, "https://imgur.com/a/00AHtOq"));
                    break;
                case "196":
                    player.sendMessage(new TextComponentString(PREFIX + "Beweise zum Spot " + args[0] + ":"));
                    player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "      \u27A5 " + TextFormatting.BLUE + "https://imgur.com/a/vWDDDCm", TextFormatting.DARK_AQUA + "Link", ClickEvent.Action.OPEN_URL, "https://imgur.com/a/vWDDDCm"));
                    break;
                case "189":
                    player.sendMessage(new TextComponentString(PREFIX + "Beweise zum Spot " + args[0] + ":"));
                    player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "      \u27A5 " + TextFormatting.BLUE + "https://imgur.com/a/W5oRm7P", TextFormatting.DARK_AQUA + "Link", ClickEvent.Action.OPEN_URL, "https://imgur.com/a/W5oRm7P"));
                    break;
                case "185":
                    player.sendMessage(new TextComponentString(PREFIX + "Beweise zum Spot " + args[0] + ":"));
                    player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "      \u27A5 " + TextFormatting.BLUE + "https://imgur.com/a/054rDBR", TextFormatting.DARK_AQUA + "Link", ClickEvent.Action.OPEN_URL, "https://imgur.com/a/054rDBR"));
                    break;
                case "340":
                    player.sendMessage(new TextComponentString(PREFIX + "Beweise zum Spot " + args[0] + ":"));
                    player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "      \u27A5 " + TextFormatting.BLUE + "https://imgur.com/a/YKdB6qL", TextFormatting.DARK_AQUA + "Link", ClickEvent.Action.OPEN_URL, "https://imgur.com/a/YKdB6qL"));
                    break;
                case "433":
                    player.sendMessage(new TextComponentString(PREFIX + "Beweise zum Spot " + args[0] + ":"));
                    player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "      \u27A5 " + TextFormatting.BLUE + "https://youtu.be/bzYSzDCtUlo", TextFormatting.DARK_AQUA + "Link", ClickEvent.Action.OPEN_URL, "https://youtu.be/bzYSzDCtUlo"));
                    player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "      \u27A5 " + TextFormatting.BLUE + "https://youtu.be/9bA10WxhyAE", TextFormatting.DARK_AQUA + "Link", ClickEvent.Action.OPEN_URL, "https://youtu.be/9bA10WxhyAE"));
                    break;
                case "36":
                    player.sendMessage(new TextComponentString(PREFIX + "Beweise zum Spot " + args[0] + ":"));
                    player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "      \u27A5 " + TextFormatting.BLUE + "https://youtu.be/8GcwIEPJkAo", TextFormatting.DARK_AQUA + "Link", ClickEvent.Action.OPEN_URL, "https://youtu.be/8GcwIEPJkAo"));
                    player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "      \u27A5 " + TextFormatting.BLUE + "https://youtu.be/We7YHiL70CM", TextFormatting.DARK_AQUA + "Link", ClickEvent.Action.OPEN_URL, "https://youtu.be/We7YHiL70CM"));
                    break;
                case "luigis":
                    player.sendMessage(new TextComponentString(PREFIX + "Beweise zum Spot " + args[0] + ":"));
                    player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "      \u27A5 " + TextFormatting.BLUE + "https://youtu.be/bEtA4FkxEFU", TextFormatting.DARK_AQUA + "Link", ClickEvent.Action.OPEN_URL, "https://youtu.be/bEtA4FkxEFU"));
                    break;
                case "133":
                    player.sendMessage(new TextComponentString(PREFIX + "Beweise zum Spot " + args[0] + ":"));
                    player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "      \u27A5 " + TextFormatting.BLUE + "https://imgur.com/a/BFYNcSU", TextFormatting.DARK_AQUA + "Link", ClickEvent.Action.OPEN_URL, "https://imgur.com/a/BFYNcSU"));
                    break;
                case "347":
                    player.sendMessage(new TextComponentString(PREFIX + "Beweise zum Spot " + args[0] + ":"));
                    player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "      \u27A5 " + TextFormatting.BLUE + "https://imgur.com/a/OQ1Zk1O", TextFormatting.DARK_AQUA + "Link", ClickEvent.Action.OPEN_URL, "https://imgur.com/a/OQ1Zk1O"));
                    break;
                case "triaden-hq":
                    player.sendMessage(new TextComponentString(PREFIX + "Beweise zum Spot " + args[0] + ":"));
                    player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "      \u27A5 " + TextFormatting.BLUE + "https://imgur.com/a/AmOZpsk", TextFormatting.DARK_AQUA + "Link", ClickEvent.Action.OPEN_URL, "https://imgur.com/a/AmOZpsk"));
                    break;
                case "50":
                    player.sendMessage(new TextComponentString(PREFIX + "Beweise zum Spot " + args[0] + ":"));
                    player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "      \u27A5 " + TextFormatting.BLUE + "https://imgur.com/a/gEOemNy", TextFormatting.DARK_AQUA + "Link", ClickEvent.Action.OPEN_URL, "https://imgur.com/a/gEOemNy"));
                    break;
                default:
                    player.sendMessage(new TextComponentString(PREFIX + "Der Spot hat keine verf\u00fcgbaren Beweise."));
            }
        } else {
            player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
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
