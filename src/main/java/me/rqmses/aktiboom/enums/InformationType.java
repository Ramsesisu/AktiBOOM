package me.rqmses.aktiboom.enums;

public enum InformationType {

    NAMES("\u00dcbersicht", "B4:B27"),
    RANKS("\u00dcbersicht", "A4:A27"),
    EMAIL_NAMES("Equiplog", "C36:C60"),
    EMAILS("Equiplog", "D36:D60"),
    EQUIPLOG("Equiplog", "D4:D27"),
    SECNAMES("SEC-Drogen", "H13:H20"),
    SECRANKS("SEC-Drogen", "I13:I20"),
    WINLOSE("Win/Lose Statistik", "C7:F64"),
    KEYS("\u00dcbersicht", "O38:O39"),
    SPRENGGUERTEL_BAN("Mod-Sperren", "A2:A25"),
    RPG_7_BAN("Mod-Sperren", "B2:B25"),
    MATESHOTS("Win/Lose Statistik", "J37:K136"),
    KILLS("Win/Lose Statistik", "M37:N136"),
    KILLS_LOG("Win/Lose Statistik", "P37:P136");

    InformationType(String sheet, String range)
    {
        this.sheet = sheet;
        this.range = range;
    }

    private final String sheet;
    private final String range;

    public String getSheet() {
        return this.sheet;
    }

    public String getRange() {
        return this.range;
    }
}
