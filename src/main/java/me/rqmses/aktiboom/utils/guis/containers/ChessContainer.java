package me.rqmses.aktiboom.utils.guis.containers;

import static me.rqmses.aktiboom.AktiBoom.PREFIX;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import me.rqmses.aktiboom.utils.GameUtils;
import me.rqmses.aktiboom.utils.guis.board.Chess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

@SuppressWarnings("ALL")
public class ChessContainer extends Container implements Chess {

    public static final IInventory inventory = new ContainerHorseChest("Schach", 72);

    public ChessContainer() {
        ItemStack itemStack;
        int index = 0;
        if ((GameUtils.turn % 2) == 0) {
            color = "iron";
        } else {
            color = "diamond";
        }

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
                        if (color.equals("iron")) {
                            kingpos = index;
                        }
                        itemStack = getWhiteKing();
                    } else {
                        if (color.equals("diamond")) {
                            kingpos = index;
                        }
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
                        if (Objects.equals(Character.toUpperCase(selectedfield.charAt(0)), 'P')) {
                            itemStack = getSpecialTile();
                        }
                    }
                    if (rochade.contains(index)) {
                        if (Objects.equals(Character.toUpperCase(selectedfield.charAt(0)), 'K')) {
                            itemStack = getSpecialTile();
                        }
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
                    if (row == 0) {
                        inventory.setInventorySlotContents(index, getDiamondPoints());
                    }
                    if (row == 7) {
                        inventory.setInventorySlotContents(index, getIronPoints());
                    }
                    if (row == 3) {
                        inventory.setInventorySlotContents(index, getExitTile());
                    }
                    if (row == 1) {
                        if (color.equals("diamond")) {
                            inventory.setInventorySlotContents(index, getTurnTile());
                        }
                    }
                    if (row == 6) {
                        if (color.equals("iron")) {
                            inventory.setInventorySlotContents(index, getTurnTile());
                        }
                    }
                }
                index++;
                addSlotToContainer(new Slot(inventory, col + row * 9, x, y));
            }
        }

        if (!mate && !draw) {
            if (getAllMoves().size() == 0) {
                if (getCheck(kingpos, kingpos).size() == 0) {
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(PREFIX + "Kein Zug mehr m\u00d6glich, Patt."));
                    draw = true;
                } else {
                    if (kingMoves(kingpos).size() == 0) {
                        if (color.equals("iron")) {
                            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(PREFIX + TextFormatting.WHITE + "" + TextFormatting.BOLD + "Iron" + TextFormatting.YELLOW + " ist Schach-Matt."));
                        } else {
                            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(PREFIX + TextFormatting.AQUA + "" + TextFormatting.BOLD + "Diamond" + TextFormatting.YELLOW + " ist Schach-Matt."));
                        }
                        mate = true;
                    }
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return false;
    }

    public static int selectedindex = -1;
    public static String selectedfield = "Ac";
    public static List<Integer> moves = new ArrayList<>();
    public static String color = "";
    public static List<Integer> enpassant = new ArrayList<>();
    public static List<Integer> rochade = new ArrayList<>();
    public static int kingpos = 67;
    public static boolean end = false;
    public static boolean mate = false;
    public static boolean draw = false;

    @Nonnull
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        EntityPlayerSP playerSP = Minecraft.getMinecraft().player;

        if (((slotId + 1) % 9) != 0) {
            if (GameUtils.playerturn.contains(playerSP.getName())) {
                if (slotId >= 0) {
                    if (selectedindex >= 0) {
                        if (selectedindex == slotId) {
                            moveZero();
                        } else if (Objects.requireNonNull(inventory.getStackInSlot(slotId).getItem().getRegistryName()).toString().contains(color)) {
                            moveOne(slotId);
                        } else {
                            moveTwo(slotId);
                        }
                    } else {
                        if (Objects.requireNonNull(inventory.getStackInSlot(slotId).getItem().getRegistryName()).toString().contains(color)) {
                            moveOne(slotId);
                        } else {
                            if (!inventory.getStackInSlot(slotId).getItem().equals(getWhiteTile().getItem())) {
                                String formatting;
                                if (color.equals("iron")) {
                                    formatting = TextFormatting.WHITE + "" + TextFormatting.BOLD;
                                } else {
                                    formatting = TextFormatting.AQUA + "" + TextFormatting.BOLD;
                                }
                                playerSP.sendMessage(new TextComponentString(PREFIX + "Du bist " + formatting + color.replaceFirst(String.valueOf(color.charAt(0)), String.valueOf(Character.toUpperCase(color.charAt(0)))) + TextFormatting.YELLOW + "!"));
                            }
                        }
                    }
                }
            } else {
                playerSP.sendMessage(new TextComponentString(PREFIX + TextFormatting.GOLD + GameUtils.playerturn + TextFormatting.YELLOW + " ist an der Reihe!"));
            }
            GameUtils.display();
        } else {
            if (slotId == 35) {
                playerSP.sendChatMessage("/f %PARTY% : end : " + GameUtils.players.toString().replace("[", "").replace("]", "").replace(",", ""));
            }
        }

        return new ItemStack(Item.getItemById(0));
    }

    private void moveZero() {
        selectedindex = -1;
        moves = new ArrayList<>();
    }

    private void moveOne(int slotId) {
        selectedfield = Arrays.asList(GameUtils.board).get(slotId);
        selectedindex = slotId;
        ChessContainer.moves = getMoves(selectedfield, selectedindex);
    }

    private void moveTwo(int slotId) {
        EntityPlayerSP playerSP = Minecraft.getMinecraft().player;

        if (!mate) {
            if (!draw) {
                if (moves.contains(slotId)) {
                    if (!Objects.equals(Character.toUpperCase(Arrays.asList(GameUtils.board).get(slotId).charAt(0)), 'K')) {
                        List<String> board = Arrays.asList(GameUtils.board);
                        int index = 0;

                        if (!Objects.equals(Character.toUpperCase(Arrays.asList(GameUtils.board).get(slotId).charAt(0)), 'A')) {
                            playerSP.sendMessage(new TextComponentString(PREFIX + inventory.getStackInSlot(selectedindex).getDisplayName() + TextFormatting.YELLOW + " schl\u00e4gt " + inventory.getStackInSlot(slotId).getDisplayName() + TextFormatting.YELLOW + "."));
                        }

                        for (String tile : board) {
                            if (Objects.equals(tile.charAt(0), 'p')) {
                                board.set(index, tile.replace('p', 'P'));
                            }
                            index++;
                        }

                        if (Objects.equals(Character.toUpperCase(selectedfield.charAt(0)), 'P')) {
                            if (slotId == (selectedindex + 18) || slotId == (selectedindex - 18)) {
                                selectedfield = selectedfield.replace('P', 'p');
                            }
                            if (enpassant.contains(slotId)) {
                                if (color.equals("iron")) {
                                    board.set(slotId + 9, "Ac");
                                } else {
                                    board.set(slotId - 9, "Ac");
                                }
                            }
                        }
                        if (Objects.equals(selectedfield.charAt(0), 'K')) {
                            selectedfield = selectedfield.replace('K', 'k');
                            if (rochade.contains(slotId)) {
                                if (slotId < selectedindex) {
                                    board.set(slotId + 1, board.get(slotId - 2).replace('R', 'r'));
                                    board.set(slotId - 2, "Ac");
                                } else {
                                    board.set(slotId - 1, board.get(slotId + 1).replace('R', 'r'));
                                    board.set(slotId + 1, "Ac");
                                }
                            }
                        }
                        if (Objects.equals(selectedfield.charAt(0), 'R')) {
                            selectedfield = selectedfield.replace('R', 'r');
                        }

                        enpassant = new ArrayList<>();
                        rochade = new ArrayList<>();

                        board.set(selectedindex, "Ac");
                        board.set(slotId, selectedfield);

                        if (Objects.equals(Character.toUpperCase(selectedfield.charAt(0)), 'P')) {
                            if (slotId < 9) {
                                board.set(slotId, "Qw");
                            }
                            if (slotId > 62) {
                                board.set(slotId, "Qb");
                            }
                        }

                        playerSP.sendChatMessage("/f %GAME% : " + GameUtils.players.toString().replace("[", "").replace("]", "").replace(",", "") + " :" + board.toString().replace("[", "").replace("]", "").replace(", ", "!"));
                        moves = new ArrayList<>();
                        selectedindex = -1;
                    } else {
                        playerSP.sendMessage(new TextComponentString(PREFIX + "Du kannst den K\u00f6nig nicht schlagen!"));
                    }
                }
            }else {
                playerSP.sendMessage(new TextComponentString(PREFIX + "Du bist patt."));
            }
        } else {
            playerSP.sendMessage(new TextComponentString(PREFIX + "Du bist schachmatt!"));
        }
    }
}