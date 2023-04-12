package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.enums.InformationType;
import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class KillsCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "kills";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/kills";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            List<List<Object>> kills;
            List<List<Object>> logs;
            try {
                kills = SheetUtils.getValueRange(InformationType.KILLS.getSheet(), InformationType.KILLS.getRange()).getValues();
                logs = SheetUtils.getValueRange(InformationType.KILLS_LOG.getSheet(), InformationType.KILLS_LOG.getRange()).getValues();
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Die letzten Kills konnten nicht erfasst werden!"));
                return;
            }

            player.sendMessage(new TextComponentString(PREFIX + "Kills der letzten Gro\u00dfaktivit\u00e4t:"));

            HashMap<String, Integer> killamounts = new HashMap<>();
            for (List<Object> log : logs) {
                String name = log.get(0).toString();

                killamounts.putIfAbsent(name, 0);
                killamounts.put(name, killamounts.get(name) + 1);
            }

            HashMap<String, List<String>> killlists = new HashMap<>();
            for (List<Object> kill : kills) {
                String name = kill.get(0).toString();

                killlists.putIfAbsent(name, new ArrayList<>(Collections.singletonList("-")));
                List<String> list = killlists.get(name);
                if (!list.contains(kill.get(1).toString())) {
                    list.add(kill.get(1).toString());
                }
                killlists.put(name, list);
            }

            for (String name : killamounts.keySet()) {
                killlists.putIfAbsent(name, new ArrayList<>(Collections.singletonList("-")));
                player.sendMessage(new TextComponentString(TextFormatting.GOLD + "    " + name + TextFormatting.DARK_GRAY + " [" + TextFormatting.YELLOW + killamounts.get(name) + "x" + TextFormatting.DARK_GRAY + "] " + TextFormatting.DARK_GRAY + "(" + TextFormatting.GRAY + killlists.get(name).toString().replace("[", "").replace("]", "").replace("-, ", "") + TextFormatting.DARK_GRAY + ")"));
            }}).start();
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