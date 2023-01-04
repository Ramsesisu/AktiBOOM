package me.rqmses.aktiboom;

import me.rqmses.aktiboom.commands.AktiBoomCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
        modid = AktiBoom.MOD_ID,
        name = AktiBoom.MOD_NAME,
        version = AktiBoom.VERSION
)
public class AktiBoom {

    public static final String MOD_ID = "aktiboom";
    public static final String MOD_NAME = "AktiBOOM";
    public static final String VERSION = "1.0-SNAPSHOT";

    @Mod.Instance(MOD_ID)
    public static AktiBoom INSTANCE;
    public static EntityPlayerSP PLAYER;
    public static String NAME;

    public static boolean connected = false;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Registration
        PLAYER = Minecraft.getMinecraft().player;
        NAME = PLAYER.getName();

        // Commands
        CommandRegistration();

        // Listeners
        ListenerRegistration();
    }

    public void CommandRegistration() {
        ClientCommandHandler.instance.registerCommand(new AktiBoomCommand());
    }

    public void ListenerRegistration() {
    }
}