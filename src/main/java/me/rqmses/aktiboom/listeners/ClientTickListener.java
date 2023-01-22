package me.rqmses.aktiboom.listeners;

import me.rqmses.aktiboom.utils.guis.GameGui;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClientTickListener {
    public static GameGui openedGuiNextTick;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (openedGuiNextTick != null) {
            Minecraft.getMinecraft().displayGuiScreen(openedGuiNextTick);
            openedGuiNextTick = null;
        }
    }
}
