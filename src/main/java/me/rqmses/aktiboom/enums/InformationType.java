package me.rqmses.aktiboom.enums;

public enum InformationType {

    MEMBER("\u00dcbersicht", "A4:B27"),
    NAMES("\u00dcbersicht", "B4:B27"),
    RANKS("\u00dcbersicht", "A4:A27"),
    EMAIL_NAMES("Equiplog", "C36:C60"),
    EMAILS("Equiplog", "D36:D60"),
    EQUIPLOG("Equiplog", "D4:D27"),
    DISCOUNT("Equiplog", "D28:D28"),
    SECDRUGS("SEC", "C4:F34"),
    SECMEMBER("SEC", "H13:J21"),
    SECNAMES("SEC", "H13:H21"),
    SECRANKS("SEC", "I13:I21"),
    SECRANKNAMES("SEC", "H31:I33"),
    SECPOINTS("SEC", "J13:J21"),
    WINLOSE("Win/Lose Statistik", "C7:F64"),
    KEYS("\u00dcbersicht", "O38:O39"),
    MEETING("Equiplog", "G15:J17"),
    SPOTS("Spot\u00fcbersicht", "C9:I299"),
    MATESHOTS("Win/Lose Statistik", "J37:K136");

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
