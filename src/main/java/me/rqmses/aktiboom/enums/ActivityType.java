package me.rqmses.aktiboom.enums;

public enum ActivityType {

    GEBIETSEINNAHMEN("B33:B53", 33, 20),
    ENTFUEHRUNGEN("B57:B77", 57, 20),
    GEISELNAHMEN("B81:B101", 81, 20),
    BOMBEN("B105:B125", 105, 20),
    SPRENGGUERTEL("B129:B149", 129, 20),
    MENSCHENHANDEL_AUSRAUB("B153:B183", 153, 30),
    AUTOBOMBEN("B187:B207", 187, 20),
    EQUIP("B211:B241", 211, 30),
    TRAININGS("B245:B259", 245, 14),
    SONSTIGES("B263:B293", 263, 30),
    ROLEPLAY("B297:B327", 297, 30);

    ActivityType(String range, int line, int max)
    {
        this.range = range;
        this.line = line;
        this.max = max;
    }

    private final String range;
    private final int line;
    private final int max;

    public String getRange() {
        return this.range;
    }

    public int getLine() {
        return this.line;
    }

    public int getMax() {
        return this.max;
    }
}
