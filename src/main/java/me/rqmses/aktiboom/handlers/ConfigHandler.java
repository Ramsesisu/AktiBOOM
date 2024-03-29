package me.rqmses.aktiboom.handlers;

import me.rqmses.aktiboom.AktiBoom;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("CanBeFinal")
@Config(modid = AktiBoom.MOD_ID, name = AktiBoom.MOD_ID)
@Mod.EventBusSubscriber
public class ConfigHandler {

    @Config.Name("Auftragsauslieferungen-Nametag")
    @Config.Comment("F\u00e4rbt die Namen der Spieler in dem gegeben ColorCode.")
    public static String auftragcolor = "";

    @Config.Name("Sprengg\u00fcrteldrohungen-Nametag")
    @Config.Comment("F\u00e4rbt die Namen der Spieler in dem gegeben ColorCode.")
    public static String drohungcolor = "";

    @Config.Name("Schutzgelder-Nametag")
    @Config.Comment("F\u00e4rbt die Namen der Spieler in dem gegeben ColorCode.")
    public static String schutzcolor = "a";

    @Config.Name("Autobomben-Nametag")
    @Config.Comment("F\u00e4rbt die Namen der Spieler in dem gegeben ColorCode.")
    public static String tuningcolor = "";

    @Config.Name("Auto-Refresh")
    @Config.Comment("Aktualisiert die Mod-Daten automatisch.")
    public static boolean autorefresh = true;

    @Config.Name("Auto-Spielbrett")
    @Config.Comment("\u00d6ffnet das Spielfeld automatisch, wenn man an der Reihe ist")
    public static boolean autoboard = true;

    @Config.Name("Auto-Navi")
    @Config.Comment("Startet in bestimmten Situationen automatisch ein Navi.")
    public static boolean autonavi = true;

    @Config.Name("Auto-Update")
    @Config.Comment("Downloaded die neueste AktiBoom-Version automatisch. Bedenke, dass es verpflichtend ist Mod aktuell zu halten!")
    public static boolean autoupdate = true;

    @Config.Name("Custom-Sounds")
    @Config.Comment("Stellt Terror-bezogenene Custom-Sounds ein.")
    public static boolean customsounds = true;

    @Config.Name("Show Ank\u00fcndigungen")
    @Config.Comment("Zeigt beim Join die Ank\u00fcndigungen des Tages an.")
    public static boolean showaktis = true;

    @Config.Name("Prefix-Farbe")
    @Config.Comment("\u00c4ndert die Farbe des AktiBoom-Prefix.")
    public static String prefix = "6";


    @SubscribeEvent
    public static void onConfigChange(ConfigChangedEvent event) {
        if (event == null || event.getModID().equals(AktiBoom.MOD_ID)) {
            ConfigManager.sync(AktiBoom.MOD_ID, Config.Type.INSTANCE);
        }
    }
}
