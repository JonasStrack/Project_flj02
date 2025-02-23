import javax.swing.*;

public class StrategoGame extends JFrame {
    private GameBoard gameBoard;
    private GameStateManager stateManager;

    public StrategoGame() {
        setTitle("Stratego Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        gameBoard = new GameBoard();
        stateManager = new GameStateManager(gameBoard);

        // Add the game board to the frame
        add(gameBoard);

        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StrategoGame game = new StrategoGame();
            game.setVisible(true);
        });
    }
}