package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.enums.CheckEquipType;
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
import java.util.Collections;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.utils.SheetUtils.getEquip;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class CheckEquipCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "checkequip";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/checkequip";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("equipment");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        player.sendMessage(new TextComponentString(PREFIX + "EquipLog von " + TextFormatting.DARK_GRAY + TextFormatting.GOLD + player.getName()));
        player.sendMessage(new TextComponentString(""));

        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "MP5: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl: " + TextFormatting.YELLOW + getEquip(CheckEquipType.MP5, CheckEquipType.MP5.getColumnAmount())));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Kosten: " + TextFormatting.YELLOW + getEquip(CheckEquipType.MP5, CheckEquipType.MP5.getColumnCosts())));
        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "Sprengg\u00fcrtel: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl: " + TextFormatting.YELLOW + getEquip(CheckEquipType.SPRENGGUERTEL, CheckEquipType.SPRENGGUERTEL.getColumnAmount())));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Kosten: " + TextFormatting.YELLOW + getEquip(CheckEquipType.SPRENGGUERTEL, CheckEquipType.SPRENGGUERTEL.getColumnCosts())));
        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "Pistole: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl: " + TextFormatting.YELLOW + getEquip(CheckEquipType.PISTOLE, CheckEquipType.PISTOLE.getColumnAmount())));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Kosten: " + TextFormatting.YELLOW + getEquip(CheckEquipType.PISTOLE, CheckEquipType.PISTOLE.getColumnCosts())));
        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "Kevlar: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl: " + TextFormatting.YELLOW + getEquip(CheckEquipType.KEVLAR, CheckEquipType.KEVLAR.getColumnAmount())));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Kosten: " + TextFormatting.YELLOW + getEquip(CheckEquipType.KEVLAR, CheckEquipType.KEVLAR.getColumnCosts())));
        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "Schwere Kevlar: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl: " + TextFormatting.YELLOW + getEquip(CheckEquipType.SCHWEREKEVLAR, CheckEquipType.SCHWEREKEVLAR.getColumnAmount())));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Kosten: " + TextFormatting.YELLOW + getEquip(CheckEquipType.SCHWEREKEVLAR, CheckEquipType.SCHWEREKEVLAR.getColumnCosts())));
        player.sendMessage(new TextComponentString( TextFormatting.GOLD + "RPG-7: "));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Anzahl: " + TextFormatting.YELLOW + getEquip(CheckEquipType.RPG7, CheckEquipType.RPG7.getColumnAmount())));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Kosten: " + TextFormatting.YELLOW + getEquip(CheckEquipType.RPG7, CheckEquipType.RPG7.getColumnCosts())));
        player.sendMessage(new TextComponentString(TextFormatting.DARK_GRAY + "-------------------------"));
        player.sendMessage(new TextComponentString( TextFormatting.GOLD+ "" + TextFormatting.BOLD + "Gesamt: "));
        String tempdifferenz = getEquip(CheckEquipType.DIFFERENZ, CheckEquipType.DIFFERENZ.getColumnCosts());
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Equipkosten: " + TextFormatting.YELLOW + getEquip(CheckEquipType.GESAMTKOSTEN, CheckEquipType.GESAMTKOSTEN.getColumnCosts())));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Eingezahlt:  " + TextFormatting.YELLOW + getEquip(CheckEquipType.EINZAHLUNG, CheckEquipType.EINZAHLUNG.getColumnCosts())));
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "   Differenz:   " + TextFormatting.YELLOW + tempdifferenz));
        player.sendMessage(new TextComponentString(""));

        if (Integer.parseInt(tempdifferenz.replace(".", "").replace("$", "")) <= 0) {
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Nachzahlungen: " + TextFormatting.GREEN + "" + TextFormatting.BOLD + "Nein"));
        } else {
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Nachzahlungen: " + TextFormatting.RED + "" + TextFormatting.BOLD + "Ja"));
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