package me.rqmses.aktiboom.handlers;

import static me.rqmses.aktiboom.AktiBoom.VERSION;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class UpdateHandler {
    private static final String URL = "https://github.com/Ramsesisu/AktiBOOM/releases/latest";

    public static void update(String release) throws Exception {
        String version = release.replace("Release ", "");
        getFile(version);
        moveFile(version);
    }

    public static String getLatest() throws IOException {
        Connection conn = Jsoup.connect(URL);
        Document doc = conn.get();
        Elements result = doc.selectXpath("//*[@id=\"repo-content-pjax-container\"]/div/div/div/div[1]/div[1]/div[1]/div[1]/h1");
        return result.text();
    }

    private static void getFile(String version) throws Exception {
        URL url = new URL("https://github.com/Ramsesisu/AktiBOOM/releases/download/Release" + version + "/AktiBOOM-" + version + ".jar");
        URLConnection connection = url.openConnection();
        InputStream in = connection.getInputStream();
        FileOutputStream fos = new FileOutputStream("AktiBOOM-" + version + ".jar");
        byte[] buffer = new byte[4096];
        int len;
        while ((len = in.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }
        fos.close();
    }

    private static void moveFile(String version) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                replaceJar(new File("mods/AktiBOOM-" + VERSION + ".jar"), new File("AktiBOOM-" + version + ".jar"), version);
            } catch (IOException e) {
                System.out.println("Update fehlgeschlagen!");
            }
        }));
    }

    private static void replaceJar(File mod, File update, String version) throws IOException {
        String batContent = "@echo off\n" +
                "\n" +
                "set /a \"i=0\"\n" +
                "set /a \"x=60\"\n" +
                "set from_file=" + update.getAbsolutePath() + "\n" +
                "set to_file=" + mod.getAbsolutePath() + "\n" +
                "set to_file_rename=" + mod.getName().replace(VERSION, version) + "\n" +
                "\n" +
                "echo update-file: %from_file%\n" +
                "echo to-file: %to_file%\n" +
                "\n" +
                ":delete_loop\n" +
                "\ttimeout /T 1 > nul\n" +
                "\n" +
                "\t2>nul (\n" +
                "\t  move /Y \"%from_file%\" \"%to_file%\"\n" +
                "\t) && (\n" +
                "\t  ren \"%to_file%\" \"%to_file_rename%\"\n" +
                "\t) && (\n" +
                "\t\techo updated AktiBOOM\n" +
                "\t\texit\n" +
                "\t) || (\n" +
                "\t\tset /a \"i = i + 1\"\n" +
                "\t\t\n" +
                "\t\tif %i% leq 60 (\n" +
                "\t\t\tset /a \"x = x - 1\"\n" +
                "\t\t\techo jar not yet available; waiting another second... [%x% tries left]\n" +
                "\t\t\tgoto delete_loop\n" +
                "\t\t)\n" +
                "\t\t\n" +
                "\t\techo 60 tries over; cancel update\n" +
                "\t\texit\n" +
                "\t)";

        File batFile = new File(System.getProperty("java.io.tmpdir"), "AktiBOOM-update.bat");
        FileUtils.write(batFile, batContent, Charset.defaultCharset());

        Process proc = Runtime.getRuntime().exec("cmd /c start \"\" " + batFile.getAbsolutePath());
        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
