public class Combat {

    public static void resolveCombat(GameBoard board, Piece attacker, Piece defender, int x, int y) {
        attacker.setRevealed(true);
        defender.setRevealed(true);

        // Miner vs. Bomb: Miner can defuse the bomb
        if (attacker.getRank() == 3 && defender.getRank() == 0) {
            System.out.println("Miner defuses the bomb!");
            board.setPieceAt(x, y, attacker); // Miner takes the field
            board.removePieceAt(attacker.getX(), attacker.getY()); // Remove miner from the starting field
        }
        // Spy vs. Marshal: Spy defeats Marshal
        else if (attacker.getRank() == 2 && defender.getRank() == 1) {
            System.out.println("Spy defeats Marshal!");
            board.setPieceAt(x, y, attacker);
            board.removePieceAt(attacker.getX(), attacker.getY());
        }
        // Normal combat: Higher rank wins
        else if (attacker.getRank() > defender.getRank()) {
            System.out.println(attacker.getType() + " defeats " + defender.getType());
            board.setPieceAt(x, y, attacker);
            board.removePieceAt(attacker.getX(), attacker.getY());
        }
        // If ranks are equal, both pieces are removed
        else if (attacker.getRank() == defender.getRank()) {
            System.out.println("Draw! Both pieces are removed.");
            board.removePieceAt(attacker.getX(), attacker.getY());
            board.removePieceAt(defender.getX(), defender.getY());
        }
        // Defender wins if attacker has a lower rank
        else {
            System.out.println(defender.getType() + " defeats " + attacker.getType());
            board.setPieceAt(x, y, defender);
            board.removePieceAt(attacker.getX(), attacker.getY());
        }

        board.refreshBoard();
    }
}