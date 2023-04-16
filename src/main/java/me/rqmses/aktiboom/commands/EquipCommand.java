package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.enums.EquipType;
import me.rqmses.aktiboom.enums.InformationType;
import me.rqmses.aktiboom.handlers.ScreenHandler;
import me.rqmses.aktiboom.listeners.ContainerListener;
import me.rqmses.aktiboom.utils.SheetUtils;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.enums.ActivityType.EQUIP;

@SuppressWarnings("ALL")
public class EquipCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "equip";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/equip [Gegenstand] ([Einzahlung])";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("ausr\u00fcstung");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (args.length == 1) {
            targets = Arrays.asList("Mp5", "Pistole", "Kevlar", "Schwere-Kevlar", "RPG-7", "Nachzahlung");
        }
        if (args.length == 2) {
            targets = Collections.singletonList("0");
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

        NetHandlerPlayClient netHandlerPlayClient = Minecraft.getMinecraft().getConnection();
        if (netHandlerPlayClient != null) {
            if (netHandlerPlayClient.getNetworkManager().channel().remoteAddress().toString().toLowerCase().contains("unicacity.de")) {
                if (args.length > 0) {
                    EquipType equiptype = EquipType.KEVLAR;
                    String price = "0";
                    int discount = 0;

                    switch (args[0].toLowerCase()) {
                        case "mp5":
                            equiptype = EquipType.MP5;
                            price = "550";
                            break;
                        case "pistole":
                            equiptype = EquipType.PISTOLE;
                            price = "350";
                            break;
                        case "kevlar":
                        case "kev":
                            price = "1450";
                            discount = 800;
                            break;
                        case "schwere-kevlar":
                        case "skev":
                            equiptype = EquipType.SCHWEREKEVLAR;
                            price = "2200";
                            discount = 800;
                            break;
                        case "rpg-7":
                        case "rpg":
                            equiptype = EquipType.RPG7;
                            price = "13000";
                            break;
                        case "nachzahlung":
                            equiptype = EquipType.NACHZAHLUNG;
                            if (args.length < 2) {
                                player.sendMessage(new TextComponentString(PREFIX + "Gib einen Betrag an!"));
                                return;
                            }
                            break;
                        default:
                            player.sendMessage(new TextComponentString(PREFIX + args[0] + " ist kein Gegenstand!"));
                            return;
                    }

                    if (args.length > 1) {
                        price = args[1];
                    }

                    String link = ScreenHandler.handleFile();

                    String finalPrice = price;
                    EquipType finalEquiptype = equiptype;
                    int finalDiscount = discount;
                    new Thread(() -> {
                        boolean success;
                        try {
                            success = SheetUtils.addActivity(EQUIP, new String[]{new SimpleDateFormat("dd.MM.yy").format(new Date()), finalPrice, finalEquiptype.getName(), link});
                        } catch (IOException e) {
                            player.sendMessage(new TextComponentString(PREFIX + "Es konnte keine Verbindung zum Aktivit\u00e4tsnachweis hergestellt werden."));
                            return;
                        }

                        if (success) {
                            if (finalDiscount != 0) {
                                try {
                                    String stringdiscount = SheetUtils.getValueRange(InformationType.DISCOUNT.getSheet(), InformationType.DISCOUNT.getRange()).getValues().get(0).get(0).toString().replace("-", "").replace(".", "").replace("$", "");
                                    SheetUtils.setLine(InformationType.DISCOUNT.getSheet(), InformationType.DISCOUNT.getRange(), new String[]{String.valueOf(Integer.parseInt(stringdiscount) + finalDiscount)});
                                } catch (IOException ignored) {
                                }
                            }
                            player.sendMessage(new TextComponentString(PREFIX + "Die " + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " wurde erfolgreich eingetragen."));
                        } else {
                            player.sendMessage(new TextComponentString(PREFIX + "Die entsprechende Kategorie ist \u00fcberf\u00fcllt."));
                        }
                    }).start();
                } else {
                    player.sendChatMessage("/equip");

                    ContainerListener.verifysprengi = false;
                    ContainerListener.verifyrpg = false;
                }
            } else {
                StringBuilder text = new StringBuilder();
                for (String arg : args) {
                    text.append(" ").append(arg);
                }
                player.sendChatMessage("/equip" + text);
            }
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
