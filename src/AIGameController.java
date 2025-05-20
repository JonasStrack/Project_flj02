import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIGameController {
    private final StrategoModel model;
    private final BoardPanel boardPanel;
    private final SidePanel sidePanel;
    private final JLabel statusBar;
    private final JLabel playerLabel;
    private final JPanel buttonPanel;
    private final JPanel mainPanel;
    private String selectedRankKey = null;
    private int selectedRow = -1, selectedCol = -1;

    // Buttons
    private JButton loadDefaultBtn, startBtn, confirmBtn, rulesBtn, resetBtn, aiLayoutBtn;

    public AIGameController(StrategoModel model) {
        this.model = model;

        boardPanel = new BoardPanel();
        sidePanel = new SidePanel();

        statusBar = new JLabel("AI Mode: Set up your side, AI will auto-place.");
        statusBar.setForeground(Color.WHITE);
        statusBar.setFont(new Font("Segoe UI", Font.BOLD, 17));
        statusBar.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));

        playerLabel = new JLabel("AI Mode (You vs Smart Opponent)");
        playerLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
        playerLabel.setForeground(new Color(60, 180, 90));

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(44, 47, 56));
        statusPanel.add(playerLabel, BorderLayout.WEST);
        statusPanel.add(statusBar, BorderLayout.CENTER);

        JLabel sideNoteLabel = new JLabel("Player 1 (Blue): Bottom Rows (6–9)   |   AI (Red): Top Rows (0–3)");
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
                        boolean isSetup = !model.isGameStarted();

                        if (isSetup) {
                            if (selectedRankKey == null) {
                                statusBar.setText("Select a piece from the right first!");
                                return;
                            }
                            if (sidePanel.getCount(selectedRankKey) == 0) {
                                statusBar.setText("No more of that piece available.");
                                return;
                            }
                            boolean inBlueZone = r >= 6;
                            if (!inBlueZone) {
                                statusBar.setText("Place pieces only in your (Blue) bottom 4 rows!");
                                return;
                            }
                            if (model.placePiece(r, c, selectedRankKey, 1)) {
                                sidePanel.decrementCount(selectedRankKey);
                                statusBar.setText("Placed " + selectedRankKey + " at " + r + "," + c);
                            } else {
                                statusBar.setText("Invalid placement.");
                            }
                        } else if (model.isGameOver()) {
                            statusBar.setText("Game is over!");
                        } else {
                            int currentPlayer = model.getCurrentPlayer();
                            if (currentPlayer != 1) {
                                statusBar.setText("Wait for the AI to move!");
                                return;
                            }
                            String piece = model.getPiece(r, c);
                            if (selectedRow == -1 && selectedCol == -1) {
                                if (piece == null || !piece.endsWith("1")) {
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
                                if (model.movePiece(selectedRow, selectedCol, r, c, 1)) {
                                    if (model.isGameOver()) {
                                        revealAndShowWinner();
                                    } else {
                                        statusBar.setText("Moved. AI thinking...");
                                        // Give the UI a moment to update, then make the AI move
                                        Timer t = new Timer(500, ev -> performAIMove());
                                        t.setRepeats(false);
                                        t.start();
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

    private void performAIMove() {
        if (model.isGameOver() || model.getCurrentPlayer() != 2) return;
        // Generate and perform an AI move!
        AIHelper.Move aiMove = AIHelper.pickBestMove(model, 2);
        if (aiMove != null) {
            model.movePiece(aiMove.fromRow, aiMove.fromCol, aiMove.toRow, aiMove.toCol, 2);
            statusBar.setText("AI moved.");
            if (model.isGameOver()) {
                revealAndShowWinner();
            }
        } else {
            statusBar.setText("AI cannot move!");
        }
    }

    private void revealAndShowWinner() {
        updateBoardView();
        int winner = model.getWinner();
        String msg = "<html><center><h2>Player " + winner + " (" + (winner==1?"Blue":"Red") + ") wins!</h2>"
                + "<br>Flag captured. All pieces revealed.<br><br></center></html>";
        JOptionPane.showMessageDialog(mainPanel, msg, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    private void setupButtons() {
        resetBtn = new HotseatGameController.ModernButton("Reset", new Color(27, 156, 252));
        startBtn = new HotseatGameController.ModernButton("Start Game", new Color(86, 204, 91));
        rulesBtn = new HotseatGameController.ModernButton("Show Rules", new Color(50, 60, 75));
        confirmBtn = new HotseatGameController.ModernButton("Confirm Layout", new Color(255, 121, 121));
        loadDefaultBtn = new HotseatGameController.ModernButton("Load Default Layout", new Color(90, 200, 255));
        aiLayoutBtn = new HotseatGameController.ModernButton("AI: Auto-Place Red", new Color(120, 70, 200));

        resetBtn.addActionListener(e -> {
            model.resetPlayerSide(1);
            model.resetPlayerSide(2);
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
            boardPanel.enlargeAllPieceIconsSmart(model.getPlacedPiecesCopy(), 50, 50, false);
            // If it's AI's turn, make the first AI move
            if (model.getCurrentPlayer() == 2) {
                Timer t = new Timer(700, ev -> performAIMove());
                t.setRepeats(false);
                t.start();
            }
        });

        rulesBtn.addActionListener(e -> {
            new ClassicGameController.SlimRulesDialog(mainPanel);
        });

        confirmBtn.addActionListener(e -> {
            model.confirmSetup(1);
            model.confirmSetup(2);
            statusBar.setText("Both layouts confirmed! You can start the game.");
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
                // Load blue side for player
                sidePanel.resetCounts();
                new DefaultLayoutLoader(model, sidePanel, 1).loadLayout(layoutPath);
                sidePanel.resetCounts();
                statusBar.setText("Loaded default layout for Player. Now set up AI or use AI button.");
            }
        });

        aiLayoutBtn.addActionListener(e -> {
            // You could randomize, or just use a default layout for AI (player 2)
            String layoutPath = "C:\\Users\\Joejoe\\Stratego2.0\\src\\layouts\\default_layout1.txt";
            sidePanel.resetCounts();
            new DefaultLayoutLoader(model, sidePanel, 2).loadLayout(layoutPath);
            sidePanel.resetCounts();
            statusBar.setText("AI (Red) layout loaded.");
        });

        buttonPanel.add(resetBtn);
        buttonPanel.add(startBtn);
        buttonPanel.add(confirmBtn);
        buttonPanel.add(rulesBtn);
        buttonPanel.add(loadDefaultBtn);
        buttonPanel.add(aiLayoutBtn);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void updateBoardView() {
        String[][] placed = model.getPlacedPiecesCopy();
        boolean revealAll = model.isRevealAll();

        for (int r = 0; r < BoardPanel.GRID_SIZE; r++) {
            for (int c = 0; c < BoardPanel.GRID_SIZE; c++) {
                String piece = placed[r][c];
                JLabel cell = boardPanel.getCell(r, c);
                if (piece == null) {
                    cell.setIcon(null);
                    cell.setText("");
                } else {
                    // ALL pieces visible (hotseat-style)
                    String baseKey = ImageAssets.getBasePieceKey(piece);
                    cell.setIcon(ImageAssets.getPieceIcon(baseKey, 40));
                    cell.setText("");
                }
            }
        }

        confirmBtn.setEnabled(!model.isGameStarted());
        resetBtn.setEnabled(!model.isGameStarted());
        startBtn.setEnabled(model.canStartGame() && !model.isGameStarted());
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
}