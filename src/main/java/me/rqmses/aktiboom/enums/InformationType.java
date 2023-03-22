package me.rqmses.aktiboom.enums;

public enum InformationType {

    NAMES("\u00dcbersicht", "B4:B27"),
    RANKS("\u00dcbersicht", "A4:A27"),
    EMAIL_NAMES("Equiplog", "C36:C60"),
    EMAILS("Equiplog", "D36:D60"),
    SECNAMES("SEC-Drogen", "H13:H20"),
    SECRANKS("SEC-Drogen", "I13:I20"),
    CHECKMOD_PERMISSION("Mod-Rechte", "A2:A25"),
    GLOBAL_PERMISSION("Mod-Rechte", "B2:B25"),
    INVSEE_PERMISSION("Mod-Rechte", "C2:C25"),
    CHECKDRUGS_PERMISSION("Mod-Rechte", "D2:D25"),
    REFRESH_PERMISSION("Mod-Rechte", "E2:E25"),
    AWAY_PERMISSION("Mod-Rechte", "F2:F25"),
    SPERRE_PERMISSION("Mod-Rechte", "G2:G25"),
    SPRENGGUERTEL_BAN("Mod-Sperren", "A2:A25"),
    RPG_7_BAN("Mod-Sperren", "B2:B25"),
    MATESHOTS("Win/Lose Statistik", "J37:K136"),
    KILLS("Win/Lose Statistik", "M37:N136");

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
