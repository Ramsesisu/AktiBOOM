package me.rqmses.aktiboom.utils.guis.board;

import me.rqmses.aktiboom.utils.GameUtils;
import me.rqmses.aktiboom.utils.guis.containers.TicTacToeContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;
import java.util.List;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

public interface TicTacToe {
    default ItemStack getClearTile() {
        return new ItemStack(Item.getItemById(0));
    }
    default ItemStack getBlankTile() {
        if (TicTacToeContainer.color.equals("purple")) {
            return new ItemStack(Item.getItemById(160),1,2);
        } else {
            return new ItemStack(Item.getItemById(160),1,5);
        }
    }

    default ItemStack getPurpleTile() {
        return new ItemStack(Item.getItemById(35),1,10).setStackDisplayName(TextFormatting.LIGHT_PURPLE + "" + TextFormatting.BOLD + "Lila");
    }

    default ItemStack getGreenTile() {
        return new ItemStack(Item.getItemById(35),1,13).setStackDisplayName(TextFormatting.GREEN + "" + TextFormatting.BOLD + "Gr\u00fcn");
    }

    default ItemStack getExitTile() {
        return new ItemStack(Item.getItemById(35),1,14).setStackDisplayName(TextFormatting.RED + "" + TextFormatting.BOLD + "Beenden");
    }

    default void handleMove() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        List<String> board = Arrays.asList(GameUtils.board);

        if (board.get(3).equals(board.get(4)) && board.get(4).equals(board.get(5)) && !TicTacToeContainer.inventory.getStackInSlot(3).getItem().equals(getBlankTile().getItem())) {
            player.sendMessage(new TextComponentString(PREFIX + TicTacToeContainer.inventory.getStackInSlot(3).getDisplayName() + TextFormatting.YELLOW + " hat gewonnen!"));
            GameUtils.win = true;
        }
        if (board.get(12).equals(board.get(13)) && board.get(13).equals(board.get(14)) && !TicTacToeContainer.inventory.getStackInSlot(12).getItem().equals(getBlankTile().getItem())) {
            player.sendMessage(new TextComponentString(PREFIX + TicTacToeContainer.inventory.getStackInSlot(12).getDisplayName() + TextFormatting.YELLOW + " hat gewonnen!"));
            GameUtils.win = true;
        }
        if (board.get(21).equals(board.get(22)) && board.get(22).equals(board.get(23)) && !TicTacToeContainer.inventory.getStackInSlot(21).getItem().equals(getBlankTile().getItem())) {
            player.sendMessage(new TextComponentString(PREFIX + TicTacToeContainer.inventory.getStackInSlot(21).getDisplayName() + TextFormatting.YELLOW + " hat gewonnen!"));
            GameUtils.win = true;
        }

        if (board.get(3).equals(board.get(12)) && board.get(12).equals(board.get(21)) && !TicTacToeContainer.inventory.getStackInSlot(3).getItem().equals(getBlankTile().getItem())) {
            player.sendMessage(new TextComponentString(PREFIX + TicTacToeContainer.inventory.getStackInSlot(3).getDisplayName() + TextFormatting.YELLOW + " hat gewonnen!"));
            GameUtils.win = true;
        }
        if (board.get(4).equals(board.get(13)) && board.get(13).equals(board.get(22)) && !TicTacToeContainer.inventory.getStackInSlot(4).getItem().equals(getBlankTile().getItem())) {
            player.sendMessage(new TextComponentString(PREFIX + TicTacToeContainer.inventory.getStackInSlot(4).getDisplayName() + TextFormatting.YELLOW + " hat gewonnen!"));
            GameUtils.win = true;
        }
        if (board.get(5).equals(board.get(14)) && board.get(14).equals(board.get(23)) && !TicTacToeContainer.inventory.getStackInSlot(5).getItem().equals(getBlankTile().getItem())) {
            player.sendMessage(new TextComponentString(PREFIX + TicTacToeContainer.inventory.getStackInSlot(5).getDisplayName() + TextFormatting.YELLOW + " hat gewonnen!"));
            GameUtils.win = true;
        }

        if (board.get(3).equals(board.get(13)) && board.get(13).equals(board.get(23)) && !TicTacToeContainer.inventory.getStackInSlot(3).getItem().equals(getBlankTile().getItem())) {
            player.sendMessage(new TextComponentString(PREFIX + TicTacToeContainer.inventory.getStackInSlot(3).getDisplayName() + TextFormatting.YELLOW + " hat gewonnen!"));
            GameUtils.win = true;
        }
        if (board.get(5).equals(board.get(13)) && board.get(13).equals(board.get(21)) && !TicTacToeContainer.inventory.getStackInSlot(5).getItem().equals(getBlankTile().getItem())) {
            player.sendMessage(new TextComponentString(PREFIX + TicTacToeContainer.inventory.getStackInSlot(5).getDisplayName() + TextFormatting.YELLOW + " hat gewonnen!"));
            GameUtils.win = true;
        }
    }
}

