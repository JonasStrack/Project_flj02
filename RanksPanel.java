import javax.swing.*;
import java.awt.*;

public class RanksPanel extends JPanel {
    public RanksPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));  // VBox equivalent
        setPreferredSize(new Dimension(200, 800));  // Set preferred size

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

        // Loop through the ranks and create JLabel for each
        for (String rank : ranks) {
            JLabel rankLabel = new JLabel(rank);
            rankLabel.setFont(new Font("Arial", Font.PLAIN, 16));  // Set font like in JavaFX
            rankLabel.setAlignmentX(CENTER_ALIGNMENT);  // Center align text
            add(rankLabel);
            add(Box.createVerticalStrut(10));  // Add spacing between components
        }
    }
}
