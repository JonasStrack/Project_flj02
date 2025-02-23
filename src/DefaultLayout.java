import javax.swing.*;
import java.awt.Point;
import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class DefaultLayout {

    private static final int TILE_SIZE = 60;
    private Map<Point, String> layout;
    private GameBoardInterface gameBoard;

    public DefaultLayout(GameBoardInterface gameBoard) {
        this.gameBoard = gameBoard;
        layout = new HashMap<>();
    }

    public void initializeLayout() {
        layout.put(new Point(1, 1), "/res/scout.png"); // Scout
        layout.put(new Point(1, 2), "/res/scout.png"); // Scout
        layout.put(new Point(1, 3), "/res/scout.png"); // Scout
        layout.put(new Point(1, 4), "/res/bomb.png"); // Bomb
        layout.put(new Point(1, 5), "/res/flag.png"); // Flag
        layout.put(new Point(1, 6), "/res/bomb.png"); // Bomb
        layout.put(new Point(1, 7), "/res/scout.png"); // Scout
        layout.put(new Point(1, 8), "/res/scout.png"); // Scout
        layout.put(new Point(1, 9), "/res/scout.png"); // Scout
        layout.put(new Point(1, 10), "/res/scout.png"); // Scout
        layout.put(new Point(2, 1), "/res/sergeant.png"); // Sergeant
        layout.put(new Point(2, 2), "/res/scout.png"); // Scout
        layout.put(new Point(2, 3), "/res/spy.png"); // Spy
        layout.put(new Point(2, 4), "/res/miner.png"); // Miner
        layout.put(new Point(2, 5), "/res/miner.png"); // Miner
        layout.put(new Point(2, 6), "/res/miner.png"); // Miner
        layout.put(new Point(2, 7), "/res/scout.png"); // Scout
        layout.put(new Point(2, 8), "/res/scout.png"); // Scout
        layout.put(new Point(2, 9), "/res/sergeant.png"); // Sergeant
        layout.put(new Point(2, 10), "/res/scout.png"); // Scout
        layout.put(new Point(3, 1), "/res/colonel.png"); // Colonel
        layout.put(new Point(3, 2), "/res/major.png"); // Major
        layout.put(new Point(3, 3), "/res/lieutenant.png"); // Lieutenant
        layout.put(new Point(3, 4), "/res/captain.png"); // Captain
        layout.put(new Point(3, 5), "/res/captain.png"); // Captain
        layout.put(new Point(3, 6), "/res/captain.png"); // Captain
        layout.put(new Point(3, 7), "/res/lieutenant.png"); // Lieutenant
        layout.put(new Point(3, 8), "/res/major.png"); // Major
        layout.put(new Point(3, 9), "/res/colonel.png"); // Colonel
        layout.put(new Point(3, 10), "/res/scout.png"); // Scout
        layout.put(new Point(4, 1), "/res/sergeant.png"); // Sergeant
        layout.put(new Point(4, 2), "/res/miner.png"); // Miner
        layout.put(new Point(4, 3), "/res/scout.png"); // Scout
        layout.put(new Point(4, 4), "/res/marshal.png"); // Marshal
        layout.put(new Point(4, 5), "/res/general.png"); // General
        layout.put(new Point(4, 6), "/res/scout.png"); // Scout
        layout.put(new Point(4, 7), "/res/scout.png"); // Scout
        layout.put(new Point(4, 8), "/res/miner.png"); // Miner
        layout.put(new Point(4, 9), "/res/sergeant.png"); // Sergeant
        layout.put(new Point(4, 10), "/res/scout.png"); // Scout

        for (Map.Entry<Point, String> entry : layout.entrySet()) {
            Point position = entry.getKey();
            String path = entry.getValue();
            java.net.URL resource = getClass().getResource(path);
            if (resource == null) {
                System.err.println("Resource not found: " + path);
                continue;
            }
            ImageIcon icon = new ImageIcon(resource);
            if (icon.getImage() == null) {
                System.err.println("Error loading image: " + path);
            } else {
                ImageIcon scaledIcon = scaleImage(resource, TILE_SIZE, TILE_SIZE);
                if (scaledIcon != null) {
                    gameBoard.placePiece(position.x, position.y, scaledIcon);
                } else {
                    System.err.println("Failed to place piece at (" + position.x + ", " + position.y + "): scaled image is null");
                }
            }
        }
    }

    private ImageIcon scaleImage(java.net.URL resource, int width, int height) {
        try {
            BufferedImage image = ImageIO.read(resource);
            if (image == null) {
                System.err.println("Image not found: " + resource);
                return null;
            }
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            System.err.println("Error loading image: " + resource);
            e.printStackTrace();
            return null;
        }
    }
}