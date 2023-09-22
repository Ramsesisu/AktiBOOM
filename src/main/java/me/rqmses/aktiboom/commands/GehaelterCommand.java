package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.enums.InformationType;
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
import java.util.List;
import java.util.Objects;

import static me.rqmses.aktiboom.AktiBoom.*;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class GehaelterCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "geh\u00e4lter";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/geh\u00e4lter";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            if (MEMBER.get(player.getName()) >= 4) {
                List<List<Object>> members;
                try {
                    members = SheetUtils.getValueRange(InformationType.EMAILS.getSheet(), "C977:D1000").getValues();
                } catch (IOException e) {
                    player.sendMessage(new TextComponentString(PREFIX + "Die Geh\u00e4lter konnten nicht erfasst werden!"));
                    return;
                }

                player.sendMessage(new TextComponentString(PREFIX + "Geh\u00e4lter der Woche:"));

                for (List<Object> member : members) {
                    if (!Objects.equals(member.get(0).toString(), "")) {
                        String money;
                        if (member.size() < 2) {
                            money = "0$";
                        } else {
                            money = member.get(1) + "$";
                        }
                        player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "   " + member.get(0) + TextFormatting.DARK_GRAY + " | " + TextFormatting.GRAY + money));
                    }
                }
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