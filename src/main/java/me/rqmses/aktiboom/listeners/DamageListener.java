package me.rqmses.aktiboom.listeners;

import me.rqmses.aktiboom.enums.InformationType;
import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.util.Objects;

import static me.rqmses.aktiboom.AktiBoom.BOMBE;
import static me.rqmses.aktiboom.AktiBoom.MEMBER;


public class DamageListener {

    public static String lastHitName = "";
    public static Long lastHitTime = 0L;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onDamage(LivingAttackEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (BOMBE) {
            if (Objects.equals(event.getSource().damageType, "arrow")) {
                if (Objects.requireNonNull(event.getSource().getTrueSource()).getName().equals(player.getName())) {
                    lastHitName = event.getEntity().getName();
                    lastHitTime = System.currentTimeMillis();

                    if (MEMBER.contains(event.getEntity().getName())) {
                        new Thread(() -> {
                            try {
                                SheetUtils.addValues(InformationType.MATESHOTS.getSheet(), InformationType.MATESHOTS.getRange(), new String[]{player.getName(), event.getEntity().getName()});
                            } catch (IOException ignored) {
                            }
                        }).start();
                    }
                }
            }
        }
    }
}
