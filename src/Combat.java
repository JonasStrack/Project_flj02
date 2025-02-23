import javax.swing.ImageIcon;

public class Combat {

    public static void resolveCombat(GameBoardInterface board, Piece attacker, Piece defender, int x, int y) {
        attacker.setRevealed(true);
        defender.setRevealed(true);

        // Miner vs. Bomb: Miner can defuse the bomb
        if (attacker.getRank() == 3 && defender.getRank() == 0) {
            System.out.println("Miner defuses the bomb!");
            board.placePiece(x, y, new ImageIcon(attacker.getImagePath())); // Miner takes the field
            // Remove miner from the starting field
        }
        // Spy vs. Marshal: Spy defeats Marshal
        else if (attacker.getRank() == 2 && defender.getRank() == 1) {
            System.out.println("Spy defeats Marshal!");
            board.placePiece(x, y, new ImageIcon(attacker.getImagePath()));
            // Remove spy from the starting field
        }
        // Normal combat: Higher rank wins
        else if (attacker.getRank() > defender.getRank()) {
            System.out.println(attacker.getType() + " defeats " + defender.getType());
            board.placePiece(x, y, new ImageIcon(attacker.getImagePath()));
            // Remove attacker from the starting field
        }
        // If ranks are equal, both pieces are removed
        else if (attacker.getRank() == defender.getRank()) {
            System.out.println("Draw! Both pieces are removed.");
            // Remove both pieces from the board
        }
        // Defender wins if attacker has a lower rank
        else {
            System.out.println(defender.getType() + " defeats " + attacker.getType());
            board.placePiece(x, y, new ImageIcon(defender.getImagePath()));
            // Remove attacker from the starting field
        }

        // Refresh the board
        ((GameBoard) board).refreshBoard();
    }
}