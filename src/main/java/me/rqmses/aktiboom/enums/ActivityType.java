package org.example.enums;

public enum ActivityType {

    GEBIETSEINNAHMEN(new Integer[]{33, 20}),
    FLUGZEUGENTFUEHRUNGEN(new Integer[]{57, 20}),
    GEISELNAHMEN(new Integer[]{81, 20}),
    BOMBEN(new Integer[]{105, 20}),
    SPRENGGUERTEL(new Integer[]{129, 20}),
    MENSCHENHANDEL_AUSRAUB(new Integer[]{153, 30}),
    AUTOBOMBEN(new Integer[]{187, 20}),
    EQUIP(new Integer[]{211, 30}),
    TRAININGS(new Integer[]{245, 14}),
    SONSTIGES(new Integer[]{261, 30}),
    ROLEPLAY(new Integer[]{295, 30});

    private final Integer[] range;

    public Integer getRange() {
        return this.range[1];
    }

    public Integer getStart() {
        return this.range[0];
    }

    public String[] getArgs() {
        return new String[] {"Gebietseinahme", "Flugzeugentf\u00fchrung", "Geiselnahme", "Bombe", "Sprengg\u00fcrtel",
                "Menschenhandel", "Ausraub", "Autobombe", "Equip", "Training", "Waffentransport", "Zuzahlung" , "Bombenspot",
                "RP-Event", "Spende", "Drohung", "Geisel", "Auftragsauslieferung", "RolePlay"};
    }

    ActivityType(Integer[] range)
    {
        this.range = range;
    }
}
