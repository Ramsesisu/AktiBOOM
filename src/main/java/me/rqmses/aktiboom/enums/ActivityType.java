package me.rqmses.aktiboom.enums;

public enum ActivityType {

    GEBIETSEINNAHMEN(new Object[]{33, 20, "B", "I"}),
    FLUGZEUGENTFUEHRUNGEN(new Object[]{57, 20, "B", "I"}),
    GEISELNAHMEN(new Object[]{81, 20, "B", "I"}),
    BOMBEN(new Object[]{105, 20, "B", "I"}),
    SPRENGGUERTEL(new Object[]{129, 20, "B", "I"}),
    MENSCHENHANDEL_AUSRAUB(new Object[]{153, 30, "B", "I"}),
    AUTOBOMBEN(new Object[]{187, 20, "B", "I"}),
    EQUIP(new Object[]{211, 30, "B", "I"}),
    TRAININGS(new Object[]{245, 14, "B", "I"}),
    SONSTIGES(new Object[]{261, 30, "B", "I"}),
    ROLEPLAY(new Object[]{295, 30, "B", "I"});

    private final Object[] range;

    ActivityType(Object[] range)
    {
        this.range = range;
    }

    public Integer getRange() {
        return (Integer) this.range[1];
    }

    public Integer getLine() {
        return (Integer) this.range[0];
    }

    public String getColumnStart() {
        return this.range[2].toString();
    }

    public String getColumnEnd() {
        return this.range[3].toString();
    }
}
