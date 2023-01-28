package me.rqmses.aktiboom.handlers;

import me.rqmses.aktiboom.AktiBoom;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = AktiBoom.MOD_ID, name = AktiBoom.MOD_ID)
@Mod.EventBusSubscriber
public class ConfigHandler {

    @Config.Name("Auftragsauslieferungen-Nametag")
    @Config.Comment("F\u00e4rbt die Namen der Spieler in dem gegeben ColorCode.")
    public static final String auftragcolor = "";

    @Config.Name("Sprengg\u00fcrteldrohungen-Nametag")
    @Config.Comment("F\u00e4rbt die Namen der Spieler in dem gegeben ColorCode.")
    public static final String drohungcolor = "";

    @Config.Name("Schutzgelder-Nametag")
    @Config.Comment("F\u00e4rbt die Namen der Spieler in dem gegeben ColorCode.")
    public static final String schutzcolor = "";

    @Config.Name("Autobomben-Nametag")
    @Config.Comment("F\u00e4rbt die Namen der Spieler in dem gegeben ColorCode.")
    public static final String tuningcolor = "";

    @Config.Name("SECChat-Farbe-Prefix")
    @Config.Comment("F\u00e4rbt den Prefix des SEC-Chats in dem gegeben ColorCode.")
    public static final String secchatprefix = "c";

    @Config.Name("SECChat-Farbe-Nachricht")
    @Config.Comment("F\u00e4rbt die Nachricht des SEC-Chats in dem gegeben ColorCode.")
    public static final String secchatmessage = "7";

    @Config.Name("Auto-Refresh")
    @Config.Comment("Aktualisiert die Mod-Daten automatisch. (Verursacht Lags)")
    public static final boolean autorefresh = false;

    @Config.Name("Auto-Spielbrett")
    @Config.Comment("\u00d6ffnet das Spielfeld automatisch, wenn man an der Reihe ist")
    public static final boolean autoboard = true;


    @SubscribeEvent
    public static void onConfigChange(ConfigChangedEvent event) {
        if (event == null || event.getModID().equals(AktiBoom.MOD_ID)) {
            ConfigManager.sync(AktiBoom.MOD_ID, Config.Type.INSTANCE);
        }
    }
}
