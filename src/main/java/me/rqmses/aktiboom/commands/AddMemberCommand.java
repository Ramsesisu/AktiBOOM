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
public class AddMemberCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "addmember";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/addmember [Name] [E-Mail]";
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
                    try {
                        int sheetindex = SheetUtils.getSheetIndex() - 1;
                        SheetUtils.copySheet(976896585, args[0], sheetindex);
                        SheetUtils.setValues(args[0], "F21:H23", new String[]{args[0]});

                        int index = SheetUtils.getValueRange("\u00dcbersicht", "B4:B27").getValues().size() + 4;
                        SheetUtils.setValues("\u00dcbersicht", "A" + index + ":Q" + index + "", new String[]{"0", args[0], "=IFNA(IFS(A" + index + "=6; WAHR;A" + index + "=5; WAHR; A" + index + "=4; WAHR; UND(A" + index + "=3;M" + index + ">=6;O" + index + ">=10000;P" + index + ">=1; Q" + index + ">=3); WAHR; UND(A" + index + "=2;M" + index + ">=5;O" + index + ">=8000;P" + index + ">=1; Q" + index + ">=3); WAHR; UND(A" + index + "=1;M" + index + ">=5;O" + index + ">=6000; Q" + index + ">=3); WAHR; UND(A" + index + "=0;M" + index + ">=4;O" + index + ">=4000; Q" + index + ">=3); WAHR); FALSCH)", "=" + args[0] + "!C6", "=" + args[0] + "!C7", "=" + args[0] + "!C9", "=" + args[0] + "!C10", "=" + args[0] + "!C11", "=" + args[0] + "!C8", "=" + args[0] + "!C13", "=" + args[0] + "!C12", "=" + args[0] + "!C14", "=SUMME(D" + index + ":L" + index + ")", "=" + args[0] + "!H13", "=" + args[0] + "!B18", "=" + args[0] + "!B24", "=" + args[0] + "!B27"});

                        SheetUtils.addValues("Equiplog", "C36:E60", new String[]{args[0], args[1]});
                    } catch (IOException e) {
                        player.sendMessage(new TextComponentString(PREFIX + "Das Tabellenblatt konnte nicht erstellt werden!"));
                        return;
                    }

                    List<String> leaderteam = new ArrayList<>();
                    List<String> leaders = new ArrayList<>();
                    try {
                        List<List<Object>> memberlist = SheetUtils.getValueRange("\u00dcbersicht", "B4:B27").getValues();

                        for (List<Object> member : memberlist) {
                            int rank = SheetUtils.getRank(member.get(0).toString());
                            if (rank == 4) {
                                leaderteam.add(member.get(0).toString());
                            }
                            if (rank >= 5) {
                                leaders.add(member.get(0).toString());
                            }
                        }
                    } catch (IOException e) {
                        player.sendMessage(new TextComponentString(PREFIX + "Das Leaderteam konnte nicht erfasst werden!!"));
                    }

                    try {
                        List<String> permissions = new ArrayList<>();

                        for (String leader : leaders) {
                            String email = SheetUtils.getEmail(leader);
                            if (!Objects.equals(email, "")) {
                                permissions.add(email);
                            }
                        }
                        SheetUtils.addProtectedRange(SheetUtils.getSheetID(args[0]), 0, 0, 29, 12, permissions);

                        permissions.add(args[1]);

                        for (String leader : leaderteam) {
                            String email = SheetUtils.getEmail(leader);
                            if (!Objects.equals(email, "")) {
                                permissions.add(email);
                            }
                        }
                        SheetUtils.addProtectedSheet(SheetUtils.getSheetID(args[0]), permissions);
                    } catch (IOException e) {
                        player.sendMessage(new TextComponentString(PREFIX + "Es konnten keine Rechte auf das Tabellenblatt \u00fcbertragen werden!"));
                        return;
                    }

                    player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + args[0] + TextFormatting.YELLOW + " wurde zum Aktivit\u00e4tsnachweis hinzugef\u00fcgt."));
                } else {
                    player.sendMessage(new TextComponentString(PREFIX + "Du bist kein Leader!"));
                }
            } else if (args.length == 0) {
                player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
            } else {
                player.sendMessage(new TextComponentString(PREFIX + "Gib eine E-Mail an!"));
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