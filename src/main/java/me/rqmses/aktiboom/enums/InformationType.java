package me.rqmses.aktiboom.enums;

public enum InformationType {

    NAMES(new Object[] {"\u00dcbersicht", "B4:B27"}),
    RANKS(new Object[] {"\u00dcbersicht", "A4:A27"}),
    SECNAMES(new Object[] {"SEC-Drogen", "H13:H20"}),
    SECRANKS(new Object[] {"SEC-Drogen", "I13:I20"});

    private final Object[] range;

    InformationType(Object[] range)
    {
        this.range = range;
    }

    public String getSheet() {
        return this.range[0].toString();
    }

    public String getRange() {
        return this.range[1].toString();
    }
}
