package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class FBankCommand extends CommandBase implements IClientCommand {

    public static boolean checkTaxes = false;

    @Override
    @Nonnull
    public String getName() {
        return "fbank";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/fbank Einzahlen/Auszahlen [Betrag]";
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (args.length == 1) {
            targets = Arrays.asList("Einzahlen", "Auszahlen");
        }
        for (String target : targets) {
            if (target.toUpperCase().startsWith(args[args.length-1].toUpperCase()))
                list.add(target);
        }
        return list;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            if (args.length > 1) {
                switch (args[0].toLowerCase()) {
                    case "einzahlen":
                        boolean taxes = false;
                        try {
                            if (SheetUtils.getValueRange("\u00dcbersicht", "C31:C31").getValues().toString().contains("Ja")) {
                                Calendar calendar = new GregorianCalendar();
                                if (calendar.get(Calendar.HOUR_OF_DAY) < 4) {
                                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                                }
                                calendar.set(Calendar.HOUR_OF_DAY, 4);
                                calendar.set(Calendar.MINUTE, 0);
                                calendar.set(Calendar.SECOND, 0);
                                Date restartDate = calendar.getTime();
                                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.GERMANY);
                                Date checkDate = formatter.parse(SheetUtils.getValueRange("\u00dcbersicht", "D31:D31").getValues().get(0).get(0).toString());

                                if (checkDate.after(restartDate)) {
                                    taxes = true;
                                } else {
                                    SheetUtils.setValues("\u00dcbersicht", "C31:D31", new String[]{"Nein", new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date())});
                                }
                            }
                        } catch (IOException | ParseException ignored) {
                        }

                        if (!taxes) {
                                player.sendChatMessage("/fbank Einzahlen " + args[1]);
                                checkTaxes = true;
                        } else {
                            player.sendMessage(new TextComponentString(PREFIX + "Die F-Bank hat aktuell Steuern!"));
                        }
                        break;
                    case "auszahlen":
                        player.sendChatMessage("/fbank Auszahlen " + args[1]);
                        break;
                    default:
                        player.sendMessage(new TextComponentString(PREFIX + getUsage(sender)));
                }
            } else {
                player.sendChatMessage("/fbank");
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