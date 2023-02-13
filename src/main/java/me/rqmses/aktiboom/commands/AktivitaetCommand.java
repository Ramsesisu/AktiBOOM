package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.enums.ActivityType;
import me.rqmses.aktiboom.handlers.ScreenHandler;
import me.rqmses.aktiboom.utils.LocationUtils;
import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class AktivitaetCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "aktivit\u00e4t";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/aktivit\u00e4t [Kategorie] ([weitere Argumente])";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Arrays.asList("akti", "activity");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> targets = new ArrayList<>();
        for (NetworkPlayerInfo playerInfo : Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfoMap()) {
            targets.add(String.valueOf(playerInfo.getGameProfile().getName()));
        }

        if (args.length == 1) {
            targets = new ArrayList<>(Arrays.asList("Gebietseinnahme", "Entf\u00fchrung", "Flugzeugentf\u00fchrung",
                    "UBoot-Entf\u00fchrung", "UBahn-Entf\u00fchrung", "Geiselnahme", "Bombe", "Sprengg\u00fcrtel",
                    "Menschenhandel", "Ausraub", "Autobombe", "Training", "Waffentransport", "Zuzahlung", "Bombenspot",
                    "RP-Event", "Spende", "Drohung", "Geisel", "Auftragsauslieferung", "Schutzgeld", "Tuning"));
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("Auftragsauslieferung")) {
                targets = new ArrayList<>(Collections.singletonList("0"));
            }
            if (args[0].equalsIgnoreCase("Bombenspot")) {
                targets = new ArrayList<>(Collections.singletonList("0"));
            }
            if (args[0].equalsIgnoreCase("Drohung")) {
                targets = new ArrayList<>(Collections.singletonList("0"));
            }
            if (args[0].equalsIgnoreCase("Geisel")) {
                targets = new ArrayList<>(Collections.singletonList("0"));
            }
            if (args[0].equalsIgnoreCase("RP-Event")) {
                targets = new ArrayList<>(Collections.singletonList("0"));
            }
            if (args[0].equalsIgnoreCase("Schutzgeld")) {
                targets = new ArrayList<>(Collections.singletonList("0"));
            }
            if (args[0].equalsIgnoreCase("Spende")) {
                targets = new ArrayList<>(Collections.singletonList("0"));
            }
            if (args[0].equalsIgnoreCase("Waffentransport")) {
                targets = new ArrayList<>(Collections.singletonList("0"));
            }
            if (args[0].equalsIgnoreCase("Zuzahlung")) {
                targets = new ArrayList<>(Collections.singletonList("0"));
            }
            if (args[0].equalsIgnoreCase("Tuning")) {
                targets = new ArrayList<>(Collections.singletonList("0"));
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("Gebietseinnahme")) {
                targets = new ArrayList<>(LocationUtils.getLocs());
            }
            if (args[0].equalsIgnoreCase("Entf\u00fchrung")) {
                targets = new ArrayList<>(LocationUtils.getLocs());
            }
            if (args[0].equalsIgnoreCase("Flugzeugentf\u00fchrung")) {
                targets = new ArrayList<>(LocationUtils.getLocs());
            }
            if (args[0].equalsIgnoreCase("UBoot-Entf\u00fchrung")) {
                targets = new ArrayList<>(LocationUtils.getLocs());
            }
            if (args[0].equalsIgnoreCase("UBahn-Entf\u00fchrung")) {
                targets = new ArrayList<>(LocationUtils.getLocs());
            }
            if (args[0].equalsIgnoreCase("Geiselnahme")) {
                targets = new ArrayList<>(LocationUtils.getLocs());
            }
            if (args[0].equalsIgnoreCase("Sprengg\u00fcrtel")) {
                targets = new ArrayList<>(LocationUtils.getLocs());
            }
            if (args[0].equalsIgnoreCase("Training")) {
                targets = new ArrayList<>(LocationUtils.getLocs());
            }
        }
        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("Bombe")) {
                targets = new ArrayList<>(LocationUtils.getLocs());
            }
            if (args[0].equalsIgnoreCase("Ausraub")) {
                targets = new ArrayList<>(Collections.singletonList("0"));
            }
            if (args[0].equalsIgnoreCase("Menschenhandel")) {
                targets = new ArrayList<>(Collections.singletonList("0"));
            }
            if (args[0].equalsIgnoreCase("Autobombe")) {
                targets = new ArrayList<>(Collections.singletonList("0"));
            }
            if (args[0].equalsIgnoreCase("Sprengg\u00fcrtel")) {
                targets = new ArrayList<>(Collections.singletonList("0"));
            }
            if (args[0].equalsIgnoreCase("Gebietseinnahme")) {
                targets = new ArrayList<>(Collections.singletonList("0"));
            }
            if (args[0].equalsIgnoreCase("Entf\u00fchrung")) {
                targets = new ArrayList<>(Collections.singletonList("0"));
            }
            if (args[0].equalsIgnoreCase("Geiselnahme")) {
                targets = new ArrayList<>(Collections.singletonList("0"));
            }
        }
        if (args.length == 5) {
            if (args[0].equalsIgnoreCase("Sprengg\u00fcrtel")) {
                targets = new ArrayList<>(Arrays.asList("Ja", "Nein"));
            }
            if (args[0].equalsIgnoreCase("Autobombe")) {
                targets = new ArrayList<>(Arrays.asList("Ja", "Nein"));
            }
        }
        Collections.sort(targets);
        for (String target : targets) {
            if (target.toUpperCase().startsWith(args[args.length-1].toUpperCase()))
                list.add(target);
        }
        return list;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (args.length > 0) {
            ActivityType type;

            final String[] leading = {"FALSCH"};
            final String[] dead = new String[1];
            final String[] category = {""};
            final String[] forum = {""};
            int argslenght;
            final String[] money = {""};
            String usage;
            String date = new SimpleDateFormat("dd.MM.yy").format(new Date());

            TextComponentString errormsg = new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden.");

            switch (args[0].toLowerCase()) {
                case "gebietseinnahme":
                    type = ActivityType.GEBIETSEINNAHMEN;
                    argslenght = 2;
                    usage = "/aktivit\u00e4t Gebietseinahme [Leiter] [Ort] ([Einnahme]) ([Protokoll])";
                    break;
                case "ubahn-entf\u00fchrung":
                case "uboot-entf\u00fchrung":
                case "flugzeugentf\u00fchrung":
                case "entf\u00fchrung":
                    type = ActivityType.ENTFUEHRUNGEN;
                    argslenght = 2;
                    usage = "/aktivit\u00e4t Entf\u00fchrung [Leiter] [Ort] ([Einnahme]) ([Protokoll])";
                    break;
                case "geiselnahme":
                    type = ActivityType.GEISELNAHMEN;
                    argslenght = 2;
                    usage = "/aktivit\u00e4t Geiselnahme [Leiter] [Ort] ([Einnahme]) ([Protokoll])";
                    break;
                case "bombe":
                    type = ActivityType.BOMBEN;
                    argslenght = 3;
                    usage = "/aktivit\u00e4t Bombe [Leiter] [Leger] [Ort] ([Protokoll])";
                    break;
                case "sprengg\u00fcrtel":
                    type = ActivityType.SPRENGGUERTEL;
                    argslenght = 3;
                    usage = "/aktivit\u00e4t Sprengg\u00fcrtel [K\u00e4ufer] [Ort] [Einzahlung] ([Tot])";
                    break;
                case "menschenhandel":
                case "ausraub":
                    if (args[0].equalsIgnoreCase("Ausraub")) {
                        category[0] = "Ausraub";
                    } else {
                        category[0] = "Menschenh.";
                    }
                    type = ActivityType.MENSCHENHANDEL_AUSRAUB;
                    argslenght = 3;
                    usage = "/aktivit\u00e4t Menschenhandel/Ausraub [Opfer] [Partner] [Einnahme]";
                    break;
                case "autobombe":
                    type = ActivityType.AUTOBOMBEN;
                    argslenght = 3;
                    usage = "/aktivit\u00e4t Autobombe [K\u00e4ufer] [Opfer] [Einnahme] ([Explodiert])";
                    break;
                case "training":
                    type = ActivityType.TRAININGS;
                    argslenght = 2;
                    usage = "/aktivit\u00e4t Training [Leiter] [Ort] ([Protokoll])";
                    break;
                case "waffentransport":
                case "zuzahlung":
                case "bombenspot":
                case "spot":
                case "rp-event":
                case "tuning":
                case "event":
                case "spende":
                case "drohung":
                case "sprengidrohung":
                case "schutzgeldforderung":
                case "schutzgeld":
                case "geisel":
                case "auftragsauslieferung":
                    type = ActivityType.SONSTIGES;
                    argslenght = 1;
                    usage = "/aktivit\u00e4t Sonstiges [Einnahme]";
                    break;
                default:
                    player.sendMessage(new TextComponentString(PREFIX + args[0] + " ist keine Kategorie!"));
                    return;
            }

            if (args.length < (argslenght + 1)) {
                player.sendMessage(new TextComponentString(PREFIX + usage));
                return;
            }

            String link = ScreenHandler.handleFile();

            new Thread(() -> {
                boolean success;

                switch (type) {
                    case ENTFUEHRUNGEN:
                    case GEBIETSEINNAHMEN:
                    case GEISELNAHMEN:
                        if (args.length > 3) {
                            leading[0] = "WAHR";
                            money[0] = args[3];
                            if (args.length > 4) {
                                forum[0] = args[4];
                            }
                        }
                        try {
                            success = SheetUtils.addActivity(type, new String[]{date, leading[0], args[1], args[2], money[0], link, forum[0]});
                        } catch (IOException e) {
                            player.sendMessage(errormsg);
                            return;
                        }
                        break;
                    case BOMBEN:
                        if (args.length > 4) {
                            leading[0] = "WAHR";
                            forum[0] = args[4];
                        }
                        try {
                            success = SheetUtils.addActivity(type, new String[]{date, leading[0], args[1], args[2], args[3], link, forum[0]});
                        } catch (IOException e) {
                            player.sendMessage(errormsg);
                            return;
                        }
                        break;
                    case SPRENGGUERTEL:
                    case AUTOBOMBEN:
                        if (args.length < 5) {
                            dead[0] = "Ja";
                        } else {
                            dead[0] = args[4];
                        }
                        try {
                            success = SheetUtils.addActivity(type, new String[]{date, args[1], args[2], args[3], dead[0], link});
                        } catch (IOException e) {
                            player.sendMessage(errormsg);
                            return;
                        }
                        break;
                    case MENSCHENHANDEL_AUSRAUB:
                        try {
                            success = SheetUtils.addActivity(type, new String[]{date, category[0], args[1], args[2], args[3], link});
                        } catch (IOException e) {
                            player.sendMessage(errormsg);
                            return;
                        }
                        break;
                    case TRAININGS:
                        if (args.length > 3) {
                            leading[0] = "WAHR";
                            forum[0] = args[3];
                        }
                        try {
                            success = SheetUtils.addActivity(type, new String[]{date, leading[0], args[1], args[2], link, forum[0]});
                        } catch (IOException e) {
                            player.sendMessage(errormsg);
                            return;
                        }
                        break;
                    case SONSTIGES:
                        switch (args[0].toLowerCase()) {
                            case "waffentransport":
                                category[0] = "Waffentransport";
                                break;
                            case "zuzahlung":
                                category[0] = "Zuzahlung";
                                break;
                            case "bombenspot":
                            case "spot":
                                category[0] = "Bombenspot";
                                break;
                            case "rp-event":
                            case "event":
                                category[0] = "RP-Event";
                                break;
                            case "spende":
                                category[0] = "Spende";
                                break;
                            case "drohung":
                            case "sprengidrohung":
                            case "schutzgeldforderung":
                            case "schutzgeld":
                                category[0] = "Sprengi-Drohung/ Schutzgeldforderung";
                                break;
                            case "geisel":
                                category[0] = "Geisel";
                                break;
                            case "auftragsauslieferung":
                                category[0] = "Auftragsauslieferung";
                                break;
                            case "tuning":
                                category[0] = "Tuning";
                                break;
                        }
                        try {
                            success = SheetUtils.addActivity(type, new String[]{date, args[1], category[0], link});
                        } catch (IOException e) {
                            player.sendMessage(errormsg);
                            return;
                        }
                        break;
                    default:
                        player.sendMessage(new TextComponentString(PREFIX + "Beim Eintragen der Aktivit\u00e4t ist ein Fehler unterlaufen."));
                        return;
                }
                if (success) {
                    player.sendMessage(new TextComponentString(PREFIX + "Die " + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + "-" + "Aktivit\u00e4t wurde erfolgreich eingetragen."));
                } else {
                    player.sendMessage(new TextComponentString(PREFIX + "Die entsprechende Kategorie ist \u00fcberf\u00fcllt."));
                }
            }).start();
        } else {
            player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
        }
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
