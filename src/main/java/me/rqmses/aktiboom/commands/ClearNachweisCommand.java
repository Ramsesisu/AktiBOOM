package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
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
import java.util.ArrayList;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.AktiBoom.RANK;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class ClearNachweisCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "clearnachweis";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/clearnachweis [Code]";
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        return new ArrayList<>();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            if (args.length > 0) {
                if (args[0].equals(player.getName())) {
                    if (RANK >= 5) {
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

                        for (String member : memberlist) {
                            try {
                                SheetUtils.copyRange(976896585, SheetUtils.getSheetID(member), 29, 0, 329, 10);
                            } catch (IOException e) {
                                player.sendMessage(new TextComponentString(PREFIX + "Die Aktivit\u00e4ten von " + TextFormatting.GOLD + member + TextFormatting.YELLOW + " konnten nicht zur\u00fcckgesetzt werden!"));
                            }
                        }

                        player.sendMessage(new TextComponentString(PREFIX + "Der Aktivit\u00e4tsnachweis wurde erfolgreich zur\u00fcckgesetzt."));
                        player.sendChatMessage("/f %INFO% :&6" + player.getName() + "&e hat den Aktivit\u00e4tsnachweis zur\u00fcckgesetzt!");
                    } else {
                        player.sendMessage(new TextComponentString(PREFIX + "Du bist kein Leader!"));
                    }
                } else {
                    player.sendMessage(new TextComponentString(PREFIX + "Gib deinen eigenen Namen an!"));
                }
            } else {
                player.sendMessage(new TextComponentString(PREFIX + "Gib sicherheitshalber deinen eigenen Namen zur Best\u00e4tigung an!"));
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