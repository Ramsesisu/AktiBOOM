package me.rqmses.aktiboom.listeners;

import me.rqmses.aktiboom.commands.BombeCommand;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerDeathListener {
    @SubscribeEvent
    public void onDeath(ClientChatReceivedEvent event)  {
        String message = event.getMessage().getUnformattedText();
        if (message.startsWith("Du bist nun f\u00fcr ") && message.endsWith(" Minuten auf dem Friedhof.")) {
            PlayerUpdateListener.showdistance = false;
            BombeCommand.planter = false;
        }
    }
}
