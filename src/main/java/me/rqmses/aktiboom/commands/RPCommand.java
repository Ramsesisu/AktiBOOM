package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.handlers.FileHandler;
import me.rqmses.aktiboom.handlers.ScreenHandler;
import me.rqmses.aktiboom.handlers.UploadHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.commands.RPSessionCommand.session;

@SuppressWarnings("NullableProblems")
public class RPCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "rp";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/roleplay";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("roleplay");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (session) {
            String hash;
            try {
                hash = UploadHandler.uploadToHash(ScreenHandler.handleFile(FileHandler.getNewImageFile()));
            } catch (IOException e) {
                player.sendMessage(new TextComponentString(PREFIX + "Beim Aufnehmen des Screens ist ein Fehler unterlaufen!"));
                return;
            }
            RPSessionCommand.imageHashes.add(hash);
            player.sendMessage(new TextComponentString(PREFIX + "Screenshot gespeichert."));
        } else {
            player.sendMessage(new TextComponentString(PREFIX + "Du hast keine Sitzung gestartet."));
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
