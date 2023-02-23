package me.rqmses.aktiboom.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class LeitfadenCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "leitfaden";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/leitfaden ([Kategorie])";
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (args.length == 1) {
            targets = Arrays.asList("Commands", "Features", "Installation");
        }
        for (String target : targets) {
            if (target.toUpperCase().startsWith(args[args.length-1].toUpperCase()))
                list.add(target);
        }
        Collections.sort(targets);
        return list;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            NetHandlerPlayClient netHandlerPlayClient = Minecraft.getMinecraft().getConnection();
            if (netHandlerPlayClient != null) {
                if (netHandlerPlayClient.getNetworkManager().channel().remoteAddress().toString().toLowerCase().contains("unicacity.de")) {
                    player.sendMessage(new TextComponentString(PREFIX + "AktiBoom-Leitfaden:"));
                    if (args.length == 0) {
                        general();
                    } else {
                        switch (args[0].toLowerCase()) {
                            case "commands":
                                commands();
                                break;
                            case "features":
                                features();
                                break;
                            case "installation":
                                installation();
                                break;
                            default:
                                general();
                                break;
                        }
                    }
                } else {
                    player.sendChatMessage("/leitfaden");
                }
            }
        }).start();
    }

    public static void general() {
        commands();
        description("");
        features();
        description("");
        installation();
    }

    private static void commands() {
        category("Commands:");
        command("/addmember");
        description("F\u00fcgt einen Member zum Aktinachweis hinzu.");
        command("/aktiboom");
        description("Gibt Informationen rund um die Mod aus.");
        command("/akti");
        description("Tr\u00e4gt eine Aktivit\u00e4t der entsprechenden Kategorie ein.");
        command("/auftr\u00e4ge");
        description("Gibt eine Liste aktueller Auftragsauslieferungen aus.");
        command("/auftragsauslieferung");
        description("F\u00fcgt eine Auftragsauslieferung zur Liste hinzu oder entfernt.");
        command("/autobombe");
        description("F\u00fcgt eine gekaufte Autobombe zur Liste hinzu oder entfernt.");
        command("/beweise");
        description("Gibt Videos zu den Fl\u00fcgen des angegebenen Spots aus.");
        command("/bombe");
        description("Legt eine Bombe.");
        command("/bombendistanz");
        description("(De-)aktiviert die Distanzanzeige zur Bombe.");
        command("/checkaktis");
        description("Gibt die Anzahl der eigenen Aktivit\u00e4ten oder des angegebenen Members aus.");
        command("/checkauftrag");
        description("Gibt Informationen zum Auslieferungsauftrag des Spielers aus.");
        command("/checkdrugs");
        description("Gibt den Tascheninhalt des Members aus.");
        command("/checkequip");
        description("Gibt das eigene Equip oder des angegebenen Members in der Woche aus.");
        command("/checkmod");
        description("\u00dcberpr\u00fcft den Installationsstatus des angegebenen Members.");
        command("/checkschutz");
        description("Gibt Informationen zum Schutzgeld des Spielers aus.");
        command("/checksecdrugs");
        description("Gibt die Anzahl \u00fcbriger und verbrauchter SEC-Drogen aus.");
        command("/checksprengi");
        description("Gibt Informationen zum Sprengg\u00fcrtelauftrag des Spielers aus.");
        command("/checktuning");
        description("Gibt Informationen zur Autobombe des Spielers aus.");
        command("/clearnachweis");
        description("Setzt den Aktivit\u00e4tsnachweis zur\u00fcck.");
        command("/continue");
        description("F\u00e4hrt die aktuelle Rechte\u00fcbertragung nach dem Rate-Limit fort.");
        command("/equip");
        description("\u00d6ffnet das Equip-GUI bzw. tr\u00e4gt es ein.");
        command("/game");
        description("Startet eine Game-Party mit den angegebenen Membern bzw. \u00f6ffnet das Spielbrett.");
        command("/global");
        description("Zeigt eine memberweite Info-Meldung an.");
        command("/info");
        description("Gibt die eigenen Informationen oder die des angegebenen Members aus.");
        command("/invsee");
        description("Gibt den Inventar-Inhalt des Members aus.");
        command("/kalender");
        description("Zeigt den Kalender an bzw. tr\u00e4gt in diesen ein.");
        command("/lastbomb");
        description("Gibt Informationen zum letzten Bomben-Plant aus.");
        command("/leitfaden");
        description("\u00d6ffnet diesen Leitfaden.");
        command("/memberderwoche");
        description("Gibt die aktuellen Member der Woche aus.");
        command("/nearestnavi");
        description("Gibt den n\u00e4chsten UC-Navipunkt von einem aus.");
        command("/refresh");
        description("L\u00e4dt deine Daten, oder die des angegebenen Members, aus dem Aktivit\u00e4tsnachweis neu.");
        command("/removemember");
        description("Entfernt einen Member aus dem Aktivit\u00e4tsnachweis.");
        command("/renamemember");
        description("Benennt einen Spieler im Aktivit\u00e4tsnachweis um.");
        command("/rp");
        description("Erstellt einen Screenshot innerhalb einer RP-Sitzung.");
        command("/rpsession");
        description("Startet und beendet eine RP-Sitzung und tr\u00e4gt diese ein.");
        command("/schutz");
        description("Gibt eine Liste aktueller Schutzgelder aus.");
        command("/schutzgeld");
        description("F\u00fcgt ein Schutzgeld zur Liste hinzu oder entfernt.");
        command("/sec");
        description("Gibt eine Liste aktueller SEC-Member aus bzw. (un-)invitet.");
        command("/secdrugs");
        description("Tr\u00e4gt eine Drogenabholung des SECs ein.");
        command("/setrank");
        description("Setzt einen Member auf den angegeben Rang im Nachweis und gibt die Rechte dazu.");
        command("/setsecrank");
        description("Setzt einen Member auf den angegeben SEC-Rang im Nachweis und gibt die Rechte dazu.");
        command("/sf");
        description("Schreibt in den SEC-Chat.");
        command("/sprengg\u00fcrtelauftrag");
        description("F\u00fcgt einen Sprengg\u00fcrtelauftrag zur Liste hinzu oder entfernt ihn.");
        command("/sprengis");
        description("Gibt eine Liste aktueller Sprengg\u00fcrtelauftr\u00e4ge aus.");
        command("/statistik");
        description("Gibt die monatliche Statistik aus bzw. tr\u00e4gt in diese ein.");
        command("/training");
        description("Stellt eine direkte Verbindung zum Trainingsserver her.");
        command("/tunings");
        description("Gibt eine Liste aktueller gekaufter Autobomben aus.");
    }

    private static void features() {
        category("Features:");
        feature("Aktivit\u00e4ten");
        description("- Aktis k\u00f6nnen Ingame eingetragen und \u00fcberpr\u00fcft werden.");
        description("- Equipment kann Ingame eingetragen und \u00fcberpr\u00fcft werden.");
        description("- Allgemein einfache Interaktion mit dem Aktivit\u00e4tsnachweis.");
        description("");
        feature("Bomben");
        description("- Nachricht beim Legen einer Bombe.");
        description("- M\u00f6glichkeit, jederzeit ein Navi zur Bombe anzeigen zu lassen.");
        description("  (Standartm\u00e4\u00dfig in den Mod-Einstellungen aktiviert)");
        description("- Ausgabe der Distanz zum n\u00e4chsten Navipunkt.");
        description("- Anzeige der Distanz zur Bombe ohne H\u00f6he");
        description("- Warnung, wenn der Radius \u00fcberschritten wird.");
        description("- Sounds, passend zum Legen und zur Entfernung einer Bombe.");
        description("  (Standartm\u00e4\u00dfig in den Mod-Einstellungen aktiviert)");
        description("- Statusmeldung zum Tod des Planters.");
        description("");
        feature("Sprengg\u00fcrtel");
        description("- Sounds beim Z\u00fcnden und der Detonation des Sprengg\u00fcrtels.");
        description("  (Automatisch in den Mod-Einstellungen aktiviert)");
        description("");
        feature("Kalender");
        description("- Eintragen in den Kalender Ingame.");
        description("- Erfolgen einer Benachrichtigung, wenn eine Aktivit\u00e4t bevorsteht zu starten.");
        description("");
        feature("Spiele");
        description("- Spielen von vorgegeben Spielen mit Membern.");
        description("");
        feature("HotKeys");
        description("- HotKey zum Legen einer Bombe einstellbar. \u21E8 Steuerung");
        description("- HotKey zum Z\u00fcnden eines Sprengg\u00fcrtels einstellbar. \u21E8 Steuerung");
        description("");
        feature("Nametags");
        description("- Ein Farbcode f\u00fcr die Nametags der Spieler auf den einzelnen Listen ist einstellbar. \u21E8 Mod-Configs");
        description("");
        feature("Aktualisierung");
        description("- Automatisches Updaten der AktiBoom-Version.");
        description("  (Standartm\u00e4\u00dfig in den Mod-Einstellungen aktiviert)");
        description("- Automatischer Refresh der Daten aus dem Nachweis.");
        description("  (Standartm\u00e4\u00dfig in den Mod-Einstellungen deaktiviert)");
    }

    private static void installation() {
        category("Installation:");
        description("1. Den Beitrag mit der neuesten Version heraussuchen (Updates werden regelm\u00e4\u00dfig hochgeladen)");
        description("");
        description("2. Die angeh\u00e4ngte `AktiBOOM-X.Y.jar`-Datei herunterladen");
        description("");
        description("3. Die Datei in folgenden Ordner verschieben: C:/Users/Dein_Name/AppData/Roaming/.minecraft/mods");
        description("");
        description("4. Minecraft neustarten");
        description("");
        description("5. W\u00e4hrend das Minecraft-Programm startet, warten, bis es f\u00fcr einen Moment so aussieht, als w\u00fcrde es crashen");
        description("");
        description("6. Aus Minecraft tabben und in das Browserfenster der Wahl schauen, welches sich im Hintergrund ge\u00f6ffnet hat");
        description("");
        description("7. Sich mit dem Account anmelden, mit dem man auch im Aktinachweis verifiziert ist");
        description("7.1. Wenn eine Warnung angezeigt wird (siehe https://i.imgur.com/RjJR2FO.png), muss man unten auf 'Erweitert' klicken und dann best\u00e4tigen, dass man seinen Account mit der Terror-Aktinachweis-API verifizieren will");
        description("7.2. Sollte man sich versehentlich mit dem falschen Account angemeldet haben, muss man folgende Datei l\u00f6schen: C:/Users/Dein_Name/AppData/Roaming/.minecraft/tokens/StoredCredential und dann Minecraft neustarten");
        description("");
        description("8. Einem Server joinen und testweise den Befehl `/aktiboom` eingeben, dann bekommt man angezeigt, ob man verbunden ist");
        description("8.1. Sollte man nicht verbunden sein, soll sichergestellt werden, dass der Name des Tabellenblatts, in dem man seine Aktis eintr\u00e4gt, den selben Namen hat, wie die Person ingame");
    }

    private static void category(String content) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.GOLD + " " + TextFormatting.BOLD + content));
    }

    private static void command(String content) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "  " + TextFormatting.ITALIC + content));
    }

    private static void feature(String content) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "  " + TextFormatting.BOLD + content));
    }

    private static void description(String content) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   " + content));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }
}