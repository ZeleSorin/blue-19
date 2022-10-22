package strategy;

import board.Board;
import mark.Mark;

public interface Strategy {
    /**
     * This method return the name of a strategy.
     * @return the name of the strategy.
     */
    public String getName();

    /**
     * This method chooses an index of a field that fits this strategy given a board
     * @param board current board
     * @param mark of the player
     * @return the index of the field of a fitting move.
     */
    /*@
        requires board != null;
        requires mark == Mark.BLACK || mark == Mark.WHITE;
        ensures board.isField(\result);
        ensures board.isEmptyField(\result);
    */
    public int determineMove(Board board, Mark mark);

    /**
     * This method chooses a quadrant to rotate and a direction to rotate it to.
     * @param board current board.
     * @param mark of the player.
     * @return a value that contains what quadrant has to be rotated and what direction.
     */
    /*@
        requires board != null;
        requires mark == Mark.BLACK || mark == Mark.WHITE;
        ensures \result >= 0 && \result <= 7;
    */
    public int determineRotation(Board board, Mark mark);
}
