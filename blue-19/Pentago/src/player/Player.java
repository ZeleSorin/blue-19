package player;

import board.Board;
import mark.Mark;

public abstract class Player {

    // variables:

    private String name;
    private Mark mark;
    private int move;
    private int rotation;

    // constructor:

    /**
     * creates a new player object with a name and a mark.
     * @param name is the name of player.
     * @param mark is the mark the player is going to use (Black or White).
     */
    /*@
        requires name != null;
        requires mark != null;
        ensures this.name == name;
        ensures this.mark == mark;
    */
    public Player(String name, Mark mark) {
        this.name = name;
        this.mark = mark;
    }

    // methods:


    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    /**
     * Return the name of a player.
     * @return the name of this player object.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the mark of a player.
     * @return the mark of this player object.
     */
    public Mark getMark() {
        return mark;
    }

    /**
     * lets an object that extends Player, determine a move for the next play
     * @param board is the current board.
     * @return the index of the determined move.
     */
    /*@
        requires board != null;
        ensures board.isField(\result) && board.isEmptyField(\result);
    */
    public abstract int determineMove(Board board);

    /**
     * lets an object that extends Player, determine a rotation for the next play.
     * @param board is the current board.
     * @return a value that contains the quadrant that has to be rotated and the direction.
     */
    /*@
        requires board != null;
        ensures \result >= 0 && \result <= 7;
    */
    public abstract int determineRotation(Board board);

    /**
     * It first calls the method determineMove, and according to that choice,
     * places a marble of this player's color on that spot.
     * @param board the board where the player has to make a move on.
     * @return true if the player wants to leave the game
     */
    /*@
        requires board != null;
    */
    public boolean makeMove(Board board) {
        int move1 = determineMove(board);

        //if determineMove() returns 666, that means that the player typed QUIT in its game,
        //so it can be caught by the client and close the connection correctly
        if (move1 == 666) {
            return true;
        }
        setMove(move1);
        board.setField(move1, getMark());
        return false;
    }


    /**
     * makes a move on a provided board according to the determinedRotation method
     * @param board the current board
     */
    /*@
        requires board != null;
    */
    public boolean makeRotation(Board board) {
        int rotation1 = determineRotation(board);

        if (rotation1 == 666) {
            return true;
        }
        setRotation(rotation1);
        board.rotateBoard(rotation1);
        return false;
    }

}
