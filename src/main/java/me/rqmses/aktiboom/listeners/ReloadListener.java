package me.rqmses.aktiboom.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Timer;
import java.util.TimerTask;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

public class ReloadListener {

    private static String ammoAlpha = null;
    public static boolean reloadTimer = false;
    public static Timer timer = new Timer();

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onReload(TickEvent.ClientTickEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        try {
            if (!player.inventory.getCurrentItem().isEmpty()) {
                ItemStack item = player.inventory.getCurrentItem();
                if (item.getDisplayName().contains("Alpha-7")) {
                    assert item.getTagCompound() != null;
                    String ammo = item.getTagCompound().getCompoundTag("display").getTagList("Lore", 8).getStringTagAt(0);

                    if (ammoAlpha == null) {
                        ammoAlpha = ammo;
                    } else if (!ammoAlpha.equals(ammo)) {
                        if (!ammo.startsWith(TextFormatting.GOLD + "0")) {
                            ammoAlpha = null;

                            if (!reloadTimer) {
                                reloadTimer = true;
                            } else {
                                timer.cancel();
                                timer = new Timer();
                            }

                            final int[] time = {75};
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (time[0] > 0) {
                                        player.sendStatusMessage(new TextComponentString(TextFormatting.YELLOW + "Alpha-Reload: " + TextFormatting.RED + time[0]-- + TextFormatting.GRAY + " Sekunden"), true);
                                    } else {
                                        player.sendMessage(new TextComponentString(PREFIX + "Deine " + TextFormatting.GOLD + "Alpha-7" + TextFormatting.YELLOW + " ist nun nachgeladen!"));
                                        reloadTimer = false;
                                        cancel();
                                    }
                                }
                            }, 0, 1000);
                        }
                    }
                }
            }
        } catch (NullPointerException ignored) {
        }
    }
}
