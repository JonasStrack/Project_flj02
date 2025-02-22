public class Piece {

    private int rank;
    private String type;
    private int x, y;
    private boolean isRevealed;

    public Piece(int rank, String type, int x, int y) {
        this.rank = rank;
        this.type = type;
        this.x = x;
        this.y = y;
        this.isRevealed = false;
    }

    public int getRank() {
        return rank;
    }

    public String getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public String getImagePath() {
        return "/images/" + type.toLowerCase() + ".png";
    }

    @Override
    public String toString() {
        return "Piece{" +
                "rank=" + rank +
                ", type='" + type + '\'' +
                ", position=(" + x + ", " + y + ")" +
                ", isRevealed=" + isRevealed +
                '}';
    }
}