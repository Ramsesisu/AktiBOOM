package me.rqmses.aktiboom.utils;

import net.minecraft.util.text.TextFormatting;

public class FormatUtils {
    public static TextFormatting getColor(String colorcode) {
        switch (colorcode.toLowerCase()) {
            case "0":
                return TextFormatting.BLACK;
            case "1":
                return TextFormatting.DARK_BLUE;
            case "2":
                return TextFormatting.DARK_GREEN;
            case "3":
                return TextFormatting.DARK_AQUA;
            case "4":
                return TextFormatting.DARK_RED;
            case "5":
                return TextFormatting.DARK_PURPLE;
            case "6":
                return TextFormatting.GOLD;
            case "7":
                return TextFormatting.GRAY;
            case "8":
                return TextFormatting.DARK_GRAY;
            case "9":
                return TextFormatting.BLUE;
            case "a":
                return TextFormatting.GREEN;
            case "b":
                return TextFormatting.AQUA;
            case "c":
                return TextFormatting.RED;
            case "d":
                return TextFormatting.LIGHT_PURPLE;
            case "e":
                return TextFormatting.YELLOW;
            case "f":
                return TextFormatting.WHITE;
            default:
                return TextFormatting.RESET;
        }
    }
}
