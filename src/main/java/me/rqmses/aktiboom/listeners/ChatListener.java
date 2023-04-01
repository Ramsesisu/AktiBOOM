package me.rqmses.aktiboom.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

public class ChatListener {

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player.inventory.armorInventory.get(2).getDisplayName().contains("Sprengg\u00fcrtel")) {
            if (event.getOriginalMessage().contains("/use")) {
                player.sendMessage(new TextComponentString(PREFIX + "Du darfst keine Drogen nehmen w\u00e4hrend du einen Sprengg\u00fcrtel tr\u00e4gst!"));
                event.setCanceled(true);
            }
        }
    }
}
