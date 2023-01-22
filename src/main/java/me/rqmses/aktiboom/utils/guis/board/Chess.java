package me.rqmses.aktiboom.utils.guis.board;

import me.rqmses.aktiboom.utils.GameUtils;
import me.rqmses.aktiboom.utils.guis.containers.ChessContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public interface Chess {
    default ItemStack getWhiteTile() {
        return new ItemStack(Item.getItemById(160),1,0);
    }

    default ItemStack getBlackTile() {
        return new ItemStack(Item.getItemById(160),1,15);
    }

    default ItemStack getMoveTile() {
        return new ItemStack(Item.getItemById(160),1,13);
    }

    default ItemStack getSpecialTile() {
        return new ItemStack(Item.getItemById(160),1,10);
    }

    default ItemStack getClearTile() {
        return new ItemStack(Item.getItemById(0));
    }

    default ItemStack getWhitePawn() {
        return new ItemStack(Item.getItemById(309),1).setStackDisplayName(TextFormatting.WHITE + "" + TextFormatting.BOLD + "Bauer");
    }

    default ItemStack getBlackPawn() {
        return new ItemStack(Item.getItemById(313),1).setStackDisplayName(TextFormatting.GRAY + "" + TextFormatting.BOLD + "Bauer");
    }

    default ItemStack getWhiteKnight() {
        return new ItemStack(Item.getItemById(258),3).setStackDisplayName(TextFormatting.WHITE + "" + TextFormatting.BOLD + "Springer");
    }

    default ItemStack getBlackKnight() {
        return new ItemStack(Item.getItemById(279),3).setStackDisplayName(TextFormatting.GRAY + "" + TextFormatting.BOLD + "Springer");
    }

    default ItemStack getWhiteBishop() {
        return new ItemStack(Item.getItemById(256),3).setStackDisplayName(TextFormatting.WHITE + "" + TextFormatting.BOLD + "L\u00e4ufer");
    }

    default ItemStack getBlackBishop() {
        return new ItemStack(Item.getItemById(277),3).setStackDisplayName(TextFormatting.GRAY + "" + TextFormatting.BOLD + "L\u00e4ufer");
    }

    default ItemStack getWhiteRook() {
        return new ItemStack(Item.getItemById(308),5).setStackDisplayName(TextFormatting.WHITE + "" + TextFormatting.BOLD + "Turm");
    }

    default ItemStack getBlackRook() {
        return new ItemStack(Item.getItemById(312),5).setStackDisplayName(TextFormatting.GRAY + "" + TextFormatting.BOLD + "Turm");
    }

    default ItemStack getWhiteQueen() {
        return new ItemStack(Item.getItemById(307),9).setStackDisplayName(TextFormatting.WHITE + "" + TextFormatting.BOLD + "Dame");
    }

    default ItemStack getBlackQueen() {
        return new ItemStack(Item.getItemById(311),9).setStackDisplayName(TextFormatting.GRAY + "" + TextFormatting.BOLD + "Dame");
    }

    default ItemStack getWhiteKing() {
        return new ItemStack(Item.getItemById(306),64).setStackDisplayName(TextFormatting.WHITE + "" + TextFormatting.BOLD + "K\u00f6nig");
    }

    default ItemStack getBlackKing() {
        return new ItemStack(Item.getItemById(310),64).setStackDisplayName(TextFormatting.GRAY + "" + TextFormatting.BOLD + "K\u00f6nig");
    }

    default void handleMoves(String piece, int index) {
        ChessContainer.moves = new ArrayList<>();
        ChessContainer.tempmoves = new ArrayList<>();
        switch (Character.toUpperCase(piece.charAt(0))) {
            case 'P':
                ChessContainer.moves = pawnMoves(index, piece.charAt(1));
                break;
            case 'N':
                ChessContainer.moves = knightMoves(index);
                break;
            case 'B':
                ChessContainer.moves = bishopMoves(index);
                break;
            case 'R':
                ChessContainer.moves = rookMoves(index);
                break;
            case 'Q':
                ChessContainer.moves = queenMoves(index);
                break;
            case 'K':
                ChessContainer.moves = kingMoves(index);
                break;
            default:
        }
    }

    default List<Integer> pawnMoves(int pos, char color) {
        ChessContainer.enpassant = new ArrayList<>();
        if (Objects.equals(Character.toLowerCase(color), 'w')) {
            if (Objects.equals(Arrays.asList(GameUtils.board).get(pos - 9).charAt(0), 'A')) {
                ChessContainer.tempmoves.add(pos - 9);
            }
            if (pos > 53) {
                if (Objects.equals(Arrays.asList(GameUtils.board).get(pos - 9).charAt(0), 'A')) {
                    ChessContainer.tempmoves.add(pos - 18);
                }
            }
            if (!Objects.equals(Arrays.asList(GameUtils.board).get(pos - 8).charAt(0), 'A')) {
                ChessContainer.tempmoves.add(pos - 8);
            }
            if (!Objects.equals(Arrays.asList(GameUtils.board).get(pos - 10).charAt(0), 'A')) {
                ChessContainer.tempmoves.add(pos - 10);
            }

            if (Objects.equals(Arrays.asList(GameUtils.board).get(pos - 1).charAt(0), 'p')) {
                if (!Objects.requireNonNull(ChessContainer.inventory.getStackInSlot(pos - 1).getItem().getRegistryName()).toString().contains(ChessContainer.color)) {
                    ChessContainer.tempmoves.add(pos - 10);
                    ChessContainer.enpassant.add(pos - 10);
                }
            }
            if (Objects.equals(Arrays.asList(GameUtils.board).get(pos + 1).charAt(0), 'p')) {
                if (!Objects.requireNonNull(ChessContainer.inventory.getStackInSlot(pos + 1).getItem().getRegistryName()).toString().contains(ChessContainer.color)) {
                    ChessContainer.tempmoves.add(pos - 8);
                    ChessContainer.enpassant.add(pos - 8);
                }
            }
        } else {
            if (Objects.equals(Arrays.asList(GameUtils.board).get(pos + 9).charAt(0), 'A')) {
                ChessContainer.tempmoves.add(pos + 9);
            }
            if (pos < 18) {
                if (Objects.equals(Arrays.asList(GameUtils.board).get(pos + 9).charAt(0), 'A')) {
                    ChessContainer.tempmoves.add(pos + 18);
                }
            }
            if (!Objects.equals(Arrays.asList(GameUtils.board).get(pos + 8).charAt(0), 'A')) {
                ChessContainer.tempmoves.add(pos + 8);
            }
            if (!Objects.equals(Arrays.asList(GameUtils.board).get(pos + 10).charAt(0), 'A')) {
                ChessContainer.tempmoves.add(pos + 10);
            }

            if (Objects.equals(Arrays.asList(GameUtils.board).get(pos - 1).charAt(0), 'p')) {
                if (!Objects.requireNonNull(ChessContainer.inventory.getStackInSlot(pos - 1).getItem().getRegistryName()).toString().contains(ChessContainer.color)) {
                    ChessContainer.tempmoves.add(pos + 8);
                    ChessContainer.enpassant.add(pos + 8);
                }
            }
            if (Objects.equals(Arrays.asList(GameUtils.board).get(pos + 1).charAt(0), 'p')) {
                if (!Objects.requireNonNull(ChessContainer.inventory.getStackInSlot(pos + 1).getItem().getRegistryName()).toString().contains(ChessContainer.color)) {
                    ChessContainer.tempmoves.add(pos + 10);
                    ChessContainer.enpassant.add(pos + 10);
                }
            }
        }
        return ChessContainer.tempmoves;
    }

    default List<Integer> knightMoves(int pos) {
        ChessContainer.tempmoves.add(pos - 19);
        ChessContainer.tempmoves.add(pos - 17);
        ChessContainer.tempmoves.add(pos - 11);
        ChessContainer.tempmoves.add(pos - 7);
        ChessContainer.tempmoves.add(pos + 7);
        ChessContainer.tempmoves.add(pos + 11);
        ChessContainer.tempmoves.add(pos + 17);
        ChessContainer.tempmoves.add(pos + 19);
        return ChessContainer.tempmoves;
    }

    default List<Integer> bishopMoves(int pos) {
        getDiagonalRD(pos);
        getDiagonalLD(pos);
        getDiagonalLU(pos);
        getDiagonalRU(pos);
        return ChessContainer.tempmoves;
    }

    default List<Integer> rookMoves(int pos) {
        getStraightR(pos);
        getStraightL(pos);
        getStraightU(pos);
        getStraightD(pos);
        return ChessContainer.tempmoves;
    }

    default List<Integer> queenMoves(int pos) {
        getStraightR(pos);
        getStraightL(pos);
        getStraightU(pos);
        getStraightD(pos);
        getDiagonalRD(pos);
        getDiagonalLD(pos);
        getDiagonalLU(pos);
        getDiagonalRU(pos);
        return ChessContainer.tempmoves;
    }

    default List<Integer> kingMoves(int pos) {
        ChessContainer.tempmoves.add(pos - 1);
        ChessContainer.tempmoves.add(pos + 1);
        ChessContainer.tempmoves.add(pos - 9);
        ChessContainer.tempmoves.add(pos + 9);
        ChessContainer.tempmoves.add(pos - 10);
        ChessContainer.tempmoves.add(pos + 10);
        ChessContainer.tempmoves.add(pos - 8);
        ChessContainer.tempmoves.add(pos + 8);

        return ChessContainer.tempmoves;
    }

    static void getDiagonalRD(int pos) {
        try {
            setField(pos + 10);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 20);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 30);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 40);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 50);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 60);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 70);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    static void getDiagonalLD(int pos) {
        try {
            setField(pos + 8);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 16);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 24);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 32);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 40);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 48);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 56);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    static void getDiagonalLU(int pos) {
        try {
            setField(pos - 10);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 20);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 30);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 40);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 50);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 60);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 70);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    static void getDiagonalRU(int pos) {
        try {
            setField(pos - 8);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 16);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 24);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 32);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 40);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 48);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 56);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    static void getStraightL(int pos) {
        try {
            setField(pos - 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 2);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 3);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 4);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 5);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 6);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 7);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    static void getStraightR(int pos) {
        try {
            setField(pos + 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 2);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 3);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 4);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 5);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 6);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 7);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    static void getStraightD(int pos) {
        try {
            setField(pos + 9);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 18);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 27);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 36);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 45);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 54);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos + 63);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    static void getStraightU(int pos) {
        try {
            setField(pos - 9);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 18);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 27);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 36);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 45);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 54);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        try {
            setField(pos - 63);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    static void setField(int index) throws ArrayIndexOutOfBoundsException {
        if (index >= 0 && index < GameUtils.board.length) {
            if (!Objects.equals(Arrays.asList(GameUtils.board).get(index).charAt(0), 'T')) {
                ChessContainer.tempmoves.add(index);
                if (!Objects.equals(Arrays.asList(GameUtils.board).get(index).charAt(0), 'A')) {
                    throw new ArrayIndexOutOfBoundsException();
                }
            } else {
                throw new ArrayIndexOutOfBoundsException();
            }
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }
}

