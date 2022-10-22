package player;

import board.Board;
import mark.Mark;

public class ServerPlayer extends Player {
    int toMove = -1;
    int toRotate = -1;

    /**
     * creates a new player object with a name and a mark.
     *
     * @param name is the name of player.
     * @param mark is the mark the player is going to use (Black or White).
     */
    public ServerPlayer(String name, Mark mark) {
        super(name, mark);
    }

    public void setMove(int move) {
        this.toMove = move;
    }

    public void setRotation(int rotation) {
        this.toRotate = rotation;
    }

    @Override
    public int determineMove(Board board) {
        return toMove;
    }

    @Override
    public int determineRotation(Board board) {
        return toRotate;
    }
}
