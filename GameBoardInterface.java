import javax.swing.*;

public interface GameBoardInterface {
    void placePiece(int row, int col, ImageIcon icon);
    JPanel[][] getTiles();
}