package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.enums.ActivityType;
import me.rqmses.aktiboom.global.SheetUtils;
import me.rqmses.aktiboom.handlers.UploadHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
public class RPSessionCommand extends CommandBase implements IClientCommand {

    public static boolean session = false;
    public static List<String> imageHashes = new ArrayList<>();
    public static String partner = "";
    public static String art;

    @Override
    @Nonnull
    public String getName() {
        return "rpsession";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/roleplaysession [Kategorie] ([Partner])";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Arrays.asList("rpsitzung", "roleplaysession", "roleplaysitzung");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> targets = new ArrayList<>();
        if (!session) {
            if (args.length == 1) {
                targets = new ArrayList<>(Arrays.asList("Ausraub", "Menschenhandel", "Auftragsauslieferung", "Propaganda", "Rekruierung", "Sprengg\u00fcrteldrohung", "Geisel-RP", "Verhandlung"));
            } else if (args.length == 2) {
                for (NetworkPlayerInfo playerInfo : Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfoMap()) {
                    targets.add(String.valueOf(playerInfo.getGameProfile().getName()));
                }
            }
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
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        session = !session;
        if (session) {
            if (args.length < 1) {
                session = false;
                player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
            } else {
                art = args[0];
                if (args.length > 1) {
                    partner = args[1];
                }
                player.sendMessage(new TextComponentString(PREFIX + "Die RolePlay-Sitzung wurde gestartet."));
            }
        } else {
            if (imageHashes.size() == 0) {
                player.sendMessage(new TextComponentString(PREFIX + "Es wurden keine Screenshots get\u00e4tigt!"));
                imageHashes = new ArrayList<>();
                return;
            }
            String link;
            String category = "";
            try {
                link = "https://imgur.com/a/" + UploadHandler.uploadAlbumToID(imageHashes);
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Beim Speichern der Aufnahmen ist ein Fehler unterlaufen!"));
                session = !session;
                return;
            }
            imageHashes = new ArrayList<>();
            switch (art.toLowerCase()) {
                case "ausraub":
                    category = "Ausraub";
                    break;
                case "menschenhandel":
                    category = "Menschenhandel";
                    break;
                case "auftragsauslieferung":
                    category = "Auftragsauslieferung";
                    break;
                case "propaganda":
                    category = "Propaganda";
                    break;
                case "rektrutierung":
                    // Rechtschreibfehler im Aktinachweis
                    category = "Rektrutierung";
                    break;
                case "sprengg\u00fcrteldrohung":
                    category = "Sprengg\u00fcrteldrohung";
                    break;
                case "geisel-rp":
                case "geisel":
                    category = "Geisel-RP";
                    break;
                case "verhandlung":
                    category = "Verhandlung";
                    break;
            }
            try {
                SheetUtils.addValues(ActivityType.ROLEPLAY, new String[]{new SimpleDateFormat("dd.MM.yy").format(new Date()), partner, category, link});
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden."));
                return;
            }
            partner = "";
            player.sendMessage(new TextComponentString(PREFIX + "Die Aufnahmen der RolePlay-Sitzung wurden gespeichert."));
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
