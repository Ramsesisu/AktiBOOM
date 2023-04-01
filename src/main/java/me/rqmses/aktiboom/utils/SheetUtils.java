package me.rqmses.aktiboom.utils;

import com.google.api.services.sheets.v4.model.*;
import me.rqmses.aktiboom.enums.ActivityType;
import me.rqmses.aktiboom.enums.InformationType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;
import static me.rqmses.aktiboom.handlers.SheetHandler.SPREADSHEET_ID;
import static me.rqmses.aktiboom.handlers.SheetHandler.sheetsService;

public class SheetUtils {

    public static boolean tobecontinued = false;
    public static int returnvalues = 0;

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

    public static boolean setRank(String name, String rank) {
        try {
            ValueRange valueRange = getValueRange(InformationType.NAMES.getSheet(), InformationType.NAMES.getRange());
            if (!valueRange.getValues().contains(Collections.singletonList(name))) {
                return false;
            }
            int index = valueRange.getValues().indexOf(Collections.singletonList(name)) + 4;
            setValues(InformationType.RANKS.getSheet(), "A"+index+":A"+index, new String[]{rank});
        } catch (IOException e) {
            return false;
        }
        return true;
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

    public static boolean setSECRank(String name, String rank) {
        try {
            ValueRange valueRange = getValueRange(InformationType.SECNAMES.getSheet(), InformationType.SECNAMES.getRange());
            if (!valueRange.getValues().contains(Collections.singletonList(name))) {
                return false;
            }
            int index = valueRange.getValues().indexOf(Collections.singletonList(name)) + 13;
            setValues(InformationType.SECRANKS.getSheet(), "I"+index+":I"+index, new String[]{rank});
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static boolean isSEC(String name) {
        try {
            return getValueRange(InformationType.SECNAMES.getSheet(), InformationType.SECNAMES.getRange()).getValues().contains(Collections.singletonList(name));
        } catch (IOException e) {
            return false;
        }
    }

    public static void sortRange(String sheet, String range) throws IOException {
        ValueRange response = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, sheet + "!" + range).execute();
        List<List<Object>> values = response.getValues();

        int firstEmptyRow = 0;
        for (List<Object> row : values) {
            boolean isEmpty = true;
            for (Object cell : row) {
                if (cell != null) {
                    isEmpty = false;
                    break;
                }
            }
            if (isEmpty) {
                break;
            }
            firstEmptyRow++;
        }

        for (int i = firstEmptyRow; i < values.size()-1; i++) {
            values.set(i,values.get(i+1));
        }

        values.remove(values.size()-1);
        ValueRange body = new ValueRange().setValues(values);
        sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, sheet + "!" + range, body)
                .setValueInputOption("USER_ENTERED")
                .execute();

        if (!(firstEmptyRow > values.size())) {
            sheetsService.spreadsheets().values()
                    .clear(SPREADSHEET_ID, sheet + "!" + range.split(":")[0].replaceAll("\\d", "") + (Integer.parseInt(range.split(":")[0].replaceAll("\\D", "")) + values.size()) + ":" + range.split(":")[1].replaceAll("\\d", "") + (Integer.parseInt(range.split(":")[0].replaceAll("\\D", "")) + values.size()), new ClearValuesRequest())
                    .execute();
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

    public static void copySheet(int source, String name, int index) throws IOException {
        sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, new BatchUpdateSpreadsheetRequest()
                        .setRequests(Collections.singletonList(new Request()
                                .setDuplicateSheet(new DuplicateSheetRequest()
                                        .setSourceSheetId(source)
                                        .setNewSheetName(name)
                                        .setInsertSheetIndex(index)))))
                .execute();
    }

    public static void deleteSheet(int sheetID) throws IOException {
        sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, new BatchUpdateSpreadsheetRequest()
                        .setRequests(Collections.singletonList(new Request()
                                .setDeleteSheet(new DeleteSheetRequest()
                                        .setSheetId(sheetID)))))
                .execute();
    }

    public static void renameSheet(int sheetID, String name) throws IOException {
        sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, new BatchUpdateSpreadsheetRequest()
                        .setRequests(Collections.singletonList(new Request()
                                .setUpdateSheetProperties(new UpdateSheetPropertiesRequest()
                                        .setProperties(new SheetProperties()
                                                .setSheetId(sheetID)
                                                .setTitle(name))
                                        .setFields("title")))))
                .execute();
    }

    public static void setTabColor(int sheet, Color color) throws IOException {
        sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, new BatchUpdateSpreadsheetRequest()
                        .setRequests(Collections.singletonList(new Request()
                                .setUpdateSheetProperties(new UpdateSheetPropertiesRequest()
                                        .setProperties(new SheetProperties()
                                                .setSheetId(sheet)
                                                .setTabColor(color))
                                        .setFields("tabColor")))))
                .execute();
    }

    public static int getSheetIndex() {
        try {
            return sheetsService.spreadsheets().get(SPREADSHEET_ID).execute().getSheets().size();
        } catch (IOException ignored) {
            return 0;
        }
    }

    public static int getSheetID(String name) throws IOException {
        int sheetID = 0;
        for (Sheet sheet : sheetsService.spreadsheets().get(SPREADSHEET_ID).execute().getSheets()) {
            if (Objects.equals(sheet.getProperties().getTitle(), name)) {
                sheetID = sheet.getProperties().getSheetId();
                break;
            }
        }
        return sheetID;
    }

    public static int getRangeID(String sheetname, String rangedescription) throws IOException {
        int rangeID = 0;
        for (Sheet sheet : sheetsService.spreadsheets().get(SPREADSHEET_ID).execute().getSheets()) {
            if (Objects.equals(sheet.getProperties().getTitle(), sheetname)) {
                for (ProtectedRange range : sheet.getProtectedRanges()) {
                    if (Objects.equals(range.getDescription(), rangedescription)) {
                        rangeID = range.getProtectedRangeId();
                    }
                }
            }
        }
        return rangeID;
    }

    public static void addProtectedRange(int sheetID, int startRow, int startColumn, int endRow, int endColumn, List<String> editors) throws IOException {
        sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, new BatchUpdateSpreadsheetRequest()
                .setRequests(Collections.singletonList(new Request()
                        .setAddProtectedRange(new AddProtectedRangeRequest()
                                .setProtectedRange(new ProtectedRange()
                                        .setRange(new GridRange()
                                                .setSheetId(sheetID)
                                                .setStartRowIndex(startRow)
                                                .setEndRowIndex(endRow)
                                                .setStartColumnIndex(startColumn)
                                                .setEndColumnIndex(endColumn))
                                        .setEditors(new Editors()
                                                .setUsers(editors))
                                        .setDescription("Range")
                                        .setWarningOnly(false))))))
                .execute();
    }

    public static void setEditors(int rangeID, List<String> editors) throws IOException {
        sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, new BatchUpdateSpreadsheetRequest()
                        .setRequests(Collections.singletonList(new Request()
                                .setUpdateProtectedRange(new UpdateProtectedRangeRequest()
                                        .setProtectedRange(new ProtectedRange()
                                                .setProtectedRangeId(rangeID)
                                                .setEditors(new Editors()
                                                        .setUsers(editors)))
                                        .setFields("editors")))))
                .execute();
    }

    public static List<String> getEditors(String sheetname, String rangedescription) throws IOException {
        List<String> editors = new ArrayList<>();
        for (Sheet sheet : sheetsService.spreadsheets().get(SPREADSHEET_ID).execute().getSheets()) {
            if (Objects.equals(sheet.getProperties().getTitle(), sheetname)) {
                for (ProtectedRange range : sheet.getProtectedRanges()) {
                    if (Objects.equals(range.getDescription(), rangedescription)) {
                        editors = range.getEditors().getUsers();
                    }
                }
            }
        }
        return editors;
    }

    public static List<Runnable> addEditorRequests = new ArrayList<>();
    public static void addEditor(String sheetname, String rangedescription, String email) {
        try {
            List<String> editors = SheetUtils.getEditors(sheetname, rangedescription);
            if (!editors.contains(email)) {
                editors.add(email);
                SheetUtils.setEditors(SheetUtils.getRangeID(sheetname, rangedescription), editors);
            }
        } catch (IOException e) {
            returnvalues++;
            if (returnvalues < 3) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(PREFIX + "Der Bereich von " + TextFormatting.GOLD + sheetname + " (" + rangedescription + ")" + TextFormatting.YELLOW + " konnte nicht bearbeitet werden!"));
            } else if (returnvalues == 3) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(PREFIX + "..."));
            }
            addEditorRequests.add(() -> addEditor(sheetname, rangedescription, email));
            tobecontinued = true;
        }
    }

    public static List<Runnable> removeEditorRequests = new ArrayList<>();
    public static void removeEditor(String sheetname, String rangedescription, String email) {
        try {
            List<String> editors = SheetUtils.getEditors(sheetname, rangedescription);
            if (editors.contains(email)) {
                editors.remove(email);
                SheetUtils.setEditors(SheetUtils.getRangeID(sheetname, rangedescription), editors);
            }
        } catch (IOException e) {
            returnvalues++;
            if (returnvalues < 3) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(PREFIX + "Der Bereich von " + TextFormatting.GOLD + sheetname + TextFormatting.YELLOW + " konnte nicht bearbeitet werden!"));
            } else if (returnvalues == 3) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(PREFIX + "..."));
            }
            addEditorRequests.add(() -> addEditor(sheetname, rangedescription, email));
            tobecontinued = true;
        }
    }

    public static void addProtectedSheet(int sheetID, List<String> editors) throws IOException {
        sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, new BatchUpdateSpreadsheetRequest()
                .setRequests(Collections.singletonList(new Request()
                        .setAddProtectedRange(new AddProtectedRangeRequest()
                                .setProtectedRange(new ProtectedRange()
                                        .setRange(new GridRange()
                                                .setSheetId(sheetID))
                                        .setEditors(new Editors()
                                                .setUsers(editors))
                                        .setDescription("Sheet")
                                        .setWarningOnly(false))))))
                .execute();
    }

    public static String getEmail(String name) {
        try {
            ValueRange valueRange = getValueRange(InformationType.EMAIL_NAMES.getSheet(), InformationType.EMAIL_NAMES.getRange());
            if (!valueRange.getValues().contains(Collections.singletonList(name))) {
                return "";
            }
            return getValueRange(InformationType.EMAILS.getSheet(), InformationType.EMAILS.getRange()).getValues().get(valueRange.getValues().indexOf(Collections.singletonList(name))).get(0).toString();
        } catch (IOException e) {
            return "";
        }
    }

    public static void moveRow(int sheetID, int source, int destination) throws IOException {
        sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, new BatchUpdateSpreadsheetRequest()
                        .setRequests(Collections.singletonList(new Request()
                                .setMoveDimension(new MoveDimensionRequest()
                                        .setSource(new DimensionRange()
                                                .setSheetId(sheetID)
                                                .setDimension("ROWS")
                                                .setStartIndex(source - 1)
                                                .setEndIndex(source))
                                        .setDestinationIndex(destination - 1)))))
                .execute();
    }

    public static void copyRange(int source, int destination, int startRow, int startColumn, int endRow, int endColumn) throws IOException {
        sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, new BatchUpdateSpreadsheetRequest()
                        .setRequests(Collections.singletonList(new Request()
                                .setCopyPaste(new CopyPasteRequest()
                                        .setSource(new GridRange()
                                                .setSheetId(source)
                                                .setStartRowIndex(startRow)
                                                .setStartColumnIndex(startColumn)
                                                .setEndRowIndex(endRow)
                                                .setEndColumnIndex(endColumn))
                                        .setDestination(new GridRange()
                                                .setSheetId(destination)
                                                .setStartRowIndex(startRow)
                                                .setStartColumnIndex(startColumn)
                                                .setEndRowIndex(endRow)
                                                .setEndColumnIndex(endColumn))))))
                .execute();
    }
}
