package me.rqmses.aktiboom.enums;

public enum CheckActivityType {

    GEBIETSEINNAHMEN(new Object[]{6, "C", "D"}),
    FLUGZEUGENTFUEHRUNGEN(new Object[]{7, "C", "D"}),
    GEISELNAHMEN(new Object[]{8, "C", "D"}),
    BOMBEN(new Object[]{9, "C", "D"}),
    SPRENGGUERTEL(new Object[]{10, "C", "D"}),
    MENSCHENH_AUSRAUB(new Object[]{11, "C", "D"}),
    AUTOBOMBEN(new Object[]{12, "C", "D"}),
    TRAININGS(new Object[]{13, "C", "D"}),
    SONSTIGES(new Object[]{14, "C", "D"}),
    ROLEPLAY(new Object[]{27, "B", "B"}),
    LEITUNGEN(new Object[]{24, "B", "B"}),
    GESAMTAKTIVITAETEN(new Object[]{21, "B", "B"}),
    GESAMTEINNAHMEN(new Object[]{18, "B", "B"});

    private final Object[] range;

    CheckActivityType(Object[] range) {
        this.range = range;
    }

    public Integer getLine() {
        return (Integer) this.range[0];
    }

    public String getColumnAmount() {
        return this.range[1].toString();
    }

    public String getColumnIncome() {
        return this.range[2].toString();
    }
}
