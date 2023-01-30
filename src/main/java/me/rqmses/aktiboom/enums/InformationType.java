package me.rqmses.aktiboom.enums;

public enum InformationType {

    NAMES("\u00dcbersicht", "B4:B27"),
    RANKS("\u00dcbersicht", "A4:A27"),
    SECNAMES("SEC-Drogen", "H13:H20"),
    SECRANKS("SEC-Drogen", "I13:I20");

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
