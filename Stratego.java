import javax.swing.*;
import java.awt.*;

public class Stratego {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Stratego Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        Board board = new Board();
        frame.add(board, BorderLayout.CENTER);

        JPanel ranksPanel = new JPanel();
        ranksPanel.setLayout(new BoxLayout(ranksPanel, BoxLayout.Y_AXIS));
        ranksPanel.setPreferredSize(new Dimension(800, 1000));

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

        for (String rank : ranks) {
            JLabel rankLabel = new JLabel(rank);
            rankLabel.setFont(new Font("Arial", Font.BOLD, 16));
            rankLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            ranksPanel.add(rankLabel);
        }

        frame.add(ranksPanel, BorderLayout.EAST);
        frame.pack();
        frame.setVisible(true);
    }
}