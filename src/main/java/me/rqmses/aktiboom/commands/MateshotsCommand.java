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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class MateshotsCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "mateshots";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/mateshots";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            List<List<Object>> shots;
            try {
                shots = SheetUtils.getValueRange(InformationType.MATESHOTS.getSheet(), InformationType.MATESHOTS.getRange()).getValues();
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Die letzten Mateshots konnten nicht erfasst werden!"));
                return;
            }

            player.sendMessage(new TextComponentString(PREFIX + "Mateshots der letzten Bombe:"));

            HashMap<String, List<String>> mateshots = new HashMap<>();
            for (List<Object> shot : shots) {
                if (mateshots.containsKey(shot.get(0).toString())) {
                    List<String> mates = mateshots.get(shot.get(0).toString());
                    mates.add(shot.get(1).toString());
                    mateshots.put(shot.get(0).toString(), mates);
                } else {
                    List<String> mates = new ArrayList<>();
                    mates.add(shot.get(1).toString());
                    mateshots.put(shot.get(0).toString(), mates);
                }
            }

            for (String shooter : mateshots.keySet()) {
                HashSet<String> mates = new HashSet<>(mateshots.get(shooter));

                player.sendMessage(new TextComponentString(TextFormatting.GOLD + "    " + shooter + TextFormatting.DARK_GRAY + " [" + TextFormatting.YELLOW + mateshots.get(shooter).size() + "x" + TextFormatting.DARK_GRAY + "] (" + TextFormatting.GRAY + mates.toString().replace("[", "").replace("]", "") + TextFormatting.DARK_GRAY + ")"));
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