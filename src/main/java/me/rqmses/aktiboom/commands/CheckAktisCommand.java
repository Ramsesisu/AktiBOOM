package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.enums.CheckActivityType;
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
import java.util.Arrays;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.utils.SheetUtils.getAktis;
import static me.rqmses.aktiboom.utils.SheetUtils.getRank;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class CheckAktisCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "checkaktis";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/checkaktis";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Arrays.asList("checkaktivit\u00e4ten", "aktis", "aktivit\u00e4ten");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        player.sendMessage(new TextComponentString(PREFIX + "Aktivit\u00e4ten von " + TextFormatting.DARK_GRAY + TextFormatting.GOLD + player.getName()));
        player.sendMessage(new TextComponentString(""));

        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "Gebietseinahmen: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + getAktis(CheckActivityType.GEBIETSEINNAHMEN, CheckActivityType.GEBIETSEINNAHMEN.getColumnAmount())));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + getAktis(CheckActivityType.GEBIETSEINNAHMEN, CheckActivityType.GEBIETSEINNAHMEN.getColumnIncome())));
        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "Flugzeugentf\u00fchrungen: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + getAktis(CheckActivityType.FLUGZEUGENTFUEHRUNGEN, CheckActivityType.FLUGZEUGENTFUEHRUNGEN.getColumnAmount())));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + getAktis(CheckActivityType.FLUGZEUGENTFUEHRUNGEN, CheckActivityType.FLUGZEUGENTFUEHRUNGEN.getColumnIncome())));
        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "Geiselnahmen: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + getAktis(CheckActivityType.GEISELNAHMEN, CheckActivityType.GEISELNAHMEN.getColumnAmount())));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + getAktis(CheckActivityType.GEISELNAHMEN, CheckActivityType.GEISELNAHMEN.getColumnIncome())));
        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "Bomben: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + getAktis(CheckActivityType.BOMBEN, CheckActivityType.BOMBEN.getColumnAmount())));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + "-"));
        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "Sprengg\u00fcrtel: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + getAktis(CheckActivityType.SPRENGGUERTEL, CheckActivityType.SPRENGGUERTEL.getColumnAmount())));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + getAktis(CheckActivityType.SPRENGGUERTEL, CheckActivityType.SPRENGGUERTEL.getColumnIncome())));
        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "Menschenh./Ausraub: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + getAktis(CheckActivityType.MENSCHENH_AUSRAUB, CheckActivityType.MENSCHENH_AUSRAUB.getColumnAmount())));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + getAktis(CheckActivityType.MENSCHENH_AUSRAUB, CheckActivityType.MENSCHENH_AUSRAUB.getColumnIncome())));
        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "Autobomben: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + getAktis(CheckActivityType.AUTOBOMBEN, CheckActivityType.AUTOBOMBEN.getColumnAmount())));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + getAktis(CheckActivityType.AUTOBOMBEN, CheckActivityType.AUTOBOMBEN.getColumnIncome())));
        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "Trainings: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + getAktis(CheckActivityType.TRAININGS, CheckActivityType.TRAININGS.getColumnAmount())));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + "-"));
        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "Sonstiges: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + getAktis(CheckActivityType.SONSTIGES, CheckActivityType.SONSTIGES.getColumnAmount())));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + getAktis(CheckActivityType.SONSTIGES, CheckActivityType.SONSTIGES.getColumnIncome())));
        player.sendMessage(new TextComponentString(TextFormatting.DARK_GRAY + "-------------------------"));
        player.sendMessage(new TextComponentString( TextFormatting.GOLD+ "" + TextFormatting.BOLD + "Gesamt: "));
        String tempamount = getAktis(CheckActivityType.GESAMTAKTIVITAETEN, CheckActivityType.GESAMTAKTIVITAETEN.getColumnAmount());
        String tempincome = getAktis(CheckActivityType.GESAMTEINNAHMEN, CheckActivityType.GESAMTEINNAHMEN.getColumnIncome());
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + tempamount));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Einnahmen: " + TextFormatting.YELLOW + tempincome));
        player.sendMessage(new TextComponentString(""));
        int amount = Integer.parseInt(tempamount);
        int income = Integer.parseInt(tempincome.replace(".", "").replace("$", ""));

        String temproleplay = getAktis(CheckActivityType.ROLEPLAY, CheckActivityType.ROLEPLAY.getColumnAmount());
        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "RolePlays: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + temproleplay));
        int roleplay = Integer.parseInt(temproleplay);
        String templeading = getAktis(CheckActivityType.LEITUNGEN, CheckActivityType.LEITUNGEN.getColumnAmount());
        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "Leitungen: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl:     " + TextFormatting.YELLOW + templeading));
        int leading = Integer.parseInt(templeading);
        player.sendMessage(new TextComponentString(""));

        int minamount = 0;
        int minincome = 0;
        int minroleplay = 0;
        int minleading = 0;

        switch (getRank()) {
            case 0:
                minamount = 4;
                minincome = 4000;
                minroleplay = 3;
                break;
            case 1:
                minamount = 5;
                minincome = 6000;
                minroleplay = 3;
                break;
            case 2:
                minamount = 5;
                minincome = 8000;
                minroleplay = 3;
                minleading = 1;
                break;
            case 3:
                minamount = 6;
                minincome = 10000;
                minroleplay = 3;
                minleading = 1;
                break;
        }

        if (amount >= minamount && income >= minincome && roleplay >= minroleplay && leading >= minleading) {
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Mindestaktivit\u00e4ten: " + TextFormatting.GREEN + "" + TextFormatting.BOLD + "Ja"));
        } else {
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Mindestaktivit\u00e4ten: " + TextFormatting.RED + "" + TextFormatting.BOLD + "Nein"));
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