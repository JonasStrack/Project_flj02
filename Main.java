import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Create the board and ranks panel
        Board board = new Board();
        RanksPanel ranksPanel = new RanksPanel();

        // Create the main frame (JFrame in Swing)
        JFrame frame = new JFrame("Stratego Game");

        // Create the layout using BorderLayout
        frame.setLayout(new BorderLayout());
        frame.add(board, BorderLayout.CENTER);
        frame.add(ranksPanel, BorderLayout.EAST);

        // Set the size of the window
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application when the window is closed
        frame.setVisible(true);  // Make the window visible
    }
}
