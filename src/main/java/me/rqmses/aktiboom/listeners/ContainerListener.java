package me.rqmses.aktiboom.listeners;

import me.rqmses.aktiboom.enums.InformationType;
import me.rqmses.aktiboom.utils.SheetUtils;
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

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static me.rqmses.aktiboom.AktiBoom.*;

public class ContainerListener {
    private static ItemStack item = null;

    public static boolean verifysprengi = false;
    public static boolean verifyrpg = false;

    private static Timer sprengiequip = new Timer();
    private static Timer rpgequip = new Timer();

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

                    if (!verifysprengi) {
                        try {
                            if (SheetUtils.getValueRange(InformationType.SPRENGGUERTEL_BAN.getSheet(), InformationType.SPRENGGUERTEL_BAN.getRange()).toString().contains(player.getName())) {
                                player.sendMessage(new TextComponentString(PREFIX + "Du hast eine " + TextFormatting.GOLD + "Sprengg\u00fcrtel" + TextFormatting.YELLOW + "-Sperre!"));
                                event.setCanceled(true);
                                return;
                            }
                        } catch (IOException ignored) {
                        }
                    }

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
                if (item.getDisplayName().contains("RPG-7")) {
                    if (System.currentTimeMillis() - lastclick < 500) {
                        event.setCanceled(true);
                        return;
                    }
                    lastclick = System.currentTimeMillis();

                    if (!verifyrpg) {
                        try {
                            if (SheetUtils.getValueRange(InformationType.RPG_7_BAN.getSheet(), InformationType.RPG_7_BAN.getRange()).toString().contains(player.getName())) {
                                player.sendMessage(new TextComponentString(PREFIX + "Du hast eine " + TextFormatting.GOLD + "RPG-7" + TextFormatting.YELLOW + "-Sperre!"));
                                event.setCanceled(true);
                                return;
                            }
                        } catch (IOException ignored) {
                        }
                    }

                    if (RANK < 3) {
                        player.sendMessage(new TextComponentString(PREFIX + "Du kannst dir keine " + TextFormatting.GOLD + "RPG-7" + TextFormatting.YELLOW + " equippen!"));
                        event.setCanceled(true);
                        return;
                    }

                    if (!SEC && RANK < 4) {
                        player.sendMessage(new TextComponentString(PREFIX + "Du darfst dir keine " + TextFormatting.GOLD + "RPG-7" + TextFormatting.YELLOW + " equippen!"));
                        event.setCanceled(true);
                        return;
                    }

                    if (!verifyrpg) {
                        event.setCanceled(true);
                        verifyrpg = true;
                        player.sendMessage(new TextComponentString(PREFIX + "Klicke erneut, um die Auswahl " + TextFormatting.GOLD + "RPG-7" + TextFormatting.YELLOW + " zu best\u00e4tigen!"));

                        rpgequip.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (verifyrpg) {
                                    verifyrpg = false;
                                }
                            }
                        }, TimeUnit.SECONDS.toMillis(5));
                    } else {
                        verifyrpg = false;

                        rpgequip = new Timer();
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
