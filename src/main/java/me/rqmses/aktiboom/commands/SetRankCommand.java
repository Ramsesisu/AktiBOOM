package me.rqmses.aktiboom.commands;

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
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.AktiBoom.RANK;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class SetRankCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "setrank";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/setrank [Name] [Rang]";
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (args.length == 1) {
            for (NetworkPlayerInfo playerInfo : Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfoMap()) {
                targets.add(String.valueOf(playerInfo.getGameProfile().getName()));
            }
        }
        if (args.length == 2) {
            targets = Arrays.asList("0", "1", "2", "3", "4", "5", "6");
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

            if (args.length > 1) {
                if (RANK >= 5) {
                    int rank;
                    int oldrank = SheetUtils.getRank(args[0]);

                    try {
                        rank = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(new TextComponentString(PREFIX + "Der Rang muss eine Zahl sein!"));
                        return;
                    }

                    if (rank >= 0 && rank <= 6) {
                        if (!SheetUtils.setRank(args[0], args[1])) {
                            player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " ist nicht in der Fraktion!"));
                        } else {
                            SheetUtils.tobecontinued = false;
                            SheetUtils.returnvalues = 0;

                            SheetUtils.addEditorRequests = new ArrayList<>();
                            SheetUtils.removeEditorRequests = new ArrayList<>();

                            List<String> memberlist = new ArrayList<>();
                            List<List<Object>> names;

                            try {
                                names = SheetUtils.getValueRange("\u00dcbersicht", "B4:B27").getValues();
                            } catch (IOException e) {
                                player.sendMessage(new TextComponentString(PREFIX + "Die Memberliste konnte nicht erfasst werden!"));
                                return;
                            }

                            for (List<Object> name : names) {
                                memberlist.add(name.get(0).toString());
                            }

                            String email = SheetUtils.getEmail(args[0]);

                            if (rank >= 3 && oldrank < 3) {
                                SheetUtils.addEditor("Kalender", "Termine", email);
                            } else if (rank < 3 && oldrank >= 3) {
                                SheetUtils.removeEditor("Kalender", "Termine", email);
                            }

                            if (rank >= 4 && oldrank < 4) {
                                SheetUtils.addEditor("Equiplog", "Equiplog", email);
                                SheetUtils.addEditor("Win/Lose Statistik", "Win/Lose", email);
                                SheetUtils.addEditor("Win/Lose Statistik", "Last", email);
                                SheetUtils.addEditor("Spot\u00fcbersicht", "Spots", email);
                                SheetUtils.addEditor("Kalender", "Datum", email);
                                SheetUtils.addEditor("Equiplog", "Besprechung", email);
                                for (String member : memberlist) {
                                    if (!Objects.equals(member, args[0])) {
                                        SheetUtils.addEditor(member, "Sheet", email);
                                    }
                                }
                            } else if (rank < 4 && oldrank >= 4) {
                                SheetUtils.removeEditor("Equiplog", "Equiplog", email);
                                SheetUtils.removeEditor("Win/Lose Statistik", "Win/Lose", email);
                                SheetUtils.removeEditor("Win/Lose Statistik", "Last", email);
                                SheetUtils.removeEditor("Spot\u00fcbersicht", "Spots", email);
                                SheetUtils.removeEditor("Kalender", "Datum", email);
                                SheetUtils.removeEditor("Equiplog", "Besprechung", email);
                                for (String member : memberlist) {
                                    if (!Objects.equals(member, args[0])) {
                                        SheetUtils.removeEditor(member, "Sheet", email);
                                    }
                                }
                            }

                            if (rank >= 5 && oldrank < 5) {
                                SheetUtils.addEditor("Kopierblatt", "Kopierblatt", email);
                                SheetUtils.addEditor("Equiplog", "E-Mails", email);
                                SheetUtils.addEditor("Win/Lose Statistik", "Date", email);
                                SheetUtils.addEditor("Regeln + Anmerkungen", "Regeln", email);
                                SheetUtils.addEditor("\u00dcbersicht", "Mitglieder", email);
                                SheetUtils.addEditor("\u00dcbersicht", "FBank", email);
                                for (String member : memberlist) {
                                    SheetUtils.addEditor(member, "Range", email);
                                }
                            } else if (rank < 5 && oldrank > 5) {
                                SheetUtils.removeEditor("Kopierblatt", "Kopierblatt", email);
                                SheetUtils.removeEditor("Equiplog", "E-Mails", email);
                                SheetUtils.removeEditor("Win/Lose Statistik", "Date", email);
                                SheetUtils.removeEditor("\u00dcbersicht", "Mitglieder", email);
                                SheetUtils.removeEditor("\u00dcbersicht", "FBank", email);
                                for (String member : memberlist) {
                                    SheetUtils.removeEditor(member, "Range", email);
                                }
                            }

                            player.sendMessage(new TextComponentString(PREFIX + "Der Rang von " + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " wurde zu " + TextFormatting.GOLD + "Rang-" + args[1] + TextFormatting.YELLOW + " aktualisiert."));

                            if (SheetUtils.tobecontinued) {
                                player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + "/continue" + TextFormatting.YELLOW + " um die verbleibenden Prozesse zu beenden."));
                                SheetUtils.tobecontinued = false;
                                SheetUtils.returnvalues = 0;
                            }
                        }
                    } else {
                        player.sendMessage(new TextComponentString(PREFIX + "Gib einen Rang zwischen 0-6 an!"));
                    }
                } else {
                    player.sendMessage(new TextComponentString(PREFIX + "Du bist kein Leader!"));
                }
            } else if (args.length == 1) {
                player.sendMessage(new TextComponentString(PREFIX + "Gib einen Rang an!"));
            } else {
                player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
            }
        }).start();
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