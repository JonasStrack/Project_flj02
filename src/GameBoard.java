import javax.swing.*;
import java.awt.*;

public class GameBoard extends JFrame implements GameBoardInterface {

    private static final int GRID_SIZE = 10;
    private static final int TILE_SIZE = 60;
    private StrategoTile[][] tiles;

    public GameBoard() {
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        boardPanel.setPreferredSize(new Dimension(GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE));

        // Initialize tiles
        tiles = new StrategoTile[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                tiles[row][col] = new StrategoTile();
                boardPanel.add(tiles[row][col]);
            }
        }

        add(boardPanel, BorderLayout.CENTER);
    }

    @Override
    public StrategoTile[][] getTiles() {
        return tiles;
    }

    @Override
    public Piece getPieceAtTile(StrategoTile tile) {
        int row = getTileRow(tile);
        int col = getTileCol(tile);
        // This method should return the piece at the given tile
        // Implement your logic to return the correct piece
        return null;
    }

    @Override
    public int getTileRow(StrategoTile tile) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (tiles[row][col] == tile) {
                    return row;
                }
            }
        }
        return -1;  // Tile not found
    }

    @Override
    public int getTileCol(StrategoTile tile) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (tiles[row][col] == tile) {
                    return col;
                }
            }
        }
        return -1;  // Tile not found
    }

    @Override
    public void placePiece(int row, int col, ImageIcon icon) {
        if (row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE) {
            tiles[row][col].setFigure(icon);
        } else {
            System.err.println("Invalid position: (" + row + ", " + col + ")");
        }
    }

    @Override
    public void refreshBoard() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                tiles[row][col].revalidate();
                tiles[row][col].repaint();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameBoard frame = new GameBoard();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(new Dimension(GRID_SIZE * TILE_SIZE + 100, GRID_SIZE * TILE_SIZE + 100));
            frame.setVisible(true);
        });
    }
}