package me.rqmses.aktiboom.enums;

public enum EquipType {

    MP5("MP5"),
    PISTOLE("Pistole"),
    KEVLAR("Kevlar"),
    SCHWEREKEVLAR("schwere Kevlar"),
    RPG7("RPG-7"),
    NACHZAHLUNG("Nachzahlung");

    private final String name;

    public String getName() {
        return this.name;
    }

    EquipType(String name) {
        this.name = name;
    }
}
