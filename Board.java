import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JPanel {
    int size = 10;
    Piece[][] board = new Piece[size][size];
    JPanel[][] panels = new JPanel[size][size];
    int[][] impassableSquares = {
        {4, 2}, {4, 3}, {5, 2}, {5, 3},
        {4, 6}, {4, 7}, {5, 6}, {5, 7}
    };
    Piece selectedPiece = null;
    boolean isPlayerOneTurn = true;

    public Board() {
        setLayout(new GridLayout(size, size));  // Use GridLayout for grid-like structure
        setPreferredSize(new Dimension(600, 600));

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                JPanel panel = new JPanel();
                panel.setBackground(isImpassableSquare(x, y) ? Color.BLUE : Color.WHITE);
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                // Add a mouse click listener
                panel.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        new ButtonListener(Board.this, x, y).actionPerformed(new ActionEvent(evt.getSource(), ActionEvent.ACTION_PERFORMED, null));
                    }
                });

                panels[x][y] = panel;
                add(panel);  // Add the panel to the layout
            }
        }

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
                panels[x][y].setBackground(piece != null ? Color.GREEN : (isImpassableSquare(x, y) ? Color.BLUE : Color.WHITE));
            }
        }
    }

    public void clearHighlights() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                panels[x][y].setBackground(isImpassableSquare(x, y) ? Color.BLUE : Color.WHITE);
            }
        }
    }
}
