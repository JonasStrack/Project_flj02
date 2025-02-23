import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class GameBoardTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Stratego Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new GameBoard());
            frame.pack();
            frame.setVisible(true);
        });
    }
}