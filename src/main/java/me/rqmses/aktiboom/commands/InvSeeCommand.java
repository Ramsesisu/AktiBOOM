package me.rqmses.aktiboom.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
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
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.AktiBoom.RANK;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class InvSeeCommand extends CommandBase implements IClientCommand {

    public static String code = "";
    public static String checkplayer = "";
    public static boolean check = false;

    @Override
    @Nonnull
    public String getName() {
        return "invsee";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/invsee [Name]";
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> targets = new ArrayList<>();
        if (args.length == 1) {
            for (NetworkPlayerInfo playerInfo : Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfoMap()) {
                targets.add(String.valueOf(playerInfo.getGameProfile().getName()));
            }
        }
        for (String target : targets) {
            if (target.toUpperCase().startsWith(args[args.length-1].toUpperCase()))
                list.add(target);
        }
        Collections.sort(targets);
        return list;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            if (args.length > 0) {
                if (RANK >= 4) {
                    checkplayer = args[0];
                    player.sendMessage(new TextComponentString(PREFIX + "Inventar von " + checkplayer + ":"));

                    code = String.valueOf((10000 + (int) (Math.random() * ((99999 - 10000) + 1))));
                    player.sendChatMessage("/f %INV% : " + checkplayer + " : " + code);

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (!InvSeeCommand.check) {
                                player.sendMessage(new TextComponentString(PREFIX + "Das Inventar von " + checkplayer + " konnte nicht erfasst werden!"));
                            } else {
                                InvSeeCommand.check = false;
                            }
                            InvSeeCommand.checkplayer = "";
                            InvSeeCommand.code = "";
                        }
                    }, 3000);
                } else {
                    player.sendMessage(new TextComponentString(PREFIX + "Du hast nicht die ben\u00f6tigten Rechte!"));
                }
            } else {
                player.sendMessage(new TextComponentString(PREFIX + "Gib einen Member an!"));
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