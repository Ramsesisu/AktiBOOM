package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.LocationUtils;
import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.AktiBoom.RANK;

@SuppressWarnings("ALL")
public class BombeCommand extends CommandBase implements IClientCommand {

    private static int x = 0;
    private static int y = 0;
    private static int z = 0;
    private static double xd = 0D;
    private static double yd = 0D;
    private static double zd = 0D;
    public static boolean planter = false;
    public static String[] time = new String[] {};

    @Override
    @Nonnull
    public String getName() {
        return "bombe";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/bombe";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("bomb");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (Minecraft.getMinecraft().getConnection().getNetworkManager().channel().remoteAddress().toString().toLowerCase().contains("unicacity.de")) {
            if (RANK >= 4) {
                Timer timer = new Timer();

                x = player.getPosition().getX();
                y = player.getPosition().getY();
                z = player.getPosition().getZ();

                xd = player.posX;
                yd = player.posY;
                zd = player.posZ;

                Date date = Calendar.getInstance().getTime();
                time = new String[] {new SimpleDateFormat("dd.MM.yyyy").format(date), new SimpleDateFormat("HH:mm").format(date)};

                String pos = x + "/" + y + "/" + z;

                String nearestnavi = LocationUtils.getNearestNavi(player.getPosition());
                BlockPos nearestpos = LocationUtils.uclocs.get(nearestnavi);
                double distance = (double) Math.round(nearestpos.getDistance(x, y, z) * 10) / 10;
                boolean max = distance > 60;

                Block block = Minecraft.getMinecraft().world.getBlockState(new BlockPos(xd, yd, zd)).getBlock();
                if (Minecraft.getMinecraft().world.isAirBlock(new BlockPos(xd, yd, zd)) || block == Blocks.WATER || block == Blocks.FLOWING_WATER || block == Blocks.LAVA || block == Blocks.FLOWING_LAVA) {
                    player.sendChatMessage("/bombe");

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (Minecraft.getMinecraft().world.getBlockState(new BlockPos(xd, yd, zd)).getBlock().toString().toLowerCase().contains("tnt")) {
                                player.sendChatMessage("/f %INFO% :&6&l" + player.getName() + "&e hat eine Bombe bei &6" + pos + "&e gelegt.");
                                planter = true;

                                new Thread(() -> {
                                    try {
                                        SheetUtils.setValues("Win/Lose Statistik", "H34:K34", new String[]{time[0], time[1], nearestnavi, player.getName()});
                                    } catch (IOException e) {
                                    }
                                }).start();

                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        player.sendChatMessage("/f %NAVI% : " + pos);
                                        if (max) {
                                            player.sendChatMessage("/f %INFO% :" + "Die Bombe ist zu weit vom n\u00e4chsten Navipunkt &6&l" + nearestnavi + "&7 (&l" + distance + "m&7) " + "&eentfernt.");
                                        } else {
                                            player.sendChatMessage("/f %INFO% :" + "Der n\u00e4chste Navipunkt zur Bombe ist: &6&l" + nearestnavi + "&7 (&l" + distance + "m&7)");
                                        }
                                    }
                                }, 2750);
                            }
                        }
                    }, 250);
                } else {
                    player.sendMessage(new TextComponentString(PREFIX + "Platzier die Bombe da, wo kein Block ist!"));
                }
            } else {
                player.sendMessage(new TextComponentString(PREFIX + "Du musst Rang-4 sein, um eine Bombe legen zu k\u00f6nnen!"));
            }
        } else {
            player.sendChatMessage("/bombe");
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
