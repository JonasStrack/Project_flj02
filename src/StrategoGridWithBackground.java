import javax.swing.*;
import java.awt.*;
import javax.swing.JFrame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class StrategoGridWithBackground extends JFrame implements GameBoard {
    private static final int GRID_SIZE = 10;
    private static final int TILE_SIZE = 60;
    private JLabel coordinateLabel;
    private JPanel[][] tiles;
    private Map<String, ImageIcon> pieceImages;
    private JPanel rightPanel;
    private DefaultLayout defaultLayout;

    public StrategoGridWithBackground() {
        setTitle("Stratego Grid with Background");
        setSize(GRID_SIZE * TILE_SIZE + 500, GRID_SIZE * TILE_SIZE + 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize the default layout
        defaultLayout = new DefaultLayout(this);

        // Initialize the rest of the UI
        initializeUI();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Board panel with background image
        JPanel boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                URL backgroundUrl = getClass().getResource("/images/Stratego-Original.jpg");
                if (backgroundUrl == null) {
                    System.err.println("Background image not found! Check the path: /images/Stratego-Original.jpg");
                    return;
                }
                System.out.println("Loading background image from: " + backgroundUrl); // Debug statement
                ImageIcon backgroundImage = new ImageIcon(backgroundUrl);
                if (backgroundImage.getImage() != null) {
                    System.out.println("Drawing background image..."); // Debug statement
                    g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.err.println("Failed to load background image!");
                }
            }
        };
        boardPanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        boardPanel.setPreferredSize(new Dimension(GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE));

        // Initialize tiles
        tiles = new JPanel[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                JPanel tile = new JPanel();
                tile.setOpaque(false);
                tile.setBorder(null);
                tile.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));

                final int finalRow = row;
                final int finalCol = col;
                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        coordinateLabel.setText("Clicked Tile: (" + finalRow + ", " + finalCol + ")");
                    }
                });

                tiles[row][col] = tile;
                boardPanel.add(tile);
            }
        }

        // Coordinate label
        coordinateLabel = new JLabel("Clicked Tile: (-, -)", SwingConstants.CENTER);
        coordinateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        coordinateLabel.setOpaque(true);
        coordinateLabel.setBackground(Color.LIGHT_GRAY);

        // Right panel for pieces
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(200, GRID_SIZE * TILE_SIZE));
        setupDragAndDrop(rightPanel);

        // Reset button
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetBoard());

        // Bottom panel for reset button
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(resetButton);

        // Add components to main panel
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
        add(coordinateLabel, BorderLayout.NORTH);

        // Initialize the default layout
        defaultLayout.initializeLayout();
    }

    private void setupDragAndDrop(JPanel rightPanel) {
        pieceImages = new HashMap<>();
        String[] pieceNames = {"Flag", "Spy", "Scout", "Miner", "Sergeant", "Lieutenant", "Captain", "Major", "Colonel", "General", "Marshal", "Bomb"};
        String[] pieceRanks = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};  // Example ranks

        for (int i = 0; i < pieceNames.length; i++) {
            String name = pieceNames[i];
            String path = "/images/" + name + ".png"; // Correct relative path

            // Try to load the image
            ImageIcon icon = scaleImage(path, TILE_SIZE, TILE_SIZE);
            if (icon != null) {
                pieceImages.put(path, icon);

                JPanel piecePanel = new JPanel();
                piecePanel.setLayout(new BoxLayout(piecePanel, BoxLayout.Y_AXIS));
                JLabel pieceIcon = new JLabel(icon);
                JLabel pieceRank = new JLabel("Rank: " + pieceRanks[i]);  // Rank next to the image

                piecePanel.add(pieceIcon);
                piecePanel.add(pieceRank);
                rightPanel.add(piecePanel);
            } else {
                System.err.println("Error loading image: " + path);
            }
        }
    }

    private ImageIcon scaleImage(String imagePath, int width, int height) {
        try {
            // Load the image from the resource path
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl == null) {
                System.err.println("Image not found: " + imagePath);
                return null;
            }
            System.out.println("Loading image from: " + imageUrl); // Debug statement
            ImageIcon icon = new ImageIcon(imageUrl);
            if (icon.getImage() != null) {
                // Scale the image to the desired size
                Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(image);
            } else {
                System.err.println("Failed to load image: " + imagePath);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + imagePath);
            e.printStackTrace();
            return null;
        }
    }

    private void resetBoard() {
        // Clear all tiles
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                tiles[row][col].removeAll();
                tiles[row][col].revalidate();
                tiles[row][col].repaint();
            }
        }

        // Reinitialize the layout
        defaultLayout.initializeLayout();
    }

    @Override
    public void placePiece(int row, int col, ImageIcon icon) {
        if (row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE) {
            // Scale the image to 60x60 pixels
            ImageIcon scaledIcon = scaleImage(icon.getDescription(), TILE_SIZE, TILE_SIZE);
            tiles[row][col].removeAll();
            tiles[row][col].add(new JLabel(scaledIcon));
            tiles[row][col].revalidate();
            tiles[row][col].repaint();
        } else {
            System.err.println("Invalid position: (" + row + ", " + col + ")");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StrategoGridWithBackground().setVisible(true));
    }
}