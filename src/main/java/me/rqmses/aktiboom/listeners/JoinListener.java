package me.rqmses.aktiboom.listeners;

import me.rqmses.aktiboom.commands.AuftraegeCommand;
import me.rqmses.aktiboom.commands.DrohungenCommand;
import me.rqmses.aktiboom.commands.SchutzCommand;
import me.rqmses.aktiboom.utils.SheetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static me.rqmses.aktiboom.AktiBoom.*;

public class JoinListener {

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayerSP) {
            refresh();
        }
    }

    public static void refresh() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        SEC = SheetUtils.isSEC(player.getName());
        RANK = SheetUtils.getRank(player.getName());
        SECRANK = SheetUtils.getSECRank(player.getName());
        if (SECRANK.startsWith("E-")) {
            SECRANK = "Executive";
        } else if (SECRANK.startsWith("C-")) {
            SECRANK = "Commander";
        } else if (SECRANK.startsWith("G-")) {
            SECRANK = "General";
        }

        SchutzCommand.syncList();
        AuftraegeCommand.syncList();
        DrohungenCommand.syncList();
    }
}
