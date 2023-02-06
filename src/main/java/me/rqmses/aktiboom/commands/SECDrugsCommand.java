package me.rqmses.aktiboom.commands;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.AktiBoom.SEC;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class SECDrugsCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "secdrugs";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/secdrugs [Kokain-Menge] [Gras-Menge]";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("secdrogen");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        if (args.length < 3) {
            list.add("0");
        }
        return list;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            if (args.length < 2) {
                player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
                return;
            }
            if (!SEC) {
                player.sendMessage(new TextComponentString(PREFIX + "Du bist kein Mitglied im SEC."));
                return;
            }

            boolean success;
            try {
                success = SheetUtils.addSECDrugs(new String[]{new SimpleDateFormat("dd.MM.yy").format(new Date()), player.getName(), args[0], args[1]});
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden."));
                return;
            }

            if (success) {
                player.sendMessage(new TextComponentString(PREFIX + "Die SEC-Drogen wurden erfolgreich eingetragen."));
            } else {
                player.sendMessage(new TextComponentString(PREFIX + "Die entsprechende Kategorie ist \u00fcberf\u00fcllt."));
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
