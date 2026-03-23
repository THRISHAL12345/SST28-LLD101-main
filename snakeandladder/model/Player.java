package model;

public class Player {

    private final String id;
    private int position;
    private boolean winner;

    public Player(String id) {
        this.id = id;
        this.position = 0;
        this.winner = false;
    }

    public String getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }
}