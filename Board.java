import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Board extends GridPane {
    int size = 10;
    Piece[][] board = new Piece[size][size];
    Rectangle[][] rectangles = new Rectangle[size][size];
    int[][] impassableSquares = {
        {4, 2}, {4, 3}, {5, 2}, {5, 3},
        {4, 6}, {4, 7}, {5, 6}, {5, 7}
    };
    Piece selectedPiece = null;
    boolean isPlayerOneTurn = true;

    public Board() {
        setPrefSize(600, 600);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Rectangle rectangle = new Rectangle(60, 60);
                rectangle.setFill(isImpassableSquare(x, y) ? Color.BLUE : Color.WHITE);
                rectangle.setStroke(Color.BLACK);
                rectangle.setOnMouseClicked(new ButtonListener(this, x, y));
                rectangles[x][y] = rectangle;
                add(rectangle, x, y);
            }
        }
        DefaultLayout.setupDefaultLayout(this);
        refreshBoard();
    }

    private boolean isImpassableSquare(int x, int y) {
        for (int[] square : impassableSquares) {
            if (square[0] == x && square[1] == y) {
                return true;
            }
        }
        return false;
    }

    public void placePiece(Piece piece, int x, int y) {
        board[x][y] = piece;
    }

    public void movePiece(int startX, int startY, int endX, int endY) {
        Piece piece = board[startX][startY];
        board[endX][endY] = piece;
        board[startX][startY] = null;
        piece.x = endX;
        piece.y = endY;
        refreshBoard();
    }

    public boolean isValidMove(Piece piece, int endX, int endY) {
        if (isImpassableSquare(endX, endY)) {
            return false;
        }
        int dx = Math.abs(endX - piece.x);
        int dy = Math.abs(endY - piece.y);

        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
    }

    public void refreshBoard() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Piece piece = board[x][y];
                rectangles[x][y].setFill(piece != null ? Color.GREEN : (isImpassableSquare(x, y) ? Color.BLUE : Color.WHITE));
            }
        }
    }

    public void clearHighlights() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                rectangles[x][y].setFill(isImpassableSquare(x, y) ? Color.BLUE : Color.WHITE);
            }
        }
    }
}