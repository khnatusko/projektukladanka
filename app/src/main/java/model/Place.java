package model;

public class Place {

    public final int x;

    public final int y;

    public Tile tile;

    public Board board;

    public Place(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
    }

    public Place(int x, int y, int number, Board board) {
        this(x, y, board);
        tile = new Tile(number);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hasTile() {
        return tile != null;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public boolean slidable() {
        return hasTile() && board.slidable(this);
    }

    public boolean slidableLeft() {
        return hasTile() && board.slidableLeft(this);
    }

    public boolean slidableRight() {
        return hasTile() && board.slidableRight(this);
    }

    public boolean slidableUp() {
        return hasTile() && board.slidableUp(this);
    }

    public boolean slidableDown() {
        return hasTile() && board.slidableDown(this);
    }

    public void slide() {
        board.slide(getTile());
    }

}
