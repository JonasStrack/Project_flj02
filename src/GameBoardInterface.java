import javax.swing.ImageIcon;

public interface GameBoardInterface {
    StrategoTile[][] getTiles();
    Piece getPieceAtTile(StrategoTile tile);
    int getTileRow(StrategoTile tile);
    int getTileCol(StrategoTile tile);
    void placePiece(int row, int col, ImageIcon icon);
    void refreshBoard();
}