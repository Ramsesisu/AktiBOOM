package me.rqmses.aktiboom.utils.guis.containers;

import com.google.common.collect.ImmutableMap;
import me.rqmses.aktiboom.utils.GameUtils;
import me.rqmses.aktiboom.utils.guis.board.Chess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.*;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

public class ChessContainer extends Container implements Chess {

    public static final IInventory inventory = new ContainerHorseChest("Schach", 72);

    public ChessContainer() {
        ItemStack itemStack;
        int index = 0;
        for (String tile : GameUtils.board) {
            boolean w = Objects.equals(tile.charAt(1), 'w');
            switch (Character.toUpperCase(tile.charAt(0))) {
                case 'P':
                    if (w) {
                        itemStack = getWhitePawn();
                    } else {
                        itemStack = getBlackPawn();
                    }
                    break;
                case 'N':
                    if (w) {
                        itemStack = getWhiteKnight();
                    } else {
                        itemStack = getBlackKnight();
                    }
                    break;
                case 'B':
                    if (w) {
                        itemStack = getWhiteBishop();
                    } else {
                        itemStack = getBlackBishop();
                    }
                    break;
                case 'R':
                    if (w) {
                        itemStack = getWhiteRook();
                    } else {
                        itemStack = getBlackRook();
                    }
                    break;
                case 'Q':
                    if (w) {
                        itemStack = getWhiteQueen();
                    } else {
                        itemStack = getBlackQueen();
                    }
                    break;
                case 'K':
                    if (w) {
                        itemStack = getWhiteKing();
                    } else {
                        itemStack = getBlackKing();
                    }
                    break;
                default:
                    if ((index % 2) == 0) {
                        itemStack = getWhiteTile();
                    } else {
                        itemStack = getBlackTile();
                    }
                    if (moves.contains(index)) {
                        itemStack = getMoveTile();
                    }
                    if (enpassant.contains(index)) {
                        itemStack = getSpecialTile();
                    }
            }
            if (selectedindex == index) {
                EnchantmentHelper.setEnchantments(ImmutableMap.of(Enchantments.BANE_OF_ARTHROPODS, 1), itemStack);
            }
            inventory.setInventorySlotContents(index++, itemStack);
        }

        index = 0;
        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = row * 18 + 84;
                if (col == 8) {
                    inventory.setInventorySlotContents(index, getClearTile());
                }
                index++;
                addSlotToContainer(new Slot(inventory, col + row * 9, x, y));
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return false;
    }

    public static int selectedindex = -1;
    public static String selectedfield = "Aw";

    public static List<Integer> tempmoves = new ArrayList<>();
    public static List<Integer> moves = new ArrayList<>();
    public static String color = "";
    public static List<Integer> enpassant = new ArrayList<>();

    @Nonnull
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        EntityPlayerSP playerSP = Minecraft.getMinecraft().player;

        if (((slotId + 1) % 9) != 0) {
            if ((GameUtils.turn % 2) == 0) {
                color = "iron";
            } else {
                color = "diamond";
            }
            if (GameUtils.playerturn.contains(playerSP.getName())) {
                if (slotId >= 0) {
                    if (selectedindex >= 0) {
                        if (selectedindex == slotId) {
                            // 0. Mal
                            selectedindex = -1;
                            moves = new ArrayList<>();
                        } else if (Objects.requireNonNull(inventory.getStackInSlot(slotId).getItem().getRegistryName()).toString().contains(color)) {
                            // 1. Mal
                            selectedfield = Arrays.asList(GameUtils.board).get(slotId);
                            selectedindex = slotId;
                            handleMoves(selectedfield, selectedindex);
                        } else {
                            // 2. Mal
                            if (moves.contains(slotId)) {
                                if (!Objects.equals(Character.toUpperCase(Arrays.asList(GameUtils.board).get(slotId).charAt(0)), 'K')) {
                                    List<String> board = Arrays.asList(GameUtils.board);
                                    moves = new ArrayList<>();
                                    if (Objects.equals(Character.toUpperCase(selectedfield.charAt(0)), 'P')) {
                                        if (slotId == (selectedindex + 18) || slotId == (selectedindex - 18)) {
                                            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("2er Bauer"));
                                            selectedfield = selectedfield.replace('P', 'p');
                                        }
                                        if (enpassant.contains(slotId)) {
                                            if (color.equals("iron")) {
                                                board.set(slotId - 9, "Aw");
                                            } else {
                                                board.set(slotId + 9, "Aw");
                                            }
                                            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("En Passant"));
                                        }
                                    }
                                    board.set(selectedindex, "Aw");
                                    board.set(slotId, selectedfield);
                                    if (Objects.equals(Character.toUpperCase(selectedfield.charAt(0)), 'P')) {
                                        if (slotId < 9) {
                                            board.set(slotId, "Qw");
                                        }
                                        if (slotId > 63) {
                                            board.set(slotId, "Qb");
                                        }
                                    }
                                    playerSP.sendChatMessage(": %GAME% : " + GameUtils.players.toString().replace("[", "").replace("]", "").replace(",", "") + " :" + board.toString().replace("[", "").replace("]", "").replace(", ", "!"));
                                    selectedindex = -1;
                                } else {
                                    playerSP.sendMessage(new TextComponentString(PREFIX + "Du kannst den K\u00f6nig nicht schlagen!"));
                                }
                            }
                        }
                    } else {
                        // 1. Mal
                        if (Objects.requireNonNull(inventory.getStackInSlot(slotId).getItem().getRegistryName()).toString().contains(color)) {
                            selectedfield = Arrays.asList(GameUtils.board).get(slotId);
                            selectedindex = slotId;
                            handleMoves(selectedfield, selectedindex);
                        } else {
                            if (!inventory.getStackInSlot(slotId).getItem().equals(getWhiteTile().getItem())) {
                                String formatting;
                                if (color.equals("iron")) {
                                    formatting = TextFormatting.WHITE + "" + TextFormatting.BOLD;
                                } else {
                                    formatting = TextFormatting.AQUA + "" + TextFormatting.BOLD;
                                }
                                playerSP.sendMessage(new TextComponentString(PREFIX + "Du bist " + formatting + color + TextFormatting.YELLOW + "!"));
                            }
                        }
                    }
                }
            } else {
                playerSP.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + GameUtils.playerturn + TextFormatting.YELLOW + " ist an der Reihe!"));
            }
            GameUtils.display();
        }

        return new ItemStack(Item.getItemById(0));
    }
}