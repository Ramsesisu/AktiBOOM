package me.rqmses.aktiboom.enums;

public enum InformationType {

    NAME(new Object[]{"\u00dcbersicht", 4, 23, "B"}),
    RANK(new Object[]{"\u00dcbersicht", 4, 23, "A"});

    private final Object[] range;

    InformationType(Object[] range) {
        this.range = range;
    }

    public String getSheet() {
        return this.range[0].toString();
    }

    public Integer getLine() {
        return (Integer) this.range[1];
    }

    public Integer getRange() {
        return (Integer) this.range[2];
    }

    public String getColumn() {
        return this.range[3].toString();
    }
}
