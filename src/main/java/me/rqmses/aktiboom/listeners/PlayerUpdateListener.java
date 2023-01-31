package me.rqmses.aktiboom.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerUpdateListener {

    private static long lastTime = System.currentTimeMillis();
    public static boolean showdistance = false;
    public static BlockPos bombpos = new BlockPos(0, -1, 0);

    @SubscribeEvent
    public void onPlayerMove(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof EntityPlayerSP) {
            if (showdistance) {
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                BlockPos pos = player.getPosition();
                double distance = (double) Math.round(pos.getDistance(bombpos.getX(), pos.getY(), bombpos.getZ()) * 10) / 10;
                long currentTime = System.currentTimeMillis();

                String color = TextFormatting.BLACK + "";
                if (distance <= 25) {
                    color = TextFormatting.DARK_GREEN + "";
                } else if (distance <= 40) {
                    color = TextFormatting.GREEN + "";
                } else if (distance <= 55) {
                    color = TextFormatting.YELLOW + "";
                } else if (distance <= 70) {
                    color = TextFormatting.GOLD + "";
                } else if (distance <= 85) {
                    color = TextFormatting.RED + "";
                } else if (distance <= 100) {
                    color = TextFormatting.DARK_RED + "";
                } else if (currentTime - lastTime >= 20000) {
                    lastTime = System.currentTimeMillis();
                    player.sendChatMessage("/f %INFO% :&6" + player.getName() + "&e hat den Bomben-Radius verlassen!");
                }
                player.sendStatusMessage(new TextComponentString( color + TextFormatting.BOLD + distance + "m"), true);
            }
        }
    }
}
