package me.rqmses.aktiboom.handlers;

import me.rqmses.aktiboom.utils.ReflectionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ScreenShotHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenHandler {

    /**
     * @see <a href="https://github.com/rettichlp/UnicacityAddon-1.12.2/blob/develop/src/main/java/com/rettichlp/unicacityaddon/base/manager/FileManager.java">UnicacityAddon-1.12.2 by rettichlp</a>
     */

    public static String handleFile() {
        File file;
        try {
            file = FileHandler.getNewImageFile();
            return handleScreenshotWithUpload(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String handleScreenshotWithUpload(File file) throws IOException {
        File screenshotFile = handleFile(file);
        return uploadScreenshot(screenshotFile);
    }

    public static File handleFile(File file) {
        if (file != null) {
            try {
                Framebuffer framebuffer = ReflectionUtils.getValue(Minecraft.getMinecraft(), Framebuffer.class);
                assert framebuffer != null;
                BufferedImage image = ScreenShotHelper.createScreenshot(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, framebuffer);
                ImageIO.write(image, "jpg", file);
                return file;
            } catch (IOException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private static String uploadScreenshot(File screenshotFile) {
        return UploadHandler.uploadToLink(screenshotFile);
    }
}