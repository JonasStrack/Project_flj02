public class GameBoardTest {
    public static void main(String[] args) {
        JFrame frame = new JFrame("GameBoard Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        GameBoard gameBoard = new GameBoard();
        frame.add(gameBoard);

        // Test placing a piece
        Piece piece = new Piece("Flag", "/images/flag.png");
        gameBoard.setPieceAt(0, 0, piece);

        frame.setVisible(true);
    }
}