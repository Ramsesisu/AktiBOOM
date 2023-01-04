package me.rqmses.aktimod;

import me.rqmses.aktimod.commands.AktiBOOMCommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
        modid = AktiBOOM.MOD_ID,
        name = AktiBOOM.MOD_NAME,
        version = AktiBOOM.VERSION
)
public class AktiBOOM {

    public static final String MOD_ID = "aktiboom";
    public static final String MOD_NAME = "AktiBOOM";
    public static final String VERSION = "1.0-SNAPSHOT";

    @Mod.Instance(MOD_ID)
    public static AktiBOOM INSTANCE;

    public static boolean connected = false;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Registration

        // Commands
        CommandRegistration();

        // Listeners
        ListenerRegistration();
    }

    public void CommandRegistration() {
        ClientCommandHandler.instance.registerCommand(new AktiBOOMCommand());
    }

    public void ListenerRegistration() {
    }
}