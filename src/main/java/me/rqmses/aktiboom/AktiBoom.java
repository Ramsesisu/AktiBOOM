package me.rqmses.aktiboom;

import me.rqmses.aktiboom.commands.AktiBoomCommand;
import me.rqmses.aktiboom.commands.AktivitaetCommand;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    public static final String VERSION = "1.0-SNAPSHOT";

    @Mod.Instance(MOD_ID)
    public static AktiBoom INSTANCE;

    public static final String PREFIX = TextFormatting.DARK_GRAY + "[" + TextFormatting.GOLD + "AktiBOOM" + TextFormatting.DARK_GRAY + "] ";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws GeneralSecurityException, IOException {
        // Commands
        CommandRegistration();

        // Listeners
        ListenerRegistration();

        sheetsService = getSheetsService();
    }

    @SideOnly(Side.CLIENT)
    public void CommandRegistration() {
        ClientCommandHandler.instance.registerCommand(new AktiBoomCommand());
        ClientCommandHandler.instance.registerCommand(new AktivitaetCommand());
    }

    public void ListenerRegistration() {
    }
}