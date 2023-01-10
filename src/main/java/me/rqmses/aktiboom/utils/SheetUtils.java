package me.rqmses.aktiboom.utils;

import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import me.rqmses.aktiboom.enums.ActivityType;
import me.rqmses.aktiboom.enums.InformationType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static me.rqmses.aktiboom.handlers.SheetHandler.SPREADSHEET_ID;
import static me.rqmses.aktiboom.handlers.SheetHandler.sheetsService;

public class SheetUtils {

    public static ValueRange getValueRange(String sheet, String range) throws IOException {
        return sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, sheet + "!" + range)
                .execute();
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public static void addValues(String sheet, String range, String[] args) throws IOException {
        Object[] values = args;
        sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, sheet + "!" + range, new ValueRange().setValues(Collections.singletonList(Arrays.asList(values))))
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("OVERWRITE")
                .setIncludeValuesInResponse(true)
                .execute();
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public static void setValues(String sheet, String range, String[] args) throws IOException {
        Object[] values = args;
        sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, sheet + "!" + range, new ValueRange().setValues(Collections.singletonList(Arrays.asList(values))))
                .setValueInputOption("USER_ENTERED")
                .execute();
    }

    public static void clearValues(String sheet, String range) throws IOException {
        sheetsService.spreadsheets().values()
                .clear(SPREADSHEET_ID, sheet + "!" + range, new ClearValuesRequest())
                .execute();
    }

    public static int searchLine(String sheet, String range, String search) throws IOException {
        return sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, sheet + "!" + range)
                .execute().getValues().indexOf(Collections.singletonList(search));
    }

    public static int getRank(String name) {
        try {
            ValueRange valueRange = getValueRange(InformationType.NAMES.getSheet(), InformationType.NAMES.getRange());
            if (!valueRange.getValues().contains(Collections.singletonList(name))) {
                return -1;
            }
            return Integer.parseInt(getValueRange(InformationType.RANKS.getSheet(), InformationType.RANKS.getRange()).getValues().get(valueRange.getValues().indexOf(Collections.singletonList(name))).get(0).toString());
        } catch (IOException e) {
            return 0;
        }
    }

    public static String getSECRank(String name) {
        try {
            ValueRange valueRange = getValueRange(InformationType.SECNAMES.getSheet(), InformationType.SECNAMES.getRange());
            if (!valueRange.getValues().contains(Collections.singletonList(name))) {
                return "-";
            }
            return getValueRange(InformationType.SECRANKS.getSheet(), InformationType.SECRANKS.getRange()).getValues().get(valueRange.getValues().indexOf(Collections.singletonList(name))).get(0).toString();
        } catch (IOException e) {
            return "E-02";
        }
    }

    public static boolean isSEC(String name) {
        try {
            return getValueRange(InformationType.SECNAMES.getSheet(), InformationType.SECNAMES.getRange()).getValues().contains(Collections.singletonList(name));
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean addActivity(ActivityType type, String[] args) throws IOException {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        ValueRange valueRange;
        valueRange = getValueRange(player.getName(), type.getRange());

        int line = 0;
        if (valueRange.getValues() != null) {
            for (Object ignored : valueRange.getValues()) {
                line++;
            }
        }

        if (line > type.getMax()) {
            return false;
        }

        Object[] values = new Object[8];
        switch (type) {
            case GEBIETSEINNAHMEN:
            case ENTFUEHRUNGEN:
            case GEISELNAHMEN:
            case BOMBEN:
                values = new String[]{args[0], args[1], args[2], args[3], "", args[4], args[5], args[6]};
                break;
            case SPRENGGUERTEL:
            case AUTOBOMBEN:
                values = new String[]{args[0], args[1], "", args[2], "", args[3], args[4], args[5]};
                break;
            case MENSCHENHANDEL_AUSRAUB:
            case TRAININGS:
                values = new String[]{args[0], args[1], args[2], "", args[3], "", args[4], args[5]};
                break;
            case EQUIP:
            case SONSTIGES:
            case ROLEPLAY:
                values = new String[]{args[0], args[1], "", args[2], "", "", args[3], ""};
                break;
        }
        sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, player.getName()+"!B"+ (type.getLine() + line), new ValueRange().setValues(Collections.singletonList(Arrays.asList(values))))
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("OVERWRITE")
                .execute();
        return true;
    }

    public static boolean addSECDrugs(String[] args) throws IOException {
        ValueRange valueRange = getValueRange("SEC-Drogen", "C4:C34");

        int line = 0;
        if (valueRange.getValues() != null) {
            for (Object ignored : valueRange.getValues()) {
                line++;
            }
        }

        if (line > 30) {
            return false;
        }

        sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, "SEC-Drogen!C" + (4 + line), new ValueRange().setValues(Collections.singletonList(Arrays.asList(new Object[]{args[0], args[1], args[2], args[3]}))))
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("OVERWRITE")
                .execute();
        return true;
    }
}
