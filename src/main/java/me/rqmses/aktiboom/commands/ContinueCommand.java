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
import java.util.ArrayList;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class ContinueCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "continue";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/continue";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            List<Runnable> addeditorrequests = SheetUtils.addEditorRequests;
            List<Runnable> removeeditorrequests = SheetUtils.removeEditorRequests;

            SheetUtils.tobecontinued = false;
            SheetUtils.returnvalues = 0;

            SheetUtils.addEditorRequests = new ArrayList<>();
            SheetUtils.removeEditorRequests = new ArrayList<>();

            for (Runnable runnable : addeditorrequests) {
                runnable.run();
            }
            for (Runnable runnable : removeeditorrequests) {
                runnable.run();
            }

            player.sendMessage(new TextComponentString(PREFIX + "Verbleibende Prozesse wurden abgeschlossen!"));

            if (SheetUtils.tobecontinued) {
                player.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + "/continue" + TextFormatting.YELLOW + " um die verbleibenden Prozesse zu beenden."));
                SheetUtils.tobecontinued = false;
                SheetUtils.returnvalues = 0;
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