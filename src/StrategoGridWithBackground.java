import javax.swing.*;
import java.awt.*;
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
    private JPanel ranksPanel;
    private ImageIcon selectedFigure = null;
    private Map<String, Integer> figureCounts;
    private Map<String, JLabel> countLabels;

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

        // Load default layout
        defaultLayout = new DefaultLayout(this);
        defaultLayout.initializeLayout();

        // Initialize ranks panel
        initializeRanksPanel();

        // Initialize UI
        initializeUI();
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
            JLabel rankImageLabel = new JLabel(scaleImage(imagePaths[i], RANK_IMAGE_SIZE, RANK_IMAGE_SIZE));
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
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> showResetOptions());
        buttonPanel.add(resetButton);

        JButton rulesButton = new JButton("Show Rules");
        rulesButton.addActionListener(e -> showRulesWindow());
        buttonPanel.add(rulesButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void showResetOptions() {
        int option = JOptionPane.showOptionDialog(this, "Choose reset option:", "Reset",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new String[]{"Default Layout", "Custom Placement"}, "Default Layout");

        if (option == 0) {
            resetBoard();
        } else if (option == 1) {
            clearBoard();
        }
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
        JFrame rulesFrame = new JFrame("Stratego Rules");
        JTextArea rulesTextArea = new JTextArea();
        rulesTextArea.setText(getStrategoRules());
        rulesTextArea.setEditable(false);
        rulesTextArea.setLineWrap(true);
        rulesTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(rulesTextArea);

        rulesFrame.add(scrollPane);
        rulesFrame.setSize(800, 600);
        rulesFrame.setLocationRelativeTo(null);
        rulesFrame.setVisible(true);
    }

    private String getStrategoRules() {
        return """
                1. GAME BOARD AND SETUP
                
                1.1 Game Board        
                - Size: The board is a 10×10 grid, totaling 100 squares.
                - Coordinates: The top-left square is (1,1), and the bottom-right square is (10,10).
                - Lakes: There are two impassable 2×2 lake areas in the center of the board, located at:
                    (5,3), (5,4), (6,3), (6,4)
                    (5,7), (5,8), (6,7), (6,8)
                    Effect: No piece can move onto or through these tiles.
                
                1.2 Starting Zones
                - Blue’s territory: The first four rows (1-4).
                - Red’s territory: The last four rows (7-10).
                - Neutral area: Rows 5 and 6 are initially empty.
                
                2. PIECES AND RANKS
                
                Each player has 40 pieces, each with a specific rank, strength, movement ability, and special rules.
                The opponent cannot see the identity of a piece until combat occurs.
                
                2.1 Piece List
                Rank	Count	Strength	Moves Per Turn	Special Ability
                Marshal (★)	1	10	1 tile	None
                General (★★)	1	9	1 tile	None
                Colonel (★★★)	2	8	1 tile	None
                Major (★★★★)	3	7	1 tile	None
                Captain (★★★★★)	4	6	1 tile	None
                Lieutenant (★★★★★★)	4	5	1 tile	None
                Sergeant (★★★★★★★)	4	4	1 tile	None
                Miner (★★★★★★★★)	5	3	1 tile	Can defuse bombs
                Scout (★★★★★★★★★)	8	2	Any number of tiles in a straight line	None
                Spy (S)	1	1	1 tile	Defeats the Marshal if attacking
                Bomb (B)	6	-	Immovable	Defeats all attacking pieces except Miners
                Flag (F)	1	-	Immovable	Losing this piece means losing the game
                
                3. GAME RULES
                
                3.1 Movement Rules
                - Most pieces move one tile per turn in any orthogonal direction (up, down, left, or right).
                - Scouts (Rank 2) can move any number of spaces in a straight line, but cannot jump over other pieces or lakes.
                - Bombs and the Flag cannot move.
                - Pieces cannot move diagonally.
                
                3.2 Combat Rules
                - When a piece moves onto a square occupied by an enemy piece, combat occurs:
                    - Both pieces reveal their ranks.
                    - The higher-ranked piece wins, and the lower-ranked piece is removed.
                    - If both pieces have the same rank, both are removed.
                
                Special Combat Cases:
                - The Spy (S) defeats the Marshal (Rank 10) only if it attacks first. If the Marshal attacks the Spy, the Spy is defeated.
                - Bombs (B) defeat all attacking pieces except Miners (Rank 3). Miners defuse bombs instead of being destroyed.
                
                4. WINNING CONDITIONS
                
                - The game ends when a player captures the opponent's Flag (F).
                - A player also loses if they are unable to make a legal move.
        """;
    }

    public StrategoTile[][] getTiles() {
        return tiles;
    }

    @Override
    public Piece getPieceAtTile(StrategoTile tile) {
        int row = getTileRow(tile);
        int col = getTileCol(tile);
        // Return the piece at the given tile
        // This implementation is a placeholder; you need to adapt it to your game logic
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
        if (row >= 1 && row <= GRID_SIZE && col >= 1 && col <= GRID_SIZE) {
            if (icon != null) {
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

    private class TileClickListener extends MouseAdapter {
        private final int row;
        private final int col;

        public TileClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (selectedFigure != null) {
                if (figureCounts.get(selectedFigure.getDescription()) > 0) {
                    placePiece(row, col, selectedFigure);
                    figureCounts.put(selectedFigure.getDescription(), figureCounts.get(selectedFigure.getDescription()) - 1);
                    updateCountLabel(selectedFigure.getDescription());
                    selectedFigure = null;
                } else {
                    JOptionPane.showMessageDialog(null, "No more pieces of this type left to place.");
                }
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
            selectedFigure = new ImageIcon(getClass().getResource(imagePath));
            selectedFigure.setDescription(imagePath); // Store the path in the description for easy reference
        }
    }

    private void updateCountLabel(String imagePath) {
        countLabels.get(imagePath).setText(String.valueOf(figureCounts.get(imagePath)));
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