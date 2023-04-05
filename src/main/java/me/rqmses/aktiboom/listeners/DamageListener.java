package me.rqmses.aktiboom.listeners;

import me.rqmses.aktiboom.enums.InformationType;
import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.util.Objects;

import static me.rqmses.aktiboom.AktiBoom.BOMBE;
import static me.rqmses.aktiboom.AktiBoom.MEMBER;


public class DamageListener {

    @SubscribeEvent
    public void onDamage(LivingAttackEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (event.getEntity() instanceof EntityOtherPlayerMP) {
            if (BOMBE) {
                new Thread(() -> {
                    if (event.getSource() != null && event.getSource().getTrueSource() != null) {
                        if (Objects.requireNonNull(event.getSource().getTrueSource()).getName().equals(player.getName())) {
                            if (Objects.equals(event.getSource().damageType, "arrow")) {
                                if (MEMBER.contains(event.getEntity().getName())) {
                                    try {
                                        SheetUtils.addValues(InformationType.MATESHOTS.getSheet(), InformationType.MATESHOTS.getRange(), new String[]{player.getName(), event.getEntity().getName()});
                                    } catch (IOException ignored) {
                                    }
                                }
                            }
                        }
                    }
                }).start();
            }
        }
    }
}
