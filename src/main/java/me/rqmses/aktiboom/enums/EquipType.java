package me.rqmses.aktiboom.enums;

public enum EquipType {

    SCATTER3("Scatter-3"),
    P69("P-69"),
    KEVLAR("Kevlar"),
    SCHWEREKEVLAR("schwere Kevlar"),
    ALPHA7("Alpha-7"),
    NACHZAHLUNG("Nachzahlung");

    EquipType(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return this.name;
    }
}
