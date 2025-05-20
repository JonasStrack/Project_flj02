import java.util.*;

public class AIHelper {

    public static class Move {
        public final int fromRow, fromCol, toRow, toCol;
        public Move(int fr, int fc, int tr, int tc) {
            fromRow = fr; fromCol = fc; toRow = tr; toCol = tc;
        }
    }

    // Main entry: pick the best move for aiPlayer
    public static Move pickBestMove(StrategoModel model, int aiPlayer) {
        List<Move> moves = generateAllLegalMoves(model, aiPlayer);
        if (moves.isEmpty()) return null;

        double bestScore = Double.NEGATIVE_INFINITY;
        List<Move> bestMoves = new ArrayList<>();

        for (Move move : moves) {
            // Clone the board and apply the move
            StrategoModel clone = model.deepCopy();
            clone.movePiece(move.fromRow, move.fromCol, move.toRow, move.toCol, aiPlayer);
            double score = evaluateBoard(clone, aiPlayer, move);

            if (score > bestScore) {
                bestScore = score;
                bestMoves.clear();
                bestMoves.add(move);
            } else if (score == bestScore) {
                bestMoves.add(move);
            }
        }
        // Randomize among equally good moves
        return bestMoves.get(new Random().nextInt(bestMoves.size()));
    }

    // Evaluate the board for the AI (higher is better for AI)
    private static double evaluateBoard(StrategoModel model, int aiPlayer, Move move) {
        int opponent = (aiPlayer == 1) ? 2 : 1;
        double score = 0;

        // 1. Material: Sum up value of AI pieces minus opponent's
        score += getMaterialScore(model, aiPlayer) - getMaterialScore(model, opponent);

        // 2. Bonus for capturing a piece
        String captured = model.getLastCaptured();
        if (captured != null && captured.endsWith("" + opponent)) {
            score += getPieceValue(captured);
        }

        // 3. Penalty for moving next to a bomb (if known)
        if (isNextToBomb(model, move.toRow, move.toCol, opponent)) {
            score -= 8;
        }

        // 4. Bonus for threatening opponent flag (if revealed)
        if (isNextToFlag(model, move.toRow, move.toCol, opponent)) {
            score += 50;
        }

        // 5. Encourage moving scouts to open spaces
        String movedPiece = model.getPiece(move.toRow, move.toCol);
        if (movedPiece != null && movedPiece.startsWith("2")) {
            score += 0.4 * countEmptyAdjacent(model, move.toRow, move.toCol);
        }

        // 6. Discourage moving own flag/bombs (illegal in real rules, but just in case)
        if (movedPiece != null && (movedPiece.startsWith("B") || movedPiece.startsWith("F"))) {
            score -= 20;
        }

        return score;
    }

    // Material score (only for visible pieces)
    private static int getMaterialScore(StrategoModel model, int player) {
        int score = 0;
        for (int r = 0; r < BoardPanel.GRID_SIZE; r++) {
            for (int c = 0; c < BoardPanel.GRID_SIZE; c++) {
                String piece = model.getPiece(r, c);
                if (piece != null && piece.endsWith("" + player)) {
                    score += getPieceValue(piece);
                }
            }
        }
        return score;
    }

    // Assign a value to each piece type (tweak as needed)
    private static int getPieceValue(String piece) {
        if (piece == null) return 0;
        String k = piece.replaceAll("[12]", "");
        return switch (k) {
            case "10" -> 12; // Marshal
            case "9" -> 10;
            case "8" -> 8;
            case "7" -> 6;
            case "6" -> 5;
            case "5" -> 4;
            case "4" -> 3;
            case "3" -> 2;
            case "2" -> 1;
            case "S" -> 2;  // Spy
            case "B" -> 3;  // Bomb
            case "F" -> 1000; // Flag, super high!
            default -> 0;
        };
    }

    private static boolean isNextToBomb(StrategoModel model, int row, int col, int opponent) {
        for (int[] d : new int[][]{{1,0},{-1,0},{0,1},{0,-1}}) {
            int nr = row + d[0], nc = col + d[1];
            if (nr >= 0 && nr < StrategoModel.SIZE && nc >= 0 && nc < StrategoModel.SIZE) {
                String piece = model.getPiece(nr, nc);
                if (piece != null && piece.equals("bomb" + opponent)) return true;
            }
        }
        return false;
    }

    private static boolean isNextToFlag(StrategoModel model, int row, int col, int opponent) {
        for (int[] d : new int[][]{{1,0},{-1,0},{0,1},{0,-1}}) {
            int nr = row + d[0], nc = col + d[1];
            if (nr >= 0 && nr < StrategoModel.SIZE && nc >= 0 && nc < StrategoModel.SIZE) {
                String piece = model.getPiece(nr, nc);
                if (piece != null && piece.equals("flag" + opponent)) return true;
            }
        }
        return false;
    }

    private static int countEmptyAdjacent(StrategoModel model, int row, int col) {
        int count = 0;
        for (int[] d : new int[][]{{1,0},{-1,0},{0,1},{0,-1}}) {
            int nr = row + d[0], nc = col + d[1];
            if (nr >= 0 && nr < StrategoModel.SIZE && nc >= 0 && nc < StrategoModel.SIZE) {
                String piece = model.getPiece(nr, nc);
                if (piece == null) count++;
            }
        }
        return count;
    }

    // Generate all legal moves for given player (AI)
    public static List<Move> generateAllLegalMoves(StrategoModel model, int aiPlayer) {
        List<Move> moves = new ArrayList<>();
        for (int r = 0; r < BoardPanel.GRID_SIZE; r++) {
            for (int c = 0; c < BoardPanel.GRID_SIZE; c++) {
                String piece = model.getPiece(r, c);
                if (piece != null && piece.endsWith("" + aiPlayer)) {
                    // Try each cardinal direction
                    int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};
                    for (int[] d : dirs) {
                        int nr = r + d[0], nc = c + d[1];
                        if (nr >= 0 && nr < BoardPanel.GRID_SIZE && nc >= 0 && nc < BoardPanel.GRID_SIZE) {
                            if (model.isLegalMove(r, c, nr, nc, aiPlayer))
                                moves.add(new Move(r, c, nr, nc));
                        }
                    }
                }
            }
        }
        return moves;
    }
}