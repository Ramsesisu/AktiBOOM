package me.rqmses.aktiboom.commands;

import me.rqmses.aktiboom.utils.InformationUtils;
import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.*;

@SuppressWarnings("NullableProblems")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class InfoCommand extends CommandBase implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "info";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/info ([Name])";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Arrays.asList("information", "informationen");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> targets = new ArrayList<>();
        if (args.length == 1) {
            for (NetworkPlayerInfo playerInfo : Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfoMap()) {
                targets.add(String.valueOf(playerInfo.getGameProfile().getName()));
            }
        }
        for (String target : targets) {
            if (target.toUpperCase().startsWith(args[args.length-1].toUpperCase()))
                list.add(target);
        }
        Collections.sort(targets);
        return list;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        new Thread(() -> {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            String name;
            int rank;
            int secrank = 0;
            if (args.length > 0) {
                name = args[0];
                rank = SheetUtils.getRank(args[0]);
                if (SECMEMBER.containsKey(player.getName())) {
                    secrank = Integer.parseInt(SheetUtils.getSECRank(args[0]));
                }
            } else {
                name = player.getName();
                rank = MEMBER.get(player.getName());
                if (SECMEMBER.containsKey(player.getName())) {
                    secrank = SECMEMBER.get(player.getName());
                }
            }

            player.sendMessage(new TextComponentString(PREFIX + "Informationen \u00fcber " + TextFormatting.GOLD + name + TextFormatting.YELLOW + ":"));

            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "  Name: " + TextFormatting.YELLOW + name));

            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "  Rang: " + TextFormatting.YELLOW + InformationUtils.getRankName(rank) + " (" + rank + ")"));
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "  SEC: " + TextFormatting.YELLOW + InformationUtils.getSECRankName(secrank)));
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