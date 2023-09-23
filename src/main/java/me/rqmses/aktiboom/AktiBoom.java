package me.rqmses.aktiboom;

import me.rqmses.aktiboom.commands.*;
import me.rqmses.aktiboom.handlers.ConfigHandler;
import me.rqmses.aktiboom.listeners.*;
import me.rqmses.aktiboom.utils.FormatUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public static final String VERSION = "3.3.3";


    public static String PREFIX = TextFormatting.DARK_GRAY + "[" + FormatUtils.getColor(ConfigHandler.prefix) + "AktiBOOM" + TextFormatting.DARK_GRAY + "] " + TextFormatting.YELLOW;
    public static boolean AFK = false;
    public static boolean KOMMS = true;
    public static String LATEST = VERSION;
    public static final HashMap<String, Integer> MEMBER = new HashMap<>();
    public static final HashMap<String, Integer> SECMEMBER = new HashMap<>();
    public static final HashMap<Integer, String> SECRANKS = new HashMap<>();
    public static boolean BOMBE = false;

    public static final List<Integer> KEYS = new ArrayList<>();

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
        ClientCommandHandler.instance.registerCommand(new EquipmentCommand());
        ClientCommandHandler.instance.registerCommand(new SECDrugsCommand());
        ClientCommandHandler.instance.registerCommand(new BombeCommand());
        ClientCommandHandler.instance.registerCommand(new AuftragsauslieferungCommand());
        ClientCommandHandler.instance.registerCommand(new AuftraegeCommand());
        ClientCommandHandler.instance.registerCommand(new CheckAuftragCommand());
        ClientCommandHandler.instance.registerCommand(new SprengguertelauftragCommand());
        ClientCommandHandler.instance.registerCommand(new SprengisCommand());
        ClientCommandHandler.instance.registerCommand(new CheckSprengiCommand());
        ClientCommandHandler.instance.registerCommand(new SchutzgeldCommand());
        ClientCommandHandler.instance.registerCommand(new SchutzCommand());
        ClientCommandHandler.instance.registerCommand(new CheckSchutzCommand());
        ClientCommandHandler.instance.registerCommand(new RefreshCommand());
        ClientCommandHandler.instance.registerCommand(new SECCommand());
        ClientCommandHandler.instance.registerCommand(new KalenderCommand());
        ClientCommandHandler.instance.registerCommand(new AutobombeCommand());
        ClientCommandHandler.instance.registerCommand(new TuningsCommand());
        ClientCommandHandler.instance.registerCommand(new CheckTuningCommand());
        ClientCommandHandler.instance.registerCommand(new GameCommand());
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
        ClientCommandHandler.instance.registerCommand(new EquipCommand());
        ClientCommandHandler.instance.registerCommand(new LeitfadenCommand());
        ClientCommandHandler.instance.registerCommand(new GlobalCommand());
        ClientCommandHandler.instance.registerCommand(new MemberDerWocheCommand());
        ClientCommandHandler.instance.registerCommand(new LastBombCommand());
        ClientCommandHandler.instance.registerCommand(new GeiselCommand());
        ClientCommandHandler.instance.registerCommand(new GeiselnCommand());
        ClientCommandHandler.instance.registerCommand(new FBankCommand());
        ClientCommandHandler.instance.registerCommand(new GehaelterCommand());
        ClientCommandHandler.instance.registerCommand(new WirtschaftCommand());
        ClientCommandHandler.instance.registerCommand(new StreamerModeCommand());
        ClientCommandHandler.instance.registerCommand(new BesprechungCommand());
        ClientCommandHandler.instance.registerCommand(new GrossaktiCommand());
        ClientCommandHandler.instance.registerCommand(new AddSpotCommand());
        ClientCommandHandler.instance.registerCommand(new RemoveSpotCommand());
        ClientCommandHandler.instance.registerCommand(new SpotCommand());
        ClientCommandHandler.instance.registerCommand(new SECPointsCommand());
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
        MinecraftForge.EVENT_BUS.register(new ContainerListener());
        MinecraftForge.EVENT_BUS.register(new ReloadListener());
    }

    public static final KeyBinding sprengguertel = new KeyBinding("key.sprengguertel", Keyboard.KEY_NONE, "key.categories.aktiboom");
    public static final KeyBinding bombe = new KeyBinding("key.bombe", Keyboard.KEY_NONE, "key.categories.aktiboom");

    public void KeyBindRegistration() {
        ClientRegistry.registerKeyBinding(sprengguertel);
        ClientRegistry.registerKeyBinding(bombe);
    }

    public void SoundRegistration() {
        MinecraftForge.EVENT_BUS.register(new SoundRegisterListener());
    }
}
