import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ImageLoaderTest extends JFrame {

    public ImageLoaderTest() {
        setTitle("Image Loader Test");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);

        // Load and display the image
        String imagePath = "/res/miner.png";
        ImageIcon icon = loadImage(imagePath);

        if (icon != null) {
            JLabel label = new JLabel(icon);
            panel.add(label);
        } else {
            System.err.println("Failed to load image: " + imagePath);
        }
    }

    private ImageIcon loadImage(String path) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                BufferedImage image = ImageIO.read(imgURL);
                return new ImageIcon(image);
            } else {
                System.err.println("Resource not found: " + path);
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error loading image: " + path);
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ImageLoaderTest frame = new ImageLoaderTest();
            frame.setVisible(true);
        });
    }
}