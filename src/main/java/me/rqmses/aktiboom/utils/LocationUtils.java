package me.rqmses.aktiboom.utils;

import me.rqmses.aktiboom.enums.NaviPoints;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LocationUtils {
    public static List<String> getLocs() {
        return Arrays.asList("Stadthalle", "Uranberg", "W\u00fcrfelpark", "261", "248", "KF-Kran", "LU-Kran", "LU-Baustelle",
                "Psychiatrie", "Golfplatz", "Funpark", "Staatsbank", "Schule", "LU-Casino", "Fightclub", "Schwimmbad", "Atomkraftwerk",
                "643,5", "OBrien", "KF-Bar", "Kerzakov", "Rotlichtbar", "FBI", "Triaden", "Chinatown", "HRT", "UC17", "Insel",
                "SWAT", "Mall", "Farm", "Altstadt", "Labor", "Weinberg", "Tabakplantage", "M\u00fcllhalde", "Eisenstollen",
                "Krankenhaus", "Maklerb\u00fcro", "Park", "SH-Kran", "Cherrys", "Cafe", "Bar", "Windrad-Mex", "Hausaddon-Shop",
                "Lasertag", "News", "Feuerwehr", "314", "Kartell", "Disko", "Strand-Mex", "Milit\u00e4rbasis", "Freibad",
                "Polizei", "Mafia", "Yachthafen", "Luxus-Autoh\u00e4ndler", "Waffenladen-Murica", "Tankestelle-Polizei",
                "Waffenfabrik", "Papierfabrik", "Hitman", "Luigis", "Apotheke-Casino", "Casino", "Kirche", "Gemeindehaus",
                "Tankstelle-Hausaddon", "Autoh\u00e4ndler", "Le-Mileu", "Asservatenkammer", "Apotheke-Zentrale", "Feinkostladen",
                "Fleischer", "Wendys", "Shishabar", "Tankstelle-Mex", "Chickenfightclub", "Tankstelle-Chinatown", "Windrad-Chinatown",
                "Fischerh\u00fctte", "Westside-Ballas", "Mechaniker", "Waffenladen-Ballas", "Basketball", "CFK", "Kran-Uran",
                "Neulingshotel", "Flughafen-Unica", "Flughafen-Chinatown", "Flughafen-LasUnicas", "Urantransport", "Deathmatch-Arena",
                "Gef\u00e4ngnis", "Hochseefischer", "Feuerwerksladen", "Angelschein", "Terroristen", "S\u00e4gewerk", "200",
                "363", "531", "B\u00e4ckerei", "Shop", "Windrad-FBI", "UCM", "Altes-Gef\u00e4ngnis", "H\u00f6lle", "Himmel",
                "Checkpoint-Gef\u00e4ngnis", "Anwaltskanzelei", "Musikladen", "Alcatraz", "36", "144", "171", "190", "Stadion",
                "433", "382", "Metzgerei", "Las-Unicas", "CT-Shop", "Mileu", "Ballas", "SH", "KH", "Uran", "523", "278",
                "280", "121", "Brauerei", "470", "133");
    }

    @SuppressWarnings("CanBeFinal")
    public static HashMap<String, BlockPos> uclocs = new HashMap<>();

    public static void setLocs() {
        for (NaviPoints naviPoint : NaviPoints.values()) {
            uclocs.put(naviPoint.getName(), naviPoint.getBlockPos());
        }
    }

    public static String getNearestNavi(BlockPos pos) {
        String point = "";
        double distance = Integer.MAX_VALUE;

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        for (String navi : uclocs.keySet()) {
            double checkdistance = uclocs.get(navi).getDistance(x, y, z);
            if (checkdistance < distance) {
                point = navi;
                distance = checkdistance;
            }
        }

        return point;
    }
}
