package me.rqmses.aktiboom.listeners;

import me.rqmses.aktiboom.commands.BombeCommand;
import me.rqmses.aktiboom.utils.guis.GameGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClientTickListener {
    public static GameGui openedGuiNextTick;
    public static double posY = 0;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (openedGuiNextTick != null) {
            Minecraft.getMinecraft().displayGuiScreen(openedGuiNextTick);
            openedGuiNextTick = null;
        }

        if (posY != 0) {
            if (player.posY - posY > 1) {
                BombeCommand.bombe();
                posY = 0;
            }
        }
    }
}
