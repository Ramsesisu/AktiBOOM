package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.SheetUtils;
import me.rqmses.aktiboom.utils.TimeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class WirtschaftCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "wirtschaft";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/wirtschaft";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("income");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            List<List<Object>> wins;
            List<List<Object>> loses;
            List<List<Object>> activities;
            List<List<Object>> equipment;
            List<List<Object>> payments;
            List<List<Object>> discounts;
            List<List<Object>> wages;
            List<List<Object>> actual;
            try {
                wins = SheetUtils.getValueRange("Win/Lose Statistik", "C6:C64").getValues();
                loses = SheetUtils.getValueRange("Win/Lose Statistik", "E6:E64").getValues();
                activities = SheetUtils.getValueRange("\u00dcbersicht", "E39:G39").getValues();
                equipment = SheetUtils.getValueRange("Equiplog", "D4:D27").getValues();
                payments = SheetUtils.getValueRange("Equiplog", "C4:C27").getValues();
                discounts = SheetUtils.getValueRange("Equiplog", "D28:D28").getValues();
                wages = SheetUtils.getValueRange("Equiplog", "D977:D1000").getValues();
                actual = SheetUtils.getValueRange("\u00dcbersicht", "H30:H31").getValues();
            } catch (IOException e) {
                player.sendMessage(new TextComponentString("Die Statistiken konnten nicht abgerufen werden!"));
                return;
            }

            LocalDateTime meetingdate = TimeUtils.dateOfMeeting().withHour(0);

            int bombamount = 0;

            int winamount = 0;
            int bombincome = 0;
            for (List<Object> win : wins) {
                if (!Objects.equals(win.get(0).toString(), "Gewonnen")) {
                    String date = win.get(0).toString().split(" ")[1].replace("(", "").replace(")", "");
                    LocalDateTime bombdate = meetingdate.withDayOfMonth(Integer.parseInt(date.split("\\.")[0])).withMonth(Integer.parseInt(date.split("\\.")[1]));

                    if (bombdate.withHour(1).isAfter(meetingdate)) {
                        bombamount++;
                        winamount++;
                        bombincome += 10000;
                    }
                }
            }

            for (List<Object> lose : loses) {
                if (!Objects.equals(lose.get(0).toString(), "Verloren")) {
                    String date = lose.get(0).toString().split(" ")[1].replace("(", "").replace(")", "");
                    LocalDateTime bombdate = meetingdate.withDayOfMonth(Integer.parseInt(date.split("\\.")[0])).withMonth(Integer.parseInt(date.split("\\.")[1]));

                    if (bombdate.withHour(1).isAfter(meetingdate)) {
                        bombamount++;
                    }
                }
            }

            int activitiesincome = Integer.parseInt(activities.get(0).get(0).toString().replace(".", "").replace("$", ""));

            int equipmentspent = 0;
            for (List<Object> equip : equipment) {
                if (equip.size() != 0) {
                    equipmentspent += Integer.parseInt(equip.get(0).toString().replace(".", "").replace("$", ""));
                }
            }

            int paymentincome = 0;
            for (List<Object> payment : payments) {
                paymentincome += Integer.parseInt(payment.get(0).toString().replace(".", "").replace("$", ""));
            }

            int discount = Integer.parseInt(discounts.get(0).get(0).toString().replace("-", "").replace(".", "").replace("$", ""));

            int wagesspent = 0;
            for (List<Object> wage : wages) {
                if (wage.size() != 0) {
                    wagesspent += Integer.parseInt(wage.get(0).toString().replace(".", "").replace("$", ""));
                }
            }

            TextFormatting color;
            player.sendMessage(new TextComponentString(PREFIX + "\u00d6konomie der F-Bank seit " + TextFormatting.GOLD + meetingdate.getDayOfMonth() + "." + meetingdate.getMonthValue() + "." + meetingdate.getYear() + TextFormatting.YELLOW + ":"));
            player.sendMessage(new TextComponentString(""));

            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Bomben: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Siege: " + TextFormatting.YELLOW + winamount + TextFormatting.DARK_GRAY + " (" + TextFormatting.GRAY + bombamount + TextFormatting.DARK_GRAY + ")"));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Entgelt: " + TextFormatting.YELLOW + NumberFormat.getInstance().format(bombincome).replace(",", ".") + "$"));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Aktivit\u00e4ten: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + NumberFormat.getInstance().format(activitiesincome).replace(",", ".") + "$"));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Equip: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Zahlungen: " + TextFormatting.YELLOW + NumberFormat.getInstance().format(paymentincome).replace(",", ".") + "$"));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Unkosten: " + TextFormatting.YELLOW + "-" + NumberFormat.getInstance().format(equipmentspent).replace(",", ".") + "$"));
            int equipdifference = paymentincome - equipmentspent;
            if (equipdifference < 0) {
                color = TextFormatting.RED;
            } else {
                color = TextFormatting.GREEN;
            }
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Abweichung: " + color + NumberFormat.getInstance().format(equipdifference).replace(",", ".") + "$"));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Rabatte: " + TextFormatting.YELLOW + "-" + discounts.get(0).get(0).toString()));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Geh\u00e4lter: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Ausgaben: " + TextFormatting.YELLOW + "-" + NumberFormat.getInstance().format(wagesspent).replace(",", ".") + "$"));

            int income = bombincome + activitiesincome;
            int spent = equipdifference - discount - wagesspent;
            int difference = income + spent;

            int last = Integer.parseInt(actual.get(0).get(0).toString().replace(".", "").replace("$", ""));
            int now = Integer.parseInt(actual.get(1).get(0).toString().replace(".", "").replace("$", ""));
            int actualdifference = now - last;

            int deviation = actualdifference - difference;
            income += deviation;
            difference += deviation;

            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Sonstiges: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Umsatz: " + TextFormatting.YELLOW + NumberFormat.getInstance().format(deviation).replace(",", ".") + "$"));

            player.sendMessage(new TextComponentString(TextFormatting.DARK_GRAY + "-------------------------"));
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "" + TextFormatting.BOLD + "Gesamt: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + NumberFormat.getInstance().format(income).replace(",", ".") + "$"));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Ausgaben: " + TextFormatting.YELLOW + NumberFormat.getInstance().format(spent).replace(",", ".") + "$"));
            if (difference < 0) {
                color = TextFormatting.RED;
            } else {
                color = TextFormatting.GREEN;
            }
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "" + TextFormatting.BOLD + "   Differenz: " + color + NumberFormat.getInstance().format(difference).replace(",", ".") + "$"));
            player.sendMessage(new TextComponentString(""));


            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "" + TextFormatting.BOLD + "F-Bank: "));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Besprechung: " + TextFormatting.YELLOW + NumberFormat.getInstance().format(last).replace(",", ".") + "$"));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Gegenw\u00e4rtig: " + TextFormatting.YELLOW + NumberFormat.getInstance().format(now).replace(",", ".") + "$"));
            player.sendMessage(new TextComponentString(""));
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