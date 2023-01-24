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

    default ItemStack getExitTile() {
        return new ItemStack(Item.getItemById(35),1,14).setStackDisplayName(TextFormatting.RED + "" + TextFormatting.BOLD + "Beenden");
    }

    default ItemStack getIronPoints() {
        int ironpoints = 0;
        for (int i = 0; i < ChessContainer.inventory.getSizeInventory(); i++) {
            if (Objects.requireNonNull(ChessContainer.inventory.getStackInSlot(i).getItem().getRegistryName()).toString().contains("iron")) {
                ironpoints = ironpoints + ChessContainer.inventory.getStackInSlot(i).getCount();
            }
        }
        int diamondpoints = 0;
        for (int i = 0; i < ChessContainer.inventory.getSizeInventory(); i++) {
            if (Objects.requireNonNull(ChessContainer.inventory.getStackInSlot(i).getItem().getRegistryName()).toString().contains("diamond")) {
                diamondpoints = diamondpoints + ChessContainer.inventory.getStackInSlot(i).getCount();
            }
        }
        int points = ironpoints - diamondpoints;
        String stringpoints;
        if (points >= 0) {
            stringpoints = TextFormatting.GREEN + "" + TextFormatting.BOLD + points;
        } else {
            stringpoints = TextFormatting.RED + "" + TextFormatting.BOLD + points;
        }
        return new ItemStack(Item.getItemById(323),1).setStackDisplayName(TextFormatting.WHITE + "Punkte: " + stringpoints);
    }

    default ItemStack getDiamondPoints() {
        int ironpoints = 0;
        for (int i = 0; i < ChessContainer.inventory.getSizeInventory(); i++) {
            if (Objects.requireNonNull(ChessContainer.inventory.getStackInSlot(i).getItem().getRegistryName()).toString().contains("iron")) {
                ironpoints = ironpoints + ChessContainer.inventory.getStackInSlot(i).getCount();
            }
        }
        int diamondpoints = 0;
        for (int i = 0; i < ChessContainer.inventory.getSizeInventory(); i++) {
            if (Objects.requireNonNull(ChessContainer.inventory.getStackInSlot(i).getItem().getRegistryName()).toString().contains("diamond")) {
                diamondpoints = diamondpoints + ChessContainer.inventory.getStackInSlot(i).getCount();
            }
        }
        int points = diamondpoints - ironpoints;
        String stringpoints;
        if (points >= 0) {
            stringpoints = TextFormatting.GREEN + "" + TextFormatting.BOLD + points;
        } else {
            stringpoints = TextFormatting.RED + "" + TextFormatting.BOLD + points;
        }
        return new ItemStack(Item.getItemById(323),1).setStackDisplayName(TextFormatting.WHITE + "Punkte: " + stringpoints);
    }

    default ItemStack getWhitePawn() {
        return new ItemStack(Item.getItemById(309),1).setStackDisplayName(TextFormatting.WHITE + "" + TextFormatting.BOLD + "Bauer");
    }

    default ItemStack getBlackPawn() {
        return new ItemStack(Item.getItemById(313),1).setStackDisplayName(TextFormatting.AQUA + "" + TextFormatting.BOLD + "Bauer");
    }

    default ItemStack getWhiteKnight() {
        return new ItemStack(Item.getItemById(258),3).setStackDisplayName(TextFormatting.WHITE + "" + TextFormatting.BOLD + "Springer");
    }

    default ItemStack getBlackKnight() {
        return new ItemStack(Item.getItemById(279),3).setStackDisplayName(TextFormatting.AQUA + "" + TextFormatting.BOLD + "Springer");
    }

    default ItemStack getWhiteBishop() {
        return new ItemStack(Item.getItemById(256),3).setStackDisplayName(TextFormatting.WHITE + "" + TextFormatting.BOLD + "L\u00e4ufer");
    }

    default ItemStack getBlackBishop() {
        return new ItemStack(Item.getItemById(277),3).setStackDisplayName(TextFormatting.AQUA + "" + TextFormatting.BOLD + "L\u00e4ufer");
    }

    default ItemStack getWhiteRook() {
        return new ItemStack(Item.getItemById(308),5).setStackDisplayName(TextFormatting.WHITE + "" + TextFormatting.BOLD + "Turm");
    }

    default ItemStack getBlackRook() {
        return new ItemStack(Item.getItemById(312),5).setStackDisplayName(TextFormatting.AQUA + "" + TextFormatting.BOLD + "Turm");
    }

    default ItemStack getWhiteQueen() {
        return new ItemStack(Item.getItemById(307),9).setStackDisplayName(TextFormatting.WHITE + "" + TextFormatting.BOLD + "Dame");
    }

    default ItemStack getBlackQueen() {
        return new ItemStack(Item.getItemById(311),9).setStackDisplayName(TextFormatting.AQUA + "" + TextFormatting.BOLD + "Dame");
    }

    default ItemStack getWhiteKing() {
        return new ItemStack(Item.getItemById(306),64).setStackDisplayName(TextFormatting.WHITE + "" + TextFormatting.BOLD + "K\u00f6nig");
    }

    default ItemStack getBlackKing() {
        return new ItemStack(Item.getItemById(310),64).setStackDisplayName(TextFormatting.AQUA + "" + TextFormatting.BOLD + "K\u00f6nig");
    }

    default List<Integer> getMoves(String piece, int index) {
        List<Integer> moves = new ArrayList<>();

        switch (Character.toUpperCase(piece.charAt(0))) {
            case 'P':
                moves = pawnMoves(index, piece.charAt(1));
                break;
            case 'N':
                moves = knightMoves(index);
                break;
            case 'B':
                moves = bishopMoves(index);
                break;
            case 'R':
                moves = rookMoves(index);
                break;
            case 'Q':
                moves = queenMoves(index);
                break;
            case 'K':
                moves = kingMoves(index);
                break;
        }

        List<Integer> illegalmoves = new ArrayList<>();
        for (int move : moves) {
            String checkfield = GameUtils.board[move];
            String field = GameUtils.board[index];

            if (ChessContainer.color.equals("iron")) {
                GameUtils.board[move] = "Pw";
            } else {
                GameUtils.board[move] = "Pb";
            }
            GameUtils.board[index] = "Ac";

            if (getCheck(ChessContainer.kingpos, ChessContainer.kingpos).size() != 0) {
                illegalmoves.add(move);
            }

            GameUtils.board[move] = checkfield;
            GameUtils.board[index] = field;
        }
        moves.removeAll(illegalmoves);

        return moves;
    }

    default List<Integer> getAllMoves() {
        List<Integer> moves = new ArrayList<>();

        int index = 0;
        for (String tile : GameUtils.board) {
            char color;
            if (ChessContainer.color.equals("iron")) {
                color = 'w';
            } else {
                color = 'b';
            }
            if (Objects.equals(tile.charAt(1), color)) {
                moves.addAll(getMoves(tile, index));
            }
            index++;
        }

        return moves;
    }

    static List<Integer> pawnMoves(int pos, char color) {
        List<Integer> tempmoves = new ArrayList<>();
        ChessContainer.enpassant = new ArrayList<>();

        if (Objects.equals(Character.toLowerCase(color), 'w')) {
            if (Objects.equals(Arrays.asList(GameUtils.board).get(pos - 9).charAt(0), 'A')) {
                tempmoves.addAll(getSingle(pos - 9));
            }
            if (pos > 53) {
                if (Objects.equals(Arrays.asList(GameUtils.board).get(pos - 9).charAt(0), 'A') && Objects.equals(Arrays.asList(GameUtils.board).get(pos - 18).charAt(0), 'A')) {
                    tempmoves.addAll(getSingle(pos - 18));
                }
            }
            if (!Objects.equals(Arrays.asList(GameUtils.board).get(pos - 8).charAt(0), 'A')) {
                tempmoves.addAll(getSingle(pos - 8));
            }
            if (!Objects.equals(Arrays.asList(GameUtils.board).get(pos - 10).charAt(0), 'A')) {
                tempmoves.addAll(getSingle(pos - 10));
            }

            if (Objects.equals(Character.toUpperCase(ChessContainer.selectedfield.charAt(0)), 'P')) {
                if (Objects.equals(Arrays.asList(GameUtils.board).get(pos - 1).charAt(0), 'p')) {
                    if (!Objects.requireNonNull(ChessContainer.inventory.getStackInSlot(pos - 1).getItem().getRegistryName()).toString().contains(ChessContainer.color)) {
                        tempmoves.addAll(getSingle(pos - 10));
                        ChessContainer.enpassant.addAll(getSingle(pos - 10));
                    }
                }
                if (Objects.equals(Arrays.asList(GameUtils.board).get(pos + 1).charAt(0), 'p')) {
                    if (!Objects.requireNonNull(ChessContainer.inventory.getStackInSlot(pos + 1).getItem().getRegistryName()).toString().contains(ChessContainer.color)) {
                        tempmoves.addAll(getSingle(pos - 8));
                        ChessContainer.enpassant.addAll(getSingle(pos - 8));
                    }
                }
            }
        } else {
            if (Objects.equals(Arrays.asList(GameUtils.board).get(pos + 9).charAt(0), 'A')) {
                tempmoves.addAll(getSingle(pos + 9));
            }
            if (pos < 18) {
                if (Objects.equals(Arrays.asList(GameUtils.board).get(pos + 9).charAt(0), 'A') && Objects.equals(Arrays.asList(GameUtils.board).get(pos + 18).charAt(0), 'A')) {
                    tempmoves.addAll(getSingle(pos + 18));
                }
            }
            if (!Objects.equals(Arrays.asList(GameUtils.board).get(pos + 8).charAt(0), 'A')) {
                tempmoves.addAll(getSingle(pos + 8));
            }
            if (!Objects.equals(Arrays.asList(GameUtils.board).get(pos + 10).charAt(0), 'A')) {
                tempmoves.addAll(getSingle(pos + 10));
            }

            if (Objects.equals(Character.toUpperCase(ChessContainer.selectedfield.charAt(0)), 'P')) {
                if (Objects.equals(Arrays.asList(GameUtils.board).get(pos - 1).charAt(0), 'p')) {
                    if (!Objects.requireNonNull(ChessContainer.inventory.getStackInSlot(pos - 1).getItem().getRegistryName()).toString().contains(ChessContainer.color)) {
                        tempmoves.addAll(getSingle(pos + 8));
                        ChessContainer.enpassant.addAll(getSingle(pos + 8));
                    }
                }
                if (Objects.equals(Arrays.asList(GameUtils.board).get(pos + 1).charAt(0), 'p')) {
                    if (!Objects.requireNonNull(ChessContainer.inventory.getStackInSlot(pos + 1).getItem().getRegistryName()).toString().contains(ChessContainer.color)) {
                        tempmoves.addAll(getSingle(pos + 10));
                        ChessContainer.enpassant.addAll(getSingle(pos + 10));
                    }
                }
            }
        }
        return tempmoves;
    }

    static List<Integer> knightMoves(int pos) {
        List<Integer> tempmoves = new ArrayList<>();

        tempmoves.addAll(getSingle(pos - 19));
        tempmoves.addAll(getSingle(pos - 17));
        tempmoves.addAll(getSingle(pos + 17));
        tempmoves.addAll(getSingle(pos + 19));
        if ((pos + 1) % 9 != 1) {
            tempmoves.addAll(getSingle(pos + 7));
            tempmoves.addAll(getSingle(pos - 11));
        }
        if ((pos + 1) % 9 != 8) {
            tempmoves.addAll(getSingle(pos - 7));
            tempmoves.addAll(getSingle(pos + 11));
        }

        return tempmoves;
    }

    static List<Integer> bishopMoves(int pos) {
        List<Integer> tempmoves = new ArrayList<>();

        tempmoves.addAll(getDiagonalRD(pos));
        tempmoves.addAll(getDiagonalLD(pos));
        tempmoves.addAll(getDiagonalLU(pos));
        tempmoves.addAll(getDiagonalRU(pos));

        return tempmoves;
    }

    static List<Integer> rookMoves(int pos) {
        List<Integer> tempmoves = new ArrayList<>();

        tempmoves.addAll(getStraightR(pos));
        tempmoves.addAll(getStraightL(pos));
        tempmoves.addAll(getStraightU(pos));
        tempmoves.addAll(getStraightD(pos));

        return tempmoves;
    }

    static List<Integer> queenMoves(int pos) {
        List<Integer> tempmoves = new ArrayList<>();

        tempmoves.addAll(getStraightR(pos));
        tempmoves.addAll(getStraightL(pos));
        tempmoves.addAll(getStraightU(pos));
        tempmoves.addAll(getStraightD(pos));
        tempmoves.addAll(getDiagonalRD(pos));
        tempmoves.addAll(getDiagonalLD(pos));
        tempmoves.addAll(getDiagonalLU(pos));
        tempmoves.addAll(getDiagonalRU(pos));

        return tempmoves;
    }

    default List<Integer> kingMoves(int pos) {
        List<Integer> tempmoves = new ArrayList<>();

        ChessContainer.rochade = new ArrayList<>();

        tempmoves.addAll(getSingle(pos - 1));
        tempmoves.addAll(getSingle(pos + 1));
        tempmoves.addAll(getSingle(pos - 9));
        tempmoves.addAll(getSingle(pos + 9));
        tempmoves.addAll(getSingle(pos - 10));
        tempmoves.addAll(getSingle(pos + 10));
        tempmoves.addAll(getSingle(pos - 8));
        tempmoves.addAll(getSingle(pos + 8));

        if (Objects.equals(Character.toUpperCase(ChessContainer.selectedfield.charAt(0)), 'K')) {
            if (Objects.equals(Arrays.asList(GameUtils.board).get(pos).charAt(0), 'K')) {
                if (Objects.equals(Arrays.asList(GameUtils.board).get(pos - 1).charAt(0), 'A') && Objects.equals(Arrays.asList(GameUtils.board).get(pos - 2).charAt(0), 'A') && Objects.equals(Arrays.asList(GameUtils.board).get(pos - 3).charAt(0), 'A') && Objects.equals(Arrays.asList(GameUtils.board).get(pos - 4).charAt(0), 'R')) {
                    if (Objects.requireNonNull(ChessContainer.inventory.getStackInSlot(pos - 4).getItem().getRegistryName()).toString().contains(ChessContainer.color)) {
                        tempmoves.addAll(getSingle(pos - 2));
                        ChessContainer.rochade.add(pos - 2);
                    }
                }
                if (Objects.equals(Arrays.asList(GameUtils.board).get(pos + 1).charAt(0), 'A') && Objects.equals(Arrays.asList(GameUtils.board).get(pos + 2).charAt(0), 'A') && Objects.equals(Arrays.asList(GameUtils.board).get(pos + 3).charAt(0), 'R')) {
                    if (Objects.requireNonNull(ChessContainer.inventory.getStackInSlot(pos + 3).getItem().getRegistryName()).toString().contains(ChessContainer.color)) {
                        tempmoves.addAll(getSingle(pos + 2));
                        ChessContainer.rochade.add(pos + 2);
                    }
                }
            }
        }

        List<Integer> illegalmoves = new ArrayList<>();
        for (int move : tempmoves) {
            if (getCheck(move, pos).size() > 0) {
                illegalmoves.add(move);
            }
        }
        tempmoves.removeAll(illegalmoves);

        return tempmoves;
    }

    default boolean checkMate(int kingpos) {
        if (kingMoves(kingpos).size() == 0) {
            List<Integer> threats = getCheck(kingpos, kingpos);
            if (threats.size() > 0) {
                List<Integer> possiblemoves = new ArrayList<>();
                for (int pos : getAllMoves()) {
                    if (threats.contains(pos)) {
                        possiblemoves.add(pos);
                        break;
                    }
                }
                threats.removeAll(possiblemoves);

                if (threats.size() != 0) {
                    for (int pos : getAllMoves()) {
                        String field = GameUtils.board[pos];

                        if (ChessContainer.color.equals("iron")) {
                            GameUtils.board[pos] = "Pw";
                        } else {
                            GameUtils.board[pos] = "Pb";
                        }

                        GameUtils.board[pos] = field;

                        if (getCheck(kingpos, kingpos).size() == 0) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    default List<Integer> getCheck(int pos, int kingpos) {
        List<String> tiles = Arrays.asList(GameUtils.board);
        List<Integer> threats = new ArrayList<>();

        String field = GameUtils.board[kingpos];
        GameUtils.board[kingpos] = "Ac";

        char color;
        if (ChessContainer.color.equals("iron")) {
            color = 'w';
        } else {
            color = 'b';
        }

        for (int move : getStraightU(pos)) {
            if (move >= 0 && move < tiles.size()) {
                char c = Character.toUpperCase(tiles.get(move).charAt(0));
                if (Objects.equals(c, 'R') || Objects.equals(c, 'Q')) {
                    if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                        threats.add(move);
                    }
                }
            }
        }
        for (int move : getStraightD(pos)) {
            if (move >= 0 && move < tiles.size()) {
                char c = Character.toUpperCase(tiles.get(move).charAt(0));
                if (Objects.equals(c, 'R') || Objects.equals(c, 'Q')) {
                    if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                        threats.add(move);
                    }
                }
            }
        }
        for (int move : getStraightL(pos)) {
            if (move >= 0 && move < tiles.size()) {
                char c = Character.toUpperCase(tiles.get(move).charAt(0));
                if (Objects.equals(c, 'R') || Objects.equals(c, 'Q')) {
                    if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                        threats.add(move);
                    }
                }
            }
        }
        for (int move : getStraightR(pos)) {
            if (move >= 0 && move < tiles.size()) {
                char c = Character.toUpperCase(tiles.get(move).charAt(0));
                if (Objects.equals(c, 'R') || Objects.equals(c, 'Q')) {
                    if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                        threats.add(move);
                    }
                }
            }
        }

        for (int move : getDiagonalLU(pos)) {
            if (move >= 0 && move < tiles.size()) {
                char c = Character.toUpperCase(tiles.get(move).charAt(0));
                if (Objects.equals(c, 'B') || Objects.equals(c, 'Q')) {
                    if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                        threats.add(move);
                    }
                }
            }
        }
        for (int move : getDiagonalLD(pos)) {
            if (move >= 0 && move < tiles.size()) {
                char c = Character.toUpperCase(tiles.get(move).charAt(0));
                if (Objects.equals(c, 'B') || Objects.equals(c, 'Q')) {
                    if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                        threats.add(move);
                    }
                }
            }
        }
        for (int move : getDiagonalRD(pos)) {
            if (move >= 0 && move < tiles.size()) {
                char c = Character.toUpperCase(tiles.get(move).charAt(0));
                if (Objects.equals(c, 'B') || Objects.equals(c, 'Q')) {
                    if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                        threats.add(move);
                    }
                }
            }
        }
        for (int move : getDiagonalRU(pos)) {
            if (move >= 0 && move < tiles.size()) {
                char c = Character.toUpperCase(tiles.get(move).charAt(0));
                if (Objects.equals(c, 'B') || Objects.equals(c, 'Q')) {
                    if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                        threats.add(move);
                    }
                }
            }
        }

        int move;

        move = pos + 1;
        if (move >= 0 && move < tiles.size()) {
            if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'K')) {
                if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                    threats.add(move);
                }
            }
        }
        move = pos - 1;
        if (move >= 0 && move < tiles.size()) {
            if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'K')) {
                if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                    threats.add(move);
                }
            }
        }
        move = pos + 8;
        if (move >= 0 && move < tiles.size()) {
            if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'K')) {
                if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                    threats.add(move);
                }
            }
        }
        move = pos + 9;
        if (move >= 0 && move < tiles.size()) {
            if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'K')) {
                if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                    threats.add(move);
                }
            }
        }
        move = pos + 10;
        if (move >= 0 && move < tiles.size()) {
            if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'K')) {
                if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                    threats.add(move);
                }
            }
        }
        move = pos - 8;
        if (move >= 0 && move < tiles.size()) {
            if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'K')) {
                if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                    threats.add(move);
                }
            }
        }
        move = pos - 9;
        if (move >= 0 && move < tiles.size()) {
            if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'K')) {
                if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                    threats.add(move);
                }
            }
        }
        move = pos - 10;
        if (move >= 0 && move < tiles.size()) {
            if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'K')) {
                if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                    threats.add(move);
                }
            }
        }

        move = pos - 19;
        if (move >= 0 && move < tiles.size()) {
            if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'N')) {
                if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                    threats.add(move);
                }
            }
        }
        move = pos - 17;
        if (move >= 0 && move < tiles.size()) {
            if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'N')) {
                if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                    threats.add(move);
                }
            }
        }
        move = pos + 17;
        if (move >= 0 && move < tiles.size()) {
            if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'N')) {
                if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                    threats.add(move);
                }
            }
        }
        move = pos + 19;
        if (move >= 0 && move < tiles.size()) {
            if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'N')) {
                if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                    threats.add(move);
                }
            }
        }
        if ((pos + 1) % 9 != 1) {
            move = pos + 7;
            if (move >= 0 && move < tiles.size()) {
                if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'N')) {
                    if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                        threats.add(move);
                    }
                }
            }
            move = pos - 11;
            if (move >= 0 && move < tiles.size()) {
                if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'N')) {
                    if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                        threats.add(move);
                    }
                }
            }
        }
        if ((pos + 1) % 9 != 8) {
            move = pos - 7;
            if (move >= 0 && move < tiles.size()) {
                if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'N')) {
                    if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                        threats.add(move);
                    }
                }
            }
            move = pos + 11;
            if (move >= 0 && move < tiles.size()) {
                if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'N')) {
                    if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                        threats.add(move);
                    }
                }
            }
        }

        if (ChessContainer.color.equals("iron")) {
            move = pos - 8;
            if (move >= 0 && move < tiles.size()) {
                if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'P')) {
                    if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                        threats.add(move);
                    }
                }
            }
            move = pos - 10;
            if (move >= 0 && move < tiles.size()) {
                if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'P')) {
                    if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                        threats.add(move);
                    }
                }
            }
        } else {
            move = pos + 8;
            if (move >= 0 && move < tiles.size()) {
                if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'P')) {
                    if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                        threats.add(move);
                    }
                }
            }
            move = pos + 10;
            if (move >= 0 && move < tiles.size()) {
                if (Objects.equals(Character.toUpperCase(tiles.get(move).charAt(0)), 'P')) {
                    if (!Objects.equals(tiles.get(move).charAt(1), color)) {
                        threats.add(move);
                    }
                }
            }
        }

        GameUtils.board[kingpos] = field;
        return threats;
    }

    static List<Integer> getSingle(int pos) {
        List<Integer> tempmoves = new ArrayList<>();

        try {
            tempmoves.add(getField(pos));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        return tempmoves;
    }

    static List<Integer> getDiagonalRD(int pos) {
        List<Integer> tempmoves = new ArrayList<>();

        try {
            tempmoves.add(getFieldInRow(pos + 10));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 20));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 30));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 40));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 50));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 60));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 70));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        return tempmoves;
    }

    static List<Integer> getDiagonalLD(int pos) {
        List<Integer> tempmoves = new ArrayList<>();

        try {
            tempmoves.add(getFieldInRow(pos + 8));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 16));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 24));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 32));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 40));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 48));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 56));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        return tempmoves;
    }

    static List<Integer> getDiagonalLU(int pos) {
        List<Integer> tempmoves = new ArrayList<>();

        try {
            tempmoves.add(getFieldInRow(pos - 10));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 20));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 30));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 40));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 50));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 60));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 70));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        return tempmoves;
    }

    static List<Integer> getDiagonalRU(int pos) {
        List<Integer> tempmoves = new ArrayList<>();

        try {
            tempmoves.add(getFieldInRow(pos - 8));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 16));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 24));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 32));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 40));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 48));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 56));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        return tempmoves;
    }

    static List<Integer> getStraightL(int pos) {
        List<Integer> tempmoves = new ArrayList<>();

        try {
            tempmoves.add(getFieldInRow(pos - 1));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 2));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 3));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 4));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 5));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 6));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 7));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        return tempmoves;
    }

    static List<Integer> getStraightR(int pos) {
        List<Integer> tempmoves = new ArrayList<>();

        try {
            tempmoves.add(getFieldInRow(pos + 1));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 2));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 3));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 4));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 5));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 6));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 7));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        return tempmoves;
    }

    static List<Integer> getStraightD(int pos) {
        List<Integer> tempmoves = new ArrayList<>();

        try {
            tempmoves.add(getFieldInRow(pos + 9));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 18));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 27));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 36));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 45));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 54));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos + 63));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        return tempmoves;
    }

    static List<Integer> getStraightU(int pos) {
        List<Integer> tempmoves = new ArrayList<>();

        try {
            tempmoves.add(getFieldInRow(pos - 9));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 18));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 27));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 36));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 45));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 54));
        } catch (ArrayIndexOutOfBoundsException e) {
            return tempmoves;
        }
        try {
            tempmoves.add(getFieldInRow(pos - 63));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        return tempmoves;
    }

    static int getFieldInRow(int index) throws ArrayIndexOutOfBoundsException {
        if (!ChessContainer.end) {
            if (index >= 0 && index < GameUtils.board.length) {
                if (!Objects.equals(Arrays.asList(GameUtils.board).get(index).charAt(0), 'T')) {
                    char color;
                    if (ChessContainer.color.equals("iron")) {
                        color = 'w';
                    } else {
                        color = 'b';
                    }
                    if (!Objects.equals(Arrays.asList(GameUtils.board).get(index).charAt(1), color)) {
                        if (!Objects.equals(Arrays.asList(GameUtils.board).get(index).charAt(0), 'A')) {
                            ChessContainer.end = true;
                        }
                        return index;
                    }
                }
            }
        } else {
            ChessContainer.end = false;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    static int getField(int index) throws ArrayIndexOutOfBoundsException {
        if (index >= 0 && index < GameUtils.board.length) {
            if (!Objects.equals(Arrays.asList(GameUtils.board).get(index).charAt(0), 'T')) {
                char color;
                if (ChessContainer.color.equals("iron")) {
                    color = 'w';
                } else {
                    color = 'b';
                }
                if (!Objects.equals(Arrays.asList(GameUtils.board).get(index).charAt(1), color)) {
                    return index;
                }
            }
        }
        throw new ArrayIndexOutOfBoundsException();
    }
}

