import javax.swing.*;
import java.awt.*;

public class StrategoTile extends JPanel {
    private ImageIcon figure;

    public StrategoTile() {
        super();
        setOpaque(false);
        setBorder(null);
        setPreferredSize(new Dimension(60, 60));
    }

    public void setFigure(ImageIcon figure) {
        this.figure = figure;
        this.removeAll();
        if (figure != null) {
            this.add(new JLabel(figure));
        }
        this.revalidate();
        this.repaint();
    }

    public ImageIcon getFigure() {
        return figure;
    }
}