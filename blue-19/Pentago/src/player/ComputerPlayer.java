package player;

import board.Board;
import mark.Mark;
import strategy.NaiveStrategy;
import strategy.Strategy;

public class ComputerPlayer extends Player {

    // variables:
    Strategy strategy;

    // constructors:
    /**
     * creates a new computerplayer object with a strategy and a mark.
     *
     * @param strat is the strategy of the computer player
     * @param mark is the mark the computer player is going to use (Black or White).
     */
    public ComputerPlayer(Mark mark, Strategy strat) {
        super(strat.getName(), mark);
        this.strategy = strat;
    }

    /**
     * creates a new computerplayer object with a naive strategy if a strategy is not provided.
     * @param mark the mark of this computer player.
     */
    public ComputerPlayer(Mark mark) {
        super("Naive-" + mark.name(), mark);
        this.strategy = new NaiveStrategy();
    }

    // methods:
    /**
     * determines the move according to this computer players strategy given the current board.
     * @param board is the current board.
     * @return the determined index of a field on the given board.
     */
    @Override
    public int determineMove(Board board) {
        return this.strategy.determineMove(board, this.getMark());
    }

    @Override
    public int determineRotation(Board board) {
        return this.strategy.determineRotation(board, this.getMark());
    }
}
