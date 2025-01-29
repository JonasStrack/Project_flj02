public class Combat {
    
    public static void resolveCombat(Board board, Piece attacker, Piece defender, int x, int y) {
        attacker.isRevealed = true;
        defender.isRevealed = true;

        // Handle Miner vs Bomb
        if (attacker.rank == 3 && defender.rank == 0) {
            board.board[x][y] = attacker; // Miner defuses Bomb
            board.board[attacker.x][attacker.y] = null;
        } else if (defender.rank == 0) {
            board.board[attacker.x][attacker.y] = null; // Bomb defeats attacker
        } 
        // Handle Spy vs Marshal
        else if (attacker.rank == 2 && defender.rank == 10) {
            board.board[x][y] = attacker; // Spy defeats Marshal if it attacks first
            board.board[attacker.x][attacker.y] = null;
        } else if (attacker.rank == defender.rank) {
            board.board[x][y] = null; // Both pieces are removed if ranks are equal
            board.board[attacker.x][attacker.y] = null;
        } else if (attacker.rank > defender.rank) {
            board.board[x][y] = attacker; // Higher rank wins
            board.board[attacker.x][attacker.y] = null;
        } else {
            board.board[x][y] = defender; // Lower rank loses
            board.board[attacker.x][attacker.y] = null;
        }

        board.refreshBoard();
    }
}