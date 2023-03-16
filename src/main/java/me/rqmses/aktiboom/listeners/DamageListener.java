package me.rqmses.aktiboom.listeners;

import me.rqmses.aktiboom.enums.InformationType;
import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.client.Minecraft;
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

        if (BOMBE) {
            if (Objects.equals(event.getSource().damageType, "arrow") && MEMBER.contains(event.getEntity().getName())) {
                if (Objects.requireNonNull(event.getSource().getTrueSource()).getName().equals(player.getName())) {
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
