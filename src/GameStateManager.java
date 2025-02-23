public class GameStateManager {
    private GameBoard gameBoard;
    private enum GameState { SETUP, PLAY }
    private GameState currentState = GameState.SETUP;

    public GameStateManager(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void transitionToNextStage() {
        if (currentState == GameState.SETUP) {
            if (validateSetup()) {
                currentState = GameState.PLAY;
                System.out.println("Transitioning to PLAY stage.");
                initializePlayStage();
            } else {
                System.out.println("Please place all pieces before proceeding.");
            }
        }
    }

    private boolean validateSetup() {
        // Check if all tiles in rows 0-3 have pieces
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 10; col++) {
                if (gameBoard.getTiles()[row][col].getComponentCount() == 0) {
                    return false; // Empty tile found
                }
            }
        }
        return true; // All tiles are filled
    }

    private void initializePlayStage() {
        // Add logic for the second stage (gameplay)
        System.out.println("Initializing PLAY stage...");
    }
}