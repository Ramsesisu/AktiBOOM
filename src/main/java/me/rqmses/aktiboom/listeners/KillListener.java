package me.rqmses.aktiboom.listeners;

import me.rqmses.aktiboom.enums.InformationType;
import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;

import static me.rqmses.aktiboom.AktiBoom.BOMBE;

public class KillListener {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onDamage(ClientChatReceivedEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        String message = event.getMessage().getUnformattedText();

        if (BOMBE) {
            if (message.startsWith("[Karma] -") && message.endsWith(" Karma.")) {
                int karma = Integer.parseInt(message.replace("[Karma] ", "").replace(" Karma.", ""));
                if (karma < 0 && karma > -9) {
                    if (System.currentTimeMillis() - DamageListener.lastHitTime < 500L) {
                        new Thread(() -> {
                            try {
                                SheetUtils.addValues(InformationType.KILLS.getSheet(), InformationType.KILLS.getRange(), new String[]{player.getName(), DamageListener.lastHitName});
                            } catch (IOException ignored) {
                            }
                        }).start();
                    }
                }
            }
        }
    }
}
