package me.rqmses.aktiboom.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ChatListener {

    public static boolean planter = false;
    public static int x = 0;
    public static int y = 0;
    public static int z = 0;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        Timer timer = new Timer();
        if (event.getMessage().getUnformattedText().equalsIgnoreCase("Du hast eine Bombe gelegt.")) {
            player.sendMessage(new TextComponentString(event.getMessage().getUnformattedText()));
            x = player.getPosition().getX();
            y = player.getPosition().getY();
            z = player.getPosition().getZ();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    planter = true;
                    player.sendChatMessage("/navi " + x + "/" + y + "/" + z);
                }
            }, TimeUnit.SECONDS.toMillis(1));
            return;
        }
        if (planter) {
            if (event.getMessage().getUnformattedText().contains("Du hast deine Route gel\u00f6scht.")) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        player.sendChatMessage("/navi " + x + "/" + y + "/" + z);
                        planter = false;
                    }
                }, TimeUnit.SECONDS.toMillis(1));
            }
            planter = false;
        }
    }
}
