import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class RulesWindow extends JFrame {

    public RulesWindow() {
        setTitle("Stratego Rules");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(60, 63, 65));
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setBackground(new Color(43, 43, 43));
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(100, 100, 100);
            }
        });
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        scrollPane.getViewport().setViewPosition(new Point(0, 0)); // Start at the top

        addRuleSection(mainPanel, "1. GAME BOARD AND SETUP", """
                1.1 Game Board        
                - Size: The board is a 10×10 grid, totaling 100 squares.
                - Coordinates: The top-left square is (1,1), and the bottom-right square is (10,10).
                - Lakes: There are two impassable 2×2 lake areas in the center of the board, located at:
                    (5,3), (5,4), (6,3), (6,4)
                    (5,7), (5,8), (6,7), (6,8)
                    Effect: No piece can move onto or through these tiles.
                
                1.2 Starting Zones
                - Blue’s territory: The first four rows (1-4).
                - Red’s territory: The last four rows (7-10).
                - Neutral area: Rows 5 and 6 are initially empty.
                """);

        addRuleSection(mainPanel, "2. PIECES AND RANKS", """
                Each player has 40 pieces, each with a specific rank, strength, movement ability, and special rules.
                The opponent cannot see the identity of a piece until combat occurs.
                
                2.1 Piece List
                Rank                    Count        Strength       Moves Per Turn      Special Ability
                Marshal (★)	1	10	1 tile	None
                General (★★)	1	9	1 tile	None
                Colonel (★★★)	2	8	1 tile	None
                Major (★★★★)	3	7	1 tile	None
                Captain (★★★★★)	4	6	1 tile	None
                Lieutenant (★★★★★★)	4	5	1 tile	None
                Sergeant (★★★★★★★)	4	4	1 tile	None
                Miner (★★★★★★★★)	5	3	1 tile	Can defuse bombs
                Scout (★★★★★★★★★)	8	2	1 tile	None
                Spy (S)	1	1	1 tile	Defeats the Marshal if attacking
                Bomb (B)	6	-	Immovable	Defeats all attacking pieces except Miners
                Flag (F)	1	-	Immovable	Losing this piece means losing the game
                """);

        addRuleSection(mainPanel, "3. GAME RULES", """
                3.1 Movement Rules
                - Most pieces move one tile per turn in any orthogonal direction (up, down, left, or right).
                - Scouts (Rank 2) can move any number of spaces in a straight line, but cannot jump over other pieces or lakes.
                - Bombs and the Flag cannot move.
                - Pieces cannot move diagonally.
                
                3.2 Combat Rules
                - When a piece moves onto a square occupied by an enemy piece, combat occurs:
                    - Both pieces reveal their ranks.
                    - The higher-ranked piece wins, and the lower-ranked piece is removed.
                    - If both pieces have the same rank, both are removed.
                
                Special Combat Cases:
                - The Spy (S) defeats the Marshal (Rank 10) only if it attacks first. If the Marshal attacks the Spy, the Spy is defeated.
                - Bombs (B) defeat all attacking pieces except Miners (Rank 3). Miners defuse bombs instead of being destroyed.
                """);

        addRuleSection(mainPanel, "4. WINNING CONDITIONS", """
                - The game ends when a player captures the opponent's Flag (F).
                - A player also loses if they are unable to make a legal move.
                """);

        add(scrollPane);
    }

    private void addRuleSection(JPanel panel, String title, String content) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BorderLayout());
        sectionPanel.setBackground(new Color(43, 43, 43));
        sectionPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setBackground(new Color(60, 63, 65));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea contentArea = new JTextArea(content);
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setForeground(Color.WHITE);
        contentArea.setBackground(new Color(43, 43, 43));
        contentArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        sectionPanel.add(titleLabel, BorderLayout.NORTH);
        sectionPanel.add(contentArea, BorderLayout.CENTER);

        panel.add(sectionPanel);
    }
}