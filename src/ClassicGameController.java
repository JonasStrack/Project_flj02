import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClassicGameController {
    private final StrategoModel model;
    private final int playerNumber; // 1 = blue, 2 = red
    private final BoardPanel boardPanel;
    private final SidePanel sidePanel;
    private final JLabel statusBar;
    private final JLabel playerLabel;
    private final JPanel buttonPanel;
    private final JPanel mainPanel;
    private String selectedRankKey = null;
    private int selectedRow = -1, selectedCol = -1;
    private final DefaultLayoutLoader layoutLoader;

    // Buttons
    private JButton loadDefaultBtn, startBtn, confirmBtn, rulesBtn, resetBtn;

    public ClassicGameController(StrategoModel model, int playerNumber) {
        this.model = model;
        this.playerNumber = playerNumber;

        boardPanel = new BoardPanel();
        sidePanel = new SidePanel();

        statusBar = new JLabel("Welcome to Stratego! Select a piece and click a square to place.");
        statusBar.setForeground(Color.WHITE);
        statusBar.setFont(new Font("Segoe UI", Font.BOLD, 17));
        statusBar.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));

        String playerLabelText = "You are Player " + playerNumber + (playerNumber == 1 ? " (Blue)" : " (Red)");
        playerLabel = new JLabel(playerLabelText);
        playerLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
        playerLabel.setForeground(playerNumber == 1 ? new Color(70,120,230) : new Color(230,60,60));

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(44, 47, 56));
        statusPanel.add(playerLabel, BorderLayout.WEST);
        statusPanel.add(statusBar, BorderLayout.CENTER);

        JLabel sideNoteLabel = new JLabel("Player 1 (Blue): Bottom Rows (6–9)   |   Player 2 (Red): Top Rows (0–3)");
        sideNoteLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sideNoteLabel.setForeground(new Color(200, 220, 255));
        sideNoteLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 12, 8));
        statusPanel.add(sideNoteLabel, BorderLayout.SOUTH);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(44, 47, 56));
        setupButtons();
        setupPiecePlacement();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(sidePanel, BorderLayout.EAST);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.NORTH);
        southPanel.add(statusPanel, BorderLayout.SOUTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        layoutLoader = new DefaultLayoutLoader(model, sidePanel, playerNumber);

        model.addChangeListener(this::updateBoardView);

        updateBoardView();
    }

    private void setupPiecePlacement() {
        sidePanel.setRankListener(rankKey -> {
            selectedRankKey = rankKey;
            statusBar.setText("Selected: " + getDisplayName(rankKey));
        });

        for (int row = 0; row < BoardPanel.GRID_SIZE; row++) {
            for (int col = 0; col < BoardPanel.GRID_SIZE; col++) {
                final int r = row, c = col;
                JLabel cell = boardPanel.getCell(r, c);

                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent evt) {
                        int turn = model.getCurrentPlayer();
                        boolean isSetup = !model.isGameStarted();

                        if (isSetup) {
                            if (model.isConfirmed(playerNumber)) {
                                statusBar.setText("You've confirmed your layout. Wait for the other player.");
                                return;
                            }
                            if (turn != playerNumber && !model.isConfirmed(turn)) {
                                statusBar.setText("Not your placement phase. Please wait for the other player.");
                                return;
                            }
                            if (selectedRankKey == null) {
                                statusBar.setText("Select a piece from the right first!");
                                return;
                            }
                            if (sidePanel.getCount(selectedRankKey) == 0) {
                                statusBar.setText("No more of that piece available.");
                                return;
                            }
                            // Only allow placing in player's zone
                            if ((playerNumber == 1 && r < 6) || (playerNumber == 2 && r > 3)) {
                                statusBar.setText("Place your pieces only in your starting rows!");
                                return;
                            }
                            if (model.placePiece(r, c, selectedRankKey, playerNumber)) {
                                sidePanel.decrementCount(selectedRankKey);
                                statusBar.setText("Placed " + selectedRankKey + " at " + r + "," + c);
                            } else {
                                statusBar.setText("Invalid placement.");
                            }
                        } else if (model.isGameOver()) {
                            statusBar.setText("Game is over!");
                        } else {
                            // After game start, movement
                            if (model.getCurrentPlayer() != playerNumber) {
                                statusBar.setText("Wait for your turn!");
                                return;
                            }
                            String piece = model.getPiece(r, c);
                            if (selectedRow == -1 && selectedCol == -1) {
                                if (piece == null || !piece.endsWith("" + playerNumber)) {
                                    statusBar.setText("Select your own piece to move.");
                                    return;
                                }
                                selectedRow = r;
                                selectedCol = c;
                                boardPanel.getCell(r, c).setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
                                statusBar.setText("Selected: " + getDisplayName(piece) + ". Click destination.");
                            } else {
                                boardPanel.getCell(selectedRow, selectedCol).setBorder(null);
                                if (selectedRow == r && selectedCol == c) {
                                    statusBar.setText("Deselected.");
                                    selectedRow = selectedCol = -1;
                                    return;
                                }
                                if (model.movePiece(selectedRow, selectedCol, r, c, playerNumber)) {
                                    if (model.isGameOver()) {
                                        revealAndShowWinner();
                                    } else {
                                        statusBar.setText("Moved.");
                                    }
                                } else {
                                    statusBar.setText("Illegal move.");
                                }
                                selectedRow = selectedCol = -1;
                            }
                        }
                    }
                });
            }
        }
    }

    private void revealAndShowWinner() {
        // Force board to update showing all pieces
        model.setRevealAll(true);
        updateBoardView();
        int winner = model.getWinner();
        String msg = "<html><center><h2>Player " + winner + " (" + (winner==1?"Blue":"Red") + ") wins!</h2>"
                + "<br>Flag captured. All pieces revealed.<br><br></center></html>";
        JOptionPane.showMessageDialog(mainPanel, msg, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    private void setupButtons() {
        resetBtn = new ModernButton("Reset", new Color(27, 156, 252));
        startBtn = new ModernButton("Start Game", new Color(86, 204, 91));
        rulesBtn = new ModernButton("Show Rules", new Color(50, 60, 75));
        confirmBtn = new ModernButton("Confirm Layout", new Color(255, 121, 121));
        loadDefaultBtn = new ModernButton("Load Default Layout", new Color(90, 200, 255));

        resetBtn.addActionListener(e -> {
            model.resetPlayerSide(playerNumber);
            sidePanel.resetCounts();
            selectedRow = selectedCol = -1;
            selectedRankKey = null;
        });

        startBtn.addActionListener(e -> {
            if (!model.canStartGame()) {
                statusBar.setText("Both players must confirm their setup!");
                return;
            }
            model.startGame();
            updateBoardView();
        });

        rulesBtn.addActionListener(e -> {
            new SlimRulesDialog(mainPanel);
        });

        confirmBtn.addActionListener(e -> {
            model.confirmSetup(playerNumber);
            statusBar.setText("Layout confirmed! Pass to the other player.");
            confirmBtn.setEnabled(false);
            resetBtn.setEnabled(false);
        });

        loadDefaultBtn.addActionListener(e -> {
            String[] options = { "Layout 1", "Layout 2", "Layout 3", "Cancel" };
            int choice = JOptionPane.showOptionDialog(mainPanel, "Select default layout to load:",
                    "Load Layout", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);
            if (choice >= 0 && choice < 3) {
                String layoutPath = "C:\\Users\\Joejoe\\Stratego2.0\\src\\layouts\\default_layout" + (choice + 1) + ".txt";
                layoutLoader.loadLayout(layoutPath);
                statusBar.setText("Loaded default layout " + (choice + 1) + ". Adjust pieces if needed.");
            }
        });

        buttonPanel.add(resetBtn);
        buttonPanel.add(startBtn);
        buttonPanel.add(confirmBtn);
        buttonPanel.add(rulesBtn);
        buttonPanel.add(loadDefaultBtn);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void updateBoardView() {
        // Hide setup controls for both windows as soon as the game starts
        if (model.isGameStarted()) {
            hideSetupControls();
        }
        String[][] placed = model.getPlacedPiecesCopy();
        boolean revealAll = model.isRevealAll();

        for (int r = 0; r < BoardPanel.GRID_SIZE; r++) {
            for (int c = 0; c < BoardPanel.GRID_SIZE; c++) {
                String piece = placed[r][c];
                JLabel cell = boardPanel.getCell(r, c);
                if (piece == null) {
                    cell.setIcon(null);
                    cell.setText("");
                } else if (revealAll) {
                    // Only at game end!
                    String baseKey = ImageAssets.getBasePieceKey(piece);
                    int colorPlayer = piece.endsWith("1") ? 1 : 2;
                    cell.setIcon(ImageAssets.getPieceIcon(baseKey, 40, colorPlayer));
                } else if (piece.endsWith("" + playerNumber)) {
                    // Own pieces: show plain icon
                    String baseKey = ImageAssets.getBasePieceKey(piece);
                    cell.setIcon(ImageAssets.getPieceIcon(baseKey, 40));
                } else {
                    // Opponent's piece: always white box
                    cell.setIcon(ImageAssets.getWhiteBoxIcon(40));
                }
                cell.setText("");
            }
        }

        confirmBtn.setEnabled(!model.isConfirmed(playerNumber) && !model.isGameStarted());
        resetBtn.setEnabled(!model.isGameStarted());
        startBtn.setEnabled(model.canStartGame() && !model.isGameStarted());
    }

    private void hideSetupControls() {
        resetBtn.setVisible(false);
        confirmBtn.setVisible(false);
        loadDefaultBtn.setVisible(false);
        // rulesBtn.setVisible(false); // Uncomment if you want to hide the rules button
        sidePanel.hideLiveCounter();
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private String getDisplayName(String key) {
        if (key == null) return "";
        String pure = key;
        if (key.length() > 0 && Character.isDigit(key.charAt(key.length() - 1))) {
            pure = key.substring(0, key.length() - 1);
        }
        for (int i = 0; i < SidePanel.RANK_KEYS.length; i++) {
            if (SidePanel.RANK_KEYS[i].equals(pure)) {
                return SidePanel.RANK_LABELS[i];
            }
        }
        return pure;
    }

    static class ModernButton extends JButton {
        public ModernButton(String text, Color color) {
            super(text);
            setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setBackground(color);
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(8, 22, 8, 22));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorderPainted(false);
        }
    }

    static class SlimRulesDialog extends JDialog {
        public SlimRulesDialog(Component parent) {
            super(SwingUtilities.getWindowAncestor(parent), "Stratego Rules", ModalityType.APPLICATION_MODAL);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            JPanel mainPanel = new JPanel();
            mainPanel.setBackground(new Color(44, 47, 56));
            mainPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(60, 120, 180), 2, true),
                    BorderFactory.createEmptyBorder(24, 36, 24, 36)
            ));
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

            JLabel title = new JLabel("Stratego Rules");
            title.setFont(new Font("Segoe UI", Font.BOLD, 26));
            title.setForeground(new Color(180, 200, 220));
            title.setAlignmentX(Component.CENTER_ALIGNMENT);

            JTextArea rules = new JTextArea();
            rules.setEditable(false);
            rules.setFocusable(true);
            rules.setBackground(new Color(44, 47, 56));
            rules.setForeground(new Color(220, 224, 235));
            rules.setFont(new Font("Segoe UI", Font.PLAIN, 17));
            rules.setLineWrap(true);
            rules.setWrapStyleWord(true);
            rules.setHighlighter(null);
            rules.setText(
                    """
                    1. GAME BOARD AND SETUP

                    • The board is a 10×10 grid (100 squares).
                    • There are two impassable 2×2 lake areas in the center:
                      (5,3), (5,4), (6,3), (6,4)
                      (5,7), (5,8), (6,7), (6,8)
                      No piece can move onto or through these tiles.

                    • Blue’s territory: rows 0-3 (top); Red’s territory: rows 6-9 (bottom).
                    • The middle (rows 4-5) is neutral ground.

                    2. PIECES AND RANKS

                    • Each player has 40 pieces. The opponent cannot see a piece’s type until battle.
                    • Pieces:
                      Marshal (10).........1      General (9).........1
                      Colonel (8)..........2      Major (7)...........3
                      Captain (6)..........4      Lieutenant (5)......4
                      Sergeant (4).........4      Miner (3)...........5
                      Scout (2)............8      Spy (S)............1
                      Bomb (B).............6      Flag (F)...........1

                    3. MOVEMENT

                    • Most pieces move one square orthogonally (no diagonal).
                    • Scouts move any number of empty squares in a straight line.
                    • Bombs and Flags do not move.
                    • Pieces cannot jump over other pieces or lakes.

                    4. COMBAT

                    • When moving onto an enemy, both pieces reveal their rank.
                    • The higher rank wins; the loser is removed.
                    • Equal ranks: both are removed.
                    • Special:
                      - Spy defeats Marshal if attacking.
                      - Miner defeats Bombs. Bombs defeat all other attackers.

                    5. WINNING

                    • The game ends when a player captures the opponent’s Flag.
                    • You also lose if you cannot move any piece on your turn.
                    """
            );
            JScrollPane scroll = new JScrollPane(rules,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.setAlignmentX(Component.CENTER_ALIGNMENT);
            scroll.setPreferredSize(new Dimension(680, 450));
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
            scroll.getVerticalScrollBar().setUnitIncrement(18);

            JButton close = new JButton("Close");
            close.setFont(new Font("Segoe UI", Font.BOLD, 17));
            close.setBackground(new Color(60, 120, 180));
            close.setForeground(Color.WHITE);
            close.setFocusPainted(false);
            close.setBorder(BorderFactory.createEmptyBorder(7, 28, 7, 28));
            close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            close.setAlignmentX(Component.CENTER_ALIGNMENT);
            close.addActionListener(e -> dispose());

            mainPanel.add(title);
            mainPanel.add(Box.createVerticalStrut(10));
            mainPanel.add(scroll);
            mainPanel.add(Box.createVerticalStrut(20));
            mainPanel.add(close);

            add(mainPanel, BorderLayout.CENTER);

            setSize(850, 640);
            setLocationRelativeTo(parent);
            setVisible(true);
        }
    }
}