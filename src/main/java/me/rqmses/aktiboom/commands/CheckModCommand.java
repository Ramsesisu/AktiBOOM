package me.rqmses.aktiboom.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.*;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class CheckModCommand extends CommandBase implements IClientCommand {

    public static String code = "";
    public static String checkplayer = "";
    public static boolean check = false;

    @Override
    @Nonnull
    public String getName() {
        return "checkmod";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/checkmod [Name]";
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
                if (MEMBER.get(player.getName()) >= 4) {
                    checkplayer = args[0];
                    player.sendMessage(new TextComponentString(PREFIX + "Mod-Infos von " + checkplayer + ":"));

                    code = String.valueOf((10000 + (int) (Math.random() * ((99999 - 10000) + 1))));
                    player.sendChatMessage("/f %CHECK% : " + checkplayer + " : " + code);

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (!CheckModCommand.check) {
                                player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Status: " + TextFormatting.RED + "Nicht installiert"));
                                player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Version: " + TextFormatting.YELLOW + "< 1.7.4"));
                            } else {
                                CheckModCommand.check = false;
                            }
                            CheckModCommand.checkplayer = "";
                            CheckModCommand.code = "";
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