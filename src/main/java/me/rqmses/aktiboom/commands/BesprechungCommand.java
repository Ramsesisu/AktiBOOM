package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.enums.InformationType;
import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class BesprechungCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "besprechung";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/besprechung [Datum: dd.MM.yyyy] [Uhrzeit: HH:mm]";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("meeting");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (args.length == 1) {
            targets = Collections.singletonList(new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
        }
        if (args.length == 2) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.MINUTE, 0);
            targets = Collections.singletonList(new SimpleDateFormat("HH:mm").format(calendar.getTime()));
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

            if (args.length > 1) {
                try {
                    SheetUtils.setLine(InformationType.MEETING.getSheet(), InformationType.MEETING.getRange(), new String[]{args[0] + " " + args[1]});
                } catch (IOException e) {
                    player.sendMessage(new TextComponentString(PREFIX + "Die Besprechung konnte nicht eingetragen werden!"));
                    return;
                }

                player.sendMessage(new TextComponentString(PREFIX + "Die Besprechung wurde erfolgreich eingetragen."));
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