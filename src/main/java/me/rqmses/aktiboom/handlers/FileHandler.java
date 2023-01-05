package me.rqmses.aktiboom.handlers;

import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileHandler {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    public static File getMinecraftDir() {
        return Minecraft.getMinecraft().gameDir;
    }

    public static File getModDir() {
        File modDir = new File(getMinecraftDir().getAbsolutePath());
        if (modDir.exists() || modDir.mkdir())
            return modDir;
        return null;
    }

    public static File getScreenshotDir() {
        if (getModDir() == null)
            return null;
        File screenshotDir = new File(getModDir().getAbsolutePath() + "/screenshots/");
        if (screenshotDir.exists() || screenshotDir.mkdir())
            return screenshotDir;
        return null;
    }

    public static File getNewImageFile() throws IOException {
        if (getScreenshotDir() == null)
            return null;

        String date = DATE_FORMAT.format(new Date());
        StringBuilder sb = new StringBuilder(date);
        int i = 1;
        while (new File(getScreenshotDir().getAbsolutePath() + "/" + sb + ".jpg").exists()) {
            if (i == 1)
                sb.append("_").append(i++);
            else
                sb.replace(sb.length() - 1, sb.length(), String.valueOf(i));
        }

        File newImageFile = new File(getScreenshotDir().getAbsolutePath() + "/" + sb + ".jpg");
        return newImageFile.createNewFile() ? newImageFile : null;
    }
}
