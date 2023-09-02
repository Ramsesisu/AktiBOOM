package me.rqmses.aktiboom.listeners;

import me.rqmses.aktiboom.commands.*;
import me.rqmses.aktiboom.handlers.ConfigHandler;
import me.rqmses.aktiboom.handlers.SoundHandler;
import me.rqmses.aktiboom.utils.GameUtils;
import me.rqmses.aktiboom.utils.SheetUtils;
import me.rqmses.aktiboom.utils.TextUtils;
import me.rqmses.aktiboom.utils.guis.GameGui;
import me.rqmses.aktiboom.utils.guis.containers.ChessContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.*;

public class ChatReceiveListener {

    public static boolean hide = false;
    public static String searchprefix = "";
    public static String result = "0";

    public static List<List<Object>> lastMembers = new ArrayList<>();
    public static final HashMap<String, String> secplayerranks = new HashMap<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMessage(ClientChatReceivedEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        String message = event.getMessage().getUnformattedText();

        List<String> prefixes = Arrays.asList(  "Rekrut", "Rekrutin",
                                                "Feldwebel", "Feldwebel",
                                                "Leutnant", "Leutnant",
                                                "Hauptmann", "Hauptfrau",
                                                "Major", "Majorin",
                                                "General", "Generalin",
                                                "Kommandant", "Kommandantin");
        if (StreamerModeCommand.streamermode) {
            if (prefixes.contains(message.split(" ")[0])) {
                event.setCanceled(true);
                return;
            }
        }

        if (message.contains(": %INFO% :")) {
            String[] contents = message.split(":", 3);
            String text = contents[2]
                    .replaceAll("&0", "" + TextFormatting.BLACK + "")
                    .replaceAll("&1", "" + TextFormatting.DARK_BLUE + "")
                    .replaceAll("&2", "" + TextFormatting.DARK_GREEN + "")
                    .replaceAll("&3", "" + TextFormatting.DARK_AQUA + "")
                    .replaceAll("&4", "" + TextFormatting.DARK_RED + "")
                    .replaceAll("&5", "" + TextFormatting.DARK_PURPLE + "")
                    .replaceAll("&6", "" + TextFormatting.GOLD + "")
                    .replaceAll("&7", "" + TextFormatting.GRAY + "")
                    .replaceAll("&8", "" + TextFormatting.DARK_GRAY + "")
                    .replaceAll("&9", "" + TextFormatting.BLUE + "")
                    .replaceAll("&a", "" + TextFormatting.GREEN + "")
                    .replaceAll("&b", "" + TextFormatting.AQUA + "")
                    .replaceAll("&c", "" + TextFormatting.RED + "")
                    .replaceAll("&d", "" + TextFormatting.LIGHT_PURPLE + "")
                    .replaceAll("&e", "" + TextFormatting.YELLOW + "")
                    .replaceAll("&f", "" + TextFormatting.WHITE + "")
                    .replaceAll("&k", "" + TextFormatting.OBFUSCATED + "")
                    .replaceAll("&l", "" + TextFormatting.BOLD + "")
                    .replaceAll("&m", "" + TextFormatting.STRIKETHROUGH + "")
                    .replaceAll("&n", "" + TextFormatting.UNDERLINE + "")
                    .replaceAll("&o", "" + TextFormatting.ITALIC + "")
                    .replaceAll("&r", "" + TextFormatting.RESET + "");

            event.setMessage(new TextComponentString(PREFIX + TextFormatting.YELLOW + text));

            if (ConfigHandler.autorefresh) {
                if (text.contains("Sprengg\u00fcrtelauftrag")) {
                    SprengisCommand.syncList();
                }
                if (text.contains("Auftragsauslieferung")) {
                    AuftraegeCommand.syncList();
                }
                if (text.contains("Autobombe")) {
                    TuningsCommand.syncList();
                }
                if (text.contains("Schutzgeld")) {
                    SchutzCommand.syncList();
                }
            }
        }

        if (message.contains(": %NAVI% :")) {
            event.setCanceled(true);

            String[] contents = message.split(":", 3);

            if (contents[0].split(" ").length < 1) {
                return;
            }

            new Thread(() -> {
                if (SheetUtils.getRank(contents[0].split(" ")[1].replace(" ", "").replace("[UC]", "")) >= 4) {
                    String navi = contents[2].replace(" ", "");
                    player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, " \u27A5 " + TextFormatting.RED + "Route anzeigen", TextFormatting.GRAY + navi, ClickEvent.Action.RUN_COMMAND, "/navi " + navi));

                    if (ConfigHandler.customsounds) {
                        SoundHandler.playSound(SoundHandler.BOMBPL);
                    }

                    NetHandlerPlayClient netHandlerPlayClient = Minecraft.getMinecraft().getConnection();
                    if (netHandlerPlayClient != null) {
                        if (netHandlerPlayClient.getNetworkManager().channel().remoteAddress().toString().toLowerCase().contains("unicacity.de")) {
                            BOMBE = true;
                            OPERATION = true;

                            if (ConfigHandler.autonavi) {
                                if (!AFK) {
                                    player.sendChatMessage("/navi " + navi);
                                }
                            }
                            PlayerUpdateListener.showdistance = true;
                            String[] coords = navi.split("/");
                            PlayerUpdateListener.bombpos = new BlockPos(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
                        }
                    }
                }
            }).start();
        }

        if (message.equals(player.getName() + " hat eine Bombe an ein Auto platziert.") || message.equals("[UC]" + player.getName() + " hat eine Bombe an ein Auto platziert.")) {
            if (KOMMS) {
                BlockPos pos = player.getPosition();
                player.sendChatMessage("/f %CAR% : " + pos.getX() + "/" + pos.getY() + "/" + pos.getZ());
            }
        }

        if (message.contains(": %CAR% :")) {
            event.setCanceled(true);

            String navi = message.split(":", 3)[2].replace(" ", "");
            player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, " \u27A5 " + TextFormatting.RED + "Route anzeigen", TextFormatting.GRAY + navi, ClickEvent.Action.RUN_COMMAND, "/navi " + navi));
        }

        if (message.contains(": %PARTY% :")) {
            event.setCanceled(true);

            String[] contents = message.split(":", 4);
            String players = contents[3];
            if (!GameUtils.party) {
                if (Arrays.asList(players.split(" ")).contains(player.getName())) {
                    GameUtils.players = new ArrayList<>(Arrays.asList(players.split(" ")));
                    GameUtils.players.removeAll(Collections.singleton(""));
                    switch (contents[2].toLowerCase().replaceAll(" ", "")) {
                        case "schach":
                            GameUtils.category = contents[2].replaceAll(" ", "").toLowerCase();
                            GameUtils.turn = -1;
                            ChessContainer.mate = false;
                            ChessContainer.draw = false;
                            ChessContainer.moves = new ArrayList<>();
                            ChessContainer.selectedindex = -1;
                            GameUtils.stringboard = "Rb!Nb!Bb!Qb!Kb!Bb!Nb!Rb!Tc!" +
                                                    "Pb!Pb!Pb!Pb!Pb!Pb!Pb!Pb!Tc!" +
                                                    "Ac!Ac!Ac!Ac!Ac!Ac!Ac!Ac!Tc!" +
                                                    "Ac!Ac!Ac!Ac!Ac!Ac!Ac!Ac!Tc!" +
                                                    "Ac!Ac!Ac!Ac!Ac!Ac!Ac!Ac!Tc!" +
                                                    "Ac!Ac!Ac!Ac!Ac!Ac!Ac!Ac!Tc!" +
                                                    "Pw!Pw!Pw!Pw!Pw!Pw!Pw!Pw!Tc!" +
                                                    "Rw!Nw!Bw!Qw!Kw!Bw!Nw!Rw!Tc";
                            break;
                        case "tictactoe":
                            GameUtils.category = contents[2].replaceAll(" ", "").toLowerCase();
                            GameUtils.turn = -1;
                            GameUtils.win = false;
                            GameUtils.stringboard = "Tb!Tb!Tb!Ab!Ab!Ab!Tb!Tb!Tb!" +
                                                    "Tb!Tb!Tb!Ab!Ab!Ab!Tb!Tb!Tb!" +
                                                    "Tb!Tb!Tb!Ab!Ab!Ab!Tb!Tb!Tb!";
                            break;
                        default:
                            return;
                    }
                    GameUtils.party = true;
                    player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + GameUtils.players.get(0) + TextFormatting.YELLOW + " hat eine " + TextFormatting.GOLD + contents[2].replaceAll(" ", "") + TextFormatting.YELLOW + "-Party gestartet."));
                    player.sendMessage(new TextComponentString(TextFormatting.GRAY + "      \u27A5 " + TextFormatting.YELLOW + "Gib /game ein, um das Spielbrett zu \u00f6ffnen."));
                    GameUtils.handleTurn();
                }
            } else {
                if (Arrays.asList(players.split(" ")).contains(player.getName())) {
                    if (contents[2].replaceAll(" ", "").equalsIgnoreCase("end")) {
                        GameUtils.party = false;
                        if (Minecraft.getMinecraft().currentScreen instanceof GameGui) {
                            player.closeScreen();
                        }
                        player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + contents[0].split(" ")[1] + TextFormatting.YELLOW + " hat die Party beendet."));
                        return;
                    }
                }
            }
        }

        if (message.contains(": %GAME% :")) {
            event.setCanceled(true);

            String[] contents = message.split(":", 4);
            String players = contents[2];
            if (GameUtils.party) {
                if (Arrays.asList(players.split(" ")).contains(player.getName())) {
                    GameUtils.stringboard = contents[3];
                    GameUtils.handleTurn();
                    if (Minecraft.getMinecraft().currentScreen instanceof GameGui) {
                        GameUtils.display();
                    }
                }
            }
        }

        if (message.contains("News: Die Bombe konnte nicht entsch\u00e4rft werden!")) {
            if (ConfigHandler.customsounds) {
                SoundHandler.playSound(SoundHandler.TERWIN);
            }
            resetBomb();
        }

        if (message.contains("News: Die Bombe konnte erfolgreich entsch\u00e4rft werden!")) {
            if (ConfigHandler.customsounds) {
                SoundHandler.playSound(SoundHandler.BOMBDEF);
            }
            resetBomb();
        }

        if (message.contains("News: Es gab ein Selbstmordattentat")) {
            if (ConfigHandler.customsounds) {
                SoundHandler.playSound(SoundHandler.HOSDOWN);
            }
        }

        if (message.equals("Du bist nun im AFK-Modus.")) {
            AFK = true;
        }

        if (message.equals("Du bist nun nicht mehr im AFK-Modus.")) {
            AFK = false;
        }

        if (message.endsWith(" hat dir deine Kommunikationsger\u00e4te abgenommen.")) {
            KOMMS = false;
        }

        if (message.endsWith(" hat dir deine Kommunikationsger\u00e4te wiedergegeben.") || message.equals("Du hast dein Handy genommen.")) {
            KOMMS = true;
        }

        if (message.startsWith("Du hast ") && message.endsWith(" in deine Fraktion invitet.")) {
            event.setCanceled(true);

            player.sendChatMessage("/f %INFO% :&6" + player.getName() + "&e hat &6&l" + message.replace("Du hast ", "").replace(" in deine Fraktion invitet.", "") + "&e invitet!");
        }

        if (message.startsWith("[Fraktion] Du hast ") && message.endsWith(" aus der Fraktion geschmissen!")) {
            event.setCanceled(true);

            player.sendChatMessage("/f %INFO% :&6" + player.getName() + "&e hat &6&l" + message.replace("[Fraktion] Du hast ", "").replace(" aus der Fraktion geschmissen!", "") + "&e uninvitet!");
        }

        if (message.equals("[Equip] Du hast dir eine Alpha-7 equippt!")) {
            player.sendChatMessage("/f %INFO% :&6" + player.getName() + "&e hat sich eine &6&lAlpha-7 &eequippt!");
        }

        if (message.equals("[Equip] Du hast dir ein Sprengg\u00fcrtel equippt!")) {
            player.sendChatMessage("/f %INFO% :&6" + player.getName() + "&e hat sich einen &6&lSprengg\u00fcrtel &eequippt!");
        }

        if (message.equals("[Waffentransport] Du hast eine Waffenlieferung abgegeben.")) {
            player.sendChatMessage("/f %INFO% :&6" + player.getName() + "&e hat das Waffenlager aufgef\u00fcllt!");
        }

        if (message.equals("[Bombe] Du hast " + player.getName() + " den Draht gezeigt.")) {

            event.setCanceled(true);
        }

        if (message.startsWith("[Bombe] " + player.getName() + " hat dir den Draht zum Entsch\u00e4rfen der Bombe gezeigt. Der Draht ist")) {
            event.setCanceled(true);

            String[] tempstring = message.split(" ");
            String tempcolor = tempstring[tempstring.length - 1];
            String color = TextFormatting.GRAY + "Unbekannt";
            if (tempcolor.toLowerCase().contains("rot")) {
                color = TextFormatting.RED + "Rot";
            }
            if (tempcolor.toLowerCase().contains("gr\u00fcn")) {
                color = TextFormatting.GREEN + "Gr\u00fcn";
            }
            if (tempcolor.toLowerCase().contains("blau")) {
                color = TextFormatting.BLUE + "Blau";
            }
            if (tempcolor.toLowerCase().contains("lila")) {
                color = TextFormatting.LIGHT_PURPLE + "Lila";
            }
            player.sendMessage(new TextComponentString(PREFIX + "Die Drahtfarbe ist: " + color));
        }

        if (message.contains(": %CHECK% :")) {
            event.setCanceled(true);

            new Thread(() -> {
                String[] contents = message.split(":");
                if (contents[2].contains(player.getName())) {
                    if (contents.length == 4) {
                        if (SheetUtils.getRank(contents[0].split(" ")[1].replace("[UC]", "")) >= 4) {
                            String version = VERSION;
                            if (!Objects.equals(LATEST, VERSION)) {
                                version = VERSION + " (" + LATEST + ")";
                            }
                            player.sendChatMessage("/f %CHECK% : " + contents[0].split(" ")[1].replace("[UC]", "") + " : " + contents[3].replace(" ", "") + " : " + version);
                        }
                    } else if (contents.length == 5) {
                        if (contents[0].split(" ")[1].contains(CheckModCommand.checkplayer)) {
                            if (contents[3].replace(" ", "").equals(CheckModCommand.code)) {
                                CheckModCommand.check = true;
                                player.sendMessage(new TextComponentString(TextFormatting.GRAY + "  Status: " + TextFormatting.GREEN + "Installiert"));
                                player.sendMessage(new TextComponentString(TextFormatting.GRAY + "  Version: " + TextFormatting.YELLOW + contents[4].replaceFirst(" ", "").replace("(", TextFormatting.DARK_GRAY + "(" + TextFormatting.GRAY).replace(")", TextFormatting.DARK_GRAY + ")")));
                            }
                        }
                    }
                }
            }).start();
        }

        if (message.contains(": %REFRESH% :")) {
            event.setCanceled(true);

            String[] contents = message.split(":");
            if (contents[2].replace(" ", "").equals(player.getName())) {
                new Thread(() -> {
                    if (SheetUtils.getRank(contents[0].split(" ")[1].replace("[UC]", "")) >= 4) {
                        PlayerJoinListener.refresh(false);

                        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(PREFIX + "Alle Daten wurden neu geladen."));
                    }
                }).start();
            }
        }

        if (message.contains(": %OPERATION% :")) {
            event.setCanceled(true);

            new Thread(() -> {
                String[] contents = message.split(":");
                if (SheetUtils.getRank(contents[0].split(" ")[1]) >= 4) {
                    if (contents[2].contains("start")) {
                        OPERATION = true;
                        player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + contents[0].split(" ")[1] + TextFormatting.YELLOW + " hat eine Gro\u00dfaktivit\u00e4t gestartet!"));
                    }
                    if (contents[2].contains("end")) {
                        OPERATION = false;
                        player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + contents[0].split(" ")[1] + TextFormatting.YELLOW + " hat die Gro\u00dfaktivit\u00e4t beendet!"));
                    }
                }
            }).start();
        }

        if (message.startsWith(" \u00BB Fraktionsgehalt: +") && message.endsWith("$")) {
            new Thread(() -> {
                try {
                    int line = SheetUtils.searchLine("Equiplog", "C977:C1000", player.getName()) + 977;

                    int added = Integer.parseInt(message.replace(" \u00BB Fraktionsgehalt: +", "").replace("$", ""));
                    List<List<Object>> values = SheetUtils.getValueRange("Equiplog", "D" + line + ":D" + line).getValues();
                    int current = 0;
                    if (values != null) {
                        current = Integer.parseInt(values.get(0).get(0).toString());
                    }
                    SheetUtils.setLine("Equiplog", "D" + line + ":D" + line, new String[]{String.valueOf(current + added)});
                } catch (IOException ignored) {
                }
            }).start();
        }

        if (message.startsWith("[FrakPayDay] Zinsen: ") && message.endsWith("$)")) {
            if (RANK >= 5) {
                String[] split = message.split(" ");
                String fbank = split[split.length - 1].replace("$)", "");

                new Thread(() -> {
                    try {
                        SheetUtils.setLine("\u00dcbersicht", "H31:H31", new String[]{fbank});
                    } catch (IOException ignored) {
                    }
                }).start();
            }
        }

        if (message.startsWith(searchprefix)) {
            result = message.replace(searchprefix, "");
        }

        if (hide) {
            if (message.equals("")) event.setCanceled(true);
            if (message.startsWith("======") && message.endsWith("======")) event.setCanceled(true);
            if (message.startsWith("  - Level: ")) event.setCanceled(true);
            if (message.startsWith("  - Status: ")) event.setCanceled(true);
            if (message.startsWith("  - Inventar: ")) event.setCanceled(true);
            if (message.startsWith("  - Wanted Punkte: ")) event.setCanceled(true);
            if (message.startsWith("  - Geld: ")) event.setCanceled(true);
            if (message.startsWith("  - Verwarnungen: ")) event.setCanceled(true);
            if (message.startsWith("  - Zeit seit PayDay: ")) event.setCanceled(true);
            if (message.startsWith("  - Experience: ")) event.setCanceled(true);
            if (message.startsWith("  - Fraktion: ")) event.setCanceled(true);
            if (message.startsWith("  - Haus: ")) event.setCanceled(true);
            if (message.startsWith("  - Beruf: ")) event.setCanceled(true);
            if (message.startsWith("  - Votepoints: ")) event.setCanceled(true);
            if (message.startsWith("  - Treuebonus: ")) {
                event.setCanceled(true);
                hide = false;
            }

            if (message.startsWith("=== EquipLog ") && message.endsWith("] ===")) {
                event.setCanceled(true);

                new Thread(() -> {
                    try {
                        lastMembers = SheetUtils.getValueRange("Equiplog", "B4:B27").getValues();
                    } catch (IOException ignored) {
                    }
                }).start();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        hide = false;
                    }
                }, 1000);
            }
            if (message.startsWith(" \u00BB ") && message.contains(" hat in den letzten ") && message.endsWith("$ equiped.")) {
                event.setCanceled(true);

                List<String> members = new ArrayList<>();
                for (List<Object> member : lastMembers) {
                    members.add(member.get(0).toString());
                }

                String[] splitted = message.replace(" \u00BB ", "").split(" ");
                String name = splitted[0].replace("[UC]", "");
                String amount = splitted[splitted.length - 2].replace("$", "");
                if (members.contains(name)) {
                    new Thread(() -> {
                        int line = members.indexOf(name) + 4;
                        try {
                            SheetUtils.setLine("Equiplog", "D" + line + ":D" + line, new String[]{amount});
                        } catch (IOException ignored) {
                        }
                    }).start();
                }
            }
        }

        if (FBankCommand.checkTaxes) {
            if (message.startsWith("[F-Bank] " + player.getName() + " hat ") && message.endsWith("$) in die F-Bank eingezahlt.") && message.contains("$ (-")) {
                try {
                    SheetUtils.setLine("\u00dcbersicht", "C31:D31", new String[]{"Ja", new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date())});
                } catch (IOException ignored) {
                }

                FBankCommand.checkTaxes = false;
            }
        }
    }

    public static void resetBomb() {
        BOMBE = false;
        OPERATION = false;

        PlayerUpdateListener.showdistance = false;
        PlayerUpdateListener.bombpos = new BlockPos(0, -1, 0);
        BombeCommand.planter = false;
    }
}
