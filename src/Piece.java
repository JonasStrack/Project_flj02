public class Piece {
    public enum Rank {
        MARSHAL(10, true),
        GENERAL(9, true),
        COLONEL(8, true),
        MAJOR(7, true),
        CAPTAIN(6, true),
        LIEUTENANT(5, true),
        SERGEANT(4, true),
        MINER(3, true),
        SCOUT(2, true),
        SPY(1, true),
        BOMB(0, false),
        FLAG(0, false);

        public final int value;
        public final boolean movable;

        Rank(int value, boolean movable) {
            this.value = value;
            this.movable = movable;
        }
    }

    private String owner; // "Red" or "Blue"
    private Rank rank;
    private int row, col;
    private boolean revealedToOpponent = false;

    public Piece(String owner, Rank rank) {
        this.owner = owner;
        this.rank = rank;
    }

    public Rank getRank() { return rank; }
    public int getRankValue() { return rank.value; }
    public boolean isMovable() { return rank.movable; }
    public String getOwner() { return owner; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public boolean isRevealedToOpponent() { return revealedToOpponent; }
    public void reveal() { revealedToOpponent = true; }
    public String getTypeName() { return rank.name(); }
}