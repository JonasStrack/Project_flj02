public class Piece {
    String name;
    int rank;
    boolean movable;
    boolean isRevealed;
    boolean isPlayerOne;
    int x, y;

    public Piece(String name, int rank, boolean movable, boolean isPlayerOne, int x, int y) {
        this.name = name;
        this.rank = rank;
        this.movable = movable;
        this.isRevealed = false;
        this.isPlayerOne = isPlayerOne;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        String playerId = isPlayerOne ? "1" : "2";
        return name + " [" + playerId + "]";
    }
}