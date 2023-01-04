package me.rqmses.aktiboom.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class AktiCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "/akti";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/akti";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Arrays.asList("aktivit\u00e4t", "activity");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

    }

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }
}
