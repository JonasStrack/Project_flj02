import javax.swing.*;
import java.awt.*;

public class Stratego {
    public static void main(String[] args) {
        // Create the main game window (JFrame)
        JFrame frame = new JFrame("Stratego Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create the Board and add it to the center of the frame
        Board board = new Board();  // Make sure Board is a JPanel or a Swing component
        frame.add(board, BorderLayout.CENTER);

        // Create the ranks panel on the right side
        JPanel ranksPanel = new JPanel();
        ranksPanel.setLayout(new BoxLayout(ranksPanel, BoxLayout.Y_AXIS));
        ranksPanel.setPreferredSize(new Dimension(200, 800));  // Adjust size of ranks panel

        // List of ranks for the game
        String[] ranks = {
            "Flag (0)",
            "Spy (1)",
            "Scout (2)",
            "Miner (3)",
            "Sergeant (4)",
            "Lieutenant (5)",
            "Captain (6)",
            "Major (7)",
            "Colonel (8)",
            "General (9)",
            "Marshal (10)",
            "Bomb (B)"
        };

        // Add each rank to the ranks panel as a JLabel
        for (String rank : ranks) {
            JLabel rankLabel = new JLabel(rank);
            rankLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Set font for ranks
            rankLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align text
            ranksPanel.add(rankLabel);
        }

        // Add the ranks panel to the frame
        frame.add(ranksPanel, BorderLayout.EAST);

        // Pack the frame to make sure all components are properly sized
        frame.pack();
        
        // Set the window to be visible
        frame.setVisible(true);
    }
}
