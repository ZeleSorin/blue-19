package test;

import board.Board;
import mark.Mark;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestHasWinnerIsWinner {

    Board board;

    @BeforeEach
    public void setup() {
        board = new Board();
    }

    @Test
    void testHasWinnerIsWinner() {
        Mark w = Mark.WHITE;
        Mark b = Mark.BLACK;

        //setting the top row to white
        board.setField(0, w);
        board.setField(1, w);
        board.setField(2, w);
        board.setField(3, w);

        //check to see that we dont have winner yet
        assertFalse(board.hasWinner());
        assertFalse(board.isWinner(w));
        assertFalse(board.isWinner(b));

        //after we set the 5 field in a row to white we check that
        // the board should have a winner and that the winner should be white.
        board.setField(4, w);
        assertTrue(board.hasWinner());
        assertTrue(board.isWinner(w));
        assertFalse(board.isWinner(b));

        //we do the same again with black
        board.setField(0, b);
        assertFalse(board.hasWinner());
        assertFalse(board.isWinner(w));
        assertFalse(board.isWinner(b));

        board.setField(6, b);
        board.setField(12, b);
        board.setField(18, b);
        board.setField(24, b);
        assertTrue(board.hasWinner());
        assertFalse(board.isWinner(w));
        assertTrue(board.isWinner(b));

        board.setField(0, Mark.EMPTY);
        assertFalse(board.hasWinner());
        assertFalse(board.isWinner(w));
        assertFalse(board.isWinner(b));
    }
}
