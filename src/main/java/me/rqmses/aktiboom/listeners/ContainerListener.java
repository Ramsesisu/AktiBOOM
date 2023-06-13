package me.rqmses.aktiboom.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static me.rqmses.aktiboom.AktiBoom.*;

public class ContainerListener {
    private static ItemStack item = null;

    public static boolean verifysprengi = false;
    public static boolean verifyalpha = false;

    private static Timer sprengiequip = new Timer();
    private static Timer alphaequip = new Timer();

    private static long lastclick = 0;

    @SubscribeEvent
    public void onSlot(GuiScreenEvent.MouseInputEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiChest)) {
            return;
        }

        if (item != null) {
            if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1) || Mouse.isButtonDown(2)) {
                if (item.getDisplayName().contains("Sprengg\u00fcrtel")) {
                    if (System.currentTimeMillis() - lastclick < 500) {
                        event.setCanceled(true);
                        return;
                    }
                    lastclick = System.currentTimeMillis();

                    if (RANK < 3) {
                        player.sendMessage(new TextComponentString(PREFIX + "Du kannst dir keinen " + TextFormatting.GOLD + "Sprengg\u00fcrtel" + TextFormatting.YELLOW + " equippen!"));
                        event.setCanceled(true);
                        return;
                    }

                    if (!verifysprengi) {
                        event.setCanceled(true);
                        verifysprengi = true;
                        player.sendMessage(new TextComponentString(PREFIX + "Klicke erneut, um die Auswahl " + TextFormatting.GOLD + "Sprengg\u00fcrtel" + TextFormatting.YELLOW + " zu best\u00e4tigen!"));

                        sprengiequip.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (verifysprengi) {
                                    verifysprengi = false;
                                }
                            }
                        }, TimeUnit.SECONDS.toMillis(5));
                    } else {
                        verifysprengi = false;

                        sprengiequip = new Timer();
                    }
                }
                if (item.getDisplayName().contains("Alpha-7")) {
                    if (System.currentTimeMillis() - lastclick < 500) {
                        event.setCanceled(true);
                        return;
                    }
                    lastclick = System.currentTimeMillis();

                    if (RANK < 3) {
                        player.sendMessage(new TextComponentString(PREFIX + "Du kannst dir keine " + TextFormatting.GOLD + "Alpha-7" + TextFormatting.YELLOW + " equippen!"));
                        event.setCanceled(true);
                        return;
                    }

                    if (!SEC && RANK < 4) {
                        player.sendMessage(new TextComponentString(PREFIX + "Du darfst dir keine " + TextFormatting.GOLD + "Alpha-7" + TextFormatting.YELLOW + " equippen!"));
                        event.setCanceled(true);
                        return;
                    }

                    if (!verifyalpha) {
                        event.setCanceled(true);
                        verifyalpha = true;
                        player.sendMessage(new TextComponentString(PREFIX + "Klicke erneut, um die Auswahl " + TextFormatting.GOLD + "Alpha-7" + TextFormatting.YELLOW + " zu best\u00e4tigen!"));

                        alphaequip.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (verifyalpha) {
                                    verifyalpha = false;
                                }
                            }
                        }, TimeUnit.SECONDS.toMillis(5));
                    } else {
                        verifyalpha = false;

                        alphaequip = new Timer();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onGUI(GuiContainerEvent event) {
        if (event.getGuiContainer().getSlotUnderMouse() != null) {
            item = event.getGuiContainer().getSlotUnderMouse().getStack();
        }
    }
}
