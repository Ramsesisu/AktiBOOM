package me.rqmses.aktiboom.enums;

public enum CheckEquipType {

    MP5(new Object[]{6, "G", "H"}),
    SPRENGGUERTEL(new Object[]{7, "G", "H"}),
    PISTOLE(new Object[]{8, "G", "H"}),
    KEVLAR(new Object[]{9, "G", "H"}),
    SCHWEREKEVLAR(new Object[]{10, "G", "H"}),
    RPG7(new Object[]{11, "G", "H"}),
    GESAMTKOSTEN(new Object[]{12, "G", "H"}),
    EINZAHLUNG(new Object[]{13, "G", "H"}),
    DIFFERENZ(new Object[]{14, "G", "H"});

    private final Object[] range;

    CheckEquipType(Object[] range) {
        this.range = range;
    }

    public Integer getLine() {
        return (Integer) this.range[0];
    }

    public String getColumnAmount() {
        return this.range[1].toString();
    }

    public String getColumnCosts() {
        return this.range[2].toString();
    }
}
