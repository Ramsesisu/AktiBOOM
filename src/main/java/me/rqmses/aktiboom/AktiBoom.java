package me.rqmses.aktiboom;

import me.rqmses.aktiboom.commands.*;
import me.rqmses.aktiboom.listeners.*;
import me.rqmses.aktiboom.utils.LocationUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static me.rqmses.aktiboom.handlers.SheetHandler.getSheetsService;
import static me.rqmses.aktiboom.handlers.SheetHandler.sheetsService;

@SideOnly(Side.CLIENT)
@Mod(
        modid = AktiBoom.MOD_ID,
        name = AktiBoom.MOD_NAME,
        version = AktiBoom.VERSION,
        clientSideOnly = true
)

public class AktiBoom {

    public static final String MOD_ID = "aktiboom";
    public static final String MOD_NAME = "AktiBOOM";
    public static final String VERSION = "1.8.2";


    public static final String PREFIX = TextFormatting.DARK_GRAY + "[" + TextFormatting.GOLD + "AktiBOOM" + TextFormatting.DARK_GRAY + "] " + TextFormatting.YELLOW;

    public static boolean SEC = false;
    public static String SECRANK = "-";
    public static int RANK = 0;
    public static boolean AFK = false;
    public static String LATEST = VERSION;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);
        LocationUtils.setLocs();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws GeneralSecurityException, IOException {
        CommandRegistration();
        KeyBindRegistration();
        SoundRegistration();
        ListenerRegistration();

        sheetsService = getSheetsService();
    }

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
        ClientCommandHandler.instance.registerCommand(new AuftragsauslieferungCommand());
        ClientCommandHandler.instance.registerCommand(new AuftraegeCommand());
        ClientCommandHandler.instance.registerCommand(new CheckAuftragCommand());
        ClientCommandHandler.instance.registerCommand(new SprengguerteldrohungCommand());
        ClientCommandHandler.instance.registerCommand(new DrohungenCommand());
        ClientCommandHandler.instance.registerCommand(new CheckDrohungCommand());
        ClientCommandHandler.instance.registerCommand(new SchutzgeldCommand());
        ClientCommandHandler.instance.registerCommand(new SchutzCommand());
        ClientCommandHandler.instance.registerCommand(new CheckSchutzCommand());
        ClientCommandHandler.instance.registerCommand(new RefreshCommand());
        ClientCommandHandler.instance.registerCommand(new SECChatCommand());
        ClientCommandHandler.instance.registerCommand(new SECCommand());
        ClientCommandHandler.instance.registerCommand(new StatistikCommand());
        ClientCommandHandler.instance.registerCommand(new KalenderCommand());
        ClientCommandHandler.instance.registerCommand(new BeweiseCommand());
        ClientCommandHandler.instance.registerCommand(new AutobombeCommand());
        ClientCommandHandler.instance.registerCommand(new TuningsCommand());
        ClientCommandHandler.instance.registerCommand(new CheckTuningCommand());
        ClientCommandHandler.instance.registerCommand(new GameCommand());
        ClientCommandHandler.instance.registerCommand(new TrainingsserverCommand());
        ClientCommandHandler.instance.registerCommand(new NearestNaviCommand());
        ClientCommandHandler.instance.registerCommand(new BombenDistanzCommand());
        ClientCommandHandler.instance.registerCommand(new CheckModCommand());
        ClientCommandHandler.instance.registerCommand(new AddMemberCommand());
        ClientCommandHandler.instance.registerCommand(new RemoveMemberCommand());
        ClientCommandHandler.instance.registerCommand(new ClearNachweisCommand());
        ClientCommandHandler.instance.registerCommand(new SetRankCommand());
        ClientCommandHandler.instance.registerCommand(new RenameMemberCommand());
        ClientCommandHandler.instance.registerCommand(new SetSECRankCommand());
        ClientCommandHandler.instance.registerCommand(new ContinueCommand());
    }

    public void ListenerRegistration() {
        MinecraftForge.EVENT_BUS.register(new HotkeyListener());
        MinecraftForge.EVENT_BUS.register(new ChatListener());
        MinecraftForge.EVENT_BUS.register(new ChatReceiveListener());
        MinecraftForge.EVENT_BUS.register(new PlayerJoinListener());
        MinecraftForge.EVENT_BUS.register(new NameFormatListener());
        MinecraftForge.EVENT_BUS.register(new ClientTickListener());
        MinecraftForge.EVENT_BUS.register(new PlayerUpdateListener());
        MinecraftForge.EVENT_BUS.register(new PlayerDeathListener());
    }

    public static final KeyBinding sprengguertel = new KeyBinding("/sprengg\u00fcrtel 10", Keyboard.KEY_NONE, "AktiBOOM");
    public static final KeyBinding bombe = new KeyBinding("/bombe", Keyboard.KEY_NONE, "AktiBOOM");

    public void KeyBindRegistration() {
        ClientRegistry.registerKeyBinding(sprengguertel);
        ClientRegistry.registerKeyBinding(bombe);
    }

    public void  SoundRegistration() {
        MinecraftForge.EVENT_BUS.register(new SoundRegisterListener());
    }
}
