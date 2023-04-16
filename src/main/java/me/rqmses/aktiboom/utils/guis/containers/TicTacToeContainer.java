package me.rqmses.aktiboom.utils.guis.containers;

import me.rqmses.aktiboom.utils.GameUtils;
import me.rqmses.aktiboom.utils.guis.board.TicTacToe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ALL")
public class TicTacToeContainer extends Container implements TicTacToe {

    public static final IInventory inventory = new ContainerHorseChest("Schach", 27);

    public static String color = "";

    public TicTacToeContainer() {
        if ((GameUtils.turn % 2) == 0) {
            color = "purple";
        } else {
            color = "green";
        }
        ItemStack itemStack;
        int index = 0;
        for (String tile : GameUtils.board) {
            if (Character.toUpperCase(tile.charAt(0)) == 'A') {
                itemStack = getBlankTile();
                if (tile.charAt(1) == 'p') {
                    itemStack = getPurpleTile();
                }
                if (tile.charAt(1) == 'g') {
                    itemStack = getGreenTile();
                }
            } else {
                itemStack = getClearTile();
            }
            inventory.setInventorySlotContents(index++, itemStack);
        }

        index = 0;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = row * 18 + 84;
                if (col == 0 || col == 1 || col == 2 || col == 6 || col == 7 || col == 8) {
                    inventory.setInventorySlotContents(index, getClearTile());
                }
                if (col == 7) {
                    if (row == 1) {
                        inventory.setInventorySlotContents(index, getExitTile());
                    }
                }
                index++;
                addSlotToContainer(new Slot(inventory, col + row * 9, x, y));
            }
        }

        handleMove();
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        EntityPlayerSP playerSP = Minecraft.getMinecraft().player;

        if (inventory.getStackInSlot(slotId).getItem().equals(getBlankTile().getItem())) {
            if (GameUtils.playerturn.contains(playerSP.getName())) {
                if (!GameUtils.win) {
                    List<String> board = Arrays.asList(GameUtils.board);

                    if (color.equals("purple")) {
                        board.set(slotId, "Ap");
                    } else {
                        board.set(slotId, "Ag");
                    }
                    playerSP.sendChatMessage("/f %GAME% : " + GameUtils.players.toString().replace("[", "").replace("]", "").replace(",", "") + " :" + board.toString().replace("[", "").replace("]", "").replace(", ", "!"));
                }
            }
        } else {
            if (slotId == 16) {
                playerSP.sendChatMessage("/f %PARTY% : end : " + GameUtils.players.toString().replace("[", "").replace("]", "").replace(",", ""));
            }
        }

        return new ItemStack(Item.getItemById(0));
    }
}