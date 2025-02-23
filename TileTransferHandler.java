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
            JPanel tile = (JPanel) support.getComponent();
            tile.removeAll();
            tile.add(new JLabel(icon));
            tile.revalidate();
            tile.repaint();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}