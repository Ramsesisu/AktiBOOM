package me.rqmses.aktiboom.listeners;

import me.rqmses.aktiboom.commands.BombeCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerDeathListener {
    @SubscribeEvent
    public void onDeath(LivingDeathEvent event)  {
        if (event.getEntity() instanceof EntityPlayerSP) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            PlayerUpdateListener.showdistance = false;
            BombeCommand.planter = false;
        }
    }
}
