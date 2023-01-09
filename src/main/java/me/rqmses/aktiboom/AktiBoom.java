package me.rqmses.aktiboom;

import me.rqmses.aktiboom.commands.*;
import me.rqmses.aktiboom.listeners.ChatListener;
import me.rqmses.aktiboom.listeners.HotkeyListener;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static me.rqmses.aktiboom.handlers.SheetHandler.getSheetsService;
import static me.rqmses.aktiboom.handlers.SheetHandler.sheetsService;

@Mod(
        modid = AktiBoom.MOD_ID,
        name = AktiBoom.MOD_NAME,
        version = AktiBoom.VERSION
)
public class AktiBoom {

    public static final String MOD_ID = "aktiboom";
    public static final String MOD_NAME = "AktiBOOM";
    public static final String VERSION = "1.3";

    public static final String PREFIX = TextFormatting.DARK_GRAY + "[" + TextFormatting.GOLD + "AktiBOOM" + TextFormatting.DARK_GRAY + "] " + TextFormatting.YELLOW;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws GeneralSecurityException, IOException {
        // Commands
        CommandRegistration();

        // Listeners
        ListenerRegistration();

        // KeyBinds
        KeyBindRegistration();

        // Registration
        sheetsService = getSheetsService();
    }

    @SideOnly(Side.CLIENT)
    public void CommandRegistration() {
        ClientCommandHandler.instance.registerCommand(new AktiBoomCommand());
        ClientCommandHandler.instance.registerCommand(new AktivitaetCommand());
        ClientCommandHandler.instance.registerCommand(new RPSessionCommand());
        ClientCommandHandler.instance.registerCommand(new RPCommand());
        ClientCommandHandler.instance.registerCommand(new InfoCommand());
        ClientCommandHandler.instance.registerCommand(new CheckAktisCommand());
        ClientCommandHandler.instance.registerCommand(new CheckEquipCommand());
        ClientCommandHandler.instance.registerCommand(new SECDrugsCommand());
        ClientCommandHandler.instance.registerCommand(new CheckSECDrugsCommand());
        ClientCommandHandler.instance.registerCommand(new BombeCommand());
    }

    public void ListenerRegistration() {
        MinecraftForge.EVENT_BUS.register(new HotkeyListener());
        MinecraftForge.EVENT_BUS.register(new ChatListener());
    }

    public static KeyBinding sprengguertel = new KeyBinding("/sprengg\u00fcrtel 10", Keyboard.KEY_NONE, "AktiBOOM");

    public void KeyBindRegistration() {
        ClientRegistry.registerKeyBinding(sprengguertel);
    }
}
