import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MovementHandler {
    private GameBoardInterface gameBoard;
    private StrategoTile selectedTile;
    private ImageIcon selectedIcon;
    private Piece selectedPiece;

    public MovementHandler(GameBoardInterface gameBoard) {
        this.gameBoard = gameBoard;
        addMouseListeners();
    }

    private void addMouseListeners() {
        for (StrategoTile[] row : gameBoard.getTiles()) {
            for (StrategoTile tile : row) {
                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleTileClick(tile);
                    }
                });
            }
        }
    }

    private void handleTileClick(StrategoTile tile) {
        if (selectedTile == null && tile.getFigure() != null) {
            selectedTile = tile;
            selectedIcon = (ImageIcon) ((JLabel) tile.getComponent(0)).getIcon();
            selectedPiece = gameBoard.getPieceAtTile(selectedTile);  // Get the piece
            return;
        }
        if (selectedTile != null && tile != selectedTile) {
            if (isMoveAllowed(selectedTile, tile)) {
                movePiece(tile);
            } else {
                System.out.println("Invalid move!");
            }
        }
    }

    private boolean isMoveAllowed(StrategoTile startTile, StrategoTile targetTile) {
        int startRow = gameBoard.getTileRow(startTile);
        int startCol = gameBoard.getTileCol(startTile);
        int targetRow = gameBoard.getTileRow(targetTile);
        int targetCol = gameBoard.getTileCol(targetTile);

        // Check movement rules (e.g., a piece can only move to adjacent tiles)
        if (selectedPiece == null) return false;

        // Example: Allow movement to adjacent tiles
        if (Math.abs(startRow - targetRow) <= 1 && Math.abs(startCol - targetCol) <= 1) {
            return true;  // Movement to adjacent tiles allowed
        }
        return false;
    }

    private void movePiece(StrategoTile targetTile) {
        targetTile.removeAll();
        targetTile.setFigure(selectedIcon);
        targetTile.revalidate();
        targetTile.repaint();

        selectedTile.removeAll();
        selectedTile.setFigure(null);
        selectedTile.revalidate();
        selectedTile.repaint();
        selectedTile = null;
        selectedPiece = null;

        // Check if combat occurs
        Piece targetPiece = gameBoard.getPieceAtTile(targetTile);
        if (targetPiece != null) {
            Combat.resolveCombat(gameBoard, selectedPiece, targetPiece, gameBoard.getTileRow(targetTile), gameBoard.getTileCol(targetTile));
        }
    }
}