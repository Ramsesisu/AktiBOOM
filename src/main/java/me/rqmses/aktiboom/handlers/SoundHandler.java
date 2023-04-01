package me.rqmses.aktiboom.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class SoundHandler {
    public static final SoundEvent TIMER;
    public static final SoundEvent BOMBDEF;
    public static final SoundEvent BOMBPL;
    public static final SoundEvent TERWIN;
    public static final SoundEvent HOSDOWN;

    static {
        TIMER = addSoundsToRegistry("timer");
        BOMBDEF = addSoundsToRegistry("bombdef");
        BOMBPL = addSoundsToRegistry("bombpl");
        TERWIN = addSoundsToRegistry("terwin");
        HOSDOWN = addSoundsToRegistry("hosdown");
    }

    private static SoundEvent addSoundsToRegistry(String soundId) {
        ResourceLocation shotSoundLocation = new ResourceLocation("aktiboom", soundId);
        SoundEvent soundEvent = new SoundEvent(shotSoundLocation);
        soundEvent.setRegistryName(shotSoundLocation);
        return soundEvent;
    }

    public static void playSound(SoundEvent sound) {
        try {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            Minecraft.getMinecraft().world.playSound(player, player.getPosition(), sound, SoundCategory.MASTER, 10.0F, 1.0F);
        } catch (Exception ignored) {
        }
    }
}
