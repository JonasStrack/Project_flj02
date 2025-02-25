import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class StrategoGridWithBackground extends JFrame implements GameBoardInterface {

    private static final int GRID_SIZE = 10;
    private static final int TILE_SIZE = 60;
    private static final int BOARD_IMAGE_SIZE = 50; // Smaller size for images on the board
    private static final int RANK_IMAGE_SIZE = 40; // Larger size for images on the side
    private static final int EXTRA_WIDTH = 350; // Extra width for the window
    private static final int EXTRA_HEIGHT = 150; // Extra height for the window
    private StrategoTile[][] tiles;
    private DefaultLayout defaultLayout;
    private DefaultLayout[] defaultLayouts; // Array to hold multiple default layouts
    private JPanel ranksPanel;
    private ImageIcon selectedFigure = null;
    private Map<String, Integer> figureCounts;
    private Map<String, JLabel> countLabels;
    private boolean isPlayerOneTurn = true; // Track turn
    private boolean gameStarted = false; // Track if the game has started
    private JLabel statusBar; // Status bar at the bottom
    private MovementHandler movementHandler;

    public StrategoGridWithBackground() {
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image backgroundImage = new ImageIcon(getClass().getResource("/res/Stratego-Original.jpg")).getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        boardPanel.setPreferredSize(new Dimension(GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE));

        // Initialize tiles
        tiles = new StrategoTile[GRID_SIZE][GRID_SIZE];
        for (int row = 1; row <= GRID_SIZE; row++) {
            for (int col = 1; col <= GRID_SIZE; col++) {
                StrategoTile tile = new StrategoTile();
                tile.addMouseListener(new TileClickListener(row, col));
                tiles[row - 1][col - 1] = tile;
                boardPanel.add(tile);
            }
        }

        add(boardPanel, BorderLayout.CENTER);

        // Initialize default layouts
        initializeDefaultLayouts();

        // Load default layout
        defaultLayout = defaultLayouts[0]; // Load the first default layout by default
        defaultLayout.initializeLayout();

        // Initialize ranks panel
        initializeRanksPanel();

        // Initialize UI
        initializeUI();

        // Initialize movement handler
        movementHandler = new MovementHandler(this);

        // Initialize status bar
        statusBar = new JLabel("Welcome to Stratego!");
        if (statusBar == null) {
            System.err.println("Constructor: statusBar is null after initialization");
        } else {
            add(statusBar, BorderLayout.SOUTH);
        }
    }

    private void initializeRanksPanel() {
        ranksPanel = new JPanel(new GridBagLayout());
        ranksPanel.setPreferredSize(new Dimension(200, GRID_SIZE * TILE_SIZE));
        ranksPanel.setBackground(new Color(60, 63, 65));

        String[] ranks = {"Marshal", "General", "Colonel", "Major", "Captain", "Lieutenant", "Sergeant", "Miner", "Scout", "Spy", "Bomb", "Flag"};
        String[] imagePaths = {"/res/marshal.png", "/res/general.png", "/res/colonel.png", "/res/major.png",
                "/res/captain.png", "/res/lieutenant.png", "/res/sergeant.png", "/res/miner.png",
                "/res/scout.png", "/res/spy.png", "/res/bomb.png", "/res/flag.png"};
        int[] rankCounts = {1, 1, 2, 3, 4, 4, 4, 5, 8, 1, 6, 1}; // Example counts, adjust as needed

        figureCounts = new HashMap<>();
        countLabels = new HashMap<>();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < ranks.length; i++) {
            figureCounts.put(imagePaths[i], rankCounts[i]);

            JPanel rankPanel = new JPanel(new BorderLayout());
            rankPanel.setBackground(new Color(43, 43, 43));
            rankPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

            JLabel rankLabel = new JLabel(ranks[i]);
            rankLabel.setForeground(Color.WHITE);
            rankLabel.setHorizontalAlignment(SwingConstants.CENTER);

            ImageIcon rankIcon = new ImageIcon(getClass().getResource(imagePaths[i]));
            rankIcon.setDescription(i + "_" + ranks[i]); // Set description with index and rank
            JLabel rankImageLabel = new JLabel(scaleImage(rankIcon, RANK_IMAGE_SIZE, RANK_IMAGE_SIZE));
            rankImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            rankImageLabel.addMouseListener(new RankClickListener(imagePaths[i]));

            JLabel countLabel = new JLabel(String.valueOf(rankCounts[i]));
            countLabel.setForeground(Color.WHITE);
            countLabel.setHorizontalAlignment(SwingConstants.CENTER);

            countLabels.put(imagePaths[i], countLabel);

            rankPanel.add(rankImageLabel, BorderLayout.WEST);
            rankPanel.add(rankLabel, BorderLayout.CENTER);
            rankPanel.add(countLabel, BorderLayout.EAST);

            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 1;
            ranksPanel.add(rankPanel, gbc);
        }

        add(ranksPanel, BorderLayout.EAST);
    }

    private void initializeUI() {
        System.out.println("initializeUI: Starting initialization");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> showResetOptions());
        buttonPanel.add(resetButton);
        System.out.println("initializeUI: Added resetButton");

        JButton startGameButton = new JButton("Start Game");
        startGameButton.addActionListener(e -> startGame());
        buttonPanel.add(startGameButton);
        System.out.println("initializeUI: Added startGameButton");

        JButton rulesButton = new JButton("Show Rules");
        rulesButton.addActionListener(e -> showRulesWindow());
        buttonPanel.add(rulesButton);
        System.out.println("initializeUI: Added rulesButton");

        JButton confirmLayoutButton = new JButton("Confirm Layout");
        confirmLayoutButton.addActionListener(e -> confirmLayout());
        buttonPanel.add(confirmLayoutButton);
        System.out.println("initializeUI: Added confirmLayoutButton for player two");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);

        // Check if statusBar is initialized
        if (statusBar == null) {
            statusBar = new JLabel("Welcome to Stratego!");
        }
        bottomPanel.add(statusBar, BorderLayout.SOUTH);
        System.out.println("initializeUI: Created bottomPanel and added components");

        // Log and check for null components before adding to the container
        if (buttonPanel == null) {
            System.err.println("initializeUI: buttonPanel is null");
        }
        if (statusBar == null) {
            System.err.println("initializeUI: statusBar is null");
        }
        if (bottomPanel == null) {
            System.err.println("initializeUI: bottomPanel is null");
        }

        if (buttonPanel != null && statusBar != null && bottomPanel != null) {
            add(bottomPanel, BorderLayout.SOUTH);
            System.out.println("initializeUI: Added bottomPanel to the container");
        } else {
            System.err.println("initializeUI: One or more components are null, cannot add to the container");
        }

        System.out.println("initializeUI: Finished initialization");
    }

    private void confirmLayout() {
        // Logic to confirm player two layout and start the game
        gameStarted = true;
        for (JLabel countLabel : countLabels.values()) {
            countLabel.setVisible(false);
        }
        statusBar.setText("Game started. Player 1's turn.");
        isPlayerOneTurn = true; // Ensure Player 1 starts the game
    }

    private void startGame() {
        gameStarted = true;
        for (JLabel countLabel : countLabels.values()) {
            countLabel.setVisible(false);
        }
        statusBar.setText("Game started. Player 1's turn.");
    }

    private void showResetOptions() {
        if (gameStarted) {
            statusBar.setText("Cannot reset layout after game has started.");
            return;
        }

        String[] options = {"Default Layout 1", "Default Layout 2", "Default Layout 3", "Custom Placement"};
        int option = JOptionPane.showOptionDialog(this, "Choose reset option:", "Reset",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                options, options[0]);

        if (option >= 0 && option < defaultLayouts.length) {
            defaultLayout = defaultLayouts[option];
            resetBoard();
            updateCountersToZero();
            statusBar.setText("Board reset to " + options[option]);
        } else if (option == defaultLayouts.length) {
            clearBoard();
            statusBar.setText("Board cleared for custom placement.");
        }
    }

    private void initializeDefaultLayouts() {
        defaultLayouts = new DefaultLayout[3];
        defaultLayouts[0] = new DefaultLayout(this, "src/layouts/default_layout1.txt");
        defaultLayouts[1] = new DefaultLayout(this, "src/layouts/default_layout2.txt");
        defaultLayouts[2] = new DefaultLayout(this, "src/layouts/default_layout3.txt");
    }

    private void resetBoard() {
        clearBoard();
        defaultLayout.initializeLayout();
    }

    private void clearBoard() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                tiles[row][col].setFigure(null);
            }
        }
        initializeRanksPanel(); // Reset the counts
    }

    private void showRulesWindow() {
        RulesWindow rulesWindow = new RulesWindow();
        rulesWindow.setVisible(true);
    }

    public StrategoTile[][] getTiles() {
        return tiles;
    }

    @Override
    public Piece getPieceAtTile(StrategoTile tile) {
        return tile.getPiece();
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
        if (row >= 1 && row <= GRID_SIZE && col >= 1 && col <= GRID_SIZE) {
            if (icon != null) {
                String description = row + "_" + col;
                icon.setDescription(description); // Set the description to store the rank and type
                tiles[row - 1][col - 1].setFigure(scaleImage(icon, BOARD_IMAGE_SIZE, BOARD_IMAGE_SIZE));
            } else {
                System.err.println("Failed to place piece at (" + row + ", " + col + "): image icon is null");
            }
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

    private ImageIcon scaleImage(ImageIcon icon, int width, int height) {
        if (icon == null) {
            System.err.println("Image icon is null");
            return null;
        }
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private ImageIcon scaleImage(String imagePath, int width, int height) {
        try {
            if (imagePath == null) {
                System.err.println("Image path is null");
                return null;
            }
            java.net.URL resource = getClass().getResource(imagePath);
            if (resource == null) {
                System.err.println("Resource not found: " + imagePath);
                return null;
            }
            BufferedImage image = ImageIO.read(resource);
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

    private void updateCountersToZero() {
        for (String path : countLabels.keySet()) {
            figureCounts.put(path, 0);
            countLabels.get(path).setText("0");
        }
    }

    private class TileClickListener extends MouseAdapter {
        private final int row;
        private final int col;

        public TileClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (!gameStarted) {
                if (selectedFigure != null) {
                    placePiece(row, col, selectedFigure);
                    selectedFigure = null;
                }
            } else {
                movementHandler.handleTileClick(tiles[row - 1][col - 1]);
            }
        }
    }

    private class RankClickListener extends MouseAdapter {
        private final String imagePath;

        public RankClickListener(String imagePath) {
            this.imagePath = imagePath;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (!gameStarted) {
                selectedFigure = new ImageIcon(getClass().getResource(imagePath));
                selectedFigure.setDescription(imagePath); // Store the path in the description for easy reference
            }
        }
    }

    private void updateCountLabel(String imagePath) {
        Integer count = figureCounts.get(imagePath);
        if (count != null) {
            countLabels.get(imagePath).setText(String.valueOf(count));
        }
    }

    public void updateStatusBar(String message) {
        statusBar.setText(message);
    }

    public void switchTurn() {
        isPlayerOneTurn = !isPlayerOneTurn;
        updateStatusBar(isPlayerOneTurn ? "Player 1's turn." : "Player 2's turn.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StrategoGridWithBackground frame = new StrategoGridWithBackground();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(new Dimension(GRID_SIZE * TILE_SIZE + EXTRA_WIDTH, GRID_SIZE * TILE_SIZE + EXTRA_HEIGHT));
            frame.setVisible(true);
        });
    }
}