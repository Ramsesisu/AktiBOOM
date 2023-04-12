package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.InformationUtils;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class GrossaktiCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "gro\u00dfakti";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/gro\u00dfakti start/end";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("operation");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (args.length == 1) {
            targets = Arrays.asList("start", "end");
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

            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "start":
                        try {
                            InformationUtils.clearOperation();
                        } catch (IOException e) {
                            player.sendMessage(new TextComponentString(PREFIX + "Die Statistiken konnten nicht zur\u00fcckgesetzt werden!"));
                        }
                    case "end":
                        player.sendChatMessage("/f %OPERATION% : " + args[0].toLowerCase());
                        break;
                    default:
                        player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
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