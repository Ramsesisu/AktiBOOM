package me.rqmses.aktiboom.listeners;

import me.rqmses.aktiboom.commands.AuftraegeCommand;
import me.rqmses.aktiboom.commands.SprengisCommand;
import me.rqmses.aktiboom.commands.SchutzCommand;
import me.rqmses.aktiboom.commands.TuningsCommand;
import me.rqmses.aktiboom.handlers.ConfigHandler;
import me.rqmses.aktiboom.utils.FormatUtils;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NameFormatListener {

    @SubscribeEvent
    public void onNameFormat(PlayerEvent.NameFormat event) {
        if (SchutzCommand.schutz.contains(event.getUsername())) {
            event.setDisplayname(event.getDisplayname().replace(event.getUsername(), FormatUtils.getColor(ConfigHandler.schutzcolor) + event.getUsername()));
        } else if (AuftraegeCommand.auftraege.contains(event.getUsername())) {
            event.setDisplayname(event.getDisplayname().replace(event.getUsername(), FormatUtils.getColor(ConfigHandler.auftragcolor) + event.getUsername()));
        } else if (SprengisCommand.drohungen.contains(event.getUsername())) {
            event.setDisplayname(event.getDisplayname().replace(event.getUsername(), FormatUtils.getColor(ConfigHandler.drohungcolor) + event.getUsername()));
        } else if (TuningsCommand.tunings.contains(event.getUsername())) {
            event.setDisplayname(event.getDisplayname().replace(event.getUsername(), FormatUtils.getColor(ConfigHandler.tuningcolor) + event.getUsername()));
        }
    }
}
