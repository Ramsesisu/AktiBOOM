package me.rqmses.aktiboom.enums;

public enum ActivityType {

    GEBIETSEINNAHMEN(new Object[] {"B33:B53", 33, 20}),
    ENTFUEHRUNGEN(new Object[] {"B57:B77", 57, 20}),
    GEISELNAHMEN(new Object[] {"B81:B101", 81, 20}),
    BOMBEN(new Object[] {"B105:B125", 105, 20}),
    SPRENGGUERTEL(new Object[] {"B129:B149", 129, 20}),
    MENSCHENHANDEL_AUSRAUB(new Object[] {"B153:B183", 153, 30}),
    AUTOBOMBEN(new Object[] {"B187:B207", 187, 20}),
    EQUIP(new Object[] {"B211:B241", 211, 30}),
    TRAININGS(new Object[] {"B245:B259", 245, 14}),
    SONSTIGES(new Object[] {"B263:B293", 263, 30}),
    ROLEPLAY(new Object[] {"B297:B327", 297, 30});

    private final Object[] range;

    ActivityType(Object[] range)
    {
        this.range = range;
    }

    public String getRange() {
        return this.range[0].toString();
    }

    public int getLine() {
        return (int) this.range[1];
    }

    public int getMax() {
        return (int) this.range[2];
    }
}
