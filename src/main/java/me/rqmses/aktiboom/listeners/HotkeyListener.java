package me.rqmses.aktiboom.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.event.KeyEvent;

import static me.rqmses.aktiboom.AktiBoom.bombe;
import static me.rqmses.aktiboom.AktiBoom.sprengguertel;

public class HotkeyListener {
    @SubscribeEvent
    public void onKlick(InputEvent.KeyInputEvent event) throws AWTException {
        if (Keyboard.isKeyDown(sprengguertel.getKeyCode())) {
            Robot KeyPresser = new Robot();
            GuiChat guiChat = new GuiChat("/sprengg\u00fcrtel 10");
            Minecraft.getMinecraft().displayGuiScreen(guiChat);
            KeyPresser.keyPress(KeyEvent.VK_ENTER);
        } else
        if (Keyboard.isKeyDown(bombe.getKeyCode())) {
            Robot KeyPresser = new Robot();
            GuiChat guiChat = new GuiChat("/bombe");
            Minecraft.getMinecraft().displayGuiScreen(guiChat);
            KeyPresser.keyPress(KeyEvent.VK_ENTER);
        }
    }
}
