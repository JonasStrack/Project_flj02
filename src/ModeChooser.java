import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ModeChooser extends JDialog {
    public enum Mode {
        HOTSEAT, CLASSIC, ONLINE, AI, CANCEL
    }

    private Mode selectedMode = Mode.CANCEL;

    public ModeChooser(JFrame parent) {
        super(parent, "Choose Game Mode", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Modern dark background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Optional: add a subtle background gradient
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(44, 47, 56), getWidth(), getHeight(), new Color(33, 37, 41));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(42, 48, 36, 48));

        JLabel title = new JLabel("Select Game Mode");
        title.setFont(new Font("Segoe UI Black", Font.BOLD, 40));
        title.setForeground(new Color(180, 200, 220));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        JLabel subtitle = new JLabel("Modern Four-Mode Edition");
        subtitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        subtitle.setForeground(new Color(140, 151, 180));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 28, 0));

        mainPanel.add(title);
        mainPanel.add(subtitle);

        Font btnFont = new Font("Segoe UI Semibold", Font.PLAIN, 22);

        JButton hotseatBtn = fancyButton("Two-Player Hotseat (One Window)", new Color(55, 180, 120), new Color(44, 121, 60), btnFont, e -> {
            selectedMode = Mode.HOTSEAT;
            dispose();
        });
        JButton classicBtn = fancyButton("Classic (Two Windows, Hidden Info)", new Color(60, 180, 255), new Color(40, 85, 220), btnFont, e -> {
            selectedMode = Mode.CLASSIC;
            dispose();
        });
        JButton aiBtn = fancyButton("Vs Smart AI (Single Window)", new Color(230, 60, 130), new Color(130, 60, 230), btnFont, e -> {
            selectedMode = Mode.AI;
            dispose();
        });
        JButton onlineBtn = fancyButton("Online Multiplayer (Coming Soon)", new Color(100, 100, 100), new Color(60, 60, 80), btnFont, e -> {
            selectedMode = Mode.ONLINE;
            JOptionPane.showMessageDialog(this, "Online multiplayer coming soon!", "Coming Soon", JOptionPane.INFORMATION_MESSAGE);
        });
        JButton cancelBtn = fancyButton("Cancel", new Color(70, 70, 80), new Color(40, 40, 50), btnFont, e -> {
            selectedMode = Mode.CANCEL;
            dispose();
        });

        mainPanel.add(hotseatBtn);
        mainPanel.add(Box.createVerticalStrut(14));
        mainPanel.add(classicBtn);
        mainPanel.add(Box.createVerticalStrut(14));
        mainPanel.add(aiBtn);
        mainPanel.add(Box.createVerticalStrut(14));
        mainPanel.add(onlineBtn);
        mainPanel.add(Box.createVerticalStrut(22));
        mainPanel.add(cancelBtn);

        JLabel credits = new JLabel("\u00A9 2025 Strack Industries");
        credits.setAlignmentX(Component.CENTER_ALIGNMENT);
        credits.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        credits.setForeground(new Color(110, 100, 130));
        credits.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        mainPanel.add(credits);

        setContentPane(mainPanel);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public Mode showDialog() {
        setVisible(true);
        return selectedMode;
    }

    private JButton fancyButton(String text, Color grad1, Color grad2, Font font, ActionListener action) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, grad1, w, h, grad2);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, w, h, 22, 22);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 36, 12, 36));
        button.setForeground(Color.WHITE);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        button.addActionListener(action);
        return button;
    }
}