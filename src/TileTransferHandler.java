import javax.swing.*;
import java.awt.datatransfer.*;

public class TileTransferHandler extends TransferHandler {

    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDataFlavorSupported(DataFlavor.imageFlavor);
    }

    @Override
    public boolean importData(TransferSupport support) {
        try {
            ImageIcon icon = (ImageIcon) support.getTransferable().getTransferData(DataFlavor.imageFlavor);
            StrategoTile tile = (StrategoTile) support.getComponent();
            tile.setFigure(icon);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}