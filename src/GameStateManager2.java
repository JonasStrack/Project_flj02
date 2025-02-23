public class GameStateManager2 {

    private GameBoard gameBoard;
    private enum GameState { SETUP, PLAY }
    private GameState currentState = GameState.SETUP;

    public GameStateManager2(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void transitionToNextStage() {
        if (currentState == GameState.SETUP) {
            if (validateSetup()) {
                currentState = GameState.PLAY;
                System.out.println("Transitioning to PLAY stage.");
            } else {
                System.out.println("Please place all pieces before proceeding.");
            }
        }
    }

    private boolean validateSetup() {
        // Check if all pieces are placed
        return true; // Placeholder
    }
}