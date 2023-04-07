package me.rqmses.aktiboom.listeners;

import me.rqmses.aktiboom.commands.*;
import me.rqmses.aktiboom.handlers.ConfigHandler;
import me.rqmses.aktiboom.handlers.SoundHandler;
import me.rqmses.aktiboom.utils.*;
import me.rqmses.aktiboom.utils.guis.GameGui;
import me.rqmses.aktiboom.utils.guis.containers.ChessContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import static me.rqmses.aktiboom.handlers.ConfigHandler.*;

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

        if (message.contains(": %SECCHAT% :")) {
            if (SEC) {
                String[] contents = message.split(":", 3);
                String name = contents[0].split(" ")[1].replace("[UC]", "");
                String text = contents[2];

                final String[] secrankname = new String[1];
                if (secplayerranks.containsKey(name)) {
                    secrankname[0] = secplayerranks.get(name);
                } else {
                    new Thread(() -> {
                        String secrank = SheetUtils.getSECRank(name);
                        secrankname[0] = secrank;
                    }).start();
                    secplayerranks.putIfAbsent(name, secrankname[0]);
                }

                event.setMessage(new TextComponentString(FormatUtils.getColor(secchatprefix) + "" + TextFormatting.ITALIC + "SEC " + FormatUtils.getColor(secchatprefix) + secrankname[0] + " " + name + TextFormatting.DARK_GRAY + ":" + FormatUtils.getColor(secchatmessage) + EncryptionUtils.decode(text, KEYS.get(0))));
            } else {
                event.setCanceled(true);
            }
        }
        if (message.contains(": %LEADER% :")) {
            if (RANK >= 4) {
                String[] contents = message.split(":", 3);
                String name = contents[0].split(" ")[1].replace("[UC]", "");
                String rank = contents[0].split(" ")[0];
                String text = contents[2];

                event.setMessage(new TextComponentString(FormatUtils.getColor(leaderchatprefix) + "" + TextFormatting.ITALIC + "Leader " + FormatUtils.getColor(leaderchatprefix) + rank + " " + name + TextFormatting.DARK_GRAY + ":" + FormatUtils.getColor(leaderchatmessage) + EncryptionUtils.decode(text, KEYS.get(1))));
            } else {
                event.setCanceled(true);
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
                    SchutzCommand.syncList();
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

        if (message.equals(" hat dir deine Kommunikationsger\u00e4te wiedergegeben.")) {
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

        if (message.equals("[Equip] Du hast dir eine RPG-7 equippt!")) {
            player.sendChatMessage("/f %INFO% :&6" + player.getName() + "&e hat sich eine &6&lRPG-7 &eequippt!");
        }

        if (message.equals("[Equip] Du hast dir ein Sprengg\u00fcrtel equippt!")) {
            player.sendChatMessage("/f %INFO% :&6" + player.getName() + "&e hat sich einen &6&lSprengg\u00fcrtel &eequippt!");
        }

        if (message.equals("[Waffentransport] Du hast eine Waffenlieferung abgegeben.")) {
            player.sendChatMessage("/f %INFO% :&6" + player.getName() + "&e hat das Waffenlager aufgef\u00fcllt!");
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
                        PlayerJoinListener.refresh();

                        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(PREFIX + "Alle Daten wurden neu geladen."));
                    }
                }).start();
            }
        }

        if (message.contains(": %INV% :")) {
            event.setCanceled(true);
            new Thread(() -> {
                String[] contents = message.split(":");
                if (contents[2].contains(player.getName())) {
                    if (contents.length == 4) {
                        if (SheetUtils.getRank(contents[0].split(" ")[1].replace("[UC]", "")) >= 4) {

                            StringBuilder content = new StringBuilder();
                            for (ItemStack item : player.inventory.mainInventory) {
                                int id = Item.getIdFromItem(item.getItem());
                                if (id != 0) {
                                    content.append(" ").append(id).append("/").append(item.getCount());
                                }
                            }

                            if (!content.toString().contains("/")) {
                                content = new StringBuilder().append(" ").append("0").append("/").append("0");
                            }

                            player.sendChatMessage("/f %INV% : " + contents[0].split(" ")[1].replace("[UC]", "") + " : " + contents[3].replace(" ", "") + " :" + content);
                        }
                    } else if (contents.length == 5) {
                        if (contents[0].split(" ")[1].contains(InvSeeCommand.checkplayer)) {
                            if (contents[3].replace(" ", "").equals(InvSeeCommand.code)) {
                                InvSeeCommand.check = true;

                                for (String content : contents[4].replaceFirst(" ", "").split(" ")) {
                                    String[] item = content.split("/");
                                    String name = Objects.requireNonNull(Objects.requireNonNull(Item.getByNameOrId(item[0])).getRegistryName()).toString().replace("minecraft:", "");
                                    if (name.equals("air")) {
                                        player.sendMessage(new TextComponentString(TextFormatting.GRAY + " Das Inventar ist leer!"));
                                    } else {
                                        player.sendMessage(new TextComponentString(TextFormatting.GRAY + " " + name + TextFormatting.DARK_GRAY + " [" + item[1] + "]"));
                                    }
                                }
                            }
                        }
                    }
                }
            }).start();
        }

        if (message.contains(": %DRUGS% :")) {
            event.setCanceled(true);
            new Thread(() -> {
                String[] contents = message.split(":");
                if (contents[2].contains(player.getName())) {
                    if (contents.length == 4) {
                        if (SheetUtils.getRank(contents[0].split(" ")[1].replace("[UC]", "")) >= 4) {
                            InformationUtils.getStats("  - Inventar: ");

                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    player.sendChatMessage("/f %DRUGS% : " + contents[0].split(" ")[1].replace("[UC]", "") + " : " + contents[3].replace(" ", "") + " :" + result);
                                }
                            }, 500);
                        }
                    } else if (contents.length == 5) {
                        if (contents[0].split(" ")[1].contains(CheckDrugsCommand.checkplayer)) {
                            if (contents[3].replace(" ", "").equals(CheckDrugsCommand.code)) {
                                CheckDrugsCommand.check = true;

                                player.sendMessage(new TextComponentString(TextFormatting.GRAY + "  Menge: " + TextFormatting.YELLOW + contents[4].replace(" ", "")));
                            }
                        }
                    }
                }
            }).start();
        }

        if (message.contains(": %POS% :")) {
            event.setCanceled(true);
            new Thread(() -> {
                String[] contents = message.split(":");
                if (contents[2].contains(player.getName())) {
                    if (contents.length == 4) {
                        if (SheetUtils.getRank(contents[0].split(" ")[1].replace("[UC]", "")) >= 4) {
                            BlockPos pos = player.getPosition();
                            player.sendChatMessage("/f %POS% : " + contents[0].split(" ")[1].replace("[UC]", "") + " : " + contents[3].replace(" ", "") + " : " + pos.getX() + "/" + pos.getY() + "/" + pos.getZ());
                        }
                    } else if (contents.length == 5) {
                        if (contents[0].split(" ")[1].contains(PositionCommand.checkplayer)) {
                            if (contents[3].replace(" ", "").equals(PositionCommand.code)) {
                                PositionCommand.check = true;

                                String navi = contents[4].replace(" ", "");
                                player.sendMessage(TextUtils.clickable(TextFormatting.GRAY, "  Koordinaten: " + TextFormatting.YELLOW + navi, TextFormatting.RED + "Route anzeigen", ClickEvent.Action.RUN_COMMAND, "/navi " + contents[4].replace(" ", "")));
                                String[] coords = navi.split("/");
                                player.sendMessage(new TextComponentString(TextFormatting.GRAY + "  Navipunkt: " + TextFormatting.YELLOW + LocationUtils.getNearestNavi(new BlockPos(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2])))));
                            }
                        }
                    }
                }
            }).start();
        }

        if (message.contains(": %AWAY% :")) {
            event.setCanceled(true);
            if (remote) {
                new Thread(() -> {
                    String[] contents = message.split(":");
                    if (contents[2].replace(" ", "").equals(player.getName())) {
                        if (SheetUtils.getRank(contents[0].split(" ")[1].replace("[UC]", "")) >= 4) {
                            Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getNetworkManager().closeChannel(new TextComponentString("Du wurdest von " + TextFormatting.BOLD + contents[0].split(" ")[1].replace(" ", "") + TextFormatting.RESET + " offline geschickt!"));
                        }
                    }
                }).start();
            }
        }

        if (message.contains(": %AFK% :")) {
            event.setCanceled(true);
            new Thread(() -> {
                String[] contents = message.split(":");
                if (contents[2].replace(" ", "").equals(player.getName())) {
                    if (SheetUtils.getRank(contents[0].split(" ")[1].replace("[UC]", "")) >= 4) {
                        player.sendChatMessage("/afk");
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
                    SheetUtils.setValues("Equiplog", "D" + line + ":D" + line, new String[]{String.valueOf(current + added)});
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
                        SheetUtils.setValues("\u00dcbersicht", "H31:H31", new String[]{fbank});
                    } catch (IOException ignored) {
                    }
                }).start();
            }
        }

        if (message.startsWith(searchprefix)) {
            result = message.replace(searchprefix, "");
        }

        if (hide) {
            if (message.equals("")) {
                event.setCanceled(true);
            }
            if (message.startsWith("======") && message.endsWith("======")) {
                event.setCanceled(true);
            }
            if (message.startsWith("  - Level: ")) {
                event.setCanceled(true);
            }
            if (message.startsWith("  - Status: ")) {
                event.setCanceled(true);
            }
            if (message.startsWith("  - Inventar: ")) {
                event.setCanceled(true);
            }
            if (message.startsWith("  - Wanted Punkte: ")) {
                event.setCanceled(true);
            }
            if (message.startsWith("  - Geld: ")) {
                event.setCanceled(true);
            }
            if (message.startsWith("  - Verwarnungen: ")) {
                event.setCanceled(true);
            }
            if (message.startsWith("  - Zeit seit PayDay: ")) {
                event.setCanceled(true);
            }
            if (message.startsWith("  - Experience: ")) {
                event.setCanceled(true);
            }
            if (message.startsWith("  - Fraktion: ")) {
                event.setCanceled(true);
            }
            if (message.startsWith("  - Haus: ")) {
                event.setCanceled(true);
            }
            if (message.startsWith("  - Beruf: ")) {
                event.setCanceled(true);
            }
            if (message.startsWith("  - Votepoints: ")) {
                event.setCanceled(true);
            }
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
                            SheetUtils.setValues("Equiplog", "D" + line + ":D" + line, new String[]{amount});
                        } catch (IOException ignored) {
                        }
                    }).start();
                }
            }
        }

        if (FBankCommand.checkTaxes) {
            if (message.startsWith("[F-Bank] " + player.getName() + " hat ") && message.endsWith("$) in die F-Bank eingezahlt.") && message.contains("$ (-")) {
                try {
                    SheetUtils.setValues("\u00dcbersicht", "C31:D31", new String[]{"Ja", new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date())});
                } catch (IOException ignored) {
                }

                FBankCommand.checkTaxes = false;
            }
        }
    }

    public static void resetBomb() {
        BOMBE = false;

        PlayerUpdateListener.showdistance = false;
        PlayerUpdateListener.bombpos = new BlockPos(0, -1, 0);
        BombeCommand.planter = false;
    }
}
