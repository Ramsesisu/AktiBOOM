package me.rqmses.aktiboom.handlers;

import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

public class UploadHandler {

    private static final String UPLOAD_API_URL = "https://api.imgur.com/3/image";
    public static final String ALBUM_API_URL = "https://api.imgur.com/3/album";

    public static String uploadToLink(File file) {
        return new JsonParser().parse(upload(file)).getAsJsonObject().get("data").getAsJsonObject().get("link").getAsString();
    }

    public static String uploadToHash(File file) {
        return new JsonParser().parse(upload(file)).getAsJsonObject().get("data").getAsJsonObject().get("deletehash").getAsString();
    }

    public static String uploadAlbumToID(List<String> imageHashes) throws IOException {
        return new JsonParser().parse(uploadAlbum(imageHashes)).getAsJsonObject().get("data").getAsJsonObject().get("id").getAsString();
    }

    public static String upload(File file) {
        HttpURLConnection conn = null;
        try {
            conn = getHttpConnection(UPLOAD_API_URL);
            writeToConnection(conn, "image=" + toBase64(file));
            return getResponse(conn);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null)
                conn.disconnect();
        }
    }

    public static String uploadAlbum(List<String> imageHashes) throws IOException {
        HttpURLConnection conn = getHttpConnection(ALBUM_API_URL);
        StringBuilder hashes = new StringBuilder();
        for (String hash : imageHashes) {
            if (!hashes.toString().equals("")) {
                hashes.append(",");
            }
            hashes.append(hash);
        }
        writeToConnection(conn, "deletehashes=" + hashes);
        return getResponse(conn);
    }

    private static String toBase64(File file) throws IOException {
        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        return URLEncoder.encode(new String(encoded, StandardCharsets.US_ASCII), StandardCharsets.UTF_8.toString());
    }

    private static HttpURLConnection getHttpConnection(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Client-ID 40ae2d5cf15eaf3");
        conn.setReadTimeout(1000000);
        conn.connect();

        return conn;
    }

    private static void writeToConnection(HttpURLConnection conn, String message) {
        OutputStreamWriter writer;
        try {
            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(message);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(PREFIX + "Fehler beim Herstellen einer Verbindung zu Imgur!"));
        }
    }

    private static String getResponse(HttpURLConnection conn) throws IOException {
        InputStream inputStream = conn.getInputStream();
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            inputStream.close();
        }

        return sb.toString();
    }
}