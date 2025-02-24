import javax.swing.*;
import java.awt.Point;
import java.awt.Image;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class DefaultLayout {

    private static final int TILE_SIZE = 60;
    private Map<Point, String> layout;
    private GameBoardInterface gameBoard;
    private String layoutFilePath;

    public DefaultLayout(GameBoardInterface gameBoard, String layoutFilePath) {
        this.gameBoard = gameBoard;
        this.layoutFilePath = layoutFilePath;
        layout = new HashMap<>();
    }

    public void initializeLayout() {
        loadLayoutFromFile(layoutFilePath);

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

    private void loadLayoutFromFile(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    int x = Integer.parseInt(parts[0].trim());
                    int y = Integer.parseInt(parts[1].trim());
                    String imagePath = parts[2].trim();
                    layout.put(new Point(x, y), imagePath);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading layout file: " + filePath);
            e.printStackTrace();
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