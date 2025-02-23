import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class GameBoard extends JPanel implements GameBoardInterface {

    private static final int GRID_SIZE = 10;
    private static final int TILE_SIZE = 60;
    private JPanel[][] tiles;
    private DefaultLayout defaultLayout;

    public GameBoard() {
        setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        setPreferredSize(new Dimension(GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE));

        // Initialize tiles
        tiles = new JPanel[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                JPanel tile = new JPanel();
                tile.setOpaque(false); // Make the tile transparent
                tile.setBorder(null); // Remove borders
                tile.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
                tiles[row][col] = tile;
                add(tile);
            }
        }

        // Load default layout
        defaultLayout = new DefaultLayout(this);
        defaultLayout.initializeLayout();
    }

    public JPanel[][] getTiles() {
        return tiles;
    }

    public ImageIcon scaleImage(String imagePath, int width, int height) {
        try {
            if (imagePath == null) {
                System.err.println("Image path is null");
                return null;
            }
            System.out.println("Loading image from: " + getClass().getResource(imagePath));
            BufferedImage image = ImageIO.read(getClass().getResource(imagePath));
            if (image == null) {
                System.err.println("Image not found: " + imagePath);
                return null;
            }
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            System.err.println("Error loading image: " + imagePath);
            e.printStackTrace();
            return null;
        }
    }

    public void placePiece(int row, int col, ImageIcon icon) {
        if (row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE) {
            if (icon != null) {
                ImageIcon scaledIcon = scaleImage(icon.getDescription(), TILE_SIZE, TILE_SIZE);
                if (scaledIcon != null) {
                    tiles[row][col].removeAll();
                    tiles[row][col].add(new JLabel(scaledIcon));
                    tiles[row][col].revalidate();
                    tiles[row][col].repaint();
                } else {
                    System.err.println("Failed to place piece at (" + row + ", " + col + "): scaled image is null");
                }
            } else {
                System.err.println("Failed to place piece at (" + row + ", " + col + "): image icon is null");
            }
        } else {
            System.err.println("Invalid position: (" + row + ", " + col + ")");
        }
    }

    public void setPieceAt(int x, int y, Piece piece) {
        if (x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE) {
            String imagePath = piece.getImagePath();
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            placePiece(x, y, icon);
        } else {
            System.err.println("Invalid position: (" + x + ", " + y + ")");
        }
    }

    public void removePieceAt(int x, int y) {
        if (x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE) {
            tiles[x][y].removeAll();
            tiles[x][y].revalidate();
            tiles[x][y].repaint();
        } else {
            System.err.println("Invalid position: (" + x + ", " + y + ")");
        }
    }

    public Piece getPieceAtTile(JPanel tile) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (tiles[row][col] == tile) {
                    // Return the piece at this tile (you need to store pieces in a data structure)
                    return null; // Placeholder
                }
            }
        }
        return null;
    }

    public int getTileRow(JPanel tile) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (tiles[row][col] == tile) {
                    return row;
                }
            }
        }
        return -1;
    }

    public int getTileCol(JPanel tile) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (tiles[row][col] == tile) {
                    return col;
                }
            }
        }
        return -1;
    }

    public void refreshBoard() {
        revalidate();
        repaint();
    }
}