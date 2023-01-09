package me.rqmses.aktiboom.utils;

import com.google.api.services.sheets.v4.model.ValueRange;
import me.rqmses.aktiboom.enums.ActivityType;
import me.rqmses.aktiboom.enums.CheckActivityType;
import me.rqmses.aktiboom.enums.CheckEquipType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static me.rqmses.aktiboom.handlers.SheetHandler.SPREADSHEET_ID;
import static me.rqmses.aktiboom.handlers.SheetHandler.sheetsService;

public class SheetUtils {

    private static Object getValue(ActivityType type, int row, int line) throws IOException {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        ValueRange valueRange = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, player.getName() + "!B" + type.getLine() + ":I" + (type.getLine() + type.getRange()))
                .execute();
        try {
            return valueRange.getValues().get(row).get(line);
        } catch (RuntimeException e) {
            return "";
        }
    }

    public static int getRank(String name) throws IOException {
        ValueRange valueRange = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, "\u00dcbersicht!B4:B27")
                .execute();
        int line = valueRange.getValues().indexOf(Collections.singletonList(name));
        valueRange = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, "\u00dcbersicht!A4:A27")
                .execute();
        return Integer.parseInt(valueRange.getValues().get(line).get(0).toString());
    }

    public static String getAktis(CheckActivityType type, String column) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        ValueRange valueRange;
        try {
            valueRange = sheetsService.spreadsheets().values()
                    .get(SPREADSHEET_ID, player.getName() + "!" + column + type.getLine())
                    .execute();
        } catch (IOException e) {
            return "0";
        }
        try {
            return valueRange.getValues().get(0).get(0).toString();
        } catch (RuntimeException e) {
            return "0";
        }
    }

    public static String getEquip(CheckEquipType type, String column) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        ValueRange valueRange;
        try {
            valueRange = sheetsService.spreadsheets().values()
                    .get(SPREADSHEET_ID, player.getName() + "!" + column + type.getLine())
                    .execute();
        } catch (IOException e) {
            return "0";
        }
        try {
            return valueRange.getValues().get(0).get(0).toString();
        } catch (RuntimeException e) {
            return "0";
        }
    }

    public static boolean addActivity(ActivityType type, String[] args) throws IOException {
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
                        .append(SPREADSHEET_ID, player.getName()+"!B"+ (type.getLine() + i), new ValueRange().setValues(Collections.singletonList(Arrays.asList(values))))
                        .setValueInputOption("USER_ENTERED")
                        .setInsertDataOption("OVERWRITE")
                        .setIncludeValuesInResponse(true)
                        .execute();
                return true;
            }
        }
        return false;
    }

    public static boolean addSECDrugs(String[] args) throws IOException {
        for (int i = 0; i <= 30; i++) {
            String value;
            ValueRange valueRange = sheetsService.spreadsheets().values()
                    .get(SPREADSHEET_ID, "SEC-Drogen!C" + (4 + i))
                    .execute();
            try {
                value = valueRange.getValues().get(0).get(0).toString();
            } catch (RuntimeException e) {
                value = "";
            }
            if (Objects.equals(value, "")) {
                sheetsService.spreadsheets().values()
                        .append(SPREADSHEET_ID, "SEC-Drogen!C" + (4 + i), new ValueRange().setValues(Collections.singletonList(Arrays.asList(new Object[]{args[0], args[1], args[2], args[3]}))))
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
