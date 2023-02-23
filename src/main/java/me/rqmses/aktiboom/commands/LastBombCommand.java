package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.SheetUtils;
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
import java.util.Collections;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class LastBombCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "lastbomb";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/lastbomb";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("letztebombe");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            List<Object> list;
            try {
                list = SheetUtils.getValueRange("Win/Lose Statistik", "H34:K34").getValues().get(0);
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Die letzte Bombe konnte nicht erfasst werden!"));
                return;
            }

            player.sendMessage(new TextComponentString(PREFIX + "Die letzte Bombe war am " + TextFormatting.GOLD + list.get(0) + TextFormatting.YELLOW + " um " + TextFormatting.GOLD + list.get(1) + " Uhr" + TextFormatting.YELLOW + " bei " + TextFormatting.GOLD + list.get(2) + TextFormatting.YELLOW + ", gelegt von:" + TextFormatting.GOLD + " " + TextFormatting.BOLD + list.get(3) + TextFormatting.YELLOW + "."));
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