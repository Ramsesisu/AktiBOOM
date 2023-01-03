package me.rqmses.aktimod;

import me.rqmses.aktimod.commands.AktiModCommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
        modid = AktiMod.MOD_ID,
        name = AktiMod.MOD_NAME,
        version = AktiMod.VERSION
)
public class AktiMod {

    public static final String MOD_ID = "aktimod";
    public static final String MOD_NAME = "AktiMod";
    public static final String VERSION = "1.0-SNAPSHOT";

    @Mod.Instance(MOD_ID)
    public static AktiMod INSTANCE;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Registration

        // Commands
        CommandRegistration();

        // Listeners
        ListenerRegistration();
    }

    public void CommandRegistration() {
        ClientCommandHandler.instance.registerCommand(new AktiModCommand());
    }

    public void ListenerRegistration() {
        MinecraftForge.EVENT_BUS.register(null);
    }
}