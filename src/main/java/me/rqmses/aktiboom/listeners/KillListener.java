package me.rqmses.aktiboom.listeners;

import me.rqmses.aktiboom.enums.InformationType;
import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.util.Objects;

import static me.rqmses.aktiboom.AktiBoom.OPERATION;

public class KillListener {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMessage(ClientChatReceivedEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        String message = event.getMessage().getUnformattedText();
        if (OPERATION) {
            if (message.startsWith("[Karma] -") && message.endsWith(" Karma.")) {
                int karma = Integer.parseInt(message.replace("[Karma] ", "").replace(" Karma.", ""));
                if (karma < 0 && karma > -9) {
                    new Thread(() -> {
                        try {
                            SheetUtils.addValues(InformationType.KILLS_LOG.getSheet(), InformationType.KILLS_LOG.getRange(), new String[]{player.getName()});
                        } catch (IOException ignored) {
                        }
                    }).start();
                }
            }
        }
    }

    @SubscribeEvent
    public void onDamage(LivingAttackEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (event.getEntity() instanceof EntityOtherPlayerMP) {
            if (OPERATION) {
                new Thread(() -> {
                    if (event.getSource() != null && event.getSource().getTrueSource() != null) {
                        if (Objects.requireNonNull(event.getSource().getTrueSource()).getName().equals(player.getName())) {
                            if ((((EntityOtherPlayerMP) event.getEntity()).getHealth() - event.getAmount()) < 1) {
                                try {
                                    SheetUtils.addValues(InformationType.KILLS.getSheet(), InformationType.KILLS.getRange(), new String[]{player.getName(), event.getEntity().getName()});
                                } catch (IOException ignored) {
                                }
                            }
                        }
                    }
                }).start();
            }
        }
    }
}
