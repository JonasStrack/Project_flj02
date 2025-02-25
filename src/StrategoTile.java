import javax.swing.*;
import java.awt.*;

public class StrategoTile extends JPanel {
    private ImageIcon figure;
    private Piece piece;

    public StrategoTile() {
        this.figure = null;
        this.piece = null;
    }

    public void setFigure(ImageIcon figure) {
        this.figure = figure;
        if (figure != null) {
            String description = figure.getDescription();
            if (description != null) {
                String[] parts = description.split("_");
                if (parts.length == 2) {
                    try {
                        int rank = Integer.parseInt(parts[0]);
                        String type = parts[1];
                        this.piece = new Piece(rank, type, -1, -1);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid rank format in image description: " + description);
                        this.piece = null;
                    }
                } else {
                    System.err.println("Invalid image description format: " + description);
                    this.piece = null;
                }
            } else {
                System.err.println("Image description is null");
                this.piece = null;
            }
        } else {
            this.piece = null;
        }
        repaint();
    }

    public ImageIcon getFigure() {
        return figure;
    }

    public Piece getPiece() {
        return piece;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (figure != null) {
            g.drawImage(figure.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }
}