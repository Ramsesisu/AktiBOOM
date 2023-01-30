package me.rqmses.aktiboom.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("ALL")
public class TrainingsserverCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "trainingsserver";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/trainingsserver";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.singletonList("training");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (Minecraft.getMinecraft().getCurrentServerData().serverIP.toLowerCase().contains("howtoswat")) {
            Minecraft.getMinecraft().getConnection().getNetworkManager().closeChannel(new TextComponentString("Betrete UnicaCity"));
            Minecraft.getMinecraft().displayGuiScreen(new GuiConnecting(new GuiMainMenu(), Minecraft.getMinecraft(), new ServerData("UnicaCity", "UnicaCity.de", false)));
        } else {
            Minecraft.getMinecraft().getConnection().getNetworkManager().closeChannel(new TextComponentString("Betrete Trainings-Server"));
            Minecraft.getMinecraft().displayGuiScreen(new GuiConnecting(new GuiMainMenu(), Minecraft.getMinecraft(), new ServerData("HowToSWAT", "HowToSWAT.eu", false)));
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
