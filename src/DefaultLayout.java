import javax.swing.*;
import java.awt.Point;
import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DefaultLayout {

    private static final int TILE_SIZE = 60; // Define TILE_SIZE
    private Map<Point, String> layout;
    private GameBoard gameBoard;

    public DefaultLayout(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        layout = new HashMap<>();
    }

    public void initializeLayout() {
        // Define the initial layout of pieces
        layout.put(new Point(0, 4), "/images/flag.png"); // Flag
        layout.put(new Point(0, 3), "/images/bomb.png"); // Bomb
        layout.put(new Point(0, 5), "/images/bomb.png"); // Bomb
        layout.put(new Point(1, 4), "/images/miner.png"); // Miner
        layout.put(new Point(1, 3), "/images/miner.png"); // Miner
        layout.put(new Point(1, 5), "/images/miner.png"); // Miner
        layout.put(new Point(1, 2), "/images/spy.png"); // Spy
        layout.put(new Point(1, 6), "/images/scout.png"); // Scout
        layout.put(new Point(1, 1), "/images/scout.png"); // Scout
        layout.put(new Point(1, 7), "/images/scout.png"); // Scout
        layout.put(new Point(1, 0), "/images/sergeant.png"); // Sergeant
        layout.put(new Point(1, 8), "/images/sergeant.png"); // Sergeant
        layout.put(new Point(1, 9), "/images/sergeant.png"); // Sergeant
        layout.put(new Point(2, 4), "/images/captain.png"); // Captain
        layout.put(new Point(2, 3), "/images/captain.png"); // Captain
        layout.put(new Point(2, 5), "/images/captain.png"); // Captain
        layout.put(new Point(2, 2), "/images/lieutenant.png"); // Lieutenant
        layout.put(new Point(2, 6), "/images/lieutenant.png"); // Lieutenant
        layout.put(new Point(2, 1), "/images/major.png"); // Major
        layout.put(new Point(2, 7), "/images/major.png"); // Major
        layout.put(new Point(2, 0), "/images/colonel.png"); // Colonel
        layout.put(new Point(2, 8), "/images/colonel.png"); // Colonel
        layout.put(new Point(3, 4), "/images/general.png"); // General
        layout.put(new Point(3, 3), "/images/marshal.png"); // Marshal
        layout.put(new Point(3, 5), "/images/scout.png"); // Scout
        layout.put(new Point(3, 2), "/images/scout.png"); // Scout
        layout.put(new Point(3, 6), "/images/scout.png"); // Scout
        layout.put(new Point(3, 1), "/images/miner.png"); // Miner
        layout.put(new Point(3, 7), "/images/miner.png"); // Miner
        layout.put(new Point(3, 0), "/images/sergeant.png"); // Sergeant
        layout.put(new Point(3, 8), "/images/sergeant.png"); // Sergeant
        layout.put(new Point(0, 0), "/images/scout.png"); // Scout
        layout.put(new Point(0, 9), "/images/scout.png"); // Scout
        layout.put(new Point(1, 9), "/images/scout.png"); // Scout
        layout.put(new Point(2, 9), "/images/scout.png"); // Scout
        layout.put(new Point(3, 9), "/images/scout.png"); // Scout

        // Place pieces on the board
        for (Map.Entry<Point, String> entry : layout.entrySet()) {
            Point position = entry.getKey();
            String path = entry.getValue();
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            if (icon.getImage() == null) {
                System.err.println("Error loading image: " + path);
            } else {
                // Scale the image to 60x60 pixels
                ImageIcon scaledIcon = scaleImage(path, TILE_SIZE, TILE_SIZE);
                gameBoard.placePiece(position.x, position.y, scaledIcon);
            }
        }
    }

    private ImageIcon scaleImage(String imagePath, int width, int height) {
        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl == null) {
                System.err.println("Image not found: " + imagePath);
                return null;
            }
            ImageIcon icon = new ImageIcon(imageUrl);
            Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        } catch (Exception e) {
            System.err.println("Error loading image: " + imagePath);
            e.printStackTrace();
            return null;
        }
    }
}