public class DefaultLayout {
    public static void setupDefaultLayout(Board board) {
        // Player 1 Layout (Bottom 4 rows)
        board.placePiece(new Piece("Flag", 0, false, true, 9, 0), 9, 0);
        board.placePiece(new Piece("Bomb", 0, false, true, 9, 1), 9, 1);
        board.placePiece(new Piece("Bomb", 0, false, true, 9, 2), 9, 2);
        board.placePiece(new Piece("Bomb", 0, false, true, 9, 3), 9, 3);
        board.placePiece(new Piece("Bomb", 0, false, true, 9, 4), 9, 4);
        board.placePiece(new Piece("Bomb", 0, false, true, 9, 5), 9, 5);
        board.placePiece(new Piece("Bomb", 0, false, true, 9, 6), 9, 6);
        board.placePiece(new Piece("Marshal", 10, true, true, 9, 7), 9, 7);
        board.placePiece(new Piece("General", 9, true, true, 9, 8), 9, 8);
        board.placePiece(new Piece("Colonel", 8, true, true, 9, 9), 9, 9);

        board.placePiece(new Piece("Colonel", 8, true, true, 8, 0), 8, 0);
        board.placePiece(new Piece("Major", 7, true, true, 8, 1), 8, 1);
        board.placePiece(new Piece("Major", 7, true, true, 8, 2), 8, 2);
        board.placePiece(new Piece("Major", 7, true, true, 8, 3), 8, 3);
        board.placePiece(new Piece("Captain", 6, true, true, 8, 4), 8, 4);
        board.placePiece(new Piece("Captain", 6, true, true, 8, 5), 8, 5);
        board.placePiece(new Piece("Captain", 6, true, true, 8, 6), 8, 6);
        board.placePiece(new Piece("Lieutenant", 5, true, true, 8, 7), 8, 7);
        board.placePiece(new Piece("Lieutenant", 5, true, true, 8, 8), 8, 8);
        board.placePiece(new Piece("Lieutenant", 5, true, true, 8, 9), 8, 9);

        board.placePiece(new Piece("Lieutenant", 5, true, true, 7, 0), 7, 0);
        board.placePiece(new Piece("Sergeant", 4, true, true, 7, 1), 7, 1);
        board.placePiece(new Piece("Sergeant", 4, true, true, 7, 2), 7, 2);
        board.placePiece(new Piece("Sergeant", 4, true, true, 7, 3), 7, 3);
        board.placePiece(new Piece("Sergeant", 4, true, true, 7, 4), 7, 4);
        board.placePiece(new Piece("Sergeant", 4, true, true, 7, 5), 7, 5);
        board.placePiece(new Piece("Sergeant", 4, true, true, 7, 6), 7, 6);
        board.placePiece(new Piece("Miner", 3, true, true, 7, 7), 7, 7);
        board.placePiece(new Piece("Miner", 3, true, true, 7, 8), 7, 8);
        board.placePiece(new Piece("Miner", 3, true, true, 7, 9), 7, 9);

        board.placePiece(new Piece("Miner", 3, true, true, 6, 0), 6, 0);
        board.placePiece(new Piece("Spy", 2, true, true, 6, 1), 6, 1);
        board.placePiece(new Piece("Scout", 1, true, true, 6, 2), 6, 2);
        board.placePiece(new Piece("Scout", 1, true, true, 6, 3), 6, 3);
        board.placePiece(new Piece("Scout", 1, true, true, 6, 4), 6, 4);
        board.placePiece(new Piece("Scout", 1, true, true, 6, 5), 6, 5);
        board.placePiece(new Piece("Scout", 1, true, true, 6, 6), 6, 6);
        board.placePiece(new Piece("Scout", 1, true, true, 6, 7), 6, 7);
        board.placePiece(new Piece("Scout", 1, true, true, 6, 8), 6, 8);
        board.placePiece(new Piece("Scout", 1, true, true, 6, 9), 6, 9);

        // Player 2 Layout (Top 4 rows)
        board.placePiece(new Piece("Flag", 0, false, false, 0, 0), 0, 0);
        board.placePiece(new Piece("Bomb", 0, false, false, 0, 1), 0, 1);
        board.placePiece(new Piece("Bomb", 0, false, false, 0, 2), 0, 2);
        board.placePiece(new Piece("Bomb", 0, false, false, 0, 3), 0, 3);
        board.placePiece(new Piece("Bomb", 0, false, false, 0, 4), 0, 4);
        board.placePiece(new Piece("Bomb", 0, false, false, 0, 5), 0, 5);
        board.placePiece(new Piece("Bomb", 0, false, false, 0, 6), 0, 6);
        board.placePiece(new Piece("Marshal", 10, true, false, 0, 7), 0, 7);
        board.placePiece(new Piece("General", 9, true, false, 0, 8), 0, 8);
        board.placePiece(new Piece("Colonel", 8, true, false, 0, 9), 0, 9);

        board.placePiece(new Piece("Colonel", 8, true, false, 1, 0), 1, 0);
        board.placePiece(new Piece("Major", 7, true, false, 1, 1), 1, 1);
        board.placePiece(new Piece("Major", 7, true, false, 1, 2), 1, 2);
        board.placePiece(new Piece("Major", 7, true, false, 1, 3), 1, 3);
        board.placePiece(new Piece("Captain", 6, true, false, 1, 4), 1, 4);
        board.placePiece(new Piece("Captain", 6, true, false, 1, 5), 1, 5);
        board.placePiece(new Piece("Captain", 6, true, false, 1, 6), 1, 6);
        board.placePiece(new Piece("Lieutenant", 5, true, false, 1, 7), 1, 7);
        board.placePiece(new Piece("Lieutenant", 5, true, false, 1, 8), 1, 8);
        board.placePiece(new Piece("Lieutenant", 5, true, false, 1, 9), 1, 9);

        board.placePiece(new Piece("Lieutenant", 5, true, false, 2, 0), 2, 0);
        board.placePiece(new Piece("Sergeant", 4, true, false, 2, 1), 2, 1);
        board.placePiece(new Piece("Sergeant", 4, true, false, 2, 2), 2, 2);
        board.placePiece(new Piece("Sergeant", 4, true, false, 2, 3), 2, 3);
        board.placePiece(new Piece("Sergeant", 4, true, false, 2, 4), 2, 4);
        board.placePiece(new Piece("Sergeant", 4, true, false, 2, 5), 2, 5);
        board.placePiece(new Piece("Sergeant", 4, true, false, 2, 6), 2, 6);
        board.placePiece(new Piece("Miner", 3, true, false, 2, 7), 2, 7);
        board.placePiece(new Piece("Miner", 3, true, false, 2, 8), 2, 8);
        board.placePiece(new Piece("Miner", 3, true, false, 2, 9), 2, 9);

        board.placePiece(new Piece("Miner", 3, true, false, 3, 0), 3, 0);
        board.placePiece(new Piece("Spy", 2, true, false, 3, 1), 3, 1);
        board.placePiece(new Piece("Scout", 1, true, false, 3, 2), 3, 2);
        board.placePiece(new Piece("Scout", 1, true, false, 3, 3), 3, 3);
        board.placePiece(new Piece("Scout", 1, true, false, 3, 4), 3, 4);
        board.placePiece(new Piece("Scout", 1, true, false, 3, 5), 3, 5);
        board.placePiece(new Piece("Scout", 1, true, false, 3, 6), 3, 6);
        board.placePiece(new Piece("Scout", 1, true, false, 3, 7), 3, 7);
        board.placePiece(new Piece("Scout", 1, true, false, 3, 8), 3, 8);
        board.placePiece(new Piece("Scout", 1, true, false, 3, 9), 3, 9);
    }
}