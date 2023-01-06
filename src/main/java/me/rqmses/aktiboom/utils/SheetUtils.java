package me.rqmses.aktiboom.utils;

import com.google.api.services.sheets.v4.model.ValueRange;
import me.rqmses.aktiboom.enums.ActivityType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static me.rqmses.aktiboom.handlers.SheetHandler.SPREADSHEET_ID;
import static me.rqmses.aktiboom.handlers.SheetHandler.sheetsService;

public class SheetUtils {

    public static Object getValue(ActivityType type, int row, int line) throws IOException {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        ValueRange valueRange = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, player.getName() + "!B" + type.getStart() + ":I" + (type.getStart() + type.getRange()))
                .execute();
        try {
            return valueRange.getValues().get(row).get(line);
        } catch (RuntimeException e) {
            return "";
        }
    }

    public static boolean addValues(ActivityType type, String[] args) throws IOException {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        for (int i = 0; i <= type.getRange(); i++) {
            if (Objects.equals(getValue(type, i, 0).toString(), "")) {
                Object[] values = new Object[10];
                switch (type) {
                    case GEBIETSEINNAHMEN:
                    case FLUGZEUGENTFUEHRUNGEN:
                    case GEISELNAHMEN:
                    case BOMBEN:
                        values = new String[] {args[0], args[1], args[2], args[3], "", args[4], args[5], args[6]};
                        break;
                    case SPRENGGUERTEL:
                    case AUTOBOMBEN:
                        values = new String[] {args[0], args[1], "", args[2], "", args[3], args[4], args[5]};
                        break;
                    case MENSCHENHANDEL_AUSRAUB:
                    case TRAININGS:
                        values = new String[] {args[0], args[1], args[2], "", args[3], "", args[4], args[5]};
                        break;
                    case EQUIP:
                    case SONSTIGES:
                    case ROLEPLAY:
                        values = new String[] {args[0], args[1], "", args[2], "", "", args[3], ""};
                        break;
                }
                sheetsService.spreadsheets().values()
                        .append(SPREADSHEET_ID, player.getName()+"!B"+ (type.getStart() + i), new ValueRange().setValues(Collections.singletonList(Arrays.asList(values))))
                        .setValueInputOption("USER_ENTERED")
                        .setInsertDataOption("OVERWRITE")
                        .setIncludeValuesInResponse(true)
                        .execute();
                return true;
            }
        }
        return false;
    }
}
