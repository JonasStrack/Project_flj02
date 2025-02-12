import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonListener extends MouseAdapter {
    private final Board board;
    private final int x;
    private final int y;

    public ButtonListener(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (board.selectedPiece == null) {
            // Select piece
            Piece piece = board.board[x][y];
            if (piece != null && piece.isPlayerOne == board.isPlayerOneTurn) {
                board.selectedPiece = piece;
                board.clearHighlights();
                board.rectangles[x][y].setBackground(Color.YELLOW); // Highlight selected piece
            }
        } else {
            // Move piece or attack
            if (board.isValidMove(board.selectedPiece, x, y)) {
                if (board.board[x][y] != null) {
                    board.resolveCombat(board.selectedPiece, board.board[x][y], x, y);
                } else {
                    board.movePiece(board.selectedPiece.x, board.selectedPiece.y, x, y);
                }
                board.isPlayerOneTurn = !board.isPlayerOneTurn; // Switch turn after move
            }
            board.selectedPiece = null; // Deselect piece
            board.clearHighlights(); // Clear all highlights
            board.refreshBoard(); // Refresh the board to clear highlights
        }
    }
}
